package nona.mi.button;

import nona.mi.main.Game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class RectButton extends Button {

    private int imageWidth;
    private int imageHeight;



    public RectButton(Game game) {
        super(game);
    }

    @Override
    public void setImages(BufferedImage standardImage, BufferedImage focusedImage) {
        super.setImages(standardImage, focusedImage);
        this.imageWidth = standardImage.getWidth();
        this.imageHeight = standardImage.getHeight();
    }

    @Override
    public void render(Graphics g) {
        if (showMask) {
            g.setColor(Color.BLUE);
            g.fillRect(imageX, imageY, imageWidth, imageHeight);
        }
        g.drawImage(standardImage, imageX, imageY, null);
        if (focused) {
            g.drawImage(focusedImage, imageX, imageY, null);
        }
    }

    @Override
    protected boolean isMouseOnButton() {
        int mx = game.getMouseX();
        int my = game.getMouseY();
        return (mx >= imageX && mx <= imageX + imageWidth) && (my >= imageY && my <= imageY + imageHeight);
    }

}
