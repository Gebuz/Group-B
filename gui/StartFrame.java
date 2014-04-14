
package gui;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javax.swing.JFrame.EXIT_ON_CLOSE;

/**
 *
 * @author flemmingxu
 */
public class StartFrame extends JFrame {
    
    private MapPanel panel;
    private MapView map;
    private Controller controller;
    private StartFrame sf = this;
    
    public final JButton krakButton;
    public final JButton osmButton;
    private JLabel textLabel;
    private JPanel buttonPanel;
    private JPanel krakPanel;
    private JPanel osmPanel;
    private JProgressBar bar;
    private Task task;
    private int progress;
    
    class Task extends SwingWorker<Void, Void> {
        
        @Override
        protected Void doInBackground() {            
            panel = new MapPanel();
            map = new MapView("Super duper map", panel);
            Controller controller = new Controller(map, sf);
            return null;
        }
        
        @Override
        public void done() {
            Toolkit.getDefaultToolkit().beep();
            bar.setValue(100);
            bar.setString("Loading completed!");
            krakButton.setEnabled(true);
            setCursor(null);
        }
    }
    
    
    public StartFrame() {
        
        //Initializing components, containers and setting layouts
        textLabel = new JLabel("Vælg kort af Danmark:");
        krakButton = new JButton("Krak (2001)");
        osmButton = new JButton("Open Street Map (2014)");
        krakButton.setEnabled(false);
        
        textLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        buttonPanel = new JPanel();
        krakPanel = new JPanel();
        osmPanel = new JPanel();
        
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        krakPanel.setLayout(new FlowLayout());
        osmPanel.setLayout(new FlowLayout());
        
        bar = new JProgressBar();
        bar.setStringPainted(true);
        bar.setString("Loading Krak data...");
        
        //Adding components to containers
        krakPanel.add(krakButton);
        krakPanel.add(bar);
        osmPanel.add(osmButton);
        
        buttonPanel.add(textLabel);
        buttonPanel.add(krakPanel);
        buttonPanel.add(osmPanel);
        
        
        //final touches
        getContentPane().add(buttonPanel, BorderLayout.CENTER);
        
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        
        task = new Task();
        task.execute();
        
//        Random rng = new Random();
//        progress = 0;
//        while(progress < 100) {
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException ex) {
//                Logger.getLogger(StartFrame.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            progress += rng.nextInt(20);
//            bar.setValue(progress);
//        }
    }
}


//    @Override
//    public void propertyChange(PropertyChangeEvent evt) {
//        if("progress" == evt.getPropertyName()) {
//            int progress = (Integer) evt.getNewValue();
//            bar.setValue(progress);
//        }
//    }
//}
