package nona.mi.scene;

import java.awt.Graphics;

import nona.mi.button.Button;
import nona.mi.jukebox.MyJukeBox;
import nona.mi.main.Game;
import nona.mi.menu.Menu;

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
	public static final int SAVE_MENU_SCENE = -98;
	protected Menu menu;



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

		updateMenu();

	}

	private void updateMenu() { //todo : parar o audio??
		if (menu != null) {
			menu.update();
			for (Button temp : menu.getButtons()) {
				if (temp.isClicked()) {
					if (temp.getNextScene() == SAVE_MENU_SCENE) {
						game.getSaveMenu().setInfo(game.getPack(), game.getScene(), game.getFrame());
						game.setSceneBasisWithoutReset(game.getSaveMenu());
						menu.reset();
						game.setClicked(false);
					} //todo : outras cenas
					break;
				}
			}
		}
	}

	public void render(Graphics g) {
		renderScene(g);
		if (menu != null) {
			menu.render(g);
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

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	public void reset() {
		lock = false;
		if (menu != null) {
			menu.reset();
		}
	}

	public void createSound(String path, int soundStyle) {
		String key = path.substring(path.lastIndexOf("/") + 1, path.indexOf("."));
		game.getPackJukebox().load(path, key);
		soundName = key;
		this.soundStyle = soundStyle;
	}

}