package nona.mi.main;

import nona.mi.button.ButtonGroup;
import nona.mi.jukebox.MyJukeBox;
import nona.mi.save.Save;
import nona.mi.scene.SaveMenuScene;
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
    protected int scene;
    protected int pack;

    protected MyJukeBox packJukebox;
    private String currentSound;

    protected ScenePackage packBasis;
    protected Scene sceneBasis;

    protected boolean up;
    protected boolean lockUp;
    protected boolean down;
    protected boolean lockDown;
    protected boolean left;
    protected boolean lockLeft;
    protected boolean right;
    protected boolean lockRight;
    protected boolean space;
    protected boolean lockSpace;

    //
    protected  boolean escape; //todo : DEL
    protected boolean lockEscape;

    private BufferedImage frame;

    protected Save save;

    protected Scene loadScene;
    protected Scene mainMenu;
    protected SaveMenuScene saveMenuScene;
    //todo : colocar no hashmap


    //
    protected HashMap<Integer, BufferedImage> screenshots;

    //
    protected ButtonGroup yn;



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

    private void update() {
        updateClass();
    }

    public abstract void updateClass();

    //*
    private void render() {
        bs = canvas.getBufferStrategy();

        //tendo que usar uma imagem para ter o screenshot da tela.
        //pelo que vi, reduziu +-200 fps
        frame = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics g = frame.getGraphics();
        g.clearRect(0, 0, width, height);
        renderClass(g);

        this.g = bs.getDrawGraphics();
        this.g.drawImage(frame, 0, 0, null);

        g.dispose();
        this.g.dispose();

        bs.show();
    }
    //*/

    /*
    private void render() {
        bs = canvas.getBufferStrategy();
        g = bs.getDrawGraphics();
        g.clearRect(0, 0, width, height);
        renderClass(g);
        g.dispose();
        bs.show();
    }
    //*/

    public abstract void renderClass(Graphics g);

    @Override
    public void run() {
        if (gameLoopStyle == HARD_GAME_LOOP) {
            hardGameLoop();
        } else {
            smoothGameLoop();
        }
    }

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

    public void nextScene() {
        if (sceneBasis.getNextScene() == Scene.LAST_SCENE) {
            pack = sceneBasis.getNextPack();
            scene = 0;
            sceneBasis.reset();
            sceneBasis = loadScene;
            loadPack();
        } else {
            scene = sceneBasis.getNextScene();
            sceneBasis.reset();
            sceneBasis = packBasis.get(scene);
        }
    }

    public void loadPack() {
        sceneBasis = loadScene;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                packBasis = new ScenePackage();

                if (packJukebox != null) {
                    closePackJukebox();
                }
                currentSound = null;
                packJukebox = new MyJukeBox();

                initPacks();

                sceneBasis = packBasis.get(scene);
            }
        });
        thread.start();
    }

    public void loadPack(int pack, int scene) {
        sceneBasis.reset();
        sceneBasis = loadScene;
        this.pack = pack;
        this.scene = scene;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                packBasis = new ScenePackage();

                if (packJukebox != null) {
                    closePackJukebox();
                }
                currentSound = null;
                packJukebox = new MyJukeBox();

                initPacks();

                sceneBasis = packBasis.get(scene);
            }
        });
        thread.start();
    }

    public abstract void initPacks();

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


    //INPUT-----------------------------------------------------------------

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
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (!lockLeft) {
                lockLeft = true;
                left = true;
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (!lockRight) {
                lockRight = true;
                right = true;
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (!lockSpace) {
                lockSpace = true;
                space = true;
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if (!lockEscape) {
                lockEscape = true;
                escape = true;
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
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            lockLeft = false;
            left = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            lockRight = false;
            right = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            lockSpace = false;
            space = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            lockEscape = false;
            escape = false;
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

    //----------------------------------------------------------------------



    public void testFill(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, width, height);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setShowLoopLog(boolean showLoopLog) {
        this.showLoopLog = showLoopLog;
    }

    public int getFps() {
        return fps;
    }

    public float getSpeedAdjust() {
        return speedAdjust;
    }

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

    public MyJukeBox getStandardJukeBox() {
        return standardJukeBox;
    }

    public String getCurrentSound() {
        return currentSound;
    }

    public void setCurrentSound(String currentSound) {
        this.currentSound = currentSound;
    }

    public MyJukeBox getPackJukebox() {
        return packJukebox;
    }


    public boolean isUp() {
        return up;
    }

    public boolean isDown() {
        return down;
    }

    public boolean isLeft() {
        return left;
    }

    public boolean isRight() {
        return right;
    }

    public boolean isSpace() {
        return space;
    }

    public boolean isEscape() {
        return escape;
    }


    public ScenePackage getPackBasis() {
        return packBasis;
    }

    public void setDirectScene(Scene sceneBasis) {
        //metodo usado por datamanager para retornar para a cena a qual o chamou
        //pegar id da cena
        this.sceneBasis.reset();
        this.sceneBasis = sceneBasis;
    }

    //TODO : HERE


    public void setSceneBasisWithoutReset(Scene scene) {
        sceneBasis = scene;
    }

    public int getPack() {
        return pack;
    }

    public int getScene() {
        return scene;
    }

    public Scene getSceneFromCurrentPack(int id) {
        //isso resume o getpack.getscene
        return packBasis.get(id);
    }

    public BufferedImage getFrame() {
        return frame;
    }

    public Save getSave() {
        return save;
    }

    public void setSave(Save save) {
        this.save = save;
    }










    //todo : temp : so testando a mudanca de cena. APAGAR!
    public Scene getLoadScene() {
        return loadScene;
    }

    public Scene getMainMenu() {
        return mainMenu;
    }

    public SaveMenuScene getSaveMenuScene() {
        return saveMenuScene;
    }

    public HashMap<Integer, BufferedImage> getScreenshots() {
        return screenshots;
    }

    public ButtonGroup getYn() {
        return yn;
    }

    public Scene getSceneBasis() {
        return sceneBasis;
    }

    //
    public void returntoMainMenu() {
        sceneBasis.reset();
        sceneBasis = mainMenu;
        closePackJukebox();
        currentSound = null;
        packJukebox = null;
    }


    public void setScene(int scene) {
        this.scene = scene;
    }
}
