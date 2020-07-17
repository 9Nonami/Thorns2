package nona.mi.scene;

import java.awt.Graphics;

import nona.mi.jukebox.MyJukeBox;
import nona.mi.main.Game;

// Com relacao ao audio, Scene so toca musicas de fundo.
// Caso algum audio esteja relacionado a fala do personagem
// ou caso seja algum efeito, fazer em outra classe.

public abstract class Scene {

	protected Game game;
	protected int sceneId;
	protected int packId;
	protected int nextPack; //a maioria das cenas vai ter isso indefinido
	protected int nextScene;
	protected String soundName; //background sound
	protected int soundStyle; //loop ou once

	public static final int LAST_SCENE = -99; //definir como last_scene significa que depois desta cena, outro pack eh carregado. NAO ESQUECER DE DEFINIR O PACK!
	public static final int LOAD_SCENE = -80;
	public static final int FADE_SCENE_LOGO = -79;
	public static final int DMS_SCENE = -78;
	public static final int MAIN_MENU_SCENE = -77;
	public static final int HISTORY_SCENE = -43;

	public static final int NO_SCENE = -2;
	public static final int NO_PACK = -1;

	protected boolean lockAudio; //faz com que o codigo do updateAudio so execute uma vez
	protected boolean lockNullAudio;


	//ESSENCIAL------------------------------------------------------

	public Scene(Game game, int nextScene, int sceneId) {
		this.game = game;
		this.nextScene = nextScene;
		this.sceneId = sceneId;
	}

	public Scene(Game game, int sceneId) {
		this.game = game;
		this.sceneId = sceneId;
	}

	//----------------------------------------------------------------


	//UR--------------------------------------------------------------

	public void update() {
		updateAudio();
		updateScene();
	}

	private void updateAudio() {
		if (soundName != null) {
			if (!lockAudio) {
				String temp = game.getCurrentSound();
				System.out.println("audio atual: " + temp);
				// verifica se esta tocando o mesmo audio que a cena anterior
				if (temp != null) {
					if (temp.equals(soundName) && game.getPackJukebox().isPlaying(temp)) {
						System.out.println("RETORNANDO! mesmo audio!" + "\n");
						lockAudio = true;
						return;
					}
				}
				// executa se o audio for diferente da cena anterior
				if (game.getPackJukebox().isPlaying(temp)) {
					game.getPackJukebox().stop(temp);
					System.out.println("audio PARADO: " + temp);
				}
				game.setCurrentSound(this.soundName);
				System.out.println("novo audio: " + soundName + "\n");
				if (soundStyle == MyJukeBox.ONCE) {
					game.getPackJukebox().play(this.soundName);
				} else if (soundStyle == MyJukeBox.LOOP) {
					game.getPackJukebox().loop(this.soundName);
				}
				lockAudio = true;
			}
		}
	}

	public abstract void updateScene();


	public void render(Graphics g) {
		renderScene(g);
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

	public abstract int getSceneType();

	public void reset() {
		lockAudio = false;
		lockNullAudio = false;
	}

}