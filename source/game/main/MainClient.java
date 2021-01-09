package game.main;

import game.Client;
import game.Server;

import javax.swing.*;
import java.io.IOException;

public class MainClient {

    public static void main(String[] args) throws IOException {
        //int choice = Integer.parseInt(JOptionPane.showInputDialog("1 for server | 2 for client"));

        MainMessenger.main(args);
        new Client();
    }
}
