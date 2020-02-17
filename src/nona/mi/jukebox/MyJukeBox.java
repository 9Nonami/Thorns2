package nona.mi.jukebox;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class MyJukeBox {

    private HashMap<String, Clip> clips;
    private int gap;

    public static final int LOOP = 0;
    public static final int ONCE = 1;

    public MyJukeBox() {
        init();
    }

    // Creates new clips HashMap.
    public void init() {
        clips = new HashMap<String, Clip>();
        gap = 0;
    }

    public HashMap<String, Clip> getClips() {
        return clips;
    }

    // Loads up audio located at path "s" and stores
    // it in the HashMap with key "n".
    public void load(String s, String n) {
        if(clips.get(n) != null) return;
        Clip clip;
        try {
            InputStream in = MyJukeBox.class.getResourceAsStream(s);
            InputStream bin = new BufferedInputStream(in);
            AudioInputStream ais =
                    AudioSystem.getAudioInputStream(bin);
            AudioFormat baseFormat = ais.getFormat();
            AudioFormat decodeFormat = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED,
                    baseFormat.getSampleRate(),
                    16,
                    baseFormat.getChannels(),
                    baseFormat.getChannels() * 2,
                    baseFormat.getSampleRate(),
                    false
            );
            AudioInputStream dais = AudioSystem.getAudioInputStream(decodeFormat, ais);
            clip = AudioSystem.getClip();
            clip.open(dais);
            clips.put(n, clip);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void play(String s) {
        play(s, gap);
    }

    public void play(String s, int i) {
        Clip c = clips.get(s);
        if(c == null) return;
        if(c.isRunning()) c.stop();
        c.setFramePosition(i);
        while(!c.isRunning()) c.start();
    }

    public void stop(String s) {
        if(clips.get(s) == null) return;
        if(clips.get(s).isRunning()) clips.get(s).stop();
    }

    public void resume(String s) {
        if(clips.get(s).isRunning()) return;
        clips.get(s).start();
    }

    public void resumeLoop(String s) {
        Clip c = clips.get(s);
        if(c == null) return;
        c.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void loop(String s) {
        loop(s, gap, gap, clips.get(s).getFrameLength() - 1);
    }

    public void loop(String s, int frame) {
        loop(s, frame, gap, clips.get(s).getFrameLength() - 1);
    }

    public void loop(String s, int start, int end) {
        loop(s, gap, start, end);
    }

    public void loop(String s, int frame, int start, int end) {
        Clip c = clips.get(s);
        if(c == null) return;
        if(c.isRunning()) c.stop();
        c.setLoopPoints(start, end);
        c.setFramePosition(frame);
        c.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void setPosition(String s, int frame) {
        clips.get(s).setFramePosition(frame);
    }

    public int getFrames(String s) { return clips.get(s).getFrameLength(); }

    public int getPosition(String s) { return clips.get(s).getFramePosition(); }

    public void close(String s) {
        stop(s);
        clips.get(s).close();
    }

    public void setVolume(String s, float f) {
        Clip c = clips.get(s);
        if(c == null) return;
        FloatControl vol = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
        vol.setValue(f);
    }

    public boolean isPlaying(String s) {
        if  (s == null){
            return  false;
        }
        Clip c = clips.get(s);
        if(c == null) return false;
        return c.isRunning();
    }

    public void closeAllClips() {
        for (Map.Entry<String, Clip> temp : clips.entrySet()) {
            System.out.println("closing: " + temp.getKey());
            if (temp.getValue().isRunning()) {
                temp.getValue().stop();
            }
            temp.getValue().close();
        }
    }

}