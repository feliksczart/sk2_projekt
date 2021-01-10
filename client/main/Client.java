package main;

import gui.GameWindow;
import gui.InfoWindow;

import java.io.IOException;

public class Client {

    public static void main(String[] args) throws IOException {

        new GameWindow();
        MainMessenger.main(args);
        new InfoWindow();
    }

}