package game.gui;

import game.Game;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {

    private static final long serialVersionUID = -3279002675238633610L;
    public Game game;

    public Window(Game game, String title, int width, int height){
        super(title);
        this.game = game;
        setResizable(false);
        getContentPane().setPreferredSize(new Dimension(width,height));
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        addWindowListener(new Listener(this));
    }

}
