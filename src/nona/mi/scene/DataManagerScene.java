package nona.mi.scene;

import nona.mi.button.Button;
import nona.mi.button.ButtonGroup;
import nona.mi.button.SlotGroup;
import nona.mi.image.BaseImage;
import nona.mi.loader.ScreenshotLoader;
import nona.mi.main.Game;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;



public class DataManagerScene extends Scene {

    private int sceneToReturn; //armazena o id da cena para a qual retornar
    private int packToReturn; //armazena o id do pack para o qual retornar
    private BufferedImage screenshot; //miniatura da cena anterior

    private int tempPack; //armazena o pack para slcd
    private int tempScene; //armazena a cena para slcd

    private int type; //controla se eh slcd

    private SlotGroup slotGroup; //slots
    private ButtonGroup buttonGroup; //prev, next, return

    private boolean lockForSave;
    private boolean lockYnForSave;

    private boolean lockForLoad;
    private boolean lockYnForLoad;

    private boolean lockForDel;
    private boolean lockYnForDel;

    private boolean lockForCopy;
    private boolean lockForPaste;
    private boolean lockYnForCopy;

    private boolean pleaseWait;

    private HashMap<Integer, BaseImage> modes; //imagens as quais identificam qual cena o player estah (slcd)

    public static final int RETURN_TO_LAST_SCENE = -74;
    public static final int PREVIOUS_SLOT_GROUP = -73;
    public static final int NEXT_SLOT_GROUP = -72;
    public static final int YES = -71;
    public static final int NO = -70;
    public static final int SAVE = -69;
    public static final int LOAD = -68;
    public static final int COPY = -67;
    public static final int DEL = -66;
    public static final int MAIN = -65;

    private int tempChosenSlot; //id do slot clicado



    //ESSENCIAL-------------------------------------

    public DataManagerScene(Game game, int sceneId, int buttonsToShow) {
        super(game, sceneId);
        slotGroup = new SlotGroup(game, buttonsToShow);

        lockForSave = false;
        lockYnForSave = true;

        lockForDel = false;
        lockYnForDel = true;

        lockForLoad = false;
        lockYnForLoad = true;

        lockForCopy = false;
        lockForPaste = true;
        lockYnForCopy = true;

    }

    public void createMiscButtons(Button[] miscButtons) {
        buttonGroup = new ButtonGroup(miscButtons);
    }

    public void createSlotImages(BufferedImage standard, BufferedImage focus) {
        slotGroup.setImages(standard, focus);
    }

    public void createSlots(int totalButtons, int row, int column, int x, int y, int spacing, String audioName) {
        slotGroup.createButtons(totalButtons, row, column, x, y, spacing, ScreenshotLoader.loadScreenshots(game.getSave()), audioName);
    }

    public void createModes(HashMap<Integer, BaseImage> modes) {
        this.modes = modes;
    }

    //-----------------------------------------------



    //GS---------------------------------------------

    public void setInfo(int packToReturn, int sceneToReturn, BufferedImage screenshot) {
        this.packToReturn = packToReturn;
        tempPack = packToReturn;
        this.sceneToReturn = sceneToReturn;
        tempScene = sceneToReturn;
        this.screenshot = screenshot;
    }

    public void setSceneToReturn(int sceneToReturn) {
        this.sceneToReturn = sceneToReturn;
    }

    public void setPackToReturn(int packToReturn) {
        this.packToReturn = packToReturn;
    }

    public void setType(int type) {
        this.type = type;
    }

    //--------------------------------------------



    //UPDATES ESPECIFICOS-------------------------

