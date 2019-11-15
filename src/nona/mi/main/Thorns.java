package nona.mi.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import nona.mi.db.FontDataBase;
import nona.mi.image.BaseImage;
import nona.mi.image.Coordinates;
import nona.mi.loader.DialogLoader;
import nona.mi.loader.ImageLoader;
import nona.mi.loader.MyJukeBox;
import nona.mi.efx.Fade;
import nona.mi.image.ImageEfx;
import nona.mi.loader.TextLoader;
import nona.mi.scene.EfxScene;
import nona.mi.scene.FadeScene;
import nona.mi.scene.FadeTopBottomScene;
import nona.mi.scene.LoadScene;
import nona.mi.scene.Scene;
import nona.mi.scene.ScenePackage;
import nona.mi.scene.StandardScene;
import nona.mi.scene.TestScene;

public class Thorns extends Game {

    private boolean up;
    private boolean lockUp;

    private boolean down;
    private boolean lockDown;

    private boolean left;
    private boolean lockLeft;

    private boolean right;
    private boolean lockRight;

    private boolean space;
    private boolean lockSpace;

    private boolean showScene;
    private int scene;
    private int pack;

    private ScenePackage packBasis;
    private Scene sceneBasis;

    private String currentSound;
    private MyJukeBox myJukeBox;

    //global res
    private FontDataBase fontDataBase;
    private FontDataBase fontFocus;
    private BaseImage nameBg;
    private LoadScene loadScene;
    private BaseImage textArea;
    private BufferedImage choicebg;
    private BufferedImage pointer;
    private ImageEfx setasAnim;
    public static final String AUDIO_CHOICE = "ac";

    public Thorns(int width, int height, String title, int gameLoopStyle) {

        super(width, height, title, gameLoopStyle);

        //FONTE DA VN
        fontDataBase = new FontDataBase("/res/font/myfont.png", "/res/font/text.txt");
        fontFocus = new FontDataBase("/res/font/myfontfocus.png", "/res/font/text.txt");

        //AREA DO DIALOGO
        BufferedImage tempTextArea = ImageLoader.loadImage("/res/font/txtarea.png");
        textArea = new BaseImage(tempTextArea, 0, (int)(getHeight() - tempTextArea.getHeight()));

        //BACKGROUND PARA TODOS OS NOMES
        BufferedImage tempNameBg = ImageLoader.loadImage("/res/font/nameBg.png");
        nameBg = new BaseImage(tempNameBg, 0, getHeight() - tempTextArea.getHeight() - tempNameBg.getHeight());

        //BACKGROUND PARA OS TEXTOS DE CHOICE
        choicebg = ImageLoader.loadImage("/res/font/choice2.png");
        //todo : criar uma imagem para o menu principal -- nova fonte tambem

        //POINTER
        pointer = ImageLoader.loadImage("/res/font/pointer.png");

        //ANIMACAO SETAS
        BufferedImage uno = ImageLoader.loadImage("/res/menu/uno.png");
        BufferedImage dos = ImageLoader.loadImage("/res/menu/dos.png");
        BufferedImage tres = ImageLoader.loadImage("/res/menu/tres.png");
        BufferedImage[] setas = {uno, dos, tres};
        Coordinates setasCoord = new Coordinates(getWidth() - uno.getWidth(), getHeight() - uno.getHeight());
        setasAnim = new ImageEfx(this, setas, setasCoord, 0.15f , ImageEfx.LOOP);

        //PACK E SCENE
        pack = 1;
        scene = 0;

        //LOAD SCENE
        loadScene = new LoadScene(this, new BaseImage(ImageLoader.loadImage("/res/bg/load.png"), 0, 0));
        sceneBasis = loadScene;

        loadPack();
    }

    @Override
    public void updateClass() {
        sceneBasis.update();
        space = false;
        up = false;
        down = false;
        left = false;
        right = false;
    }

    @Override
    public void renderClass(Graphics g) {
        super.testFill();
        sceneBasis.render(g);
        if (showScene) {
            g.setColor(Color.WHITE);
            g.drawString("scene: " + scene, 5, 15);
        }
    }

    private synchronized void initPacks() {
        if (pack == 0) {
            initPack0();
        } else {
            initPackTest(); //pack1
        }
    }

    private void initPackTest(){
        ScenePackage packTest = new ScenePackage();
        setMyJukeBox(new MyJukeBox());

        //MainMenuScene mainMenuScene = new MainMenuScene(this, 66);
        //packTest.put(0, mainMenuScene);

        TestScene testScene = new TestScene(this);
        packTest.put(0, testScene);

        packBasis = packTest;
        sceneBasis = packBasis.get(0);
    }

