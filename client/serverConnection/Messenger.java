package serverConnection;

import java.io.*;
import java.net.Socket;

public class Messenger {

    private Socket clientSocket;
    private PrintWriter serverWriter;
    private BufferedReader serverReader;

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
}
