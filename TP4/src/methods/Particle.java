package methods;

public class Particle {

    private double x;
    private double y;
    private double m;
    private double vx;
    private double vy;

    public Particle(int x,double y, double vx, double vy, double m) {
        this.x = x;
        this.y = y;
        this.m = m;
        this.vx = vx;
        this.vy = vy;
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
        return "methods.Particle{" +
                ", vx = " + vx +
                ", vy = " + vy +
                ", m= " + m +
                ", x= " + x +
                ", y= " + y +
                '}';
    }
}
