import javafx.util.Pair;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

}
