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

    public static boolean flag = true;

    public static ArrayList<Particle> particlesArray = new ArrayList<>();
    public static ArrayList<Particle> particlesCollision;
    public static List<ArrayList<Double>> frames = new ArrayList<>();

    public static Particle particleAuxUp;
    public static Particle particleAuxDown;


    public static void main(String[] args){
        Parser.ParseParameters(args[0], args[1], particlesArray);
        System.out.println("N: " + N);
        System.out.println("L: " + L);
        System.out.println("particulas: " + particlesArray);
        System.out.println("----------------------------------------------------------------------------");

        gazDiffusion();

        GeneratorFiles.outputFrames(frames);

    }

    public static void gazDiffusion() {
        List<Particle> particles1 = new ArrayList<>();
        List<Particle> particles2 = new ArrayList<>();
        int count = 0;
        double fraction = 1;
        boolean eqFrac = false;
        double eqTime = 0;
        double epsilon = 0.01;
        List<ParticleCollision> collisions = new ArrayList<>();

//        while (!eqFrac || (currentTime < eqTime*2)) {
        while(count < 810) {

            System.out.println("---------------------------- ITERACION " + count + " -----------------------------------");

            if(flag){ //primera vez
                checkWallsAndParticles(collisions, particlesArray);
                flag = false;
            }
            else {
                notInCollisions(collisions, particlesCollision);
//                System.out.println("particle collision " + particlesCollision);
                checkWallsAndParticles(collisions, particlesCollision);
            }

            particles1 = new ArrayList<>();
            particles2 = new ArrayList<>();

            collisions.sort(ParticleCollision::compareTo);
            System.out.println("collisions " + collisions);

            //sacar el menor tiempo
            //updatear con EL TC ESTE DEL MENOR TIEMPO
            fraction = updatePositions(collisions.get(0).getTc() - currentTime);
//            currentTime = collisions.get(0).getTc();

            addFrames(collisions.get(0), frames);

            updateVelocity(collisions);

//            System.out.println("dps de updateVelocity y updatePosition " + particlesArray);

            for (Particle particle : particlesArray) {
                if (particle.getX() < 0.12) {
                    particles1.add(particle);
                } else {
                    particles2.add(particle);
                }
            }
            System.out.println("particulas del lado izquierda " + particles1.size());
            System.out.println("particulas del lado derecha " + particles2.size());
            count++;

            particlesCollision = new ArrayList<>();
            particlesCollision.add(collisions.get(0).getParticle1());
            if(collisions.get(0).getWall() != ParticleCollision.CollisionWall.TABIQUE && collisions.get(0).getParticle2() != null)
                particlesCollision.add(collisions.get(0).getParticle2());
            System.out.println("las elegidas " + particlesCollision);

            currentTime = collisions.get(0).getTc();

            collisions = removeCollisions(collisions);
//            System.out.println("collisions dps de borrar " + collisions);

/*
            if(!eqFrac && fraction <= 0.5 + epsilon && fraction >= 0.5 - epsilon){
                eqFrac = true;
                eqTime = currentTime;
            }
*/
        }
    }

    private static void notInCollisions(List<ParticleCollision> collisions, List<Particle> particlesCollisions) {
        for (Particle particle: particlesArray) {
            boolean isParticle = false;
            for (ParticleCollision collision: collisions) {
                if(collision.getParticle2() == null) {
                    if (particle.getId() == collision.getParticle1().getId()) {
                        isParticle = true;
                    }
                }
                else {
                    if (particle.getId() == collision.getParticle1().getId() || particle.getId() == collision.getParticle2().getId()) {
                        isParticle = true;
                    }
                }
            }
            if(!isParticle) {
                particlesCollisions.add(particle);
            }

        }
    }

    private static List<ParticleCollision> removeCollisions(List<ParticleCollision> collisions) {
        // p1 p2
        // p1 null
        // p1 p2inventada

        // 1  null
        // 1  2

        // 3  2
        // 2  4
        // 5 null
        // 1  2
        // 6  1
        ParticleCollision particleCollision = collisions.get(0); //1 2
        List<ParticleCollision> new_collision = new ArrayList<>();
        for(ParticleCollision collision : collisions){
            if(particleCollision.getParticle2() == null){
                if(collision.getParticle2()==null){
                    if (particleCollision.getParticle1().getId() == collision.getParticle1().getId() ) {
                        continue;
                    } else {
                        new_collision.add(collision);
                    }
                }
                else {
                    if (particleCollision.getParticle1().getId() == collision.getParticle1().getId() || particleCollision.getParticle1().getId() == collision.getParticle2().getId()) {
                        continue;
                    } else {
                        new_collision.add(collision);
                    }
                }
            }
            else {
                if(collision.getParticle2()==null){
                    if (particleCollision.getParticle1().getId() == collision.getParticle1().getId()) {
                        continue;
                    } else if (particleCollision.getParticle2().getId() == collision.getParticle1().getId()) {
                        continue;
                    } else {
                        new_collision.add(collision);
                    }
                }
                else {
                    if (particleCollision.getParticle1().getId() == collision.getParticle1().getId() || particleCollision.getParticle1().getId() == collision.getParticle2().getId()) {
                        continue;
                    } else if (particleCollision.getParticle2().getId() == collision.getParticle1().getId() || particleCollision.getParticle2().getId() == collision.getParticle2().getId()) {
                        continue;
                    } else {
                        new_collision.add(collision);
                    }
                }
            }
        }
        return new_collision;
    }

    public static void checkWallsAndParticles(List<ParticleCollision> collisions, List<Particle> partArray ){
        for (Particle particle: partArray) {
            Double tch = null;
            Double tcv = null;
            Double newY = null;
            ParticleCollision.CollisionWall wallh = null;
            ParticleCollision.CollisionWall wallv = null;
            //paredes verticales
            if (particle.getVx() > 0) {
                //primera mitad, veo si choca con la del medio o la de la derecha
                if (particle.getX() < width/2){
                    tcv = (width/2 - particle.getRadio() - particle.getX()) / particle.getVx();
                    newY = (tcv * particle.getVy()) + particle.getY();
                    if(newY >= (height/2 + openingLength/2) || newY <= (height/2 - openingLength/2)){   // choca con la pared del medio
                        wallv = ParticleCollision.CollisionWall.VERTICAL;
                    }
                    else{ // pasa de largo y choca con la ultima pared
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
                //segunda mitad, veo si choca con la del medio o la izquierda
                if (particle.getX() > width/2){
                    if(particle.getId() == 54){
                        System.out.println("entre");
                    }
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


            // paredes verticales
            if (particle.getVy() > 0){
                tch = (height - particle.getRadio() - particle.getY()) / particle.getVy();
                wallh = ParticleCollision.CollisionWall.HORIZONTAL;
            } else {
                tch = (particle.getRadio() - particle.getY()) / particle.getVy();
                wallh = ParticleCollision.CollisionWall.HORIZONTAL;
            }

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

            if(particle.getId()==54){
                System.out.println("tcv " +tcv);
                System.out.println("tch " +tch);
                System.out.println("coordenadas de 54: " + particle.getX());
                if(particletc!=null)System.out.println("particletc " + particletc.getKey() + " contra " + particletc.getValue().getId());

            }

            if (particletc!=null && particletc.getKey()<0) System.out.println("particle tc: " + particletc.getKey());
            //comparo con choque con particula
            if(tc != null) {
                if((particletc != null) && (tc > particletc.getKey())) {
                    if (particletc.getValue().getM() != Double.POSITIVE_INFINITY ) {
                        collisions.add(new ParticleCollision(particle, particletc.getValue(), particletc.getKey() + currentTime, null));
                    } else {
                        collisions.add(new ParticleCollision(particle, particletc.getValue(), particletc.getKey() + currentTime, ParticleCollision.CollisionWall.TABIQUE));
                    }
                } else {
                    collisions.add(new ParticleCollision(particle, null, tc + currentTime, wall));
                }
            }
        }
    }

    public static Pair<Double,Particle> checkParticles(Particle particle1){

        ArrayList<Particle> arrayParticles = new ArrayList<>(particlesArray);
        particleAuxUp = new Particle(particlesArray.size(), GeneratorFiles.getR(), 0,0, Double.POSITIVE_INFINITY ,0);
        particleAuxDown = new Particle(particlesArray.size()+1, GeneratorFiles.getR(), 0,0, Double.POSITIVE_INFINITY, 0);
        particleAuxUp.setX(width/2);
        particleAuxUp.setY(tabiqueUp);
        particleAuxDown.setX(width/2);
        particleAuxDown.setY(tabiqueDown);
        arrayParticles.add(particleAuxUp);
        arrayParticles.add(particleAuxDown);
        double minTc = 10000;
        Pair<Double, Particle> aux = null;
        for (Particle particle2: arrayParticles) {

            if (particle2.getId() == particle1.getId()){
                continue;
            }

            double tc;

            if (particle1.getId()==54 && particle2.getId()==33  && particle1.getX() == 0.22966890361745546){
                System.out.println("gola");
            }

            double dX = particle2.getX() - particle1.getX();
            double dY = particle2.getY() - particle1.getY();
            double dVx = particle2.getVx() - particle1.getVx();
            double dVy = particle2.getVy() - particle1.getVy();
            double sigma = particle1.getRadio() + particle2.getRadio();
            double deltaRSq = dX*dX +dY*dY;
            double deltaVSq = dVx*dVx + dVy*dVy;
            double dVdR =dVx*dX + dVy*dY;
            double d = dVdR*dVdR - deltaVSq * (deltaRSq - sigma*sigma);

            //no chocan las particulas
            if (dVdR >= 0 || d < 0) {
                continue;
            }
            else {
                tc = - ((dVdR + Math.sqrt(d) )/(deltaVSq));
                if (tc < 0)
                    System.out.println("tc " + tc + " para la " + particle1.getId() + " con la " + particle2.getId());
                if(tc < minTc) {        // descarto la collisiones
                    aux = new Pair<>(tc, particle2);
                    minTc = tc;
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

        collisionArray.add(collision.getTc());
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

        if((particle.getWall() == null && particle.getParticle2() != null) || (particle.getWall() == ParticleCollision.CollisionWall.TABIQUE && particle.getParticle2() != null )){
            Particle particle2 = particle.getParticle2();

            double dX = particle2.getX() - particle1.getX();
            double dY = particle2.getY() - particle1.getY();
            double dVx = particle2.getVx() - particle1.getVx();
            double dVy = particle2.getVy() - particle1.getVy();
            double sigma = particle1.getRadio() + particle2.getRadio();
            double dVdR = dVx * dX + dVy * dY;
            double J;
            if(particle2.getM() == Double.POSITIVE_INFINITY ){ // si son las del tabique
                J = (2  * dVdR) / (sigma);
            }
            else {
                J = (2 * (particle1.getM() * particle2.getM()) * dVdR) / (sigma * (particle1.getM() + particle2.getM()));
            }

            double Jx = (J * dX) / sigma;
            double Jy = (J * dY) / sigma;

            double Vx1 = particle1.getVx() + (Jx / particle1.getM());
            double Vy1 = particle1.getVy() + (Jy / particle1.getM());

            particle1.setVx(Vx1);
            particle1.setVy(Vy1);

            if(!(particle2.getM() == Double.POSITIVE_INFINITY )) { // si son las del tabique

                double Vx2 = particle2.getVx() - (Jx / particle2.getM());
                double Vy2 = particle2.getVy() - (Jy / particle2.getM());

                particle2.setVx(Vx2);
                particle2.setVy(Vy2);
            }
        }
    }
}
