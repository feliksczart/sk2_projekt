package main;

import gui.GameWindow;

import java.io.IOException;

public class Client4 {

    public static void main(String[] args) throws IOException {

        new GameWindow();
        MainMessenger.main(args);
    }

}