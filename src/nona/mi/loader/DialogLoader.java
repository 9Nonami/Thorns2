package nona.mi.loader;

import java.util.HashMap;

public class DialogLoader {
    
    /*
    example:
    1=some text bla bla bla bla%
    2=asd asd asd asd sdasdsads%
    3=huse
    */
    
    public static HashMap<Integer, String> load(String  stringao) {
        HashMap<Integer, String> map = new HashMap<Integer, String>();
        String[] lines = stringao.split("%");
        for (int i = 0; i < lines.length; i++) {
            String[] sentence = lines[i].split("=");
            map.put(Integer.parseInt(sentence[0]), sentence[1]);
        }
        return map;
    }
    
}