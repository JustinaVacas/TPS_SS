package methods;

import javafx.util.Pair;
import utils.GeneratorFiles;

import java.util.ArrayList;
import java.util.List;

public class Simulator {

    public static int N = 0;
    public static int L = 0;
    public static int time = 0;
    public static double wallX1 = 0;
    public static double wallX2 = 0.12;
    public static double wallX3 = 0.24;
    public static double height = 0.09;
    public static double width = 0.24;;
    public static double tabiqueUp = 0.5;
    public static double tabiqueDown = 0.4;

    public static ArrayList<Particle> particlesArray = new ArrayList<>();
    public static List<ArrayList<Double>> frames = new ArrayList<>();
    public static List<Double> times = new ArrayList<>();

    public static void main(String[] args){
        Parser.ParseParameters(args[0], args[1], particlesArray);
        System.out.println("N: " + N);
        System.out.println("L: " + L);
        System.out.println("particulas: " + particlesArray);
        System.out.println("---------------------------------------------------");

        gazDiffusion();
    }

    public static void gazDiffusion() {
        System.out.println("particles: " + particlesArray.size());
        List<Particle> particles1 = new ArrayList<>();
        List<Particle> particles2 = new ArrayList<>();
        int count = 0;
        while(count < 20) {
            /*if(particles2.size() != 0) {
                if (Math.floor(particles1.size() / particles2.size()) == 0.5 || Math.ceil(particles1.size() / particles2.size()) == 0.5) {
                    break;
                }
            }*/
            particles1 = new ArrayList<>();
            particles2 = new ArrayList<>();
            List<ParticleCollision> collisions = new ArrayList<>();
            checkWallsAndParticles(collisions);
            // System.out.println("collision dps de choque con paredes: " + collisions);
            // System.out.println("---------------------------------------------------");

            updatePositions(collisions);
            System.out.println("dps de updatePosition " + particlesArray);
            // System.out.println("---------------------------------------------------");

            addFrames(collisions, frames);
            //System.out.println("frames " + frames);
            // System.out.println("---------------------------------------------------");

            updateVelocity(collisions);
            //System.out.println("dps de updateVelocity " + particlesArray);
            // System.out.println("---------------------------------------------------");
            for (Particle particle : particlesArray) {
                if (particle.getX() < 0.12) {
                    particles1.add(particle);
                } else {
                    particles2.add(particle);
                }
            }
            System.out.println("particulas1 " + particles1.size());
            System.out.println("particulas2 " + particles2.size());
            count++;
        }
    }

