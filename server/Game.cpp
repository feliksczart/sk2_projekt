//
// Created by mister_hz on 2021-01-06.
//

#include <algorithm>
#include "Game.h"

std::vector<int> cross_team;
std::vector<int> circle_team;
char field[9];
int turns_made = 0;

int Game::get_player_count() {
    return cross_team.size() + circle_team.size();
}

char Game::add_player(int player) {
    char team = 'o';
    if(is_full()) throw std::runtime_error("kek");
    if(cross_team.size() > circle_team.size()) {
        circle_team.push_back(player);
    } else {
        cross_team.push_back(player);
        team = 'x';
    }
    return team;
}

bool Game::is_full() {
    return get_player_count() == 2 * MAX_PLAYERS_IN_TEAM;
}

const int *Game::get_win_position() {
    for(auto i : WIN_POSITIONS) {
        int* p = const_cast<int *>(i);
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

int Game::place(int position, char c) {
    if(position > 8 || position < 0) {
        return -2;
    }
    if(field[position] == '\0') {
        field[position] = c;
        turns_made++;
    } else if(field[position] != '\0') {
        return 0;
    }
    if(turns_made >= 5 && someone_won()) {
        game_manager->multicast(get_players(), "winner " + std::string(1, get_winner()));
        reset_game();
        return -1;
    }
    return turns_made;
}

bool Game::is_player_here(int player) {
    bool in_cross = std::find(cross_team.begin(), cross_team.end(), player) != cross_team.end();
    bool in_circle = std::find(circle_team.begin(), circle_team.end(), player) != circle_team.end();
    return in_cross || in_circle;
}

void Game::remove_player(int player) {
    bool in_cross = std::find(cross_team.begin(), cross_team.end(), player) != cross_team.end();
    if(in_cross) {
        cross_team.erase(std::remove(cross_team.begin(), cross_team.end(), player), cross_team.end());
        cross_team.shrink_to_fit();
    } else {
        circle_team.erase(std::remove(circle_team.begin(), circle_team.end(), player), circle_team.end());
        circle_team.shrink_to_fit();
    }
}

bool Game::is_empty() {
    int cross = cross_team.size();
    int circle = circle_team.size();
    return (cross + circle) == 0;
}

char Game::get_team(int player) {
    if(!is_player_here(player)) return '-';
    bool in_cross = std::find(cross_team.begin(), cross_team.end(), player) != cross_team.end();
    return in_cross ? 'x' : 'o';
}

void Game::reset_game() {
    for(char & i : field) {
        i = '\0';
    }
    turns_made = 0;
}

int Game::process_vote(int player, int position) {
    char t = get_team(player);
    return place(position, t);
}

Game::Game(GameManager *pManager) {
    game_manager = pManager;
}

std::vector<int> *Game::get_players() {
    auto* pl = new std::vector<int>;
    for(auto p : cross_team) pl->push_back(p);
    for(auto p : circle_team) pl->push_back(p);
    return pl;
}

char Game::get_turn() {
    return turn;
}



