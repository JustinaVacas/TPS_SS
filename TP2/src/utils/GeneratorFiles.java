package utils;

import methods.Particle;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeneratorFiles {

    public static int N = 300;
    public static int L = 7;
    public static double n = 2;
    public static double v = 0.03;

    public static void main(String[] args) {
        try {
            FileWriter myWriter = new FileWriter("./dynamic.txt");
            myWriter.write("   0\n");
            for(int i=0; i < N; i++){
                double randomX = ((Math.random() * (L)) + 0);
                double randomY = ((Math.random() * (L)) + 0);
                myWriter.write("   " + randomX + "   " + randomY + "\n");
            }
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        try {
            final Random random = new Random();
            FileWriter myWriter = new FileWriter("./static.txt");
            myWriter.write("   " + N + "\n");
            myWriter.write("   " + L + "\n");
            myWriter.write("   " + n + "\n");
            for(int i = 0; i < N; i++){
                double ratio = 1.000;
                double velocity = v;
                double angle = random.nextDouble() * (2 * Math.PI);
                myWriter.write("   " + ratio + "   " + velocity + "   " + angle + "\n");
            }
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void output(List<Double> orders){
        try {
            FileWriter myWriter = new FileWriter("./orders.txt");
            for (Double order: orders) {
                myWriter.write(String.valueOf(order)+'\n');
            }
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
