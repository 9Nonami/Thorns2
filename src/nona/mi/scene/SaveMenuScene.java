package nona.mi.scene;

import nona.mi.button.Button;
import nona.mi.button.RectButton;
import nona.mi.loader.ImageLoader;
import nona.mi.main.Game;
import nona.mi.save.Save;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;

public class SaveMenuScene extends Scene{

    private Button[] buttons;
    private int startButtonID;
    private int buttonsToShow;
    private int startIncrement;
    private int showIncrement;

    private int saveScene;
    private int savePack;
    private BufferedImage screenshot;

    private BufferedImage standardButtonImage;
    private BufferedImage focusedButtonImage;

    private Save save;



    public SaveMenuScene(Game game, Save save) {
        super(game);
        this.save = save;
        startButtonID = 0;
        buttonsToShow = 6;
        startIncrement = 0;
        showIncrement = 1;
        standardButtonImage = ImageLoader.loadImage("/res/buttons/empty-slot.png");
        focusedButtonImage = ImageLoader.loadImage("/res/buttons/focused-slot.png");
    }

    @Override
    public void update() {
        super.update();
        for (int i = startButtonID + startIncrement; i < buttonsToShow * showIncrement; i++) {
            buttons[i].update();
            if (buttons[i].isClicked()) {
                game.setSceneBasis(game.getPackBasis().get(saveScene));
                game.setClicked(false);
                break;
            }
        }
    }

    @Override
    public void renderScene(Graphics g) {
        for (int i = startButtonID + startIncrement; i < buttonsToShow * showIncrement; i++) {
            buttons[i].render(g);
        }
    }

    public void createButtons(int totalButtons, int row, int column, int x, int y, int spacing) {

        this.buttons = new Button[totalButtons];
        for (int i = 0; i < totalButtons; i++) {
            buttons[i] = new RectButton(game);
            buttons[i].setId(i);
            buttons[i].setAudioName("click");
        }
        HashMap<Integer, BufferedImage> screenshots = loadScreenshots(save.getFolderPath().getAbsolutePath());

        int buttonWidth = standardButtonImage.getWidth();
        int buttonHeight = standardButtonImage.getHeight();

        //original values - for resetting
        int xx = x;
        int yy = y;

        int loop = totalButtons / (row * column);

        for (int h = 0; h < loop; h++) {
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < column; j++) {
                    int id = (i * column) + j + ((row * column) * h); //esta formula faz o id ir de 1 em 1. ex: 0, 1, 2...
                    if (screenshots.containsKey(buttons[id].getId())) {
                        buttons[id].setImages(screenshots.get(buttons[id].getId()), focusedButtonImage, xx, yy);
                    } else {
                        buttons[id].setImages(standardButtonImage, focusedButtonImage, xx, yy);
                    }
                    xx += spacing + buttonWidth;
                }
                xx = x;
                yy += spacing + buttonHeight;
            }
            xx = x;
            yy = y;
        }

    }

    private HashMap<Integer, BufferedImage> loadScreenshots(String path) {

        //contem as imagens lidas do disco
        HashMap<Integer, BufferedImage> screenshots = new HashMap<Integer, BufferedImage>();

        //caminho temporario para os screenshots
        File[] imagesPath = finder(path);

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

    public void setInfo(int savePack, int saveScene, BufferedImage screenshot) {
        this.savePack = savePack;
        this.saveScene = saveScene;
        this.screenshot = screenshot;
    }

    private File[] finder(String path){
        //CREDITS: https://stackoverflow.com/questions/1384947/java-find-txt-files-in-specified-folder

        File dir = new File(path);

        return dir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String filename) {
                return filename.endsWith(".png");
            }
        });

    }

    @Override
    public void reset() {
        super.reset();
        for(Button temp : buttons) {
            temp.reset();
        }
    }

}
