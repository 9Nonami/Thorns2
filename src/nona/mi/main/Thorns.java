package nona.mi.main;

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
import nona.mi.loader.TextLoader;
import nona.mi.save.Save;
import nona.mi.scene.EfxScene;
import nona.mi.scene.FadeScene;
import nona.mi.scene.FadeTopBottomScene;
import nona.mi.scene.LoadScene;
import nona.mi.scene.MainMenuScene;
import nona.mi.scene.DataManagerScene;
import nona.mi.scene.Scene;
import nona.mi.scene.StandardScene;

public class Thorns extends Game {



    public Thorns(int width, int height, String title, int gameLoopStyle) {

        super(width, height, title, gameLoopStyle);

        //SOM BOTOES
        String audioClick = "click";

        //STANDARD AUDIOS
        standardJukeBox.load("/res/audio/click.wav", audioClick);

        //HASHMAP DE CENAS PUBLICAS
        publicScenes = new HashMap<Integer, Scene>();


        //YN
        BufferedImage uno = ImageLoader.loadImage("/res/buttons/uno.png");
        BufferedImage dos = ImageLoader.loadImage("/res/buttons/dos.png");

        //YES BUTTON
        RectButton yesButton = new RectButton(this);
        yesButton.setImages(uno, dos, 150, 50);
        yesButton.setAudioName(audioClick);
        yesButton.setId(DataManagerScene.YES);

        //NO BUTTON
        RectButton noButton = new RectButton(this);
        noButton.setImages(uno, dos, 350, 50);
        noButton.setAudioName(audioClick);
        noButton.setId(DataManagerScene.NO);
        yn = new ButtonGroup(new Button[]{yesButton, noButton}); //buttonGroup de Game



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

        //LOAD SCENE
        createLoadScene();

        //MAIN MENU SCENE
        createMainMenuScene(audioClick);

        //FADESCENE logo
        createFadeLogoScene();

        //PRIMEIRA CENA (fadelogo)
        defineFirstScene();

        //SAVE
        save = new Save(12);

        //IMAGEM DE FOCO PARA SAVE, LOAD...
        BufferedImage focusMisc = ImageLoader.loadImage("/res/misc/focus.png");

        //MENU COM SAVE, LOAD, COPY, DEL E MAIN
        createSceneMenu(focusMisc, audioClick, tempTextArea);

        //DATA MANAGER SCENE
        createDataManagerScene(focusMisc, audioClick);

    }

    private void createDataManagerScene(BufferedImage focusMisc, String audioClick) {

        BufferedImage uno = ImageLoader.loadImage("/res/buttons/uno.png");
        BufferedImage dos = ImageLoader.loadImage("/res/buttons/dos.png");

        BufferedImage returnImage = ImageLoader.loadImage("/res/misc/return.png");

        //RETURN BUTTON
        RectButton returnButton = new RectButton(this);
        returnButton.setImages(returnImage, focusMisc, 50, 390);
        returnButton.setAudioName(audioClick);
        returnButton.setId(DataManagerScene.RETURN_TO_LAST_SCENE);

        //PREV BUTTON
        RectButton previousButton = new RectButton(this);
        previousButton.setImages(uno, dos, 250, 390);
        previousButton.setAudioName(audioClick);
        previousButton.setId(DataManagerScene.PREVIOUS_SLOT_GROUP);

        //NEXT BUTTON
        RectButton nextButton = new RectButton(this);
        nextButton.setImages(uno, dos, 400, 390);
        nextButton.setAudioName(audioClick);
        nextButton.setId(DataManagerScene.NEXT_SLOT_GROUP);

        //MODOS
        int modeX = 200;
        int modeY = 0;
        BaseImage saveMode = new BaseImage(ImageLoader.loadImage("/res/misc/save-mode.png"), modeX, modeY);
        BaseImage loadMode = new BaseImage(ImageLoader.loadImage("/res/misc/load-mode.png"), modeX, modeY);
        BaseImage copyMode = new BaseImage(ImageLoader.loadImage("/res/misc/copy-mode.png"), modeX, modeY);
        BaseImage deleteMode = new BaseImage(ImageLoader.loadImage("/res/misc/delete-mode.png"), modeX, modeY);
        HashMap<Integer, BaseImage> modes = new HashMap<Integer, BaseImage>();
        modes.put(DataManagerScene.SAVE, saveMode);
        modes.put(DataManagerScene.LOAD, loadMode);
        modes.put(DataManagerScene.COPY, copyMode);
        modes.put(DataManagerScene.DEL, deleteMode);


        DataManagerScene tempDataManagerScene = new DataManagerScene(this, Scene.DMS_SCENE, save, 6);
        tempDataManagerScene.createSlotImages(ImageLoader.loadImage("/res/buttons/empty-slot.png"), ImageLoader.loadImage("/res/buttons/focused-slot.png"));
        tempDataManagerScene.createSlots(12, 2, 3, 44, 44, 31);
        tempDataManagerScene.createMiscButtons(new Button[]{returnButton, previousButton, nextButton});
        tempDataManagerScene.createModes(modes);
        tempDataManagerScene.setPackId(Scene.NO_PACK);
        publicScenes.put(tempDataManagerScene.getSceneId(), tempDataManagerScene);
    }

