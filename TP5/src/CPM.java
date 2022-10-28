import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CPM {

    private static double EPSILON = 1e-10;
    private static double TAU = 0.5;
    private static double deltaT;
    public static double vdH;
    public static double vdZ;
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

        //while () {

            //agregar a frames

            //fijarse si la transformacion de humano a zombie termino
            //verifyTransformations();

            //fijarse si un zombie esta en contacto con un humano
            //y empezar la transformacion del humano
            //poner en la cola y sacar al zombie y human de los arrays
            //verifyZombieContact();

            //itera y encuentra contactos, nuevos radios y calcular velocidades y objetivos
            humans(humans);
            zombies(zombies);
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
          /*  else if(wallContacts.containsKey(human)){
                human.setRadio(Rmin);
            } */
            //si choca con una pared
            else if(wallContacts.contains(human)){
                human.setRadio(Rmin);
            }
            //si no choco contra nada, nuevo radio
            else if(human.getRadio() < Rmax - EPSILON){
                human.setRadio(human.getRadio() + Rmax/(TAU/deltaT));
                if(human.getRadio() < Rmax){
                    human.setRadio(Rmax);
                }
            }
        }

    }

    public static void zombies(ArrayList<Particle> zombies){

    }

    public static Map<Particle,Particle> calculateDistance(ArrayList<Particle> humans){
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



}
