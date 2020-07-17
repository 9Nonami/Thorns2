package nona.mi.main;

import java.awt.image.BufferedImage;
import java.util.HashMap;

import nona.mi.button.Button;
import nona.mi.button.ButtonGroup;
import nona.mi.button.ButtonOrganizer;
import nona.mi.button.RectButton;
import nona.mi.cont.Cont;
import nona.mi.db.FontDataBase;
import nona.mi.db.NameDataBase;
import nona.mi.image.BaseImage;
import nona.mi.image.Coordinates;
import nona.mi.loader.DialogLoader;
import nona.mi.loader.ImageLoader;
import nona.mi.jukebox.MyJukeBox;
import nona.mi.efx.Fade;
import nona.mi.image.ImageEfx;
import nona.mi.loader.TextLoader;
import nona.mi.save.Save;
import nona.mi.scene.ChoiceScene;
import nona.mi.scene.DataManagerScene;
import nona.mi.scene.EfxScene;
import nona.mi.scene.FadeScene;
import nona.mi.scene.FadeTopBottomScene;
import nona.mi.scene.HistoryScene;
import nona.mi.scene.LoadScene;
import nona.mi.scene.MainMenuScene;
import nona.mi.scene.Scene;
import nona.mi.scene.StandardScene;


public class Thorns extends Game {

