package nona.mi.spritesheet;

import nona.mi.loader.ImageLoader;

import java.awt.image.BufferedImage;

public class SpriteSheet {

    public static BufferedImage[] getSprites(String spriteSheetPath, int y, int spriteWidth, int spriteHeight, int sprites) {
        BufferedImage sheet = ImageLoader.loadImage(spriteSheetPath);
        BufferedImage[] out = new BufferedImage[sprites];
        for (int i = 0; i < sprites; i++) {
            out[i] = sheet.getSubimage( i * spriteWidth, y, spriteWidth, spriteHeight);
        }
        return out;
    }

}
