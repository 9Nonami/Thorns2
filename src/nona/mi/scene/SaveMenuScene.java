package nona.mi.scene;

import nona.mi.button.Button;
import nona.mi.button.ButtonGroup;
import nona.mi.button.SlotGroup;
import nona.mi.button.RectButton;
import nona.mi.loader.ImageLoader;
import nona.mi.main.Game;
import nona.mi.save.Save;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

//todo : DataManagerScene
//todo : texto "saving" no lock4save
// todo : ver se para deixar mais abstrato seria legal dar setButtonGroup e setSlotGroup, em polimorquaza
public class SaveMenuScene extends Scene { //todo : resetar a cena anterior se voltar para algum lugar que nao seja ela

    private int saveScene;
    private int savePack;
    private BufferedImage screenshot;

    private SlotGroup slotGroup;
    private Save save;
    private boolean lockForSave;

    private ButtonGroup buttonGroup;
    private final int RETURN_TO_LAST_SCENE = 0;
    private final int PREVIOUS_SLOT_GROUP = 1;
    private final int NEXT_SLOT_GROUP = 2;
    private boolean lockForOverwrite;

    private ButtonGroup yn;
    private final int YES = 3;
    private final int NO = 4;

    private int type;
    public static final int SAVE = 5;
    public static final int LOAD = 6;
    public static final int COPY = 7;
    public static final int DEL = 8;
    //main

    private boolean lockForDel;
    private boolean lockYnForDel;


