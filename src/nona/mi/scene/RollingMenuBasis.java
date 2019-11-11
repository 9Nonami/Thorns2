package nona.mi.scene;

import nona.mi.loader.ImageLoader;
import nona.mi.main.Thorns;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class RollingMenuBasis {

    private Thorns thorns;

    private BufferedImage emptySlot;
    private BufferedImage filledSlot;
    private BufferedImage[] slots;
    private int totalSlots;

    private int[] visibleSlots = {0, 1, 2, 3, 4, 5, 6, 7};
    private int pointer;

    private int imageHeight;


    public RollingMenuBasis(Thorns thorns, int totalSlots){
        this.thorns = thorns;
        this.totalSlots = totalSlots; // todo : save.NUM_SLOTS
        slots = new BufferedImage[totalSlots];
        emptySlot = ImageLoader.loadImage("/res/menu/emptySlot.png");
        filledSlot = ImageLoader.loadImage("/res/menu/filledSlot.png");
        imageHeight = filledSlot.getHeight();
        pointer = 3;
    }

    public void update() {

        boolean up = thorns.isUp();
        boolean down = thorns.isDown();
        boolean space = thorns.isSpace(); //todo anim. confirmacao

        int increment = 0;
        if (up){
            increment = -1;
        }
        if (down){
            increment = 1;
        }

        //ROLL
        if (up || down){
            for (int i = 0; i < visibleSlots.length; i++) {
                visibleSlots[i] = visibleSlots[i] + increment;
                if (visibleSlots[i] > slots.length - 1) {
                    visibleSlots[i] = 0;
                } else if (visibleSlots[i] < 0) {
                    visibleSlots[i] = slots.length - 1;
                }
            }
        }

        //SAVE
        if (space){
            slots[visibleSlots[pointer]] = filledSlot;
        }
    }

    public void render(Graphics g){

        int x = 0;
        int y = 0;

        for (int i = 0; i < visibleSlots.length; i++) {
            g.drawImage(slots[visibleSlots[i]], x, y, null);
            y += imageHeight;
        }
        //todo : y * height

        /*

        if (slot[i] == 0){
            drawEmpty;
        } else {
            drawFilled();
        }

         */
    }



}