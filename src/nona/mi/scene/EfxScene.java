package nona.mi.scene;

import java.awt.Graphics;

import nona.mi.image.ImageEfx;
import nona.mi.image.BaseImage;
import nona.mi.main.Game;

public class EfxScene extends Scene {

    private ImageEfx[] images;
    private BaseImage background;
    private BaseImage[] backgrounds;
    private BaseImage textArea;
    private boolean endAnimation;

    public EfxScene(Game game, ImageEfx[] images, BaseImage background, int nextScene) {
        super(game, nextScene);
        this.images = images;
        this.background = background;
    }

    public EfxScene(Game game, ImageEfx[] images, BaseImage[] backgrounds, int nextScene) {
        super(game, nextScene);
        this.images = images;
        this.backgrounds = backgrounds;
    }

    @Override
    public void update() {
        super.update();
        if (!endAnimation) {
            for (int i = 0; i < images.length; i++) {
                images[i].update();
                if (allImagesAreLoaded()) {
                    endAnimation = true;
                    game.nextScene();
                    reset();
                }
            }
        }
    }

    public boolean allImagesAreLoaded() {
        boolean b = true;
        for (int i = 0; i < images.length; i++) {
            b = images[i].getAllFinishedAnimations();
            if (b == false) {
                return b;
            }
        }
        return b;
    }

    @Override
    public void renderScene(Graphics g) {

        if (backgrounds != null){
            for (int i = 0; i < backgrounds.length; i++) {
                backgrounds[i].render(g);
            }
        } else {
            background.render(g);
        }

        for (int i = 0; i < images.length; i++) {
            images[i].render(g);
        }

        if (textArea != null) {
            textArea.render(g);
        }
    }

    public boolean getEndAnimation() {
        return endAnimation;
    }

    public void setTextArea(BaseImage textArea) {
        this.textArea = textArea;
    }

    @Override
    public void reset() {
        super.reset();
        for (int i = 0; i < images.length; i++) {
            images[i].reset();
        }
        endAnimation = false;
    }

}
