package de.diavololoop.simulation;

import de.diavololoop.util.Vec;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chloroplast on 06.04.2017.
 */
public class GameObject {

    ArrayList<GameObjectElement> elements = new ArrayList<GameObjectElement>();


    public GameObject(){

    }



    public void checkForBounds(double x0, double x1, double y0, double y1, double dt){
        elements.parallelStream().forEach(obj -> obj.checkForBounds(x0, x1, y0, y1));
    }

    public void simulate(double dt){
        elements.parallelStream().forEach(obj -> obj.simulate(new Vec(0, -9.81), dt));
    }

    public void flipBuffer(){
        elements.parallelStream().forEach(obj -> obj.flipBuffer());
    }

    public void linkAll(){
        elements.parallelStream().forEach(obj -> obj.addConnection(elements, GameObjectElement.TYPE_SOLID));
    }

    public void addDisplayable(List<GameObjectElement.Drawing> positions){
        elements.stream().forEach(obj -> obj.addPosition(positions));
    }

    public void addRect(int cols, int rows, double x, double y, double width, double height, Color color, double mass){

        for(int i = 0; i < cols; ++i){
            for(int j = 0; j < rows; ++j){
                //double px = x + (Math.random()/2+i)*width/(cols-2);
                //double py = y + (Math.random()/2+j)*height/(rows-2);


                double px = x + i*width/(cols);
                double py = y + j*height/(rows);

                elements.add(new GameObjectElement(px, py, mass, color));
            }
        }
    }

}
