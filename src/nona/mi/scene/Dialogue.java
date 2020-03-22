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

    private FontDataBase fdb; //fonte deste dialogo
    private boolean endAnimation; //estado da animacao
    private char[] arr; //dialogo a ser renderizado
    private char[] name; //recebe de NameDataBase
    private double cont; //todo : objeto

    private BaseImage textArea; //area retangular onde o texto eh 'centralizado'.
    private boolean renderTextArea; //controla quando a textArea deve ser exibida. Ex.: historyScene nao usa a area

    private BaseImage nameBg; //imagem onde o nome do personagem eh 'centralizado'

    private boolean lockAudio; //bloqueia o audio apos ser executado uma vez
    private String audioName; //id para resgatar uma fala em packjuke. eh definido no txt de dialogos

    private boolean playFullAudio; //permite que o usuario seja obrigado a escutar a fala por inteiro
    private Game game; //ref de game
    private boolean audioPaused; //para controle da fala

    private boolean historyConfiguration; //identifica que esta sendo renderizado em historyScene
    public static final int X = 10; //base x
    public static final int Y = 351; // base y
    public static final int X_NAME = 50; //base x do nome
    public static final int Y_NAME = 50; //base y do nome
    public static final int X_NAME_BG = 50; //base x do retangulo do nome
    public static final int Y_NAME_BG = 50; //base y do retangulo do nome
    public static final int SPACING = 5; //espacamento entre os caracteres em y



    //ESSENCIAL------------------------------------------

    public Dialogue(Game game, FontDataBase fdb, BaseImage textArea, BaseImage nameBg) {
        this.game = game;
        this.fdb = fdb;
        cont = 0;
        this.textArea = textArea;
        this.nameBg = nameBg; //todo : pegar de um mapa de acordo com o nome
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
                cont += game.getSpeedAdjust();
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
                tempy += fdb.getFontHeight() + SPACING;
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
