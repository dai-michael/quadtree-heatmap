// take regions and draw them into 2^depth
// color in regions
// reset colors
// zoom in zoom out
package DaiToku;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

public class HeatMap extends JFrame{

	public HeatMapGrids grid;
    public HeatMap(int totalWidth, int totalHeight, int depth) {
        Quadtree quad = new Quadtree(totalWidth, totalHeight, depth);
        grid = new HeatMapGrids(quad);

        // wrap in a scroll pane if you like automatic scrollbars
        add(new JScrollPane(grid));

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 800);
        setLocationRelativeTo(null);
        setVisible(true);
    }
	public void drawBoundaries(){

	}
	public void resetMap(){

	}
	public void colorRegions(){

	}
	public void zoomInOut(){

	}




    public static void main(String[] args) {
        // e.g. a 100×100 quadtree at depth 4
        new HeatMap(100, 100, 4);
    }
}

