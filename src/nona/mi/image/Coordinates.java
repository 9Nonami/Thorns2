package nona.mi.image;

public class Coordinates {
    
    private float x;
    private float y;
    private int baseX;
    private int baseY;

    public Coordinates(float x, float y) {
        this.x = x;
        this.y = y;
        this.baseX = (int) x;
        this.baseY = (int) y;
    }

    public void reset(){
        x = baseX;
        y = baseY;
    } 

    public float getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }   
    
    public float getY() {
        return y;
    }
    
    public void setY(int y) {
        this.y = y;
    }
    
    public void addX(float value){
        this.x += value;
    }
    
    public void addY(float value){
        this.y += value;
    }

    public int getBaseX() {
        return baseX;
    }

    public int getBaseY() {
        return baseY;
    }
    
}
