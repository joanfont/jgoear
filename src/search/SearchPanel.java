package search;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import javax.swing.*;
import javazoom.jl.decoder.JavaLayerException;
import jgoear.JGoear;
import lib.Goear;
import lib.Play;
import lib.Song;

public class SearchPanel extends JPanel implements Observer {

    private SearchTableModel tableModel;
    private JTable table;
    private JButton downloadButton, searchButton, playButton, stopButton;
    private final static String SEARCH_QUERY = "Search Query...";
    private JTextField searchBox;
    private Play play;
    private Thread player;

    public SearchPanel() {
        initComponents();
        this.play = null;
        this.player = null;
    }

    private void initComponents() {
        setSize(640, 480);
        tableModel = new SearchTableModel();
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel downloadsPanel = new JPanel();
        downloadsPanel.setLayout(new BorderLayout());
        downloadsPanel.add(new JScrollPane(table),
                BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel();


        downloadButton = new JButton("Download");
        downloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionDownload();
            }
        });



        buttonsPanel.add(downloadButton);


        searchBox = new JTextField(30);
        searchBox.setText(SEARCH_QUERY);
        searchBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionSearch();
            }
        });

        searchBox.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent fe) {
                gainedSearch();
            }

            @Override
            public void focusLost(FocusEvent fe) {
                lostSearch();
            }
        });

        buttonsPanel.add(searchBox);




        searchButton = new JButton("Search");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionSearch();
            }
        });
        buttonsPanel.add(searchButton);



        playButton = new JButton("Play");
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                actionPlay();
            }
        });
        
        buttonsPanel.add(playButton);

     
        stopButton = new JButton("Stop");
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                actionStop();
            }
        });
        
        buttonsPanel.add(stopButton);
        
        

        setLayout(new BorderLayout());
        add(downloadsPanel, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);

    }

    private void actionSearch() {
        if (tableModel.getRowCount() > 0) {
            clearList();
        }
        if (!searchBox.getText().equals("") && !searchBox.getText().equals(SEARCH_QUERY)) {
            Goear s = new Goear(searchBox.getText(), tableModel);
            Thread t = new Thread(s);
            t.start();


        }
    }

    private void clearList() {
        tableModel.clearSongs();
    }

    private void actionDownload() {
        Song s = (Song) tableModel.getSong(table.getSelectedRow());
        JGoear.getDownloadPanel().addDownload(s, s.getSongURL());
    }

    private void gainedSearch() {
        if (searchBox.getText().equals(SEARCH_QUERY)) {
            searchBox.setText("");
        }
    }

    private void lostSearch() {
        if (searchBox.getText().equals("")) {
            searchBox.setText(SEARCH_QUERY);
        }
    }
    
    private void actionPlay(){
        
        if(this.play == null){
            try {
                Song s = (Song) tableModel.getSong(table.getSelectedRow());
                play = new Play(new URL(s.getSongURL()));
                this.player = new Thread(play);
                this.player.start();
            } catch (IOException ex) {
            } catch (JavaLayerException ex) {
            }
        }else{
           play.stop();
           this.player = null;
            try {
                Song s = (Song) tableModel.getSong(table.getSelectedRow());
                play = new Play(new URL(s.getSongURL()));
                this.player = new Thread(play);
                this.player.start();
            } catch (IOException ex) {
            } catch (JavaLayerException ex) {
            }
        }
        
    }

    
    private void actionStop(){
       
        play.stop();
        this.player = null;
        
    }

    @Override
    public void update(Observable o, Object o1) {
    }
}
