package serverConnection;

import main.Game;

import java.io.IOException;

public class Listener {

    Messenger messenger;

    public Listener(Messenger messenger){
        this.messenger = messenger;
    }

    public void messageRecieved() {
    }
}
