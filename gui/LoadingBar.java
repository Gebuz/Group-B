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
    public JPanel barPanel;
    
    public LoadingBar() {
        super("Super Exxo Loader v1.0");
        
        loadPanel = new JPanel();
        barPanel = new JPanel();
        
        loadText = new JLabel("Now loading data...");
        loadText.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        loadPanel.setLayout(new BoxLayout(loadPanel, BoxLayout.Y_AXIS));
        barPanel.setLayout(new FlowLayout());
        
        bar = new JProgressBar(0, 100);
        bar.setValue(0);
        bar.setStringPainted(true); 
        
        barPanel.add(bar);
        
        loadPanel.add(loadText);
        loadPanel.add(barPanel);
        
        getContentPane().add(loadPanel, BorderLayout.CENTER);
        
    }
    
    public void showLoadingBar() {
        setMinimumSize(new Dimension(250,100));
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
//    public void changeText() {
//        if(bar.getPercentComplete() <= 0.1) {
//            loadText.setText("Readying buffer2000...");
//        }
//        else if(bar.getPercentComplete() <= 0.2 ) {
//            loadText.setText("Brushing dust off data...");
//        }
//        else if(bar.getPercentComplete () <= 0.5) {
//            loadText.setText("Calling Krak...");
//        }
//        else if(bar.getPercentComplete() <= 0.7) {
//            loadText.setText("Giving up...");
//        }
//        else if(bar.getPercentComplete() <= 0.8) {
//            loadText.setText("Contemplating suicide...");
//        }
//        else if(bar.getPercentComplete() <= 0.9) {
//            loadText.setText("Manning up...");
//        }
//        else if(bar.getPercentComplete() <= 0.95) {
//            loadText.setText("FATAL ERROR TERMINATING...");
//        }
//        else if(bar.getPercentComplete() <= 1.0) {
//            loadText.setText("Just kidding...");
//        }
//    }
}
