//
// Created by mister_hz on 2021-01-06.
//

#ifndef SERVER_GAME_H
#define SERVER_GAME_H

#include <vector>
#include "GameManager.h"
#include "GameRunner.h"
#include <string>
#include <thread>

class GameManager;
class GameRunner;

class Game {
private:
    std::vector<int> cross_team;
    std::vector<int> circle_team;

    std::vector<std::pair<int, int>*> votes;
    std::vector<int> players_voted;
    char board[9]{};

    char turn{};
    int turns_made{};
    GameManager* game_manager;
    GameRunner* game_runner;
    bool circle_ready, cross_ready;
    constexpr const static int WIN_POSITIONS[8][3] = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, {0, 4, 8}, {2, 4, 6}};

    constexpr const static int NO_WINNER_POSITION[] = {0, 0, 0};

    const int * get_win_position();
    bool* everyone_voted;

public:
    bool* kill_runner;

    pthread_mutex_t game_mutex = PTHREAD_MUTEX_INITIALIZER;
    std::thread* game_runner_thread;

    const static int MAX_PLAYERS_IN_TEAM = 10;
    const static int ROUND_DURATION_SECONDS = 10;

    int get_player_count();
    bool is_full();
    bool is_empty();
    char add_player(int player);
    bool someone_won();
    char get_winner();
    int place(int position, char c);
    bool has_player(int player);
    char get_team(int player);
    void reset_game();
    int process_vote(int player, int position);
    std::vector<int>* get_players();

    void next_turn();
    bool is_cross_team_empty();
    bool is_circle_team_empty();
    void reconnect_team(char team);
    bool player_voted(int player);
    void broadcast(const std::string& msg);
    int process_poll();
    bool end_of_round();
    void remove_player(int player);
    void set_ready(char team);
    bool both_teams_ready() const;
    void run();
    void reconnect_team(const std::vector<int> *team);
    std::vector<int> *get_free_fields();
    void reconnect_players();

    Game(GameManager *pManager);
    ~Game();

};


#endif //SERVER_GAME_H
