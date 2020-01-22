package nona.mi.save;

import nona.mi.loader.TextLoader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class Save {

    private String[] slots; //contem "p-s"; o &\n eh adicionado no loop
    private File folderPath;
    private File savePath;
    private String os;

    //x-x
    //[0]pack [1]- [2]scene

    public static final int PACK_ID = 0;
    public static final int SCENE_ID = 2;



    public Save(int totalSlots) {

        slots = new String[totalSlots];

        os = System.getProperty("os.name").toLowerCase();
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
            saveFileStr = folderStr + "\\save.9"; //todo : dava pra deixar em 1 linha
            savePath = new File(saveFileStr);
        } else {
            System.out.println("I guess you're not using Linux or Windows. The save file for this system was not implemented yet. Please see the Save.class to adjust it.");
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
                bw.write("0-0&\n");
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

    public void save(int slot, int pack, int scene) {
        slots[slot] = "" + pack + "-" + scene;
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

    public File getFolderPath() {
        return folderPath;
    }

    public String getOs() {
        return os;
    }
}
