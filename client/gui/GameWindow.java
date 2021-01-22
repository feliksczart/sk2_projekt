package gui;

import mainGame.Game;

import javax.swing.*;
import java.awt.*;

public class GameWindow {

    private Game game;
    private JFrame mainFrame, infoFrame;
    private JLabel teamLabel, turnLabel, winnerLabel;
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
        infoFrame.setSize(200, 240);
        infoFrame.setBackground(Color.BLACK);
        infoFrame.setLocationRelativeTo(mainFrame);
        infoFrame.setResizable(false);

        initializeComponents();

        mainFrame.setVisible(true);
        infoFrame.setVisible(true);
    }

    private void initializeComponents() {
        infoNorthPanel = new JPanel();
        infoNorthPanel.setLayout(new BoxLayout(infoNorthPanel, BoxLayout.PAGE_AXIS));
        infoNorthPanel.setBackground(Color.black);
        infoFrame.setLayout(new BorderLayout());
        infoFrame.add(infoNorthPanel, BorderLayout.NORTH);

        teamLabel = new JLabel("Team: " + game.getTeam());
        teamLabel.setFont(new Font("Arial", Font.PLAIN, 30));
        teamLabel.setForeground(Color.white);

        turnLabel = new JLabel("Turn: " + game.getTurn());
        turnLabel.setFont(new Font("Arial", Font.PLAIN, 30));
        turnLabel.setForeground(Color.white);

        winnerLabel = new JLabel("Winner: " + game.getWinner());
        winnerLabel.setFont(new Font("Arial", Font.PLAIN, 30));
        winnerLabel.setForeground(Color.white);

        readyButton = new JButton("Ready");
        readyButton.setFont(new Font("Arial", Font.PLAIN, 30));
        readyButton.setBackground(Color.green);
        readyButton.addActionListener((e) -> {
            JButton button = (JButton) e.getSource();
            sendMessage("ready");
            button.setEnabled(false);
        });
        infoNorthPanel.add(teamLabel);
        infoNorthPanel.add(turnLabel);
        infoNorthPanel.add(winnerLabel);
        infoFrame.add(readyButton, BorderLayout.SOUTH);

        initializeButtons();
    }

    private void initializeButtons() {
        buttons = new JButton[9];
        for(int i = 0; i < buttons.length; i++) {
            buttons[i] = new JButton("");
            buttons[i].setFont(new Font("Arial", Font.PLAIN, 100));
            buttons[i].setName(i + "");
            buttons[i].setBackground(Color.BLACK);
            buttons[i].setForeground(Color.WHITE);
            buttons[i].addActionListener((e) -> {
                game.buttonClicked(e);
            });
            mainFrame.add(buttons[i]);
        }
    }

    public void redraw(){
        updateTeamLabel();
        updateTurnLabel();
        updateButtons();
    }

    private void updateTeamLabel(){
        teamLabel.setText("Team: " + game.getTeam());
    }

    private void updateTurnLabel(){
        turnLabel.setText("Turn: " + game.getTurn());
    }

    private void updateButtons(){
        for (int i=0; i<9; i++){
            if(!game.getBoard()[i].equals("-")) {
                String sign = game.getBoard()[i];
                Color lightBlue = new Color(137,209,254);

                if (sign.equals("X")) buttons[i].setForeground(Color.RED);
                else buttons[i].setForeground(lightBlue);

                buttons[i].setText(sign);
            }
        }
    }

    private void sendMessage(String msg) {
        game.sendMessage(msg);
    }

    public JButton[] getButtons(){
        return buttons;
    }
}
