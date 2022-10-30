import javafx.util.Pair;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GeneratorFiles {

    public static int Nh = 80;
    public static double Rmin = 0.1;
    public static double Rmax = 0.3;
    public static double R = 11;
    public static double vzInactivo = 0.3;

    public static void main(String[] args) {
        try {
            FileWriter myWriter = new FileWriter("./dynamic.txt");

            double zAngle = Math.toRadians(Math.random() * 360);
            double zVx = Math.cos(zAngle) * vzInactivo;
            double zVy = Math.sin(zAngle) * vzInactivo;
            //id x y vx vy
            myWriter.write(0 + "    " + 0 + "    " + 0 + "    " + zVx + "    " + zVy +"\n");

            List<Pair<Double,Double>> particles = new ArrayList<>();

            while(particles.size() != Nh){
                double hAngle = Math.toRadians(Math.random() * 360);
                double distance = (Math.random() * ((R - Rmax)- 1)) + 1;
                double hX = distance * Math.cos(hAngle);
                double hY = distance * Math.sin(hAngle) * distance;

                boolean flag = false;
                for(Pair particle : particles){
                    double dist = Math.pow(hX - (double) particle.getKey(),2) + Math.pow(hY - (double) particle.getValue(), 2);
                    if(Math.sqrt(dist) <= 2*Rmax){
                        flag = true;
                        break;
                    }
                }
                if(flag){
                    continue;
                }
                particles.add(new Pair<>(hX, hY));
                myWriter.write( particles.size() + "    " + hX + "    " + hY + "    " + 0 + "    " + 0 +"\n");
            }
            myWriter.close();
            System.out.println("Successfully wrote to the file dynamic.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        try {
            FileWriter myWriter = new FileWriter("./static.txt");
            //cantidad de particulas + radio max + radio del recinto
            myWriter.write(Nh+1 + "    " + Rmax + "    " + R +"\n");
            for(int i = 0; i <= Nh; i++){
                myWriter.write(i + "    " + Rmin + "\n");
            }
            myWriter.close();
            System.out.println("Successfully wrote to the file static.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void generateFile(){
        try {
            FileWriter myWriter = new FileWriter("./frames.txt");
            myWriter.close();
            System.out.println("Successfully wrote frames.txt.");
        }
        catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void outputStates(double t, ArrayList<Particle> humans, ArrayList<Particle> zombies, Map<Double, ArrayList<Particle>> converting){
        //t
        //humans
        //zombies
        //conv
        try {
            FileWriter myWriter = new FileWriter("./frames.txt", true);
            myWriter.write(t + "\n");
            for (Particle human : humans) {
                myWriter.write(human.getX() + "\t" + human.getY() + "\t" + human.getRadio() + 1 + "\t" + 0 + "\t" + 0 + "\n");
            }
            for (Particle zombie : zombies) {
                myWriter.write(zombie.getX() + "\t" + zombie.getY() + "\t" + zombie.getRadio() + 0 + "\t" + 1 + "\t" + 0 + "\n");
            }
            for(ArrayList<Particle> pair : converting.values()){
                myWriter.write(pair.get(0).getX() + "\t" + pair.get(0).getY() + "\t" + pair.get(0).getRadio() + 0 + "\t" + 0 + "\t" + 1 + "\n"); //humano
                myWriter.write(pair.get(1).getX() + "\t" + pair.get(1).getY() + "\t" + pair.get(1).getRadio() + 0 + "\t" + 0 + "\t" + 1 + "\n"); //zombie
            }
            myWriter.close();
            System.out.println("Successfully wrote frames.txt.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

}
