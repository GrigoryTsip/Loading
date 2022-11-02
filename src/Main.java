import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Main {

    public static GameProgress game;
    public static String path = ("C://Games/savegames/");

    public static String arxName = "Games";

    public static void main(String[] args) {

        Map<String, GameProgress> gameProp = new HashMap();

        try (ZipInputStream zin = new ZipInputStream(new
                FileInputStream(path + arxName + ".zip"))) {

            ZipEntry entry;
            String name;

            while ((entry = zin.getNextEntry()) != null) {
                name = openZip(zin, entry);
                if (name != null) {
                    game = openProgress(path + name);
                    gameProp.put(name, game);
                } else {
                    // System.out.println("Что-то пошло не так...");
                    zin.closeEntry();
                    zin.close();
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        int arrSize = gameProp.size();

        String[] nm = new String[arrSize];
        gameProp.keySet().toArray(nm);

        GameProgress[] pr = new GameProgress[arrSize];
        gameProp.values().toArray(pr);

        for (int i = 0; i < arrSize; i++) {
            System.out.print("Состояние игры " + nm[arrSize - (i + 1)] + ": ");
            System.out.println(pr[arrSize - (i + 1)].toString());
        }
    }

    public static String openZip(ZipInputStream zin, ZipEntry entry) throws IOException {

        String name = entry.getName();

        try {
            FileOutputStream fout = new FileOutputStream(path + name);
            for (int c = zin.read(); c != -1; c = zin.read()) fout.write(c);

            fout.flush();
            fout.close();

            return name;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static GameProgress openProgress(String path) {

        try (FileInputStream fis = new FileInputStream(path);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (GameProgress) ois.readObject();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
