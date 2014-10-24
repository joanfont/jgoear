package search;

import java.util.*;
import javax.swing.table.*;
import lib.Song;

public class SearchTableModel extends AbstractTableModel implements Observer {

    private static final String[] columnNames = {"Song", "Artist","Length","Quality"};
    private static final Class[] columnClasses = {String.class, String.class,String.class,String.class};
    private ArrayList songList = new ArrayList();

    public void addSong(Song song) {
        song.addObserver(this);
        songList.add(song);
        fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
    }

    public Song getSong(int row) {
        return (Song) songList.get(row);
    }

    public void clearSong(int row) {
        songList.remove(row);
        fireTableRowsDeleted(row, row);
    }

    public void clearSongs() {
        while (this.getRowCount() > 0) {
            this.clearSong(0);
        }
        songList = new ArrayList();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    @Override
    public Class getColumnClass(int col) {
        return columnClasses[col];
    }

    @Override
    public int getRowCount() {
        return songList.size();
    }

    @Override
    public void update(Observable o, Object arg) {
        int index = songList.indexOf(o);
        fireTableRowsUpdated(index, index);
    }

    @Override
    public Object getValueAt(int row, int col) {

        Song song = (Song) songList.get(row);
        switch (col) {
            case 0:
                return song.getSongName();
            case 1:
                return song.getSongArtist();
            case 2: 
                return song.getSongLength();
            case 3: 
                return song.getSongBritate();

        }
        return "";
    }
}