package nona.mi.scene;

import nona.mi.main.Thorns;
import nona.mi.menu.RollingMenuScene;

import java.awt.Graphics;

public class TestScene extends Scene {

    private RollingMenuScene rollingMenu;
    //private Menu menu;

    public TestScene(Thorns thorns) {
        super(thorns, 666);
        rollingMenu = new RollingMenuScene(thorns);
        //menu = new Menu(thorns, thorns.getChoicebg(), thorns.getFontDataBase(), thorns.getPointer(), Menu.STYLE_VERTICAL);
        //menu.createOptions(10, 10, 10, "ARTICUNO_ZAPDOS_MOLTRES_MISSINGNO");
    }

    @Override
    public void update(){
        rollingMenu.update();
        //menu.update();
    }

    @Override
    public void render(Graphics g) {
        rollingMenu.render(g);
        //menu.render(g);
    }
}