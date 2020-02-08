package nona.mi.scene;

import java.awt.Graphics;

import nona.mi.image.BaseImage;
import nona.mi.main.Game;

public class LoadScene extends Scene {

    private BaseImage background;

    public LoadScene(Game game, int sceneId, BaseImage background) {
        super(game, sceneId);
        this.background = background;
    }

    @Override
    public void updateScene() {

    }

    @Override
    public void renderScene(Graphics g) {
        background.render(g);
    }

}
