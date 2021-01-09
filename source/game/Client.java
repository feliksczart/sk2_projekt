package game;

import game.packets.ClientPlayPacket;
import game.packets.EndGamePacket;
import game.packets.UpdatePacket;

import java.io.IOException;
import java.net.Socket;

public class Client extends Game{

    private Socket socket;
//    private Connection connection;

    public Client() {
        super(Game.PLAYER_X);
        //socket = new Socket("localhost", Game.PORT);
//            socket = new Socket("127.0.0.1", 1111);
//            connection = new Connection(this,socket);
    }

    @Override
    public void inputRecieved(int x, int y) {

        if (isMyTurn()){
//            connection.sendPacket(new ClientPlayPacket(x,y));
        }
    }

    @Override
    public void packetRecieved(Object object) {

        if (object instanceof UpdatePacket){
            UpdatePacket packet = (UpdatePacket) object;
            fields = packet.getFields();
            currentPlayer = packet.getCurrentPlayer();
        } else if (object instanceof EndGamePacket){
            EndGamePacket packet = (EndGamePacket) object;
            showWinner(packet.getWinner());
        }

        gameWindow.repaint();
    }

    @Override
    public void close() {
        try {
//            connection.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
