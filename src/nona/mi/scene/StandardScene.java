package nona.mi.scene;

import java.awt.Color;
import java.awt.Graphics;

import nona.mi.button.Button;
import nona.mi.image.BaseImage;
import nona.mi.main.Game;



public class StandardScene extends Scene {

    private BaseImage background;
    private BaseImage[] backgrounds;

    private BaseImage[] characters;

    private Dialogue[] dialogues;
    private Dialogue dialogueBasis;
    private int dialogueID;

    private boolean mainClicked;
    private boolean hide;
    private boolean lockHCheck;



    //ESSENCIAL--------------------------------------

    public StandardScene(Game game, BaseImage background, int nextScene, int sceneId) {
        super(game, nextScene, sceneId);
        this.background = background;
    }

    //para o caso de mais uma imagem como BG
    public StandardScene(Game game, BaseImage[] backgrounds, int nextScene, int sceneId) {
        super(game, nextScene, sceneId);
        this.backgrounds = backgrounds;
    }

    //-----------------------------------------------


    //GS----------------------------------------------

    public Dialogue[] getDialogues() {
        if (dialogues == null) {
            System.exit(0);
        }
        return dialogues;
    }

    public void setDialog(String s) {

        //-Rose:asd@zxc#/res/audio/wht.wav_-:qwe

        String[] splitedDialogues = s.split("_");
        Dialogue[] tempDialogues = new Dialogue[splitedDialogues.length];


        for (int i = 0; i < tempDialogues.length; i++) {
            //CRIA O DIALOG
            tempDialogues[i] = new Dialogue(game, game.getFontDataBase());

            //DEFINE O TEXTO E O AUDIO, CASO HAJA
            String dialog;
            if (splitedDialogues[i].contains("#")) {
                dialog = splitedDialogues[i].substring(splitedDialogues[i].indexOf(":") + 1, splitedDialogues[i].indexOf("#"));

                String tempAudioPath;
                String tempAudioName;

                if (splitedDialogues[i].endsWith("f")){
                    tempDialogues[i].setPlayFullAudio(true);
                    tempAudioPath = splitedDialogues[i].substring(splitedDialogues[i].indexOf("#") + 1, splitedDialogues[i].lastIndexOf("f"));
                    tempAudioName = tempAudioPath.substring(tempAudioPath.lastIndexOf("/") + 1, tempAudioPath.indexOf("."));
                } else {
                    tempAudioPath = splitedDialogues[i].substring(splitedDialogues[i].indexOf("#") + 1);
                    tempAudioName = tempAudioPath.substring(tempAudioPath.lastIndexOf("/") + 1, tempAudioPath.indexOf("."));
                }

                tempDialogues[i].setAudio(tempAudioName, tempAudioPath);

            } else {
                dialog = splitedDialogues[i].substring(splitedDialogues[i].indexOf(":") + 1);
            }

            //ATRIBUI O TEXTO AO DIALOG
            tempDialogues[i].setDialogue(dialog.toCharArray());

            //DEFINE O NOME, SE HOUVER
            String name = splitedDialogues[i].substring((splitedDialogues[i].indexOf("-") + 1), (splitedDialogues[i].indexOf(":")));
            if (!(name.equals(""))) {
                tempDialogues[i].setName(game.getNameDataBase().get(name));
            }

        }

        dialogues = tempDialogues;
        dialogueID = 0;
        dialogueBasis = dialogues[dialogueID];
    }

    public void setCharacters(BaseImage[] characters) {
        this.characters = characters;
    }

    public void setLockHCheck(boolean lockHCheck) {
        //se for apertado 'h' em outra cena, elas usam
        //este metodo para resetar a var antes de voltar para cah
        this.lockHCheck = lockHCheck;
    }

    //------------------------------------------------


    //UR----------------------------------------------

    @Override
    public void updateScene() {
        //o audio eh iniciado em Scene

        resetHStuff(); //se antes de chegar a esta cena 'h' foi pressionado, reseta para nao comecar com o dialogo escondido - soh executa uma vez

        if (!mainClicked) {
            hide = game.ishKey();
        }

        if (!hide) {
            if (!mainClicked) {
                updateButtonGroup();
                if (game.getSlcd().getClickedButton() == Button.NO_CLICK) {
                    updateDialog();
                }
            } else {
                updateYn();
            }
        }

    }

    private void updateButtonGroup() {
        game.getSlcd().update();
        if (game.getSlcd().getClickedButton() != Button.NO_CLICK) {

            //se estiver no meio do audio quando um botao for pressionado, o pausa
            pauseDialogAudio();

            if (game.getSlcd().getClickedButton() == DataManagerScene.MAIN) {
                mainClicked = true;
            } else if (game.getSlcd().getClickedButton() == HISTORY_SCENE) {
                HistoryScene tempHistoryScene = (HistoryScene) game.getSceneFromPublicScenes(HISTORY_SCENE);
                tempHistoryScene.setSceneToReturn(sceneId);
                System.out.println("retornarah para: " + sceneId);
                tempHistoryScene.checkInitialId();
                game.setSceneBasisFromPublicScenesWithoutReset(HISTORY_SCENE);
                game.getSlcd().reset();
                game.setClicked(false);
            } else {
                DataManagerScene tempDataManagerScene = (DataManagerScene) game.getSceneFromPublicScenes(DMS_SCENE);
                tempDataManagerScene.setType(game.getSlcd().getClickedButton());
                tempDataManagerScene.setInfo(game.getSceneBasis().getPackId(), game.getSceneBasis().getSceneId(), game.getFrame());
                game.setSceneBasisFromPublicScenesWithoutReset(DMS_SCENE); //para nao resetar a cena
                game.getSlcd().reset();
                game.setClicked(false);
            }
        }
    }

