package serverConnection;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Messenger {

    private Socket clientSocket;
    private PrintWriter serverWriter;
    private BufferedReader serverReader;

    private String turn;
    private String team;
    private String placedSymbol;
    private String buttonName;

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
                String[] args = msg.split(" ");

                if (args[0].equals("joined")) setTeam(args[1].toUpperCase());
                else if (args[0].equals("turn")) setTurn(args[1].toUpperCase());
                else if (args[0].equals("placed")) setPlacedSymbol(args[1],args[2].toUpperCase());
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

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getTurn() {
        return turn;
    }

    public void setTurn(String turn) {
        this.turn = turn;
    }

    public List<String> getPlacedSymbol() {
        List<String> result = new ArrayList<>();
        result.add(buttonName);
        result.add(placedSymbol);
        return result;
    }

    public void setPlacedSymbol(String buttonName, String placedSymbol) {
        this.buttonName = buttonName;
        this.placedSymbol = placedSymbol;
    }
}
