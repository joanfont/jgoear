
package about;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author joanfont
 */
public class AboutPanel extends JPanel {
    
    private JTextArea jta;
    
    public AboutPanel(){
        initComponents();
    }
    
    private void initComponents(){
        jta = new JTextArea();
        jta.setEditable(false);
        jta.setColumns(20);
        jta.setLineWrap(true);
        jta.setRows(5);
        jta.setText("jGoear 2.0\nJoan Font - http://www.joan-font.com/");
        JScrollPane jp = new JScrollPane();
        jp.setViewportView(jta);
        
        setLayout(new BorderLayout());
        add(jp,BorderLayout.CENTER);
    }
    
    
}
