package nona.mi.scene;

import nona.mi.button.Button;
import nona.mi.main.Game;

import java.awt.Color;
import java.awt.Graphics;



public class MainMenuScene extends Scene {

    public static final int SCENE_TYPE = 7;

    public MainMenuScene(Game game, int sceneId) {
        super(game, Scene.LAST_SCENE, sceneId);
    }

    @Override
    public int getSceneType() {
        return SCENE_TYPE;
    }

    @Override
    public void updateScene() {
        game.getMainButtonGroup().update();
        if (game.getMainButtonGroup().getClickedButton() != Button.NO_CLICK) {
            if (game.getMainButtonGroup().getClickedButton() == Button.START) {
                nextPack = 0;
                game.getSave().getTracer().setEmptyTraces();
                game.nextScene(); //usa a lastScene do construtor
            } else if (game.getMainButtonGroup().getClickedButton() == Button.LOAD) {

                //pega a dms do hashmap em game
                DataManagerScene tempDataManagerScene = (DataManagerScene) game.getSceneFromPublicScenes(Scene.DMS_SCENE);
                tempDataManagerScene.setType(Button.LOAD);

                tempDataManagerScene.setSceneToReturn(sceneId);

                tempDataManagerScene.setPackToReturn(packId);

                game.setDirectSceneFromPublicScenes(Scene.DMS_SCENE);
            }
        }
    }

    @Override
    public void renderScene(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, game.getWidth(), game.getHeight());
        game.getMainButtonGroup().render(g);
    }

    @Override
    public void reset() {
        super.reset();
        game.getMainButtonGroup().reset();
    }

}
