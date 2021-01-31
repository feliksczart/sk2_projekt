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
#include "Game.h"
#include "GameManager.h"

std::vector<int> clients;
pollfd* poll_array;
int server_socket_descriptor;
GameManager* game_manager;
std::vector<std::pair<int, std::string>*> messages;
int nc_mode = 1;

void update_poll_array() {
    delete poll_array;
    poll_array = nullptr;
    poll_array = (pollfd *) malloc(sizeof(pollfd) * clients.size());
    int s = clients.size();
    for(int i = 0; i < s; ++i) {
        poll_array[i].fd = clients[i];
        poll_array[i].events = (POLLIN | POLLOUT | POLLRDHUP);
    }
//    printf("created poll_array with %d elements\n", s);
}

void add_connection(int client) {
    clients.push_back(client);
    game_manager->add_player(client);
}

void remove_connection(int client) {
    clients.erase(std::remove(clients.begin(), clients.end(), client), clients.end());
    game_manager->remove_player(client, false);
    shutdown(client, SHUT_RDWR);
    close(client);
}

void add_message(int client, const std::string& msg) {
    auto* p = new std::pair<int, std::string>(client, msg);
    messages.emplace_back(p);
}

void send_message(int client) {
    for(auto p : messages) {
        if(p->first == client) {
            std::string& m = p->second;
            const char* cm = m.c_str();
            write(client, cm, strlen(cm));
            if(nc_mode) write(client, "\n", 1);
            messages.erase(std::remove(messages.begin(), messages.end(), p), messages.end());
            delete p;
        }
    }
}

[[noreturn]] void* poll_thread(void* v) {
    char buf[1024];

    while(true) {
        int ready = poll(poll_array, clients.size(), 0);
        if (ready == -1) {
            error(1, errno, "poll failed");
        }
        for (int i = 0; i < clients.size(); i++) {
            int client = poll_array[i].fd;
            if (poll_array[i].revents & (POLLRDHUP | POLLHUP | POLLERR)) {
                remove_connection(client);
                printf("bye %d\n", client);
            } else if(poll_array[i].revents & (POLLIN)) {
                int r = read(client, buf, 1024);
                buf[strcspn(buf, "\n")] = 0;
                if(strcmp("", buf) != 0) {
                    printf("%d sent %s\n", client, buf);
                    game_manager->execute_command(client, std::string(buf));
                }
                strncpy(buf, "\0", sizeof(buf));
            } else if(poll_array[i].revents & POLLOUT) {
                send_message(client);
            }
        }
        update_poll_array();
    }
}

[[noreturn]] void* accept_thread(void* v) {
    while(true) {
        sockaddr_in clientAddr{};
        socklen_t clientAddrLen = sizeof(clientAddr);
        int client = accept(server_socket_descriptor, (sockaddr*) &clientAddr, &clientAddrLen);

        if(client == -1){
            perror("accept failed");
            continue;
        }
        add_connection(client);
        printf("Connection from %s:%hu\n", inet_ntoa(clientAddr.sin_addr), ntohs(clientAddr.sin_port));
        printf("fd: %d\n", client);
    }
}

int init() {

    int serverPort = 1111;
    int bind_result;
    int listen_result;
    int reuse_addr_val = 1;
    int QUEUE_SIZE = 16;
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

    game_manager = new GameManager;

    srand((unsigned) time(nullptr));

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
