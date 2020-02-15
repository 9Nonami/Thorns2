package nona.mi.main;

import nona.mi.button.ButtonGroup;
import nona.mi.jukebox.MyJukeBox;
import nona.mi.save.Save;
import nona.mi.scene.Scene;
import nona.mi.scene.ScenePackage;

import javax.sound.sampled.Clip; //todo : encapsular o close do audio
import javax.swing.JFrame;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public abstract class Game implements Runnable, KeyListener, MouseListener, MouseMotionListener {

    private JFrame jframe;
    private BufferStrategy bs;
    private Canvas canvas;

    private Graphics g;
    private boolean running;
    private Thread t;

    private int width; //854
    private int height; //480

    private double limit;
    private int fps;
    private float speedAdjust;

    public static final int HARD_GAME_LOOP = 0;
    public static final int SMOOTH_GAME_LOOP = 1;
    private int gameLoopStyle;
    private boolean showLoopLog;

    private boolean clicked;
    private int mouseX;
    private int mouseY;

    protected MyJukeBox standardJukeBox;

    protected MyJukeBox packJukebox;
    private String currentSound;

    protected ScenePackage packBasis;
    protected Scene sceneBasis;

    protected boolean up;
    protected boolean lockUp;
    protected boolean down;
    protected boolean lockDown;
    protected boolean space;
    protected boolean lockSpace;

    private BufferedImage frame;

    protected Save save;

    protected HashMap<Integer, Scene> publicScenes;


    // todo : ver se nao esta inutil
    protected HashMap<Integer, BufferedImage> screenshots;

    //
    protected ButtonGroup yn;

    private boolean showScene;



    //ESSENCIAL-------------------------------------------------------------------------------

    public Game(int width, int height, String title, int gameLoopStyle) {

        this.width = width;
        this.height = height;
        this.gameLoopStyle = gameLoopStyle;

        if (gameLoopStyle == HARD_GAME_LOOP){
            fps = 30;
        } else {
            fps = 60;
        }

        limit = (double) 1_000_000_000 / fps;

        //se for 30fps, roda com os valores definidos
        //se for 60fps, nao deixa ser o dobro da velocidade
        if (gameLoopStyle == HARD_GAME_LOOP){
            speedAdjust = 1;
        } else {
            speedAdjust = 0.5f;
        }

        jframe = new JFrame(title);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        canvas = new Canvas();
        Dimension d = new Dimension(width, height);
        canvas.setPreferredSize(d);
        canvas.setMinimumSize(d);
        canvas.setMaximumSize(d);

        jframe.add(canvas, BorderLayout.CENTER);
        jframe.pack();
        jframe.setLocationRelativeTo(null);
        //jframe.setResizable(false);

        jframe.addKeyListener(this);

        canvas.createBufferStrategy(2);

        jframe.setIgnoreRepaint(true);
        canvas.setFocusable(false);
        canvas.addMouseListener(this);
        canvas.addMouseMotionListener(this);

        standardJukeBox = new MyJukeBox();

        t = new Thread(this);
    }

    public void start() {
        running = true;
        jframe.setVisible(true);
        t.start();
    }

    @Override
    public void run() {
        if (gameLoopStyle == HARD_GAME_LOOP) {
            hardGameLoop();
        } else {
            smoothGameLoop();
        }
    }

    //----------------------------------------------------------------------------------------



    //UR--------------------------------------------------------------------------------------

    private void update() {
        sceneBasis.update();
        resetKeys();
    }

    private void render() {
        bs = canvas.getBufferStrategy();

        //tendo que usar uma imagem para ter o screenshot da tela.
        //pelo que vi, reduziu +-200 fps
        frame = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics g = frame.getGraphics();
        g.clearRect(0, 0, width, height);

        testFill(g);
        sceneBasis.render(g);

        //DESENHA O NUMERO ATUAL DA CENA
        if (showScene) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, 130, 20);
            g.setColor(Color.GREEN);
            g.drawString("PACK: " + sceneBasis.getPackId() + " | SCENE: " + sceneBasis.getSceneId(), 5, 15);
        }

        this.g = bs.getDrawGraphics();
        this.g.drawImage(frame, 0, 0, null);

        g.dispose();
        this.g.dispose();

        bs.show();
    }

    public void testFill(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, width, height);
    }

    //----------------------------------------------------------------------------------------



    //GAME LOOP-------------------------------------------------------------------------------

    public void hardGameLoop() {
        double ini = 0;
        double end = 0;
        double delta = 0;
        double wait = 0;
        while (running) {
            ini = System.nanoTime();
            update();
            render();
            end = System.nanoTime();
            delta = end - ini;
            wait = limit - delta;
            if (wait > 0) {
                try {
                    wait = wait / 1_000_000;
                    if (showLoopLog) {
                        System.out.println("wait: " + (int) wait + " millis");
                    }
                    Thread.sleep((long) wait);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                if (showLoopLog) {
                    System.out.println("overload: " + (int) wait / 1_000_000);
                }
            }
        }
    }

	public void smoothGameLoop() {
		double ini = 0;
		double end = 0;
		double delta = 0;
		
		double timer = 0;
		int fps = 0;
		int ups = 0;

		timer = System.currentTimeMillis();
		ini = System.nanoTime();
		while(running){
			end = System.nanoTime();
			delta += end - ini;
			ini = end;
			fps++;
			while (delta >= limit) {
				ups++;
				update();
				delta -= limit;
			}
			render();
			try {
				Thread.sleep(1);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (System.currentTimeMillis() - timer >= 1000) {
				timer += 1000;
				if (showLoopLog) {
                    System.out.println("ups: " + ups);
                    System.out.println("fps: " + fps);
                    System.out.println("");
                }
				ups = 0;
				fps = 0;
			}
		}
	}

    //----------------------------------------------------------------------------------------



    //ESPECIFICO------------------------------------------------------------------------------

    private void closePackJukebox() {
        //todo : ver se nao eh melhor colocar em uma thread
        //not sure if this is the best way to iterate and modify the hashmap contents
        if (packJukebox != null) {
            for (Map.Entry<String, Clip> temp : packJukebox.getClips().entrySet()) {
                System.out.println("closing: " + temp.getKey());
                if (temp.getValue().isRunning()) {
                    temp.getValue().stop();
                }
                temp.getValue().close();
            }
        }
    }

    //----------------------------------------------------------------------------------------



    //MUDANCA DE CENA-------------------------------------------------------------------------

    public void nextScene() {

        int tempNextPack;
        int tempNextScene;

        if (sceneBasis.getNextScene() == Scene.LAST_SCENE) {

            tempNextPack = sceneBasis.getNextPack();
            tempNextScene = 0;

            loadPack(tempNextPack, tempNextScene);

        } else {
            tempNextScene = sceneBasis.getNextScene();
            sceneBasis.reset();
            sceneBasis = getSceneFromCurrentPack(tempNextScene);
        }

    }

    public Scene getSceneFromCurrentPack(int id) {
        return packBasis.get(id);
    }

    public void loadPack(int tempNextPack, int tempNextScene) {

        sceneBasis.reset();
        sceneBasis = getSceneFromPublicScenes(Scene.LOAD_SCENE);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                //todo : apagar
                try {
                    Thread.sleep(1000);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                packBasis = new ScenePackage();

                if (packJukebox != null) {
                    closePackJukebox();
                }
                currentSound = null;
                packJukebox = new MyJukeBox();

                initPacks(tempNextPack);

                //ateh o momento, loadScene nao tem nada para ser resetado
                sceneBasis.reset();

                sceneBasis = getSceneFromCurrentPack(tempNextScene);
            }
        });
        thread.start();
    }

    public abstract void initPacks(int tempNextPack);

    public Scene getSceneFromPublicScenes(int id) {
        return publicScenes.get(id);
    }

    public void setDirectScene(int id) {
        sceneBasis.reset();
        sceneBasis = getSceneFromCurrentPack(id);
    }

    public void setDirectSceneFromPublicScenes(int id) {
        sceneBasis.reset();
        sceneBasis = getSceneFromPublicScenes(id);
    }

    public void setSceneBasisFromPublicScenesWithoutReset(int id) {
        sceneBasis = getSceneFromPublicScenes(id);
    }

    public void returnToMainMenu() {
        sceneBasis.reset();
        sceneBasis = getSceneFromPublicScenes(Scene.MAIN_MENU_SCENE);
        closePackJukebox();
        currentSound = null;
        packJukebox = null;
    }

    //----------------------------------------------------------------------------------------



    //INPUT-----------------------------------------------------------------------------------

    private void resetKeys() {
        space = false;
        up = false;
        down = false;
        setClicked(false);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            if (!lockUp) {
                lockUp = true;
                up = true;
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            if (!lockDown) {
                lockDown = true;
                down = true;
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (!lockSpace) {
                lockSpace = true;
                space = true;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            lockUp = false;
            up = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            lockDown = false;
            down = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            lockSpace = false;
            space = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        clicked = true;
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        mouseX = mouseEvent.getX();
        mouseY = mouseEvent.getY();
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        mouseX = mouseEvent.getX();
        mouseY = mouseEvent.getY();
    }

    //----------------------------------------------------------------------------------------



    //GS--------------------------------------------------------------------------------------

    //TELA
    public void setShowScene(boolean showScene) {
        this.showScene = showScene;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public BufferedImage getFrame() {
        return frame;
    }

    public HashMap<Integer, BufferedImage> getScreenshots() {
        return screenshots;
    }

    public float getSpeedAdjust() {
        return speedAdjust;
    }

    public int getFps() {
        return fps;
    }

    public void setShowLoopLog(boolean showLoopLog) {
        this.showLoopLog = showLoopLog;
    }


    //INPUTS
    public boolean isClicked() {
        return clicked;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    public boolean isUp() {
        return up;
    }

    public boolean isDown() {
        return down;
    }

    public boolean isSpace() {
        return space;
    }

    public ButtonGroup getYn() {
        return yn;
    }


    //JUKEBOX
    public MyJukeBox getStandardJukeBox() {
        return standardJukeBox;
    }

    public MyJukeBox getPackJukebox() {
        return packJukebox;
    }

    public String getCurrentSound() {
        return currentSound;
    }

    public void setCurrentSound(String currentSound) {
        this.currentSound = currentSound;
    }


    //SCENEBASIS
    public Scene getSceneBasis() {
        return sceneBasis;
    }

    //----------------------------------------------------------------------------------------

}
