package main;

import serverConnection.Messenger;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class Game extends JPanel {

    char currentPlayer = 'x';
    JButton[] buttons = new JButton[10];
    public int port;
    public static Messenger messenger;

    public Game() throws IOException {
        setLayout(new GridLayout(3, 3));
        initializeButtons();
        this.port = Integer.parseInt(JOptionPane.showInputDialog("Choose port"));
        messenger = new Messenger(this.port);
    }

    public void initializeButtons() {
        for (int i = 0; i <= 8; i++) {
            buttons[i] = new JButton();
            buttons[i].setPreferredSize(new Dimension(100, 100));
            buttons[i].setText(" ");
            buttons[i].setBackground(Color.BLACK);
            buttons[i].setName(String.valueOf(i));
            buttons[i].addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {

                    JButton buttonClicked = (JButton) e.getSource(); //get the particular button that was clicked

                    new Thread(() -> {
                        try {
                            messenger.sendMessage("vote "+ buttonClicked.getName());
                        } catch (IOException ioe) {
                            ioe.printStackTrace();
                        }
                    }).start();

                    if(Messenger.vote){
                        buttonClicked.setFont(new Font("Arial", Font.PLAIN, 100));
                        buttonClicked.setForeground(Color.white);
                        buttonClicked.setText(Messenger.turn);
                    }

                    if (currentPlayer == 'x') {
                        currentPlayer = 'o';
                        buttonClicked.setBackground(Color.BLACK);
                    } else {
                        currentPlayer = 'x';
                        buttonClicked.setBackground(Color.BLACK);
                    }
                    displayVictor();


                }
            });

            add(buttons[i]);
        }
    }

    public void displayVictor() {

        if (checkForWinner()) {

            if (currentPlayer == 'x') currentPlayer = 'o';
            else currentPlayer = 'x';

            JOptionPane pane = new JOptionPane();
            int dialogResult = JOptionPane.showConfirmDialog(pane, "Game Over. " + currentPlayer + " wins. Would you like to play again?", "Game over.",
                    JOptionPane.YES_NO_OPTION);

            if (dialogResult == JOptionPane.YES_OPTION) resetTheButtons();
            else System.exit(0);
        } else if (checkDraw()) {
            JOptionPane pane = new JOptionPane();
            int dialogResult = JOptionPane.showConfirmDialog(pane, "Draw. Play again?", "Game over.", JOptionPane.YES_NO_OPTION);
            if (dialogResult == JOptionPane.YES_OPTION) resetTheButtons();
            else System.exit(0);
        }
    }

    private void resetTheButtons() {
        currentPlayer = 'x';
        for (int i = 0; i < 9; i++) {

            buttons[i].setText(" ");
            buttons[i].setBackground(Color.BLACK);

        }
    }

    public boolean checkDraw() {
        boolean full = true;
        for (int i = 0; i < 9; i++) {
            if (buttons[i].getText().charAt(0) == ' ') {
                full = false;
            }
        }
        return full;
    }

    public boolean checkForWinner() {
        if (checkRows() || checkColumns() || checkDiagonals()) return true;
        else return false;
    }

    public boolean checkRows() {
        int i = 0;
        for (int j = 0; j < 3; j++) {
            if (buttons[i].getText().equals(buttons[i + 1].getText()) && buttons[i].getText().equals(buttons[i + 2].getText())
                    && buttons[i].getText().charAt(0) != ' ') {
                return true;
            }
            i = i + 3;

        }
        return false;
    }

    public boolean checkColumns() {

        int i = 0;
        for (int j = 0; j < 3; j++) {
            if (buttons[i].getText().equals(buttons[i + 3].getText()) && buttons[i].getText().equals(buttons[i + 6].getText())
                    && buttons[i].getText().charAt(0) != ' ') {
                return true;
            }
            i++;
        }
        return false;
    }

    public boolean checkDiagonals() {
        if (buttons[0].getText().equals(buttons[4].getText()) && buttons[0].getText().equals(buttons[8].getText())
                && buttons[0].getText().charAt(0) != ' ')
            return true;
        else if (buttons[2].getText().equals(buttons[4].getText()) && buttons[2].getText().equals(buttons[6].getText())
                && buttons[2].getText().charAt(0) != ' ') return true;

        else return false;
    }
}