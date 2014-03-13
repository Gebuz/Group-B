package gui;

import java.io.IOException;

public class X
{

    public static void main(String[] args) throws IOException {
        MapPanel panel = new MapPanel();
        MapView map = new MapView("Super duper map", panel);
        Controller controller = new Controller(map);
    }
}