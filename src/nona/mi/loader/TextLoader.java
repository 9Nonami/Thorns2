package nona.mi.loader;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class TextLoader{

	public static String load(String path) {

	    String stringao = "";
        String read = "";

        try (BufferedReader br = new BufferedReader(new InputStreamReader(TextLoader.class.getResourceAsStream(path)))){

            while (((read = br.readLine()) != null)) {
                stringao += read;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("reading text error");
            System.exit(0);
        }

        return stringao;
    }

}