    public SaveMenuScene(Game game, Save save, int buttonsToShow) {
        super(game);
        this.save = save;
        slotGroup = new SlotGroup(game, buttonsToShow);
        lockForSave = false;
        lockForOverwrite = true;

        lockForDel = false;
        lockYnForDel = true;

        createButtons();
        createYnButtons();
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

    private void createYnButtons() {
        //todo : del e pegar as img certas
        BufferedImage stan = ImageLoader.loadImage("/res/buttons/uno.png");
        BufferedImage focus = ImageLoader.loadImage("/res/buttons/dos.png");

        //YES
        RectButton yes = new RectButton(game);
        yes.setImages(stan, focus, 150, 50);
        yes.setAudioName("click");
        yes.setId(YES);

        //NO
        RectButton no = new RectButton(game);
        no.setImages(stan, focus, 350, 50);
        no.setAudioName("click"); //TODO : DEIXAR ESSE CLICK PUBLICO!!!!!!! <<<<<<<<<<<<<<<<<<<<
        no.setId(NO);

        //BUTTON GROUP
        yn = new ButtonGroup(new Button[]{yes, no});
    }


    //--------------------------------------------

    private void updateSave() {
        if (!lockForSave) { //nao deixa atualizar se estiver salvando ou verificando Y/N

            if (updateButtons()) {
                return; //se foi clicado no botao de return, nao atualiza os slots
            }

            updateSlots();

        } else if (!lockForOverwrite) {
            updateYn();
        }
    }

    private void updateDel() {
        //ver se image eh padrao
        //de for, confirmar acao de del
        //deletar no .9 e colocar a stan como imagem

        if (!lockForDel) {
            if (updateButtons()) {
                return;
            }
            updateSlotsForDel(); //da lock aqui se clicar no lugar certo > thread unlocks
        } else if (!lockYnForDel) {
            updateYnForDel();
        }

    }

    //update do return, prev e next
    private boolean updateButtons() {
        buttonGroup.update();
        if (buttonGroup.getClickedButton() != ButtonGroup.NO_CLICK) {
            if (buttonGroup.getClickedButton() == RETURN_TO_LAST_SCENE) {
                game.setSceneBasis(game.getPackBasis().get(game.getScene()));
                reset();
                return true; //para nao atualizar os slots
            } else if (buttonGroup.getClickedButton() == PREVIOUS_SLOT_GROUP) {
                if (slotGroup.getStartIncrement() > 0) { // 0 = first slot. there is nothing before it.
                    slotGroup.decrement();
                }
            } else if (buttonGroup.getClickedButton() == NEXT_SLOT_GROUP) { //12 - 6
                if (slotGroup.getStartIncrement() < slotGroup.getTotalButtons() - slotGroup.getButtonsToShow()) {
                    slotGroup.increment();
                }
            }
        }
        return  false;
    }

    //slots - save
    private void updateSlots() {
        slotGroup.update();
        if (slotGroup.getClickedSlot() != SlotGroup.NO_CLICK) {
            lockForSave = true;
            if (slotGroup.getButtons()[slotGroup.getClickedSlot()].getStandardImage() == slotGroup.getStandardButtonImage()) {
                //clicou em slot vazio
                save();
            } else {
                //clicou em slot com progresso
                lockForOverwrite = false;
            }
        }
    }

    private void updateSlotsForDel() {
        slotGroup.update();
        if ((slotGroup.getClickedSlot() != SlotGroup.NO_CLICK) && (slotGroup.getButtons()[slotGroup.getClickedSlot()].getStandardImage() != slotGroup.getStandardButtonImage())) {
            lockForDel = true;
            lockYnForDel = false;
        }
    }

    //confirmacao yes | no
    private void updateYn() {
        yn.update();
        if (yn.getClickedButton() == YES) {
            lockForOverwrite = true;
            save();
        } else if (yn.getClickedButton() == NO) {
            yn.reset();
            lockForSave = false;
            lockForOverwrite = true;
        }
    }

    private void updateYnForDel() {
        yn.update();
        if (yn.getClickedButton() == YES) {
            //del
            //thread = unlocks
            lockYnForDel = true; //nao deixa o infeliz iniciar mais de 1 thread
            del();
            //setWaitMessage
        } else if (yn.getClickedButton() == NO) {
            yn.reset();
            lockForDel = false;
            lockYnForDel = true;
        }
    }

    //--------------------------------------------

    @Override
    public void update() {
        super.update();

        if (type == SAVE) {
            updateSave();
        } else if (type == LOAD) {

        } else if (type == COPY) {

        } else if (type == DEL) {
            updateDel();
        }

    }

    private void save() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    //obtem o caminho da imagem de acordo com o os
                    String tempPath = "";
                    if (save.getOs().startsWith("w")) {
                        tempPath = save.getFolderPath() + "\\" + slotGroup.getClickedSlot() + ".png";
                    } else if (save.getOs().startsWith("l")) {
                        tempPath = save.getFolderPath() + "/" + slotGroup.getClickedSlot() + ".png";
                    }

                    //cria e salva a imagem no disco
                    Image scaledInstance = screenshot.getScaledInstance(235, 132, Image.SCALE_SMOOTH);
                    BufferedImage resized = new BufferedImage(235, 132, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g2d = resized.createGraphics();
                    g2d.drawImage(scaledInstance, 0, 0, null);
                    g2d.dispose();
                    ImageIO.write(resized, "png", new File(tempPath));

                    //salva a imagem no slot
                    slotGroup.saveImage(resized);

                    //salva no .9
                    save.save(slotGroup.getClickedSlot(), savePack, saveScene);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                lockForSave = false;
                //render
            }
        });
        thread.start();
    }

    private void del() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                //coloca a imagem padrao
                //slotGroup.redefineStandardImage(int buttonID, BufferedImage nImage);
                slotGroup.deleteImage();
                //slotGroup.getButtons()[slotGroup.getClickedSlot()].setStandardImage(slotGroup.getStandardButtonImage());

                //deleta a imagem salva no disco
                try {

                    //obtem o caminho da imagem de acordo com o os
                    String tempPath = "";
                    if (save.getOs().startsWith("w")) {
                        tempPath = save.getFolderPath() + "\\" + slotGroup.getClickedSlot() + ".png";
                    } else if (save.getOs().startsWith("l")) {
                        tempPath = save.getFolderPath() + "/" + slotGroup.getClickedSlot() + ".png";
                    }

                    //deleta a imagem
                    File file = new File(tempPath);
                    if (file.exists()) {
                        System.out.println("existe");
                        file.delete();
                    } else {
                        System.exit(0);
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                //deleta os dados do .9
                save.delete(slotGroup.getClickedSlot());


                //desbloqueia o update da cena
                lockForDel = false;
            }
        });
        thread.start();
    }

    @Override
    public void renderScene(Graphics g) {
        slotGroup.render(g);
        buttonGroup.render(g);
        if (!lockForOverwrite || !lockYnForDel) {
            g.setColor(new Color(0, 0, 0, 180)); //todo : criar metodo
            g.fillRect(0, 0, game.getWidth(), game.getHeight());
            yn.render(g);
        }
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public void reset() {
        super.reset();
        slotGroup.reset();

        lockForSave = false; //talvez nem precise - ver.
        lockForOverwrite = true;

        lockForDel = false;
        lockYnForDel = true;

        buttonGroup.reset();
        yn.reset();
    }

}
