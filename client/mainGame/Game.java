package mainGame;

import gui.GameWindow;
import serverConnection.Messenger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.concurrent.TimeUnit;


public class Game {
    private String turn;
    private String team;
    private GameWindow gameWindow;
    private Messenger messenger;
    private String[] board;
    private String winner = "-";

    public Game(Messenger messenger) {
        this.messenger = messenger;
        this.board = new String[9];

        for (int i = 0; i < 9; i++) {
            board[i] = "-";
        }
    }

    public void executeCommand(String cmd) {
        String[] args = cmd.split(" ");

        if (args[0].equals("joined")){
            setTeam(args[1].toUpperCase());
            gameWindow.resetButtons();
            gameWindow.resetGame();
        }
        else if (args[0].equals("turn")) setTurn(args[1].toUpperCase());
        else if (args[0].equals("placed")) setPlacedSymbol(Integer.parseInt(args[1]), args[2].toUpperCase());
        else if (args[0].equals("winner")) {
            if (args[1].equals("-")) setWinner("Draw");
            else setWinner(args[1].toUpperCase());
        }
        gameWindow.redraw();
    }

    public void buttonClicked(ActionEvent e) {
        JButton button = (JButton) e.getSource();
        sendMessage("vote " + button.getName());

        if (getTeam() == getTurn()) {
            button.setFont(new Font("Serif",Font.PLAIN,50));
            button.setForeground(Color.yellow);
            button.setText(getTurn());
        }
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

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getTurn() {
        return turn;
    }

    public void setTurn(String turn) {
        this.turn = turn;
    }

    public void setPlacedSymbol(int buttonName, String placedSymbol) {
        board[buttonName] = placedSymbol;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public String getWinner() {
        return winner;
    }

    public void sendMessage(String msg) {
        messenger.sendMessage(msg);
    }

    public String[] getBoard() {
        return board;
    }

    public void waitSecond(int time) {
        try {
            TimeUnit.SECONDS.sleep(time);
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
    }
}