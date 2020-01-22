package nona.mi.button;

import nona.mi.loader.ImageLoader;
import nona.mi.main.Game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class ButtonGroup {

    private Button[] buttons;
    private int startButtonID;
    private int buttonsToShow;
    private int startIncrement;
    private int showIncrement;
    private BufferedImage standardButtonImage;
    private BufferedImage focusedButtonImage;
    private Game game;
    private int clickedButton;

    public static final int NO_CLICK = -97;



    public ButtonGroup(Game game, int buttonsToShow) {
        this.game = game;
        this.buttonsToShow = buttonsToShow;
        startButtonID = 0;
        startIncrement = 0;
        showIncrement = 1;
    }

    public void setImages(String standardButtonImage, String focusedButtonImage) {
        this.standardButtonImage = ImageLoader.loadImage(standardButtonImage);
        this.focusedButtonImage = ImageLoader.loadImage(focusedButtonImage);
    }

    /*todo
        tentar deixar os screenshots publicos
        ai podera alterar e checar qual slot ja tem save
        alterar na hora do save
     */
    public void createButtons(int totalButtons, int row, int column, int x, int y, int spacing, HashMap<Integer, BufferedImage> screenshots) {

        //cria os botoes (rect), atribui o id e o audio
        this.buttons = new Button[totalButtons];
        for (int i = 0; i < totalButtons; i++) {
            buttons[i] = new RectButton(game);
            buttons[i].setId(i);
            buttons[i].setAudioName("click");
        }

        //tamanho padrao das imagens
        int buttonWidth = standardButtonImage.getWidth();
        int buttonHeight = standardButtonImage.getHeight();

        //valores originais  - para reset
        int xx = x;
        int yy = y;

        //todo : ?
        int loop = totalButtons / (row * column);

        for (int h = 0; h < loop; h++) {
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < column; j++) {
                    int id = (i * column) + j + ((row * column) * h); //esta formula faz o id ir de 1 em 1. ex: 0, 1, 2...
                    if (screenshots.containsKey(buttons[id].getId())) {
                        buttons[id].setImages(screenshots.get(buttons[id].getId()), focusedButtonImage, xx, yy);
                    } else {
                        buttons[id].setImages(standardButtonImage, focusedButtonImage, xx, yy);
                    }
                    xx += spacing + buttonWidth;
                }
                xx = x;
                yy += spacing + buttonHeight;
            }
            xx = x;
            yy = y;
        }

    }

    public void update() {
        clickedButton = NO_CLICK;
        for (int i = startButtonID + startIncrement; i < buttonsToShow * showIncrement; i++) {
            buttons[i].update();
            if (buttons[i].isClicked()) {
                System.out.println("buttonsgroup-button: " + buttons[i].getId());
                clickedButton = buttons[i].getId();
                break;
            }
        }
    }

    public int getClickedButton() {
        return clickedButton;
    }

    public Button[] getButtons() {
        return buttons;
    }

    public BufferedImage getStandardButtonImage() {
        return standardButtonImage;
    }

    public void render(Graphics g) {
        for (int i = startButtonID + startIncrement; i < buttonsToShow * showIncrement; i++) {
            buttons[i].render(g);
        }
    }

    public void reset() {
        for (Button button : buttons) {
            button.reset();
        }
        clickedButton = NO_CLICK;
    }

}