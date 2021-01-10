package gui;

import game.Game;
import game.Messenger;

import javax.swing.*;
import java.awt.*;

public class InfoWindow extends JPanel {

    private final JFrame info;

    public InfoWindow(){
        info = new JFrame("Info");
        info.getContentPane().setBackground(Color.BLACK);
        info.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        info.setSize(200,600);
        info.setVisible(true); // show the window
        info.setLocationRelativeTo(null);
        writeInfo();
    }

    protected void writeInfo(){
        info.setLayout(new BorderLayout());
        String informations = "<html>Team: "+ Messenger.team
                + "<br/>" + "Turn: " + Messenger.turn
                +"</html>";
        JLabel label = new JLabel(informations);
        label.setFont(new Font("Arial", Font.PLAIN, 20));
        label.setForeground(Color.white);
        info.add(label, BorderLayout.BEFORE_FIRST_LINE);

    }
}
