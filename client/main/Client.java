package main;

import gui.GameWindow;
import mainGame.Game;
import serverConnection.Messenger;

import javax.swing.*;
import java.io.IOException;
import java.net.ConnectException;

import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.showMessageDialog;

public class Client {

    public static void main(String[] args) throws IOException {
        try{
            Messenger messenger = new Messenger(1111);
            Game game = new Game(messenger);
            GameWindow gameWindow = new GameWindow(game);
            game.setGameWindow(gameWindow);
            messenger.setGame(game);
            //uruchamiamy wątek, który na bieżąco odczytuje wiadomości z serwera
            messenger.startReadThread();
        } catch (ConnectException ce){
            showMessageDialog(null, "No server running", "", ERROR_MESSAGE);
            System.exit(1);
        }
    }
}