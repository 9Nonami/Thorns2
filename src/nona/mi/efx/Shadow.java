package nona.mi.efx;

import nona.mi.image.BaseImage;
import nona.mi.main.Thorns;

import java.awt.Color;
import java.awt.Graphics;

public class Shadow {

    private Color color;
    private int x;
    private int y;
    private int width;
    private int height;
    private int speed;
    private boolean endAnimation;
    private boolean firstGo;

    public static final int FAST = 20;

    private BaseImage[] images;
    private int id;

    private Thorns thorns;

    public Shadow(Thorns thorns, int speed){
        this.thorns = thorns;
        x = 0;
        y = 0;
        width = 0;
        height = thorns.getHeight();
        this.speed = speed;
        endAnimation = false;
        color = Color.BLACK;
        firstGo = true;

        id = 0;
    }

    public void update(){
        if (!endAnimation){
            if (firstGo){
                width += speed;
                if (width >= thorns.getWidth()){
                    firstGo = false;
                    width = thorns.getWidth();
                    id++;
                }
            } else {
                x += speed;
                if (x >= thorns.getWidth()){
                    endAnimation = true;
                    x = thorns.getWidth();
                }
            }
        }
    }

    public void render(Graphics g){
        if (images != null){
            images[id].render(g);
        }
        g.setColor(color);
        g.fillRect(x, y, width, height);
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setImages(BaseImage[] images) {
        this.images = images;
    }

    public void reset(){
        endAnimation = false;
        width = 0;
        firstGo = true;
        id = 0;
    }

}