    //update do return, prev e next
    private boolean updateButtons() {
        buttonGroup.update();
        if (buttonGroup.getClickedButton() != Button.NO_CLICK) {
            if (buttonGroup.getClickedButton() == RETURN_TO_LAST_SCENE) {
                if (sceneToReturn == Scene.MAIN_MENU_SCENE) {
                    System.out.println(sceneToReturn);
                    System.out.println("");
                    game.returnToMainMenu();
                } else {
                    game.setDirectScene(sceneToReturn);
                    //retoma uma fala caso tenha sido pausada
                    if (game.getSceneFromCurrentPack(sceneToReturn) instanceof StandardScene) {
                        StandardScene temp = (StandardScene) game.getSceneFromCurrentPack(sceneToReturn);
                        temp.resumeDialogAudio();
                        temp.setLockHCheck(false); //caso tenha apertado h, não volta para stan com dialog escondido
                    }
                }
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



    //TYPE SAVE
    private void updateSave() {
        if (!lockForSave) { //nao deixa atualizar se estiver salvando ou verificando Y/N
            if (updateButtons()) {
                return; //se foi clicado no botao de return, nao atualiza os slots
            }
            updateSlotsForSave();
        } else if (!lockYnForSave) { // para nao clicar durante a escrita dos dados
            updateYnForSave();
        }
    }

    private void updateSlotsForSave() {
        slotGroup.update();
        if (slotGroup.getClickedSlot() != Button.NO_CLICK) {
            lockForSave = true;
            if (slotGroup.getButtons()[slotGroup.getClickedSlot()].getStandardImage() == slotGroup.getStandardButtonImage()) {
                pleaseWait = true;
                save();
            } else {
                //clicou em slot com progresso
                lockYnForSave = false;
            }
        }
    }

    private void updateYnForSave() {
        game.getYn().update();
        if (game.getYn().getClickedButton() != Button.NO_CLICK) {
            if (game.getYn().getClickedButton() == YES) {
                lockYnForSave = true;
                pleaseWait = true;
                save();
            } else if (game.getYn().getClickedButton() == NO) {
                game.getYn().reset();
                lockForSave = false;
                lockYnForSave = true;
            }
        }
    }



    //TYPE LOAD
    private void updateLoad() {
        if (!lockForLoad) {
            if (updateButtons()) {
                return;
            }
            updateSlotsForLoad();
        } else if (!lockYnForLoad) {
            updateYnForLoad();
        }
    }

    private void updateSlotsForLoad() {
        slotGroup.update();
        if ((slotGroup.getClickedSlot() != Button.NO_CLICK) && (slotGroup.getButtons()[slotGroup.getClickedSlot()].getStandardImage() != slotGroup.getStandardButtonImage())) {
            tempChosenSlot = slotGroup.getClickedSlot();
            tempPack = game.getSave().getPackOfSlot(tempChosenSlot);
            tempScene = game.getSave().getSceneOfSlot(tempChosenSlot);
            lockForLoad = true;
            lockYnForLoad = false;
        }
    }

    private void updateYnForLoad() {
        game.getYn().update();
        if (game.getYn().getClickedButton() != Button.NO_CLICK) {
            if (game.getYn().getClickedButton() == YES) {
                if (packToReturn != tempPack) {
                    //nao esta lendo alguma cena do pack atual
                    game.loadPack(tempPack, tempScene); // < reseta esta cena (dms)
                } else {
                    //a cena do slot esta no pack atual

                    //RESETA A CENA ANTERIOR (VINDA DE STANDARDSCENE, POR EXEMPLO)
                    //SE A CENA ANTERIOR FOR O MAINMENU NAO PRECISA RESETAR, POIS
                    //ISSO EH FEITO QUANDO O MAIN VEM PARA A TELA DE LOAD
                    if (sceneToReturn != Scene.MAIN_MENU_SCENE) {
                        game.getSceneFromCurrentPack(sceneToReturn).reset();
                    }

                    //coloca a cena em sceneBasis
                    game.setDirectScene(tempScene); // < reseta esta cena (dms)
                }
                //preenche o tracer com os dados do slot especifico
                game.getSave().initTracer(tempChosenSlot);
            } else if (game.getYn().getClickedButton() == NO) {
                game.getYn().reset();
                lockForLoad = false;
                lockYnForLoad = true;
            }
        }

    }



    //TYPE COPY
    private void updateCopy() {
        if (!lockForCopy) {
            if (updateButtons()){
                return;
            }
            updateSlotsForCopy();
        } else if (!lockForPaste){
            if (updateButtons()){
                return;
            }
            updateSlotsForPaste();
        } else if (!lockYnForCopy) {
            updateYnForCopy(); //here
        }
    }

    private void updateSlotsForCopy() {
        slotGroup.update();
        if (slotGroup.getClickedSlot() != Button.NO_CLICK) {
            if (!(slotGroup.getButtons()[slotGroup.getClickedSlot()].getStandardImage() == slotGroup.getStandardButtonImage())) {

                //clicou no lugar certo, slot com progresso

                lockForCopy = true;
                lockForPaste = false;

                //pega os dados do slot
                int slotId = slotGroup.getClickedSlot();
                tempPack = game.getSave().getPackOfSlot(slotId);
                tempScene = game.getSave().getSceneOfSlot(slotId);
                screenshot = slotGroup.getSlotImage(slotId);
            }
        }
    }

    private void updateSlotsForPaste() {
        slotGroup.update();
        if (slotGroup.getClickedSlot() != Button.NO_CLICK) {
            if (slotGroup.getButtons()[slotGroup.getClickedSlot()].getStandardImage() == slotGroup.getStandardButtonImage()) {
                //slot vazio
                pleaseWait = true;
                save();
            } else {
                //slot com dados
                lockYnForCopy = false;
            }
            lockForPaste = true;
        }
    }

    private void updateYnForCopy() {
        game.getYn().update();
        if (game.getYn().getClickedButton() != Button.NO_CLICK) {
            if (game.getYn().getClickedButton() == YES) {
                lockYnForCopy = true;
                pleaseWait = true;
                save();
            } else if (game.getYn().getClickedButton() == NO) {
                game.getYn().reset();
                lockForCopy = false;
                lockYnForCopy = true;
            }
        }
    }



    //TYPE DEL
    private void updateDel() {
        if (!lockForDel) {
            if (updateButtons()) {
                return;
            }
            updateSlotsForDel(); //da lock aqui se clicar no lugar certo > thread unlocks
        } else if (!lockYnForDel) {
            updateYnForDel();
        }
    }

    private void updateSlotsForDel() {
        slotGroup.update();
        if ((slotGroup.getClickedSlot() != Button.NO_CLICK) && (slotGroup.getButtons()[slotGroup.getClickedSlot()].getStandardImage() != slotGroup.getStandardButtonImage())) {
            lockForDel = true;
            lockYnForDel = false;
        }
    }

    private void updateYnForDel() {
        game.getYn().update();
        if (game.getYn().getClickedButton() != Button.NO_CLICK) {
            if (game.getYn().getClickedButton() == YES) {
                lockYnForDel = true; //nao deixa o infeliz iniciar mais de 1 thread
                pleaseWait = true;
                del();
            } else if (game.getYn().getClickedButton() == NO) {
                game.getYn().reset();
                lockForDel = false;
                lockYnForDel = true;
            }
        }
    }

    //--------------------------------------------



    //ACOES ESPECIFICAS---------------------------

    private void save() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    //Thread.sleep(1000);

                    //obtem o caminho da imagem de acordo com o os
                    String tempPath = ""; //todo atomic ref.
                    if (game.getSave().getOs().startsWith("w")) {
                        tempPath = game.getSave().getFolderPath() + "\\" + slotGroup.getClickedSlot() + ".png";
                    } else if (game.getSave().getOs().startsWith("l")) {
                        tempPath = game.getSave().getFolderPath() + "/" + slotGroup.getClickedSlot() + ".png";
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
                    game.getSave().save(slotGroup.getClickedSlot(), tempPack, tempScene);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                pleaseWait = false;
                lockForSave = false;
                lockForCopy = false;
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
                    if (game.getSave().getOs().startsWith("w")) {
                        tempPath = game.getSave().getFolderPath() + "\\" + slotGroup.getClickedSlot() + ".png";
                    } else if (game.getSave().getOs().startsWith("l")) {
                        tempPath = game.getSave().getFolderPath() + "/" + slotGroup.getClickedSlot() + ".png";
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
                game.getSave().delete(slotGroup.getClickedSlot());

                //tira a mensagem de wait
                pleaseWait = false;

                //desbloqueia o update da cena
                lockForDel = false;

            }
        });
        thread.start();
    }

    private void renderShadow(Graphics g) {
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, game.getWidth(), game.getHeight());
    }

