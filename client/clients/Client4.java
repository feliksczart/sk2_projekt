package clients;

import gui.GameWindow;
import mainGame.Game;
import serverConnection.Messenger;

import java.io.IOException;

public class Client4 {

    public static void main(String[] args) throws IOException {
        Messenger messenger = new Messenger(1111);
        messenger.startReadThread();
        Game game = new Game(messenger);
        GameWindow gameWindow = new GameWindow(game);
    }
}