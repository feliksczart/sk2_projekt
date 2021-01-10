package game.main;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Forum {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                JFrame frame = new JFrame();
                frame.setSize(800, 600);
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                JButton button = new JButton("Click me");
                //Explicitly set the size to what you want
                button.setPreferredSize(new Dimension(100, 100));

                //Content pane default layout is BorderLayout which does NOT
                //Respect the preferred sizes. Set it to FlowLayout which does
                frame.getContentPane().setLayout(new FlowLayout());
                frame.getContentPane().add(button);

                //Finally display the frame
                frame.setVisible(true);
            }
        });
    }
}