    private void renderPleaseWait(Graphics g) {
        g.setColor(Color.RED);
        g.drawString("PLEASE WAIT", 50, 50);
    }

    private void renderModes(Graphics g) {
        modes.get(type).render(g);
    }

    //--------------------------------------------



    //URR-----------------------------------------

    @Override
    public void updateScene() {
        if (type == SAVE) {
            updateSave();
        } else if (type == LOAD) {
            updateLoad();
        } else if (type == COPY) {
            updateCopy();
        } else if (type == DEL) {
            updateDel();
        }
    }

    @Override
    public void renderScene(Graphics g) {
        renderModes(g);
        slotGroup.render(g);
        buttonGroup.render(g);
        if (!lockYnForSave || !lockYnForDel || !lockYnForCopy || !lockYnForLoad) {
            renderShadow(g);
            game.getYn().render(g);
        }
        if (pleaseWait) {
            renderShadow(g);
            renderPleaseWait(g);
        }
    }

    @Override
    public void reset() {
        super.reset();
        slotGroup.reset();

        lockForSave = false;
        lockYnForSave = true;

        lockForLoad = false;
        lockYnForLoad = true;

        lockForDel = false;
        lockYnForDel = true;

        lockForCopy = false;
        lockForPaste = true;
        lockYnForCopy = true;

        pleaseWait = false;

        buttonGroup.reset();
        game.getYn().reset();
    }

    //--------------------------------------------

}
