package game.gui;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {

    private static final long serialVersionUID = -3279002675238633610L;

    public Window(String title, int width, int height){
        super(title);
        setResizable(false);
        getContentPane().setPreferredSize(new Dimension(width,height));
        pack();
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
