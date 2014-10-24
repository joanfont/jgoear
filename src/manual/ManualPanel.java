/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package manual;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import jgoear.JGoear;
import lib.Goear;
import lib.Song;

/**
 *
 * @author joanfont
 */
public class ManualPanel extends JPanel {

    private JLabel songUrlLabel;
    private JTextField songURLField;
    private JLabel exampleLabel;
    private JButton downloadButton;


    public ManualPanel() {
        initComponents();
    }
    

    private void initComponents() {
        
        JPanel songURLPanel = new JPanel();
        songUrlLabel = new JLabel("Song URL: ");
        songURLPanel.add(songUrlLabel);
        
        songURLField = new JTextField(30);
        songURLPanel.add(songURLField);
        downloadButton = new JButton("Download");
        downloadButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                actionDownload();
            }
            
        });


        songURLPanel.add(downloadButton);
       

        JPanel examplePane = new JPanel();
        exampleLabel = new JLabel("Example: http://www.goear.com/listen/55dc7a3/something-the-beatles");
        examplePane.add(exampleLabel);

        setLayout(new BorderLayout());
        add(songURLPanel, BorderLayout.NORTH);
        add(examplePane,BorderLayout.CENTER);

    }
    
    public void actionDownload(){
        Song s = Goear.getSongObjectFromURL(songURLField.getText());
        JGoear.getDownloadPanel().addDownload(s, s.getSongURL());
    }
}
