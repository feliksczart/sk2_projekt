package main;

import gui.GameWindow;
import serverConnection.Messenger;


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

    public void setGameWindow(GameWindow gameWindow) {
        this.gameWindow = gameWindow;
    }

    public String getTurn() {
        return turn;
    }

    public void setTurn(String turn) {
        this.turn = turn;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public void executeCommand(String cmd) {
        System.out.println(cmd);
    }

    public void sendMessage(String msg) {
        messenger.sendMessage(msg);
    }
}