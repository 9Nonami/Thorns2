/*
Classe responsável por armazenar o map que os diálogos usam
para renderizar o texto
*/

package nona.mi.db;

import java.util.HashMap;
import java.awt.image.BufferedImage;

import nona.mi.loader.FontGenerator;
import nona.mi.loader.ImageLoader;

public class FontDataBase{

	private HashMap<Character, BufferedImage> map;
	private int fontHeight;

	public FontDataBase(String imagePath, String textPath) {
		BufferedImage image = ImageLoader.loadImage(imagePath);
		fontHeight = image.getHeight();
		map = FontGenerator.generateFont(image, textPath);
	}

	public int getFontHeight() {
		return fontHeight;
	}

	public BufferedImage get(char key) {
		if (map.containsKey(key)) {
			return map.get(key);
		} else {
			System.out.println("Can not find character" + key);
			System.out.println(getClass().getName());
			System.exit(0);
		}
		
		return null;
	}

}
