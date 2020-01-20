package nona.mi.scene;

import nona.mi.button.ButtonGroup;
import nona.mi.main.Game;
import nona.mi.save.Save;

import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;


public class SaveMenuScene extends Scene {

    private int saveScene;
    private int savePack;
    private BufferedImage screenshot;

    private ButtonGroup buttonGroup;
    private Save save;

    //
    private boolean lockForSave;



    public SaveMenuScene(Game game, Save save, int buttonsToShow) {
        super(game);
        this.save = save;
        buttonGroup = new ButtonGroup(game, buttonsToShow);
        lockForSave = false;
    }

    public void setInfo(int savePack, int saveScene, BufferedImage screenshot) {
        this.savePack = savePack;
        this.saveScene = saveScene;
        this.screenshot = screenshot;
    }

    public void setImages(String standardButtonImage, String focusedButtonImage) {
        buttonGroup.setImages(standardButtonImage, focusedButtonImage);
    }

    public void createButtons(int totalButtons, int row, int column, int x, int y, int spacing) {
        buttonGroup.createButtons(totalButtons, row, column, x, y, spacing, game.getScreenshots());
    }

    @Override
    public void update() {
        super.update();

        if (!lockForSave) { //nao deixa atualizar se estiver salvando
            buttonGroup.update();

            if (buttonGroup.getClickedButton() != ButtonGroup.NO_CLICK) {
                //lockForSave = true; //todo : ver.
                if (game.getScreenshots().containsKey(buttonGroup.getClickedButton())) {
                    //override
                } else {
                    //create
                    //lock-update
                    lockForSave = true;
                    //thread
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                //no linuxrix
                                String tempPath = save.getFolderPath() + "/" + buttonGroup.getClickedButton() + ".png";
                                System.out.println(tempPath);
                                //image-creation
                                ImageIO.write(screenshot, "png", new File(tempPath));
                                System.out.println("foi");
                                //Thread.sleep(3000);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                            lockForSave = false;
                        }
                    });
                    thread.start();
                }
                //updatear o save
            }
        }
    }

    @Override
    public void renderScene(Graphics g) {
        buttonGroup.render(g);
    }

    @Override
    public void reset() {
        super.reset();
        buttonGroup.reset();
        lockForSave = false; //talvez nem precise - ver.
    }

}
