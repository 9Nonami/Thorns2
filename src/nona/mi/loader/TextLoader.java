package nona.mi.loader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;

public class TextLoader {

	public static String load(String path) {

	    StringBuilder stringao = new StringBuilder();
        String read = "";

        try (BufferedReader br = new BufferedReader(new InputStreamReader(TextLoader.class.getResourceAsStream(path), "UTF-8"))) {

            while (((read = br.readLine()) != null)) {
                stringao.append(read);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("reading text error");
            System.exit(0);
        }

        return stringao.toString();
    }

    public static String loadFromDisk(File file) {

        StringBuilder stringao = new StringBuilder();
        String read = "";

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            while (((read = br.readLine()) != null)) {
                stringao.append(read);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("reading text error");
            System.exit(0);
        }

        return stringao.toString();
    }



}
