package mainGame;

import javax.swing.*;
import java.io.IOException;

public class GameUpdate {

    private Game game;

    public GameUpdate(Game game){
        this.game = game;
    }

    public void updateButton(){
        try {
            String buttonName = game.getMessenger().getPlacedSymbol().get(0);
            String placedSymbol = game.getMessenger().getPlacedSymbol().get(1);
            JButton[] buttons = game.getGameWindow().getButtons();

            buttons[Integer.parseInt(buttonName)].setText(placedSymbol);
        } catch (NullPointerException ignored){}
    }

    public void startUpdateThread(){
        new Thread(() -> {
            updateButton();
        }).start();
    }
}
