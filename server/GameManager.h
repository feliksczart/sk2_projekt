//
// Created by mister_hz on 2021-01-07.
//

#ifndef SERVER_GAMEMANAGER_H
#define SERVER_GAMEMANAGER_H


#include "Game.h"

class Game;

class GameManager {
private:
    std::vector<Game*> games;
    std::vector<int> players;

    void remove_game(Game* game);
public:
    bool games_exist();
    bool all_games_full();
    Game* get_free_game();
    Game* find_game_by_player(int player);

    void add_player(int player);

    void execute_command(int player, const std::string& command);
    static std::vector<std::string> split(const std::string& str, const std::string& delim);
    void broadcast(const std::string& msg);

    void multicast(std::vector<int> *receivers, const std::string &msg);

    void unicast(int receiver, const std::string &msg);

    bool remove_player(int player, bool team_disconnected);
};


#endif //SERVER_GAMEMANAGER_H
