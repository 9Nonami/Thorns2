package nona.mi.scene;

import nona.mi.button.ButtonGroup;
import nona.mi.button.RectButton;
import nona.mi.loader.ImageLoader;
import nona.mi.main.Game;
import nona.mi.save.Save;

import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;


public class SaveMenuScene extends Scene { //todo : resetar a cena anterior se voltar para algum lugar que nao seja ela

    private int saveScene;
    private int savePack;
    private BufferedImage screenshot;

    private ButtonGroup buttonGroup;
    private Save save;
    private boolean lockForSave;

    //todo : como saber a cena para a qual retornar? > game.get p-s
    RectButton rb;



    public SaveMenuScene(Game game, Save save, int buttonsToShow) {
        super(game);
        this.save = save;
        buttonGroup = new ButtonGroup(game, buttonsToShow);
        lockForSave = false;

        //todo : del?
        rb = new RectButton(game);
        rb.setImages(ImageLoader.loadImage("/res/buttons/uno.png"), ImageLoader.loadImage("/res/buttons/dos.png"), 50, 390);
        rb.setAudioName("click");
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

            //todo : updatear os outros botoes
            rb.update();
            if (rb.isClicked()) {
                game.setSceneBasis(game.getPackBasis().get(game.getScene())); //todo : nao esta pegando a cena certa
                reset();
                return;
            }


            buttonGroup.update();

            if (buttonGroup.getClickedButton() != ButtonGroup.NO_CLICK) {
                //lockForSave = true; //todo : ver.

                if (buttonGroup.getButtons()[buttonGroup.getClickedButton()].getStandardImage() == buttonGroup.getStandardButtonImage()) {
                    //create
                    //lock-update
                    lockForSave = true;
                    //thread
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                //no linuxrix, mas ta funcionando no windows tambem
                                String tempPath = save.getFolderPath() + "/" + buttonGroup.getClickedButton() + ".png";
                                System.out.println(tempPath);
                                //image-creation

                                Image scaledInstance = screenshot.getScaledInstance(235, 132, Image.SCALE_SMOOTH);
                                BufferedImage resized = new BufferedImage(235, 132, BufferedImage.TYPE_INT_ARGB);
                                Graphics2D g2d = resized.createGraphics();
                                g2d.drawImage(scaledInstance, 0, 0, null);
                                g2d.dispose();

                                ImageIO.write(resized, "png", new File(tempPath));

                                buttonGroup.getButtons()[buttonGroup.getClickedButton()].setStandardImage(resized);

                                //salva no .9
                                save.save(buttonGroup.getClickedButton(), savePack, saveScene);

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

            }
        }
    }

    @Override
    public void renderScene(Graphics g) {
        buttonGroup.render(g);

        rb.render(g);
    }

    @Override
    public void reset() {
        super.reset();
        buttonGroup.reset();
        lockForSave = false; //talvez nem precise - ver.

        rb.reset();
    }

}
