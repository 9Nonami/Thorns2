/*
Classe responsável por armazenar o map que os diálogos usam
para renderizar o texto
*/

package nona.mi.db;

import java.util.HashMap;
import java.awt.image.BufferedImage;

import nona.mi.loader.FontGenerator;

public class FontDataBase{

	private HashMap<Character, BufferedImage> map;
	private int fontHeight;

	public FontDataBase(String imagePath, String textPath){
		map = FontGenerator.generateFont(imagePath, textPath);
		fontHeight = FontGenerator.getHeight(imagePath);
	}

	public int getFontHeight(){
		return fontHeight;
	}

	public BufferedImage get(char key){
		if (map.containsKey(key)) {
			//no render isso aqui é chamado a todo tempo
			return map.get(key);
		} else {
			System.out.println("Can not find " + key);
			System.exit(0);
		}
		
		return null; //retornar um '?'
	}

}
