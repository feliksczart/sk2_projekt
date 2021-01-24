//
// Created by mister_hz on 2021-01-06.
//

#include <algorithm>
#include <thread>
#include <iostream>
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

    if(field[position] == '\0') {
        field[position] = c;
        result = ++turns_made;
    } else {
        throw std::logic_error("field not empty");
    }

    if(turns_made >= 5 && someone_won()) {
        game_manager->multicast(get_players(), "winner " + std::string(1, get_winner()));
        send_to_all("placed " + std::to_string(position) + " " + std::string(1, c));
//        reset_game();
        result = 0;
    }

    if(end_of_round() && !someone_won()) {
        game_manager->multicast(get_players(), "winner -"); //FIXME after winner - turn %char% msg is sent 2 times
//        reset_game();
        result = 0;
    }
    if(result > 0) {
//        next_turn();
        send_to_all("placed " + std::to_string(position) + " " + std::string(1, c));
    }
    return result;
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
    turn = rand() % 2 ? 'x' : 'o';
    turns_made = 0;
    *kill_runner = false;
    std::string m = "turn " + std::string(1, turn);
    game_manager->multicast(&cross_team, m);
    game_manager->multicast(&circle_team, m);
    circle_ready = false;
    cross_ready = false;
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
    } else if(!both_teams_ready()){
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

        auto* team_vector = turn == 'x' ? &cross_team : &circle_team;
        if(players_voted.size() == team_vector->size()) {
            *everyone_voted = true;
        }

        return 0;
    }
}

Game::Game(GameManager *pManager) {
    game_manager = pManager;
    kill_runner = new bool;
    everyone_voted = new bool;
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
    if(team->size() == 1) {
        game_manager->unicast(team->at(0), "reset");
    }
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

int Game::process_poll() {
//    sort(votes.begin(), votes.end(), sort_votes);
    int max_vote_count = -1;
    auto* choose_from = new std::vector<int>;

    for(auto* p : votes) {
        int vote_count = p->second;
        int position = p->first;
        if(vote_count > max_vote_count) {
            max_vote_count = vote_count;
            choose_from->clear();
            choose_from->push_back(position);
        } else if(vote_count == max_vote_count) {
            choose_from->push_back(position);
        }
    }
    if(max_vote_count <= 0) { //FIXME either 0 or -1
        delete choose_from;
        choose_from = get_free_fields();
    }
    int size = choose_from->size();
    int num = rand() % size;
    int position = choose_from->at(num);

    int r = place(position, turn);

    players_voted.clear();
    votes.clear();
    delete choose_from;

    return r;
}

bool Game::sort_votes(const std::pair<int,int> &a,
               const std::pair<int,int> &b) {
    return (a.second > b.second);
}

bool Game::end_of_round() {
    return (turns_made == 9) || someone_won();
}

void Game::run() {
    delete game_runner_thread;
    game_runner_thread = new std::thread(GameRunner::run, this, kill_runner, everyone_voted);
    game_runner_thread->detach();
    send_to_all("reset");
    std::cout << "start" << std::endl;
}

std::vector<int> *Game::get_free_fields() {
    auto* f = new std::vector<int>;
    for(int i = 0; i < 9; i++) {
        if(field[i] == '\0') {
            f->push_back(i);
        }
    }
    return f;
}

void Game::ready(char team) {
    if(team == 'o') {
        circle_ready = true;
    } else {
        cross_ready = true;
    }
}

bool Game::both_teams_ready() const {
    return cross_ready && circle_ready;
}

Game::~Game() {
    delete kill_runner;
    delete everyone_voted;
}



