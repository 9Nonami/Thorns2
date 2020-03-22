package nona.mi.db;

import java.util.HashMap;

public class NameDataBase {

    private HashMap<String, char[]> map;
    private static final char[] NONAME = "NONAME".toCharArray();

    public NameDataBase() {
        map = new HashMap<String, char[]>();
        map.put("Rose", "Rose".toCharArray());
        map.put("Tulip", "Tulip".toCharArray());
        map.put("Marisa", "Marisa".toCharArray());
    }

    public char[] get(String name) {
        if (map.containsKey(name)) {
            return map.get(name);
        }
        return NONAME;
    }

}