    public static void checkWallsAndParticles(List<ParticleCollision> collisions){
        for (Particle particle: particlesArray ) {
            double vx = particle.getVx();
            double vy = particle.getVy();
            Double tch = null;
            Double tcv = null;
            ParticleCollision.CollisionWall wall;
            //paredes verticales
            if (vx > 0) {
                //choca la pared del medio, esta entre la pared 1 y 2
                if (particle.getX() < wallX2 && particle.getX() > wallX1 && (particle.getY() < 0.4 || particle.getY() > 0.5)) {
                    tcv = (wallX2 - particle.getRadio() - particle.getX()) / vx;
                    double newY = particle.getY() + vy * tcv;
                    if(newY > tabiqueDown && newY < tabiqueUp){ // si pasa por el tabique
                        tcv = (wallX3 - particle.getRadio() - particle.getX()) / vx;
                    }
                }
                // choca en la ultima pared por pasar por el tabique, esta entre la pared 1 y la 2
                else if (particle.getX() < wallX2 && particle.getX() > wallX1 && (particle.getY() > 0.4 && particle.getY() < 0.5)) {
                    tcv = (wallX3 - particle.getRadio() - particle.getX()) / vx;
                }
                //choca ultima pared, esta entre 2 y 3 pared
                else if (particle.getX() > wallX2 && particle.getX() < wallX3) {
                    tcv = (wallX3 - particle.getRadio() - particle.getX()) / vx;
                }
            } else {
                //choca la pared del medio y esta entre la pared 2 y 3
                if (particle.getX() > wallX2 && particle.getX() < wallX3 && (particle.getY() < 0.4 || particle.getY() > 0.5)) {
                    tcv = (wallX2 + particle.getRadio() - particle.getX()) / vx;
                    double newY = particle.getY() + vy * tcv;
                    if(newY > tabiqueDown && newY < tabiqueUp){ // si pasa por el tabique
                        tcv = (wallX1 + particle.getRadio() - particle.getX()) / vx;
                    }
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
            } else {
                //choca pared abajo y no esta en el tabique (esta entre la 1 y 2 o la 2 y 3)
                if(particle.getX() > wallX2 && particle.getX() < wallX3){
                    tch =  (0 + particle.getRadio() - particle.getY()) / vy;
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

            Pair<Double,Particle> particletc = checkParticles(particle);
            //comparo con choque con particula
            if(tc != null) {
                if((particletc != null) && (tc > particletc.getKey())) {
                    if (particletc.getValue() != null) {
                        collisions.add(new ParticleCollision(particle, particletc.getValue(), particletc.getKey(), null));
                    } else {
                        collisions.add(new ParticleCollision(particle, null, particletc.getKey(), ParticleCollision.CollisionWall.TABIQUE));
                    }
                } else {
                    collisions.add(new ParticleCollision(particle, null, tc, wall));
                }
            }
        }
    }

    public static Pair<Double,Particle> checkParticles(Particle particle1){

        ArrayList<Particle> arrayParticles = new ArrayList<>(particlesArray);
        Particle particleAuxUp = new Particle(particlesArray.size(), GeneratorFiles.getR(), 0, GeneratorFiles.getM(), 0);
        particleAuxUp.setX(width/2);
        particleAuxUp.setY(0.5);
        Particle particleAuxDown = new Particle(particlesArray.size()+1, GeneratorFiles.getR(), 0, GeneratorFiles.getM(), 0);
        particleAuxDown.setX(width/2);
        particleAuxDown.setY(0.4);
        arrayParticles.add(particleAuxUp);
        arrayParticles.add(particleAuxDown);

        for (Particle particle2: arrayParticles) {
            double tc;
            double sigma = particle1.getRadio() + particle2.getRadio();

            double[] deltaR = new double[]{particle1.getX() - particle2.getX(),particle1.getY()- particle2.getY()};
            double r2 = deltaR[0] * deltaR[0] + deltaR[1] * deltaR[1];;

            double[] deltaV = new double[]{particle1.getVx() - particle2.getVx(), particle1.getVy() - particle2.getVy()};
            double v2 = deltaV[0] * deltaV[0] + deltaV[1] * deltaV[1];

            double vr = deltaR[0] * deltaV[0] + deltaR[1] * deltaV[1];
            double d = (vr * vr) - (v2 * (r2 - (sigma * sigma)));

            //chocan las particulas
            if (!(vr >= 0 || d < 0)) {
                tc = - (vr + Math.sqrt(d) )/(v2);
                if(particle2.getV() != 0) {
                    return new Pair<>(tc,particle2);
                }
                else {
                    return new Pair<>(tc,null);
                }
            }
        }
        return null;
    }

    public static void updatePositions(List<ParticleCollision> collisions){
        for(ParticleCollision particle : collisions){
            Particle particle1 = particle.getParticle1();
            double newX = particle1.getX() + (particle1.getVx() * particle.getTc());
            double newY = particle1.getY() + (particle1.getVy() * particle.getTc());
            particle1.setX(newX);
            particle1.setY(newY);
        }
    }

    public static void addFrames(List<ParticleCollision> collisions, List<ArrayList<Double>> frames) {
        for(ParticleCollision particle : collisions){
            Particle particle1 = particle.getParticle1();
            ArrayList<Double> state = new ArrayList<Double>();
            state.add((double) particle1.getId());
            state.add(particle1.getX());
            state.add(particle1.getY());
            state.add(particle1.getAngle());
            state.add(particle1.getV());
            state.add(particle1.getRadio());
            state.add(particle.getTc());
            frames.add(state);
        }
    }

    public static void updateVelocity(List<ParticleCollision> collisions){
        for(ParticleCollision particle : collisions){
            Particle particle1 = particle.getParticle1();
            //choca pared vertical
            if(particle.getWall() == ParticleCollision.CollisionWall.VERTICAL || particle.getWall() == ParticleCollision.CollisionWall.TABIQUE){
                particle1.setVx(-particle1.getVx());
            }
            //choca pared horizontal
            if(particle.getWall() == ParticleCollision.CollisionWall.HORIZONTAL){
                particle1.setVy(-particle1.getVy());
            }
            //choca pared horizontal
            if(particle.getWall() == ParticleCollision.CollisionWall.CORNER){
                particle1.setVy(-particle1.getVy());
                particle1.setVx(-particle1.getVx());
            }
            particle1.setV(Math.sqrt(Math.pow(particle1.getVx(),2)+Math.pow(particle1.getVy(),2)));

            if(particle.getWall() == null && particle.getParticle2() != null ){

                Particle particle2 = particle.getParticle2();

                double sigma = particle1.getRadio() + particle2.getRadio();
                double[] deltaR = new double[]{particle1.getX() - particle2.getX(),particle1.getY()- particle2.getY()};
                double[] deltaV = new double[]{particle1.getVx() - particle2.getVx(), particle1.getVy() - particle2.getVy()};
                double vr = deltaR[0] * deltaV[0] + deltaR[1] * deltaV[1];
                double J = (2 * particle1.getM() * particle2.getM() * vr) / (sigma * (particle1.getM() + particle2.getM()));
                double Jx = (J * (particle1.getX() - particle2.getX()) )/sigma;
                double Jy = (J * (particle1.getY() - particle2.getY()) )/sigma;

                double Vx1 = particle1.getVx() + Jx/particle1.getM();
                double Vy1 = particle1.getVy() + Jy/particle1.getM();
                double Vx2 = particle2.getVx() - Jx/particle2.getM();
                double Vy2 = particle2.getVy() - Jy/particle2.getM();

                particle1.setV(Math.sqrt(Vx1*Vx1 + Vy1*Vy1));
                particle2.setV(Math.sqrt(Vx2*Vx2 + Vy2*Vy2));

                particle1.setVx(Vx1);
                particle1.setVy(Vy1);
                particle2.setVx(Vx2);
                particle2.setVy(Vy2);
            }
        }
    }
}
