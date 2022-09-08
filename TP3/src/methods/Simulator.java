package methods;

import java.util.ArrayList;
import java.util.List;
import static java.lang.Math.*;

public class Simulator {

    public static int N = 0;
    public static int L = 0;
    public static int time = 0;
    public static double wallX1 = 0;
    public static double wallX2 = 0.12;
    public static double wallX3 = 0.24;
    public static double height = 0.09;
    public static double width = 0.24;;
    //0 - 0.4 y 0.5 - 0.9

    public static ArrayList<Particle> particlesArray = new ArrayList<>();
    public static List<ArrayList<Double>> frames = new ArrayList<>();
    public static List<Double> times = new ArrayList<>();

    public static void main(String[] args){
        Parser.ParseParameters(args[0], args[1], particlesArray);
        System.out.println("N: " + N);
        System.out.println("L: " + L);
        System.out.println("particulas: " + particlesArray);

        gazDiffusion();
    }

    public static void gazDiffusion() {

        List<ParticleCollision> collisions = new ArrayList<>();
        checkWalls(collisions);
        checkParticles(collisions);
    }

    public static void checkWalls(List<ParticleCollision> collisions){

        for (Particle particle: particlesArray ) {
            double vx = cos(particle.getId()) * particle.getV();
            double vy = sin(particle.getId()) * particle.getV();
            Double tch = null;
            Double tcv = null;
            ParticleCollision.CollisionWall wall;
            //paredes verticales
            if (vx > 0) {
                //choca tabique, esta entre la pared 1 y. 2
                if (particle.getX() < wallX2 && particle.getX() > wallX1 && (particle.getY() < 0.4 || particle.getY() > 0.5)) {
                    tcv = (wallX2 - particle.getRadio() - particle.getX()) / vx;
                }
                // choca en la ultima pared por pasar por el tabique, esta entre la 1 y la 2
                else if (particle.getX() < wallX2 && particle.getX() > wallX1 && (particle.getY() > 0.4 && particle.getY() < 0.5)) {
                    tcv = (wallX3 - particle.getRadio() - particle.getX()) / vx;
                }
                //choca ultima pared, x entre 2 y 3 pared
                else if (particle.getX() > wallX2 && particle.getX() < wallX3) {
                    tcv = (wallX3 - particle.getRadio() - particle.getX()) / vx;
                }
            } else {
                //choca tabique y esta entre la pared 2 y 3
                if (particle.getX() > wallX2 && particle.getX() < wallX3 && (particle.getY() < 0.4 || particle.getY() > 0.5)) {
                    tcv = (wallX2 + particle.getRadio() - particle.getX()) / vx;
                }
                // choca en la primera pared por pasar por el tabique, esta entre la 2 y la 3
                else if (particle.getX() > wallX2 && particle.getX() < wallX3 && (particle.getY() > 0.4 && particle.getY() < 0.5)) {
                    tcv = (wallX1 + particle.getRadio() - particle.getX()) / vx;
                }
                //choca primera pared, x entre 1 y 2 pared
                else if (particle.getX() < wallX2 && particle.getX() > wallX1) {
                    tcv = (wallX1 + particle.getRadio() - particle.getX()) / vx;
                }
            }
            //paredes horizontal
            if (vy > 0) {
                //choca pared arriba y no esta en el tabique (esta entre la 1 y 2 o la 2 y 3)
                if((particle.getX() > wallX1 && particle.getX() < wallX2) || (particle.getX() > wallX2 && particle.getX() < wallX3) ){
                    tch = (height - particle.getRadio() - particle.getY()) / vy;
                }
                //particula en el tabique puede chocar arriba
                if(particle.getX() == width/2){
                    tch = (0.5 - particle.getRadio() - particle.getY()) / vy;
                }
            } else {
                //choca pared abajo y no esta en el tabique (esta entre la 1 y 2 o la 2 y 3)
                if(particle.getX() > wallX2 && particle.getX() < wallX3){
                    tch =  (height + particle.getRadio() - particle.getY()) / vy;
                }
                //particula en el tabique  puede chocar abajo
                if(particle.getX() == width/2){
                    //TODO es 0.4 + o 0.4 -
                    tch = (0.4 + particle.getRadio() - particle.getY()) / vy;
                }
            }

            Double tc;
            if(tch != null && tch.equals(tcv)){
                tc = tch;
                wall = ParticleCollision.CollisionWall.CORNER;
            }
            else if(tch == null){
                tc = tcv;
                wall = ParticleCollision.CollisionWall.VERTICAL;
            }
            else if(tcv == null){
                tc = tch;
                wall = ParticleCollision.CollisionWall.HORIZONTAL;
            }
            else {
                tc = tch < tcv ? tch : tcv;
                wall = tch < tcv ? ParticleCollision.CollisionWall.HORIZONTAL : ParticleCollision.CollisionWall.VERTICAL;
            }
            collisions.add(new ParticleCollision(particle, null, tc, wall));
        }
    }

    public static void checkParticles(List<ParticleCollision> collisions){

        double tc;
        for (Particle particle1: particlesArray) {
            for (Particle particle2: particlesArray) {
                double sigma = particle1.getRadio() + particle2.getRadio();
                double[] deltaR;
                deltaR= new double[]{particle2.getX() - particle1.getX(),particle2.getY()- particle1.getY()};
                double[] deltaV;
                deltaV= new double[]{cos(particle1.getAngle()) * particle1.getV() - cos(particle2.getAngle()) * particle2.getV(),sin(particle1.getAngle())* particle1.getV() - sin(particle2.getAngle())* particle2.getV()};
                double vr = deltaR[0] * deltaV[0] + deltaR[1] * deltaV[1];
                double v2 = deltaV[0] * deltaV[0] + deltaV[1] * deltaV[1];
                double r2 = deltaR[0] * deltaR[0] + deltaR[1] * deltaR[1];;
                double d = (vr * vr) - (v2) * (r2 - (sigma * sigma));

                //chocan las particulas
                if (!(vr >= 0 || d < 0)) {
                    tc = - (vr + Math.sqrt(d) )/(v2);
                    collisions.add(new ParticleCollision(particle1, particle2, tc, null));
                }
            }
        }
    }

}
