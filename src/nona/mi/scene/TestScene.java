package nona.mi.scene;

import nona.mi.main.Thorns;

import java.awt.Graphics;

public class TestScene extends Scene {

    private RollingMenuBasis rollingMenuBasis;

    public TestScene(Thorns thorns) {
        super(thorns, 666);
        rollingMenuBasis = new RollingMenuBasis(thorns, 30);
    }

    @Override
    public void update(){
        rollingMenuBasis.update();
    }

    @Override
    public void render(Graphics g) {
        rollingMenuBasis.render(g);
    }
}
