package nona.mi.scene;

import java.awt.Graphics;

import nona.mi.efx.Fade;
import nona.mi.image.BaseImage;
import nona.mi.main.Thorns;

public class FadeScene extends Scene {

    private Fade fade;
    private BaseImage background;
    private BaseImage[] backgrounds;

    public FadeScene(Thorns thorns, BaseImage[] backgrounds, Fade fade, int nextScene) {
        super(thorns, nextScene);
        this.backgrounds = backgrounds;
        this.fade = fade;
    }

    public FadeScene(Thorns thorns, BaseImage background, Fade fade, int nextScene) {
        super(thorns, nextScene);
        this.background = background;
        this.fade = fade;
    }

    @Override
    public void update() {
        super.update();

        if (!fade.getEndAnimation()) {
            fade.update();
        } else {
            thorns.nextScene(nextScene);
            reset();
        }
    }

    @Override
    public void render(Graphics g) {

        if (backgrounds != null) {
            for (int i = 0; i < backgrounds.length; i++) {
                backgrounds[i].render(g);
            }
        } else {
            background.render(g);
        }

        fade.render(g);
    }

    @Override
    public void reset(){
        super.reset();
        fade.reset();
    }

}