    private void initPack0(){
        ScenePackage pack0 = new ScenePackage();
        setMyJukeBox(new MyJukeBox());

        //LOCAL-----------------------------------

        //BACKGROUND - CENTRO TREINAMENTO
        BufferedImage imgScene0 = ImageLoader.loadImage("/res/bg/trainning-center.png");
        BaseImage bgScene0 = new BaseImage(imgScene0, 0, 0);

        //FADES
        Fade fadeoutSlow = new Fade(this, Fade.SOLID, Fade.SLOW);
        Fade fadeoutFast = new Fade(this, Fade.SOLID, Fade.FAST);
        Fade fadeinFast = new Fade(this, Fade.TRANSPARENT, Fade.FAST);

        //ROSE
        BufferedImage rose = ImageLoader.loadImage("/res/chars/rose.png");
        Coordinates roseCoordScene2 = new Coordinates(-30, 0);

        //TULI
        BufferedImage tuli = ImageLoader.loadImage("/res/chars/tuli.png");
        Coordinates tuliCoordScene2 = new Coordinates(430, 0);

        //LIVROS - imagens
        BufferedImage livro1Buffered = ImageLoader.loadImage("/res/pack0/livro1.png");
        BufferedImage livro2Buffered = ImageLoader.loadImage("/res/pack0/livro2.png");
        BufferedImage livro3Buffered = ImageLoader.loadImage("/res/pack0/livro3.png");

        //MAPA COM AS FALAS
        HashMap<Integer, String> sentences = DialogLoader.load(TextLoader.load("/res/dialog/scene0.txt"));

        //----------------------------------------

        //cena 0
        FadeScene scene0 = new FadeScene(this, bgScene0, fadeoutSlow, 1);
        scene0.createSound("/res/audio/scene0.wav", MyJukeBox.LOOP);
        pack0.put(0, scene0);

        //cena 1
        String txtScene1 = sentences.get(1);
        StandardScene scene1 = new StandardScene(this, bgScene0, txtScene1, 2);
        pack0.put(1, scene1);

        //cena 2
        ImageEfx efxRose = new ImageEfx(this, rose, roseCoordScene2);
        efxRose.setAlpha(ImageEfx.TRANSPARENT, 0.02f);
        efxRose.setMoveTo(0, 0, 1, 0); //rose e tuli estao movendo 30px
        ImageEfx efxTuli = new ImageEfx(this, tuli, tuliCoordScene2);
        efxTuli.setAlpha(ImageEfx.TRANSPARENT, 0.02f);
        efxTuli.setMoveTo(400, 0, 1, 0);
        ImageEfx[] images = {efxRose, efxTuli};
        EfxScene scene2 = new EfxScene(this, images, bgScene0, 3);
        pack0.put(2, scene2);

        //cena 3
        BaseImage roseSc3 = new BaseImage(rose, 0, 0);
        BaseImage tuliSc3 = new BaseImage(tuli, 400, 0);
        BaseImage[] characters = {roseSc3, tuliSc3};
        String txtScene3 = sentences.get(3);
        StandardScene scene3 = new StandardScene(this, bgScene0, txtScene3, 4);
        scene3.setCharacters(characters);
        pack0.put(3, scene3);

        //cena 4 //0.05f old speed
        BaseImage[] bottom4 = {bgScene0, roseSc3, tuliSc3};
        ImageEfx bgSc4 = new ImageEfx(this, imgScene0, new Coordinates(0, 0));
        bgSc4.setAlpha(ImageEfx.TRANSPARENT, 0.025f);
        ImageEfx tuliSc4 = new ImageEfx(this, tuli, (new Coordinates((float)((getWidth() / 2) - (tuli.getWidth() / 2)), 0)));
        tuliSc4.setAlpha(ImageEfx.TRANSPARENT, 0.025f);
        ImageEfx[] top4 = {bgSc4, tuliSc4};
        FadeTopBottomScene scene4 = new FadeTopBottomScene(this, bottom4, top4, 5);
        scene4.setTextArea(textArea);
        pack0.put(4, scene4);

        //cena 5
        String txtScene5 = sentences.get(5);
        StandardScene scene5 = new StandardScene(this, bgScene0, txtScene5, 6);
        BaseImage tuliSc5 = new BaseImage(tuli, (int)((getWidth() / 2) - (tuli.getWidth()/2)), 0);
        BaseImage[] chars5 = {tuliSc5};
        scene5.setCharacters(chars5);
        pack0.put(5, scene5);

        //cena 6
        BaseImage[] bottom6 = {bgScene0, tuliSc5};
        ImageEfx bgSc6 = new ImageEfx(this, imgScene0, new Coordinates(0, 0));
        bgSc6.setAlpha(ImageEfx.TRANSPARENT, 0.01f);
        ImageEfx roseSc6 = new ImageEfx(this, rose, new Coordinates(((float)(getWidth() / 2) - (float)(rose.getWidth() / 2)), 0));
        roseSc6.setAlpha(ImageEfx.TRANSPARENT, 0.01f);
        ImageEfx[] top6 = {bgSc6, roseSc6};
        FadeTopBottomScene scene6 = new FadeTopBottomScene(this, bottom6, top6, 7);
        scene6.setTextArea(textArea);
        pack0.put(6, scene6);

        //cena 7
        String txtScene7 = sentences.get(7);
        StandardScene scene7 = new StandardScene(this, bgScene0, txtScene7, 8);
        BaseImage roseSc7 = new BaseImage(rose, getWidth() / 2 - rose.getWidth() / 2, 0);
        scene7.setCharacters(new BaseImage[]{roseSc7});
        pack0.put(7, scene7);

        //cena 8
        FadeScene scene8 = new FadeScene(this, new BaseImage[]{bgScene0, roseSc7}, fadeinFast, 9);
        pack0.put(8, scene8);

        //cena 9
        BaseImage livro1 = new BaseImage(livro1Buffered);
        FadeScene scene9 = new FadeScene(this, livro1, fadeoutFast, 10);
        pack0.put(9, scene9);

        //cena 10
        String txtScene10 = sentences.get(10);
        StandardScene scene10 = new StandardScene(this, livro1, txtScene10, 11);
        pack0.put(10, scene10);

        //cena 11
        ImageEfx efxLivro2 = new ImageEfx(this, livro2Buffered, new Coordinates(0, 0));
        efxLivro2.setAlpha(ImageEfx.TRANSPARENT, 0.02f);
        EfxScene scene11 = new EfxScene(this, new ImageEfx[]{efxLivro2}, livro1, 12);
        //scene11.setTextArea(textArea);
        pack0.put(11, scene11);

        //cena 12
        String txtScene12 = sentences.get(12);
        BaseImage livro2 = new BaseImage(livro2Buffered);
        BaseImage[] imagesSc12 = {livro1, livro2};
        StandardScene scene12 = new StandardScene(this, imagesSc12, txtScene12, 13);
        pack0.put(12, scene12);

        //cena 13
        ImageEfx efxLivro3 = new ImageEfx(this, livro3Buffered, new Coordinates(0, 0));
        efxLivro3.setAlpha(ImageEfx.TRANSPARENT, 0.02f);
        EfxScene scene13 = new EfxScene(this, new ImageEfx[]{efxLivro3}, imagesSc12, 14);
        pack0.put(13, scene13);

        //scene 14
        String txtScene14 = sentences.get(14);
        BaseImage livro3 = new BaseImage(livro3Buffered);
        StandardScene scene14 = new StandardScene(this, new BaseImage[]{livro1, livro2, livro3}, txtScene14, 0);
        pack0.put(14, scene14);


        packBasis = pack0;
        sceneBasis = packBasis.get(scene);
    }

