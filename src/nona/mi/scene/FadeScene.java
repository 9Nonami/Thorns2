package nona.mi.scene;

import java.awt.Graphics;

import nona.mi.efx.Fade;
import nona.mi.image.BaseImage;
import nona.mi.main.Game;

public class FadeScene extends Scene {

    private Fade fade;
    private BaseImage background;
    private BaseImage[] backgrounds;
    private int directScene;



    public FadeScene(Game game, BaseImage[] backgrounds, Fade fade, int nextScene, int sceneId) {
        super(game, nextScene, sceneId);
        this.backgrounds = backgrounds;
        this.fade = fade;
        directScene = Scene.NO_SCENE;
    }

    public FadeScene(Game game, BaseImage background, Fade fade, int nextScene, int sceneId) {
        super(game, nextScene, sceneId);
        this.background = background;
        this.fade = fade;
        directScene = Scene.NO_SCENE;
    }

    public void setDirectScene(int directScene) {
        this.directScene = directScene;
    }

    @Override
    public void updateScene() {
        if (!fade.getEndAnimation()) {
            fade.update();
        } else {
            if (directScene != Scene.NO_SCENE) { //todo : se der erro eh aqui
                game.setDirectSceneFromPublicScenes(directScene);
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
