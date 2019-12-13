/*
    este objeto dispara uma animacao quando eh clicado.
    nao muda de cena.
 */

package nona.mi.animatedcomponent;

import nona.mi.main.Game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class AnimatedComponent {

    private BufferedImage[] images;
    private int x;
    private int y;
    private int width;
    private int height;
    private float speed;
    private float id;
    private boolean animating;
    private Game game;
    private String audioName;


    public AnimatedComponent(Game game) {
        this.game = game;
        id = 0;
    }

    public void setImages(BufferedImage[] images, int x, int y, float speed) {
        this.images = images;
        this.x = x;
        this.y = y;
        this.speed = speed;
        width = images[0].getWidth();
        height = images[0].getHeight();
    }

    public void defineAudio(String audioPath, String audioName) {
        this.audioName = audioName;
        game.getPackJukebox().load(audioPath, audioName);
    }

    public void setAudioName(String audioName) {
        this.audioName = audioName;
    }

    public void update() {
        if (isMouseOnComponent() && isComponentClicked() && !animating && !game.getPackJukebox().isPlaying(audioName)) {
            animating = true;
            game.getPackJukebox().play(audioName);
        }
        if (animating) {
            id += speed;
            if (id >= images.length) {
                id = 0;
                animating = false;
            }
        }
    }

    private boolean isMouseOnComponent() {
        int mx = game.getMouseX();
        int my = game.getMouseY();
        return (mx >= x && mx <= x + width) && (my >= y && my <= y + height);
    }

    private boolean isComponentClicked() {
        return game.isClicked();
    }

    public void render(Graphics g) {
        g.drawImage(images[(int)id], x, y, null);
    }

    public void reset() {
        animating = false;
        id = 0;
        if (game.getPackJukebox().isPlaying(audioName)) {
            game.getPackJukebox().stop(audioName);
        }
    }

}
