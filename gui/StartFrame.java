
package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private int bool;

    
    class Task extends SwingWorker<Void, Void> {
        
        @Override
        protected Void doInBackground() {  
            map = new MapPanel(bool);
            view = new MapView("Super duper map", map);
            controller = new Controller(view);
            return null;
        }
            
        @Override
        public void done() {
            if(bool == 0) {
                krakButton.setEnabled(true);
            }
            else {
                osmButton.setEnabled(true);
            }
            setCursor(null);
            view.setVisible(true);
        }
    }
    
    
    public StartFrame() {
        
        //Initializing components, containers and setting layouts
        textLabel = new JLabel("Choose map of Denmark:");
        krakButton = new JButton("Krak (2001)");
        osmButton = new JButton("Open Street Map (2014)");
        krakButton.addActionListener(this);
        osmButton.addActionListener(this);
        
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
        if(e.getSource() == krakButton) {
            if(task.isDone()) {
                view.setVisible(true);
            }
            else {
                krakButton.setEnabled(false);
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                osmButton.setEnabled(false);
                bool = 0;
                task.execute();
            }
        }
        else if(e.getSource() == osmButton) {
            if(task.isDone()) {
                view.setVisible(true);
            }
            else {
                osmButton.setEnabled(false);
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                krakButton.setEnabled(false);
                bool = 1;
                task.execute();
            }
        }
    }
}


        
