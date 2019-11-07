package nona.mi.efx;

import nona.mi.main.Game;
import nona.mi.main.Thorns;

import java.awt.*;
import java.util.Random;

public class Bubbles {

    private final int SIZE = 15;
    private int xRange;
    private final Random RANDOM = new Random();
    private final int SPEED_RANGE = 10;
    private final Color COLOR = Color.WHITE;

    private Bubble[] bubbles;

    private Thorns thorns;

    public Bubbles(Thorns thorns){

        this.thorns = thorns;

        xRange = (thorns.getWidth() - (2 * SIZE));

        bubbles = new Bubble[50];
        for (int i = 0; i < bubbles.length; i++) {
            bubbles[i] = new Bubble();
        }

    }

    public void update(){
        for (int i = 0; i < bubbles.length; i++) {
            bubbles[i].update();
        }
    }

    public void render(Graphics g){
        for (int i = 0; i < bubbles.length; i++) {
            bubbles[i].render(g);
        }
    }

    class Bubble {

        private int x;
        private int y;
        private int speed;
        private int yBasis;

        Bubble(){
            x = (RANDOM.nextInt(xRange) + SIZE);
            y = thorns.getHeight() + SIZE;
            yBasis = y;
            speed = (RANDOM.nextInt(SPEED_RANGE) + 1);
        }

        void update(){

            y -= speed;

            if (y < 0){
                reset();
            }
        }

        void render(Graphics g){
            g.setColor(COLOR);
            g.drawOval(x, y, SIZE, SIZE);
        }

        void reset(){
            x = (RANDOM.nextInt(xRange) + SIZE);
            y = yBasis;
            speed = (RANDOM.nextInt(SPEED_RANGE) + 1);
        }

    }

}