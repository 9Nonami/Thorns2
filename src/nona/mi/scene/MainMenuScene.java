package nona.mi.scene;

import nona.mi.button.ButtonGroup;
import nona.mi.constant.ID;
import nona.mi.main.Game;

import java.awt.Color;
import java.awt.Graphics;



public class MainMenuScene extends Scene {

    private ButtonGroup buttonGroup;



    public MainMenuScene(Game game, int sceneId, ButtonGroup buttonGroup) {
        super(game, Scene.LAST_SCENE, sceneId);
        this.buttonGroup = buttonGroup;
    }

    @Override
    public void updateScene() {
        buttonGroup.update();
        if (buttonGroup.getClickedButton() != ID.NO_CLICK) {
            if (buttonGroup.getClickedButton() == ID.NEW_GAME) {
                nextPack = 0;
                game.nextScene(); //usa a lastScene do construtor
            } else if (buttonGroup.getClickedButton() == ID.LOAD_GAME) {

                //pega a dms do hashmap em game
                SaveMenuScene tempSaveMenuScene = (SaveMenuScene) game.getSceneFromPublicScenes(ID.DMS_SCENE);
                tempSaveMenuScene.setType(ID.LOAD);

                tempSaveMenuScene.setSaveScene(sceneId);
                //game.getSaveMenuScene().setSaveScene(MAIN_MENU_ID);

                tempSaveMenuScene.setSavePack(packId);
                //game.getSaveMenuScene().setSavePack(PACK_FOR_LOAD_GAME);

                game.setDirecSceneFromPublicScenes(ID.DMS_SCENE);
            }
        }
    }

    @Override
    public void renderScene(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, game.getWidth(), game.getHeight());
        buttonGroup.render(g);
    }

    @Override
    public void reset() { //todo : se pah fazer a mesma coisa e criar um abstract resetScene
        super.reset();
        buttonGroup.reset();
    }

}
