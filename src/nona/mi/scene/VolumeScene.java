package nona.mi.scene;

import nona.mi.button.VolumeButton;
import nona.mi.button.VolumeGroup;
import nona.mi.main.Game;

import java.awt.Graphics;

public class VolumeScene extends Scene {

    private VolumeGroup volumeGroup;
    public static final int STANDARD_JUKE = 0;
    public static final int PACK_JUKE = 1;



    public VolumeScene(Game game, int nextScene, int sceneId) {
        super(game, nextScene, sceneId);
        create();
    }

    public VolumeScene(Game game, int sceneId) {
        super(game, sceneId);
        create();
    }

    private void create() {
        int vWidth = 10;
        int vHeight = 30;

        int gWidth = 300;
        int gHeight = 10;

        VolumeButton volume1 = new VolumeButton(STANDARD_JUKE);
        volume1.definePointer(20, 90, vWidth, vHeight); //must be the 1st one
        volume1.defineGauge(20, 100, gWidth, gHeight); //must be the 2nd one
        volume1.defineArea(10, 90, 330, 30);
        volume1.defineRange(-80.0f, 6.0f);
        volume1.defineRenderValueXY(200, 20);
        volume1.defineInitialValue(0.0f);

        VolumeButton volume2 = new VolumeButton(PACK_JUKE);
        volume2.definePointer(20, 140, vWidth, vHeight);
        volume2.defineGauge(20, 150, gWidth, gHeight);
        volume2.defineArea(10, 140, 330, 30);
        volume2.defineRange(-80.0f, 6.0f);
        volume2.defineRenderValueXY(200, 40);
        volume2.defineInitialValue(0.0f);

        volumeGroup = new VolumeGroup(game, new VolumeButton[]{volume1, volume2});
    }

    @Override
    public void updateScene() {
        int mx = game.getMouseX();
        int my = game.getMouseY();
        boolean dragged = game.isDragged();
        volumeGroup.update(mx, my, dragged);
        if (volumeGroup.getSelectedId() != VolumeGroup.NO_VOLUME) {
            if (volumeGroup.getSelectedId() == STANDARD_JUKE) {
                System.out.println(volumeGroup.getValueOf(STANDARD_JUKE));
            } else if (volumeGroup.getSelectedId() == PACK_JUKE) {
                System.out.println(volumeGroup.getValueOf(PACK_JUKE));
                game.getPackJukebox().setVolume(game.getCurrentSound(), volumeGroup.getValueOf(PACK_JUKE));
            }
        }
    }

    @Override
    public void renderScene(Graphics g) {
        volumeGroup.render(g);
    }

    @Override
    public void reset() {
        super.reset();
    }

}
