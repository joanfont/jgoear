/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package settings;

import lib.SettingsFile;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author joanfont
 */
public class SettingsPanel extends JPanel {

    private JLabel saveToLabel;
    private JTextField saveToPath;
    private JFileChooser jfc;
    private JButton openJfc;
    private SettingsFile sf;
    private static final String[] ITEMS = {"10", "25", "30", "50", "100"};
    private JLabel itemsLabel;
    private JComboBox itemsList;

    public SettingsPanel() {
        initComponents();
    }
    

    private void initComponents() {
        try {
            sf = new SettingsFile();
            File f = new File(sf.getSavePath());
            if(!f.isDirectory()){
                sf.setSavePath(System.getProperty("user.home"));
            }
            try{
                int items = Integer.parseInt(sf.getItems());
            }catch(NumberFormatException ex){
                sf.setItems("25");
            }
        } catch (IOException ex) {
            try {
                SettingsFile.touch();
                sf = new SettingsFile();
                sf.setSavePath(System.getProperty("user.home"));
                sf.setItems("25");
            } catch (Exception e) {
            }
        }
        JPanel configPane = new JPanel();

        saveToLabel = new JLabel("Save songs at: ");

        configPane.add(saveToLabel);


        saveToPath = new JTextField(30);
        String path = sf.getSavePath();
        saveToPath.setText(path);
        configPane.add(saveToPath);

        jfc = new JFileChooser();
        jfc.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory();
            }

            @Override
            public String getDescription() {
                return "Directory";
            }
        });
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        openJfc = new JButton("Navigate...");
        openJfc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                openFileChooser();
            }
        });
        configPane.add(openJfc);


        JPanel itemsPane = new JPanel();

        itemsLabel = new JLabel("Items to display on the table: ");
        itemsPane.add(itemsLabel);

        itemsList = new JComboBox();
        String currentItems = sf.getItems();
        for(String item : ITEMS){
            itemsList.addItem(item);
            if(item.equals(currentItems)){
                itemsList.setSelectedItem(item);
            }
        }
        itemsList.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                updateItems();
            }
        });

        itemsPane.add(itemsList);

        setLayout(new BorderLayout());
        add(configPane, BorderLayout.NORTH);
        add(itemsPane,BorderLayout.CENTER);

    }

    private void openFileChooser() {
        int r = jfc.showOpenDialog(this);
        if (r == JFileChooser.APPROVE_OPTION) {
            String ruta = jfc.getSelectedFile().getAbsolutePath();
            try {
                sf.setSavePath(ruta);
                saveToPath.setText(ruta);

            } catch (Exception e) {
            }
        }

    }

    private void updateItems() {
        String selected = (String) itemsList.getSelectedItem();
        try{
            sf.setItems(selected);
        }catch(Exception e){}
        
    }
}
