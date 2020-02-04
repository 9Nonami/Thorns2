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

    private int x;
    private int y;
    private int spacing;

    private float textSpeed;

    private int fontHeight;

    private BaseImage textArea;
    private BaseImage nameBg;
    private int xname;
    private int yname;

    private boolean lockAudio;
    private String audioName;

    private boolean playFullAudio;
    private Game game;
    private boolean audioPaused;



    //ESSENCIAL------------------------------------------

    public Dialogue(Game game, FontDataBase fdb, int x, int y, BaseImage textArea, BaseImage nameBg) {
        this.game = game;
        this.fdb = fdb;
        fontHeight = fdb.getFontHeight();

        this.x = x;
        this.y = y;

        cont = 0;
        textSpeed = 2 * game.getSpeedAdjust();
        //textSpeed = thorns.getSpeedAdjust();

        this.textArea = textArea;
        this.nameBg = nameBg;

        spacing = 5;
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
        initNameCoordinates();
    }

    public boolean getEndAnimation() {
        if  (playFullAudio && (audioName != null)){
            return endAnimation && !game.getPackJukebox().isPlaying(audioName);
        }
        return endAnimation;
    }

    private int getCenterY() {
        return (int) ((nameBg.getY() + (nameBg.getHeight() / 2)) - (fdb.getFontHeight() / 2));
    }

    private int getCenterXOf(char[] c) {
        int width = 0;
        for (int i = 0; i < c.length; i++) {
            width += fdb.get(c[i]).getWidth();
        }
        return (int) ((nameBg.getWidth() / 2) - (width / 2));
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

        textArea.render(g);

        renderNameBg(g);

        renderName(g);

        renderDialogue(g);
    }

    private void renderNameBg(Graphics g) {
        if (name != null) {
            nameBg.render(g);
        }
    }

    private void renderName(Graphics g) {

        int tempxname = xname;
        int tempyname = yname;

        if (name != null) {
            for (int i = 0; i < name.length; i++) {
                g.drawImage(fdb.get(name[i]), tempxname, tempyname, null);
                tempxname += fdb.get(name[i]).getWidth();
            }
        }
    }

    private void renderDialogue(Graphics g) {

        int tempx = x;
        int tempy = y;

        for (int id = 0; id < (int) cont; id++) {

            if (arr[id] == '@') {
                tempy += fontHeight + spacing;
                tempx = x;
                continue;
            }

            g.drawImage(fdb.get(arr[id]), tempx, tempy, null);

            tempx += fdb.get(arr[id]).getWidth();

        }
    }

    //---------------------------------------------------


    //OTHER----------------------------------------------

    public void initNameCoordinates() {
        xname = getCenterXOf(name);
        yname = getCenterY();
    }

    //---------------------------------------------------


    public void reset() {
        endAnimation = false;
        cont = 0;
        lockAudio = false;
        audioPaused = false;
    }

}
