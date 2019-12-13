package nona.mi.scene;

import nona.mi.button.RectButton;
import nona.mi.loader.ImageLoader;
import nona.mi.main.Game;

import java.awt.Graphics;

public class TestButtonScene extends Scene {

    private RectButton rectButton;



    public TestButtonScene(Game game) {
        super(game);

        rectButton = new RectButton(game, Scene.LAST_SCENE);
        rectButton.setImages(ImageLoader.loadImage("/res/buttons/uno.png"), ImageLoader.loadImage("/res/buttons/dos.png"), 50, 50);
        rectButton.setAudioName("click");

        setNextPack(0);

    }

    @Override
    public void update() {
        super.update();

        rectButton.update();

        if (rectButton.isClicked()) {
            nextScene = rectButton.getNextScene();
            game.nextScene();
        }
    }

    @Override
    public void render(Graphics g) {
        rectButton.render(g);
    }

    @Override
    public void reset() {
        super.reset();
        rectButton.reset();
    }
}
