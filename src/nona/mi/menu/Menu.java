package nona.mi.menu;

import nona.mi.db.FontDataBase;
import nona.mi.image.BaseImage;
import nona.mi.main.Game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;


public class Menu {

    private int numOptions;
    private BufferedImage optionBg;
    private BaseImage pointer;

    private int y;
    private int x;
    private int spacing;

    private String[] texts;
    private int[] xTexts;
    private int[] yTexts;
    private FontDataBase fdb;

    private Game game;

    private boolean pressed;
    private int chosenOption;
    private int optionID;
    private int maxOptionID;

    public static final int STYLE_VERTICAL = 0;
    public static final int STYLE_HORIZONTAL = 1;
    private int style;

    private String audioName;



    //CONSTRUCTOR
    public Menu(Game game, BufferedImage optionBg, FontDataBase fdb, BufferedImage pointerImage, int style) {
        this.game = game;
        this.optionBg = optionBg;
        this.fdb = fdb;
        this.style = style;
        this.pointer = new BaseImage(pointerImage, 0, 0);
    }

    public void createOptions(int x, int y, int spacing, String texts) {

        String[] temp = texts.split("_");

        this.numOptions = temp.length;
        this.optionID = 0;
        this.maxOptionID = numOptions - 1; //-1 por que quero base 0 e nao base 1

        this.x = x;
        this.y = y;
        this.spacing = spacing;

        pointer.setX(x);
        pointer.setY(y);

        createTexts(temp);
    }

    private void createTexts(String[] texts) {

        //TEXTOS
        this.texts = texts;

        //COORDENADAS X/Y DOS TEXTOS
        xTexts = new int[numOptions];
        yTexts = new int[numOptions];

        for (int i = 0; i < numOptions; i++) {

            //VARIAVEL A QUAL ARMAZENA A LARGURA TOTAL
            //DO TEXTO PARA QUE POSSA SER UTILIZADA NA
            //CENTRALIZACAO
            int tempWidth = 0;

            //LOOP USADO PARA SOMAR A LARGURA
            //DE CADA CARACTERE DO TEXTO
            for (int j = 0; j < this.texts[i].length(); j++) {
                tempWidth += fdb.get(this.texts[i].charAt(j)).getWidth();
            }

            //DEPOIS DE CALCULADA A LARGURA, VERIFICA SE
            //O POSICIONAMENTO DEVERA SER FEITO NA VERTICAL
            //OU HORIZONTAL
            if (style == STYLE_VERTICAL) {
                //DEFINE AS POSICOES DOS TEXTOS
                xTexts[i] = (x + (optionBg.getWidth() / 2)) - (tempWidth / 2);
                yTexts[i] = (y + (spacing * i)  + (optionBg.getHeight() / 2) - (fdb.getFontHeight() / 2) + (optionBg.getHeight() * i));
            } else {
                xTexts[i] = ((x + (spacing * i) + (optionBg.getWidth() / 2)) - (tempWidth / 2) + (optionBg.getWidth() * i));
                yTexts[i] = y  + (optionBg.getHeight() / 2) - (fdb.getFontHeight() / 2);
            }
        }

    }

    public void setAudioName(String audioName) {
        this.audioName = audioName;
    }

    //UPDATE
    public void update() {

        boolean up = game.isUp();
        boolean down = game.isDown();
        boolean left = game.isLeft();
        boolean right = game.isRight();
        boolean space = game.isSpace();

        if (!(game.getStandardJukeBox().isPlaying(audioName))){
            if (style == STYLE_VERTICAL) {

                if (up) {
                    playSound();
                    optionID--;
                    if (optionID < 0) {
                        optionID = maxOptionID;
                    }
                    movePointerVertically();
                } else if (down) {
                    playSound();
                    optionID++;
                    if (optionID > maxOptionID) {
                        optionID = 0;
                    }
                    movePointerVertically();
                }

            } else {

                if (left){
                    playSound();
                    optionID--;
                    if (optionID < 0) {
                        optionID = maxOptionID;
                    }
                    movePointerHorizontally();
                } else if (right){
                    playSound();
                    optionID++;
                    if (optionID > maxOptionID) {
                        optionID = 0;
                    }
                    movePointerHorizontally();
                }

            }
        }

        if (space){
            pressed = true;
            chosenOption = optionID;
        }
    }

    private void playSound() {
        game.getStandardJukeBox().play(audioName);
    }

    private void movePointerVertically() {
        pointer.setY(y + (optionBg.getHeight() * optionID) + (spacing * optionID));
    }

    private void movePointerHorizontally() {
        pointer.setX(x + (optionBg.getWidth() * optionID) + (spacing * optionID));
    }

    //RENDER
    public void render(Graphics g) {
        renderOptions(g);
        renderTexts(g);
        renderPointer(g);
    }

    //RENDERIZA AS IMAGENS
    private void renderOptions(Graphics g) {
        int x = this.x;
        int y = this.y;
        for (int i = 0; i < numOptions; i++) {
            g.drawImage(optionBg, x, y, null); //optionImage
            if (style == STYLE_VERTICAL){
                y += optionBg.getHeight() + spacing;
            } else {
                x += optionBg.getWidth() + spacing;
            }
        }
    }

    //RENDERIZA OS TEXTOS
    private void renderTexts(Graphics g) {
        for (int i = 0; i < numOptions; i++) {
            int tempX = xTexts[i];
            for (int j = 0; j < texts[i].length(); j++) {
                g.drawImage(fdb.get(texts[i].charAt(j)), tempX, yTexts[i], null);
                tempX += fdb.get(texts[i].charAt(j)).getWidth();
            }
        }
    }

    //RENDERIZA O PONTEIRO DO TEXTO
    private void renderPointer(Graphics g) {
        pointer.render(g);
    }

    //GS
    public boolean isPressed() {
        return pressed;
    }

    public int getChosenOptionAsInt() {
        return chosenOption;
    }

    public String getChosenOptionAsString() {
        return texts[chosenOption];
    }

    //RESET
    public void reset() {
        optionID = 0;
        pointer.setX(x);
        pointer.setY(y);
        pressed = false;
    }

    // todo : calcular direito o y da fonte
    // todo : ou usar uma fonte quadrada
}