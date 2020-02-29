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
import nona.mi.loader.ScreenshotLoader;
import nona.mi.loader.TextLoader;
import nona.mi.save.Save;
import nona.mi.scene.*;

//TODO : DAR UM JEITO DE CRIAR OS AUDIOS E BUTTONS TUDO AQUI. NADA DE FAZER ISSO DENTRO DAS CLASSES!
//todo : deixar ao maximo tudo quanto eh load de imagem aqui!
public class Thorns extends Game {

    private FontDataBase fontDataBase;
    private FontDataBase fontFocus;
    private BaseImage nameBg;
    private BaseImage textArea;
    private BufferedImage choicebg;
    private BufferedImage pointer;
    private ImageEfx setasAnim;
    private ButtonGroup sceneMenu; //save, load, copy, del



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
        yesButton.setId(SaveMenuScene.YES);

        //NO BUTTON
        RectButton noButton = new RectButton(this);
        noButton.setImages(uno, dos, 350, 50);
        noButton.setAudioName(audioClick);
        noButton.setId(SaveMenuScene.NO);
        yn = new ButtonGroup(new Button[]{yesButton, noButton});



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

        //MENU COM SAVE, LOAD, COPY E DEL
        createSceneMenu(focusMisc, audioClick, tempTextArea);

        //DATA MANAGER SCENE
        createDataManagerScene(focusMisc, audioClick);

    }

    private void createDataManagerScene(BufferedImage focusMisc, String audioClick) {

        BufferedImage uno = ImageLoader.loadImage("/res/buttons/uno.png");
        BufferedImage dos = ImageLoader.loadImage("/res/buttons/dos.png");

        BufferedImage returnImage = ImageLoader.loadImage("/res/misc/return.png");
        //BufferedImage prevImage = ImageLoader.loadImage("/res/buttons/uno.png");
        //BufferedImage nextImage = ImageLoader.loadImage("/res/buttons/uno.png");
        //BufferedImage pnFocus = ImageLoader.loadImage("/res/buttons/dos.png");

        //RETURN BUTTON
        RectButton returnButton = new RectButton(this);
        returnButton.setImages(returnImage, focusMisc, 50, 390);
        returnButton.setAudioName(audioClick);
        returnButton.setId(SaveMenuScene.RETURN_TO_LAST_SCENE);

        //PREV BUTTON
        RectButton previousButton = new RectButton(this);
        previousButton.setImages(uno, dos, 250, 390);
        previousButton.setAudioName(audioClick);
        previousButton.setId(SaveMenuScene.PREVIOUS_SLOT_GROUP);

        //NEXT BUTTON
        RectButton nextButton = new RectButton(this);
        nextButton.setImages(uno, dos, 400, 390);
        nextButton.setAudioName(audioClick);
        nextButton.setId(SaveMenuScene.NEXT_SLOT_GROUP);



        //YES BUTTON
        RectButton yesButton = new RectButton(this);
        yesButton.setImages(uno, dos, 150, 50);
        yesButton.setAudioName(audioClick);
        yesButton.setId(SaveMenuScene.YES);

        //NO BUTTON
        RectButton noButton = new RectButton(this);
        noButton.setImages(uno, dos, 350, 50);
        noButton.setAudioName(audioClick);
        noButton.setId(SaveMenuScene.NO);



        //MODOS
        int modeX = 200;
        int modeY = 0;
        BaseImage saveMode = new BaseImage(ImageLoader.loadImage("/res/misc/save-mode.png"), modeX, modeY);
        BaseImage loadMode = new BaseImage(ImageLoader.loadImage("/res/misc/load-mode.png"), modeX, modeY);
        BaseImage copyMode = new BaseImage(ImageLoader.loadImage("/res/misc/copy-mode.png"), modeX, modeY);
        BaseImage deleteMode = new BaseImage(ImageLoader.loadImage("/res/misc/delete-mode.png"), modeX, modeY);
        HashMap<Integer, BaseImage> modes = new HashMap<Integer, BaseImage>();
        modes.put(SaveMenuScene.SAVE, saveMode);
        modes.put(SaveMenuScene.LOAD, loadMode);
        modes.put(SaveMenuScene.COPY, copyMode);
        modes.put(SaveMenuScene.DEL, deleteMode);



        SaveMenuScene tempSaveMenuScene = new SaveMenuScene(this, Scene.DMS_SCENE, save, 6);
        tempSaveMenuScene.createSlotImages(ImageLoader.loadImage("/res/buttons/empty-slot.png"), ImageLoader.loadImage("/res/buttons/focused-slot.png"));
        tempSaveMenuScene.createSlots(12, 2, 3, 44, 44, 31);
        tempSaveMenuScene.createMiscButtons(new Button[]{returnButton, previousButton, nextButton});
        tempSaveMenuScene.createYn(new Button[]{yesButton, noButton});
        tempSaveMenuScene.createModes(modes);
        tempSaveMenuScene.setPackId(Scene.NO_PACK);
        publicScenes.put(tempSaveMenuScene.getSceneId(), tempSaveMenuScene);
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

        BufferedImage saveImage = ImageLoader.loadImage("/res/misc/save.png");
        BufferedImage loadImage = ImageLoader.loadImage("/res/misc/load.png");
        BufferedImage copyImage = ImageLoader.loadImage("/res/misc/copy.png");
        BufferedImage deleteImage = ImageLoader.loadImage("/res/misc/delete.png");
        int spacing = 3;


        RectButton saveButton = new RectButton(this);
        saveButton.setImages(saveImage, focusMisc, (textArea.getX() + tempTextArea.getWidth() + spacing), textArea.getY());
        saveButton.setAudioName(audioClick);
        saveButton.setId(SaveMenuScene.SAVE);

        RectButton loadButton = new RectButton(this);
        loadButton.setImages(loadImage, focusMisc, saveButton.getX(), (saveButton.getY() + saveButton.getHeight() + spacing));
        loadButton.setAudioName(audioClick);
        loadButton.setId(SaveMenuScene.LOAD);

        RectButton copyButton = new RectButton(this);
        copyButton.setImages(copyImage, focusMisc, loadButton.getX(), (loadButton.getY() + loadButton.getHeight() + spacing));
        copyButton.setAudioName(audioClick);
        copyButton.setId(SaveMenuScene.COPY);

        RectButton deleteButton = new RectButton(this);
        deleteButton.setImages(deleteImage, focusMisc, copyButton.getX(), (copyButton.getY() + copyButton.getHeight() + spacing));
        deleteButton.setAudioName(audioClick);
        deleteButton.setId(SaveMenuScene.DEL);

        //
        RectButton mainButton = new RectButton(this);
        mainButton.setImages(ImageLoader.loadImage("/res/misc/main.png"), focusMisc, 100, 0);
        mainButton.setAudioName(audioClick);
        mainButton.setId(SaveMenuScene.MAIN);

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
        FadeScene scene0 = new FadeScene(this, bgScene0, fadeoutSlow, 66, 0);
        scene0.setBackgroundAudio(trainningCenterAudio, MyJukeBox.LOOP);
        packBasis.put(0, scene0);

        //cena 1
        String txtScene1 = sentences.get(1);
        StandardScene scene1 = new StandardScene(this, bgScene0, setasAnim, 2, 1);
        scene1.setDialog(txtScene1, fontDataBase, textArea, nameBg);
        scene1.setBackgroundAudio(trainningCenterAudio, MyJukeBox.LOOP);
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
        EfxScene scene2 = new EfxScene(this, images, bgScene0, 3, 2);
        scene2.setBackgroundAudio(trainningCenterAudio, MyJukeBox.LOOP);
        packBasis.put(2, scene2);

        //cena 3
        BaseImage roseSc3 = new BaseImage(rose, 0, 0);
        BaseImage tuliSc3 = new BaseImage(tuli, 400, 0);
        BaseImage[] characters = {roseSc3, tuliSc3};
        String txtScene3 = sentences.get(3);
        StandardScene scene3 = new StandardScene(this, bgScene0, setasAnim, 4, 3);
        scene3.setDialog(txtScene3, fontDataBase, textArea, nameBg);
        scene3.setCharacters(characters);
        scene3.setBackgroundAudio(trainningCenterAudio, MyJukeBox.LOOP);
        packBasis.put(3, scene3);

        //cena 4 //0.05f old speed
        BaseImage[] bottom4 = {bgScene0, roseSc3, tuliSc3};
        ImageEfx bgSc4 = new ImageEfx(this, imgScene0, new Coordinates(0, 0));
        bgSc4.setAlpha(ImageEfx.TRANSPARENT, 0.025f);
        ImageEfx tuliSc4 = new ImageEfx(this, tuli, (new Coordinates((float)((getWidth() / 2) - (tuli.getWidth() / 2)), 0)));
        tuliSc4.setAlpha(ImageEfx.TRANSPARENT, 0.025f);
        ImageEfx[] top4 = {bgSc4, tuliSc4};
        FadeTopBottomScene scene4 = new FadeTopBottomScene(this, bottom4, top4, 5, 4);
        scene4.setTextArea(textArea);
        scene4.setBackgroundAudio(trainningCenterAudio, MyJukeBox.LOOP);
        packBasis.put(4, scene4);

        //cena 5
        String txtScene5 = sentences.get(5);
        StandardScene scene5 = new StandardScene(this, bgScene0, setasAnim, 6, 5);
        scene5.setDialog(txtScene5, fontDataBase, textArea, nameBg);
        BaseImage tuliSc5 = new BaseImage(tuli, (int)((getWidth() / 2) - (tuli.getWidth()/2)), 0);
        BaseImage[] chars5 = {tuliSc5};
        scene5.setCharacters(chars5);
        scene5.setBackgroundAudio(trainningCenterAudio, MyJukeBox.LOOP);
        packBasis.put(5, scene5);

        //cena 6
        BaseImage[] bottom6 = {bgScene0, tuliSc5};
        ImageEfx bgSc6 = new ImageEfx(this, imgScene0, new Coordinates(0, 0));
        bgSc6.setAlpha(ImageEfx.TRANSPARENT, 0.01f);
        ImageEfx roseSc6 = new ImageEfx(this, rose, new Coordinates(((float)(getWidth() / 2) - (float)(rose.getWidth() / 2)), 0));
        roseSc6.setAlpha(ImageEfx.TRANSPARENT, 0.01f);
        ImageEfx[] top6 = {bgSc6, roseSc6};
        FadeTopBottomScene scene6 = new FadeTopBottomScene(this, bottom6, top6, 7, 6);
        scene6.setTextArea(textArea);
        scene6.setBackgroundAudio(trainningCenterAudio, MyJukeBox.LOOP);
        packBasis.put(6, scene6);

        //cena 7
        String txtScene7 = sentences.get(7);
        StandardScene scene7 = new StandardScene(this, bgScene0, setasAnim, 8, 7);
        scene7.setDialog(txtScene7, fontDataBase, textArea, nameBg);
        BaseImage roseSc7 = new BaseImage(rose, getWidth() / 2 - rose.getWidth() / 2, 0);
        scene7.setCharacters(new BaseImage[]{roseSc7});
        scene7.setBackgroundAudio(trainningCenterAudio, MyJukeBox.LOOP);
        packBasis.put(7, scene7);

        //cena 8
        FadeScene scene8 = new FadeScene(this, new BaseImage[]{bgScene0, roseSc7}, fadeinFast, 9, 8);
        scene8.setBackgroundAudio(trainningCenterAudio, MyJukeBox.LOOP);
        packBasis.put(8, scene8);

        //cena 9
        BaseImage livro1 = new BaseImage(livro1Buffered);
        FadeScene scene9 = new FadeScene(this, livro1, fadeoutFast, 10, 9);
        scene9.setBackgroundAudio(trainningCenterAudio, MyJukeBox.LOOP);
        packBasis.put(9, scene9);

        //cena 10
        String txtScene10 = sentences.get(10);
        StandardScene scene10 = new StandardScene(this, livro1, setasAnim, 11, 10);
        scene10.setDialog(txtScene10, fontDataBase, textArea, nameBg);
        scene10.setBackgroundAudio(trainningCenterAudio, MyJukeBox.LOOP);
        packBasis.put(10, scene10);

        //cena 11
        ImageEfx efxLivro2 = new ImageEfx(this, livro2Buffered, new Coordinates(0, 0));
        efxLivro2.setAlpha(ImageEfx.TRANSPARENT, 0.02f);
        EfxScene scene11 = new EfxScene(this, new ImageEfx[]{efxLivro2}, livro1, 12, 11);
        //scene11.setTextArea(textArea);
        scene11.setBackgroundAudio(trainningCenterAudio, MyJukeBox.LOOP);
        packBasis.put(11, scene11);

        //cena 12
        String txtScene12 = sentences.get(12);
        BaseImage livro2 = new BaseImage(livro2Buffered);
        BaseImage[] imagesSc12 = {livro1, livro2};
        StandardScene scene12 = new StandardScene(this, imagesSc12, setasAnim, 13, 12);
        scene12.setDialog(txtScene12, fontDataBase, textArea, nameBg);
        scene12.setBackgroundAudio(trainningCenterAudio, MyJukeBox.LOOP);
        packBasis.put(12, scene12);

        //cena 13
        ImageEfx efxLivro3 = new ImageEfx(this, livro3Buffered, new Coordinates(0, 0));
        efxLivro3.setAlpha(ImageEfx.TRANSPARENT, 0.02f);
        EfxScene scene13 = new EfxScene(this, new ImageEfx[]{efxLivro3}, imagesSc12, 14, 13);
        scene13.setBackgroundAudio(trainningCenterAudio, MyJukeBox.LOOP);
        packBasis.put(13, scene13);

        //cena 14
        String txtScene14 = sentences.get(14);
        BaseImage livro3 = new BaseImage(livro3Buffered);
        StandardScene scene14 = new StandardScene(this, new BaseImage[]{livro1, livro2, livro3}, setasAnim, 15, 14);
        scene14.setDialog(txtScene14, fontDataBase, textArea, nameBg);
        scene14.setBackgroundAudio(trainningCenterAudio, MyJukeBox.LOOP);
        packBasis.put(14, scene14);

        //cena 15 - temp
        String txtScene15 = sentences.get(15);
        StandardScene scene15 = new StandardScene(this, bgScene0, setasAnim, 0, 15);
        //scene15.setNextPack(0);
        scene15.setDialog(txtScene15, fontDataBase, textArea, nameBg);
        scene15.setBackgroundAudio(trainningCenterAudio, MyJukeBox.LOOP);
        packBasis.put(15, scene15);

        //cena 99
        String txtScene99 = sentences.get(99);
        StandardScene scene99 = new StandardScene(this, bgScene0, setasAnim, 1, 99);
        scene99.setDialog(txtScene99, fontDataBase, textArea, nameBg);
        scene99.setBackgroundAudio(trainningCenterAudio, MyJukeBox.LOOP);
        scene99.setButtonGroup(sceneMenu);
        packBasis.put(99, scene99);

        //cena 66 - temp
        VolumeScene volumeScene = new VolumeScene(this, 66);
        packBasis.put(volumeScene.getSceneId(), volumeScene);


    }


    // todo : imageEfx e Stan, com os arrays estao ok, nao quero alterar
}