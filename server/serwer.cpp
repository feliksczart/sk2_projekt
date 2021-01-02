#include <thread>
#include <netdb.h>
#include <fcntl.h>
#include <unistd.h>
#include <stdio.h>
#include <errno.h>
#include <cstring>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>

int main(int argc, char ** argv) {
    int serverPort = 1111;
    int server_socket_descriptor;
    int bind_result;
	int listen_result;
    int read_result;
    int reuse_addr_val = 1;
    int QUEUE_SIZE = 2;
    char buf[5];
	struct sockaddr_in server_address;

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
	setsockopt(server_socket_descriptor, SOL_SOCKET, SO_REUSEADDR, (char*)&reuse_addr_val, sizeof(reuse_addr_val));

	//bind socket
	bind_result = bind(server_socket_descriptor, (struct sockaddr*)&server_address, sizeof(struct sockaddr));
	if (bind_result < 0)
	{
		perror("Error while assigning ip addres and port for socket...");
		exit(1);
	}

	//set mode to listen
	listen_result = listen(server_socket_descriptor, QUEUE_SIZE);
	if (listen_result < 0) {
		perror("Error while setting up queue size...");
		exit(1);
	}


    // accept zwraca deskryptor pliku nawiązanego połączenia (czekając na to połączenie, jeśli żadnego nie ma w kolejce)
    sockaddr_in clientAddr;
    socklen_t clientAddrLen = sizeof(clientAddr);
    int clientFd = accept(server_socket_descriptor, (sockaddr*)&clientAddr, &clientAddrLen);
   
    if(clientFd == -1){
        perror("accept failed");
        return 1;
    }
    printf("Connection from %s:%hu\n", inet_ntoa(clientAddr.sin_addr), ntohs(clientAddr.sin_port));

    while (1)
    {
        read_result = read(clientFd, buf, sizeof(buf));
        if(read_result == 0) {
            printf("END OF FILE w buferku");    
            break;
        }

        if(read_result > 0){
            printf("read_result:%s\n", buf);    
        }
        if(read_result == -1){
            perror("read failed");
            close(clientFd);
            return 1;
        }

    }
    

        return 0;
    }
