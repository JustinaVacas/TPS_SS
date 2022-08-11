import java.util.ArrayList;
import java.util.List;

public class Particle {

    private int id;
    private double radio;
    private double prop;
    private double x;
    private double y;

    public Particle(int id, double radio, double prop) {
        this.id = id;
        this.radio = radio;
        this.prop = prop;
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

    public double getProp() {
        return prop;
    }

    public void setProp(double prop) {
        this.prop = prop;
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

    @Override
    public String toString() {
        return "Particle{" +
                "id=" + id +
                ", radio=" + radio +
                ", prop=" + prop +
                ", x=" + x +
                ", y=" + y +
                ", neighbours=" + neighbours +
                '}';
    }
}
