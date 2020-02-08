package nona.mi.scene;

import java.awt.Color;
import java.awt.Graphics;

import nona.mi.button.ButtonGroup;
import nona.mi.constant.ID;
import nona.mi.jukebox.MyJukeBox;
import nona.mi.main.Game;

// Com relacao ao audio, Scene so toca musicas de fundo.
// Caso algum audio esteja relacionado a fala do personagem
// ou caso seja algum efeito, fazer em outra classe.

public abstract class Scene {

	protected Game game;
	protected int nextPack;
	protected int nextScene;
	protected String soundName; //background sound
	protected int soundStyle; //loop ou once
	protected boolean lock; //faz com que o codigo do updateAudio so execute uma vez
	public static final int LAST_SCENE = -99; //definir como last_scene significa que depois desta cena, outro pack eh carregado. NAO ESQUECER DE DEFINIR O PACK!
	private ButtonGroup buttonGroup; //save, load, copy, del

	protected ButtonGroup yn; //confirma a ida para o main
	protected boolean esc;

	protected int sceneId;
	protected int packId;



	//ESSENCIAL------------------------------------------------------

	public Scene(Game game, int nextScene, int sceneId) {
		this.game = game;
		this.nextScene = nextScene;
		this.sceneId = sceneId;
		yn = game.getYn();
	}

	public Scene(Game game, int sceneId) {
		this.game = game;
		this.sceneId = sceneId;
		yn = game.getYn();
	}

	//----------------------------------------------------------------


	//UR--------------------------------------------------------------

	public void update() {
		if (!esc) {
			updateAudio();
			updateButtonGroup();
			if (!esc) { //precisa disso pois o metodo acima muda o status de esc
				updateScene();
			}
		} else {
			yn.update();
			if (yn.getClickedButton() != ID.NO_CLICK) {
				if (yn.getClickedButton() == ID.YES) {
					game.returntoMainMenu();
				} else if (yn.getClickedButton() == ID.NO) {
					esc = false;
					yn.reset();

					//retoma uma fala caso tenha sido pausada
					if (game.getSceneBasis() instanceof StandardScene) {
						StandardScene temp = (StandardScene) game.getSceneBasis();
						temp.resumeDialogAudio();
					}
				}
			}
		}
	}

	public abstract void updateScene();

	private void updateAudio() {
		if	(!lock && soundName != null) {

			String temp = game.getCurrentSound();
			System.out.println("audio atual: " + temp);

			// verifica se esta tocando o mesmo audio que a cena anterior
			if (temp != null) {
				if (temp.equals(soundName) && game.getPackJukebox().isPlaying(temp)) {
					System.out.println("RETORNANDO! mesmo audio!" + "\n");
					lock = true;
					return;
				}
			}

			// executa se o audio for diferente da cena anterior
			if	(game.getPackJukebox().isPlaying(temp)) {
				game.getPackJukebox().stop(temp);
				System.out.println("audio PARADO: " + temp);
			}

			game.setCurrentSound(this.soundName);

			System.out.println("novo audio: " + soundName + "\n");

			if	(soundStyle == MyJukeBox.ONCE) {
				game.getPackJukebox().play(this.soundName);
			} else if (soundStyle == MyJukeBox.LOOP) {
				game.getPackJukebox().loop(this.soundName);
			}

			lock = true;
		}
	}

	private void updateButtonGroup() {
		if (buttonGroup != null) {
			buttonGroup.update();
			if (buttonGroup.getClickedButton() != ID.NO_CLICK) {

				//se estiver no meio do audio quando um botao for pressionado, pausa
				if (game.getSceneBasis() instanceof StandardScene) {
					StandardScene temp = (StandardScene) game.getSceneBasis();
					temp.pauseDialogAudio();
				}

				if (buttonGroup.getClickedButton() == ID.MAIN) {
					esc = true;
				} else {

					SaveMenuScene tempSaveMenuScene = (SaveMenuScene) game.getSceneFromPublicScenes(ID.DMS_SCENE);
					tempSaveMenuScene.setType(buttonGroup.getClickedButton());
					tempSaveMenuScene.setInfo(game.getPack(), game.getScene(), game.getFrame()); // nao vou passar packId e sceneId para saber se scene:game esta atualizando certinho
					game.setSceneBasisWithoutReset(ID.DMS_SCENE); //para nao resetar a cena
					buttonGroup.reset();
					game.setClicked(false);
				}
			}
		}
	}

	public void render(Graphics g) {
		renderScene(g);
		renderButtons(g);
		renderYn(g);
	}

	private void renderButtons(Graphics g) {
		if (buttonGroup != null) {
			buttonGroup.render(g);
		}
	}

	private void renderYn(Graphics g) {
		if (esc) {
			g.setColor(new Color(0, 0, 0, 150));
			g.fillRect(0, 0, game.getWidth(), game.getHeight());
			yn.render(g);
		}
	}

	public abstract void renderScene(Graphics g);

	//----------------------------------------------------------------


	//GS--------------------------------------------------------------

	public void setBackgroundAudio(String soundName, int soundStyle) {
		this.soundName = soundName;
		this.soundStyle = soundStyle;
	}

	public void setNextPack(int nextPack) {
		this.nextPack = nextPack;
	}

	public int getNextPack() {
		return nextPack;
	}

	public void setNextScene(int nextScene) {
		this.nextScene = nextScene;
	}

	public int getNextScene() {
		return nextScene;
	}

	public void setButtonGroup(ButtonGroup buttonGroup) {
		this.buttonGroup = buttonGroup;
	}

	public int getSceneId() {
		return sceneId;
	}

	public int getPackId() {
		return packId;
	}

	public void setPackId(int packId) {
		this.packId = packId;
	}

	//----------------------------------------------------------------

	public void reset() {
		lock = false;
		if (buttonGroup != null) {
			buttonGroup.reset();
		}
		yn.reset();
		esc = false;
	}

}