package nona.mi.scene;

import java.awt.Graphics;

import nona.mi.image.BaseImage;
import nona.mi.main.Game;

public class LoadScene extends Scene {

    private BaseImage background;

    public LoadScene(Game game, BaseImage background) {
        super(game);
        this.background = background;
    }

    @Override
    public void update() {

    }

    @Override
    public void render(Graphics g) {
        background.render(g);
    }

}
