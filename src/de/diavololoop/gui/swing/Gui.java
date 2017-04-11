package de.diavololoop.gui.swing;

import de.diavololoop.simulation.GameObject;
import de.diavololoop.simulation.GameObjectElement;
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
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "0");

        GameObject generic = new GameObject();
        Gui gui = new Gui();

        Thread.sleep(50);

        generic.addRect(10, 10, -200, -200, 400, 400, Color.GREEN, 0.5);

        //generic.addRect(10, 10, -200, -200, 400, 400, Color.GREEN, 0.5);
        //generic.addRect(5, 5, -200, 500, 400, 200, Color.BLUE, 20);
        generic.linkAll();

        ArrayList<GameObjectElement.Drawing> positions = new ArrayList<GameObjectElement.Drawing>();
        generic.addDisplayable(positions);



        Thread.sleep(15);
        Thread t = new Thread(() -> {

            long time = System.nanoTime();

            while(gui.isVisible()){
                long current = System.nanoTime();
                long dt = current - time; //nanoseconds
                time = current;

                generic.simulate(dt*1e-9 *0.2);
                generic.checkForBounds(gui.x0, gui.x1, gui.y0, gui.y1, dt);
                generic.flipBuffer();
            }

        });
        t.start();


        while(gui.isVisible()){
            gui.draw(positions);
            gui.repaint();

            Thread.sleep(20);
        }



    }


    private BufferedImage buffer;
    private Graphics2D bufferG;

    private double screenX, screenY;
    private double x0, x1, y0, y1; //onWorld
    private double zoom = 1; // screen -> world


    public Gui(){
        JFrame frame = new JFrame();
        frame.setSize(800, 700);
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

    public void draw(ArrayList<GameObjectElement.Drawing> positions){
        if(buffer == null || buffer.getWidth()!=getWidth() || buffer.getHeight()!=getHeight()){
            buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
            bufferG = buffer.createGraphics();
        }

        bufferG.setColor(Color.WHITE);
        bufferG.fillRect(0, 0, getWidth(), getHeight());

        //Random rand = new Random();bufferG.setColor(new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)));
        bufferG.setColor(Color.BLACK);

        positions.stream()
                .filter(dr -> dr.pos.x>x0 && dr.pos.x<x1 && dr.pos.y>y0 && dr.pos.y<y1)
                .forEach(dr -> dr.draw(bufferG, x0, y0, zoom, getHeight()));

    }


}
