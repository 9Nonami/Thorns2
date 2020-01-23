package nona.mi.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import nona.mi.button.Button;
import nona.mi.button.ButtonGroup;
import nona.mi.button.RectButton;
import nona.mi.db.FontDataBase;
import nona.mi.image.BaseImage;
import nona.mi.image.Coordinates;
import nona.mi.loader.DialogLoader;
import nona.mi.loader.ImageLoader;
import nona.mi.jukebox.MyJukeBox;
import nona.mi.efx.Fade;
import nona.mi.image.ImageEfx;
import nona.mi.loader.ScreenshotLoader;
import nona.mi.loader.TextLoader;
import nona.mi.save.Save;
import nona.mi.scene.EfxScene;
import nona.mi.scene.FadeScene;
import nona.mi.scene.FadeTopBottomScene;
import nona.mi.scene.LoadScene;
import nona.mi.scene.SaveMenuScene;
import nona.mi.scene.Scene;
import nona.mi.scene.StandardScene;
import nona.mi.scene.TestButtonScene;

public class Thorns extends Game {

    private FontDataBase fontDataBase;
    private FontDataBase fontFocus;
    private BaseImage nameBg;
    private BaseImage textArea;
    private BufferedImage choicebg;
    private BufferedImage pointer;
    private ImageEfx setasAnim;
    private boolean showScene;

    private ButtonGroup sceneMenu; //save, load, copy, del



    public Thorns(int width, int height, String title, int gameLoopStyle) {

        super(width, height, title, gameLoopStyle);

        //FONTE DA VN
        fontDataBase = new FontDataBase("/res/font/myfont.png", "/res/font/text.txt");
        fontFocus = new FontDataBase("/res/font/myfontfocus.png", "/res/font/text.txt");

        //AREA DO DIALOGO
        BufferedImage tempTextArea = ImageLoader.loadImage("/res/misc/textarea.png");// /res/font/txtarea.png
        textArea = new BaseImage(tempTextArea, 10, (int)(getHeight() - tempTextArea.getHeight() - 10));

        //BACKGROUND PARA TODOS OS NOMES
        BufferedImage tempNameBg = ImageLoader.loadImage("/res/font/nameBg.png");
        nameBg = new BaseImage(tempNameBg, 0, getHeight() - tempTextArea.getHeight() - tempNameBg.getHeight());

        //BACKGROUND PARA OS TEXTOS DE CHOICE
        choicebg = ImageLoader.loadImage("/res/font/choice2.png");
        //todo : criar uma imagem para o menu principal -- nova fonte tambem

        //POINTER
        pointer = ImageLoader.loadImage("/res/font/pointer.png");

        //ANIMACAO SETAS
        BufferedImage s1 = ImageLoader.loadImage("/res/misc/s1.png");
        BufferedImage s2 = ImageLoader.loadImage("/res/misc/s2.png");
        BufferedImage s3 = ImageLoader.loadImage("/res/misc/s3.png");
        BufferedImage s4 = ImageLoader.loadImage("/res/misc/s4.png");
        BufferedImage s5 = ImageLoader.loadImage("/res/misc/s5.png");

        BufferedImage[] setas = {s1, s2, s3, s4, s5};
        Coordinates setasCoord = new Coordinates(getWidth() - s1.getWidth(), getHeight() - s1.getHeight());
        setasAnim = new ImageEfx(this, setas, setasCoord, 0.25f , ImageEfx.LOOP); //0.15f

        //PACK E SCENE
        scene = 0;
        pack = 0;

        //LOAD SCENE
        loadScene = new LoadScene(this, new BaseImage(ImageLoader.loadImage("/res/bg/load.png"), 0, 0));

        //MAIN MENU SCENE
        TestButtonScene testButtonScene = new TestButtonScene(this); //todo : tirar o 2
        mainMenu = testButtonScene;

        //FADESCENE logo
        Fade fadeLogo = new Fade(this, Fade.SOLID, Fade.FAST);
        fadeLogo.setFadeOutIn(true);
        FadeScene fs = new FadeScene(this, new BaseImage(ImageLoader.loadImage("/res/menu/logo.png"), 0, 0), fadeLogo, Scene.LAST_SCENE);
        fs.setDirectScene(mainMenu);
        sceneBasis = fs;

        //STANDARD AUDIOS
        standardJukeBox.load("/res/audio/click.wav", "click");

        //MENU COM SAVE, LOAD, COPY E DEL
        BufferedImage saveImage = ImageLoader.loadImage("/res/misc/save.png");
        BufferedImage loadImage = ImageLoader.loadImage("/res/misc/load.png");
        BufferedImage copyImage = ImageLoader.loadImage("/res/misc/copy.png");
        BufferedImage deleteImage = ImageLoader.loadImage("/res/misc/delete.png");
        BufferedImage focusImage = ImageLoader.loadImage("/res/misc/focus.png");
        int spacing = 3;
        String audioButtons = "click"; // todo : <<< deixar publico

        RectButton saveButton = new RectButton(this);
        saveButton.setImages(saveImage, focusImage, (textArea.getX() + tempTextArea.getWidth() + spacing), textArea.getY());
        saveButton.setAudioName(audioButtons);
        saveButton.setId(SaveMenuScene.SAVE);

        RectButton loadButton = new RectButton(this);
        loadButton.setImages(loadImage, focusImage, saveButton.getX(), (saveButton.getY() + saveButton.getHeight() + spacing));
        loadButton.setAudioName(audioButtons);
        loadButton.setId(SaveMenuScene.LOAD);

        RectButton copyButton = new RectButton(this);
        copyButton.setImages(copyImage, focusImage, loadButton.getX(), (loadButton.getY() + loadButton.getHeight() + spacing));
        copyButton.setAudioName(audioButtons);
        copyButton.setId(SaveMenuScene.COPY);

        RectButton deleteButton = new RectButton(this);
        deleteButton.setImages(deleteImage, focusImage, copyButton.getX(), (copyButton.getY() + copyButton.getHeight() + spacing));
        deleteButton.setAudioName(audioButtons);
        deleteButton.setId(SaveMenuScene.DEL);

        sceneMenu = new ButtonGroup(new Button[]{saveButton, loadButton, copyButton, deleteButton});


        //SAVE
        save = new Save(12);

        //SCREENSHOTS
        screenshots = ScreenshotLoader.loadScreenshots(save);

        //SAVE MENU
        SaveMenuScene saveMenuScene = new SaveMenuScene(this, save, 6); // todo : save
        saveMenuScene.setImages("/res/buttons/empty-slot.png", "/res/buttons/focused-slot.png");
        saveMenuScene.createSlots(12, 2, 3, 44, 44, 31);
        this.saveMenuScene = saveMenuScene;

    }

