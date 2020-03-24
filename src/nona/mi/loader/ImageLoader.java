package nona.mi.loader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;

public class ImageLoader {

	public static BufferedImage loadImage(String path) {

		BufferedImage temp = null;
		InputStream is = null;

		try {
			is = ImageLoader.class.getResourceAsStream(path); 
			temp = ImageIO.read(is);
		} catch (Exception ex){
			ex.printStackTrace();
			System.out.println("image loading error");
			System.exit(0);
		} finally {
			try {
				is.close();
			} catch (Exception ex){
				ex.printStackTrace();
				System.out.println("closing stream error");
				System.exit(0);
			}
		}

		return temp;
	}

	public static BufferedImage loadFromDisk(String path) {

		File file = new File(path);
		BufferedImage temp = null;

		try {
			temp = ImageIO.read(file);
		} catch (Exception ex){
			ex.printStackTrace();
			System.out.println("image loading error");
			System.exit(0);
		}

		return temp;
	}

}

//ImageIO API:
//read(InputStream is):
	//This method does not close the provided
	//InputStream after the read operation has completed;
	//it is the responsibility of the caller to close the stream, if desired.