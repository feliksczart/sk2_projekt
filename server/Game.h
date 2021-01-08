//
// Created by mister_hz on 2021-01-06.
//

#ifndef SERVER_GAME_H
#define SERVER_GAME_H

#include <vector>
#include "GameManager.h"

class GameManager;

class Game {
private:
    std::vector<int> cross_team;
    std::vector<int> circle_team;
    char field[9]{};
    char turn{};
    int turns_made{};
    GameManager* game_manager;

    constexpr const static int WIN_POSITIONS[8][3] = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, {0, 4, 8}, {2, 4, 6}};
    constexpr const static int NO_WINNER_POSITION[] = {0, 0, 0};

    const int * get_win_position();

public:

    const static int MAX_PLAYERS_IN_TEAM = 1;

    int get_player_count();
    bool is_full();
    bool is_empty();
    char add_player(int player);
    void remove_player(int player);
    bool someone_won();
    char get_winner();
    int place(int position, char c);
    bool is_player_here(int player);
    char get_team(int player);
    void reset_game();
    int process_vote(int player, int position);
    std::vector<int>* get_players();
    char get_turn();

    Game(GameManager *pManager);
};


#endif //SERVER_GAME_H
