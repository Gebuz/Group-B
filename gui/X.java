package gui;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class X {

    public static void main(String[] args) throws IOException {
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        
//        int bool = 0;
//        
//        MapPanel map = new MapPanel(bool);
//        MapView view = new MapView("Super duper map", map);
//        Controller controller = new Controller(view);
//        view.setVisible(true);
        
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                StartFrame sf = new StartFrame();
            }
        });
    }
}