package de.diavololoop.simulation;

import de.diavololoop.util.Vec;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chloroplast on 06.04.2017.
 */
public class GameObjectElement {

    public final static int TYPE_FREE = 0;

    private Vec position;
    private Vec speed;

    private Vec bufferPosition;
    private Vec bufferSpeed;

    private double mass = 10;
    private double softness = 1e-6;

    private Vec tempForce = new Vec(); //temporary used in simulate()
    private Vec tempForceObject = new Vec(); //temporary used in simulate()

    ArrayList<Connection> connections = new ArrayList<Connection>();

    public GameObjectElement(double x, double y){
        this.position = new Vec(x, y);
        this.bufferPosition = new Vec(x, y);
        this.speed = new Vec();
        this.bufferSpeed = new Vec();
    }

    public void addConnection(List<GameObjectElement> others, int type){
        others.stream()
                .filter(obj -> (obj!=this)  )
                .map(obj ->  new Connection(obj, obj.position.lengthTo(position), type))
                .forEach(con -> connections.add(con));
    }

    public void flipBuffer(){
        bufferPosition.cloneTo(position);
        bufferSpeed.cloneTo(speed);
    }

    public void simulate(Vec force, double dt){
        tempForce.set(0, 0);

        connections.stream().forEach(con -> {
            con.other.position.cloneTo(tempForceObject);
            tempForceObject.remove(position);
            double length = tempForceObject.length();
            switch(con.type){
                case TYPE_FREE:
                    tempForceObject.multiply(-length*softness/* (*length/length) */);
                    break;
            }


            tempForce.add(tempForceObject);

        });



        tempForce.multiply(dt/mass);


        bufferSpeed.add(tempForce);

        bufferSpeed.cloneTo(tempForce);

        tempForce.multiply(dt);

        bufferPosition.add(tempForce);
    }

    public void checkForBounds(double x0, double x1, double y0, double y1) {
        if(bufferPosition.x < x0){
            bufferPosition.x = 2*x0 - bufferPosition.x;
            bufferSpeed.x = -bufferSpeed.x*0.99;
        }else if(bufferPosition.x > x1){
            bufferPosition.x = 2*x1 - bufferPosition.x;
            bufferSpeed.x = -bufferSpeed.x*0.99;
        }

        if(bufferPosition.y < y0){
            bufferPosition.y = 2*y0 - bufferPosition.y;
            bufferSpeed.y = -bufferSpeed.y*0.99;
        }else if(bufferPosition.y > y1){
            bufferPosition.y = 2*y1 - bufferPosition.y;
            bufferSpeed.y = -bufferSpeed.y*0.99;
        }
    }


    class Connection {

        final GameObjectElement other;
        final double length;
        final int type;

        public Connection(GameObjectElement other, double length, int type){
            this.other = other;
            this.length = length;
            this.type = type;
        }



    }

    public Vec getPosition(){
        return position;
    }
}
