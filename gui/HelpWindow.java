package gui;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author flemmingxu
 */
public class HelpWindow extends JFrame {
    
    private JTabbedPane tabPane;
    private JPanel shortcuts, faq, about;
    private JButton oneButton;
    private JLabel one, two, three;
    
    public HelpWindow() {
        super("Help");
        
        tabPane = new JTabbedPane();
        
        shortcuts = new JPanel();
        faq = new JPanel();
        about = new JPanel();
            
        one = new JLabel("<html><h2>\n" +
"           &nbsp; Shortcuts</h2>\n" +
"<p>&nbsp; &nbsp; Alt+M: Enable/Disable relative mouse zoom.</p>\n" +
"<p>&nbsp; &nbsp; Alt+N: Show/Hide navigation panel.</p>\n" +
"<p>&nbsp; &nbsp; Alt+R: Show/hide road names on map.</p>\n" +
"<p>&nbsp; &nbsp; Shift+Right mouse click on map: A list of navigation options pop up.</p></html>");
        two = new JLabel("Hello");
        three = new JLabel("This is something about us!");
            
        oneButton = new JButton("Button");
        
        shortcuts.setLayout((new GridBagLayout()));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        
        one.setVerticalAlignment(JLabel.TOP);
        //three.setHorizontalAlignment(JLabel.CENTER);
        
        shortcuts.add(one, gbc);
        //shortcuts.add(oneButton);
        shortcuts.add(two, gbc);
//        shortcuts.add(three);
        //faq.add(two);
        
        tabPane.addTab("Shortcuts", null, one, "DUUGUHURRRH");
        tabPane.addTab("FAQ", null, two, "Stop being such a FAQ");
        tabPane.addTab("About", null, three, "Who are we? Your fathers.");
        
        getContentPane().add(tabPane, BorderLayout.CENTER);
        setMinimumSize(new Dimension(300, 250));
        pack();
        setVisible(false);
    }
}
