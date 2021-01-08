package game.main;

import game.Messenger;
import java.io.IOException;

public class MainMessenger {

    public static void main(String[] args) throws IOException {

        Messenger messenger = new Messenger(1111);
        new Thread(() -> {
            try {
                messenger.read();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                messenger.send();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}