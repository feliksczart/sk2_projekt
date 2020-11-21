package game;

import game.packets.ClientPlayPacket;
import game.packets.EndGamePacket;
import game.packets.UpdatePacket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Game{

    private ServerSocket serverSocket;
    private Socket socket;

    private Connection connection;

    public Server(){
        super(Game.PLAYER_O);
        try {
            serverSocket = new ServerSocket(Game.PORT);
            socket = serverSocket.accept();
            connection = new Connection(this,socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void inputRecieved(int x, int y) {

        if (isMyTurn()){
            updateField(x,y);
        }
    }

    @Override
    public void packetRecieved(Object object) {

        if (object instanceof ClientPlayPacket){
            ClientPlayPacket packet = (ClientPlayPacket) object;
            updateField(packet.getX(),packet.getY());
        }
    }

    private void updateField(int x, int y){
        if (fields[x][y] == Game.NOBODY){
            fields[x][y] = currentPlayer;
            if (currentPlayer == Game.PLAYER_O){
                currentPlayer = Game.PLAYER_X;
            } else if (currentPlayer == Game.PLAYER_X){
                currentPlayer = Game.PLAYER_O;
            }
        }

        connection.sendPacket(new UpdatePacket(fields, currentPlayer));

        gameWindow.repaint();

        int winner = checkWin();

        if (winner != Game.NOBODY){
            endGame(winner);
        } else{
            int emptyCount = 0;
            for (int i = 0; i < 3; i++){
                for (int j = 0; j < 3; j++){
                    if (fields[i][j] == Game.NOBODY){
                        emptyCount++;
                    }
                }
            }
            if (emptyCount == 9){
                endGame(winner);
            }
        }
    }

    private void endGame(int winner){
        showWinner(winner);
        connection.sendPacket(new EndGamePacket(winner));
    }

    private int checkWin(){
        for (int player = Game.PLAYER_O; player <= Game.PLAYER_X; player++){
            for (int y = 0; y < 3; y++){
                int playerCount = 0;

                for (int x = 0; x < 3; x++){
                    if (fields[x][y] == player){
                        playerCount++;
                    }
                }
                if (playerCount == 3){
                    return player;
                }
            }
            for (int x = 0; x < 3; x++){
                int playerCount = 0;

                for (int y = 0; y < 3; y++){
                    if (fields[x][y] == player){
                        playerCount++;
                    }
                }
                if (playerCount == 3){
                    return player;
                }
            }
            int playerCount = 0;
            for (int coordinate = 0; coordinate < 3; coordinate++){
                if (fields[coordinate][coordinate] == player){
                    playerCount++;
                }
            }
            if (playerCount == 3){
                return player;
            }
            playerCount = 0;
            for (int coordinate = 0; coordinate < 3; coordinate++){
                if (fields[2-coordinate][coordinate] == player){
                    playerCount++;
                }
            }
            if (playerCount == 3){
                return player;
            }
        }
        return Game.NOBODY;
    }

    @Override
    public void close() {
        try {
            connection.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
