package gui;


import serverConnection.Messenger;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.TimeUnit;

public class InfoWindow extends JPanel {

    public static JFrame info;
    public static JLabel label;

    public InfoWindow(){
        info = new JFrame("Info");
        info.getContentPane().setBackground(Color.BLACK);
        info.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        info.setSize(200,600);
        info.setVisible(true);
        info.setLocationRelativeTo(null);
        info.setResizable(false);
        writeInfo();
    }

    protected void writeInfo(){
        info.setLayout(new BorderLayout());
        String informations = "<html>Team: "+ Messenger.team
                + "<br/>" + "Turn: " + Messenger.turn
                +"</html>";
        label = new JLabel(informations);
        label.setFont(new Font("Arial", Font.PLAIN, 20));
        label.setForeground(Color.white);
        info.add(label, BorderLayout.BEFORE_FIRST_LINE);
    }

    public static void updateInfo(){
        String informations = "<html>Team: "+ Messenger.team
                + "<br/>" + "Turn: " + Messenger.turn
                +"</html>";

        label.setText(informations);
    }

    public static void infoReset(JFrame info){
        info.remove(label);
    }
}
