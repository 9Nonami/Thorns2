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

    //
    private Menu menu;
    private boolean showMenu;
    private boolean rollingMenuFocus;


    public RollingMenuBasis(Thorns thorns){
        this.thorns = thorns;
        emptySlot = ImageLoader.loadImage("/res/menu/emptySlot.png");
        filledSlot = ImageLoader.loadImage("/res/menu/filledSlot.png");
        imageHeight = filledSlot.getHeight();
        pointer = 3;
        save = new Save();
        menu = new Menu(thorns, thorns.getChoicebg(), thorns.getFontDataBase(), thorns.getPointer(), Menu.STYLE_HORIZONTAL);
        menu.createOptions(10, 10, 10, "WRITE_CANCEL");
        rollingMenuFocus = true;
    }

    public void update() {

        boolean up = thorns.isUp();
        boolean down = thorns.isDown();
        boolean space = thorns.isSpace(); //todo anim. confirmacao

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
                String old = save.getSlots()[visibleSlots[pointer]]; //x-x-x-x << retorno
                if (old.charAt(Save.IMAGE_SLOT_ID) == '1'){
                    showMenu = true;
                    rollingMenuFocus = false;
                } else {
                    save.getSlots()[visibleSlots[pointer]] = Replacer.replace(old, Save.IMAGE_SLOT_ID, '1');
                    save.save();
                }
            }

        } else {
            menu.update();
            if (menu.isPressed()){
                if (menu.getChosenOptionAsString().equals("CANCEL")) {
                    showMenu = false;
                    rollingMenuFocus = true;
                    menu.reset();
                } else {
                    String old = save.getSlots()[visibleSlots[pointer]];
                    save.getSlots()[visibleSlots[pointer]] = Replacer.replace(old, Save.IMAGE_SLOT_ID, '1');
                    save.save();
                    showMenu = false;
                    rollingMenuFocus = true;
                    menu.reset();
                }
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
            menu.render(g);
        }

    }

    public void reset(){
        rollingMenuFocus = true;
    }

}