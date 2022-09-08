package methods;

public class Particle {

    private int id;
    private double radio;
    private double v;
    private double x;
    private double y;
    private double m;
    private double angle;

    public Particle(int id, double radio, double v, double m, double angle) {
        this.id = id;
        this.radio = radio;
        this.v = v;
        this.m = m;
        this.angle = angle;
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

    public double getM() {
        return m;
    }

    public void setM(double m) {
        this.m = m;
    }


    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    @Override
    public String toString() {
        return "Particle{" +
                "id= " + id +
                ", radio= " + radio +
                ", v= " + v +
                ", m= " + m +
                ", x= " + x +
                ", y= " + y +
                ", angle = " + angle +
                '}';
    }
}
