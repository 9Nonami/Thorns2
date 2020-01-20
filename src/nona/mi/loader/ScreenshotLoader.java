package nona.mi.loader;

import nona.mi.save.Save;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;

public class ScreenshotLoader {

    public static HashMap<Integer, BufferedImage> loadScreenshots(Save save) {

        //contem as imagens lidas do disco
        HashMap<Integer, BufferedImage> screenshots = new HashMap<Integer, BufferedImage>();

        //caminho temporario para os screenshots
        File[] imagesPath = finder(save.getFolderPath().getAbsolutePath());

        //adicionando as imagens ao hashmap de acordo com o nome do arquivo da imagem
        //para dar certo, o nome precisa ser um inteiro
        if (imagesPath != null) {
            for (int i = 0; i < imagesPath.length; i++) {

                String tempPath = imagesPath[i].getAbsolutePath();
                System.out.println(tempPath);
                String tempID = "";
                int screenshotID = 0;

                if (save.getOs().startsWith("l")) {
                    tempID = tempPath.substring(tempPath.lastIndexOf("/") + 1, tempPath.indexOf("."));
                } else if (save.getOs().startsWith("w")) {
                    tempID = tempPath.substring(tempPath.lastIndexOf("\\") + 1, tempPath.indexOf("."));
                }
                screenshotID = Integer.parseInt(tempID);
                System.out.println(screenshotID);

                screenshots.put(screenshotID, ImageLoader.loadFromDisk(tempPath));
            }
        }

        return screenshots;
    }

    private static File[] finder(String path){
        //CREDITS: https://stackoverflow.com/questions/1384947/java-find-txt-files-in-specified-folder

        File dir = new File(path);

        return dir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String filename) {
                return filename.endsWith(".png");
            }
        });

    }

}
