package nona.mi.scene;

import java.awt.Graphics;

import nona.mi.main.Game;
import nona.mi.image.BaseImage;
import nona.mi.image.ImageEfx;

public class FadeTopBottomScene extends Scene {

    private BaseImage[] bottom;
    private ImageEfx[] top;

    public static final int SCENE_TYPE = 4;



    public FadeTopBottomScene(Game game, BaseImage[] bottom, ImageEfx[] top, int nextScene, int sceneId) {
        super(game, nextScene, sceneId);
        this.bottom = bottom;
        this.top = top;
    }

    @Override
    public int getSceneType() {
        return SCENE_TYPE;
    }

    @Override
    public void updateScene() {
        if (!top[0].getAllFinishedAnimations()) {
            for (ImageEfx imageEfx : top) {
                imageEfx.update();
            }
        } else {
            game.nextScene();
            reset();
        }
    }

    @Override
    public void renderScene(Graphics g) {
        for (BaseImage baseImage : bottom) {
            baseImage.render(g);
        }
        for (ImageEfx imageEfx : top) {
            imageEfx.render(g);
        }
    }

    @Override
    public void reset() {
        super.reset();
        for (ImageEfx imageEfx : top) {
            imageEfx.reset();
        }
    }

}
