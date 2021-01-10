package main;

import gui.GameWindow;
import gui.InfoWindow;

import java.io.IOException;

public class Client2 {

    public static void main(String[] args) throws IOException {

        MainMessenger.main(args);

        new GameWindow();
        new InfoWindow();
    }

}