    public void nextScene(int scene) {
        this.scene = scene;
        if (scene == Scene.LAST_SCENE) {

            this.pack = sceneBasis.getNextPack();
            this.scene = 0;

            sceneBasis = loadScene;

            loadPack();

        } else {
            sceneBasis = packBasis.get(scene);
        }
    }

    public void nextScene(int pack, int scene){
        sceneBasis = loadScene;
        this.pack = pack;
        this.scene = scene;
        loadPack();
    }

    private void loadPack(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                initPacks();
            }
        });
        thread.start();
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

    public BufferedImage getPointer() {
        return pointer;
    }

    public boolean isSpace() {
        return space;
    }

    public FontDataBase getFontDataBase(){
        return fontDataBase;
    }

    public FontDataBase getFontFocus(){
        return fontFocus;
    }

    public BufferedImage getChoicebg(){
        return choicebg;
    }

    public BaseImage getTextArea(){
        return textArea;
    }

    public BaseImage getNameBg(){
        return nameBg;
    }

    public ImageEfx getSetasAnim(){
        return setasAnim;
    }

    public String getCurrentSound(){
        return this.currentSound;
    }

    public void setCurrentSound(String currentSound){
        this.currentSound = currentSound;
    }

    public  MyJukeBox getMyJukeBox(){
        return myJukeBox;
    }

    public void setMyJukeBox(MyJukeBox myJukeBox){
        this.myJukeBox = myJukeBox;
    }

    public void setShowScene(boolean showScene) {
        this.showScene = showScene;
    }

    public int getScene() {
        return scene;
    }

    public int getPack() {
        return pack;
    }

    public void showLoadScene() {
        sceneBasis = loadScene;
    }

    // todo : imageEfx e Stan, com os arrays estao ok, nao quero alterar
    // todo : definir som para cada cena no construtor - isso evita dar load numa cena e vir sem som (o qual so inicia em outra cena especifica)
}