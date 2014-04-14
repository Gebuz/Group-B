package gui;


import javax.swing.*;
import java.awt.*;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
/**
 *
 * @author flemmingxu
 */
public class LoadingBar extends JFrame {
    public JProgressBar bar;
    public JLabel loadText;
    public JPanel loadPanel;
    
    public LoadingBar() {
        loadText = new JLabel("Now loading data...");
        loadPanel = new JPanel();
        
        loadText.setAlignmentX(Component.CENTER_ALIGNMENT);
        loadPanel.setLayout(new BoxLayout(loadPanel, BoxLayout.Y_AXIS));
        
        bar = new JProgressBar(0, 100);
        bar.setValue(0);
        bar.setStringPainted(true);    
        
        loadPanel.add(loadText);
        loadPanel.add(bar);
        
        getContentPane().add(loadPanel, BorderLayout.CENTER);
        
    }
    
    public void showLoadingBar() {
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