    @Override
    public void updateClass() {
        sceneBasis.update();
        resetKeys();
    }

    @Override
    public void renderClass(Graphics g) {
        testFill(g);
        sceneBasis.render(g);//todo

        //DESENHA O NUMERO ATUAL DA CENA
        if (showScene) {
            g.setColor(Color.WHITE);
            g.drawString("scene: " + scene, 5, 15);
        }
    }

    @Override
    public synchronized void initPacks() {
        if (pack == 0) {
            initPack0();
        }
    }

    private void initPack0() {

        //LOCAL-----------------------------------

        //BACKGROUND - CENTRO TREINAMENTO
        BufferedImage imgScene0 = ImageLoader.loadImage("/res/bg/trainning-center.png");
        BaseImage bgScene0 = new BaseImage(imgScene0, 0, 0);

        //FADES
        Fade fadeoutSlow = new Fade(this, Fade.SOLID, Fade.DEMONIAC); //TODO : SLOW
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

        //BACKGROUND AUDIO
        String trainningCenterAudio = "trainningCenterAudio";
        packJukebox.load("/res/audio/scene0.wav", trainningCenterAudio);


        //----------------------------------------

        //cena 0
        FadeScene scene0 = new FadeScene(this, bgScene0, fadeoutSlow, 1);
        scene0.defineSound(trainningCenterAudio, MyJukeBox.LOOP);
        packBasis.put(0, scene0);

        //cena 1
        String txtScene1 = sentences.get(1);
        StandardScene scene1 = new StandardScene(this, bgScene0, setasAnim, 2);
        scene1.setDialog(txtScene1, fontDataBase, textArea, nameBg);
        scene1.defineSound(trainningCenterAudio, MyJukeBox.LOOP);
        scene1.setButtonGroup(sceneMenu);
        packBasis.put(1, scene1);

        //cena 2
        ImageEfx efxRose = new ImageEfx(this, rose, roseCoordScene2);
        efxRose.setAlpha(ImageEfx.TRANSPARENT, 0.02f);
        efxRose.setMoveTo(0, 0, 1, 0); //rose e tuli estao movendo 30px
        ImageEfx efxTuli = new ImageEfx(this, tuli, tuliCoordScene2);
        efxTuli.setAlpha(ImageEfx.TRANSPARENT, 0.02f);
        efxTuli.setMoveTo(400, 0, 1, 0);
        ImageEfx[] images = {efxRose, efxTuli};
        EfxScene scene2 = new EfxScene(this, images, bgScene0, 3);
        scene2.defineSound(trainningCenterAudio, MyJukeBox.LOOP);
        packBasis.put(2, scene2);

        //cena 3
        BaseImage roseSc3 = new BaseImage(rose, 0, 0);
        BaseImage tuliSc3 = new BaseImage(tuli, 400, 0);
        BaseImage[] characters = {roseSc3, tuliSc3};
        String txtScene3 = sentences.get(3);
        StandardScene scene3 = new StandardScene(this, bgScene0, setasAnim, 4);
        scene3.setDialog(txtScene3, fontDataBase, textArea, nameBg);
        scene3.setCharacters(characters);
        scene3.defineSound(trainningCenterAudio, MyJukeBox.LOOP);
        packBasis.put(3, scene3);

        //cena 4 //0.05f old speed
        BaseImage[] bottom4 = {bgScene0, roseSc3, tuliSc3};
        ImageEfx bgSc4 = new ImageEfx(this, imgScene0, new Coordinates(0, 0));
        bgSc4.setAlpha(ImageEfx.TRANSPARENT, 0.025f);
        ImageEfx tuliSc4 = new ImageEfx(this, tuli, (new Coordinates((float)((getWidth() / 2) - (tuli.getWidth() / 2)), 0)));
        tuliSc4.setAlpha(ImageEfx.TRANSPARENT, 0.025f);
        ImageEfx[] top4 = {bgSc4, tuliSc4};
        FadeTopBottomScene scene4 = new FadeTopBottomScene(this, bottom4, top4, 5);
        scene4.setTextArea(textArea);
        scene4.defineSound(trainningCenterAudio, MyJukeBox.LOOP);
        packBasis.put(4, scene4);

        //cena 5
        String txtScene5 = sentences.get(5);
        StandardScene scene5 = new StandardScene(this, bgScene0, setasAnim, 6);
        scene5.setDialog(txtScene5, fontDataBase, textArea, nameBg);
        BaseImage tuliSc5 = new BaseImage(tuli, (int)((getWidth() / 2) - (tuli.getWidth()/2)), 0);
        BaseImage[] chars5 = {tuliSc5};
        scene5.setCharacters(chars5);
        scene5.defineSound(trainningCenterAudio, MyJukeBox.LOOP);
        packBasis.put(5, scene5);

        //cena 6
        BaseImage[] bottom6 = {bgScene0, tuliSc5};
        ImageEfx bgSc6 = new ImageEfx(this, imgScene0, new Coordinates(0, 0));
        bgSc6.setAlpha(ImageEfx.TRANSPARENT, 0.01f);
        ImageEfx roseSc6 = new ImageEfx(this, rose, new Coordinates(((float)(getWidth() / 2) - (float)(rose.getWidth() / 2)), 0));
        roseSc6.setAlpha(ImageEfx.TRANSPARENT, 0.01f);
        ImageEfx[] top6 = {bgSc6, roseSc6};
        FadeTopBottomScene scene6 = new FadeTopBottomScene(this, bottom6, top6, 7);
        scene6.setTextArea(textArea);
        scene6.defineSound(trainningCenterAudio, MyJukeBox.LOOP);
        packBasis.put(6, scene6);

        //cena 7
        String txtScene7 = sentences.get(7);
        StandardScene scene7 = new StandardScene(this, bgScene0, setasAnim, 8);
        scene7.setDialog(txtScene7, fontDataBase, textArea, nameBg);
        BaseImage roseSc7 = new BaseImage(rose, getWidth() / 2 - rose.getWidth() / 2, 0);
        scene7.setCharacters(new BaseImage[]{roseSc7});
        scene7.defineSound(trainningCenterAudio, MyJukeBox.LOOP);
        packBasis.put(7, scene7);

        //cena 8
        FadeScene scene8 = new FadeScene(this, new BaseImage[]{bgScene0, roseSc7}, fadeinFast, 9);
        scene8.defineSound(trainningCenterAudio, MyJukeBox.LOOP);
        packBasis.put(8, scene8);

        //cena 9
        BaseImage livro1 = new BaseImage(livro1Buffered);
        FadeScene scene9 = new FadeScene(this, livro1, fadeoutFast, 10);
        scene9.defineSound(trainningCenterAudio, MyJukeBox.LOOP);
        packBasis.put(9, scene9);

        //cena 10
        String txtScene10 = sentences.get(10);
        StandardScene scene10 = new StandardScene(this, livro1, setasAnim, 11);
        scene10.setDialog(txtScene10, fontDataBase, textArea, nameBg);
        scene10.defineSound(trainningCenterAudio, MyJukeBox.LOOP);
        packBasis.put(10, scene10);

        //cena 11
        ImageEfx efxLivro2 = new ImageEfx(this, livro2Buffered, new Coordinates(0, 0));
        efxLivro2.setAlpha(ImageEfx.TRANSPARENT, 0.02f);
        EfxScene scene11 = new EfxScene(this, new ImageEfx[]{efxLivro2}, livro1, 12);
        //scene11.setTextArea(textArea);
        scene11.defineSound(trainningCenterAudio, MyJukeBox.LOOP);
        packBasis.put(11, scene11);

        //cena 12
        String txtScene12 = sentences.get(12);
        BaseImage livro2 = new BaseImage(livro2Buffered);
        BaseImage[] imagesSc12 = {livro1, livro2};
        StandardScene scene12 = new StandardScene(this, imagesSc12, setasAnim, 13);
        scene12.setDialog(txtScene12, fontDataBase, textArea, nameBg);
        scene12.defineSound(trainningCenterAudio, MyJukeBox.LOOP);
        packBasis.put(12, scene12);

        //cena 13
        ImageEfx efxLivro3 = new ImageEfx(this, livro3Buffered, new Coordinates(0, 0));
        efxLivro3.setAlpha(ImageEfx.TRANSPARENT, 0.02f);
        EfxScene scene13 = new EfxScene(this, new ImageEfx[]{efxLivro3}, imagesSc12, 14);
        scene13.defineSound(trainningCenterAudio, MyJukeBox.LOOP);
        packBasis.put(13, scene13);

        //cena 14
        String txtScene14 = sentences.get(14);
        BaseImage livro3 = new BaseImage(livro3Buffered);
        StandardScene scene14 = new StandardScene(this, new BaseImage[]{livro1, livro2, livro3}, setasAnim, 15);
        scene14.setDialog(txtScene14, fontDataBase, textArea, nameBg);
        scene14.defineSound(trainningCenterAudio, MyJukeBox.LOOP);
        packBasis.put(14, scene14);

        //cena 15 - temp
        String txtScene15 = sentences.get(15);
        StandardScene scene15 = new StandardScene(this, bgScene0, setasAnim, 0);
        //scene15.setNextPack(0);
        scene15.setDialog(txtScene15, fontDataBase, textArea, nameBg);
        scene15.defineSound(trainningCenterAudio, MyJukeBox.LOOP);
        packBasis.put(15, scene15);

    }

    private void resetKeys() {
        space = false;
        up = false;
        down = false;
        left = false;
        right = false;
        setClicked(false);
    }

    public void setShowScene(boolean showScene) {
        this.showScene = showScene;
    }

    public int getScene() {
        return scene;
    }


    // todo : imageEfx e Stan, com os arrays estao ok, nao quero alterar
    // todo : definir som para cada cena no construtor - isso evita dar load numa cena e vir sem som (o qual so inicia em outra cena especifica)
}