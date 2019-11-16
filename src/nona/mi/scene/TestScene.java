package nona.mi.scene;

import nona.mi.image.Coordinates;
import nona.mi.image.ImageEfx;
import nona.mi.main.Thorns;

import java.awt.Graphics;

public class TestScene extends Scene {

    private ImageEfx imageEfx;

    public TestScene(Thorns thorns) {
        super(thorns, -666);
        imageEfx = new ImageEfx(thorns, thorns.getChoicebg(), new Coordinates(10, 10));
        imageEfx.setAlpha(ImageEfx.SOLID, 0.01f); //0.05f
    }

    @Override
    public void update(){
        imageEfx.update();
        if (imageEfx.getEndAlphaAnimation()) {
            imageEfx.reset();
        }
    }

    @Override
    public void render(Graphics g) {
        imageEfx.render(g);
    }

}