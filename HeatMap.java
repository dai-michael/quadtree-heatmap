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
import java.io.File;

//image imports
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Single-class HeatMap that builds a quadtree, inserts points,
 * colors each region by ride-count, and supports zoom via clicking.
 */
public class HeatMap extends JFrame {

    private final MapPanel panel;

    public HeatMap(Quadtree quadtree) {
        panel = new MapPanel(quadtree);
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

        File csv = new File("rides.csv");
        QuadtreeAdapter test = new QuadtreeAdapter(csv, 6);
        HeatMap frame = new HeatMap(test.quadtree);
    }

    /**
     * Inner panel that does all the painting, quadtree logic, and zooming
     */
    private class MapPanel extends JComponent {
        private int maxCount = 1;
        private double sizeMultiplier;
        private final Quadtree quadtree;
        private final Map<Region, Color> regionColors = new HashMap<>();
        private int maxDepth;
        private int zoom = 1;
        private BufferedImage mapImage;

        public MapPanel(Quadtree quadtree) {
            this.quadtree = quadtree;
            // initial color mapping
            this.maxDepth = quadtree.getDefaultDepth();
            sizeMultiplier = 800.0/quadtree.TOT_X;

            try {
                mapImage = ImageIO.read(new File("image1.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }            

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

        public void colorGrids(){
            getMaxCount(quadtree.getRoot(), 0);
            colorGridsHelper(quadtree.getRoot(), 0);
        }

        public void colorGridsHelper(Region region, int currDepth) {
            // base case: thing reached
            if (currDepth >= maxDepth) {
                // Add color to the thingamabob
                int count = quadtree.countRides(region);
                if (count == 0){
                    return;
                }
                // System.out.println("CurrDepth: " + currDepth + " maxcount: " + maxCount);
                // System.out.println(region);
                // System.out.println(maxDepth);
                double intensity = (double) count / 5000;

                int trancy;
                if (intensity > 1) {
                    trancy = 255;
                }
                else {
                    trancy = (int) (intensity * 255);
                }
                regionColors.put(region, new Color(0, 0, 255, trancy));
            }
            // recursive step
            else {
                for (Region currRegion : region.subregionList) {
                    colorGridsHelper(currRegion, currDepth + 1);
                }
            }
        }

        public void getMaxCount(Region region, int currDepth) {
            // base case: desired depth reached
            if (currDepth >= maxDepth) {
                // collect max count
                int count = quadtree.countRides(region);
                if (count > maxCount) {
                    maxCount = count;
                }
            }

            // recurse
            else {
                for (Region currRegion : region.subregionList) {
                    getMaxCount(currRegion, currDepth + 1);
                }
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;

            g2.scale(zoom, zoom);
            if (mapImage != null) {
                int w = (int)(quadtree.TOT_X * sizeMultiplier);
                int h = (int)(quadtree.TOT_Y * sizeMultiplier);
                g2.drawImage(mapImage, 0, 0, w, h, null);
            }


            for (Map.Entry<Region, Color> e : regionColors.entrySet()) {
                Region r = e.getKey();
                g2.setColor(e.getValue());
                int x = (int) (r.X1 * sizeMultiplier);
                int y = (int) (r.Y1 * sizeMultiplier);
                int w = (int) ((r.X2 - r.X1) * sizeMultiplier);
                int h = (int) ((r.Y2 - r.Y1) * sizeMultiplier);
                g2.fillRect(x, y, w, h);
            }

            g2.setColor(Color.BLACK);
            for (Region leaf : regionColors.keySet()) {
                int x = (int) (leaf.X1 * sizeMultiplier * zoom);
                int y = (int) (leaf.Y1 * sizeMultiplier * zoom);
                int w = (int) ((leaf.X2 - leaf.X1) * sizeMultiplier * zoom);
                int h = (int) ((leaf.Y2 - leaf.Y1) * sizeMultiplier * zoom);
                g2.drawRect(x, y, w, h);
            }
        }



    }
}
