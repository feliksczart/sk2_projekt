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
        window.getContentPane().add(new Game()); // adds the data
        window.setVisible(true); // show the window
        window.setLocationRelativeTo(null); // center the window
    }
}
