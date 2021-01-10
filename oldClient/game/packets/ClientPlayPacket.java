package game.packets;

import java.io.Serializable;

public class ClientPlayPacket implements Serializable {

    private static final long serialVersionUID = -7677690212874843003L;

    private int x, y;

    public ClientPlayPacket(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
