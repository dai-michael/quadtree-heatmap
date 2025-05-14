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
import java.awt.Point;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

public class HeatMap extends JFrame{

	public HeatMapGrids grid;
    public HeatMap(int totalWidth, int totalHeight, int depth) {
        Quadtree quad = new Quadtree(totalWidth, totalHeight, depth);
        quad.insert(new RidePt(10,10));
        quad.insert(new RidePt(11,10));

        grid = new HeatMapGrids(quad);

        // wrap in a scroll pane if you like automatic scrollbars
        add(new JScrollPane(grid));

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 800);
        setLocationRelativeTo(null);
        setVisible(true);
    }

	public void resetMap(){

	}

    /**
     * Colors the grid regions based on their size
     * Smaller regions will have darker colors, larger regions lighter colors
     */
    public void colorGrids() {
        if (grid != null) {
            grid.colorRegionsBySize();
            grid.repaint(); // calls paintComponent in HeatMapGrids (Jcomponent)
        }
    }
    
	public void zoomInOut(){

	}




    public static void main(String[] args) {
        // e.g. a 100×100 quadtree at depth 4
        new HeatMap(100, 100, 1);

    }
}

