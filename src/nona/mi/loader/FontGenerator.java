/*
Classe responsável por unir o texto e sprite em um map
Utiliza ImageLoader e TextLoader
É preciso que haja concordância entre os caracteres do txt
com os caracteres desenhados na imagem. Ex:
	.txt com 4 caracteres[a, b, c, d]
	sprite precisa ter os mesmos 4 para a classe conseguir
	relacioná-los
Retorna um HashMap com todas as fontes que um diálogo
possa querer utilizar
*/

package nona.mi.loader;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.awt.Color;
import java.util.HashMap;

public class FontGenerator {

	public static HashMap<Character, BufferedImage> generateFont(BufferedImage image, String keysPath) {

		String allCharacters = TextLoader.load(keysPath);
	
		ArrayList<Integer> listWidth = new ArrayList<Integer>();		
		ArrayList<Integer> listX = new ArrayList<Integer>();

		int x = 0;
		int y = 0;
		int cont = 0;

		boolean running = true;
		boolean lock = false;
		
		String hex = null;
		
		Color tempColor = null;

		while (running) {
			
			tempColor = new Color(image.getRGB(x, y));
			hex = String.format("%02x%02x%02x", tempColor.getRed(), tempColor.getGreen(), tempColor.getBlue());

			if (hex.equals("00ff00") && !lock) { //ini green
				lock = true;
				cont = 0;
				listX.add((x + 1));
				x++;
				continue;
			} else if (hex.equals("ff0000")) { //end red
				lock = false;
				listWidth.add(cont);
			} else if (hex.equals("0000ff")) { //endLoop blue
				running = false;
			}

			if (lock) {
				cont++;
			}

			x++;
		}

		HashMap<Character, BufferedImage> map = new HashMap<Character, BufferedImage>();

		for (int id = 0; id < listX.size(); id++) {
			map.put(allCharacters.charAt(id), image.getSubimage(listX.get(id), 0, listWidth.get(id), image.getHeight()));
		}

		return map;
	}

}
