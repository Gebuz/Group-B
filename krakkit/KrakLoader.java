package krakkit;

import gui.LoadingBar;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Parse Krak data files (kdv_node_unload.txt, kdv_unload.txt).
 *
 * Customize to your needs by overriding processNode and processEdge. See
 * example in main.
 *
 * Original author Peter Tiedemann petert@itu.dk; updates (2014) by Søren
 * Debois, debois@itu.dk
 */
public abstract class KrakLoader
{

    public abstract void processNode(KrakNodeData nd);

    public abstract void processEdge(KrakEdgeData ed);

    /**
     * Load krak-data from given files, invoking processNode and processEdge
     * once for each node- and edge- specification in the input file,
     * respectively.
     *
     * @param nodeFile
     * @param edgeFile
     * @return
     * @throws IOException if there is a problem reading data or the files dont
     * exist
     */
    public void load(String nodeFile, String edgeFile) throws IOException
    {
        LoadingBar loadBar = new LoadingBar();
        loadBar.showLoadingBar();
        int lineCount = 0;
        int progress = 0;
        
        /* Nodes. */
        BufferedReader br;
        br = new BufferedReader(new FileReader(nodeFile));
        br.readLine(); // First line is column names, not data.

        String line;
        while ((line = br.readLine()) != null) {
            if(lineCount == 14882) {
                progress++;
                loadBar.bar.setValue(progress);
                //loadBar.changeText();
                lineCount = 0;
            }
            else {
                lineCount++;
            }
            processNode(new KrakNodeData(line));
        }
        br.close();

        /* Edges. */

        br = new BufferedReader(new InputStreamReader(new FileInputStream(edgeFile), "LATIN1"));
        
        br.readLine(); // Again, first line is column names, not data.

        while ((line = br.readLine()) != null) {
            if(lineCount == 14882) {
                progress++;
                loadBar.bar.setValue(progress);
                //loadBar.changeText();
                lineCount = 0;
            }
            else {
                lineCount++;
            }
            processEdge(new KrakEdgeData(line));
        }
        br.close();
        loadBar.bar.setValue(100);
        loadBar.bar.setString("Load complete!");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(KrakLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        loadBar.setVisible(false);

        DataLine.resetInterner();
        System.gc();
    }
}
