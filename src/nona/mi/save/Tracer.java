package nona.mi.save;

import nona.mi.scene.Scene;

import java.util.Arrays;

public class Tracer {

    private int[] traces;
    public static final int TOTAL_TRACES = 10;

    public Tracer() {
        traces = new int[TOTAL_TRACES];
        setEmptyTraces();
    }

    public void setEmptyTraces() {
        Arrays.fill(traces, Scene.NO_SCENE);
    }

    //stan usa esse metodo
    public void add(int trace) {
        for (int i = 0; i < (traces.length - 1); i++) {
            traces[i] = traces[i + 1];
        }
        traces[traces.length - 1] = trace;
    }

    public String getTracesAsString() {
        StringBuilder ini = new StringBuilder("" + traces[0]);
        for (int i = 1; i < traces.length; i++) {
            ini.append(",").append(traces[i]);
        }
        return ini.toString();
    }

    public int[] getTraces() {
        return traces;
    }

    public void setTraces(int[] traces) {
        this.traces = traces;
    }

}
