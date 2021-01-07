//
// Created by mister_hz on 2021-01-07.
//

#ifndef SERVER_SERVER_H
#define SERVER_SERVER_H

void update_poll_array();
void add_connection(int client);
void remove_connection(int client);
void add_message(int client, const std::string& msg);
void send_message(int client);
[[noreturn]] void* poll_thread(void* v);
[[noreturn]] void* accept_thread(void* v);
int init();
int main(int argc, char** argv);

#endif //SERVER_SERVER_H
