package DaiToku;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
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

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Single-class HeatMap that builds a quadtree, inserts points,
 * colors each region by ride-count, and supports zoom via clicking.
 */
public class HeatMap extends JFrame {

    // changed to not be final
    private MapPanel panel;

    public HeatMap(Quadtree quadtree) {
        panel = new MapPanel(quadtree);
        JScrollPane scroll = new JScrollPane(panel);
        getContentPane().add(scroll);
        // jframe methods:
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 800);
        setLocationRelativeTo(null);
        setVisible(true);

        RegionMouseListener listener = new RegionMouseListener();
        panel.addMouseListener(listener);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    /**
     * Insert a new RidePt and refresh colors
     */
    public void addRide(RidePt pt) {
        panel.addRide(pt);
        panel.colorGrids();
        panel.repaint();
    }

    /**
     * Inner panel that does all the painting, quadtree logic, and zooming
     */
    private class MapPanel extends JComponent {
        private int maxCount = 1;
        private double sizeMultiplier;
        private final Quadtree quadtree;
        private Map<Region, Color> regionColors;
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

        /** Insert a point and refresh color mapping */
        public void addRide(RidePt pt) {
            quadtree.insert(pt);
            colorGrids();
            repaint();
        }

        public void colorGrids(){ 
            regionColors = new HashMap<>();
            getMaxCount(quadtree.getRoot(), 0);
            colorGridsHelper(quadtree.getRoot(), 0);
        }

        public void colorGridsHelper(Region region, int currDepth) {

            // Base case: Max depth reached
            if (currDepth >= maxDepth) {
                int count = quadtree.countRides(region);
                if (count == 0){
                    return;
                }

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
            else if (region.isDivided()) {
                for (Region currRegion : region.subregionList) {
                    colorGridsHelper(currRegion, currDepth + 1);
                }
            }
        }

        public void getMaxCount(Region region, int currDepth) {
            // Base case: desired depth reached
            if (currDepth >= maxDepth) {
                // Collect max count
                int count = quadtree.countRides(region);
                if (count > maxCount) {
                    maxCount = count;
                }
            }

            else if (region.isDivided()) {
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

        }
    }

    /**
     * An inner class to respond to mouse events.
     */
    private class RegionMouseListener implements MouseListener {

        public RegionMouseListener() {
            System.out.println("DEBUG: Mouselistener activated");
        }

        /**
         *  Zooms in if mouse clicked
         */
        public void mousePressed(MouseEvent event) {
            int x = event.getX();
            int y = event.getY();

            // Convert pixel to quadtree point
            System.out.println(panel.sizeMultiplier);
            int quadX = (int) (x / panel.sizeMultiplier);
            int quadY = panel.quadtree.TOT_Y  -  (int) (y / panel.sizeMultiplier);
            RidePt currPoint = new RidePt(quadX, quadY);

            // If point is inside of quadtree look for its location
            if (panel.quadtree.getRoot().containsLocation(currPoint)) {
                // this requires making a function to copy the quadtree
                // i dont think i can do it
                System.out.println(panel.quadtree.findRegion(currPoint));
                panel.quadtree.root = panel.quadtree.findRegion(currPoint);
                //panel = new MapPanel(quadtree);
                System.out.println(panel.quadtree.root);
                panel.sizeMultiplier = 800.0/(panel.quadtree.root.X2 - panel.quadtree.root.X1);
                panel.colorGrids();
                panel.repaint();
                System.out.println("repainted: " + panel.quadtree.root);
                JOptionPane.showMessageDialog(panel, "test", "Clicked location information", JOptionPane.PLAIN_MESSAGE);

            }
            
            System.out.println("x: " + x + "y: " + y);
            System.out.println("x y" + quadX + " " + quadY);
        }

        public void mouseReleased(MouseEvent event) {}
        public void mouseClicked(MouseEvent event) {}
        public void mouseEntered(MouseEvent event) {}
        public void mouseExited(MouseEvent event) {}
    }

    public static void main(String[] args) {
        File csv = new File("rides.csv");
        QuadtreeAdapter test = new QuadtreeAdapter(csv, 4);
        HeatMap frame = new HeatMap(test.quadtree);
    }
}
