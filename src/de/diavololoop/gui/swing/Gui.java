package de.diavololoop.gui.swing;

import de.diavololoop.simulation.GameObject;
import de.diavololoop.util.Vec;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Chloroplast on 06.04.2017.
 */
public class Gui extends JPanel {

    public static void main(String[] args) throws InterruptedException {
        GameObject generic = new GameObject();
        Gui gui = new Gui();

        generic.addRect(10, 10, -200, -200, 400, 400);
        generic.linkAll();

        ArrayList<Vec> positions = new ArrayList<Vec>();
        generic.addDisplayable(positions);

        long time = System.nanoTime();

        Thread.sleep(15);

        while(true){
            long current = System.nanoTime();
            long dt = current - time; //nanoseconds
            time = current;

            generic.simulate(dt*1e-9);
            generic.checkForBounds(gui.x0, gui.x1, gui.y0, gui.y1);
            generic.flipBuffer();

            gui.draw(positions);
            gui.repaint();

            Thread.sleep(15);
        }

    }


    private BufferedImage buffer;
    private Graphics2D bufferG;

    private double screenX, screenY;
    private double x0, x1, y0, y1; //onWorld
    private double zoom = 1; // screen -> world


    public Gui(){
        JFrame frame = new JFrame();
        frame.setSize(800, 800);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setLayout(new BorderLayout());
        frame.add(this);

        addComponentListener(new ComponentListener() {

            public void componentResized(ComponentEvent e) {
                x0 = screenX - zoom*getWidth()/2;
                x1 = screenX + zoom*getWidth()/2;
                y0 = screenY - zoom*getHeight()/2;
                y1 = screenY + zoom*getHeight()/2;
            }

            public void componentMoved(ComponentEvent e) {}
            public void componentShown(ComponentEvent e) {}
            public void componentHidden(ComponentEvent e) {}
        });

        frame.setVisible(true);
    }

    public void paintComponent(Graphics g){
        if(buffer != null){
            g.drawImage(buffer, 0, 0, this);
        }
    }

    public void draw(ArrayList<Vec> positions){
        if(buffer == null || buffer.getWidth()!=getWidth() || buffer.getHeight()!=getHeight()){
            buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
            bufferG = buffer.createGraphics();
        }

        bufferG.setColor(Color.WHITE);
        bufferG.fillRect(0, 0, getWidth(), getHeight());

        //Random rand = new Random();bufferG.setColor(new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)));
        bufferG.setColor(Color.BLACK);

        positions.stream().filter(pos -> pos.x>x0 && pos.x<x1 && pos.y>y0 && pos.y<y1)
                .forEach(pos -> {
                    bufferG.fillOval((int)((pos.x-x0) / zoom)-4, (int)((pos.y-y0) / zoom)-4, 8, 8);
                });

    }


}
