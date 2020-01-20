package nona.mi.efx;

import java.awt.Color;
import java.awt.Graphics;

import nona.mi.main.Game;

public class Fade {

    public static final int TRANSPARENT = 0;
    public static final int SOLID = 255;

    public static final int SLOW = 1;
    public static final int MEDIUM = 2;
    public static final int FAST = 5;
    public static final int DEMONIAC = 25;

    private float alpha;
    private int alphaBasis;

    private float speed;
    private float speedBasis;

    private boolean endAnimation;
    private boolean fadeOutIn;

    private int r;
    private int g;
    private int b;

    private Game game;



    public Fade(Game game, int alphaValue, int speed) {
        this.game = game;
        this.speed = (speed * game.getSpeedAdjust());
        endAnimation = false;

        this.alpha = alphaValue;
        if (alphaValue == Fade.SOLID) {
            this.speed *= -1;
        }

        alphaBasis = alphaValue;

        speedBasis = this.speed;

        this.r = 0;
        this.g = 0;
        this.b = 0;
    }
    
    public void setColor(Color color) {
        this.r = color.getRed();
        this.g = color.getGreen();
        this.b = color.getBlue();
    }

    public void update() {
        if (!endAnimation) {
            alpha += speed;
            if (!fadeOutIn) {
                if (alpha > SOLID || alpha < TRANSPARENT) {
                    endAnimation = true;
                    if (alpha > SOLID) {
                        alpha = SOLID;
                    } else if (alpha < TRANSPARENT) {
                        alpha = TRANSPARENT;
                    }
                }
            } else {
                if (alpha < TRANSPARENT) {
                    alpha = TRANSPARENT;
                    speed *= -1;
                } else if (alpha > SOLID) {
                    alpha = SOLID;
                    endAnimation = true;
                }
            }
        }
    }

    public void render(Graphics g) {
        g.setColor(new Color(this.r, this.g, this.b, (int)this.alpha));
        g.fillRect(0, 0, game.getWidth(), game.getHeight());
    }

    public boolean getEndAnimation() {
        return endAnimation;
    }

    public void setFadeOutIn(boolean fadeOutIn) {
        this.fadeOutIn = fadeOutIn;
    }

    public void reset() {
        alpha = alphaBasis;
        speed = speedBasis;
        endAnimation = false;
    }

}
