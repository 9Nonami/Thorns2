package nona.mi.rectangle;

import java.awt.Color;
import java.awt.Graphics;

public class Rectangle {

    private int x;
    private int y;
    private int width;
    private int height;
    public static final int NO_FOCUS = -1;



    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isOnArea(int px, int py) {
        return (px > x && px < x + width && py > y && py < y + height);
    }

    public void render(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x, y, width, height);
    }

}
