//
// Created by mister_hz on 2021-01-07.
//

#include <algorithm>
#include <iostream>
#include "GameManager.h"
#include "server.h"

bool GameManager::games_exist() {
    return !games.empty();
}

void GameManager::add_player(int player) {
    Game* to_be_joined;
    if(all_games_full()) {
        to_be_joined = new Game(this);
        games.push_back(to_be_joined);
    } else {
        to_be_joined = get_free_game();
    }
    char team = to_be_joined->add_player(player);
    std::string msg = "joined " + std::string(1, team);
    add_message(player, msg);
    players.push_back(player);
}

bool GameManager::all_games_full() {
    for(Game* g : games) {
        if(!g->is_full()) return false;
    }
    return true;
}

Game *GameManager::get_free_game() {
    for(Game* g : games) {
        if(!g->is_full()) return g;
    }
    return nullptr;
}

void GameManager::remove_player(int player) {
    Game* g = find_game_by_player(player);
    if(g == nullptr) {
        return;
    }
    g->remove_player(player);
    if(g->is_empty()) {
        remove_game(g);
    }
    players.erase(std::remove(players.begin(), players.end(), player), players.end());
}

Game *GameManager::find_game_by_player(int player) {
    for(Game* g : games) {
        if(g->is_player_here(player)) return g;
    }
    return nullptr;
}

void GameManager::remove_game(Game* game) {
    games.erase(std::remove(games.begin(), games.end(), game), games.end());
    delete game;
    game = nullptr;
}

void GameManager::execute_command(int player, const std::string& command) {
    auto v = split(command, " ");
    const std::string& cmd = v.at(0);
    if(cmd == "vote") {
        int position = std::stoi(v.at(1));
        Game* game = find_game_by_player(player);
        char c = game->get_team(player);
        int result = game->process_vote(player, position);
        std::string msg = std::to_string(position) + " " + std::string(1, c);
        auto pl = game->get_players();
        if(result > 0) {
            multicast(pl, msg);
        } else if(result < 0) {
            unicast(player, "illegal_move");
        }
        std::cout << msg << "\n";
    }
}

std::vector<std::string> GameManager::split(const std::string& str, const std::string& delim) {
    std::vector<std::string> tokens;
    size_t prev = 0, pos = 0;
    do
    {
        pos = str.find(delim, prev);
        if (pos == std::string::npos) pos = str.length();
        std::string token = str.substr(prev, pos-prev);
        if (!token.empty()) tokens.push_back(token);
        prev = pos + delim.length();
    }
    while (pos < str.length() && prev < str.length());
    return tokens;
}

void GameManager::multicast(std::vector<int>* receivers, const std::string& msg) {
    for(auto r : *receivers) add_message(r, msg);
}

void GameManager::broadcast(const std::string& msg) {
    multicast(&players, msg);
}

void GameManager::unicast(int receiver, const std::string& msg) {
    add_message(receiver, msg);
}