package methods;

import java.util.ArrayList;
import java.util.List;

public class Particle {

    private int id;
    private double radio;
    private double v;
    private double angle;
    private double x;
    private double y;
    //TODO cambiar a una lista de particulas
    private List<Integer> neighbours;

    public Particle(int id, double radio, double v, double angle) {
        this.id = id;
        this.radio = radio;
        this.v = v;
        this.angle = angle;
        this.neighbours = new ArrayList<>();
    }

    public List<Integer> getNeighbours() {
        return neighbours;
    }

    public void setNeighbours(List<Integer> neighbours) {
        this.neighbours = neighbours;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getRadio() {
        return radio;
    }

    public void setRadio(double radio) {
        this.radio = radio;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getV() {
        return v;
    }

    public void setV(double v) {
        this.v = v;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    @Override
    public String toString() {
        return "methods.Particle{" +
                "id= " + id +
                ", radio= " + radio +
                ", v= " + v +
                ", angle= " + angle +
                ", x= " + x +
                ", y= " + y +
                ", neighbours= " + neighbours +
                '}';
    }
}
