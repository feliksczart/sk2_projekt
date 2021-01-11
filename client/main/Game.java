package main;

import gui.InfoWindow;
import serverConnection.Listener;
import serverConnection.Messenger;
import serverConnection.MyListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import static gui.InfoWindow.updateInfo;


public class Game extends JPanel implements MyListener {

    char currentPlayer = 'x';
    JButton[] buttons = new JButton[9];
    List<Integer> placed = new ArrayList<Integer>();

    public int port;
    public static Messenger messenger;
    public static boolean trueIsPlace;
    public static boolean ready = false;
    public static String trueTurn;

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
            if(i==4){
                buttons[i].setFont(new Font("Arial", Font.PLAIN, 40));
                buttons[i].setForeground(Color.BLACK);
                buttons[i].setText("I'm ready");
                buttons[i].setBackground(Color.GREEN);
            } else {
                buttons[i].setText(" ");
                buttons[i].setBackground(Color.BLACK);
            }
            buttons[i].setName(String.valueOf(i));
            buttons[i].addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {

                    JButton buttonClicked = (JButton) e.getSource(); //get the particular button that was clicked
                    trueTurn = Messenger.turn;

                    if (ready) {
                        new Thread(() -> {
                            try {
                                messenger.sendMessage("vote " + buttonClicked.getName());
                            } catch (IOException ioe) {
                                ioe.printStackTrace();
                            }
                        }).start();

                        try {
                            TimeUnit.MICROSECONDS.sleep(10);
                        } catch (InterruptedException interruptedException) {
                            interruptedException.printStackTrace();
                        }

                        trueIsPlace = Messenger.isPlace;

                        if (Messenger.vote && !Arrays.asList(placed).contains(Integer.valueOf(buttonClicked.getName()))) {
                            buttonClicked.setFont(new Font("Arial", Font.PLAIN, 100));
                            buttonClicked.setForeground(Color.white);
                            buttonClicked.setText(trueTurn);
                            placed.add(Integer.valueOf(buttonClicked.getName()));
                            updateInfo(InfoWindow.info);
                        }

                        if (currentPlayer == 'x') {
                            currentPlayer = 'o';
                            buttonClicked.setBackground(Color.BLACK);
                        } else {
                            currentPlayer = 'x';
                            buttonClicked.setBackground(Color.BLACK);
                        }

                        displayVictor();
                    } else {
                        if(buttonClicked.getName().equals("4")) {
                            new Thread(() -> {
                                try {
                                    messenger.sendMessage("ready");
                                } catch (IOException ioe) {
                                    ioe.printStackTrace();
                                }
                            }).start();
                            ready = true;
                            buttons[4].setText(" ");
                            buttons[4].setBackground(Color.BLACK);

                            new InfoWindow();
                        }
                    }
                }
            });

            add(buttons[i]);
        }
    }

    @Override
    public void messageReceived() {
        try {
            List<String> opponentSymbol = opponentSymbol();
            updateTheButtons(Integer.parseInt(opponentSymbol.get(0)), opponentSymbol.get(1));
            updateInfo(InfoWindow.info);
        } catch (NullPointerException ignored){}


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

    public List<String> opponentSymbol(){

        String[] splittedPlace = Messenger.place.split(" ");
        List<String> result = new ArrayList<String>();
        String where = splittedPlace[1];
        String what = splittedPlace[2];

        result.add(where);
        result.add(what);
        return result;

    }

    private void updateTheButtons(int where, String what) {
        buttons[where].setFont(new Font("Arial", Font.PLAIN, 100));
        buttons[where].setForeground(Color.white);
        buttons[where].setText(what.toUpperCase());
        buttons[where].setBackground(Color.BLACK);
    }
}
