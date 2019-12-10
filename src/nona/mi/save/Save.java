package nona.mi.save;

import nona.mi.loader.TextLoader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class Save {

    private String[] slots;
    public static final int NUM_SLOTS = 10;
    private File folderPath;
    private File savePath;

    //x-x-x-x
    //[0]id_do_slot [1]- [2]id_filling_state [3]- [4]pack [5]- [6]scene
    public static final int SLOT_ID = 0;
    public static final int IMAGE_SLOT_ID = 2;
    public static final int PACK_ID = 4;
    public static final int SCENE_ID = 6;

    public static final char FILLED = '1';
    public static final char EMPTY = '0';

    public Save() {

        slots = new String[NUM_SLOTS];

        String os = System.getProperty("os.name").toLowerCase();
        String home = System.getProperty("user.home");

        String folderStr;
        String saveFileStr;

        if (os.startsWith("l")) {
            folderStr = home + "/thorns";
            folderPath = new File(folderStr);
            saveFileStr = folderStr + "/save.9";
            savePath = new File(saveFileStr);
        } else if (os.startsWith("w")) {
            folderStr = home + "\\thorns";
            folderPath = new File(folderStr);
            saveFileStr = folderStr + "\\save.9";
            savePath = new File(saveFileStr);
        } else {
            System.out.println("I guess you're not using Linux or Windows. The save for this system was not implemented yet. Please see the Save.class to adjust it.");
            System.exit(0);
        }

        //CRIA A PASTA
        if (!(folderPath.exists())) {
            folderPath.mkdir();
        }

        //CRIA O ARQUIVO
        if (!(savePath.exists())) {
            try {
                savePath.createNewFile();
                createBasicSaveFile(savePath);
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }

        load();

    }

    private void createBasicSaveFile(File savePath) {
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(savePath))){
            for (int i = 0; i < slots.length; i++) {
                String temp = "" + i + "-0-0-0&" + "\n";
                bw.write(temp);
            }
            bw.flush();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void load() {
        String loaded = TextLoader.loadFromDisk(savePath);
        String[] splitted = loaded.split("&");
        for (int i = 0; i < slots.length; i++) {
            slots[i] = splitted[i];
        }
    }

    public void save() {
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(savePath))) {
            for (int i = 0; i < slots.length; i++) {
                bw.write(slots[i] + "&\n");
            }
            bw.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String[] getSlots() {
        return slots;
    }
}
