package game;

import game.gui.GameWindow;
import game.gui.Window;

public class Game {

    public static final int WIDTH = 900, HEIGHT = 600;
    public static final int FIELD_WIDTH = (WIDTH-300)/3, FIELD_HEIGHT = 600/3;

    public static final int FREE = 0, PLAYER_O = 1, PLAYER_X = 2;
    private int[][] fields;

    private Window window;
    private GameWindow gameWindow;

    public Game(){
        window = new Window("TicTacToe", WIDTH, HEIGHT);
        gameWindow = new GameWindow(this);
        fields = new int[3][3];
        fields[0][0] = PLAYER_O;
        fields[2][0] = PLAYER_X;
        window.add(gameWindow);
    }

    public int[][] getFields() {
        return fields;
    }
}
