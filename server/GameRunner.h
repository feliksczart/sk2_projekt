//
// Created by mister_hz on 2021-01-09.
//

#ifndef SERVER_GAMERUNNER_H
#define SERVER_GAMERUNNER_H


#include "Game.h"

class Game;

class GameRunner {
public:
    static void run(Game*, bool*, bool*);
};


#endif //SERVER_GAMERUNNER_H
