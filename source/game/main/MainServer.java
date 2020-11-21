package game.main;

import game.Client;
import game.Server;

import javax.swing.*;

public class MainServer {

    public static void main(String[] args){
        int choice = Integer.parseInt(JOptionPane.showInputDialog("1 for server | 2 for client"));

        if (choice == 1){
            new Server();
        } else if (choice == 2){
            new Client();
        }
    }
}
