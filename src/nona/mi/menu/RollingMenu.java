package nona.mi.menu;

import nona.mi.efx.Fade;
import nona.mi.image.Coordinates;
import nona.mi.image.ImageEfx;
import nona.mi.loader.ImageLoader;
import nona.mi.main.Thorns;
import nona.mi.save.Save;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class RollingMenu {

    private Thorns thorns;

    private BufferedImage emptySlot;
    private BufferedImage filledSlot;

    private int[] visibleSlots = {0, 1, 2, 3, 4, 5, 6, 7};
    private int pointer;

    private int imageHeight;

    private Save save;

    private Menu containerMenu;
    private Menu saveMenu;
    private Menu deleteMenu;
    private Menu loadMenu;
    private Menu copyMenu;
    private boolean showMenu;
    private boolean rollingMenuFocus;

    private String old; //representa o slot especificado pelo pointer, antes de alterar
    private String copy;

    private boolean lockCopy;
    private boolean lockMode; //nao deixa apertar left/right durante o processo de copia -o modo nao pode ser alterado

    private int mode;
    public static final int SAVE_MODE = 0;
    public static final int LOAD_MODE = 1;
    public static final int COPY_MODE = 2;
    public static final int DELETE_MODE = 3;
    private final int TOTAL_MODES = 4;

    private boolean slotChangeAnimation;
    private ImageEfx slotAnimation;

    private boolean fadeinAnimation;
    private Fade fadein;

    private boolean animating;


    public RollingMenu(Thorns thorns) {
        this.thorns = thorns;
        mode = SAVE_MODE;
        emptySlot = ImageLoader.loadImage("/res/menu/emptySlot.png");
        filledSlot = ImageLoader.loadImage("/res/menu/filledSlot.png");
        BufferedImage slotEfx = ImageLoader.loadImage("/res/menu/slotEfx.png");

        imageHeight = filledSlot.getHeight();
        pointer = 3;
        save = new Save();

        saveMenu = new Menu(thorns, thorns.getChoicebg(), thorns.getFontDataBase(), thorns.getPointer(), Menu.STYLE_HORIZONTAL);
        saveMenu.createOptions(10, 10, 10, "WRITE_CANCEL");

        deleteMenu = new Menu(thorns, thorns.getChoicebg(), thorns.getFontDataBase(), thorns.getPointer(), Menu.STYLE_HORIZONTAL);
        deleteMenu.createOptions(10, 10, 10, "DELETE_CANCEL");

        loadMenu = new Menu(thorns, thorns.getChoicebg(), thorns.getFontDataBase(), thorns.getPointer(), Menu.STYLE_HORIZONTAL);
        loadMenu.createOptions(10, 10, 10, "LOAD_CANCEL");

        copyMenu = new Menu(thorns, thorns.getChoicebg(), thorns.getFontDataBase(), thorns.getPointer(), Menu.STYLE_HORIZONTAL);
        copyMenu.createOptions(10, 10, 10, "COPY_CANCEL");

        containerMenu = deleteMenu;
        rollingMenuFocus = true;
        old = "";
        copy = "";
        lockCopy = false;
        lockMode = false;

        slotAnimation = new ImageEfx(thorns, slotEfx, new Coordinates(0, pointer * imageHeight));
        slotAnimation.setAlpha(ImageEfx.SOLID, 0.05f);

        animating = false;

        fadein = new Fade(thorns, Fade.TRANSPARENT, Fade.FAST);
        fadeinAnimation = false;
    }

    public void update() {

        boolean up = thorns.isUp();
        boolean down = thorns.isDown();
        boolean space = thorns.isSpace(); //todo anim. confirmacao
        boolean left = thorns.isLeft();
        boolean right = thorns.isRight();

        if (!animating) {
            if (rollingMenuFocus) {
                rollMenu(up, down);
                changeMode(left, right);
                managePressedOption(space);
            } else {
                updateContainerMenu();
            }
        } else {
            if (slotChangeAnimation) {
                slotAnimation.update();
                if (slotAnimation.getEndAlphaAnimation()) {
                    slotChangeAnimation = false;
                    slotAnimation.reset();
                    animating = false;
                }
            } else if (fadeinAnimation) {
                fadein.update();
                if (fadein.getEndAnimation()) {
                    fadeinAnimation = false;
                    fadein.reset();

                    //PARAMETROS DO SAVE -- load nao pode ter resetado os textos
                    int[] hifen = new int[3];
                    int cont = 0;
                    for (int i = 0; i < old.length(); i++) {
                        if (old.charAt(i) == '-'){
                            hifen[cont] = i;
                            cont++;
                        }
                    }

                    //slot-filling-pack-scene
                    int preFillId = 0;
                    int prePackId = 1;
                    int preSceneId = 2;

                    int pack = Integer.parseInt(old.substring(hifen[prePackId] + 1, hifen[preSceneId]));
                    int scene = Integer.parseInt(old.substring(hifen[preSceneId] + 1));

                    //VERIFICA SE O LOAD ESTA CHAMANDO A CENA DO PACK ATUAL
                    //EVITA O CARREGAMENTO DO MESMO PACK VARIAS VEZES
                    if (thorns.getPack() == pack){
                        thorns.setDirectScene(thorns.getPackBasis().get(scene));
                    } else {
                        thorns.nextScene(pack, scene);
                    }

                    reset();
                    animating = false; //todo : ver um local seguro para isso aqui
                    thorns.setShowRollingMenu(false);
                }
            }
        }

    }

    private void rollMenu(boolean up, boolean down){
        int increment = 0;
        if (up) {
            increment = -1;
        }
        if (down) {
            increment = 1;
        }

        if (up || down) {
            for (int i = 0; i < visibleSlots.length; i++) {
                visibleSlots[i] = visibleSlots[i] + increment;
                if (visibleSlots[i] > save.getSlots().length - 1) {
                    visibleSlots[i] = 0;
                } else if (visibleSlots[i] < 0) {
                    visibleSlots[i] = save.getSlots().length - 1;
                }
            }
        }
    }

    private void changeMode(boolean left, boolean right){
        if (!lockMode) {
            if (left) {
                mode--;
            }
            if (right) {
                mode++;
            }

            if (left || right) {
                if (mode > TOTAL_MODES - 1) { // -1 pois eh base 0
                    mode = 0;
                } else if (mode < 0) {
                    mode = TOTAL_MODES - 1;
                }
            }
        }
    }

    private void managePressedOption(boolean space) {
        if (space) {
            old = save.getSlots()[visibleSlots[pointer]]; //returns x-x-x-x
            if (mode == SAVE_MODE) {
                if (old.charAt(Save.IMAGE_SLOT_ID) == Save.FILLED) {
                    containerMenu = saveMenu;
                    gotoMenu();
                } else {
                    String temp = "" + old.charAt(Save.SLOT_ID) + "-" + Save.FILLED + "-" + thorns.getPack() + "-" + thorns.getScene();
                    save.getSlots()[visibleSlots[pointer]] = temp;
                    save.save();
                    animating = true;
                    slotChangeAnimation = true;
                }
            } else if (mode == LOAD_MODE) {
                if (old.charAt(Save.IMAGE_SLOT_ID) == Save.FILLED) {
                    containerMenu = loadMenu;
                    gotoMenu();
                }
            } else if (mode == COPY_MODE) {
                if (!lockCopy && old.charAt(Save.IMAGE_SLOT_ID) == Save.FILLED) {
                    lockCopy = true;
                    copy = old;
                    lockMode = true;
                } else if (lockCopy) {
                    if (old.charAt(Save.IMAGE_SLOT_ID) == Save.EMPTY) {
                        String temp = "" + visibleSlots[pointer] + copy.substring(1); // 1 para excluir o primeiro caractere (0)
                        save.getSlots()[visibleSlots[pointer]] = temp;
                        save.save();
                        lockCopy = false;
                        lockMode = false;
                    } else {
                        containerMenu = copyMenu;
                        gotoMenu();
                    }
                }
            } else if (mode == DELETE_MODE) {
                if (old.charAt(Save.IMAGE_SLOT_ID) == Save.FILLED) {
                    containerMenu = deleteMenu;
                    gotoMenu();
                }
            }
        }
    }

    private void updateContainerMenu(){
        containerMenu.update();
        if (containerMenu.isPressed()){
            if (mode == SAVE_MODE) {
                if (containerMenu.getChosenOptionAsString().equals("WRITE")) {
                    String temp = "" + old.charAt(Save.SLOT_ID) + "-" + Save.FILLED + "-" + thorns.getPack() + "-" + thorns.getScene();
                    save.getSlots()[visibleSlots[pointer]] = temp;
                    save.save();
                }
            } else if (mode == DELETE_MODE) {
                if (containerMenu.getChosenOptionAsString().equals("DELETE")) {
                    save.getSlots()[visibleSlots[pointer]] = "" + visibleSlots[pointer] + "-0-0-0";
                    save.save();
                }
            } else if (mode == LOAD_MODE) {
                if (containerMenu.getChosenOptionAsString().equals("LOAD")) {
                    animating = true;
                    fadeinAnimation = true;
                    return;
                }
            } else if (mode == COPY_MODE) {
                if (containerMenu.getChosenOptionAsString().equals("COPY")) {
                    String temp = "" + visibleSlots[pointer] + copy.substring(1); // 1 para excluir o primeiro caractere (0)
                    save.getSlots()[visibleSlots[pointer]] = temp;
                    save.save();
                }
                lockCopy = false;
                lockMode = false;
            }
            reset();
        }
    }


    public void render(Graphics g){
        for (int i = 0; i < visibleSlots.length; i++) {
            char temp = save.getSlots()[visibleSlots[i]].charAt(Save.IMAGE_SLOT_ID);
            if (temp == Save.EMPTY){
                g.drawImage(emptySlot, 0, i*imageHeight, null);
            } else if (temp == Save.FILLED){
                g.drawImage(filledSlot, 0, i*imageHeight, null);
            }
            //todo : x y
        }
        if (showMenu){
            containerMenu.render(g);
        }

        //todo : apagar os testes
        //test
        g.setColor(Color.WHITE);
        g.drawString(String.valueOf(mode), 5, 15);

        //test2
        g.drawString(String.valueOf(lockCopy), 100, 15);
        g.drawString(String.valueOf(visibleSlots[pointer]), 150, 15);

        if (slotChangeAnimation) {
            slotAnimation.render(g);
        }

        if (fadeinAnimation) {
            fadein.render(g);
        }

    }

    private void gotoMenu(){
        showMenu = true;
        rollingMenuFocus = false;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public boolean isAnimating() {
        return animating;
    }

    public void reset(){
        showMenu = false;
        rollingMenuFocus = true;

        old = "";
        copy = "";

        lockCopy = false;
        lockMode = false;

        saveMenu.reset();
        loadMenu.reset();
        copyMenu.reset();
        deleteMenu.reset();
    }

}