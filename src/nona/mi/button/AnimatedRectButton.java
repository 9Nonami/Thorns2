/*
    um botao animado.
    muda de cena quando eh clicado.
 */

package nona.mi.button;

import nona.mi.main.Game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class AnimatedRectButton extends Button {

    private BufferedImage[] images;
    private int imageWidth;
    private int imageHeight;
    private float id;
    private float speed;

    public AnimatedRectButton(Game game) {
        super(game);
        id = 0;
    }

    public void setImages(BufferedImage[] images, int imageX, int imageY, float speed) {
        this.images = images;
        this.imageX = imageX;
        this.imageY = imageY;
        this.speed = speed;
        imageWidth = images[0].getWidth();
        imageHeight = images[0].getHeight();
    }

    @Override
    public void update() {
        if (isMouseOnButton()) {
            if (!lockAudio) {
                lockAudio = true;
                if (!game.getPackJukebox().isPlaying(audioName)) {
                    game.getPackJukebox().play(audioName);
                }
            }
            if (game.isClicked()) {
                act();
            }
        } else {
            lockAudio = false;
        }
        id += speed;
        if (id >= images.length) {
            id = 0;
        }
    }

    @Override
    public void render(Graphics g) {
        if (showMask) {
            g.setColor(Color.BLUE);
            g.fillRect(imageX, imageY, imageWidth, imageHeight);
        }
        g.drawImage(images[(int)id], imageX, imageY, null);
    }

    @Override
    protected boolean isMouseOnButton() {
        int mx = game.getMouseX();
        int my = game.getMouseY();
        return (mx >= imageX && mx <= imageX + imageWidth) && (my >= imageY && my <= imageY + imageHeight);
    }

    @Override
    public void reset() {
        super.reset();
        id = 0;
        if (game.getPackJukebox().isPlaying(audioName)) {
            game.getPackJukebox().stop(audioName);
        }
    }

}
