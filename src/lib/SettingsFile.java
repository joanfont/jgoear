package lib;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class SettingsFile {

    public final static String SETTINGS_FILE = ".jgoear";
    private final static String SAVE_PATH = "SAVE_PATH";
    private final static String TABLE_ITEMS = "TABLE_ITEMS";

    private final String actualSettingsFile;
    private final Properties p;

    public SettingsFile() throws IOException {
        actualSettingsFile = System.getProperty("user.home") + File.separator + SETTINGS_FILE;
        p = new Properties();
        p.load(new FileReader(actualSettingsFile));
    }

    public static void touch() {
        String actualSettingsFile = System.getProperty("user.home") + File.separator + SETTINGS_FILE;
        File f = new File(actualSettingsFile);
        if(!f.exists()){
            try {
                f.createNewFile();
            } catch (IOException e) {
            }
        }
    }

    public void setSavePath(String path) throws IOException {
        p.put(SAVE_PATH, path);
        p.store(new FileWriter(actualSettingsFile), "");
    }

    public String getSavePath() {
        return p.getProperty(SAVE_PATH);
    }

    public void setItems(String items) throws IOException {
        p.put(TABLE_ITEMS, items);
        p.store(new FileWriter(actualSettingsFile), "");
    }

    public String getItems() {
        return p.getProperty(TABLE_ITEMS);
    }
}
