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

    private BaseImage textArea; //area retangular onde o texto eh 'centralizado'.
    private boolean renderTextArea; //controla quando a textArea deve ser exibida. Ex.: historyScene nao usa a area

    private BaseImage nameBg; //imagem onde o nome do personagem eh 'centralizado'
    private boolean renderNameBg; //controla a exibicao da imagem atras do nome do char

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
        this.textArea = textArea; //todo : game.getStandardTextArea();
        this.nameBg = nameBg; //todo : pegar de um mapa de acordo com o nome
        renderTextArea = true;
        renderNameBg = true;
    }

    //---------------------------------------------------


    //GS-------------------------------------------------

    public void setAudio(String audioName, String audioPath) {
        this.audioName = audioName;
        lockAudio = false;
        game.getPackJukebox().load(audioPath, this.audioName);
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


    public void setRenderTextArea(boolean renderTextArea) {
        this.renderTextArea = renderTextArea;
    }

    public void setRenderNameBg(boolean renderNameBg) {
        this.renderNameBg = renderNameBg;
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
                game.getContForStan().update(game.getSpeedAdjust());
                if (game.getContForStan().getValue() >= arr.length) {
                    if (game.getContForStan().getValue() > arr.length) {
                        game.getContForStan().setValue(arr.length);
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
        if (name != null && renderNameBg) {
            nameBg.render(g);
        }
    }

    private void renderName(Graphics g) {
        if (name != null) {
            int tempXName;
            int tempYName;
            if (!historyConfiguration) {
                tempXName = X_NAME;
                tempYName = Y_NAME;
            } else {
                tempXName = HistoryScene.NEW_NAME_X;
                tempYName = HistoryScene.NEW_NAME_Y;
            }
            for (char c : name) {
                g.drawImage(fdb.get(c), tempXName, tempYName, null);
                tempXName += fdb.get(c).getWidth();
            }
        }
    }

    private void renderDialogue(Graphics g) {
        if (!historyConfiguration) {
            //renderiza para stan
            int tempX = X;
            int tempY = Y;
            for (int id = 0; id < (int) game.getContForStan().getValue(); id++) {
                if (arr[id] == '@') {
                    tempY += fdb.getFontHeight() + SPACING;
                    tempX = X;
                    continue;
                }
                g.drawImage(fdb.get(arr[id]), tempX, tempY, null);
                tempX += fdb.get(arr[id]).getWidth();
            }
        } else {
            //renderiza para history
            int tempX = HistoryScene.NEW_DIALOG_X;
            int tempY = HistoryScene.NEW_DIALOG_Y;
            for (char c : arr) {
                if (c == '@') {
                    tempY += fdb.getFontHeight() + SPACING;
                    tempX = HistoryScene.NEW_DIALOG_X;
                    continue;
                }
                g.drawImage(fdb.get(c), tempX, tempY, null);
                tempX += fdb.get(c).getWidth();
            }
        }
    }

    //---------------------------------------------------


    //OTHER----------------------------------------------

    public void completeDialogue() {
        game.getContForStan().setValue(arr.length);
        endAnimation = true;
    }

    //---------------------------------------------------


    public void reset() {
        endAnimation = false;
        lockAudio = false;
        audioPaused = false;
        historyConfiguration = false;
        renderTextArea = true;
        renderNameBg = true;
    }

}
