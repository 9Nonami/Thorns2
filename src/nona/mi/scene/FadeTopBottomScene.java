package nona.mi.scene;

import java.awt.Graphics;

import nona.mi.main.Game;
import nona.mi.image.BaseImage;
import nona.mi.image.ImageEfx;

public class FadeTopBottomScene extends Scene {

    private BaseImage[] bottom;
    private ImageEfx[] top;
    private BaseImage textArea;



    public FadeTopBottomScene(Game game, BaseImage[] bottom, ImageEfx[] top, int nextScene, int sceneId) {
        super(game, nextScene, sceneId);
        this.bottom = bottom;
        this.top = top;
    }

    public void setTextArea(BaseImage textArea) {
        this.textArea = textArea;
    }

    @Override
    public void updateScene() {
        if (!top[0].getAllFinishedAnimations()) {
            for (int i = 0; i < top.length; i++) {
                top[i].update();
            }
        } else {
            game.nextScene();
            reset();
        }
    }

    @Override
    public void renderScene(Graphics g) {
        for (int i = 0; i < bottom.length; i++) {
            bottom[i].render(g);
        }
        for (int i = 0; i < top.length; i++) {
            top[i].render(g);
        }
        if (textArea != null) {
            textArea.render(g);
        }
    }

    @Override
    public void reset() {
        super.reset();
        for (int i = 0; i < top.length; i++) {
            top[i].reset();
        }
    }

}
