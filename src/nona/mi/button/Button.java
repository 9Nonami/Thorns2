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

    public static final int NO_CLICK = -9;

    public static final int YES = -10;
    public static final int NO = -11;

    public static final int START = -12;

    public static final int RETURN = -13;
    public static final int PREV = -14;
    public static final int NEXT = -15;

    public static final int SAVE = -16;
    public static final int LOAD = -17;
    public static final int COPY = -18;
    public static final int DEL = -19;
    public static final int HISTORY = -20;
    public static final int MAIN = -21;

    public static final int STAN_Y_ALL = 323;
    public static final int CHOICE_Y_ALL = 457;

    public static final int STAN_CHOICE_X_SAVE = 289;
    public static final int STAN_CHOICE_X_LOAD = 382;
    public static final int STAN_CHOICE_X_COPY = 475;
    public static final int STAN_CHOICE_X_DELETE = 568;
    public static final int STAN_CHOICE_X_HISTORY = 661;
    public static final int STAN_CHOICE_X_MAIN = 754;

    public static final int MAIN_X_START = 10;
    public static final int MAIN_Y_START = 10;
    public static final int MAIN_X_LOAD = 10;
    public static final int MAIN_Y_LOAD = 40;



    public Button(Game game) {
        this.game = game;
    }

    public void setImages(BufferedImage standardImage, BufferedImage focusedImage) {
        this.standardImage = standardImage;
        this.focusedImage = focusedImage;
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

    public void setXY(int imageX, int imageY) {
        this.imageX = imageX;
        this.imageY = imageY;
    }

    public int getWidth() {
        return standardImage.getWidth();
    }

    public int getHeight() {
        return standardImage.getHeight();
    }

}
