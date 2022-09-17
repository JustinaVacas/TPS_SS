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
    public static double tabiqueUp = 0.05;
    public static double tabiqueDown = 0.04;
    public static double openingLength = 0.01;
    public static double currentTime = 0;

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

        GeneratorFiles.outputFrames(frames);

    }

    public static void gazDiffusion() {
        System.out.println("particles: " + particlesArray.size());
        List<Particle> particles1 = new ArrayList<>();
        List<Particle> particles2 = new ArrayList<>();
        int count = 0;
        double fraction = 1;
        boolean eqFrac = false;
        double eqTime = 0;
        double epsilon = 0.01;
//        while (!eqFrac || (currentTime < eqTime*2)) {
        while(count < 5) {
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
            fraction = updatePositions(collisions.get(0).getTc() - currentTime);
            //TODO
            addFrames(collisions.get(0), frames);

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

            currentTime += (collisions.get(0).getTc() - currentTime);
/*
            if(!eqFrac && fraction <= 0.5 + epsilon && fraction >= 0.5 - epsilon){
                eqFrac = true;
                eqTime = currentTime;
            }
*/
            System.out.println("frames" + frames);

        }
    }

    public static void checkWallsAndParticles(List<ParticleCollision> collisions){
        for (Particle particle: particlesArray ) {
            double vx = particle.getVx();
            double vy = particle.getVy();
            Double tch = null;
            Double tcv = null;
            Double newY = null;
            ParticleCollision.CollisionWall wallh = null;
            ParticleCollision.CollisionWall wallv = null;
            //paredes verticales
            if (vx > 0) {
                //primera mitad
                if (particle.getX() < width/2){     // Is in left enclosure going right. Check if collision would be with middle wall or right wall
                    tcv = (width/2 - particle.getRadio() - particle.getX()) / particle.getVx();
                    newY = (tcv * particle.getVy()) + particle.getY();
                    //auxY > 0.5 o auxY < 0.4
                    //height = 0.09
                    //0.5 0.4 0.05 y 0.04
                    if(newY >= (height/2 + openingLength/2) || newY <= (height/2 - openingLength/2)){   // choca con la pared del medio
                        wallv = ParticleCollision.CollisionWall.VERTICAL;
                    }
                    else{ // pasa de largo y choca con la ultima pared
                        //Aux < 0.5 y y > 0.4
                        if (!(newY < (height/2 + openingLength/2) && newY > (height/2 + openingLength/2 - particle.getRadio())) || (newY > (height/2 - openingLength/2) && newY < (height/2 - openingLength/2 + particle.getRadio()))) {
                            tcv = (width - particle.getRadio() - particle.getX())/particle.getVx();
                            wallv = ParticleCollision.CollisionWall.VERTICAL;
                        }
                    }
                } else { // esta del otro lado, del derecho
                    tcv = (width - particle.getRadio() - particle.getX())/particle.getVx();
                    wallv = ParticleCollision.CollisionWall.VERTICAL;
                }
            } else {
                //segunda mitad
                if (particle.getX() > width/2){     // Is in right enclosure going left. Check if collision would be with middle wall or right wall
                    tcv = (width/2 + particle.getRadio() - particle.getX()) / particle.getVx();
                    newY = (tcv * particle.getVy()) + particle.getY();
                    if(newY >= (height/2 + openingLength/2) || newY <= (height/2 - openingLength/2)){
                        wallv = ParticleCollision.CollisionWall.VERTICAL;
                    }
                    else{
                        if (!(newY < (height/2 + openingLength/2) && newY > (height/2 + openingLength/2 - particle.getRadio())) || (newY > (height/2 - openingLength/2) && newY < (height/2 - openingLength/2 + particle.getRadio()))) {
                            tcv = (particle.getRadio() - particle.getX()) / particle.getVx();
                            wallv = ParticleCollision.CollisionWall.VERTICAL;
                        }
                    }
                } else {
                    tcv = (particle.getRadio() - particle.getX())/particle.getVx();
                    wallv = ParticleCollision.CollisionWall.VERTICAL;
                }
            }

            // Check vertical collision
            if (particle.getVy() > 0){
                tch = (height - particle.getRadio() - particle.getY()) / particle.getVy();
                wallh = ParticleCollision.CollisionWall.HORIZONTAL;
            } else {
                tch = (particle.getRadio() - particle.getY()) / particle.getVy();
                wallh = ParticleCollision.CollisionWall.HORIZONTAL;
            }



                //choca la pared del medio, esta entre la pared 1 y 2
                /*if (particle.getX() < wallX2 && particle.getX() > wallX1 && (particle.getY() < 0.4 || particle.getY() > 0.5)) {
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

                }*/
            /*} else {
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
            }*/
            //paredes horizontal
//            if (vy > 0) {
//                //choca pared arriba y no esta en el tabique (esta entre la 1 y 2 o la 2 y 3)
//                if((particle.getX() > wallX1 && particle.getX() < wallX2) || (particle.getX() > wallX2 && particle.getX() < wallX3) ){
//                    tch = (height - particle.getRadio() - particle.getY()) / vy;
//                    wallh = ParticleCollision.CollisionWall.HORIZONTAL;
//
//                }
//            } else {
//                //choca pared abajo y no esta en el tabique (esta entre la 1 y 2 o la 2 y 3)
//                if(particle.getX() > wallX2 && particle.getX() < wallX3 || particle.getX() > wallX1 && particle.getX() < wallX2 ){
//                    tch =  (0 + particle.getRadio() - particle.getY()) / vy;
//                    wallh = ParticleCollision.CollisionWall.HORIZONTAL;
//                }
//            }

            if (tch<0) System.out.println("tch: " + tch);
            if (tcv<0) System.out.println("tcv: " + tcv);

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
            if (particletc!=null && particletc.getKey()<0) System.out.println("particle tc: " + particletc.getKey());
            //comparo con choque con particula
            if(tc != null) {
                if((particletc != null) && (tc > particletc.getKey())) {
                    if (particletc.getValue().getVy() != 0 || particletc.getValue().getVx() != 0 ) {
//                        System.out.println("choque de la " + particle.getId() + " con la " + particletc.getValue().getId());
                        collisions.add(new ParticleCollision(particle, particletc.getValue(), particletc.getKey() + currentTime, null));
                    } else {
                        collisions.add(new ParticleCollision(particle, particletc.getValue(), particletc.getKey() + currentTime, ParticleCollision.CollisionWall.TABIQUE));
                    }
                } else {
//                    System.out.println("choque de la " + particle.getId() + " con pared" );
                    collisions.add(new ParticleCollision(particle, null, tc + currentTime, wall));
                }
            }
        }
    }

    public static Pair<Double,Particle> checkParticles(Particle particle1){

        ArrayList<Particle> arrayParticles = new ArrayList<>(particlesArray);
        Particle particleAuxUp = new Particle(particlesArray.size(), GeneratorFiles.getR(), 0,0, Double.POSITIVE_INFINITY ,0);
        particleAuxUp.setX(width/2);
        particleAuxUp.setY(tabiqueUp);
        Particle particleAuxDown = new Particle(particlesArray.size()+1, GeneratorFiles.getR(), 0,0, Double.POSITIVE_INFINITY, 0);
        particleAuxDown.setX(width/2);
        particleAuxDown.setY(tabiqueDown);
        arrayParticles.add(particleAuxUp);
        arrayParticles.add(particleAuxDown);
        double minTc = 10000;
        Pair<Double, Particle> aux = null;
        for (Particle particle2: arrayParticles) {
            double tc;

            double deltaX = particle2.getX() - particle1.getX();
            double deltaY = particle2.getY() - particle1.getY();
            double deltaVx = particle2.getVx() - particle1.getVx();
            double deltaVy = particle2.getVy() - particle1.getVy();
            double sigma = particle1.getRadio() + particle2.getRadio();
            double deltaRSq = deltaX*deltaX +deltaY*deltaY;
            double deltaVSq = deltaVx*deltaVx + deltaVy*deltaVy;
            double deltaVdeltaR =deltaVx*deltaX + deltaVy*deltaY;
            double d = deltaVdeltaR*deltaVdeltaR - deltaVSq * (deltaRSq - sigma*sigma);


            //no chocan las particulas
            if (deltaVdeltaR >= 0 || d < 0) {
                continue;
            }
            else {
                tc = - ((deltaVdeltaR + Math.sqrt(d) )/(deltaVSq));
                if (tc < 0) System.out.println("tc " + tc + " para la " + particle1.getId() + " con la " + particle2.getId());
                if(tc < minTc) {        // descarto la collisiones
                    aux = new Pair<>(tc, particle2);
                }
            }
        }
        return aux;
    }

    public static double updatePositions(double tc){
        int particles = 0;
        for(Particle particle : particlesArray){
            double newX = particle.getX() + (particle.getVx() * tc);
            double newY = particle.getY() + (particle.getVy() * tc);
            if (newY<0) System.out.println("--------------- new y " + newY + " para la particula " + particle.getId());
            particle.setX(newX);
            particle.setY(newY);
            if(newX < width/2){
                particles++;
            }
        }
        return particles / particlesArray.size();
    }

    public static void addFrames(ParticleCollision collision, List<ArrayList<Double>> frames) {
        frames.add(new ArrayList<Double>());
        ArrayList<Double> collisionArray = new ArrayList<Double>();

        if(collision.getWall()!=null){
            collisionArray.add(collision.getTc());
            collisionArray.add((double)1);
            collisionArray.add((double)collision.getParticle1().getId());
            collisionArray.add((double)-1);
        }
        if(collision.getWall()==null){
            collisionArray.add(collision.getTc());
            collisionArray.add((double)0);
            collisionArray.add((double) collision.getParticle1().getId());
            collisionArray.add((double) collision.getParticle2().getId());
        }
        frames.add(collisionArray);

        for(Particle particle : particlesArray){
            ArrayList<Double> state = new ArrayList<Double>();
            state.add((double) particle.getId());
            state.add(particle.getX());
            state.add(particle.getY());
            state.add(particle.getVx());
            state.add(particle.getVy());
            frames.add(state);
        }
    }

    public static void updateVelocity(List<ParticleCollision> collisions){
        double vy;
        double vx;
        ParticleCollision particle = collisions.get(0);
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

                double deltaX = particle2.getX() - particle1.getX();
                double deltaY = particle2.getY() - particle1.getY();
                double deltaVx = particle2.getVx() - particle1.getVx();
                double deltaVy = particle2.getVy() - particle1.getVy();
                double sigma = particle1.getRadio() + particle2.getRadio();
                double deltaVdeltaR =deltaVx*deltaX + deltaVy*deltaY;
                double J = (2 * (particle1.getM() * particle2.getM()) * deltaVdeltaR) / (sigma * (particle1.getM() + particle2.getM()));
                double Jx = (J *deltaX) / sigma;
                double Jy = (J *deltaY) / sigma;

                double Vx1 = particle1.getVx() + (Jx/particle1.getM());
                double Vy1 = particle1.getVy() + (Jy/particle1.getM());
                double Vx2 = particle2.getVx() - (Jx/particle2.getM());
                double Vy2 = particle2.getVy() - (Jy/particle2.getM());

                particle1.setVx(Vx1);
                particle1.setVy(Vy1);
                particle2.setVx(Vx2);
                particle2.setVy(Vy2);
            }
    }
}
