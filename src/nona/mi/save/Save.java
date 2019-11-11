package nona.mi.save;

import nona.mi.loader.TextLoader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class Save {

    private String[] slots;
    private final int NUM_SLOTS = 10;
    private File folderPath;
    private File savePath;

    public Save(){

        slots = new String[NUM_SLOTS];

        String home = System.getProperty("user.home");
        String folderStr = home + "\\.thorns";
        folderPath = new File(folderStr);
        String saveFileStr = folderStr + "\\save.9";
        savePath = new File(saveFileStr);

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