    private void createMainMenuScene(String audioClick) {

        BufferedImage uno = ImageLoader.loadImage("/res/misc/save.png");
        BufferedImage dos = ImageLoader.loadImage("/res/misc/focus.png");

        RectButton newGame = new RectButton(this);
        newGame.setImages(uno, dos, 10, 10);
        newGame.setAudioName(audioClick);
        newGame.setId(MainMenuScene.NEW_GAME);

        RectButton loadGame = new RectButton(this);
        loadGame.setImages(uno, dos, 10, 50);
        loadGame.setAudioName(audioClick);
        loadGame.setId(MainMenuScene.LOAD_GAME);

        MainMenuScene tempMainMenu = new MainMenuScene(this, Scene.MAIN_MENU_SCENE, new ButtonGroup(new Button[]{newGame, loadGame}));
        tempMainMenu.setPackId(Scene.NO_PACK);
        publicScenes.put(tempMainMenu.getSceneId(), tempMainMenu);
    }

    private void createLoadScene() {
        LoadScene tempLoadScene = new LoadScene(this, Scene.LOAD_SCENE, new BaseImage(ImageLoader.loadImage("/res/bg/load.png"), 0, 0));
        tempLoadScene.setPackId(Scene.NO_PACK);
        publicScenes.put(tempLoadScene.getSceneId(), tempLoadScene);
    }

    private void createFadeLogoScene() {
        Fade fadeLogo = new Fade(this, Fade.SOLID, Fade.FAST);
        fadeLogo.setFadeOutIn(true);
        FadeScene fs = new FadeScene(this, new BaseImage(ImageLoader.loadImage("/res/menu/logo.png"), 0, 0), fadeLogo, Scene.LAST_SCENE, Scene.FADE_SCENE_LOGO);
        fs.setDirectScene(Scene.MAIN_MENU_SCENE); //a cena para a qual irah
        fs.setPackId(Scene.NO_PACK);
        publicScenes.put(fs.getSceneId(), fs);
    }

    private void defineFirstScene() {
        sceneBasis = getSceneFromPublicScenes(Scene.FADE_SCENE_LOGO); //nao uso setdirec... por que ele vai tentar resetar, mas nao ha o que resetar, esta null
    }

    private void createSceneMenu(BufferedImage focusMisc, String audioClick, BufferedImage tempTextArea) {

        //BOTOES DE SAVE, LOAD, COPY, DEL, MAIN

        BufferedImage saveImage = ImageLoader.loadImage("/res/misc/save.png");
        BufferedImage loadImage = ImageLoader.loadImage("/res/misc/load.png");
        BufferedImage copyImage = ImageLoader.loadImage("/res/misc/copy.png");
        BufferedImage deleteImage = ImageLoader.loadImage("/res/misc/delete.png");
        int spacing = 3;


        RectButton saveButton = new RectButton(this);
        saveButton.setImages(saveImage, focusMisc, (textArea.getX() + tempTextArea.getWidth() + spacing), textArea.getY());
        saveButton.setAudioName(audioClick);
        saveButton.setId(DataManagerScene.SAVE);

        RectButton loadButton = new RectButton(this);
        loadButton.setImages(loadImage, focusMisc, saveButton.getX(), (saveButton.getY() + saveButton.getHeight() + spacing));
        loadButton.setAudioName(audioClick);
        loadButton.setId(DataManagerScene.LOAD);

        RectButton copyButton = new RectButton(this);
        copyButton.setImages(copyImage, focusMisc, loadButton.getX(), (loadButton.getY() + loadButton.getHeight() + spacing));
        copyButton.setAudioName(audioClick);
        copyButton.setId(DataManagerScene.COPY);

        RectButton deleteButton = new RectButton(this);
        deleteButton.setImages(deleteImage, focusMisc, copyButton.getX(), (copyButton.getY() + copyButton.getHeight() + spacing));
        deleteButton.setAudioName(audioClick);
        deleteButton.setId(DataManagerScene.DEL);

        RectButton mainButton = new RectButton(this);
        mainButton.setImages(ImageLoader.loadImage("/res/misc/main.png"), focusMisc, 100, 0);
        mainButton.setAudioName(audioClick);
        mainButton.setId(DataManagerScene.MAIN);

        sceneMenu = new ButtonGroup(new Button[]{saveButton, loadButton, copyButton, deleteButton, mainButton});

    }

