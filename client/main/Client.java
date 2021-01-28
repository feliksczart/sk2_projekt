package main;

import gui.GameWindow;
import mainGame.Game;
import serverConnection.Messenger;

import java.io.IOException;

public class Client {

    public static void main(String[] args) throws IOException {
        Messenger messenger = new Messenger(1111);
        Game game = new Game(messenger);
        GameWindow gameWindow = new GameWindow(game);
        game.setGameWindow(gameWindow);
        messenger.setGame(game);
        //uruchamiamy wątek, który na bieżąco odczytuje wiadomości z serwera
        messenger.startReadThread();
    }
}