    private void updateDialog() {

        dialogueBasis.update();

        boolean clicked = game.isClicked();
        boolean space = game.isSpace();

        //termina o audio e a animacao do texto
        if ((clicked || space) && !(dialogueBasis.getEndAnimation())) {
            dialogueBasis.completeDialogue();
            clicked = false;
            space = false;
            if(dialogueBasis.getAudioName() != null && game.getPackJukebox().isPlaying(dialogueBasis.getAudioName())) {
                game.getPackJukebox().stop(dialogueBasis.getAudioName());
            }
        }

        //update das setas
        if (dialogueBasis.getEndAnimation()){
            game.getSetasAnim().update();
        }

        //proximo dialogo ou cena
        if (dialogueBasis.getEndAnimation() && (space || clicked)) {

            if (dialogueBasis.getAudioName() != null && game.getPackJukebox().isPlaying(dialogueBasis.getAudioName())) {
                game.getPackJukebox().stop(dialogueBasis.getAudioName());
            }

            dialogueBasis.reset();
            game.getContForStan().reset();
            game.getSetasAnim().reset();

            dialogueID++;

            //vai para a proxima cena pois cheguou ao final dos dialogos
            if (dialogueID == dialogues.length) {
                dialogueID = 0;
                dialogueBasis = dialogues[dialogueID];
                game.getSave().getTracer().add(sceneId);
                game.nextScene();
                return;
            }

            dialogueBasis = dialogues[dialogueID];
        }

    }

    private void updateYn() {
        game.getYn().update();
        if (game.getYn().getClickedButton() != Button.NO_CLICK) {
            if (game.getYn().getClickedButton() == DataManagerScene.YES) {
                game.returnToMainMenu(); //reseta esta cena
            } else if (game.getYn().getClickedButton() == DataManagerScene.NO) {
                mainClicked = false;
                game.getYn().reset();
                //para o caso de hide ser ativado
                lockHCheck = false;
                resetHStuff();
                hide = false;
                //retoma uma fala caso tenha sido pausada
                resumeDialogAudio();
            }
        }
    }


    @Override
    public void renderScene(Graphics g) {
        renderBackground(g);
        renderCharacters(g);
        renderDialog(g);
        renderSlcd(g);
        renderYn(g);
    }

    private void renderBackground(Graphics g) {
        if (backgrounds != null) {
            for (BaseImage baseImage : backgrounds) {
                baseImage.render(g);
            }
        } else {
            if (background != null) {
                background.render(g);
            }
        }
    }

    private void renderCharacters(Graphics g) {
        if (characters != null) {
            for (BaseImage character : characters) {
                character.render(g);
            }
        }
    }

    private void renderDialog(Graphics g) {
        if (!hide) {
            dialogueBasis.render(g);
            if (dialogueBasis.getEndAnimation()) {
                game.getSetasAnim().render(g);
            }
        }
    }

    private void renderSlcd(Graphics g) {
        if (!hide) {
            game.getSlcd().render(g);
        }
    }

    private void renderYn(Graphics g) {
        if (mainClicked) {
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, game.getWidth(), game.getHeight());
            game.getYn().render(g);
        }
    }

    //------------------------------------------------


    //OTHER-------------------------------------------

    public void pauseDialogAudio() {
        String audioName = dialogueBasis.getAudioName();
        if (audioName != null) {
            if (game.getPackJukebox().isPlaying(audioName)) {
                game.getPackJukebox().stop(audioName);
                dialogueBasis.setAudioPaused(true);
            }
        }
    }

    public void resumeDialogAudio() {
        String audioName = dialogueBasis.getAudioName();
        if (audioName != null && dialogueBasis.isAudioPaused()) {
            game.getPackJukebox().resume(audioName);
        }
    }

    //------------------------------------------------


    @Override
    public void reset(){
        super.reset();

        //se der load durante a animacao do dialogue,
        //talvez ele nao seja resetado, isso aqui
        //forca o reset, mesmo que ja esteja resetado
        dialogueID = 0;
        dialogueBasis = dialogues[dialogueID];
        for (Dialogue dialogue : dialogues) {
            dialogue.reset();
        }
        game.getContForStan().reset();
        game.getSetasAnim().reset();

        game.getYn().reset();
        game.getSlcd().reset();

        lockHCheck = false;
        mainClicked = false;

    }

    private void resetHStuff() {
        if (!lockHCheck) {
            lockHCheck = true;
            game.resetHStuff();
        }
    }

}
