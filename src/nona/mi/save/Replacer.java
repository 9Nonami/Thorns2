package nona.mi.save;

public class Replacer {

    public static String replace(String original, int id, char replace){
        char[] temp = original.toCharArray();
        temp[id] = replace;
        return new String(temp);
    }

    public static String replace(String original, int[] ids, char[] replaces){
        if (ids.length != replaces.length){
            System.out.println("ids and chars must have the same length!");
            System.exit(0);
        }
        char[] temp = original.toCharArray();
        for (int i = 0; i < ids.length; i++) {
            temp[ids[i]] = replaces[i];
        }
        return new String(temp);
    }

}
