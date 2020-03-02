package nona.mi.scene;

import java.awt.Graphics;

import nona.mi.image.BaseImage;
import nona.mi.db.FontDataBase;
import nona.mi.image.ImageEfx;
import nona.mi.main.Game;

public class StandardScene extends Scene {

    private BaseImage background;
    private BaseImage[] backgrounds;
    private BaseImage[] characters;

    private Dialogue[] dialogues; //resets
    private int dialogueID;
    private Dialogue dialogueBasis;
    private boolean space;
    private boolean clicked;

    private ImageEfx setasAnim; //resets



    //ESSENCIAL--------------------------------------

    public StandardScene(Game game, BaseImage background, ImageEfx setasAnim, int nextScene, int sceneId) {
        super(game, nextScene, sceneId);
        this.background = background;
        this.setasAnim = setasAnim;
        buttonGroup = game.getSceneMenu();
    }

    //para o caso de mais uma imagem como BG
    public StandardScene(Game game, BaseImage[] backgrounds, ImageEfx setasAnim, int nextScene, int sceneId) {
        super(game, nextScene, sceneId);
        this.backgrounds = backgrounds;
        this.setasAnim = setasAnim;
        buttonGroup = game.getSceneMenu();
    }

    //-----------------------------------------------


    //GS----------------------------------------------

    public void setDialog(String s, FontDataBase fdb, BaseImage textArea, BaseImage nameBg) {

        //-Rose:asd@zxc#/res/audio/wht.wav_-:qwe

        String[] splitedDialogues = s.split("_");
        Dialogue[] tempDialogues = new Dialogue[splitedDialogues.length];

        int xx = 10;
        int yy = (int) (textArea.getY() + xx); //+ xx para espacamento

        for (int i = 0; i < tempDialogues.length; i++) {
            //CRIA O DIALOG
            tempDialogues[i] = new Dialogue(game, fdb, xx, yy, textArea, nameBg);

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
            if (name != null && !(name.equals(""))) {
                tempDialogues[i].setName(name.toCharArray());
            }
        }

        dialogues = tempDialogues;
        dialogueID = 0;
        dialogueBasis = dialogues[dialogueID];
    }

    public void setCharacters(BaseImage[] characters) {
        this.characters = characters;
    }

    //------------------------------------------------


    //UR----------------------------------------------

    @Override
    public void updateScene() {

        //nao faz sentido atualizar se nao estiver visivel
        if (!hide) {
            dialogueBasis.update();
            space = game.isSpace();
            clicked = game.isClicked();
        }

        //termina o audio e a animacao do texto
        if (clicked && !(dialogueBasis.getEndAnimation())) {
            dialogueBasis.completeDialogue();
            clicked = false;
            space = false;
            if(dialogueBasis.getAudioName() != null && game.getPackJukebox().isPlaying(dialogueBasis.getAudioName())) {
                game.getPackJukebox().stop(dialogueBasis.getAudioName());
            }
        }

        //update das setas
        if (dialogueBasis.getEndAnimation()){
            setasAnim.update();
        }

        //proximo dialogo ou cena
        if (dialogueBasis.getEndAnimation() && (space || clicked)) {

            if(dialogueBasis.getAudioName() != null && game.getPackJukebox().isPlaying(dialogueBasis.getAudioName())) {
                game.getPackJukebox().stop(dialogueBasis.getAudioName());
            }

            dialogueBasis.reset();
            setasAnim.reset(); //I don't think it is really necessary, but ok

            dialogueID++;

            if (dialogueID == dialogues.length) {
                dialogueID = 0;
                dialogueBasis = dialogues[dialogueID];
                game.nextScene();
                return;
            }

            dialogueBasis = dialogues[dialogueID];
        }
    }

    @Override
    public void renderScene(Graphics g) {

        if (backgrounds != null) {
            for (BaseImage baseImage : backgrounds) {
                baseImage.render(g);
            }
        } else {
            background.render(g);
        }

        if (characters != null) {
            for (BaseImage character : characters) {
                character.render(g);
            }
        }

        if (!hide) {
            dialogueBasis.render(g);
            if (dialogueBasis.getEndAnimation()) {
                setasAnim.render(g);
            }
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
        for (int i = 0; i < dialogues.length; i++) {
            dialogues[i].reset();
        }
        setasAnim.reset();
    }

}
