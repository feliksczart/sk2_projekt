package game.main;

import game.Client;

import java.io.IOException;

public class MainClient2 {

    public static void main(String[] args) throws IOException {
        //int choice = Integer.parseInt(JOptionPane.showInputDialog("1 for server | 2 for client"));

        MainMessenger.main(args);
        new Client();
    }
}
