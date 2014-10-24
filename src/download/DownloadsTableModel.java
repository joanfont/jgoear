package download;


import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import lib.Download;
import lib.Utils;

public class DownloadsTableModel extends AbstractTableModel implements Observer {

    private static final String[] columnNames = {"Song", "Size",
    "Progress", "Status"};
    private static final Class[] columnClasses = {String.class, String.class, JProgressBar.class, String.class};
    private ArrayList downloadList = new ArrayList();
    
    public void addDownload(Download download) {
        download.addObserver(this);
        downloadList.add(download);
        fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
    }
    
    public Download getDownload(int row) {
        return (Download) downloadList.get(row);
    }
    
    public void clearDownload(int row) {
        downloadList.remove(row);
        fireTableRowsDeleted(row, row);
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
        return downloadList.size();
    }
    
    @Override
    public Object getValueAt(int row, int col) {
        
        Download download = (Download) downloadList.get(row);
        switch (col) {
            case 0:
                return download.getSong();
            case 1:
                int size = download.getSize();
                return (size == -1) ? "" : Utils.humanReadableByteCount(size, true);
            case 2: 
                return new Float(download.getProgress());
            case 3:
                return Download.STATUSES[download.getStatus()];
        }
        return "";
    }

    @Override
    public void update(Observable o, Object arg) {
        int index = downloadList.indexOf(o);
        fireTableRowsUpdated(index, index);
    }
}