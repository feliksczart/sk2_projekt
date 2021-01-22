package serverConnection;

import mainGame.Game;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Messenger {

    private Socket clientSocket;
    private PrintWriter serverWriter;
    private BufferedReader serverReader;
    private Game game;

    public Messenger(int port) throws IOException {
        clientSocket = new Socket("localhost", port);
        serverWriter = new PrintWriter(clientSocket.getOutputStream(),true);
        serverReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public void read() throws IOException {

        while (true) {
            while (serverReader.ready()) {
                String msg = serverReader.readLine();
                System.out.println(msg);
                game.executeCommand(msg);
            }
        }
    }

    public void sendMessage(String message) {
        serverWriter.write(message);
        serverWriter.flush();
    }

    public void startReadThread() {
        new Thread(() -> {
            try {
                read();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void setGame(Game game){
        this.game = game;
    }
}
