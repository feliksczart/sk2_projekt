//
// Created by mister_hz on 2021-01-09.
//

#include <unistd.h>
#include "GameRunner.h"

void GameRunner::run(Game* game, bool* stop, bool* everyone_voted) {
    pthread_mutex_t* mutex = &(game->game_mutex);

    pthread_mutex_lock(mutex);
    if(*stop) {
        return;
    }
//    game->reset_game();
    pthread_mutex_unlock(mutex);

    unsigned long time_started;
    unsigned long current_time;
    bool end_of_round = game->end_of_round();
    while(!end_of_round) {
        time_started = std::chrono::duration_cast<std::chrono::milliseconds>(
                std::chrono::system_clock::now().time_since_epoch()).count();
        current_time = std::chrono::duration_cast<std::chrono::milliseconds>(
                std::chrono::system_clock::now().time_since_epoch()).count();
        while(((current_time - time_started) < Game::ROUND_DURATION_SECONDS * 1000) && !(*everyone_voted)) {
            if(*stop) {
                return;
            }
            current_time = std::chrono::duration_cast<std::chrono::milliseconds>(
                    std::chrono::system_clock::now().time_since_epoch()).count();
        }
        pthread_mutex_lock(mutex);
        *everyone_voted = false;

        if(*stop) {
            return;
        }
        int result = game->process_poll();
        end_of_round = game->end_of_round();
        if(result > 0) {
            game->next_turn();
        } else if(result == 0) {
            game->reset_game();
        }
        pthread_mutex_unlock(mutex);
    }
    game->reconnect_players();
}
