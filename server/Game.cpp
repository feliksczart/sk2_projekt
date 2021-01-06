//
// Created by mister_hz on 2021-01-06.
//

#include "Game.h"

std::vector<int> cross_team;
std::vector<int> circle_team;
char field[9];

int Game::get_player_count() {
    return cross_team.size() + circle_team.size();
}

void Game::add_player(int player) {
    if(is_full()) throw 1;
    if(cross_team.size() > circle_team.size()) circle_team.push_back(player);
    else cross_team.push_back(player);
}

bool Game::is_full() {
    return get_player_count() == 2 * MAX_PLAYERS;
}

const int *Game::get_win_position() {
    for(int i = 0; i < std::size(WIN_POSITIONS); i++) {
        int* p = const_cast<int *>(WIN_POSITIONS[i]);
        if(field[p[0]] == field[p[1]] && field[p[2]] == field[p[1]] && field[p[0]] != 0) {
            return p;
        }
    }
    return NO_WINNER_POSITION;
}

bool Game::someone_won() {
    return get_win_position()[1] != 0;
}

char Game::get_winner() {
    return field[get_win_position()[0]];
}

void Game::place(int position, char c) {
    field[position] = c;
}
