package nona.mi.cont;

public class Cont {

    private float value;



    public Cont() {
        reset();
    }

    public void update(int speedAdjust) {
        value += speedAdjust;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public void reset() {
        value = 0;
    }


}
