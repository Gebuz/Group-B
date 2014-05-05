package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.util.*;

/**
 *
 * @author flemmingxu
 */
public class MovableImage {
    private BufferedImage image;
    private Rectangle rect;
    
    public MovableImage(BufferedImage image, int x, int y) {
        this.image = image;
        rect = new Rectangle(x, y, image.getWidth(), image.getHeight());
    }
    
    public BufferedImage getImg() {
        return image;
    }
    
    public Rectangle getRect() {
        return rect;
    }
    
    public void move(int x, int y) {
        rect.setBounds(x, y, rect.width, rect.height);
    }
    
    public void draw(Graphics2D g2) {
        g2.drawImage(image, rect.x, rect.y, null);
    }
}
