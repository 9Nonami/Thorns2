package nona.mi.button;

import nona.mi.main.Game;

import java.awt.Graphics;

public class VolumeGroup {

	private VolumeButton[] volumes;
	private Game game;
	private float[] values;

	public VolumeGroup(Game game, VolumeButton[] volumes) {
		this.game = game;
		this.volumes = volumes;
		values = new float[volumes.length];
	}

	public void update(int mx, int my, boolean dragged) {
		for (int i = 0; i < volumes.length; i++) {
			if (volumes[i].isOnArea(mx, my) && dragged) {
				volumes[i].update(mx, my);
				return;
			}
		}
		//soh vai chegar aqui se nao estiver movendo nenhum botao
		//esse reset garante que o player precise soltar e clicar
		//novamente, caso tenha saido da area de interacao.
		game.setDragged(false);
	}

	public void render(Graphics g) {
		for (int i = 0; i < volumes.length; i++) {
			volumes[i].render(g);
		}
	}

	public float getValueOf(int id) {
		return volumes[id].getValue();
	}

}