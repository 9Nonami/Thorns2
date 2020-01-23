package nona.mi.scene;

import java.awt.Graphics;

import nona.mi.button.ButtonGroup;
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
	protected boolean lock; //faz com que o codigo do update so execute uma vez
	public static final int LAST_SCENE = -99; //definir como last_scene significa que depois desta cena, outro pack eh carregado. NAO ESQUECER DE DEFINIR O PACK!
	private ButtonGroup buttonGroup; //save, load, copy, del



	public Scene(Game game, int nextScene) {
		this.game = game;
		this.nextScene = nextScene;
	}

	public Scene(Game game) {
		this.game = game;
	}

	public void update() {
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

		updateButtonGroup();

	}

	private void updateButtonGroup() { //todo : parar o audio?? > se pah nao > se for fala, sim; talvez o bg nao, mas se pah sim.
		if (buttonGroup != null) {
			buttonGroup.update();
			int type = buttonGroup.getClickedButton();
			if (type != ButtonGroup.NO_CLICK) {
				if (type == SaveMenuScene.SAVE) {
					game.getSaveMenuScene().setType(type);
					game.getSaveMenuScene().setInfo(game.getPack(), game.getScene(), game.getFrame());
					game.setSceneBasisWithoutReset(game.getSaveMenuScene()); //para nao comecar a cena do 0 quando voltar
					buttonGroup.reset();
					game.setClicked(false);
				} else if (type == SaveMenuScene.LOAD) {

				} else if (type == SaveMenuScene.COPY) {

				} else if (type == SaveMenuScene.DEL) {

				}
			}
		}
	}

	public void render(Graphics g) {
		renderScene(g);
		if (buttonGroup != null) {
			buttonGroup.render(g);
		}
	}

	public abstract void renderScene(Graphics g);

	public void defineSound(String soundName, int soundStyle) {
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

	public void reset() {
		lock = false;
		if (buttonGroup != null) {
			buttonGroup.reset();
		}
	}

	public void createSound(String path, int soundStyle) {
		String key = path.substring(path.lastIndexOf("/") + 1, path.indexOf("."));
		game.getPackJukebox().load(path, key);
		soundName = key;
		this.soundStyle = soundStyle;
	}

}