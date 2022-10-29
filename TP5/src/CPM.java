import java.util.*;

public class CPM {

    private static final double EPSILON = 1e-10;
    private static final double TAU = 0.5;
    private static final double ZOMBIE_VISION= 4;
    private static final double HUMAN_VISION = 4;
    private static final double Ap = 2000; //TODO preguntar valor
    private static final double Bp = 0.08; //TODO preguntar valor
    private static final double AVOID_WEIGHT = 2.5; //TODO preguntar valor
    private static final double ApZ = Ap * AVOID_WEIGHT;
    private static final double BpZ = Bp * AVOID_WEIGHT;
    private static final double beta = 1; //TODO preguntar valor

    private static double deltaT;
    public static double vdH;
    public static double vdZ;
    public static double vzi = 0.3;
    private static double t;

    public static double Rmax;
    public static double Rmin;
    public static double R;
    public static ArrayList<Particle> humans;
    public static ArrayList<Particle> zombies;

    public static void run(double rmax, double rmin,double radio, double vdHumans, double vdZombies, ArrayList<Particle> humansArray,ArrayList<Particle> zombiesArray){

        Rmax = rmax;
        Rmin = rmin;
        R = radio;
        vdH = vdHumans;
        vdZ = vdZombies;
        humans = humansArray;
        zombies = zombiesArray;
        deltaT = Rmin / (2*(vdH)); //TODO chequear valor
        t = 0;
        //while () {

            //agregar a frames

            //fijarse si la transformacion de humano a zombie termino
            //verifyTransformations();

            //fijarse si un zombie está en contacto con un humano
            //y empezar la transformacion del humano
            //poner en la cola y sacar al zombie y human de los arrays
            //verifyZombieContact();

            //itera y encuentra contactos, nuevos radios y calcular velocidades y objetivos
            humans(humans);
            zombies(zombies);

            moveZombies();
            moveHumans();
            t += deltaT;
        //}
    }


    public static void humans(ArrayList<Particle> humans){
        Map<Particle, Particle> contacts = calculateDistance(humans);
        List<Particle> wallContacts = calculateWalls(humans);
        for (Particle human : humans){

            //si un humano choca con otro
            if(contacts.containsKey(human)){
                human.setRadio(Rmin);
                contacts.get(human).setRadio(Rmin); //con el que choca
            }
            //si choca con una pared
            else if(wallContacts.contains(human)){
                human.setRadio(Rmin);
            }
            //si no choco contra nada, nuevo radio
            else if(human.getRadio() < Rmax - EPSILON){
                human.setRadio(human.getRadio() + Rmax/(TAU/deltaT));
                if(human.getRadio() > Rmax){
                    human.setRadio(Rmax);
                }
            }
            // si choco con una persona que escape
            if(contacts.containsKey(human)){
                Particle contactHuman = contacts.get(human);
                velocityEscape(human, contactHuman.getX(), contactHuman.getY(), false);
            }
            // si choco con una pared
            else if (wallContacts.contains(human)){
                //TODO chequear todo lo que tenga que ver con la pared
            }
            // nuevo target, me fijo los humanos, zombies y paredes cerca
            else{
                Particle nextHuman = nextHuman(human,true);
                Particle nextZombie = nextZombie(human, true);
                List<Double> wall = nextWall(human, true);
                newHumanTarget(human,nextHuman,nextZombie,wall);
            }
        }

    }

    public static void zombies(ArrayList<Particle> zombies) {
        Map<Particle, Particle> contacts = calculateDistanceZombies(zombies);
        List<Particle> wallContacts = calculateWalls(zombies);
        for (Particle zombie : zombies) {

            //si un humano choca con otro
            if (contacts.containsKey(zombie)) {
                zombie.setRadio(Rmin);
                contacts.get(zombie).setRadio(Rmin); //con el que choca
            }
            //si choca con una pared
            else if (wallContacts.contains(zombie)) {
                zombie.setRadio(Rmin);
            }
            //si no choco contra nada, nuevo radio
            else if (zombie.getRadio() < Rmax - EPSILON) {
                zombie.setRadio(zombie.getRadio() + Rmax / (TAU / deltaT));
                if (zombie.getRadio() > Rmax) {
                    zombie.setRadio(Rmax);
                }
            }
            // si choco con una persona que escape
            if (contacts.containsKey(zombie)) {
                Particle contactZombie = contacts.get(zombie);
                velocityEscape(zombie, contactZombie.getX(), contactZombie.getY(), true);
            }
            // si choco con una pared
            else if (wallContacts.contains(zombie)) {
                //TODO chequear todo lo que tenga que ver con la pared
            }
            // nuevo target, me fijo los humanos, zombies y paredes cerca
            else {
                Particle nextHuman = nextHuman(zombie, false);
                newZombieTarget(zombie, nextHuman);
            }
        }
    }

