package gui;

import main.NewMain;

import javax.swing.*;
import java.awt.*;

public class GameWindow {

    public GameWindow(){
        JFrame window = new JFrame("Tic Tac Toe");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.getContentPane().setBackground(Color.BLACK);
        window.setSize(800,600);
        window.getContentPane().add(new NewMain()); // adds the data
        window.setVisible(true); // show the window
        window.setLocationRelativeTo(null); // center the window
    }
}
