package main;

import gui.GameWindow;
import gui.InfoWindow;

import java.io.IOException;

public class Client2 {

    public static void main(String[] args) throws IOException {

        new GameWindow();
        MainMessenger.main(args);
//        Game.messenger.addListener(new Game());
        new InfoWindow();
    }

}