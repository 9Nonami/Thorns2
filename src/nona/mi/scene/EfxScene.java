package nona.mi.scene;

import java.awt.Graphics;

import nona.mi.image.ImageEfx;
import nona.mi.image.BaseImage;
import nona.mi.main.Game;

public class EfxScene extends Scene {

    private ImageEfx[] images;
    private BaseImage background;
    private BaseImage[] backgrounds;
    private boolean endAnimation;

    public static final int SCENE_TYPE = 2;



    public EfxScene(Game game, ImageEfx[] images, BaseImage background, int nextScene, int sceneId) {
        super(game, nextScene, sceneId);
        this.images = images;
        this.background = background;
    }

    public EfxScene(Game game, ImageEfx[] images, BaseImage[] backgrounds, int nextScene, int sceneId) {
        super(game, nextScene, sceneId);
        this.images = images;
        this.backgrounds = backgrounds;
    }

    @Override
    public int getSceneType() {
        return SCENE_TYPE;
    }

    @Override
    public void updateScene() {
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
    }

    public boolean getEndAnimation() {
        return endAnimation;
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
