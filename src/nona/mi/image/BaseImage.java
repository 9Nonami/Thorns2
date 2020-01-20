package nona.mi.image;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class BaseImage {
    
    protected int x;
    protected int y;
    protected BufferedImage image;
    
    public BaseImage(BufferedImage image) {
        this.image = image;
        this.x = 0;
        this.y = 0;
    }
    
    public BaseImage(BufferedImage image, int x, int y) {
        this.image = image;
        this.x = x;
        this.y = y;
    }
    
    public int getX() {
        return this.x;
    }
    
    public void setX(int x) {
        this.x = x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public void setY(int y) {
        this.y = y;
    }
    
    public int getWidth() {
        return this.image.getWidth();
    }
    
    public int getHeight() {
        return this.image.getHeight();
    }

    public BufferedImage getImage() {
        return image;
    }

    public void update() {
        
    }
    
    public void render(Graphics g) {
        if (image != null) {
            g.drawImage(image, x, y, null);
        } else {
            System.out.println("Null image: " + BaseImage.class.getName());
            System.exit(0);
        }
    }    
}
