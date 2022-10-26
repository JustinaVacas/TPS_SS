

public class Particle {

    private int id;
    private double radio;
    private double x;
    private double y;
    private double vx;
    private double vy;

    public Particle(int id, double x, double y,double vx, double vy) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
    }

    public Particle(int id, double radio) {
        this.id = id;
        this.radio = radio;
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
                ", x= " + x +
                ", y= " + y +
                ", radio= " + radio +
                ", vx = " + vx +
                ", vy = " + vy +
                '}';
    }
}
