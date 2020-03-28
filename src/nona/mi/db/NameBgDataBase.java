package nona.mi.db;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.HashMap;

public class NameBgDataBase {

    private HashMap<char[], BufferedImage> map;

    public NameBgDataBase() {
        map = new HashMap<char[], BufferedImage>();
    }

    public void put(char[] key, BufferedImage value) {
        map.put(key, value);
    }

    public BufferedImage get(char[] nameBg) {
        if (map.containsKey(nameBg)) {
            return map.get(nameBg);
        }
        System.out.println(NameDataBase.class.getName());
        System.out.println(Arrays.toString(nameBg) + " not found!");
        System.exit(0);
        return null;
    }

}
