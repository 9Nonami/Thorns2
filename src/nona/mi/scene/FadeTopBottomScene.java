package nona.mi.scene;

import java.awt.Graphics;

import nona.mi.main.Thorns;
import nona.mi.image.BaseImage;
import nona.mi.image.ImageEfx;

public class FadeTopBottomScene extends Scene {

    private BaseImage[] bottom;
    private ImageEfx[] top;
    private BaseImage textArea;

    public FadeTopBottomScene(Thorns thorns, BaseImage[] bottom, ImageEfx[] top, int nextScene) {
        super(thorns, nextScene);
        this.bottom = bottom;
        this.top = top;
    }

    public void setTextArea(BaseImage textArea) {
        this.textArea = textArea;
    }

    @Override
    public void update() {
        super.update();
        if (!top[0].getAllFinishedAnimations()) {
            for (int i = 0; i < top.length; i++) {
                top[i].update();
            }
        } else {
            thorns.nextScene(nextScene);
            reset();
        }
    }

    @Override
    public void render(Graphics g) {
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
