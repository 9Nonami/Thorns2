package nona.mi.scene;

import java.awt.Graphics;

import nona.mi.loader.MyJukeBox;
import nona.mi.main.Game;

// Com relacao ao audio, Scene so toca musicas de fundo.
// Caso algum audio esteja relacionado a fala do personagem
// ou caso seja algum efeito, fazer em outra classe.

public abstract class Scene {

	protected Game game;
	protected int nextPack;
	protected int nextScene;

	protected String soundName;
	protected int soundStyle; //loop ou once
	protected boolean lock; //faz com que o codigo do update so execute uma vez

	public static final int LAST_SCENE = -99;


	public Scene(Game game, int nextScene) {
		this.game = game;
		this.nextScene = nextScene;
	}

	//LoadScene usa esse, pois nao precisa ir para outra cena
	public Scene(Game game) {
		this.game = game;
	}

	public void update() {
		if	(!lock && soundName != null) {

			String temp = game.getCurrentSound();

			// verifica se esta tocando o mesmo audio que a cena anterior
			if (temp != null) {
				if (temp.equals(soundName) && game.getPackJukebox().isPlaying(temp)) {
					lock = true;
					return;
				}
			}

			// executa se o audio for diferente da cena anterior
			if	(game.getPackJukebox().isPlaying(temp)) {
				game.getPackJukebox().close(temp); //todo : <<<<<<<<<<<<<<
				System.out.println("audio fechado: " + temp);
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

	public abstract void render(Graphics g);

	public void setNextPack(int nextPack) {
		this.nextPack = nextPack;
	}

	public int getNextPack() {
		return nextPack;
	}

	public int getNextScene() {
		return nextScene;
	}

	public void createSound(String path, int soundStyle) {
		String key = path.substring(path.lastIndexOf("/") + 1, path.indexOf("."));
		game.getPackJukebox().load(path, key);
		soundName = key;
		this.soundStyle = soundStyle;
	}

	public void reset() {
		lock = false;
	}

}