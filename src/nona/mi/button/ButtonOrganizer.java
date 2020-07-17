package nona.mi.button;

import java.util.HashMap;

public class ButtonOrganizer {

    private HashMap<Integer, RectButton> map;

    public ButtonOrganizer(RectButton[] rectButtons) {
        //PRECISA DE:
        //save, load, copy, delete, main, history
        map = new HashMap<Integer, RectButton>();
        for (RectButton rectButton : rectButtons) {
            map.put(rectButton.getId(), rectButton);
        }
    }

    public void defineStanConfig() {
        map.get(Button.SAVE).setXY(Button.STAN_CHOICE_X_SAVE, Button.STAN_Y_ALL);
        map.get(Button.LOAD).setXY(Button.STAN_CHOICE_X_LOAD, Button.STAN_Y_ALL);
        map.get(Button.COPY).setXY(Button.STAN_CHOICE_X_COPY, Button.STAN_Y_ALL);
        map.get(Button.DEL).setXY(Button.STAN_CHOICE_X_DELETE, Button.STAN_Y_ALL);
        map.get(Button.HISTORY).setXY(Button.STAN_CHOICE_X_HISTORY, Button.STAN_Y_ALL);
        map.get(Button.MAIN).setXY(Button.STAN_CHOICE_X_MAIN, Button.STAN_Y_ALL);
    }

    public void defineChoiceConfig() {
        map.get(Button.SAVE).setXY(Button.STAN_CHOICE_X_SAVE, Button.CHOICE_Y_ALL);
        map.get(Button.LOAD).setXY(Button.STAN_CHOICE_X_LOAD, Button.CHOICE_Y_ALL);
        map.get(Button.COPY).setXY(Button.STAN_CHOICE_X_COPY, Button.CHOICE_Y_ALL);
        map.get(Button.DEL).setXY(Button.STAN_CHOICE_X_DELETE, Button.CHOICE_Y_ALL);
        map.get(Button.HISTORY).setXY(Button.STAN_CHOICE_X_HISTORY, Button.CHOICE_Y_ALL);
        map.get(Button.MAIN).setXY(Button.STAN_CHOICE_X_MAIN, Button.CHOICE_Y_ALL);
    }

    public void defineMainConfig() {
        map.get(Button.START).setXY(Button.MAIN_X_START, Button.MAIN_Y_START);
        map.get(Button.LOAD).setXY(Button.MAIN_X_LOAD, Button.MAIN_Y_LOAD);
    }

}
