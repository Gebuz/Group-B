package gui;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author flemmingxu
 */
public class HelpWindow extends JFrame {
    
    private JTabbedPane tabPane;
    private JLabel one, two;
    
    /**
     * Initializes all components of this window.
     */
    public HelpWindow() {
        super("Help");
        
        tabPane = new JTabbedPane();
            
        one = new JLabel("<html><h2>\n" +
"           &nbsp; Shortcuts</h2>\n" +
"<p>&nbsp; &nbsp; Alt+C: Clear map (removes route and pins).</p>\n" +
"<p>&nbsp; &nbsp; Alt+H: Show help window.</p>\n" +
"<p>&nbsp; &nbsp; Alt+M: Enable/Disable relative mouse zoom.</p>\n" +
"<p>&nbsp; &nbsp; Alt+N: Show/Hide navigation panel.</p>\n" +
"<p>&nbsp; &nbsp; Alt+R: Show/hide road names on map.</p>\n" +
"<p>&nbsp; &nbsp; Shift+Right mouse click on map: A list of navigation options pop up.</p></html>");
        
        two = new JLabel("<html><h2>\n" +
"           &nbsp; FAQ</h2>\n" +
"<p>1. I put two pins trying to find a route between them, but nothing happens.</p>\n" +
"<ul>\n" +
"<li>Patience is the keyword here. The program can take some time finding a route, especially if the route you&#39;re trying to find is pretty long.</li>\n" +
"</ul>\n" +
"<p>2. The OSM map took so long to load compared to Krak. Is it just my computer?</p>\n" +
"<ul>\n" +
"<li>It&#39;s not your computer. The OSM just has much more content, about three times as much, so it takes quite a bit longer to load. Thank you for your patience, if you&#39;re reading this from the OSM map!</li>\n" +
"</ul>\n" +
"<p>3. I just zoomed in to my home and it appears the whole neighbourhood has been flooded!</p>\n" +
"<ul>\n" +
"<li>Don&#39;t worry, consider it as a feature. You should actually feel very lucky if that happens to your home!</li>\n" +
"</ul>\n" +
"<p>4. How come you have a map that&#39;s over a decade old. Nobody&#39;s ever gonna use that.</p>\n" +
"<ul>\n" +
"<li>We strive to appeal to most audiences, so that means hipsters, too.</li>\n" +
"</ul>\n" +
"<p>5. Is there a reason you chose PINK on GREEN??</p>\n" +
"<ul>\n" +
"<li>It really started as a joke. But we grew fond of it. The pink, that is.</li>\n" +
"</ul>\n" +
"<p>6. A dancing banana?</p>\n" +
"<ul>\n" +
"<li>Yes. A dancing banana.</li>\n" +
"</ul>\n" +
"<p>&nbsp;</p></html>");
        
        tabPane.addTab("Shortcuts", null, one, "What kind of shortcuts does this program have?");
        tabPane.addTab("FAQ", null, two, "Frequently Asked Questions!");
        
        getContentPane().add(tabPane, BorderLayout.CENTER);
        setPreferredSize(new Dimension(590, 500));
        pack();
        setVisible(false);
    }
}
