package nona.mi.scene;

import java.awt.Graphics;

import nona.mi.image.BaseImage;
import nona.mi.db.FontDataBase;
import nona.mi.image.ImageEfx;
import nona.mi.main.Thorns;

public class StandardScene extends Scene {

    private BaseImage background;
    private BaseImage[] backgrounds;
    private BaseImage[] characters;

    private Dialogue[] dialogues;
    private int dialogueID;
    private Dialogue dialogueBasis;
    private boolean space;

    private ImageEfx setasAnim;


    public StandardScene(Thorns thorns, BaseImage background, String dialog, int nextScene) {
        super(thorns, nextScene);
        this.background = background;
        this.setasAnim = thorns.getSetasAnim();
        setDialog(dialog, thorns.getFontDataBase(), thorns.getTextArea(), thorns.getNameBg());
    }

    //para o caso de mais uma imagem como BG
    public StandardScene(Thorns thorns, BaseImage[] backgrounds, String dialog, int nextScene) {
        super(thorns, nextScene);
        this.backgrounds = backgrounds;
        this.setasAnim = thorns.getSetasAnim();
        setDialog(dialog, thorns.getFontDataBase(), thorns.getTextArea(), thorns.getNameBg());
    }

    public void setDialog(String s, FontDataBase fdb, BaseImage textArea, BaseImage nameBg) {

        //-Rose:asd@zxc#/res/audio/wht.wav_-:qwe

        String[] splitedDialogues = s.split("_");
        Dialogue[] tempDialogues = new Dialogue[splitedDialogues.length];

        int xx = 10;
        int yy = (int) (textArea.getY() + xx); //+ xx para espacamento

        for (int i = 0; i < tempDialogues.length; i++) {
            //CRIA O DIALOG
            tempDialogues[i] = new Dialogue(thorns, fdb, xx, yy, textArea, nameBg);

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

                float delayInSeconds = 0f;

                tempDialogues[i].setAudio(delayInSeconds, tempAudioName, tempAudioPath);

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

    @Override
    public void update() {
        super.update();

        dialogueBasis.update();

        space = thorns.getSpace();

        if  (dialogueBasis.getEndAnimation()){
            setasAnim.update();
        }

        if (dialogueBasis.getEndAnimation() && space) {

            if(dialogueBasis.getAudioName() != null){
                thorns.getMyJukeBox().stop(dialogueBasis.getAudioName());
            }

            dialogueBasis.reset();
            setasAnim.reset();

            dialogueID++;

            if (dialogueID == dialogues.length) {
                dialogueID = 0;
                dialogueBasis = dialogues[dialogueID];
                thorns.nextScene(nextScene);
                reset();
                return;
            }

            dialogueBasis = dialogues[dialogueID];
        }
    }

    @Override
    public void render(Graphics g) {

        if (backgrounds != null) {
            for (int i = 0; i < backgrounds.length; i++) {
                backgrounds[i].render(g);
            }
        } else {
            background.render(g);
        }

        if (characters != null) {
            for (int i = 0; i < characters.length; i++) {
                characters[i].render(g);
            }
        }

        dialogueBasis.render(g);

        if  (dialogueBasis.getEndAnimation()){
            setasAnim.render(g);
        }
    }

    @Override
    public void reset(){
        super.reset();
    }

}
