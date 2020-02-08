/*
    esta classe soh junta varios objetos do
    tipo button para dar o update, render e reset;

    para saber qual button foi clicado, precisa usar o getId,
    portanto, todos butttons para esta classe precisam de id;

    diferente de SlotGroup, esta classe nao tem objetido de
    organizar os buttons em coordenadas especificas, podendo
    ficar em qualquer posicao desordenada;
 */

package nona.mi.button;

import nona.mi.constant.ID;

import java.awt.Graphics;

public class ButtonGroup {

    private Button[] buttons;
    private int clickedButton;



    public ButtonGroup(Button[] buttons) {
        this.buttons = buttons;
    }

    public void update() {
        clickedButton = ID.NO_CLICK;
        for (Button temp : buttons) {
            temp.update();
            if (temp.isClicked()) {
                clickedButton = temp.getId();
                break;
            }
        }
    }

    public void render(Graphics g) {
        for (Button temp : buttons) {
            temp.render(g);
        }
    }

    public int getClickedButton() {
        return clickedButton;
    }

    public void reset() {
        for (Button temp : buttons) {
            temp.reset();
        }
    }

}
