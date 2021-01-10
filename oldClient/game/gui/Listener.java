package game.gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class Listener extends WindowAdapter {

    private final Window window;

    public Listener(Window window) {
        this.window = window;
    }

    @Override
    public void windowClosing(WindowEvent e) {
        super.windowClosing(e);
        window.game.close();
    }
}
