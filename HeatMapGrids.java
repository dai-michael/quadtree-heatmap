package DaiToku;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JComponent;

public class HeatMapGrids extends JComponent {
    private static final int CELL_SIZE = 10;  // pixels per quadtree unit
    private Quadtree quadtree;
    private boolean colorBySize = false;
    private Map<Region, Color> regionColors = new HashMap<>();
    private int renderDepth;
    private int maxRenderDepth;

    public HeatMapGrids(Quadtree quad) {
        this.quadtree = quad;
        maxRenderDepth = quadtree.getDefaultDepth();
        renderDepth = maxRenderDepth;
        // … set up mouse listeners for pan/zoom, etc.
    }

    @Override // maybe, this is good coding practice.
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        // apply your AffineTransform for zoom/pan here
        // e.g. g2.setTransform(currentTransform);
        
        if (colorBySize) {
            // Draw colored regions
            drawColoredRegions(g2, quadtree.getRoot());
        } else {
            // Draw just boundaries in black
            g2.setColor(Color.BLACK);
            drawBoundaries(g2, quadtree.getRoot());
        }
    }

    /**
     * Recursively walks the quadtree and draws each region's outline.
     */
    public void drawBoundaries(Graphics2D g2, Region region) {
        drawBoundariesHelper(g2, quadtree.getRoot(), 0, renderDepth);
    }


    public void drawBoundariesHelper(Graphics2D g2, Region region, int currDepth, int renderDepth) {
        // Base case: depth exceeds depth we want to render at
        if (currDepth > renderDepth) {
            return;
        }

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
                drawBoundariesHelper(g2, child, currDepth + 1, renderDepth);
            }
        }
    }
    
    /**
     * Draw regions with colors based on their size
     */
    public void drawColoredRegions(Graphics2D g2, Region region) {
        drawColoredRegionsHelper(g2, quadtree.getRoot(), 0, renderDepth);
    }

    public void drawColoredRegionsHelper(Graphics2D g2, Region region, int currDepth, int renderDepth) {
        // Base case: depth exceeds depth we want to render at
        if (currDepth > renderDepth) {
            return;
        }

        // map quadtree coordinates → pixels
        int x      = region.X1 * CELL_SIZE;
        int y      = region.Y1 * CELL_SIZE;
        int width  = (region.X2 - region.X1) * CELL_SIZE;
        int height = (region.Y2 - region.Y1) * CELL_SIZE;

        // Fill the rectangle with the appropriate color
        Color regionColor = regionColors.getOrDefault(region, Color.WHITE);
        g2.setColor(regionColor);
        g2.fill(new Rectangle2D.Double(x, y, width, height));
        
        // Draw the outline in black
        g2.setColor(Color.BLACK);
        g2.draw(new Rectangle2D.Double(x, y, width, height));

        // Recurse if subdivided
        if (region.isDivided()) {
            for (Region child : region.subregionList) {
                drawColoredRegionsHelper(g2, child, currDepth + 1, renderDepth);
            }
        }
    }
    
    /**
     * Assigns colors to regions based on their size
     * Smaller regions get darker colors
     */
    public void colorRegionsBySize() {
        // Clear previous colors
        regionColors.clear();
        
        // Set flag to use colored drawing
        colorBySize = true;
        
        // Compute colors for all regions
        calculateRegionColors(quadtree.getRoot());
    }
    
    /**
     * Recursively calculate colors for regions based on num of points
     */
    private void calculateRegionColors(Region region) {
        // Calculate region size
        int width = region.X2 - region.X1;
        int height = region.Y2 - region.Y1;
        int area = width * height;
        int numPoints = quadtree.countRides(region);

        // Calculate color intensity based on size
        // Smaller regions get darker colors
        // Max area is the full quadtree area
        int maxArea = quadtree.TOT_X * quadtree.TOT_Y;
        float ratio =  numPoints / quadtree.size();//(float) area / maxArea;
        
        // Create a color that ranges from dark blue to light blue based on size
        int colorValue = (int) (ratio * 255);
        Color regionColor = new Color(colorValue, colorValue, 255); // Blue shade
        
        // Store the color for this region
        regionColors.put(region, regionColor);
        
        // Recurse if region is divided
        if (region.isDivided()) {
            for (Region child : region.subregionList) {
                calculateRegionColors(child);
            }
        }
    }
    
    /**
     * Reset to show only boundaries without coloring
     */
    public void resetColors() {
        colorBySize = false;
        regionColors.clear();
        repaint();
    }
}