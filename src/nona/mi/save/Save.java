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

    public static final int SLOT_ID = 0;
    public static final int IMAGE_SLOT_ID = 2;


    public Save(){

        slots = new String[NUM_SLOTS];

        String os = System.getProperty("os.name").toLowerCase();
        String home = System.getProperty("user.home");

        String folderStr;
        String saveFileStr;

        if (os.startsWith("l")){
            folderStr = home + "/thorns";
            folderPath = new File(folderStr);
            saveFileStr = folderStr + "/save.9";
            savePath = new File(saveFileStr);
        } else if (os.startsWith("w")){
            folderStr = home + "\\thorns";
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
                createBasicSaveFile(savePath);
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }

        load();

    }

    private void createBasicSaveFile(File savePath){
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

    public void save(){
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(savePath))){
            for (int i = 0; i < slots.length; i++) {
                bw.write(slots[i] + "&\n");
            }
            bw.flush();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public String[] getSlots() {
        return slots;
    }
}