    public static void newZombieTarget(Particle zombie, Particle nextHuman){
        double directionX;
        double directionY;
        if(nextHuman != null){ //si hay humano objetivo
            directionX = nextHuman.getX() - zombie.getX();
            directionY = nextHuman.getY() - zombie.getY();
            double norma = getNorm(directionX, directionY);
            directionX = directionX / norma;
            directionY = directionY / norma;

            zombie.setVx((directionX * vdZ * Math.pow((zombie.getRadio() - Rmin) / (Rmax - Rmin), beta)));
            zombie.setVy((directionY * vdZ * Math.pow((zombie.getRadio() - Rmin) / (Rmax - Rmin), beta)));
        }
        else{
        //si no hay otro humano, deambula hacia objetivo random con vzi
            double angle = Math.atan2(zombie.getVy(), zombie.getVx());
            //pag 51 teorica 6 calculo Vd
            zombie.setVx(vzi * Math.cos(angle) * Math.pow((zombie.getRadio() - Rmin) / (Rmax - Rmin), beta));
            zombie.setVy(vzi * Math.sin(angle) * Math.pow((zombie.getRadio() - Rmin) / (Rmax - Rmin), beta));
        }
    }


    public static Map<Particle,Particle> calculateDistanceZombies(ArrayList<Particle> zombies){
        Map<Particle,Particle> contacts = new HashMap<>();
        double distance;

        ArrayList<Particle> allZombies = new ArrayList<>(zombies); //TODO chequear que funcione
        for(int i = 0; i < allZombies.size(); i++){
            for(int j = i+1; j < allZombies.size(); j++){
                Particle zombie1 = allZombies.get(i);
                Particle zombie2 = allZombies.get(j);
                distance = Math.sqrt(Math.pow(zombie2.getX() - zombie1.getX(),2) + Math.pow(zombie2.getY() - zombie1.getY(),2)) - (zombie1.getRadio() + zombie2.getRadio());
                if(distance <= EPSILON){
                    contacts.put(zombie1,zombie2);
                    break; //??
                }
            }
        }
        return contacts;
    }


    public static Map<Particle,Particle> calculateDistanceHumans(ArrayList<Particle> humans){
        Map<Particle,Particle> contacts = new HashMap<>();
        double distance;

        ArrayList<Particle> allHumans = new ArrayList<>(humans); //TODO chequear que funcione
        for(int i = 0; i < allHumans.size(); i++){
            for(int j = i+1; j < allHumans.size(); j++){
                    Particle human1 = allHumans.get(i);
                    Particle human2 = allHumans.get(j);
                    distance = Math.sqrt(Math.pow(human2.getX() - human1.getX(),2) + Math.pow(human2.getY() - human1.getY(),2)) - (human1.getRadio() + human2.getRadio());
                    if(distance <= EPSILON){
                        contacts.put(human1,human2);
                        break; //??
                    }
            }
        }
        return contacts;
    }

    public static List<Particle> calculateWalls(ArrayList<Particle> humans){ //TODO chequear que ande
        //Map<Particle, List<Double>> wallsContacts = new HashMap<>();
        List<Particle> wallsContacts = new ArrayList<>();
        double distance;
        double hx, hy;

        for(Particle human : humans){
            hx = human.getX();
            hy = human.getY();

            //Centro circulo (0,0)
            distance = Math.sqrt(Math.pow(hx,2)+Math.pow(hy,2));
            //choca si R-1 < radioHuman
            if(R-distance < human.getRadio()){
                //wallsContacts.put(human,)
                wallsContacts.add(human);
            }

        }

        return wallsContacts;
    }

