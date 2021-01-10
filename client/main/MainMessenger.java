package main;
import serverConnection.Messenger;

import javax.swing.*;
import java.io.IOException;

public class MainMessenger {

    public static void main(String[] args) throws IOException {

        new Thread(() -> {
            try {
                Game.messenger.read();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                Game.messenger.send();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}