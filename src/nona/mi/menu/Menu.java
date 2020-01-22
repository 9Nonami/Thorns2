package nona.mi.menu;

import nona.mi.button.Button;
import nona.mi.main.Game;

import java.awt.Graphics;

public class Menu { // todo : ver. se esta fazendo o mesmo que buttonGroup

    private Game game;
    private Button[] buttons;

    public Menu(Game game, Button[] buttons) {
        this.game = game;
        this.buttons = buttons;
    }

    public void update() {
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].update();
        }
    }

    public void render(Graphics g) {
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].render(g);
        }
    }

    public Button[] getButtons() {
        return buttons;
    }

    public void reset() {
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].reset();
        }
    }

}
