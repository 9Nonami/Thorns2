package nona.mi.scene;

import java.util.HashMap;

public class ScenePackage {

	private HashMap<Integer, Scene> map;

	public ScenePackage() {
		map = new HashMap<Integer, Scene>();
	}

	public void put(Scene scene) {
		map.put(scene.getSceneId(), scene);
	}

	public Scene get(int key) {
		if (map.containsKey(key)) {
			return map.get(key);
		}
		return null;
	}

	public HashMap<Integer, Scene> getMap() {
		return map;
	}
}