package game;

import game.gui.GameWindow;
import game.gui.Window;

import javax.swing.*;

public abstract class Game {

    public static final int PORT = 55555;

    public static final int WIDTH = 900, HEIGHT = 600;
    public static final int FIELD_WIDTH = (WIDTH-300)/3, FIELD_HEIGHT = 600/3;

    public static final int NOBODY = 0, PLAYER_O = 1, PLAYER_X = 2;
    protected int[][] fields;

    private Window window;
    protected GameWindow gameWindow;

    protected int currentPlayer;
    protected int thisPlayer;

    public Game(int thisPlayer){
        this.thisPlayer = thisPlayer;
        window = new Window(this,"TicTacToe", WIDTH, HEIGHT);
        gameWindow = new GameWindow(this);
        fields = new int[3][3];
        window.add(gameWindow);
        window.setVisible(true);
        //window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        if (Messenger.team == 'X')
            currentPlayer = Game.PLAYER_X;
        else if (Messenger.team == 'O')
            currentPlayer = Game.PLAYER_O;
    }

    protected void showWinner(int winner){
        if (winner == Game.NOBODY){
            JOptionPane.showMessageDialog(null,"TIE!");

        } else {
            JOptionPane.showMessageDialog(null, "The player " + winner + " has won the game!");
        }
    }

    protected boolean isMyTurn(){
        return thisPlayer == currentPlayer;
    }

    public abstract void inputRecieved(int x, int y);

    public abstract void close();

    public abstract void packetRecieved(Object object);

    public int[][] getFields() {
        return fields;
    }
}
