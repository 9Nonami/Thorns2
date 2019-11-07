package nona.mi.scene;

import java.awt.Graphics;

import nona.mi.image.BaseImage;
import nona.mi.main.Thorns;

public class LoadScene extends Scene {

    private BaseImage background;

    public LoadScene(Thorns thorns, BaseImage background) {
        super(thorns, -1); //nextScene nunca usado
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
