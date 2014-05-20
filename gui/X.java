package gui;

import java.io.IOException;

public class X {

    public static void main(String[] args) throws IOException {
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                StartFrame sf = new StartFrame();
            }
        });
    }
}