package nona.mi.scene;

import java.awt.Color;
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
	protected boolean lock; //faz com que o codigo do updateAudio so execute uma vez
	public static final int LAST_SCENE = -99; //definir como last_scene significa que depois desta cena, outro pack eh carregado. NAO ESQUECER DE DEFINIR O PACK!
	private ButtonGroup buttonGroup; //save, load, copy, del

	private ButtonGroup yn; //confirma a ida para o main
	private boolean esc;



	//ESSENCIAL------------------------------------------------------

	public Scene(Game game, int nextScene) {
		this.game = game;
		this.nextScene = nextScene;
		yn = game.getYn();
	}

	public Scene(Game game) {
		this.game = game;
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
			if (yn.getClickedButton() == SaveMenuScene.YES) {
				game.returntoMainMenu();
			} else if (yn.getClickedButton() == SaveMenuScene.NO) {
				esc = false;
				yn.reset();
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

	private void updateButtonGroup() { //todo : parar o audio?? > se pah nao > se for fala, sim; talvez o bg nao, mas se pah sim. > scene nao toca falas
		if (buttonGroup != null) {
			buttonGroup.update();
			if (buttonGroup.getClickedButton() != ButtonGroup.NO_CLICK) {
				if (buttonGroup.getClickedButton() == SaveMenuScene.MAIN) {
					esc = true;
				} else {
					game.getSaveMenuScene().setType(buttonGroup.getClickedButton());
					game.getSaveMenuScene().setInfo(game.getPack(), game.getScene(), game.getFrame()); //soh save precisa disso, mas nao vou criar um if soh pra ele
					game.setSceneBasisWithoutReset(game.getSaveMenuScene()); //para nao comecar a cena do 0 quando voltar
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