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

    public Save(){

        slots = new String[NUM_SLOTS];

        String os = System.getProperty("os.name").toLowerCase();

        String home;
        String folderStr;
        String saveFileStr;

        if (os.startsWith("l")){
            home = System.getProperty("user.home");
            folderStr = home + "/thorns";
            folderPath = new File(folderStr);
            saveFileStr = folderStr + "/save.9";
            savePath = new File(saveFileStr);
        } else if (os.startsWith("w")){
            home = System.getProperty("user.home");
            folderStr = home + "\\.thorns";
            folderPath = new File(folderStr);
            saveFileStr = folderStr + "\\save.9";
            savePath = new File(saveFileStr);
        } else {
            System.exit(0);
        }

        //CRIA A PASTA
        if (!(folderPath.exists())){
            folderPath.mkdir();
        }

        //CRIA O ARQUIVO
        if (!(savePath.exists())){
            try {
                savePath.createNewFile();
                createBasicSaveText(savePath);
            } catch (Exception ex){
                ex.printStackTrace();
            }
        } else {
            load();
        }

    }

    private void createBasicSaveText(File savePath){
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

    public void load(){
        String loaded = TextLoader.loadFromDisk(savePath);
        String[] splitted = loaded.split("&");
        for (int i = 0; i < slots.length; i++) {
            slots[i] = splitted[i];
        }
    }

    public String[] getSlots() {
        return slots;
    }
}