    public Thorns(int width, int height, String title, int gameLoopStyle) {

        super(width, height, title, gameLoopStyle);

        //CONTADOR PARA O DIALOG
        contForStan = new Cont();

        //STANDARD AUDIOS
        standardJukeBox.load("/res/audio/click.wav", NameDataBase.STANDARD_CLICK);

        //HASHMAP DE CENAS PUBLICAS
        publicScenes = new HashMap<Integer, Scene>();


        //IMAGENS DOS BOTOES
        BufferedImage yesImage = ImageLoader.loadImage("/res/misc/yes.png");
        BufferedImage noImage = ImageLoader.loadImage("/res/misc/no.png");

        BufferedImage startImage = ImageLoader.loadImage("/res/misc/start.png");

        BufferedImage returnImage = ImageLoader.loadImage("/res/misc/return.png");
        BufferedImage prevImage = ImageLoader.loadImage("/res/misc/prev.png");
        BufferedImage nextImage = ImageLoader.loadImage("/res/misc/next.png");

        BufferedImage saveImage = ImageLoader.loadImage("/res/misc/save.png");
        BufferedImage loadImage = ImageLoader.loadImage("/res/misc/load.png");
        BufferedImage copyImage = ImageLoader.loadImage("/res/misc/copy.png");
        BufferedImage deleteImage = ImageLoader.loadImage("/res/misc/delete.png");
        BufferedImage historyImage = ImageLoader.loadImage("/res/misc/history.png");
        BufferedImage mainImage = ImageLoader.loadImage("/res/misc/main.png");

        BufferedImage focusImage = ImageLoader.loadImage("/res/misc/focus.png");
        BufferedImage focuslargeImage = ImageLoader.loadImage("/res/misc/focuslarge.png");


        //YES BUTTON
        RectButton yesButton = new RectButton(this);
        yesButton.setImages(yesImage, focuslargeImage);
        yesButton.setAudioName(NameDataBase.STANDARD_CLICK);
        yesButton.setId(Button.YES);

        //NO BUTTON
        RectButton noButton = new RectButton(this);
        noButton.setImages(noImage, focuslargeImage);
        noButton.setAudioName(NameDataBase.STANDARD_CLICK);
        noButton.setId(Button.NO);

        //START BUTTON
        RectButton startButton = new RectButton(this);
        startButton.setImages(startImage, focusImage);
        startButton.setAudioName(NameDataBase.STANDARD_CLICK);
        startButton.setId(Button.START);

        //RETURN BUTTON
        RectButton returnButton = new RectButton(this);
        returnButton.setImages(returnImage, focusImage);
        returnButton.setAudioName(NameDataBase.STANDARD_CLICK);
        returnButton.setId(Button.RETURN);

        //PREV BUTTON
        RectButton prevButton = new RectButton(this);
        prevButton.setImages(prevImage, focuslargeImage);
        prevButton.setAudioName(NameDataBase.STANDARD_CLICK);
        prevButton.setId(Button.PREV);

        //NEXT BUTTON
        RectButton nextButton = new RectButton(this);
        nextButton.setImages(nextImage, focuslargeImage);
        nextButton.setAudioName(NameDataBase.STANDARD_CLICK);
        nextButton.setId(Button.NEXT);

        //SAVE BUTTON
        RectButton saveButton = new RectButton(this);
        saveButton.setImages(saveImage, focusImage);
        saveButton.setAudioName(NameDataBase.STANDARD_CLICK);
        saveButton.setId(Button.SAVE);

        //LOAD BUTTON
        RectButton loadButton = new RectButton(this);
        loadButton.setImages(loadImage, focusImage);
        loadButton.setAudioName(NameDataBase.STANDARD_CLICK);
        loadButton.setId(Button.LOAD);

        //COPY BUTTON
        RectButton copyButton = new RectButton(this);
        copyButton.setImages(copyImage, focusImage);
        copyButton.setAudioName(NameDataBase.STANDARD_CLICK);
        copyButton.setId(Button.COPY);

        //DELETE BUTTON
        RectButton deleteButton = new RectButton(this);
        deleteButton.setImages(deleteImage, focusImage);
        deleteButton.setAudioName(NameDataBase.STANDARD_CLICK);
        deleteButton.setId(Button.DEL);

        //HISTORY BUTTON
        RectButton historyButton = new RectButton(this);
        historyButton.setImages(historyImage, focusImage);
        historyButton.setAudioName(NameDataBase.STANDARD_CLICK);
        historyButton.setId(Button.HISTORY);

        //MAIN BUTTON
        RectButton mainButton = new RectButton(this);
        mainButton.setImages(mainImage, focusImage);
        mainButton.setAudioName(NameDataBase.STANDARD_CLICK);
        mainButton.setId(Button.MAIN);

        //TODO : INICIAR AS POSICOES XY COM ORGANIZER
        RectButton[] rectButtons = {startButton, saveButton, loadButton, copyButton, deleteButton, historyButton, mainButton};
        buttonOrganizer = new ButtonOrganizer(rectButtons);
        buttonOrganizer.defineMainConfig(); //inicia os botoes nas posicoes que mainscene usa

        //BUTTON GROUPS
        yn = new ButtonGroup(new Button[]{yesButton, noButton});
        navigationButtonGroup = new ButtonGroup(new Button[]{returnButton, prevButton, nextButton});
        stanButtonGroup = new ButtonGroup(new Button[]{saveButton, loadButton, copyButton, deleteButton, historyButton, mainButton});
        mainButtonGroup = new ButtonGroup(new Button[]{startButton, loadButton});


        //FONTE DA VN
        fontDataBase = new FontDataBase("/res/font/myfont.png", "/res/font/text.txt");
        fontFocus = new FontDataBase("/res/font/myfontfocus.png", "/res/font/text.txt");

        //TEXT AREA
        BufferedImage tempTextArea = ImageLoader.loadImage("/res/misc/textarea.png");// /res/font/txtarea.png
        textArea = new BaseImage(tempTextArea, 10, (int)(getHeight() - tempTextArea.getHeight() - 10));

        //NAMES DATA BASE
        nameDataBase = new NameDataBase();

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
        setasAnim = new ImageEfx(this, setas, setasCoord, 0.15f , ImageEfx.LOOP); //0.15f

        //LOAD SCENE
        createLoadScene();

        //MAIN MENU SCENE
        createMainMenuScene();

        //FADESCENE logo
        createFadeLogoScene();

        //PRIMEIRA CENA (fadelogo)
        defineFirstScene();

        //SAVE
        save = new Save(12);

        //IMAGEM DE FOCO PARA SAVE, LOAD...
        BufferedImage focusMisc = ImageLoader.loadImage("/res/misc/focus.png");

        //DATA MANAGER SCENE
        createDataManagerScene();

        //HISTORY SCENE
        createHistoryScene();

    }

