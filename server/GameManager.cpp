//
// Created by mister_hz on 2021-01-07.
//

#include <algorithm>
#include <iostream>
#include "GameManager.h"
#include "server.h"

void GameManager::add_player(int player) {
    Game* game_to_join;
    if(all_games_full()) {
        game_to_join = new Game(this);
        games.push_back(game_to_join);
    } else {
        game_to_join = get_free_game();
    }
    game_to_join->add_player(player);
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

bool GameManager::remove_player(int player, bool team_disconnected) {
    Game* g = find_game_by_player(player);
    if(g == nullptr) {
        return false;
    }

    g->remove_player(player);
    players.erase(std::remove(players.begin(), players.end(), player), players.end());
    bool circle_team_empty = g->is_circle_team_empty();
    bool cross_team_empty = g->is_cross_team_empty();

    if((circle_team_empty || cross_team_empty) && !team_disconnected) {
        if(circle_team_empty) {
            g->reconnect_team('x');
        } else {
            g->reconnect_team('o');
        }

    }
    if(g->is_empty()) remove_game(g);
    return true;
}

Game *GameManager::find_game_by_player(int player) {
    for(Game* g : games) {
        if(g->has_player(player)) return g;
    }
    return nullptr;
}

void GameManager::remove_game(Game* game) {
    games.erase(std::remove(games.begin(), games.end(), game), games.end());

    pthread_mutex_t* game_mutex = &(game->game_mutex);
    bool* kill_runner = game->kill_runner;
    *kill_runner = true;
    pthread_mutex_lock(game_mutex);
    delete game;
    game = nullptr;
    pthread_mutex_unlock(game_mutex);
}

void GameManager::execute_command(int player, const std::string& command) {
    auto command_vector = split(command, " ");
    const std::string& cmd = command_vector.at(0);
    if(cmd == "vote") {
        int position = std::stoi(command_vector.at(1));
        Game* game = find_game_by_player(player);
        int result = game->process_vote(player, position);
        if(result < 0) {
            unicast(player, "illegal_vote");
        }
    } else if(cmd == "ready") {
        Game* game = find_game_by_player(player);
        if(game->both_teams_ready()) return;
        char team = game->get_team(player);
        game->set_ready(team);
        game->broadcast("ready " + std::string(1, team));
        if(game->both_teams_ready()) {
            std::cout << "ready" << std::endl;
            game->run();
        }
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

void GameManager::unicast(int receiver, const std::string& msg) {
    add_message(receiver, msg);
}