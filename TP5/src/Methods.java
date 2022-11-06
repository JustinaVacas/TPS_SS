import java.util.ArrayList;
import java.util.Map;

public class Methods {

    public static void moveZombies(ArrayList<Particle> zombies, double deltaT) {
        for (Particle zombie : zombies) {
            zombie.setX(zombie.getX() + zombie.getVx() * deltaT);
            zombie.setY(zombie.getY() + zombie.getVy() * deltaT);
        }
    }

    public static void moveHumans(ArrayList<Particle> humans, double deltaT){
        for(Particle human : humans) {
            human.setX(human.getX() + human.getVx() * deltaT);
            human.setY(human.getY() + human.getVy() * deltaT);
        }
    }

    public static double getNorm(double x, double y){
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    public static ArrayList<Particle> getList(int i, Map<Double,ArrayList<Particle>> converting){
        ArrayList<Particle> list = new ArrayList<>();
        if(converting!=null) {
            for (ArrayList<Particle> pair : converting.values()) {
                list.add(pair.get(i));
            }
            return list;
        }
        else
            return new ArrayList<>();
    }
}
