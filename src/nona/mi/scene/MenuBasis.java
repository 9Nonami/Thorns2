package nona.mi.scene;

import nona.mi.db.FontDataBase;
import nona.mi.image.BaseImage;
import nona.mi.main.Thorns;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;


public class MenuBasis {

    private int numOptions;
    private BufferedImage optionBg;
    private BaseImage pointer;

    private int[] yOptions;
    private int xOption;

    private String[] texts;
    private FontDataBase fdb;
    private int[] xTexts;
    private int[] yTexts;

    private boolean rotatePointer;
    private int pointerRotation;
    private int pointerRotationSpeed;

    private Thorns thorns;

    private boolean pressed;
    private int chosenOption;
    private int optionID;
    private int maxOptionID;

    //CONSTRUCTOR
    public MenuBasis(Thorns thorns) {
        this.thorns = thorns;
        optionBg = thorns.getChoicebg();
        fdb = thorns.getFontDataBase();
    }

    public void createOptions(int numOptions, int x, int y, int spacing, String texts){

        this.numOptions = numOptions;
        this.optionID = 0;
        this.maxOptionID = numOptions - 1; //-1 por que quero base 0 e nao base 1

        xOption = x;
        yOptions = new int[numOptions];
        yOptions[0] = y;

        for (int i = 1; i < numOptions; i++) {
            yOptions[i] = yOptions[i - 1] + optionBg.getHeight() + spacing;
        }

        createTexts(texts);
        createPointer();
    }

    private void createTexts(String texts){

        this.texts = texts.split("_");

        xTexts = new int[numOptions];
        yTexts = new int[numOptions];

        for (int i = 0; i < numOptions; i++) {
            int tempX = 0;
            for (int j = 0; j < this.texts[i].length(); j++) {
                tempX += fdb.get(this.texts[i].charAt(j)).getWidth();
            }
            xTexts[i] = (xOption + (optionBg.getWidth() / 2)) - (tempX / 2);
            yTexts[i] = yOptions[i] + (optionBg.getHeight() / 2) - (fdb.getFontHeight() / 2);
        }
    }

    private void createPointer(){

        BufferedImage tempPointerImage = thorns.getPointer();
        int pointerX = xOption;
        int pointerY = yOptions[0] + (optionBg.getHeight() / 2) - (tempPointerImage.getHeight() / 2);

        this.pointer = new BaseImage(tempPointerImage, pointerX, pointerY);
    }

    public void setPointerRotationSpeed(int pointerRotationSpeed) {
        this.pointerRotation = 0;
        this.rotatePointer = true;
        this.pointerRotationSpeed = pointerRotationSpeed;
    }

    public void update() {

        boolean up = thorns.isUp();
        boolean down = thorns.isDown();
        boolean space = thorns.isSpace();

        if (rotatePointer){
            pointerRotation += pointerRotationSpeed;
            if (pointerRotation > 360) {
                pointerRotation = 0;
            }
        }

        if (!(thorns.getMyJukeBox().isPlaying(Thorns.AUDIO_CHOICE))){
            if (up) {
                thorns.getMyJukeBox().play(Thorns.AUDIO_CHOICE);
                optionID--;
                if (optionID < 0) {
                    optionID = maxOptionID;
                }
                pointer.setY(yOptions[optionID] + (optionBg.getHeight() / 2) - (pointer.getHeight() / 2));
            } else if (down) {
                thorns.getMyJukeBox().play(Thorns.AUDIO_CHOICE);
                optionID++;
                if (optionID > maxOptionID) {
                    optionID = 0;
                }
                pointer.setY(yOptions[optionID] + (optionBg.getHeight() / 2) - (pointer.getHeight() / 2));
            }
        }

        if (space){
            pressed = true;
            chosenOption = optionID;
            System.out.println("chosen: " + optionID);
        }
    }

    public void render(Graphics g) {
        renderTextBackgrounds(g);
        renderTexts(g);
        renderPointer(g);
    }

    public void reset() {
        optionID = 0;
        pointer.setY(yOptions[optionID] + (optionBg.getHeight() / 2) - (pointer.getHeight() / 2));
        pointerRotation = 0;
        pressed = false;
    }

    private void renderTextBackgrounds(Graphics g){
        for (int i = 0; i < numOptions; i++) {
            g.drawImage(optionBg, xOption, yOptions[i], null);
        }
    }

    private void renderTexts(Graphics g){
        for (int i = 0; i < numOptions; i++) {
            int tempX = xTexts[i];
            for (int j = 0; j < texts[i].length(); j++) {
                g.drawImage(fdb.get(texts[i].charAt(j)), tempX, yTexts[i], null);
                tempX += fdb.get(texts[i].charAt(j)).getWidth();
            }
        }
    }

    private void renderPointer(Graphics g){
        if (rotatePointer){
            renderRotatePointer(g);
        } else {
            renderStaticPointer(g);
        }
    }

    private void renderRotatePointer(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform at = g2d.getTransform();

        g2d.translate((pointer.getX() + (pointer.getWidth() / 2)), (pointer.getY() + (pointer.getHeight() / 2)));
        g2d.rotate(Math.toRadians(pointerRotation));

        g2d.drawImage(pointer.getImage(), -pointer.getWidth()/2, -pointer.getHeight()/2, null);

        g2d.setTransform(at);
    }

    private void renderStaticPointer(Graphics g){
        pointer.render(g);
    }

    public boolean isPressed() {
        return pressed;
    }

    public int getChosenOption() {
        return chosenOption;
    }

    public int getNumOptions() {
        return numOptions;
    }

    // todo : calcular direito o y da fonte
}
