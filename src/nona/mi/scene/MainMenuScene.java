package nona.mi.scene;

import nona.mi.button.ButtonGroup;
import nona.mi.main.Game;

import java.awt.Color;
import java.awt.Graphics;

//TODO : COLOCAR O ID DESTA CENA EM SCENE:GAME
public class MainMenuScene extends Scene {

    private ButtonGroup buttonGroup;
    public static final int MAIN_MENU_ID = -40;
    public static final int NEW_GAME = -41; //TODO : VERIFICAR ESSAS CONSTANTES PARA NAO TER NENHUM IGUAL, OU CENTRALIZAR NUMA INTERFACE
    public static final int LOAD_GAME = -42;

    public static final int PACK_FOR_NEW_GAME = 0;
    public static final int PACK_FOR_LOAD_GAME = -44;


    public MainMenuScene(Game game, ButtonGroup buttonGroup) {
        super(game, Scene.LAST_SCENE);
        this.buttonGroup = buttonGroup;
    }

    @Override
    public void updateScene() {
        buttonGroup.update();
        if (buttonGroup.getClickedButton() != ButtonGroup.NO_CLICK) {
            if (buttonGroup.getClickedButton() == MainMenuScene.NEW_GAME) {
                nextPack = PACK_FOR_NEW_GAME;
                game.nextScene(); //usa a lastScene do construtor
            } else if (buttonGroup.getClickedButton() == MainMenuScene.LOAD_GAME) {
                System.out.println("CLICANDO EM LOAD A PARTIR DO MAIN MENU");
                game.getSaveMenuScene().setType(SaveMenuScene.LOAD);
                game.getSaveMenuScene().setSaveScene(MAIN_MENU_ID);
                System.out.println("Definindo cena: " + MAIN_MENU_ID);
                game.getSaveMenuScene().setSavePack(PACK_FOR_LOAD_GAME);
                System.out.println("Definindo pack: " + PACK_FOR_LOAD_GAME + "\n");
                game.setDirectScene(game.getSaveMenuScene());
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