    private void createDataManagerScene() {

        //MODOS
        int modeX = 200;
        int modeY = 0;
        BaseImage saveMode = new BaseImage(ImageLoader.loadImage("/res/misc/save-mode.png"), modeX, modeY);
        BaseImage loadMode = new BaseImage(ImageLoader.loadImage("/res/misc/load-mode.png"), modeX, modeY);
        BaseImage copyMode = new BaseImage(ImageLoader.loadImage("/res/misc/copy-mode.png"), modeX, modeY);
        BaseImage deleteMode = new BaseImage(ImageLoader.loadImage("/res/misc/delete-mode.png"), modeX, modeY);
        HashMap<Integer, BaseImage> modes = new HashMap<Integer, BaseImage>();
        modes.put(Button.SAVE, saveMode);
        modes.put(Button.LOAD, loadMode);
        modes.put(Button.COPY, copyMode);
        modes.put(Button.DEL, deleteMode);

        DataManagerScene tempDataManagerScene = new DataManagerScene(this, Scene.DMS_SCENE, 6);
        tempDataManagerScene.createSlotImages(ImageLoader.loadImage("/res/buttons/empty-slot.png"), ImageLoader.loadImage("/res/buttons/focused-slot.png"));
        tempDataManagerScene.createSlots(12, 2, 3, 44, 44, 31, NameDataBase.STANDARD_CLICK);
        tempDataManagerScene.createModes(modes);
        tempDataManagerScene.setPackId(Scene.NO_PACK);
        publicScenes.put(tempDataManagerScene.getSceneId(), tempDataManagerScene);

    }

    private void createMainMenuScene() {
        MainMenuScene tempMainMenu = new MainMenuScene(this, Scene.MAIN_MENU_SCENE);
        tempMainMenu.setPackId(Scene.NO_PACK);
        publicScenes.put(tempMainMenu.getSceneId(), tempMainMenu);
    }

    private void createHistoryScene() {
        HistoryScene tempHistoryScene = new HistoryScene(this, Scene.HISTORY_SCENE);
        tempHistoryScene.setPackId(Scene.NO_PACK);
        publicScenes.put(tempHistoryScene.getSceneId(), tempHistoryScene);

    }

    private void createLoadScene() {
        LoadScene tempLoadScene = new LoadScene(this, Scene.LOAD_SCENE, new BaseImage(ImageLoader.loadImage("/res/bg/load.png"), 0, 0));
        tempLoadScene.setPackId(Scene.NO_PACK);
        publicScenes.put(tempLoadScene.getSceneId(), tempLoadScene);
    }

    private void createFadeLogoScene() {
        Fade fadeLogo = new Fade(this, Fade.SOLID, Fade.FAST);
        fadeLogo.setFadeOutIn(true);
        FadeScene tempFadeScene = new FadeScene(this, new BaseImage(ImageLoader.loadImage("/res/menu/logo.png"), 0, 0), fadeLogo, Scene.LAST_SCENE, Scene.FADE_SCENE_LOGO); //todo: ver esse last scene
        tempFadeScene.setDirectScene(Scene.MAIN_MENU_SCENE); //a cena para a qual irah
        tempFadeScene.setPackId(Scene.NO_PACK);
        publicScenes.put(tempFadeScene.getSceneId(), tempFadeScene);
    }

    private void defineFirstScene() {
        sceneBasis = getSceneFromPublicScenes(Scene.FADE_SCENE_LOGO); //nao uso setdirec... por que ele vai tentar resetar, mas nao ha o que resetar, esta null
    }

