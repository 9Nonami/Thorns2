package nona.mi.db;

import java.util.HashMap;

public class NameDataBase {

    public static final String STANDARD_CLICK = "click";
    public static final String MARISA = "Marisa";

    private HashMap<String, char[]> map;

    public NameDataBase() {
        map = new HashMap<String, char[]>();
        map.put(MARISA, MARISA.toCharArray());
    }

    public char[] get(String name) {
        if (map.containsKey(name)) {
            return map.get(name);
        }
        System.out.println(NameDataBase.class.getName());
        System.out.println(name + " not found!");
        System.exit(0);
        return null;
    }





}
