package jgoear;

import about.AboutPanel;
import download.DownloadsPanel;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import lib.Utils;
import search.SearchPanel;
import lib.SettingsFile;
import manual.ManualPanel;
import settings.SettingsPanel;

public class JGoear extends JFrame {

    private static SearchPanel searchPanel;
    private static ManualPanel manualPanel;
    private static DownloadsPanel downloadPanel;
    private static SettingsPanel settingsPanel;
    private static AboutPanel aboutPanel;
    private static JTabbedPane panel;

    public JGoear() {
        initComponents();
    }

    private void initComponents() {

        setTitle("jGoear");
        setSize(840, 580);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        panel = new JTabbedPane();

        searchPanel = new SearchPanel();
        manualPanel = new ManualPanel();
        downloadPanel = new DownloadsPanel();
        settingsPanel = new SettingsPanel();
        aboutPanel = new AboutPanel();

        panel.addTab("Search", searchPanel);
        panel.addTab("Manual", manualPanel);
        panel.addTab("Download", downloadPanel);
        panel.addTab("Settings", settingsPanel);
        panel.addTab("About", aboutPanel);
        add(panel);
    }

    public static void main(String args[]) {

        if (Utils.isOSX()) {
            System.setProperty("apple.laf.useScreenMenuBar", "true");
        }
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        } catch (UnsupportedLookAndFeelException e) {
        }

        new JGoear().setVisible(true);
        SettingsFile.touch();
    }

    public static DownloadsPanel getDownloadPanel() {
        return downloadPanel;
    }

    public static SearchPanel getSearchPanel() {
        return searchPanel;
    }
}
