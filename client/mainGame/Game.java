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
    private String percent = "-";
    private GameWindow gameWindow;
    private Messenger messenger;
    private String[] board;
    private String winner = "-";
    private int placeholdButton;
    private boolean firstVote = false;

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
        else if (args[0].equals("placed")) {
            if (firstVote){
                setPlacedSymbol(Integer.parseInt(args[1]), args[2].toUpperCase());
            }
        }
        else if (args[0].equals("winner")) {
            if (args[1].equals("-")) setWinner("Draw");
            else setWinner(args[1].toUpperCase());
        }
        else if (args[0].equals("voted")) setPercent(args[1]);

        gameWindow.redraw();
    }

    public void buttonClicked(ActionEvent e) {
        JButton button = (JButton) e.getSource();
        sendMessage("vote " + button.getName());

        if (getTeam().equals(getTurn())) {
            button.setFont(new Font("Serif",Font.PLAIN,50));
            button.setForeground(Color.yellow);
            button.setText(getTurn());
            setPlaceholdButton(Integer.parseInt(button.getName()));
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
        gameWindow.getButtons()[getPlaceholdButton()].setText("");
        setPercent("-");
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public String getWinner() {
        return winner;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    public String getPercent() {
        return percent;
    }

    public void setPlaceholdButton(int placeholdButton) {
        this.placeholdButton = placeholdButton;
    }

    public int getPlaceholdButton() {
        return placeholdButton;
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

    public boolean getFirstVote(){return firstVote;}
    public boolean setFirstVote(boolean firstVote){return this.firstVote = firstVote;}
}