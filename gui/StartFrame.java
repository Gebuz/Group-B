
package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static javax.swing.JFrame.EXIT_ON_CLOSE;



/**
 *
 * @author flemmingxu
 */
public class StartFrame extends JFrame implements ActionListener {
    
    private MapPanel map;
    private MapView view;
    private Controller controller;
    public StartFrame sf = this;
    
    public final JButton krakButton;
    public final JButton osmButton;
    private JLabel textLabel;
    private JPanel buttonPanel;
    private JPanel krakPanel;
    private JPanel osmPanel;
    Task task;
    private int progress;

    
    class Task extends SwingWorker<Void, Void> {
        
        @Override
        protected Void doInBackground() {  
            map = new MapPanel();
            view = new MapView("Super duper map", map);
            Controller controller = new Controller(view);
            return null;
        }
            
        @Override
        public void done() {
//            loadBar.bar.setString("Loading completed!");
//            loadBar.bar.setValue(100);
//            loadBar.bar.setVisible(false);
            krakButton.setEnabled(true);
            setCursor(null);
            view.setVisible(true);
        }
    }
    
    
    public StartFrame() {
        
        //Initializing components, containers and setting layouts
        textLabel = new JLabel("Vælg kort af Danmark:");
        krakButton = new JButton("Krak (2001)");
        osmButton = new JButton("Open Street Map (2014)");
        krakButton.addActionListener(this);
        
        textLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        buttonPanel = new JPanel();
        krakPanel = new JPanel();
        osmPanel = new JPanel();
        
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        krakPanel.setLayout(new FlowLayout());
        osmPanel.setLayout(new FlowLayout());
        
        //Adding components to containers
        krakPanel.add(krakButton);
        osmPanel.add(osmButton);
        
        buttonPanel.add(textLabel);
        buttonPanel.add(krakPanel);
        buttonPanel.add(osmPanel);
        
        
        //final touches
        getContentPane().add(buttonPanel, BorderLayout.CENTER);
     
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        
        task = new Task();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(task.isDone()) {
            view.setVisible(true);
        }
        else {
            //bar.setString("Loading Krak data...");
            krakButton.setEnabled(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            osmButton.setEnabled(false);
            task.execute();
        }
    }
}


        
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
    
//    @Override
//    public void propertyChange(PropertyChangeEvent evt) {
//        if("progress" == evt.getPropertyName()) {
//            int progress = (Integer) evt.getNewValue();
//            bar.setValue(progress);
//        }
//    }
//}
