package nona.mi.scene;

import nona.mi.button.Button;
import nona.mi.button.ButtonGroup;
import nona.mi.button.SlotGroup;
import nona.mi.image.BaseImage;
import nona.mi.main.Game;
import nona.mi.save.Save;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;

//todo : DataManagerScene
//todo : resetar a cena anterior se voltar para algum lugar que nao seja ela
//todo : retornar para o main >> scene
public class SaveMenuScene extends Scene {

    private int saveScene;
    private int savePack;
    private BufferedImage screenshot;

    private Save save;

    private int type;

    private SlotGroup slotGroup;
    private ButtonGroup buttonGroup;
    private ButtonGroup yn;

    public static final int RETURN_TO_LAST_SCENE = 0;
    public static final int PREVIOUS_SLOT_GROUP = 1;
    public static final int NEXT_SLOT_GROUP = 2;
    public static final int YES = 3;
    public static final int NO = 4;
    public static final int SAVE = 5;
    public static final int LOAD = 6;
    public static final int COPY = 7;
    public static final int DEL = 8;
    public static final int MAIN = 9;

    private boolean lockForSave;
    private boolean lockYnForSave;

    private boolean lockForDel;
    private boolean lockYnForDel;

    private boolean lockForCopy;
    private boolean lockForPaste;
    private boolean lockYnForCopy;

    private boolean pleaseWait;

    private HashMap<Integer, BaseImage> modes;



    //ESSENCIAL-------------------------------------

    public SaveMenuScene(Game game, Save save, int buttonsToShow) {
        super(game);
        this.save = save;
        slotGroup = new SlotGroup(game, buttonsToShow);

        lockForSave = false;
        lockYnForSave = true;

        lockForDel = false;
        lockYnForDel = true;

        lockForCopy = false;
        lockForPaste = true;
        lockYnForCopy = true;

    }

    public void createMiscButtons(Button[] miscButtons) {
        buttonGroup = new ButtonGroup(miscButtons);
    }

    public void createYn(Button[] ynButtons) {
        yn = new ButtonGroup(ynButtons);
    }

    public void createSlotImages(BufferedImage standard, BufferedImage focus) {
        slotGroup.setImages(standard, focus);
    }

    public void createSlots(int totalButtons, int row, int column, int x, int y, int spacing) {
        slotGroup.createButtons(totalButtons, row, column, x, y, spacing, game.getScreenshots());
    }

    public void createModes(HashMap<Integer, BaseImage> modes) {
        this.modes = modes;
    }

    //-----------------------------------------------



    //GS---------------------------------------------

    public void setInfo(int savePack, int saveScene, BufferedImage screenshot) {
        this.savePack = savePack;
        this.saveScene = saveScene;
        this.screenshot = screenshot;
    }

    public void setType(int type) {
        this.type = type;
    }

    //--------------------------------------------



    //UPDATES ESPECIFICOS-------------------------

    //update do return, prev e next
    private boolean updateButtons() {
        buttonGroup.update();
        if (buttonGroup.getClickedButton() != ButtonGroup.NO_CLICK) {
            if (buttonGroup.getClickedButton() == RETURN_TO_LAST_SCENE) {

                game.setSceneBasis(game.getPackBasis().get(game.getScene()));

                reset();

                //retoma uma fala caso tenha sido pausada
                if (game.getPackBasis().get(saveScene) instanceof StandardScene) {
                    StandardScene temp = (StandardScene) game.getPackBasis().get(saveScene);
                    temp.resumeDialogAudio();
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
        } else if (!lockYnForSave) { //todo : ??? acho que bloqueia os dois para nao clicar durante a escrita dos dados
            updateYnForSave();
        }
    }

    private void updateSlotsForSave() {
        slotGroup.update();
        if (slotGroup.getClickedSlot() != SlotGroup.NO_CLICK) {
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
        yn.update();
        if (yn.getClickedButton() == YES) {
            lockYnForSave = true;
            pleaseWait = true;
            save();
        } else if (yn.getClickedButton() == NO) {
            yn.reset();
            lockForSave = false;
            lockYnForSave = true;
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
            updateYnForCopy();
        }
    }

    private void updateSlotsForCopy() {
        slotGroup.update();
        if (slotGroup.getClickedSlot() != SlotGroup.NO_CLICK) {
            if (!(slotGroup.getButtons()[slotGroup.getClickedSlot()].getStandardImage() == slotGroup.getStandardButtonImage())) {

                //clicou no lugar certo, slot com progresso

                lockForCopy = true;
                lockForPaste = false;

                //pega os dados do slot
                int slotId = slotGroup.getClickedSlot();
                savePack = save.getPackOfSlot(slotId);
                saveScene = save.getSceneOfSlot(slotId);
                screenshot = slotGroup.getButtons()[slotId].getStandardImage(); //TODO : ENCAPSULAR LINHAS COMO ESSA!!!

                //todo : setText << deixar o padrao no reset
            }
        }
    }

    private void updateSlotsForPaste() {
        slotGroup.update();
        if (slotGroup.getClickedSlot() != SlotGroup.NO_CLICK) {
            if (slotGroup.getButtons()[slotGroup.getClickedSlot()].getStandardImage() == slotGroup.getStandardButtonImage()) {
                //slot vazio
                //save
                pleaseWait = true;
                lockForPaste = true;
                save();
            } else {
                //slot com dados
            }
        }
    }

    private void updateYnForCopy() {
        yn.update();
        if (yn.getClickedButton() == YES) {
            //
        } else if (yn.getClickedButton() == NO) {
            //
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
        if ((slotGroup.getClickedSlot() != SlotGroup.NO_CLICK) && (slotGroup.getButtons()[slotGroup.getClickedSlot()].getStandardImage() != slotGroup.getStandardButtonImage())) {
            lockForDel = true;
            lockYnForDel = false;
        }
    }

    private void updateYnForDel() {
        yn.update();
        if (yn.getClickedButton() == YES) {
            lockYnForDel = true; //nao deixa o infeliz iniciar mais de 1 thread
            pleaseWait = true;
            del();
        } else if (yn.getClickedButton() == NO) {
            yn.reset();
            lockForDel = false;
            lockYnForDel = true;
        }
    }

    //--------------------------------------------



    //ACOES ESPECIFICAS---------------------------

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
        if (pleaseWait) {
            g.setColor(Color.RED);
            g.drawString("PLEASE WAIT", 50, 50);
        }
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

        } else if (type == COPY) {

        } else if (type == DEL) {
            updateDel();
        }

    }

    @Override
    public void renderScene(Graphics g) {
        renderModes(g);
        slotGroup.render(g);
        buttonGroup.render(g);
        if (!lockYnForSave || !lockYnForDel) {
            renderShadow(g);
            yn.render(g);
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

        lockForDel = false;
        lockYnForDel = true;

        pleaseWait = false;

        buttonGroup.reset();
        yn.reset();
    }

    //--------------------------------------------

}
