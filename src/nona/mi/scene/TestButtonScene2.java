package nona.mi.scene;

import nona.mi.button.RectButton;
import nona.mi.loader.ImageLoader;
import nona.mi.main.Game;

import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;

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
                if (yes.isClicked()) {
                    thread = new Thread(this);
                    thread.start();
                } else if (no.isClicked()) {
                    reset();
                }
            }
        }

    }

    @Override
    public void run() {
        System.out.println("iniciando a thread");

        String home = System.getProperty("user.home");
        String image = home + "\\thorns\\asd.png";
        File file = new File(image);


        try {
            //file.createNewFile();
            ImageIO.write(game.getFrame(), "png", file);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("foi");

        reset();
    }

    @Override
    public void renderScene(Graphics g) {
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
