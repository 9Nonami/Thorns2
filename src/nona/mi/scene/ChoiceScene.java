package nona.mi.scene;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import nona.mi.db.FontDataBase;
import nona.mi.image.BaseImage;
import nona.mi.main.Game;

public class ChoiceScene extends Scene {

    private char[] option1;
    private int[] xzesOption1;
    private int y1;
    private int nextSceneOp1;

    private char[] option2;
    private int[] xzesOption2;
    private int y2;
    private int nextSceneOp2;

    private boolean space;
    private boolean swp;
    private boolean up;
    private boolean down;

    private FontDataBase fdb;
    private FontDataBase fdbFocus;
    private FontDataBase fontop1;
    private FontDataBase fontop2;

    private BaseImage background;

    private BufferedImage choiceBg;
    private int yChoiceUp;
    private int yChoiceDown;
    private int xChoice;

    private String audioName;



    public ChoiceScene(Game game, BaseImage background, String texts, int nextSceneOp1, int nextSceneOp2) {
        super(game, nextSceneOp1);
        this.background = background;
        this.nextSceneOp1 = nextSceneOp1;
        this.nextSceneOp2 = nextSceneOp2;

        initTexts(texts);

        initCoordinates();

        up = false;
        down = false;
        swp = true;

        fontop1 = fdbFocus;
        fontop2 = fdb;
    }

    public void setFdb(FontDataBase fdb) {
        this.fdb = fdb;
    }

    public void setFdbFocus(FontDataBase fdbFocus) {
        this.fdbFocus = fdbFocus;
    }

    public void setChoiceBg(BufferedImage choiceBg) {
        this.choiceBg = choiceBg;
    }

    public void setAudioName(String audioName) {
        this.audioName = audioName;
    }

    @Override
    public void updateScene() {
        up = game.isUp();
        down = game.isDown();
        space = game.isSpace();
        if ((up || down) && !(game.getStandardJukeBox().isPlaying(audioName))) {
            swap();
            game.getStandardJukeBox().play(audioName);
        }
        if (space) {
            game.nextScene(); //todo : ????
            reset();
        }
    }

    @Override
    public void renderScene(Graphics g) {
        background.render(g);

        renderChoicebg(g);

        renderArr(g, option1, xzesOption1, y1, fontop1);
        renderArr(g, option2, xzesOption2, y2, fontop2);

    }

    @Override
    public void reset() {
        super.reset();
        up = false;
        down = false;
        swp = true;
        space = false;
        nextScene = nextSceneOp1;
        fontop1 = fdbFocus;
        fontop2 = fdb;
    }

    private void swap() {
        // swp começa com true
        // true = texto de cima
        // false = texto de baixo

        swp = !swp;
        if (swp) {
            nextScene = nextSceneOp1;

            fontop1 = fdbFocus;
            fontop2 = fdb;

        } else {
            nextScene = nextSceneOp2;

            fontop1 = fdb;
            fontop2 = fdbFocus;
        }
    }

    private void renderArr(Graphics g, char[] option, int[] xzes, int y, FontDataBase fdb) {
        //xzes armazena o valor inicial de x do texto centralizado
        //o numero de indices é igual ao numero de '@' no texto
        int tempX = xzes[0];

        int tempY = y;

        int id = 0;

        for (int i = 0; i < option.length; i++) {
            if (option[i] == '@') {
                tempY += fdb.getFontHeight();
                tempX = xzes[++id];
                continue;
            }
            g.drawImage(fdb.get(option[i]), tempX, tempY, null);
            tempX += fdb.get(option[i]).getWidth();
        }
    }

    private void renderChoicebg(Graphics g) {
        g.drawImage(choiceBg, xChoice, yChoiceUp, null);
        g.drawImage(choiceBg, xChoice, yChoiceDown, null);
    }

    private int getCenterOf(char[] c) {  //centro em X de cada frase
        int width = 0;
        for (int i = 0; i < c.length; i++) {
            width += fdb.get(c[i]).getWidth();
        }
        return (int) (game.getWidth() / 2) - (width/2);
    }

    private void initTexts(String s) { //option1_option2

        String[] ss = s.split("_"); //option1[0] option2[1]

        option1 = ss[0].toCharArray();
        xzesOption1 = createXArr(ss[0]); //xzes identifica o ponto x de cada FRASE para ficar centralizado

        option2 = ss[1].toCharArray();
        xzesOption2 = createXArr(ss[1]);
    }

    private int[] createXArr(String option) {
        String[] splitedOption = option.split("@");

        int[] temp = new int[splitedOption.length];
        for (int i = 0; i < splitedOption.length; i++) {
            temp[i] = getCenterOf(splitedOption[i].toCharArray());
        }

        return temp; //retorna o centro em X de cada frase separada por @
    }

    private int getOptionHeight(char[] option) {
        int height = this.fdb.getFontHeight();
        for (int i = 0; i < option.length; i++) {
            if (option[i] == '@') {
                height += fdb.getFontHeight();
            }
        }
        return height;
    }

    private void initCoordinates() {
        int middleScreenY = game.getHeight() / 2;
        int offset = this.fdb.getFontHeight();

        this.yChoiceUp = middleScreenY - offset - choiceBg.getHeight();
        this.yChoiceDown = middleScreenY + offset;
        this.xChoice = game.getWidth() / 2 - choiceBg.getWidth() / 2;

        int option1Height = getOptionHeight(this.option1);
        int option2Height = getOptionHeight(this.option2);

        this.y1 = yChoiceUp + choiceBg.getHeight()/2 - option1Height/2;
        this.y2 = yChoiceDown + choiceBg.getHeight()/2 - option2Height/2;
    }

}