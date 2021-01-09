package game.gui;

import game.Game;
import res.Resources;

import javax.swing.*;
import java.awt.*;

public class GameWindow extends JPanel {

    private static final long serialVersionUID = -5812109375780198918L;

    public Game game;
    public GameWindow(Game game){
        this.game = game;
        addMouseListener(new Input(this));
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D g2D = (Graphics2D) g;
        g2D.setStroke(new BasicStroke(10));
        
        int actualX = 0;
        for (int x = Game.FIELD_WIDTH; x <= 2*Game.FIELD_WIDTH; x+= Game.FIELD_WIDTH){
            g2D.drawLine(x,0,x,Game.HEIGHT);
            actualX = x;
        }
        actualX += Game.FIELD_WIDTH;
        g2D.drawLine(actualX,0,actualX,Game.HEIGHT);
        for (int y = Game.FIELD_HEIGHT; y <= 2*Game.FIELD_HEIGHT; y += Game.FIELD_HEIGHT){
            g2D.drawLine(0,y,Game.WIDTH-300,y);
        }

        for (int x = 0; x < 3; x++){
            for (int y = 0; y < 3; y++) {
                int field = game.getFields()[x][y];
                if (field != Game.NOBODY) {
                    g2D.drawImage(Resources.symbols[field - 1], x * Game.FIELD_WIDTH, y * Game.FIELD_HEIGHT, Game.FIELD_WIDTH-10, Game.FIELD_HEIGHT-10, null);
                    System.out.println("vote 0");
                }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawString("Informations about the game", Game.WIDTH - 200,30);
    }

}
