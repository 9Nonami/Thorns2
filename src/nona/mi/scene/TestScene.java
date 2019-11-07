package nona.mi.scene;

import nona.mi.main.Thorns;

import java.awt.Graphics;

public class TestScene extends Scene {

    private MenuBasis menuBasis;

    public TestScene(Thorns thorns) {
        super(thorns, 666);
        menuBasis = new MenuBasis(thorns);
        menuBasis.createOptions(4, 0, 0, 10, "ARTICUNO_ZAPDOS_MOLTRES_MISSINGNO");
        menuBasis.setPointerRotationSpeed(1);
    }

    @Override
    public void update(){
        menuBasis.update();
        if (menuBasis.isPressed()){
            System.out.println("chosen: " + menuBasis.getChosenOption());
            System.exit(0);
        }

    }

    @Override
    public void render(Graphics g) {
        menuBasis.render(g);
    }
}
