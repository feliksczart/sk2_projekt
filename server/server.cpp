#include <thread>
#include <netdb.h>
#include <fcntl.h>
#include <unistd.h>
#include <cstdio>
#include <cerrno>
#include <cstring>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <vector>
#include <pthread.h>
#include <csignal>
#include <algorithm>
#include <poll.h>
#include <error.h>

int new_client = 0;
std::vector<int> descriptors;
pollfd* poll_array;
int server_socket_descriptor;

void remove_connection(int fd) {
    descriptors.erase(std::remove(descriptors.begin(), descriptors.end(), fd), descriptors.end());
    shutdown(fd, SHUT_RDWR);
    close(fd);
}

void update_poll_array() {
    delete poll_array;
    poll_array = (pollfd*) malloc(sizeof(pollfd) * descriptors.size());
    int s = descriptors.size();
    for(int i = 0; i < s; ++i) {
        poll_array[i].fd = descriptors[i];
        poll_array[i].events = (POLLIN | POLLOUT | POLLRDHUP | POLLHUP);
    }
    printf("created poll_array with %d elements\n", s);
}

[[noreturn]] void* poll_thread(void* v) {
    char buf[1024];

    while(true) {
        int ready = poll(poll_array, descriptors.size(), 250);
        if(ready == -1) {
            error(1, errno, "poll failed");
            continue;
        } else if(ready == 0) {
            continue;
        }
        for(int i = 0; i < descriptors.size(); i++) {
            int fd = poll_array[i].fd;
            if(poll_array[i].revents & (POLLRDHUP | POLLHUP | POLLERR)) {
                remove_connection(fd);
                update_poll_array();
                printf("bye %d\n", fd);
            } else if(poll_array[i].revents & (POLLIN)) {
                int r = read(fd, buf, 1024);
                printf("%s", buf);
                strncpy(buf, "\0", sizeof(buf));
            }
        }
    }
}

[[noreturn]] void* accept_thread(void* v) {
    while(true) {
        sockaddr_in clientAddr{};
        socklen_t clientAddrLen = sizeof(clientAddr);
        int clientFd = accept(server_socket_descriptor, (sockaddr*) &clientAddr, &clientAddrLen);

        if(clientFd == -1){
            perror("accept failed");
            continue;
        }
        descriptors.push_back(clientFd);
        update_poll_array();
        printf("Connection from %s:%hu\n", inet_ntoa(clientAddr.sin_addr), ntohs(clientAddr.sin_port));
    }
}

int init() {
    int serverPort = 1111;
    int bind_result;
    int listen_result;
    int read_result;
    int reuse_addr_val = 1;
    int QUEUE_SIZE = 16;
    char buf[5];
    struct sockaddr_in server_address{};

    signal(SIGPIPE, SIG_IGN);

    server_address.sin_family = AF_INET;
    server_address.sin_addr.s_addr = htonl(INADDR_ANY);
    server_address.sin_port = htons(serverPort);

    //create socket
    server_socket_descriptor = socket(AF_INET, SOCK_STREAM, 0);
    if (server_socket_descriptor < 0)
    {
        fprintf(stderr, "Error while creating a socket...\n");
        exit(1);
    }
    setsockopt(server_socket_descriptor, SOL_SOCKET, SO_REUSEADDR, (char*) &reuse_addr_val, sizeof(reuse_addr_val));

    //bind socket
    bind_result = bind(server_socket_descriptor, (struct sockaddr*) &server_address, sizeof(struct sockaddr));
    if (bind_result < 0)
    {
        perror("Error while assigning ip address and port for socket...");
        exit(1);
    }

    //set mode to listen
    listen_result = listen(server_socket_descriptor, QUEUE_SIZE);
    if (listen_result < 0) {
        perror("Error while setting up queue size...");
        exit(1);
    }

    poll_array = (pollfd*) malloc(sizeof(pollfd));

    return server_socket_descriptor;
}

int main(int argc, char** argv) {

	server_socket_descriptor = init();

	pthread_t accept_t;
	pthread_create(&accept_t, nullptr, accept_thread, nullptr);

	pthread_t poll_t;
    pthread_create(&poll_t, nullptr, poll_thread, nullptr);

	pthread_join(accept_t, nullptr);
	pthread_join(poll_t, nullptr);

    // accept zwraca deskryptor pliku nawiązanego połączenia (czekając na to połączenie, jeśli żadnego nie ma w kolejce)


//    while (1)
//    {
//        read_result = read(clientFd, buf, sizeof(buf));
//        if(read_result == 0) {
//            printf("END OF FILE w buferku");
//            break;
//        }
//
//        if(read_result > 0){
//            printf("read_result:%s\n", buf);
//        }
//        if(read_result == -1){
//            perror("read failed");
//            close(clientFd);
//            return 1;
//        }
//
//    }
//
//
        return 0;
    }
