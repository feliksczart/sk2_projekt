//
// Created by mister_hz on 2021-01-09.
//

#ifndef SERVER_GAMERUNNER_H
#define SERVER_GAMERUNNER_H


#include "Game.h"

class Game;

class GameRunner {
private:
    Game* game;
public:
    GameRunner(Game *game);
    void run();
};


#endif //SERVER_GAMERUNNER_H
