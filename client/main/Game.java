package main;

import gui.GameWindow;
import gui.InfoWindow;
import serverConnection.Listener;
import serverConnection.Messenger;
import serverConnection.MyListener;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import static gui.InfoWindow.infoReset;
import static gui.InfoWindow.updateInfo;


public class Game extends JPanel implements MyListener {

    char currentPlayer = 'x';
    public static JButton[] buttons;
    List<Integer> placed = new ArrayList<Integer>();

    public int port;
    public static Messenger messenger;
    public static boolean trueIsPlace;
    public static boolean ready = false;
    public static boolean gameReset = false;
    public static String trueTurn;

    public Game() throws IOException {
        setLayout(new GridLayout(3, 3));
        initializeButtons();
        //this.port = Integer.parseInt(JOptionPane.showInputDialog("Choose port"));
        messenger = new Messenger(1111);
    }

    public void initializeButtons() {
        buttons = new JButton[9];
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

                        try {
                            messenger.sendMessage("vote " + buttonClicked.getName());
                        } catch (IOException ioe) {
                            ioe.printStackTrace();
                        }

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
                            updateInfo();
                        }

                        if (currentPlayer == 'x') {
                            currentPlayer = 'o';
                            buttonClicked.setBackground(Color.BLACK);
                        } else {
                            currentPlayer = 'x';
                            buttonClicked.setBackground(Color.BLACK);
                        }

                    } else {
                        if(buttonClicked.getName().equals("4")) {

                            try {
                                messenger.sendMessage("ready");
                            } catch (IOException ioe) {
                                ioe.printStackTrace();
                            }

                            ready = true;
                            buttons[4].setText(" ");
                            buttons[4].setBackground(Color.BLACK);

                            if(!gameReset){
                                new InfoWindow();
                            }
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
            updateInfo();
            winnerChosen();
        } catch (NullPointerException | IOException ignored){}


    }

    public void winnerChosen() throws IOException {
        if(Messenger.winner != null){
            JOptionPane pane = new JOptionPane();
            int dialogResult;
            if(!Messenger.winner.equals("Draw")){
                dialogResult = JOptionPane.showConfirmDialog(pane, Messenger.winner + " wins. Would you like to play again?", "Game over.",
                        JOptionPane.YES_NO_OPTION);
            } else {
                dialogResult = JOptionPane.showConfirmDialog(pane, Messenger.winner + "! Would you like to play again?", "Game over.",
                        JOptionPane.YES_NO_OPTION);
            }

            if (dialogResult == JOptionPane.YES_OPTION){
                GameWindow.windowReset();
                infoReset(InfoWindow.info);
                //ready = false;
                gameReset = true;
                Messenger.winner = null;
                //resetTheButtons();
                //initializeButtons();
                resetTheButtons();
                buttons[4].setFont(new Font("Arial", Font.PLAIN, 40));
                buttons[4].setForeground(Color.BLACK);
                buttons[4].setText("I'm ready");
                buttons[4].setBackground(Color.GREEN);

            }
            else System.exit(0);
        }
    }



    private void resetTheButtons() {

        for (int i = 0; i < 9; i++) {

            buttons[i].setText(" ");
            buttons[i].setBackground(Color.BLACK);

        }
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