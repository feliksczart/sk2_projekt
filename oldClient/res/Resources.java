package res;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Resources {

    public static BufferedImage[] symbols;

    static {
        symbols = new BufferedImage[2];
        symbols[0] = loadImage("resources/o.png");
        symbols[1] = loadImage("resources/x.png");
    }

    private static BufferedImage loadImage(String path){
        try {
            return ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return null;
    }
}