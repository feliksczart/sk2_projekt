package game.gui;

import game.Game;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Input extends MouseAdapter {

    private final GameWindow gameWindow;

    public Input(GameWindow gameWindow) {
        this.gameWindow = gameWindow;
    }

    @Override
    public void mousePressed(MouseEvent e) {

        if (e.getButton() == MouseEvent.BUTTON1) {
            gameWindow.game.inputRecieved(e.getX() / Game.FIELD_WIDTH, e.getY() / Game.FIELD_HEIGHT);
        }
    }
}
