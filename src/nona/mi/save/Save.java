package nona.mi.save;

import nona.mi.loader.TextLoader;
import nona.mi.main.Game;
import nona.mi.scene.Scene;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class Save {

    private String[] slots; //contem "p-s"; o &\n eh adicionado no loop
    private String[] traces; //contem o caminho do jogador de cada save (total de 10 cenas anteriores) para preencher HistoryScene

    private File folderPath;
    private File savePath;
    private String os;

    private Tracer tracer;



    public Save(int totalSlots) {

        tracer = new Tracer();

        slots = new String[totalSlots];
        traces = new String[totalSlots];

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
            saveFileStr = folderStr + "\\save.9";
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
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(savePath))) {
            String basicSave = "0-0;";
            String basicTrace = createStringTrace();
            //0-0;0,0,0,0,0,0,0,0,0,0&
            for (int i = 0; i < slots.length; i++) {
                bw.write(basicSave + basicTrace + "&\n");
            }
            bw.flush();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private String createStringTrace() {
        StringBuilder ini = new StringBuilder("" + Scene.NO_SCENE);
        for (int i = 1; i < Tracer.TOTAL_TRACES; i++) {
            ini.append(",").append(Scene.NO_SCENE);
        }
        return ini.toString();
    }

    public void initTracer(int slot) {
        System.out.println(slot);
        String[] ss = traces[slot].split(",");
        int[] temp = new int[ss.length];
        for (int i = 0; i < ss.length; i++) {
            temp[i] = Integer.parseInt(ss[i]);
        }
        tracer.setTraces(temp);
    }

    public void load() {
        String loaded = TextLoader.loadFromDisk(savePath);
        String[] splitted = loaded.split("&");
        for (int i = 0; i < slots.length; i++) {
            String[] sp = splitted[i].split(";");
            slots[i] = sp[0];
            traces[i] = sp[1];
        }
    }

    public void save(int slot, int pack, int scene) {
        slots[slot] = "" + pack + "-" + scene;
        traces[slot] = tracer.getTracesAsString();
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(savePath))) {
            for (int i = 0; i < slots.length; i++) {
                bw.write(slots[i] + ";" + traces[i] + "&\n");
            }
            bw.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void delete(int slot) {
        slots[slot] = "0-0";
        traces[slot] = createStringTrace();
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(savePath))) {
            for (int i = 0; i < slots.length; i++) {
                bw.write(slots[i] + ";" + traces[i] + "&\n");
            }
            bw.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public File getFolderPath() {
        return folderPath;
    }

    public String getOs() {
        return os;
    }

    public int getPackOfSlot(int slot) {
        return Integer.parseInt(slots[slot].split("-")[0]);
    }

    public int getSceneOfSlot(int slot) {
        return Integer.parseInt(slots[slot].split("-")[1]);
    }

    public Tracer getTracer() {
        return tracer;
    }

}
