package nona.mi.scene;

import java.util.HashMap;

public class ScenePackage {

	private HashMap<Integer, Scene> map;

	public ScenePackage() {
		map = new HashMap<Integer, Scene>();
	}

	public void put(int key, Scene value) {
		map.put(key, value);
	}

	public Scene get(int key) {
		if (map.containsKey(key)) {
			return map.get(key);
		}
		return null;
	}

}