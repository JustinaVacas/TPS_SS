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
            System.out.println("---------------------------- ITERACION " + count + " -----------------------------------");
            /*if(particles2.size() != 0) {
                if (Math.floor(particles1.size() / particles2.size()) == 0.5 || Math.ceil(particles1.size() / particles2.size()) == 0.5) {
                    break;
                }
            }*/
            particles1 = new ArrayList<>();
            particles2 = new ArrayList<>();
            List<ParticleCollision> collisions = new ArrayList<>(); //1: tc=3 2: tc=5 3: tc=1

            checkWallsAndParticles(collisions);
            collisions.sort(ParticleCollision::compareTo);
            System.out.println("collisions " + collisions);

            //sacar el menor tiempo
            //updatear con EL TC ESTE DEL MENOR TIEMPO
            updatePositions(collisions.get(0).getTc());

            addFrames(collisions, frames);

            updateVelocity(collisions);

            System.out.println("dps de updateVelocity y updatePosition " + particlesArray);

            for (Particle particle : particlesArray) {
                if (particle.getX() < 0.12) {
                    particles1.add(particle);
                } else {
                    particles2.add(particle);
                }
            }
            System.out.println("particulas del lado derecho " + particles1.size());
            System.out.println("particulas del lado izquierdo " + particles2.size());
            count++;
        }
    }

    public static void checkWallsAndParticles(List<ParticleCollision> collisions){
        for (Particle particle: particlesArray ) {
            double vx = particle.getVx();
            double vy = particle.getVy();
            Double tch = null;
            Double tcv = null;
            ParticleCollision.CollisionWall wallh = null;
            ParticleCollision.CollisionWall wallv = null;
            //paredes verticales
            if (vx > 0) {
                //choca la pared del medio, esta entre la pared 1 y 2
                if (particle.getX() < wallX2 && particle.getX() > wallX1 && (particle.getY() < 0.4 || particle.getY() > 0.5)) {
                    tcv = (wallX2 - particle.getRadio() - particle.getX()) / vx;
                    wallv = ParticleCollision.CollisionWall.VERTICAL;
                    double newY = particle.getY() + (vy * tcv);
                    if(newY > tabiqueDown && newY < tabiqueUp){ // si pasa por el tabique
                        tcv = (wallX3 - particle.getRadio() - particle.getX()) / vx;
                        wallv = ParticleCollision.CollisionWall.VERTICAL;
                    }
                }
                // choca en la ultima pared por pasar por el tabique, esta entre la pared 1 y la 2
                else if (particle.getX() < wallX2 && particle.getX() > wallX1 && (particle.getY() > 0.4 && particle.getY() < 0.5)) {
                    tcv = (wallX3 - particle.getRadio() - particle.getX()) / vx;
                    wallv = ParticleCollision.CollisionWall.VERTICAL;
                }
                //choca ultima pared, esta entre 2 y 3 pared
                else if (particle.getX() > wallX2 && particle.getX() < wallX3) {
                    tcv = (wallX3 - particle.getRadio() - particle.getX()) / vx;
                    wallv = ParticleCollision.CollisionWall.VERTICAL;

                }
            } else {
                //choca la pared del medio y esta entre la pared 2 y 3
                if (particle.getX() > wallX2 && particle.getX() < wallX3 && (particle.getY() < 0.4 || particle.getY() > 0.5)) {
                    tcv = (wallX2 + particle.getRadio() - particle.getX()) / vx;
                    wallv = ParticleCollision.CollisionWall.VERTICAL;
                    double newY = particle.getY() + (vy * tcv);
                    if(newY > tabiqueDown && newY < tabiqueUp){ // si pasa por el tabique
                        tcv = (wallX1 + particle.getRadio() - particle.getX()) / vx;
                        wallv = ParticleCollision.CollisionWall.VERTICAL;
                    }
                }
                // choca en la primera pared por pasar por el tabique, esta entre la 2 y la 3
                else if (particle.getX() > wallX2 && particle.getX() < wallX3 && (particle.getY() > 0.4 && particle.getY() < 0.5)) {
                    tcv = (wallX1 + particle.getRadio() - particle.getX()) / vx;
                    wallv = ParticleCollision.CollisionWall.VERTICAL;

                }
                //choca primera pared, x entre 1 y 2 pared
                else if (particle.getX() < wallX2 && particle.getX() > wallX1) {
                    tcv = (wallX1 + particle.getRadio() - particle.getX()) / vx;
                    wallv = ParticleCollision.CollisionWall.VERTICAL;
                }
            }
            //paredes horizontal
            if (vy > 0) {
                //choca pared arriba y no esta en el tabique (esta entre la 1 y 2 o la 2 y 3)
                if((particle.getX() > wallX1 && particle.getX() < wallX2) || (particle.getX() > wallX2 && particle.getX() < wallX3) ){
                    tch = (height - particle.getRadio() - particle.getY()) / vy;
                    wallh = ParticleCollision.CollisionWall.HORIZONTAL;

                }
            } else {
                //choca pared abajo y no esta en el tabique (esta entre la 1 y 2 o la 2 y 3)
                if(particle.getX() > wallX2 && particle.getX() < wallX3 || particle.getX() > wallX1 && particle.getX() < wallX2 ){
                    tch =  (0 + particle.getRadio() - particle.getY()) / vy;
                    wallh = ParticleCollision.CollisionWall.HORIZONTAL;
                }
            }

            Double tc;
            ParticleCollision.CollisionWall wall;
            if(tch != null && tch.equals(tcv)){
                tc = tch;
                wall = wallh;
            }
            else if(tch == null){
                tc = tcv;
                wall = wallv;
            }
            else if(tcv == null){
                tc = tch;
                wall = wallh;
            }
            else {
                tc = tch < tcv ? tch : tcv;
                wall = tch < tcv ? wallh : wallv;
            }

            Pair<Double,Particle> particletc = checkParticles(particle);
            //comparo con choque con particula
            if(tc != null) {
                if((particletc != null) && (tc > particletc.getKey())) {
                    if (particletc.getValue().getVy() != 0 || particletc.getValue().getVx() != 0 ) {
//                        System.out.println("choque de la " + particle.getId() + " con la " + particletc.getValue().getId());
                        collisions.add(new ParticleCollision(particle, particletc.getValue(), particletc.getKey(), null));
                    } else {
                        collisions.add(new ParticleCollision(particle, particletc.getValue(), particletc.getKey(), ParticleCollision.CollisionWall.TABIQUE));
                    }
                } else {
//                    System.out.println("choque de la " + particle.getId() + " con pared" );
                    collisions.add(new ParticleCollision(particle, null, tc, wall));
                }
            }
        }
    }

    public static Pair<Double,Particle> checkParticles(Particle particle1){

        ArrayList<Particle> arrayParticles = new ArrayList<>(particlesArray);
        Particle particleAuxUp = new Particle(particlesArray.size(), GeneratorFiles.getR(), 0,0, Double.POSITIVE_INFINITY ,0);
        particleAuxUp.setX(width/2);
        particleAuxUp.setY(0.5);
        Particle particleAuxDown = new Particle(particlesArray.size()+1, GeneratorFiles.getR(), 0,0, Double.POSITIVE_INFINITY, 0);
        particleAuxDown.setX(width/2);
        particleAuxDown.setY(0.4);
        arrayParticles.add(particleAuxUp);
        arrayParticles.add(particleAuxDown);
        double minTc = 10000;
        Pair<Double, Particle> aux = null;
        for (Particle particle2: arrayParticles) {
            double tc;
            double sigma = particle1.getRadio() + particle2.getRadio();

            double[] deltaR = new double[]{particle2.getX() - particle1.getX(), particle2.getY()- particle1.getY()};
            double r2 = deltaR[0] * deltaR[0] + deltaR[1] * deltaR[1];

            double[] deltaV = new double[]{particle2.getVx() - particle1.getVx(), particle2.getVy() - particle1.getVy()};
            double v2 = deltaV[0] * deltaV[0] + deltaV[1] * deltaV[1];

            double vr = deltaR[0] * deltaV[0] + deltaR[1] * deltaV[1];
            double d = (vr*vr) - (v2 * (r2 - (sigma * sigma)));

            //chocan las particulas
            if (vr >= 0 || d < 0) {
                continue;
            }
            else {
                tc = - ((vr + Math.sqrt(d) )/(v2));
                if (tc<0) System.out.println("tc " + tc + " para la " + particle1.getId() + " con la " + particle2.getId());
                if(tc < minTc) {        // descarto la collisiones
                    aux = new Pair<>(tc, particle2);
                }
            }
        }
        return aux;
    }

    public static void updatePositions(double tc){
        for(Particle particle : particlesArray){
            double newX = particle.getX() + (particle.getVx() * tc);
            double newY = particle.getY() + (particle.getVy() * tc);
            if (newY<0) System.out.println("--------------- new y " + newY + " para la particula " + particle.getId());
            particle.setX(newX);
            particle.setY(newY);
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
            double v = Math.sqrt(Math.pow(particle1.getVx(),2)+Math.pow(particle1.getVy(),2));
            state.add(v);
            state.add(particle1.getRadio());
            state.add(particle.getTc());
            frames.add(state);
        }
    }

    public static void updateVelocity(List<ParticleCollision> collisions){
        double vy;
        double vx;
        ParticleCollision particle = collisions.get(0);
//        for(ParticleCollision particle : collisions){
            Particle particle1 = particle.getParticle1();
            //choca pared vertical
            if(particle.getWall() == ParticleCollision.CollisionWall.VERTICAL){
                vx = particle1.getVx();
                particle1.setVx(-vx);
            }
            //choca pared horizontal
            if(particle.getWall() == ParticleCollision.CollisionWall.HORIZONTAL){
                vy = particle1.getVy();
                particle1.setVy(-vy);
            }
            //choca con corner
            if(particle.getWall() == ParticleCollision.CollisionWall.CORNER){
                vx = particle1.getVx();
                vy = particle1.getVy();
                particle1.setVy(-vy);
                particle1.setVx(-vx);
            }

            //choca con particula
            if((particle.getWall() == null && particle.getParticle2() != null) || (particle.getWall() == ParticleCollision.CollisionWall.TABIQUE && particle.getParticle2() != null )){
                Particle particle2 = particle.getParticle2();

                double sigma = particle1.getRadio() + particle2.getRadio();
                double[] deltaR = new double[]{particle2.getX() - particle1.getX(),particle2.getY()- particle1.getY()};
                double[] deltaV = new double[]{particle2.getVx() - particle1.getVx(), particle2.getVy() - particle1.getVy()};
                double vr = deltaR[0] * deltaV[0] + deltaR[1] * deltaV[1];
                double J = (2 * particle1.getM() * particle2.getM() * vr) / (sigma * (particle1.getM() + particle2.getM()));
                double Jx = (J * (particle2.getX() - particle1.getX()) )/sigma;
                double Jy = (J * (particle2.getY() - particle1.getY()) )/sigma;

                double Vx1 = particle1.getVx() + Jx/particle1.getM();
                double Vy1 = particle1.getVy() + Jy/particle1.getM();
                double Vx2 = particle2.getVx() - Jx/particle2.getM();
                double Vy2 = particle2.getVy() - Jy/particle2.getM();

                particle1.setVx(Vx1);
                particle1.setVy(Vy1);
                particle2.setVx(Vx2);
                particle2.setVy(Vy2);
            }
//        }
    }
}
