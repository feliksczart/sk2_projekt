package game.main;

import game.Messenger;

import javax.swing.*;
import java.io.IOException;

public class MainMessenger {

    public static void main(String[] args) throws IOException {

        int port = Integer.parseInt(JOptionPane.showInputDialog("Choose port"));
        Messenger messenger = new Messenger(port);
        new Thread(() -> {
            try {
                messenger.read();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                messenger.send();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}