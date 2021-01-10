package main;

import game.main.MainMessenger;
import gui.GameWindow;
import gui.InfoWindow;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class Client extends JPanel {

    char playerMark = 'x';
    JButton[] buttons = new JButton[10];

    public Client() {
        setLayout(new GridLayout(3,3));
        initializeButtons();

    }

    // a method used to create 9 buttons
    // set the text, add action listeners
    // and add them to the screen
    public void initializeButtons()
    {
        for(int i = 0; i <= 8; i++)
        {
            buttons[i] = new JButton();
            buttons[i].setPreferredSize(new Dimension(100, 100));
            buttons[i].setText(" ");
            buttons[i].setBackground(Color.BLACK);
            buttons[i].addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    JButton buttonClicked = (JButton) e.getSource(); //get the particular button that was clicked
                    buttonClicked.setFont(new Font("Arial", Font.PLAIN, 100));
                    buttonClicked.setForeground(Color.white);
                    buttonClicked.setText(String.valueOf(playerMark).toUpperCase());


                    if(playerMark == 'x') {
                        playerMark = 'o';
                        buttonClicked.setBackground(Color.BLACK);
                    }
                    else {
                        playerMark ='x';
                        buttonClicked.setBackground(Color.BLACK);
                    }
                    displayVictor();


                }
            });

            add(buttons[i]); //adds this button to JPanel
        }
    }


    // display the victorious player

    public void displayVictor() {

        if(checkForWinner()) {

            // reverse the player marks
            // because after we put x and we win, the game changes it to o
            // but x is the winner
            if(playerMark == 'x') playerMark = 'o';
            else playerMark ='x';

            JOptionPane pane = new JOptionPane();
            int dialogResult = JOptionPane.showConfirmDialog(pane, "Game Over. "+ playerMark + " wins. Would you like to play again?","Game over.",
                    JOptionPane.YES_NO_OPTION);

            if(dialogResult == JOptionPane.YES_OPTION) resetTheButtons();
            else System.exit(0);
        }

        else if(checkDraw()) {
            JOptionPane pane = new JOptionPane();
            int dialogResult = JOptionPane.showConfirmDialog(pane, "Draw. Play again?","Game over.", JOptionPane.YES_NO_OPTION);
            if(dialogResult == JOptionPane.YES_OPTION)  resetTheButtons();
            else System.exit(0);
        }
    }



    // method used to reset the buttons
    // so you can play again
    private void resetTheButtons() {
        playerMark = 'x';
        for(int i =0;i<9;i++) {

            buttons[i].setText(" ");
            buttons[i].setBackground(Color.BLACK);

        }
    }

    // checks for draw

    public boolean checkDraw() {
        boolean full = true;
        for(int i = 0 ; i<9;i++) {
            if(buttons[i].getText().charAt(0) == ' ') {
                full = false;
            }
        }
        return full;
    }

    // checks for a winner
    public boolean checkForWinner() {
        if(checkRows() || checkColumns() || checkDiagonals()) return true;
        else return false;
    }

    // checks rows for a win
    public boolean checkRows() {
        int i = 0;
        for(int j = 0;j<3;j++) {
            if( buttons[i].getText().equals(buttons[i+1].getText()) && buttons[i].getText().equals(buttons[i+2].getText())
                    && buttons[i].getText().charAt(0) != ' ') {
                return true;
            }
            i = i+3;

        }
        return false;
    }


    // checks columns for a win
    public boolean checkColumns() {

        int i = 0;
        for(int j = 0;j<3;j++) {
            if( buttons[i].getText().equals(buttons[i+3].getText()) && buttons[i].getText().equals(buttons[i+6].getText())
                    && buttons[i].getText().charAt(0) != ' ')
            {
                return true;
            }
            i++;
        }
        return false;
    }

    // checks diagonals for a win
    public boolean checkDiagonals() {
        if(buttons[0].getText().equals(buttons[4].getText()) && buttons[0].getText().equals(buttons[8].getText())
                && buttons[0].getText().charAt(0) !=' ')
            return true;
        else if(buttons[2].getText().equals(buttons[4].getText()) && buttons[2].getText().equals(buttons[6].getText())
                && buttons[2].getText().charAt(0) !=' ') return true;

        else return false;
    }



    public static void main(String[] args) throws IOException {

        MainMessenger.main(args);

        new GameWindow();
        new InfoWindow();
    }

}