package mainGame;

import gui.GameWindow;
import serverConnection.Messenger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.concurrent.TimeUnit;


public class Game {
    private String turn;
    private String team;
    private GameWindow gameWindow;
    private Messenger messenger;

    public Game(Messenger messenger) {
        this.messenger = messenger;
    }

    public Messenger getMessenger() {
        return messenger;
    }

    public GameWindow getGameWindow() {
        return gameWindow;
    }

    public void setGameWindow(GameWindow gameWindow) {
        this.gameWindow = gameWindow;
    }

    public void executeCommand(String cmd) {
        System.out.println(cmd);
    }

    public void sendMessage(String msg) {
        messenger.sendMessage(msg);
    }

    public void buttonClicked(ActionEvent e){
        JButton button = (JButton) e.getSource();
        sendMessage("vote " + button.getName());

        try {
            TimeUnit.MICROSECONDS.sleep(10);
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }

        //button.setText(messenger.getPlacedSymbol());
    }
}