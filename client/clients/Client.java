package clients;

import gui.GameWindow;
import mainGame.Game;
import mainGame.GameUpdate;
import serverConnection.Messenger;

import java.io.IOException;

public class Client {

    public static void main(String[] args) throws IOException {
        Messenger messenger = new Messenger(1111);
        messenger.startReadThread();
        Game game = new Game(messenger);
        GameWindow gameWindow = new GameWindow(game);
        GameUpdate gameUpdate = new GameUpdate(game);
        gameUpdate.startUpdateThread();
    }
}