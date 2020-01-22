package nona.mi.scene;

import nona.mi.button.Button;
import nona.mi.button.ButtonGroup;
import nona.mi.button.SlotGroup;
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

// todo : ver se para deixar mais abstrato seria legal dar setButtonGroup e setSlotGroup, em polimorquaza
public class SaveMenuScene extends Scene { //todo : resetar a cena anterior se voltar para algum lugar que nao seja ela

    private int saveScene;
    private int savePack;
    private BufferedImage screenshot;

    private SlotGroup slotGroup;
    private Save save;
    private boolean lockForSave;

    //
    private ButtonGroup buttonGroup;
    private final int RETURN_TO_LAST_SCENE = 0;
    private final int PREVIOUS_SLOT_GROUP = 1;
    private final int NEXT_SLOT_GROUP = 2;



    public SaveMenuScene(Game game, Save save, int buttonsToShow) {
        super(game);
        this.save = save;
        slotGroup = new SlotGroup(game, buttonsToShow);
        lockForSave = false;

        createButtons();
    }

    public void setInfo(int savePack, int saveScene, BufferedImage screenshot) {
        this.savePack = savePack;
        this.saveScene = saveScene;
        this.screenshot = screenshot;
    }

    public void setImages(String standardButtonImage, String focusedButtonImage) {
        slotGroup.setImages(standardButtonImage, focusedButtonImage);
    }

    public void createSlots(int totalButtons, int row, int column, int x, int y, int spacing) {
        slotGroup.createButtons(totalButtons, row, column, x, y, spacing, game.getScreenshots());
    }

    private void createButtons() {
        //todo : ver se algumas imagens podem ficar publicas em Game --thorns nao
        BufferedImage stan = ImageLoader.loadImage("/res/buttons/uno.png");
        BufferedImage focus = ImageLoader.loadImage("/res/buttons/dos.png");

        //RETURN BUTTON
        RectButton returnToLastScene = new RectButton(game);
        returnToLastScene.setImages(stan, focus, 50, 390);
        returnToLastScene.setAudioName("click");
        returnToLastScene.setId(RETURN_TO_LAST_SCENE);

        //PREV BUTTON
        RectButton previous = new RectButton(game);
        previous.setImages(stan, focus, 250, 390);
        previous.setAudioName("click");
        previous.setId(PREVIOUS_SLOT_GROUP);

        //NEXT BUTTON
        RectButton next = new RectButton(game);
        next.setImages(stan, focus, 400, 390);
        next.setAudioName("click");
        next.setId(NEXT_SLOT_GROUP);

        //BUTTON GROUP
        buttonGroup = new ButtonGroup(new Button[]{returnToLastScene, previous, next});
    }

    @Override
    public void update() {
        super.update();

        if (!lockForSave) { //nao deixa atualizar se estiver salvando

            //todo : updatear os outros botoes
            buttonGroup.update();

            if (buttonGroup.getClickedButton() != ButtonGroup.NO_CLICK) {
                if (buttonGroup.getClickedButton() == RETURN_TO_LAST_SCENE) {
                    game.setSceneBasis(game.getPackBasis().get(game.getScene()));
                    reset();
                    return;
                } else if (buttonGroup.getClickedButton() == PREVIOUS_SLOT_GROUP) {
                    slotGroup.decrement();
                } else if (buttonGroup.getClickedButton() == NEXT_SLOT_GROUP) {
                    slotGroup.increment();
                }
            }

            //colocar em metodo ^
            //--------------------------------------------------------------------


            slotGroup.update();

            if (slotGroup.getClickedSlot() != SlotGroup.NO_CLICK) {
                //lockForSave = true; //todo : ver.

                if (slotGroup.getButtons()[slotGroup.getClickedSlot()].getStandardImage() == slotGroup.getStandardButtonImage()) {
                    //create
                    //lock-update
                    lockForSave = true;
                    //thread
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                //no linuxrix, mas ta funcionando no windows tambem
                                String tempPath = save.getFolderPath() + "/" + slotGroup.getClickedSlot() + ".png";
                                System.out.println(tempPath);
                                //image-creation

                                Image scaledInstance = screenshot.getScaledInstance(235, 132, Image.SCALE_SMOOTH);
                                BufferedImage resized = new BufferedImage(235, 132, BufferedImage.TYPE_INT_ARGB);
                                Graphics2D g2d = resized.createGraphics();
                                g2d.drawImage(scaledInstance, 0, 0, null);
                                g2d.dispose();

                                ImageIO.write(resized, "png", new File(tempPath));

                                slotGroup.getButtons()[slotGroup.getClickedSlot()].setStandardImage(resized);

                                //salva no .9
                                save.save(slotGroup.getClickedSlot(), savePack, saveScene);

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
        slotGroup.render(g);

        buttonGroup.render(g);
    }

    @Override
    public void reset() {
        super.reset();
        slotGroup.reset();
        lockForSave = false; //talvez nem precise - ver.

        buttonGroup.reset();
    }

}
