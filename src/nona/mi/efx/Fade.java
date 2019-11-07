package nona.mi.efx;

import java.awt.Color;
import java.awt.Graphics;

import nona.mi.main.Thorns;

public class Fade {

    public static final int TRANSPARENT = 0;
    public static final int SOLID = 255;
    public static final int SLOW = 1;
    public static final int MEDIUM = 2;
    public static final int FAST = 5;
    private int alpha;
    private int alphaBasis;
    private int speed;
    private boolean endAnimation;

    private int r;
    private int g;
    private int b;

    private Thorns thorns;

    public Fade(Thorns thorns, int alphaValue, int speed) {
        this.thorns = thorns;
        this.speed = speed;
        endAnimation = false;

        this.alpha = alphaValue;
        if (alphaValue == Fade.SOLID) {
            this.speed *= -1;
        }

        alphaBasis = alphaValue;

        this.r = 0;
        this.g = 0;
        this.b = 0;
    }
    
    public void setColor(Color color){
        this.r = color.getRed();
        this.g = color.getGreen();
        this.b = color.getBlue();
    }

    public void update() {
        if (!endAnimation) {
            alpha += speed;
            if (alpha > 255 || alpha < 0) {
                endAnimation = true;
                if (alpha > 255) {
                    alpha = 255;
                } else if (alpha < 0) {
                    alpha = 0;
                }
            }
        }
    }

    public void render(Graphics g) {
        g.setColor(new Color(this.r, this.g, this.b, this.alpha));
        g.fillRect(0, 0, thorns.getWidth(), thorns.getHeight());
    }

    public boolean getEndAnimation() {
        return endAnimation;
    }

    public void reset() {
        alpha = alphaBasis;
        endAnimation = false;
    }

}
