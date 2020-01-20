package nona.mi.scene;

import java.awt.Graphics;

import nona.mi.efx.Fade;
import nona.mi.image.BaseImage;
import nona.mi.main.Game;

public class FadeScene extends Scene {

    private Fade fade;
    private BaseImage background;
    private BaseImage[] backgrounds;
    private Scene directScene;



    public FadeScene(Game game, BaseImage[] backgrounds, Fade fade, int nextScene) {
        super(game, nextScene);
        this.backgrounds = backgrounds;
        this.fade = fade;
    }

    public FadeScene(Game game, BaseImage background, Fade fade, int nextScene) {
        super(game, nextScene);
        this.background = background;
        this.fade = fade;
    }

    public void setDirectScene(Scene directScene) {
        this.directScene = directScene;
    }

    @Override
    public void update() {
        super.update();

        if (!fade.getEndAnimation()) {
            fade.update();
        } else {

            if (directScene != null) {
                game.setSceneBasis(directScene);
            } else {
                game.nextScene();
            }
        }
    }

    @Override
    public void renderScene(Graphics g) {

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
    public void reset() {
        super.reset();
        fade.reset();
    }

}
