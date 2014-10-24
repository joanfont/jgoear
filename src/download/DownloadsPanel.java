package download;

import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import lib.Download;
import lib.Song;

public class DownloadsPanel extends JPanel implements Observer {

    private DownloadsTableModel tableModel;
    private JTable table;
    private JButton cancelButton;
    private Download selectedDownload;
    private boolean clearing;

    public DownloadsPanel() {
        initComponents();
    }

    public DownloadsTableModel getTableModel() {
        return this.tableModel;
    }

    private void initComponents() {
        setSize(640, 480);

        tableModel = new DownloadsTableModel();
        table = new JTable(tableModel);
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                tableSelectionChanged();
            }
        });
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        ProgressRenderer renderer = new ProgressRenderer(0, 100);
        renderer.setStringPainted(true);
        table.setDefaultRenderer(JProgressBar.class, renderer);

        table.setRowHeight(
                (int) renderer.getPreferredSize().getHeight());

        JPanel downloadsPanel = new JPanel();
        downloadsPanel.setLayout(new BorderLayout());
        downloadsPanel.add(new JScrollPane(table),
                BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel();


        cancelButton = new JButton("Cancel Selected");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionCancel();
            }
        });
        cancelButton.setEnabled(false);
        buttonsPanel.add(cancelButton);

        setLayout(new BorderLayout());
        add(downloadsPanel, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);
    }

    public void actionDownloadSingle() {
        selectedDownload.download();
    }

    public void actionDownloadAll() {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            Download d = tableModel.getDownload(i);
            d.download();
        }
    }

    public void addDownload(Song song, String url) {
        URL verifiedUrl = verifyUrl(url);
        if (verifiedUrl != null) {
            Download d = new Download(song, verifiedUrl);
            tableModel.addDownload(d);
            d.download();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Invalid Download URL", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private URL verifyUrl(String url) {
        if (!url.toLowerCase().startsWith("http://")) {
            return null;
        }

        URL verifiedUrl;
        try {
            verifiedUrl = new URL(url);
        } catch (Exception e) {
            return null;
        }

        if (verifiedUrl.getFile().length() < 2) {
            return null;
        }

        return verifiedUrl;
    }

    private void tableSelectionChanged() {
        if (selectedDownload != null) {
            selectedDownload.deleteObserver(DownloadsPanel.this);
        }

        if (!clearing) {
            selectedDownload =
                    tableModel.getDownload(table.getSelectedRow());
            selectedDownload.addObserver(DownloadsPanel.this);
            updateButtons();
        }
    }

    private void actionCancel() {
        selectedDownload.cancel();
        updateButtons();
    }

    private void updateButtons() {
        if (selectedDownload != null) {
            int status = selectedDownload.getStatus();
            switch (status) {
                case Download.DOWNLOADING:
                    cancelButton.setEnabled(true);
                    break;
                case Download.ERROR:
                    cancelButton.setEnabled(false);
                    break;
                default:
                    cancelButton.setEnabled(false);
            }
        } else {
            cancelButton.setEnabled(false);

        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (selectedDownload != null && selectedDownload.equals(o)) {
            updateButtons();
        }
    }
}