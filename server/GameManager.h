//
// Created by mister_hz on 2021-01-07.
//

#ifndef SERVER_GAMEMANAGER_H
#define SERVER_GAMEMANAGER_H


#include "Game.h"

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
    void remove_player(int player);

    void execute_command(int player, const std::string& command);
    static std::vector<std::string> split(const std::string& str, const std::string& delim);

    void multicast(std::vector<int>& receivers, const std::string& msg);
    void broadcast(const std::string& msg);
};


#endif //SERVER_GAMEMANAGER_H
