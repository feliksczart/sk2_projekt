package gui;

import main.Client;
import main.Game;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class GameWindow {

    public GameWindow() throws IOException {
        JFrame window = new JFrame("Tic Tac Toe");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.getContentPane().setBackground(Color.BLACK);
        window.setSize(800,600);
        Game game = new Game();
        window.getContentPane().add(game);
        Game.messenger.addListener(game);
        window.setVisible(true);
        window.setLocationRelativeTo(null);
    }
}
