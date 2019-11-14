package nona.mi.scene; //todo : mudar para nona.mi.menu

import nona.mi.loader.ImageLoader;
import nona.mi.main.Thorns;
import nona.mi.menu.Menu;
import nona.mi.save.Replacer;
import nona.mi.save.Save;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class RollingMenuBasis {

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
    private boolean showMenu;
    private boolean rollingMenuFocus;

    private String old; //representa o slot especificado pelo pointer, antes de alterar

    private int mode;
    public static final int SAVE_MODE = 0;
    public static final int LOAD_MODE = 1;
    public static final int COPY_MODE = 2;
    public static final int DELETE_MODE = 3;


    public RollingMenuBasis(Thorns thorns, int mode){
        this.thorns = thorns;
        this.mode = mode;
        emptySlot = ImageLoader.loadImage("/res/menu/emptySlot.png");
        filledSlot = ImageLoader.loadImage("/res/menu/filledSlot.png");
        imageHeight = filledSlot.getHeight();
        pointer = 3;
        save = new Save();

        saveMenu = new Menu(thorns, thorns.getChoicebg(), thorns.getFontDataBase(), thorns.getPointer(), Menu.STYLE_HORIZONTAL);
        saveMenu.createOptions(10, 10, 10, "WRITE_CANCEL");

        deleteMenu = new Menu(thorns, thorns.getChoicebg(), thorns.getFontDataBase(), thorns.getPointer(), Menu.STYLE_HORIZONTAL);
        deleteMenu.createOptions(10, 10, 10, "DELETE_CANCEL");

        containerMenu = deleteMenu;
        rollingMenuFocus = true;
    }

    public void update() {

        boolean up = thorns.isUp();
        boolean down = thorns.isDown();
        boolean space = thorns.isSpace(); //todo anim. confirmacao
        //
        boolean left = thorns.isLeft();
        boolean right = thorns.isRight();


        if (rollingMenuFocus) {

            int increment = 0;
            if (up) {
                increment = -1;
            }
            if (down) {
                increment = 1;
            }

            //ROLL
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

            //SAVE
            if (space) {

                old = save.getSlots()[visibleSlots[pointer]]; //returns x-x-x-x

                if (mode == SAVE_MODE) {

                    if (old.charAt(Save.IMAGE_SLOT_ID) == '1') {
                        containerMenu = saveMenu;
                        gotoMenu();
                    } else {
                        save.getSlots()[visibleSlots[pointer]] = Replacer.replace(old, Save.IMAGE_SLOT_ID, '1');
                        save.save();
                    }

                } else if (mode == LOAD_MODE) {

                } else if (mode == COPY_MODE) {

                } else if (mode == DELETE_MODE) {
                    if (old.charAt(Save.IMAGE_SLOT_ID) == '1') {
                        containerMenu = deleteMenu;
                        gotoMenu();
                    }
                }

            }

        } else {

            containerMenu.update();

            if (containerMenu.isPressed()){

                if (containerMenu.getChosenOptionAsString().equals("WRITE")) {
                    save.getSlots()[visibleSlots[pointer]] = Replacer.replace(old, Save.IMAGE_SLOT_ID, '1');
                    save.save();
                } else if (containerMenu.getChosenOptionAsString().equals("DELETE")) {
                    save.getSlots()[visibleSlots[pointer]] = "" + visibleSlots[pointer] + "-0-0-0";
                    save.save();
                }
                gotoRolling();
            }

        }
    }

    public void render(Graphics g){
        for (int i = 0; i < visibleSlots.length; i++) {
            char temp = save.getSlots()[visibleSlots[i]].charAt(2);
            if (temp == '0'){
                g.drawImage(emptySlot, 0, i*imageHeight, null);
            } else if (temp == '1'){
                g.drawImage(filledSlot, 0, i*imageHeight, null);
            }
            //todo : x y
        }
        if (showMenu){
            containerMenu.render(g);
        }

    }

    private void gotoMenu(){
        showMenu = true;
        rollingMenuFocus = false;
    }

    private void gotoRolling(){
        showMenu = false; //para o render
        rollingMenuFocus = true;
        containerMenu.reset();
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public void reset(){
        rollingMenuFocus = true;
    }


    //todo : nome das opcoes que alteram o menu como variaveis
}