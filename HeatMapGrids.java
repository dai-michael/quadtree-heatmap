package DaiToku;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JComponent;

public class HeatMapGrids extends JComponent {
    private static final int CELL_SIZE = 10;  // pixels per quadtree unit
    private Quadtree quadtree;

    public HeatMapGrids(Quadtree quad) {
        this.quadtree = quad;
        // … set up mouse listeners for pan/zoom, etc.
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        // apply your AffineTransform for zoom/pan here
        // e.g. g2.setTransform(currentTransform);
        
        // draw all boundaries
        g2.setColor(Color.BLACK);
        drawBoundaries(g2, quadtree.getRoot());
    }

    /**
     * Recursively walks the quadtree and draws each region's outline.
     */
    public void drawBoundaries(Graphics2D g2, Region region) {
        // map quadtree coordinates → pixels
        int x      = region.X1 * CELL_SIZE;
        int y      = region.Y1 * CELL_SIZE;
        int width  = (region.X2 - region.X1) * CELL_SIZE;
        int height = (region.Y2 - region.Y1) * CELL_SIZE;

        // 2) stroke the rectangle
        g2.draw(new Rectangle2D.Double(x, y, width, height));

        // 3) recurse if subdivided
        if (region.isDivided()) {
            for (Region child : region.subregionList) {
                drawBoundaries(g2, child);
            }
        }
    }
}