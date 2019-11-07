package nona.mi.scene;

import nona.mi.main.Thorns;

import java.awt.Graphics;

public class MainMenuScene extends Scene{

    private MenuBasis menuBasis;

    public MainMenuScene(Thorns thorns, int nextScene) { // next scene = scene local do pack no qual este menu está
        super(thorns, nextScene);
        int x = (thorns.getWidth() / 2) - (thorns.getChoicebg().getWidth() / 2);
        int y = thorns.getHeight() / 2;
        menuBasis = new MenuBasis(thorns);
        menuBasis.createOptions(2, x, y, 10, "START_LOAD");
        menuBasis.setPointerRotationSpeed(1);
    }

    @Override
    public void update() {
        super.update();
        menuBasis.update();
        if (menuBasis.isPressed()){
            if (menuBasis.getChosenOption() < menuBasis.getNumOptions()){ // 0 || 1
                if (menuBasis.getChosenOption() == 0){
                    setNextPack(0);
                } //todo : load >> none == return
                thorns.nextScene(LAST_SCENE);
            }
            reset();
        }
    }

    @Override
    public void render(Graphics g) {
        menuBasis.render(g);
    }

    @Override
    public void reset() {
        super.reset();
        menuBasis.reset();
    }
}
