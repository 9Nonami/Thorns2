package nona.mi.button;

import nona.mi.main.Game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public abstract class Button {

    protected Game game;
    protected boolean showMask;
    protected boolean clicked;
    protected boolean focused;
    protected BufferedImage standardImage;
    protected BufferedImage focusedImage;
    protected int imageX;
    protected int imageY;
    protected String audioName;
    protected boolean lockAudio;
    protected int id;

    public static final int NO_CLICK = -64;



    public Button(Game game) {
        this.game = game;
    }

    public void setImages(BufferedImage standardImage, BufferedImage focusedImage, int imageX, int imageY) {
        this.standardImage = standardImage;
        this.focusedImage = focusedImage;
        this.imageX = imageX;
        this.imageY = imageY;
    }

    public void update() {
        clicked = false;
        if (isMouseOnButton()) {
            focused = true;
            if (!lockAudio) {
                lockAudio = true;
                if (!game.getStandardJukeBox().isPlaying(audioName)) {
                    game.getStandardJukeBox().play(audioName);
                }
            }
            if (game.isClicked()) {
                act();
            }
        } else {
            focused = false;
            lockAudio = false;
        }
    }

    public abstract void render(Graphics g);

    protected abstract boolean isMouseOnButton();

    protected void act() {
        clicked = true;
    }

    public void setShowMask(boolean showMask) {
        this.showMask = showMask;
    }

    public boolean isClicked() {
        return clicked;
    }

    public void reset() {
        clicked = false;
        lockAudio = false;
        focused = false;
    }


    public void defineAudio(String audioPath, String audioName) {
        this.audioName = audioName;
        game.getStandardJukeBox().load(audioPath, audioName);
    }

    public void setAudioName(String audioName) {
        this.audioName = audioName;
    }

    //get/set usados pela saveScene para ver. se ja ha dados no slot(button)
    public BufferedImage getStandardImage() {
        return standardImage;
    }

    public void setStandardImage(BufferedImage standardImage) {
        this.standardImage = standardImage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getX() {
        return imageX;
    }

    public int getY() {
        return imageY;
    }

    public int getWidth() {
        return standardImage.getWidth();
    }

    public int getHeight() {
        return standardImage.getHeight();
    }

}
