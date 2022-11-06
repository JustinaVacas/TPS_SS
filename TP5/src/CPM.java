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
    private static final double conversionTime = 7;
    private static final double auxV = 0.0000001;
    private static double deltaT;
    public static double vdH;
    public static double vdZ;
    public static double vzi = 0.3;
    private static double t;
    private static double Nh;
    private static double fractionZ;

    public static double Rmax;
    public static double Rmin;
    public static double R;
    public static ArrayList<Particle> humans;
    public static ArrayList<Particle> zombies;

    public static Map<Double,ArrayList<Particle>> converting = new HashMap<>();
    public static Queue<Double> times = new LinkedList<>();
    //cola tiempo
    //k:tiempo v:ArrayList<ArrayList<particle>>

    public static void run(double rmax, double rmin,double radio, double vdHumans, double vdZombies, ArrayList<Particle> humansArray,ArrayList<Particle> zombiesArray){

        Rmax = rmax;
        Rmin = rmin;
        R = radio;
        Nh = humansArray.size();
        vdH = vdHumans;
        vdZ = vdZombies;
        humans = humansArray;
        zombies = zombiesArray;
        deltaT = Rmin / (2*(vdH)); //TODO chequear valor
        t = 0;
        double fractionZ = 0.0001;
        GeneratorFiles.generateFile();
        GeneratorFiles.generateFractionFile();
        //corte por cantidad de zombies o tiempo
        while(t < 300) {
//        while(fractionZ != 1) {

            //agregar a frames
            GeneratorFiles.outputStates(t, humans, zombies, converting);

            //fijarse si la conversion de humano a zombie termino
            if(times!=null) {
                verifyConversion();
            }

            //fijarse si un zombie está en contacto con un humano
            //y empezar la transformacion del humano
            //poner en la cola y sacar al zombie y human de los arrays
            zombieAttack();

            //itera y encuentra contactos, nuevos radios y calcular velocidades y objetivos
            humans(humans);
            zombies(zombies);

            Methods.moveZombies(zombies, deltaT);
            Methods.moveHumans(humans, deltaT);
            t += deltaT;

            double fz = zombies.size() + converting.size();
            double total = Nh + 1 ;
            fractionZ = fz/total;
            GeneratorFiles.fraction(t, fz, total);
        }

    }


    public static void humans(ArrayList<Particle> humans){
        Map<Particle, Particle> contacts = calculateDistanceHumans(humans);
        Map<Particle,List<Double>> wallContacts = calculateWalls(humans);
        for (Particle human : humans){

            //si un humano choca con otro
            if(contacts.containsKey(human)){
                human.setRadio(Rmin);
                contacts.get(human).setRadio(Rmin); //con el que choca
            }
            //si choca con una pared
            else if(wallContacts.containsKey(human)){
                human.setRadio(Rmin);
            }
            //si no choco contra nada, nuevo radio
            else if(human.getRadio() < (Rmax - EPSILON)){
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
            else if (wallContacts.containsKey(human)){
                List<Double> wallPos = wallContacts.get(human);
                velocityEscape(human, wallPos.get(0), wallPos.get(1), false);
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

    public static void newHumanTarget(Particle human, Particle nextHuman, Particle nextZombie, List<Double> nextWall){
        if(nextZombie == null){
            human.setVx(0);
            human.setVy(0);
            return;
        }

        // escapa de un humano
        double[] awayFromHumanDir = {0, 0};
        if (nextHuman != null){
            awayFromHumanDir = calculateEscape(human, nextHuman.getX(), nextHuman.getY(), nextHuman.getRadio(),false);
        }
        // escapa de una pared
        double[] awayFromWallDir = {0, 0};
        if (nextWall != null){
            awayFromWallDir = calculateEscape(human, nextWall.get(0),nextWall.get(1),0,false);
        }
        // escapa de un zombie
        double[] zombieEscape = calculateEscape(human, nextZombie.getX(),nextZombie.getY(), nextZombie.getRadio(),true);

        // elegir segun heuristica
        double sumXdirection = zombieEscape[0] + awayFromHumanDir[0] + awayFromWallDir[0];
        double sumYdirection = zombieEscape[1] + awayFromHumanDir[1] + awayFromWallDir[1];
        double norma = Methods.getNorm(sumXdirection, sumYdirection);
        //normalizo
        sumXdirection = sumXdirection / norma;
        sumYdirection = sumYdirection / norma;
        human.setVx((sumXdirection * vdH * Math.pow((human.getRadio() - Rmin) / (Rmax - Rmin), beta)));
        human.setVy((sumYdirection * vdH * Math.pow((human.getRadio() - Rmin) / (Rmax - Rmin), beta)));

    }

    private static double[] calculateEscape(Particle human, double x2, double y2, double r2, boolean isZombie){
        // isZombie lo que estoy escapando
        double x1 = human.getX();
        double y1 = human.getY();
        double r1 = human.getRadio();
        double distance = Math.sqrt(Math.pow(x1 - x2,2) + Math.pow(y1 - y2,2)) - (r1 + r2);
        double newAp = Ap;
        double newBp = Bp;
        if(isZombie){
            newAp = ApZ;
            newBp = BpZ;
        }
        double aux = newAp * Math.exp(-distance / newBp);
        double eX = (x1 - x2) / distance;
        double eY = (y1 - y2) / distance;
        return new double[]{eX * aux, eY * aux};
    }

    public static void zombies(ArrayList<Particle> zombies) {
        Map<Particle, Particle> contacts = calculateDistanceZombies(zombies);
        Map<Particle,List<Double>> wallContacts = calculateWalls(zombies);
        for (Particle zombie : zombies) {

            //si un zombie choca con otro
            if (contacts.containsKey(zombie)) {
                zombie.setRadio(Rmin);
                contacts.get(zombie).setRadio(Rmin); //con el que choca
            }
            //si choca con una pared
            else if (wallContacts.containsKey(zombie)) {
                zombie.setRadio(Rmin);
            }
            //si no choco contra nada, nuevo radio
            else if (zombie.getRadio() < Rmax - EPSILON) {
                zombie.setRadio(zombie.getRadio() + Rmax / (TAU / deltaT));
                if (zombie.getRadio() > Rmax) {
                    zombie.setRadio(Rmax);
                }
            }
            // si choco con una pared que eluda
            if(wallContacts.containsKey(zombie)) {
                List<Double> wallPos = wallContacts.get(zombie);
                velocityEscape(zombie, wallPos.get(0), wallPos.get(1), true);

            } //si choca
            else if (contacts.containsKey(zombie)) {
                Particle contactZombie = contacts.get(zombie);
                velocityEscape(zombie, contactZombie.getX(), contactZombie.getY(), true);
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
            double norma = Methods.getNorm(directionX, directionY);
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

        ArrayList<Particle> humansList = Methods.getList(0,converting);
        ArrayList<Particle> zombiesList = Methods.getList(1,converting);
        ArrayList<Particle> allZombies = new ArrayList<>(zombies);
        //System.out.println("size zombie antes: "+ allZombies.size());
        allZombies.addAll(humansList);
        allZombies.addAll(zombiesList);
        //System.out.println("size zombie dps: "+ allZombies.size());


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

        ArrayList<Particle> humansList = Methods.getList(0,converting);
        ArrayList<Particle> zombiesList = Methods.getList(1,converting);
        ArrayList<Particle> allHumans = new ArrayList<>(humans);
       // System.out.println("size antes: " + allHumans.size());
        allHumans.addAll(humansList);
        allHumans.addAll(zombiesList);
        //System.out.println("size dps: " + allHumans.size());


        for(int i = 0; i < allHumans.size(); i++){
            for(int j = i+1; j < allHumans.size(); j++){
                    Particle human1 = allHumans.get(i);
                    Particle human2 = allHumans.get(j);
                    distance = Math.sqrt(Math.pow(human2.getX() - human1.getX(),2) + Math.pow(human2.getY() - human1.getY(),2)) - (human1.getRadio() + human2.getRadio());
                    if(distance <= EPSILON){
                        contacts.put(human1,human2);
                        break;
                    }
            }
        }
        return contacts;
    }

    public static Map<Particle,List<Double>>  calculateWalls(ArrayList<Particle> humans){
        Map<Particle,List<Double>> contactsMap = new HashMap<>();
        double distance;
        double wallX;
        double wallY;
        // Centro circulo (0,0)
        for(Particle particle : humans){
            double norma = Methods.getNorm(particle.getX(), particle.getY());
            wallX = particle.getX() / norma * R;
            wallY = particle.getY() / norma * R;
            distance = Math.sqrt(Math.pow(particle.getX()-wallX,2) + Math.pow(particle.getY()-wallY,2)) - particle.getRadio();
            if(distance <= EPSILON){
                contactsMap.put(particle,Arrays.asList(wallX,wallY));
            }
        }
        return contactsMap;
    }

    public static void velocityEscape(Particle human, double x, double y, boolean isZombie){
        double deltaX = human.getX() - x;
        double deltaY = human.getY() - y;
        //ppt pag 52
        double norm = Methods.getNorm(deltaX, deltaY);
        deltaX = deltaX / norm;
        deltaY = deltaY / norm;
        if (isZombie){
            human.setVx(deltaX * vdZ);
            human.setVy(deltaY * vdZ);
        } else {
            human.setVx(deltaX * vdH);
            human.setVy(deltaY * vdH);
        }
    }


    public static Particle nextZombie(Particle human, boolean isHuman) {
        double minDist = Integer.MAX_VALUE;
        Particle nextZombie = null;

        ArrayList<Particle> convertingZombies = Methods.getList(1,converting);
        ArrayList<Particle> allZombies = new ArrayList<>(zombies);
        //System.out.println("check size: " +  allZombies.size());
        allZombies.addAll(convertingZombies); //no se si la pisa
        //System.out.println("desp del addAll check size: " +  allZombies.size());

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

        double distance;
        for (Particle human : humans){
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

    public static void verifyConversion(){
        boolean flag = false;
        double humanAngle;
        double zombieAngle;

        while (!flag) {
            //k:12 v: ArrayList[human,zombie]
            Double time = times.peek();
            if (time != null && (t - EPSILON) > time) {
                ArrayList<Particle> pair = converting.get(time);
                // Fin de conversion
                if(pair != null) {
                    Particle human = pair.get(0);
                    Particle zombie = pair.get(1);
                    humanAngle = Math.toRadians(Math.random() * 360);
                    zombieAngle = Math.toRadians(Math.random() * 360);
                    human.setRadio(Rmin);
                    zombie.setRadio(Rmin);
                    //deambulan
                    human.setVx(auxV * humanAngle);
                    human.setVy(auxV * humanAngle);
                    zombie.setVx(auxV * zombieAngle);
                    zombie.setVy(auxV * zombieAngle);
                    zombies.add(human);
                    zombies.add(zombie);
                }

                converting.remove(time);
                times.remove();
            }
            else{
                flag = true;
            }
        }
    }

    private static void zombieAttack(){
        Iterator<Particle> iter = zombies.iterator();
        double distance;
        while (iter.hasNext()){
            Particle zombie = iter.next();
            for(int i = 0; i < humans.size(); i++) {
                distance = Math.sqrt(Math.pow(humans.get(i).getX()-zombie.getX(),2)+Math.pow(humans.get(i).getY()-zombie.getY(),2)) - (humans.get(i).getRadio()+zombie.getRadio());
                if (distance <= ZOMBIE_VISION) {
                    //esta dentro de la vision del zombie
                    if (distance <= EPSILON) {
                        //zombie come al humano y se quedan quietos
                        iter.remove();
                        zombie.setVx(0);
                        zombie.setVy(0);
                        humans.get(i).setVx(0);
                        humans.get(i).setVy(0);

                        double key = t + conversionTime;
                        times.add(key);

                        ArrayList<Particle> newPair = new ArrayList<>();
                        newPair.add(humans.get(i));
                        newPair.add(zombie);
                        converting.putIfAbsent(key,newPair);

                        humans.remove(i);
                        break;
                    }
                }
            }
        }
    }

}
