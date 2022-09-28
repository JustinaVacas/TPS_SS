package methods;

public class Planet {

    private double x;
    private double y;
    private double m;
    private double vx;
    private double vy;
    private double r;
    private int id;

    public Planet(int id, double r, double m) {
        this.id = id;
        this.m = m;
        this.r = r;
    }

    public double getR() {
        return r;
    }

    public void setR(double r) {
        this.r = r;
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
        return "methods.Planet{" +
                "r= " + r +
                ", m= " + m +
                ", x= " + x +
                ", y= " + y +
                ", vx = " + vx +
                ", vy = " + vy +
                '}';
    }
}
