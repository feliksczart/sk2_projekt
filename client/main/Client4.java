package main;

import gui.GameWindow;
import mainGame.Game;
import serverConnection.Messenger;

import java.io.IOException;
import java.net.ConnectException;

import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.showMessageDialog;

public class Client4 {

    public static void main(String[] args) throws IOException {
        try{
            Messenger messenger = new Messenger(1111);
            Game game = new Game(messenger);
            GameWindow gameWindow = new GameWindow(game);
            game.setGameWindow(gameWindow);
            messenger.setGame(game);
            messenger.startReadThread();
        } catch (ConnectException ce){
            showMessageDialog(null, "No server running", "", ERROR_MESSAGE);
            System.exit(1);
        }
    }
}