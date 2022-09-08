package utils;

import javafx.util.Pair;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeneratorFiles {

    static int N = 100;
    static int L = 6;
    static double width = 0.24;
    static double height = 0.09;
    static double r = 0.0015;
    static double m = 1;
    static double v = 0.01;
    static List<Pair<Double,Double>> particles = new ArrayList<>();

    public static void main(String[] args) {
        try {
            FileWriter myWriter = new FileWriter("./dynamic.txt");
            myWriter.write("   0\n");

            while(particles.size() != N){
                double randomX = ((Math.random() * (width/2)) + 0);
                double randomY = ((Math.random() * (height)) + 0);
                boolean flag = false;
                for(Pair particle : particles){
                    double dist = Math.pow(randomX - (double) particle.getKey(),2) + Math.pow(randomY - (double) particle.getValue(), 2);
                    if(dist <= Math.pow(r + r,2)){
                        flag = true;
                        break;
                    }
                }
                if(flag){
                    continue;
                }
                particles.add(new Pair<>(randomX, randomY));
                myWriter.write("   " + randomX + "   " + randomY + "\n");
            }

            myWriter.close();
            System.out.println("Successfully wrote to the file dynamic.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        try {
            FileWriter myWriter = new FileWriter("./static.txt");
            //r v m angle
            final Random random = new Random();
            myWriter.write("   " + N +"\n");
            myWriter.write("   " + L + "\n");
            for(int i = 0; i < N; i++){
                double angle = random.nextDouble() * (2 * Math.PI);
                myWriter.write("   " + r + "   " + v + "   " + m + "   " + angle + "\n");
            }
            myWriter.close();
            System.out.println("Successfully wrote to the file static.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

}
