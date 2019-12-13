package nona.mi.scene;

import nona.mi.button.RectButton;
import nona.mi.loader.ImageLoader;
import nona.mi.main.Game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class TestButtonScene2 extends Scene implements Runnable{

    private RectButton rectButton;
    private boolean confirm;
    private RectButton yes;
    private RectButton no;
    private Thread thread;


    public TestButtonScene2(Game game) {
        super(game);

        BufferedImage stan = ImageLoader.loadImage("/res/buttons/uno.png");
        BufferedImage focus = ImageLoader.loadImage("/res/buttons/dos.png");
        String audioName = "click";

        rectButton = new RectButton(game);
        rectButton.setImages(stan, focus, 50, 50);
        rectButton.setAudioName(audioName);

        yes = new RectButton(game);
        yes.setImages(stan, focus, 50, 150);
        yes.setAudioName(audioName);

        no = new RectButton(game);
        no.setImages(stan, focus, 50, 250);
        no.setAudioName(audioName);

        thread = new Thread(this);

    }

    @Override
    public void update() {
        super.update();

        if (!confirm) {
            rectButton.update();
            if (rectButton.isClicked()) {
                confirm = true;
            }
        } else {
            if (!thread.isAlive()) {
                yes.update();
                no.update();
                if (yes.isClicked() || no.isClicked()) {
                    //confirm = false;
                    thread = new Thread(this);
                    thread.start();
                    //reset();
                }
            }
        }

    }

    @Override
    public void run() {
        System.out.println("iniciando a thread");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("foi");
        confirm = false;
        reset();
    }

    @Override
    public void render(Graphics g) {
        rectButton.render(g);
        if (confirm) {
            yes.render(g);
            no.render(g);
        }
    }

    @Override
    public void reset() {
        super.reset();
        rectButton.reset();
        yes.reset();
        no.reset();
        confirm = false;
    }


}
