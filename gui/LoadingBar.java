package gui;


import javax.swing.*;
import java.awt.*;
/**
 *
 * @author flemmingxu
 */
public class LoadingBar extends JFrame {

    public final JProgressBar bar;
    public final JLabel loadText, seriousLabel;
    public final JPanel loadPanel, barPanel, textPanel, seriousPanel;
    private ImageIcon seriousImage;
    
    
    /**
     * Initializes all components of this window.
     */
    public LoadingBar() {
        super("Super Exxo Loader v2.0");
        
        seriousImage = new ImageIcon("data/needles/banana.gif");
        seriousLabel = new JLabel(seriousImage);
        
        loadPanel = new JPanel();
        barPanel = new JPanel();
        textPanel = new JPanel();
        seriousPanel = new JPanel();
        
        loadText = new JLabel("Now loading data...");
        loadText.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        loadPanel.setLayout(new BoxLayout(loadPanel, BoxLayout.Y_AXIS));
        
        bar = new JProgressBar(0, 100);
        bar.setValue(0);
        bar.setStringPainted(true); 
        
        barPanel.add(bar);
        textPanel.add(loadText);
        seriousPanel.add(seriousLabel);
        
        loadPanel.add(seriousPanel);
        loadPanel.add(textPanel);
        loadPanel.add(barPanel);
        
        getContentPane().add(loadPanel, BorderLayout.CENTER);   
    }
    
    /**
     * Shows the loading bar.
     */
    public void showLoadingBar() {
        setMinimumSize(new Dimension(250,100));
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    /**
     * Changes the text above the loading bar to the specified text.
     * @param s The specified text.
     */
    public void changeText(String s) {
        loadText.setText(s);
    }
}
