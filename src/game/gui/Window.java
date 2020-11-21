package game.gui;

import javax.swing.*;

public class Window extends JFrame {

    private static final long serialVersionUID = -3279002675238633610L;

    public Window(String title, int width, int height){
        super(title);
        setSize(width,height);
        setVisible(true);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
