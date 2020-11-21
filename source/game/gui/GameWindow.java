package game.gui;

import game.Game;
import res.Resources;

import javax.swing.*;
import java.awt.*;

public class GameWindow extends JPanel {

    private static final long serialVersionUID = -5812109375780198918L;

    private Game game;
    public GameWindow(Game game){
        this.game = game;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D g2D = (Graphics2D) g;
        g2D.setStroke(new BasicStroke(10));

        for (int x = Game.FIELD_WIDTH; x <= 2*Game.FIELD_WIDTH; x+= Game.FIELD_WIDTH){
            g2D.drawLine(x,0,x,Game.HEIGHT);
        }
        for (int y = Game.FIELD_HEIGHT; y <= 2*Game.FIELD_HEIGHT; y += Game.FIELD_HEIGHT){
            g2D.drawLine(0,y,Game.WIDTH-300,y);
        }

        for (int x = 0; x < 3; x++){
            for (int y = 0; y < 3; y++) {
                int field = game.getFields()[x][y];
                if (field != Game.FREE) {
                    g2D.drawImage(Resources.symbols[field - 1], x * Game.FIELD_WIDTH, y * Game.FIELD_HEIGHT, Game.FIELD_WIDTH-10, Game.FIELD_HEIGHT-10, null);
                }
            }
        }
    }
}
