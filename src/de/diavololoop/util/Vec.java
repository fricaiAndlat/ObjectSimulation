package de.diavololoop.util;

/**
 * Created by Chloroplast on 06.04.2017.
 */
public class Vec {

    public double x;
    public double y;

    public Vec(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vec(){
        this(0, 0);
    }

    public void set(double x, double y){
        this.x = x;
        this.y = y;
    }

    public void cloneTo(Vec other){
        other.x = this.x;
        other.y = this.y;
    }

    public void add(Vec other){
        this.x += other.x;
        this.y += other.y;
    }

    public void remove(Vec other){
        this.x -= other.x;
        this.y -= other.y;
    }

    public double length(){
        return Math.sqrt(x*x + y*y);
    }

    public double lengthTo(Vec other){
        return Math.sqrt(Math.pow(other.x - x , 2) + Math.pow(other.y - y, 2));
    }

    public void scaleTo(double length){
        double factor = length/length();
        x *= factor;
        y *= factor;
    }
    public void multiply(double scalar){
        x *= scalar;
        y *= scalar;
    }

    public String toString(){
        return "Vec ("+x+", "+y+")";
    }

}
