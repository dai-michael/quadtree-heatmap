package DaiToku;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JComponent;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Dimension;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;


/**
 * Single-class HeatMap that builds a quadtree, inserts points,
 * colors each region by ride-count, and supports zoom via mouse wheel.
 */
public class HeatMap extends JFrame {

    private final MapPanel panel;

    public HeatMap(int totalWidth, int totalHeight, int depth) {
        super("HeatMap");
        panel = new MapPanel(totalWidth, totalHeight, depth);
        JScrollPane scroll = new JScrollPane(panel);
        getContentPane().add(scroll);
        // jframe methods:
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 800);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void resetMap() {
        panel.resetMap();
    }

    /**
     * Insert a new RidePt and refresh colors
     */
    public void addRide(RidePt pt) {
        panel.addRide(pt);
        panel.colorGrids();
        panel.repaint();
    }

    public static void main(String[] args) {
        // example: 100×100 grid, depth 4
        HeatMap frame = new HeatMap(100, 100, 4);

        // test
        frame.addRide(new RidePt(10, 10));
        frame.addRide(new RidePt(11, 10));
    }

    /**
     * Inner panel that does all the painting, quadtree logic, and zooming
     */
    private class MapPanel extends JComponent {
        private static final int CELL_SIZE = 10;
        private final Quadtree quadtree;
        private final Map<Region, Color> regionColors = new HashMap<>();
        private double zoom = 1.0; // used later

        public MapPanel(int width, int height, int depth) {
            this.quadtree = new Quadtree(width, height, depth);
            // initial color mapping
            colorGrids();
        }

        /** Clears all points and colors */
        public void resetMap() {
            //quadtree.clear(); //clear() not implemented yet
            //regionColors.clear();
            repaint();
        }

        /** Insert a point and refresh color mapping */
        public void addRide(RidePt pt) {
            quadtree.insert(pt);
            colorGrids();
            repaint();
        }

        /** Recompute colors based on ride counts */
        private void colorGrids() {
            Region root = quadtree.getRoot();
            List<Region> leaves = new ArrayList<>();
            collectLeaves(root, leaves);

            int maxCount = 0;
            for (Region leaf : leaves) {
                maxCount = Math.max(maxCount, quadtree.countRides(leaf));
            }

            //regionColors.clear();
            for (Region leaf : leaves) {
                int count = quadtree.countRides(leaf);
                double intensity = 0.0;
                if (maxCount > 0) {
                    intensity = (double) count / maxCount;
                }
                int blue = (int) (intensity * 255);
                regionColors.put(leaf, new Color(0, 0, blue));
            }
        }

        /** Recursively collect all leaf regions */
        private void collectLeaves(Region r, List<Region> leaves) {
            if (!r.isDivided()) {
                leaves.add(r);
            } else {
                for (Region child : r.subregionList) {
                    collectLeaves(child, leaves);
                }
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;

            g2.scale(zoom, zoom);
            for (Map.Entry<Region, Color> e : regionColors.entrySet()) {
                Region r = e.getKey();
                g2.setColor(e.getValue());
                int x = r.X1 * CELL_SIZE;
                int y = r.Y1 * CELL_SIZE;
                int w = (r.X2 - r.X1) * CELL_SIZE;
                int h = (r.Y2 - r.Y1) * CELL_SIZE;
                g2.fillRect(x, y, w, h);
            }

            g2.setColor(Color.BLACK);
            for (Region leaf : regionColors.keySet()) {
                int x = (int) (leaf.X1 * CELL_SIZE * zoom);
                int y = (int) (leaf.Y1 * CELL_SIZE * zoom);
                int w = (int) ((leaf.X2 - leaf.X1) * CELL_SIZE * zoom);
                int h = (int) ((leaf.Y2 - leaf.Y1) * CELL_SIZE * zoom);
                g2.drawRect(x, y, w, h);
            }
        }
    }
}
