package nona.mi.scene;

import nona.mi.main.Thorns;
import nona.mi.menu.Menu;

import java.awt.Graphics;

public class TestScene extends Scene {

    private RollingMenuBasis rollingMenuBasis;
    //private Menu menu;

    public TestScene(Thorns thorns) {
        super(thorns, 666);
        rollingMenuBasis = new RollingMenuBasis(thorns, 30);
        //menu = new Menu(thorns, thorns.getChoicebg(), thorns.getFontDataBase(), thorns.getPointer(), Menu.STYLE_VERTICAL);
        //menu.createOptions(10, 10, 10, "ARTICUNO_ZAPDOS_MOLTRES_MISSINGNO");
    }

    @Override
    public void update(){
        rollingMenuBasis.update();
        //menu.update();
    }

    @Override
    public void render(Graphics g) {
        rollingMenuBasis.render(g);
        //menu.render(g);
    }
}