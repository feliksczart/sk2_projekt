//
// Created by mister_hz on 2021-01-06.
//

#include <algorithm>
#include "Game.h"

std::vector<int> cross_team;
std::vector<int> circle_team;
char field[9];
int turns_made = 0;

std::vector<std::pair<int, int>*> votes;
std::vector<int> players_voted;

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
    game_manager->unicast(player, "joined " + std::string(1, team));
    game_manager->unicast(player, "turn " + std::string(1, turn));
    for(int i = 0; i < 9; i++) {
        char c = field[i];
        if(c != '\0') {
            game_manager->unicast(player, "placed " + std::to_string(i) + " " + std::string(1, c));
        }
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

    int result;

    // >0 - ok
    //  0 - end of game
    // -1 - cell already occupied
    // -2 - illegal move
    // -3 - wrong turn

    if(field[position] == '\0') {
        field[position] = c;
        result = ++turns_made;
    } else {
        throw std::logic_error("field not empty");
    }

    if(turns_made >= 5 && someone_won()) {
        game_manager->multicast(get_players(), "winner " + std::string(1, get_winner()));
        reset_game();
        result = 0;
    }

    if(turns_made == 9 && !someone_won()) {
        game_manager->multicast(get_players(), "winner -");
        reset_game();
        result = 0;
    }
    next_turn();
    return result;
}

bool Game::is_player_here(int player) {
    bool in_cross = std::find(cross_team.begin(), cross_team.end(), player) != cross_team.end();
    bool in_circle = std::find(circle_team.begin(), circle_team.end(), player) != circle_team.end();
    return in_cross || in_circle;
}

void Game::remove_player(int player, bool team_empty_disconnection) {
    bool in_cross = std::find(cross_team.begin(), cross_team.end(), player) != cross_team.end();
    if(in_cross) {
        cross_team.erase(std::remove(cross_team.begin(), cross_team.end(), player), cross_team.end());
        cross_team.shrink_to_fit();
    } else {
        circle_team.erase(std::remove(circle_team.begin(), circle_team.end(), player), circle_team.end());
        circle_team.shrink_to_fit();
    }
    if(team_empty_disconnection) {
        game_manager->unicast(player, "disconnected");
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
    turn = rand() % 2 ? 'x' : 'o';
    std::string m = "turn " + std::string(1, turn);
    game_manager->multicast(&cross_team, m);
    game_manager->multicast(&circle_team, m);
}

int Game::process_vote(int player, int position) {
    char t = get_team(player);
    if(t != turn) {
        return -1;
    } else if(position > 8 || position < 0) {
        return -1;
    } else if(field[position] != '\0') {
        return -1;
    } else if(player_voted(player)){
        return -1;
    } else {
        bool found = false;
        for(auto* p : votes) {
            if(p->first == position) {
                ++(p->second);
                found = true;
            }
        }
        if(!found) {
            auto* p = new std::pair<int, int>;
            p->first = position;
            p->second = 1;
            votes.push_back(p);
        }
        players_voted.push_back(player);
        send_to_all("voted " + std::to_string(player) + " " + std::to_string(position));
        return 0;
    }
}

Game::Game(GameManager *pManager) {
    game_manager = pManager;
    reset_game();
}

std::vector<int> *Game::get_players() {
    auto* pl = new std::vector<int>;
    for(auto p : cross_team) pl->push_back(p);
    for(auto p : circle_team) pl->push_back(p);
    return pl;
}

char Game::get_turn() const {
    return turn;
}

void Game::next_turn() {
    turn = turn == 'x' ? 'o' : 'x';
    std::string m = "turn " + std::string(1, turn);
    send_to_all(m);
}

bool Game::is_circle_team_empty() {
    return circle_team.empty();
}

bool Game::is_cross_team_empty() {
    return cross_team.empty();
}

void Game::reconnect_team(char team) {
    if(team == 'x') {
        reconnect_team(&cross_team);
    } else {
        reconnect_team(&circle_team);
    }
}

void Game::reconnect_team(const std::vector<int>* team) {
    auto* v = new std::vector<int>;
    for(auto p : *team) {
        bool player_present = game_manager->remove_player(p, true);
        if(player_present) {
            v->push_back(p);
        }
    }
    for(auto p : *v) {
        game_manager->add_player(p);
    }
    delete v;
}

bool Game::player_voted(int player) {
    return std::find(players_voted.begin(), players_voted.end(), player) != players_voted.end();
}

void Game::send_to_all(const std::string& msg) {
    game_manager->multicast(&cross_team, msg);
    game_manager->multicast(&circle_team, msg);
}

void Game::process_poll() {

}



