package methods;

public class Particle {

    private int id;
    private double radio;
    private double x;
    private double y;
    private double m;
    private double angle;
    private double vx;
    private double vy;

    public Particle(int id, double radio, double vx, double vy, double m, double angle) {
        this.id = id;
        this.radio = radio;
        this.m = m;
        this.angle = angle;
        this.vx = vx;
        this.vy = vy;
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

    public double getVx() {
        return vx;
    }

    public void setVx(double vx) {
        this.vx = vx;
    }

    public double getVy() {
        return vy;
    }

    public void setVy(double vy) {
        this.vy = vy;
    }

    @Override
    public String toString() {
        return "Particle{" +
                "id= " + id +
                ", radio= " + radio +
                ", vx = " + vx +
                ", vy = " + vy +
                ", m= " + m +
                ", x= " + x +
                ", y= " + y +
                ", angle = " + angle +
                '}';
    }
}