    @Override
    public synchronized void initPacks(int tempNextPack) {
        if (tempNextPack == 0) {
            initPack0();
        }
    }

    private void initPack0() {

        //LOCAL-----------------------------------

        //BACKGROUND - CENTRO TREINAMENTO
        BufferedImage imgScene0 = ImageLoader.loadImage("/res/bg/trainning-center.png");
        BaseImage bgScene0 = new BaseImage(imgScene0, 0, 0);

        //FADES
        Fade fadeoutSlow = new Fade(this, Fade.SOLID, Fade.DEMONIAC); //slow
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
        FadeScene scene0 = new FadeScene(this, bgScene0, fadeoutSlow, 99, 0);
        scene0.setBackgroundAudio(trainningCenterAudio, MyJukeBox.LOOP);
        packBasis.put(scene0);

        //cena 1
        StandardScene scene1 = new StandardScene(this, bgScene0, 2, 1);
        scene1.setDialog(sentences.get(scene1.getSceneId()));
        scene1.setBackgroundAudio(trainningCenterAudio, MyJukeBox.LOOP);
        packBasis.put(scene1);

        //cena 2
        ImageEfx efxRose = new ImageEfx(this, rose, roseCoordScene2);
        efxRose.setAlpha(ImageEfx.TRANSPARENT, 0.02f);
        efxRose.setMoveTo(0, 0, 1, 0); //rose e tuli estao movendo 30px
        ImageEfx efxTuli = new ImageEfx(this, tuli, tuliCoordScene2);
        efxTuli.setAlpha(ImageEfx.TRANSPARENT, 0.02f);
        efxTuli.setMoveTo(400, 0, 1, 0);
        ImageEfx[] images = {efxRose, efxTuli};
        EfxScene scene2 = new EfxScene(this, images, bgScene0, 3, 2);
        scene2.setBackgroundAudio(trainningCenterAudio, MyJukeBox.LOOP);
        packBasis.put(scene2);

        //cena 3
        BaseImage roseSc3 = new BaseImage(rose, 0, 0);
        BaseImage tuliSc3 = new BaseImage(tuli, 400, 0);
        BaseImage[] characters = {roseSc3, tuliSc3};
        StandardScene scene3 = new StandardScene(this, bgScene0, 4, 3);
        scene3.setDialog(sentences.get(scene3.getSceneId()));
        scene3.setCharacters(characters);
        scene3.setBackgroundAudio(trainningCenterAudio, MyJukeBox.LOOP);
        packBasis.put(scene3);

        //cena 4 //0.05f old speed
        BaseImage[] bottom4 = {bgScene0, roseSc3, tuliSc3};
        ImageEfx bgSc4 = new ImageEfx(this, imgScene0, new Coordinates(0, 0));
        bgSc4.setAlpha(ImageEfx.TRANSPARENT, 0.025f);
        ImageEfx tuliSc4 = new ImageEfx(this, tuli, (new Coordinates((float)((getWidth() / 2) - (tuli.getWidth() / 2)), 0)));
        tuliSc4.setAlpha(ImageEfx.TRANSPARENT, 0.025f);
        ImageEfx[] top4 = {bgSc4, tuliSc4};
        FadeTopBottomScene scene4 = new FadeTopBottomScene(this, bottom4, top4, 5, 4);
        scene4.setBackgroundAudio(trainningCenterAudio, MyJukeBox.LOOP);
        packBasis.put(scene4);

        //cena 5
        StandardScene scene5 = new StandardScene(this, bgScene0, 6, 5);
        scene5.setDialog(sentences.get(scene5.getSceneId()));
        BaseImage tuliSc5 = new BaseImage(tuli, (int)((getWidth() / 2) - (tuli.getWidth()/2)), 0);
        scene5.setCharacters(new BaseImage[]{tuliSc5});
        scene5.setBackgroundAudio(trainningCenterAudio, MyJukeBox.LOOP);
        packBasis.put(scene5);

        //cena 6
        BaseImage[] bottom6 = {bgScene0, tuliSc5};
        ImageEfx bgSc6 = new ImageEfx(this, imgScene0, new Coordinates(0, 0));
        bgSc6.setAlpha(ImageEfx.TRANSPARENT, 0.01f);
        ImageEfx roseSc6 = new ImageEfx(this, rose, new Coordinates(((float)(getWidth() / 2) - (float)(rose.getWidth() / 2)), 0));
        roseSc6.setAlpha(ImageEfx.TRANSPARENT, 0.01f);
        ImageEfx[] top6 = {bgSc6, roseSc6};
        FadeTopBottomScene scene6 = new FadeTopBottomScene(this, bottom6, top6, 7, 6);
        scene6.setBackgroundAudio(trainningCenterAudio, MyJukeBox.LOOP);
        packBasis.put(scene6);

        //cena 7
        StandardScene scene7 = new StandardScene(this, bgScene0, 8, 7);
        scene7.setDialog(sentences.get(scene7.getSceneId()));
        BaseImage roseSc7 = new BaseImage(rose, getWidth() / 2 - rose.getWidth() / 2, 0);
        scene7.setCharacters(new BaseImage[]{roseSc7});
        scene7.setBackgroundAudio(trainningCenterAudio, MyJukeBox.LOOP);
        packBasis.put(scene7);

        //cena 8
        FadeScene scene8 = new FadeScene(this, new BaseImage[]{bgScene0, roseSc7}, fadeinFast, 9, 8);
        scene8.setBackgroundAudio(trainningCenterAudio, MyJukeBox.LOOP);
        packBasis.put(scene8);

        //cena 9
        BaseImage livro1 = new BaseImage(livro1Buffered);
        FadeScene scene9 = new FadeScene(this, livro1, fadeoutFast, 10, 9);
        scene9.setBackgroundAudio(trainningCenterAudio, MyJukeBox.LOOP);
        packBasis.put(scene9);

        //cena 10
        StandardScene scene10 = new StandardScene(this, livro1, 11, 10);
        scene10.setDialog(sentences.get(scene10.getSceneId()));
        scene10.setBackgroundAudio(trainningCenterAudio, MyJukeBox.LOOP);
        packBasis.put(scene10);

        //cena 11
        ImageEfx efxLivro2 = new ImageEfx(this, livro2Buffered, new Coordinates(0, 0));
        efxLivro2.setAlpha(ImageEfx.TRANSPARENT, 0.02f);
        EfxScene scene11 = new EfxScene(this, new ImageEfx[]{efxLivro2}, livro1, 12, 11);
        scene11.setBackgroundAudio(trainningCenterAudio, MyJukeBox.LOOP);
        packBasis.put(scene11);

        //cena 12
        BaseImage livro2 = new BaseImage(livro2Buffered);
        BaseImage[] imagesSc12 = {livro1, livro2};
        StandardScene scene12 = new StandardScene(this, imagesSc12, 13, 12);
        scene12.setDialog(sentences.get(scene12.getSceneId()));
        scene12.setBackgroundAudio(trainningCenterAudio, MyJukeBox.LOOP);
        packBasis.put(scene12);

        //cena 13
        ImageEfx efxLivro3 = new ImageEfx(this, livro3Buffered, new Coordinates(0, 0));
        efxLivro3.setAlpha(ImageEfx.TRANSPARENT, 0.02f);
        EfxScene scene13 = new EfxScene(this, new ImageEfx[]{efxLivro3}, imagesSc12, 14, 13);
        scene13.setBackgroundAudio(trainningCenterAudio, MyJukeBox.LOOP);
        packBasis.put(scene13);

        //cena 14
        BaseImage livro3 = new BaseImage(livro3Buffered);
        StandardScene scene14 = new StandardScene(this, new BaseImage[]{livro1, livro2, livro3}, 15, 14);
        scene14.setDialog(sentences.get(scene14.getSceneId()));
        scene14.setBackgroundAudio(trainningCenterAudio, MyJukeBox.LOOP);
        packBasis.put(scene14);

        //cena 15 - temp
        StandardScene scene15 = new StandardScene(this, bgScene0, 0, 15);
        scene15.setDialog(sentences.get(scene15.getSceneId()));
        scene15.setBackgroundAudio(trainningCenterAudio, MyJukeBox.LOOP);
        packBasis.put(scene15);

        //cena 99
        StandardScene scene99 = new StandardScene(this, bgScene0, 1, 99);
        scene99.setDialog(sentences.get(scene99.getSceneId()));
        scene99.setBackgroundAudio(trainningCenterAudio, MyJukeBox.LOOP);
        packBasis.put(scene99);

    }


    //imageEfx e Stan, com os arrays estao ok, nao quero alterar
}