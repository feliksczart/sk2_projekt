package serverConnection;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Messenger {

    private Socket clientSocket;
    public static String team;
    public static String turn;
    public static int port;
    public static boolean vote;
    public static String place;
    public static boolean isPlace;

    public static List<MyListener> listeners = new ArrayList<MyListener>();

    public Messenger(int port) throws IOException {
        this.port = port;
        this.clientSocket = new Socket("localhost",port);
    }

    public void addListener(MyListener toAdd) {
        listeners.add(toAdd);
    }

    public void read() throws IOException {

        BufferedReader FromServer =
                new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        vote = true;
        while (true) {
            while (FromServer.ready()) {
                String info = FromServer.readLine();
                System.out.println(info);

                if(info.equals("joined x")){
                    team = "X";
                }
                else if(info.equals("joined o")){
                    team = "O";
                }
//                else if(info.equals("ready x")){
//                    team = "X";
//                }
//                else if(info.equals("ready o")){
//                    team = "O";
//                }
                else if(info.equals("turn x")){
                    turn = "X";
                }
                else if(info.equals("turn o")){
                    turn = "O";
                }
                else if(info.equals("illegal_vote")){
                    vote = false;
                }
                else if(info.contains("placed")){
                    place = info;
                    isPlace = true;
                }

                for (MyListener hl : listeners)
                    hl.messageReceived();
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

    public void sendMessage(String message) throws IOException {
        PrintWriter toServer =
                new PrintWriter(clientSocket.getOutputStream(),true);
        toServer.write(message);
        toServer.flush();

    }
}
