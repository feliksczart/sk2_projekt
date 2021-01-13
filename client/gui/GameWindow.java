package gui;

import main.Client;
import main.Game;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class GameWindow {

    public static JFrame window;
    public static Game game;

    public GameWindow() throws IOException {
        window = new JFrame("Tic Tac Toe");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.getContentPane().setBackground(Color.BLACK);
        window.setSize(800,600);
        game = new Game();
        window.getContentPane().add(game);
        Game.messenger.addListener(game);
        window.setVisible(true);
        window.setLocationRelativeTo(null);
    }

    public static void windowReset() throws IOException {
        window.getContentPane().removeAll();
//        window.repaint();
        game = new Game();
        window.getContentPane().add(game);
        window.setSize(801,601);
        //Game.messenger.addListener(game);

    }
}
