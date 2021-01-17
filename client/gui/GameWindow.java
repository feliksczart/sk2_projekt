package gui;

import main.Game;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.Socket;

public class GameWindow {

    private Game game;
    private JFrame mainFrame, infoFrame;
    private JLabel teamLabel, turnLabel;
    private JButton readyButton;
    private JButton[] buttons;

    private JPanel infoNorthPanel;

    public GameWindow(Game game){
        this.game = game;
        mainFrame = new JFrame("Tic Tac Toe");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setBackground(Color.BLACK);
        mainFrame.setSize(600,600);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setResizable(false);
        mainFrame.setLayout(new GridLayout(3, 3, 2, 2));

        infoFrame = new JFrame("Info");
        infoFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        infoFrame.setBackground(Color.BLACK);
        infoFrame.setSize(200, 170);
        infoFrame.setLocationRelativeTo(mainFrame);
        infoFrame.setResizable(false);

        initializeComponents();

        mainFrame.setVisible(true);
        infoFrame.setVisible(true);
    }

    private void initializeComponents() {
        infoNorthPanel = new JPanel();
        infoNorthPanel.setLayout(new BoxLayout(infoNorthPanel, BoxLayout.PAGE_AXIS));
        infoFrame.setLayout(new BorderLayout());
        infoFrame.add(infoNorthPanel, BorderLayout.NORTH);

        teamLabel = new JLabel("Team: -");
        teamLabel.setFont(new Font("Arial", Font.PLAIN, 30));

        turnLabel = new JLabel("Turn: -");
        turnLabel.setFont(new Font("Arial", Font.PLAIN, 30));

        readyButton = new JButton("Ready");
        readyButton.setFont(new Font("Arial", Font.PLAIN, 30));
        readyButton.addActionListener((e) -> {
            JButton button = (JButton) e.getSource();
            sendMessage("ready");
            button.setEnabled(false);
        });
        infoFrame.add(readyButton, BorderLayout.SOUTH);

        infoNorthPanel.add(teamLabel);
        infoNorthPanel.add(turnLabel);

        initializeButtons();
    }

    private void initializeButtons() {
        buttons = new JButton[9];
        for(int i = 0; i < buttons.length; i++) {
            buttons[i] = new JButton("");
            buttons[i].setFont(new Font("Arial", Font.PLAIN, 40));
            buttons[i].setName(i + "");
            buttons[i].setBackground(Color.BLACK);
            buttons[i].setForeground(Color.WHITE);
            buttons[i].addActionListener((e) -> {
                JButton button = (JButton) e.getSource();
                sendMessage("vote " + button.getName());
            });
            mainFrame.add(buttons[i]);
        }
    }

    private void sendMessage(String msg) {
        game.sendMessage(msg);
    }
}