    @Override
    public synchronized void initPacks(int tempNextPack) {
        if (tempNextPack == 0) {
            initPack0();
        }
        setPackIdToScenes(tempNextPack);
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
        packJukebox.load("/res/audio/scene0.wav", NameDataBase.TRAINNING_CENTER_AUDIO);

        //BACKGROUND DOS NOMES
        nameBgDataBaseBasis.put(nameDataBase.get(NameDataBase.MARISA), ImageLoader.loadImage("/res/font/nameBg.png"));


        //----------------------------------------

        //cena 0
        FadeScene scene0 = new FadeScene(this, bgScene0, fadeoutSlow, 1, 0);
        scene0.setBackgroundAudio(NameDataBase.TRAINNING_CENTER_AUDIO, MyJukeBox.LOOP);
        packBasis.put(scene0);

        //cena 1
        StandardScene scene1 = new StandardScene(this, bgScene0, 2, 1);
        scene1.setDialog(sentences.get(scene1.getSceneId()));
        scene1.setBackgroundAudio(NameDataBase.TRAINNING_CENTER_AUDIO, MyJukeBox.LOOP);
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
        scene2.setBackgroundAudio(NameDataBase.TRAINNING_CENTER_AUDIO, MyJukeBox.LOOP);
        packBasis.put(scene2);

        //cena 3
        BaseImage roseSc3 = new BaseImage(rose, 0, 0);
        BaseImage tuliSc3 = new BaseImage(tuli, 400, 0);
        BaseImage[] characters = {roseSc3, tuliSc3};
        StandardScene scene3 = new StandardScene(this, bgScene0, 4, 3);
        scene3.setDialog(sentences.get(scene3.getSceneId()));
        scene3.setCharacters(characters);
        scene3.setBackgroundAudio(NameDataBase.TRAINNING_CENTER_AUDIO, MyJukeBox.LOOP);
        packBasis.put(scene3);

        //cena 4 //0.05f old speed
        BaseImage[] bottom4 = {bgScene0, roseSc3, tuliSc3};
        ImageEfx bgSc4 = new ImageEfx(this, imgScene0, new Coordinates(0, 0));
        bgSc4.setAlpha(ImageEfx.TRANSPARENT, 0.025f);
        ImageEfx tuliSc4 = new ImageEfx(this, tuli, (new Coordinates((float)((getWidth() / 2) - (tuli.getWidth() / 2)), 0)));
        tuliSc4.setAlpha(ImageEfx.TRANSPARENT, 0.025f);
        ImageEfx[] top4 = {bgSc4, tuliSc4};
        FadeTopBottomScene scene4 = new FadeTopBottomScene(this, bottom4, top4, 5, 4);
        scene4.setBackgroundAudio(NameDataBase.TRAINNING_CENTER_AUDIO, MyJukeBox.LOOP);
        packBasis.put(scene4);

        //cena 5
        StandardScene scene5 = new StandardScene(this, bgScene0, 6, 5);
        scene5.setDialog(sentences.get(scene5.getSceneId()));
        BaseImage tuliSc5 = new BaseImage(tuli, (int)((getWidth() / 2) - (tuli.getWidth()/2)), 0);
        scene5.setCharacters(new BaseImage[]{tuliSc5});
        scene5.setBackgroundAudio(NameDataBase.TRAINNING_CENTER_AUDIO, MyJukeBox.LOOP);
        packBasis.put(scene5);

        //cena 6
        BaseImage[] bottom6 = {bgScene0, tuliSc5};
        ImageEfx bgSc6 = new ImageEfx(this, imgScene0, new Coordinates(0, 0));
        bgSc6.setAlpha(ImageEfx.TRANSPARENT, 0.01f);
        ImageEfx roseSc6 = new ImageEfx(this, rose, new Coordinates(((float)(getWidth() / 2) - (float)(rose.getWidth() / 2)), 0));
        roseSc6.setAlpha(ImageEfx.TRANSPARENT, 0.01f);
        ImageEfx[] top6 = {bgSc6, roseSc6};
        FadeTopBottomScene scene6 = new FadeTopBottomScene(this, bottom6, top6, 7, 6);
        scene6.setBackgroundAudio(NameDataBase.TRAINNING_CENTER_AUDIO, MyJukeBox.LOOP);
        packBasis.put(scene6);

        //cena 7
        StandardScene scene7 = new StandardScene(this, bgScene0, 8, 7);
        scene7.setDialog(sentences.get(scene7.getSceneId()));
        BaseImage roseSc7 = new BaseImage(rose, getWidth() / 2 - rose.getWidth() / 2, 0);
        scene7.setCharacters(new BaseImage[]{roseSc7});
        scene7.setBackgroundAudio(NameDataBase.TRAINNING_CENTER_AUDIO, MyJukeBox.LOOP);
        packBasis.put(scene7);

        //cena 8
        FadeScene scene8 = new FadeScene(this, new BaseImage[]{bgScene0, roseSc7}, fadeinFast, 9, 8);
        scene8.setBackgroundAudio(NameDataBase.TRAINNING_CENTER_AUDIO, MyJukeBox.LOOP);
        packBasis.put(scene8);

        //cena 9
        BaseImage livro1 = new BaseImage(livro1Buffered);
        FadeScene scene9 = new FadeScene(this, livro1, fadeoutFast, 10, 9);
        scene9.setBackgroundAudio(NameDataBase.TRAINNING_CENTER_AUDIO, MyJukeBox.LOOP);
        packBasis.put(scene9);

        //cena 10
        StandardScene scene10 = new StandardScene(this, livro1, 11, 10);
        scene10.setDialog(sentences.get(scene10.getSceneId()));
        scene10.setBackgroundAudio(NameDataBase.TRAINNING_CENTER_AUDIO, MyJukeBox.LOOP);
        packBasis.put(scene10);

        //cena 11
        ImageEfx efxLivro2 = new ImageEfx(this, livro2Buffered, new Coordinates(0, 0));
        efxLivro2.setAlpha(ImageEfx.TRANSPARENT, 0.02f);
        EfxScene scene11 = new EfxScene(this, new ImageEfx[]{efxLivro2}, livro1, 12, 11);
        scene11.setBackgroundAudio(NameDataBase.TRAINNING_CENTER_AUDIO, MyJukeBox.LOOP);
        packBasis.put(scene11);

        //cena 12
        BaseImage livro2 = new BaseImage(livro2Buffered);
        BaseImage[] imagesSc12 = {livro1, livro2};
        StandardScene scene12 = new StandardScene(this, imagesSc12, 13, 12);
        scene12.setDialog(sentences.get(scene12.getSceneId()));
        scene12.setBackgroundAudio(NameDataBase.TRAINNING_CENTER_AUDIO, MyJukeBox.LOOP);
        packBasis.put(scene12);

        //cena 13
        ImageEfx efxLivro3 = new ImageEfx(this, livro3Buffered, new Coordinates(0, 0));
        efxLivro3.setAlpha(ImageEfx.TRANSPARENT, 0.02f);
        EfxScene scene13 = new EfxScene(this, new ImageEfx[]{efxLivro3}, imagesSc12, 14, 13);
        scene13.setBackgroundAudio(NameDataBase.TRAINNING_CENTER_AUDIO, MyJukeBox.LOOP);
        packBasis.put(scene13);

        //cena 14
        BaseImage livro3 = new BaseImage(livro3Buffered);
        StandardScene scene14 = new StandardScene(this, new BaseImage[]{livro1, livro2, livro3}, 15, 14);
        scene14.setDialog(sentences.get(scene14.getSceneId()));
        scene14.setBackgroundAudio(NameDataBase.TRAINNING_CENTER_AUDIO, MyJukeBox.LOOP);
        packBasis.put(scene14);

        //cena 15 - teste de fala
        StandardScene scene15 = new StandardScene(this, bgScene0, 16, 15); //nx = 0
        scene15.setDialog(sentences.get(scene15.getSceneId()));
        scene15.setBackgroundAudio(NameDataBase.TRAINNING_CENTER_AUDIO, MyJukeBox.LOOP);
        packBasis.put(scene15);

        //cena 16 - teste de choice
        String asd = "FOLLOW ROSE@";
        String zxc = "FOLLOW AKHNAHRR@";
        String qwe = "UNO DOS TRES@DE OLIVEIRA QUATRO@HURR DURR@";
        ChoiceScene scene16 = new ChoiceScene(this, 16, new String[]{asd, zxc, qwe});
        packBasis.put(scene16);

    }


    //imageEfx e Stan, com os arrays estao ok, nao quero alterar
}