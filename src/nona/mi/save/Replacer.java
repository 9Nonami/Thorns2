package nona.mi.save;

public class Replacer {

    public static String replace(String original, int id, char replace){
        char[] temp = original.toCharArray();
        temp[id] = replace;
        return new String(temp);
    }

}
