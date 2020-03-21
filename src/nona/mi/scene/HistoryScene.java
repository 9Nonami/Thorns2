package nona.mi.scene;

import nona.mi.button.Button;
import nona.mi.button.ButtonGroup;
import nona.mi.main.Game;
import nona.mi.save.Tracer;

import java.awt.Color;
import java.awt.Graphics;

public class HistoryScene extends Scene {

    private int tracerId; //usado para controlar qual cena resgatar do trace
    private int initialTracerId; //guarda o id inicial para previnir que nao sejam acessado ids vazios
    private int dialogId; //id do dialogo dentro do array de dialogue adquirido da cena

    private int sceneToReturn; //id da cena para a qual retornar - atribuido na vinda para esta cena

    private Dialogue[] dialogues; //container para os dialogos da cena resgatada
    private Dialogue dialogue; //dialogo container
    private ButtonGroup btns; //armazena os botoes desta cena
    public static final int PREV = 0; //id para a criacao dos botoes
    public static final int NEXT = 1; //id para a criacao dos botoes
    public static final int RETURN = 2; //id para a criacao dos botoes

    public static final int NEW_DIALOG_X = 50; //nova posicao do dialogo
    public static final int NEW_DIALOG_Y = 100; //nova posicao do dialogo
    public static final int NEW_NAME_X = 50; //nova posicao do nome
    public static final int NEW_NAME_Y = 50; //nova posicao do nome



    //todo : nopack
    public HistoryScene(Game game, int nextScene, int sceneId) {
        super(game, nextScene, sceneId);
    }

    public HistoryScene(Game game, int sceneId) {
        super(game, sceneId);
    }

    public void setBtns(ButtonGroup btns) {
        this.btns = btns;
    }

    public void setSceneToReturn(int sceneToReturn) {
        this.sceneToReturn = sceneToReturn;
    }

    public void checkInitialId() {
        //verifica a primeira cena em tracer
        boolean tempLock = true;
        int[] tempTraces = game.getSave().getTracer().getTraces();
        for (int i = 0; i < tempTraces.length; i++) {
            if (tempTraces[i] != NO_SCENE) {
                tracerId = i;
                initialTracerId = tracerId;
                dialogId = 0;
                configScene();
                configDialog();
                tempLock = false;
                break;
            }
        }
        if (tempLock) {
            tracerId = tempTraces.length;
            initialTracerId = tracerId;
        }
        System.out.println("initial: " + initialTracerId);
    }

    private void configScene() {
        //resgata o dialogo da cena
        //id da cena armazenado em tracer
        StandardScene tempStan = (StandardScene) game.getSceneFromCurrentPack(game.getSave().getTracer().getTraces()[tracerId]);
        dialogues = tempStan.getDialogues();
    }

    private void configDialog() {
        //define o dialogo a ser renderizado
        dialogue = dialogues[dialogId];
        dialogue.defineCompleteDialogue();
        dialogue.setHistoryConfiguration(true);
        dialogue.setRenderTextArea(false);
    }

    @Override
    public void updateScene() {
        updateButtons();
    }

    private void updateButtons() {
        btns.update();
        if (btns.getClickedButton() != Button.NO_CLICK) {
            if (btns.getClickedButton() == RETURN) {
                game.setDirectScene(sceneToReturn);
                //retoma uma fala caso tenha sido pausada
                if (game.getSceneFromCurrentPack(sceneToReturn) instanceof StandardScene) {
                    StandardScene temp = (StandardScene) game.getSceneFromCurrentPack(sceneToReturn);
                    temp.resumeDialogAudio();
                    temp.setLockHConfig(false); //caso tenha apertado h, não volta para stan com dialog escondido
                }
            } else if (btns.getClickedButton() == PREV) {
                if (tracerId != Tracer.TOTAL_TRACES) {
                    if (dialogId > 0) {
                        dialogId--;
                        configDialog();
                    } else {
                        if (tracerId > initialTracerId) {
                            tracerId--;
                            configScene();
                            dialogId = dialogues.length - 1;
                            configDialog();
                        }
                    }
                }
            } else if (btns.getClickedButton() == NEXT) {
                if (tracerId != Tracer.TOTAL_TRACES) { //entra no metodo se trace nao for vazio
                    //verifica os dialogos ateh chegar ao ultimo id do array
                    if (dialogId < dialogues.length - 1) { //entra se for o penultimo dialogo (ou < ultimo) e muda para o ultimo. sendo o ultimo, nao mais entra aqui
                        dialogId++; //se ainda nao eh o ultimo dialogo, prepara o id do proximo
                        configDialog(); //coloca o dialogo especificado pelo id na cena
                    } else {
                        //quando chegar ao ultimo dialogo, verifica se ha mais cenas
                        if (tracerId < Tracer.TOTAL_TRACES - 1) { //entra se for a penultima cena (ou < ultima) e muda para a ultima. sendo a ultima, nao mais entra aqui
                            tracerId++; //se ainda nao eh a ultimo cena, prepara o id da proxima
                            configScene(); //pega os dialogos da cena
                            dialogId = 0; //como NEXT eh de ordem crescente, define o primeiro dialogo da nova cena
                            configDialog(); //coloca o dialogo especificado pelo id na cena
                        }
                    }
                }
            }
        }
    }

    @Override
    public void renderScene(Graphics g) {
        btns.render(g);
        if (tracerId != Tracer.TOTAL_TRACES) {
            dialogue.render(g);
        } else {
            g.setColor(Color.RED);
            g.drawString("NO HISTORY YET", 100, 100);
        }
    }

    @Override
    public void reset() {

        super.reset();
        btns.reset();

        if (initialTracerId != Tracer.TOTAL_TRACES) {

            for (int i = (Tracer.TOTAL_TRACES - 1); i >= initialTracerId ; i--) {
                int tempIdfromTracer = game.getSave().getTracer().getTraces()[i];
                System.out.println("tentando resetar dialogo da cena: " + tempIdfromTracer);
                StandardScene tempStan = (StandardScene) game.getSceneFromCurrentPack(tempIdfromTracer);
                Dialogue[] tempDialoguesForReset = tempStan.getDialogues();
                for (Dialogue value : tempDialoguesForReset) {
                    value.reset();
                }
            }
        }
        //nao precisa resetar as variaveis int, pois elas serao alteradas quando voltar para esta cena
    }

}
