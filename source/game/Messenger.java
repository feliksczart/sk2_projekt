package game;

import java.io.*;
import java.net.Socket;

public class Messenger {

    private Socket clientSocket;
    private DataInputStream in;

    public Messenger(int port) throws IOException {
        this.clientSocket = new Socket("localhost",port);
        this.in = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
    }

    public void read() throws IOException {

        BufferedReader FromServer =
                new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        while (true) {
            while (FromServer.ready()) {
                System.out.println(FromServer.readLine());
            }
        }
    }

    public void send() throws IOException {
        PrintWriter toServer =
                new PrintWriter(clientSocket.getOutputStream(),true);
        BufferedReader inFromUser =
                new BufferedReader(new InputStreamReader(System.in));

        while (true) {

            if (inFromUser.ready()) {
                String line = inFromUser.readLine();
                toServer.write(line);
                toServer.flush();
            }
        }
    }
}
