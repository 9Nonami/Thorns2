package nona.mi.button;

import nona.mi.main.Game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;

public class PolygonalButton extends Button {

    private Polygon polygon;
    private final int MOUSE_MASK = 6;


    public PolygonalButton(Game game, int nextScene, int[] x, int[] y) {
        super(game, nextScene);
        polygon = new Polygon(x, y, x.length);
    }

    @Override
    public void render(Graphics g) {
        if (showMask) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.BLUE);
            g2d.drawPolygon(polygon);
        }
        g.drawImage(standardImage, imageX, imageY, null);
        if (focused) {
            g.drawImage(focusedImage, imageX, imageY, null);
        }
    }

    @Override
    protected boolean isMouseOnButton() {
        int mx = game.getMouseX();
        int my = game.getMouseY();
        return polygon.intersects((mx - (MOUSE_MASK / 2)), (my - (MOUSE_MASK / 2)), MOUSE_MASK, MOUSE_MASK);
    }

}