    public static Particle nextZombie(Particle human, boolean isHuman) {
        double minDist = Integer.MAX_VALUE;
        Particle nextZombie = null;
        //TODO cambiar
        ArrayList<Particle> allZombies = new ArrayList<>(zombies); //TODO chequear que funcione

        double distance;
        for (Particle zombie : allZombies){
            distance = Math.sqrt(Math.pow(human.getX() - zombie.getX(), 2) + Math.pow(human.getY() - zombie.getY(),2)) - (human.getRadio()+zombie.getRadio());

            if(isHuman){
                if (distance <= HUMAN_VISION && distance < minDist && !zombie.equals(human)){
                    minDist = distance;
                    nextZombie = zombie;
                }
            } else {
                if (distance <= ZOMBIE_VISION && distance < minDist && !zombie.equals(human)){
                    minDist = distance;
                    nextZombie = zombie;
                }
            }
        }
        return nextZombie;
    }

    public static Particle nextHuman(Particle zombie, boolean isHuman) {
        double minDist = Integer.MAX_VALUE;
        Particle nextHuman = null;
        //TODO cambiar
        ArrayList<Particle> allHumans = new ArrayList<>(humans); //TODO chequear que funcione

        double distance;
        for (Particle human : allHumans){
            distance = Math.sqrt(Math.pow(zombie.getX() - human.getX(), 2) + Math.pow(zombie.getY() - human.getY(),2)) - (zombie.getRadio()+human.getRadio());

            if(isHuman){
                if (distance < minDist && !human.equals(zombie)){
                    minDist = distance;
                    nextHuman = human;
                }
            } else {
                if (distance <= ZOMBIE_VISION && distance < minDist && !human.equals(zombie)){
                    minDist = distance;
                    nextHuman = human;
                }
            }
        }
        return nextHuman;
    }

    public static List<Double> nextWall(Particle particle, boolean isHuman) {
        double closestWallX;
        double closestWallY;

        // Centro circulo (0,0)
        double h = Math.sqrt(particle.getX()*particle.getX() + particle.getY()*particle.getY());
        if (h == 0) {
            closestWallX = 0;
            closestWallY = R;
        } else {
            closestWallX = particle.getX() / h * R;
            closestWallY = particle.getY() / h * R;
        }

        if (!isHuman && h > ZOMBIE_VISION)
            return null;

        return Arrays.asList(closestWallX,closestWallY);
    }

//    public static void verifyTransformations(){
//        boolean stop = false;
//        double humanAngle;
//        double zombieAngle;
//
//        while (!stop){
//            //k:12 v: ArrayList[human,zombie],[human,zombie]
//            //12
//            //12
//            TransformingAction closest = transformingActions.peek();
//            if (closest != null && (t - EPSILON) > closest.getTimestamp()){
//               // Transformation done
//                Particle human = closest.getHuman();
//                humanAngle = Math.toRadians(Math.random() * 360);
//                human.setRadio(Rmin);
//                human.setVx(0.0000001 * humanAngle);
//                human.setVy(0.0000001 * humanAngle);
//                zombies.add(human);
//
//                zombieAngle = Math.toRadians(Math.random() * 360);
//                closest.getZombie().setR(Rmin);
//                closest.getZombie().setVx(0.0000001 * zombieAngle);
//                closest.getZombie().setVy(0.0000001 * zombieAngle);
//                zombies.add(closest.getZombie());
//
//                transformingActions.remove();
//            } else
//                stop = true;
//        }
//    }

    private static void moveZombies() {
        for (Particle zombie : zombies) {
            zombie.setX(zombie.getX() + zombie.getVx() * deltaT);
            zombie.setY(zombie.getY() + zombie.getVy() * deltaT);
        }
    }

    private static void moveHumans(){
        for(Particle human : humans) {
            human.setX(human.getX() + human.getVx() * deltaT);
            human.setY(human.getY() + human.getVy() * deltaT);
        }
    }

    public static double getNorm(double x, double y){
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }
}
