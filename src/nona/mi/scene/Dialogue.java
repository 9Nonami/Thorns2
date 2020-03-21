/*
Dialogue é uma classe a qual contém apenas uma sequencia
de caracteres para serem animados de acordo com a speed,
ou seja, se speed = 2, dois caracteres sao desenhados por
frame.
Vai do primeiro até o último caractere (contido num char[]),
quando atinge o final, endAnimation fica true.
Este objeto precisa basicamente do char[] e de um FontDataBase,
para pegar os sprites da fonte. Também é possível definir x e y
diferenciados para cada objeto.
'@' breakline.
'_' separa os textos
'&' separa as scenes
 */
package nona.mi.scene;

import java.awt.Graphics;

import nona.mi.db.FontDataBase;
import nona.mi.image.BaseImage;
import nona.mi.main.Game;



public class Dialogue {

    private FontDataBase fdb;
    private boolean endAnimation;
    private char[] arr;
    private char[] name;
    private double cont;

    private float textSpeed;

    private int fontHeight;

    private BaseImage textArea; //todo : game.getTextArea
    private boolean renderTextArea;

    private BaseImage nameBg;

    private boolean lockAudio;
    private String audioName;

    private boolean playFullAudio;
    private Game game;
    private boolean audioPaused;

    private boolean historyConfiguration;
    public static final int X = 10;
    public static final int Y = 351;
    public static final int X_NAME = 50;
    public static final int Y_NAME = 50;
    public static final int X_NAME_BG = 50;
    public static final int Y_NAME_BG = 50;
    public static final int SPACING = 5;



    //ESSENCIAL------------------------------------------

    public Dialogue(Game game, FontDataBase fdb, BaseImage textArea, BaseImage nameBg) {
        this.game = game;
        this.fdb = fdb;
        fontHeight = fdb.getFontHeight();

        cont = 0;
        textSpeed = 2 * game.getSpeedAdjust();
        //textSpeed = thorns.getSpeedAdjust();

        this.textArea = textArea;
        this.nameBg = nameBg;

        renderTextArea = true;
    }

    //---------------------------------------------------


    //GS-------------------------------------------------

    public void setAudio(String audioName, String audioPath) {
        this.audioName = audioName;
        lockAudio = false;
        game.getPackJukebox().load(audioPath, this.audioName);
    }

    public void setDialogue(char[] arr) {
        this.arr = arr;
    }

    public void setName(char[] name) {
        this.name = name;
    }

    public boolean getEndAnimation() {
        if  (playFullAudio && (audioName != null)){
            return endAnimation && !game.getPackJukebox().isPlaying(audioName);
        }
        return endAnimation;
    }

    public String getAudioName() {
        return audioName;
    }

    public void setPlayFullAudio(boolean b) {
        playFullAudio = b;
    }

    public void setAudioPaused(boolean audioPaused) {
        this.audioPaused = audioPaused;
    }

    public boolean isAudioPaused() {
        return audioPaused;
    }

    public void setRenderTextArea(boolean renderTextArea) { //todo : history acessa
        this.renderTextArea = renderTextArea;
    }

    public void setHistoryConfiguration(boolean historyConfiguration) {
        this.historyConfiguration = historyConfiguration;
    }

    //---------------------------------------------------


    //UR-------------------------------------------------

    public void update() {
        updateAudio();
        updateText();
    }

    private void updateAudio() {
        if (!lockAudio && audioName != null) {
            lockAudio = true;
            game.getPackJukebox().play(audioName);
        }
    }

    private void updateText() {
        if (arr != null) {
            if (!endAnimation) {
                cont += textSpeed;
                if (cont >= arr.length) {
                    if (cont > arr.length) {
                        cont = arr.length;
                    }
                    endAnimation = true;
                }
            }
        } else {
            System.out.println("null dialogue");
            System.exit(0);
        }
    }

    public void render(Graphics g) {
        renderTextArea(g);
        renderNameBg(g);
        renderName(g);
        renderDialogue(g);
    }

    private void renderTextArea(Graphics g) {
        if (renderTextArea) {
            textArea.render(g);
        }
    }

    private void renderNameBg(Graphics g) {
        if (name != null) {
            nameBg.render(g);
        }
    }

    private void renderName(Graphics g) {
        int tempxname;
        if (!historyConfiguration) {
            tempxname = X_NAME;
        } else {
            tempxname = HistoryScene.NEW_NAME_X;
        }
        if (name != null) {
            for (char c : name) {
                g.drawImage(fdb.get(c), tempxname, Y_NAME, null);
                tempxname += fdb.get(c).getWidth();
            }
        }
    }

    private void renderDialogue(Graphics g) {
        int tempx;
        int tempy;
        if (!historyConfiguration) {
            tempx = X;
            tempy = Y;
        } else {
            tempx = HistoryScene.NEW_DIALOG_X;
            tempy = HistoryScene.NEW_DIALOG_Y;
        }
        for (int id = 0; id < (int) cont; id++) {
            if (arr[id] == '@') {
                tempy += fontHeight + SPACING;
                if (!historyConfiguration) {
                    tempx = X;
                } else {
                    tempx = HistoryScene.NEW_DIALOG_X;
                }
                continue;
            }
            g.drawImage(fdb.get(arr[id]), tempx, tempy, null);
            tempx += fdb.get(arr[id]).getWidth();
        }
    }

    //---------------------------------------------------


    //OTHER----------------------------------------------

    public void completeDialogue() {
        cont = arr.length;
        endAnimation = true;
    }

    public void defineCompleteDialogue() {
        cont = arr.length;
    }

    //---------------------------------------------------


    public void reset() {
        endAnimation = false;
        cont = 0;
        lockAudio = false;
        audioPaused = false;
        historyConfiguration = false;
        renderTextArea = true;
    }

}
