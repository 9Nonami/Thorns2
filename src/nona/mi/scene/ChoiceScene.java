package nona.mi.scene;

import nona.mi.main.Game;
import nona.mi.rectangle.Rectangle;

import java.awt.Color;
import java.awt.Graphics;

public class ChoiceScene extends Scene {

    private String[] sentences;
    private int[][] x;
    private int[][] y;
    private Rectangle[] rectangles;
    private int focusedRectangle;

    public static final int LINE_SPACING = 0;
    public static final int SENTENCE_SPACING = 50;



    public ChoiceScene(Game game, int sceneId, String[] sentences) {
        super(game, sceneId);
        this.sentences = sentences;
        x = new int[sentences.length][];
        y = new int[sentences.length][];
        rectangles = new Rectangle[sentences.length];
        for (int i = 0; i < rectangles.length; i++) {
            rectangles[i] = new Rectangle();
        }
        defineLines();
        defineX();
        defineInitialY();
        focusedRectangle = Rectangle.NO_FOCUS;
        //log();
    }

    private void defineLines() {
        int totalLines = 0;
        for (int i = 0; i < sentences.length; i++) {
            for (int j = 0; j < sentences[i].length(); j++) {
                char c = sentences[i].charAt(j);
                if (c == Dialogue.BREAK_LINE) {
                    totalLines++;
                }
            }
            x[i] = new int[totalLines];
            y[i] = new int[totalLines];
            totalLines = 0;
        }
        /*System.out.println("sentences: " + sentences.length);
        for (int i = 0; i < sentences.length; i++) {
            System.out.println("lines of sentence " + i + ": " + x[i].length);
        }
        System.out.println("");*/
    }

    private void defineX() {
        int width = 0;
        int lineId = 0;
        int higherWidth = 0;
        int lowerX = game.getWidth(); //soh para comecar com um numero grande
        for (int i = 0; i < sentences.length; i++) {
            for (int j = 0; j < sentences[i].length(); j++) {
                char c = sentences[i].charAt(j);
                if (c == Dialogue.BREAK_LINE) {
                    x[i][lineId] = (game.getWidth() / 2) - (width / 2);
                    if (width > higherWidth) {
                        higherWidth = width;
                    }
                    if (x[i][lineId] < lowerX) {
                        lowerX = x[i][lineId];
                    }
                    width = 0;
                    lineId++;
                } else {
                    width += game.getFontDataBase().get(c).getWidth();
                }
            }
            rectangles[i].setWidth(higherWidth);
            rectangles[i].setX(lowerX);
            lineId = 0;
        }
    }

    private void defineInitialY() {
        int tempHeight = 0;
        for (int i = 0; i < sentences.length; i++) {
            int sentenceHeight = (x[i].length * game.getFontDataBase().getFontHeight()) + ((x[i].length - 1) * LINE_SPACING);
            tempHeight += sentenceHeight;
            rectangles[i].setHeight(sentenceHeight);
        }
        int totalHeight = tempHeight + ((sentences.length - 1) * SENTENCE_SPACING);
        //System.out.println("totalHeight: " + totalHeight + "\n");
        int iniY = (int) ((game.getHeight() / 2) - (totalHeight / 2));
        defineY(iniY);
    }

    private void defineY(int tempY) {
        for (int i = 0; i < sentences.length; i++) {
            rectangles[i].setY(tempY);
            for (int j = 0; j < y[i].length; j++) {
                y[i][j] = tempY;
                tempY += game.getFontDataBase().getFontHeight() + LINE_SPACING;
            }
            tempY += SENTENCE_SPACING - LINE_SPACING;
        }
    }

    private void log() {
        for (int i = 0; i < sentences.length; i++) {
            System.out.println("sentence: " + i);
            for (int j = 0; j < x[i].length; j++) {
                System.out.println("x[" + j + "] " + x[i][j]);
                System.out.println("y[" + j + "] " + y[i][j]);
                System.out.println("-----------------------");
            }
            System.out.println("");
        }
    }

    @Override
    public void updateScene() {
        int mx = game.getMouseX();
        int my = game.getMouseY();
        focusedRectangle = Rectangle.NO_FOCUS;
        for (int i = 0; i < rectangles.length; i++) {
            if (rectangles[i].isOnArea(mx, my)) {
                focusedRectangle = i;
                break;
            }
        }
    }

    @Override
    public void renderScene(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0,0, game.getWidth(), game.getHeight());
        //renderRectangles(g);
        for (int i = 0; i < sentences.length; i++) {
            String[] spl = sentences[i].split("@");
            for (int j = 0; j < spl.length; j++) { //corre as linhas
                int tempX = x[i][j];
                int tempY = y[i][j];
                for (int k = 0; k < spl[j].length(); k++) { //corre as letras
                    char c = spl[j].charAt(k);

                    if (focusedRectangle == i) {
                        g.drawImage(game.getFontFocus().get(c), tempX, tempY, null);
                    } else {
                        g.drawImage(game.getFontDataBase().get(c), tempX, tempY, null);
                    }

                    tempX += game.getFontDataBase().get(c).getWidth();
                }
            }
        }
    }

    private void renderRectangles(Graphics g) {
        for (int i = 0; i < rectangles.length; i++) {
            rectangles[i].render(g);
        }
    }

}
