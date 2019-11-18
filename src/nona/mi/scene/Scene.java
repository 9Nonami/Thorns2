package nona.mi.scene;

import java.awt.Graphics;

import nona.mi.loader.MyJukeBox;
import nona.mi.main.Thorns;

public abstract class Scene{

	protected Thorns thorns;
	protected int nextPack;
	protected int nextScene;

	protected String soundName;
	protected int style;
	protected boolean lock;

	public static final int LAST_SCENE = -99;


	public Scene(Thorns thorns, int nextScene){
		this.thorns = thorns;
		this.nextScene = nextScene;
	}

	public void update(){
		if	(!lock && soundName != null){

			String temp = thorns.getCurrentSound();

			if	(thorns.getMyJukeBox().isPlaying(temp)){
				thorns.getMyJukeBox().stop(temp);
			}

			thorns.setCurrentSound(this.soundName);

			if	(style == MyJukeBox.ONCE) {
				thorns.getMyJukeBox().play(this.soundName);
			} else if (style == MyJukeBox.LOOP) {
				thorns.getMyJukeBox().loop(this.soundName);
			}

			lock = true;
		}

	}

	public abstract void render(Graphics g);

	public void setNextPack(int nextPack){
		this.nextPack = nextPack;
	}

	public int getNextPack(){
		return nextPack;
	}

	public int getNextScene() {
		return nextScene;
	}

	public void createSound(String path, int style){
		String key = path.substring(path.lastIndexOf("/") + 1, path.indexOf("."));
		thorns.getMyJukeBox().load(path, key);
		soundName = key;
		this.style = style;
	}

	public void reset(){
		lock = false;
	}

}