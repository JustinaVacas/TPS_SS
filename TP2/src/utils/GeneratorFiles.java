package utils;

import methods.Particle;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import static java.lang.Math.*;

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

    public static void outputFrames(List<ArrayList<Double>> frames){
        try {
            FileWriter myWriter = new FileWriter("./frames.txt");
            int frameNum = 0;
            int count = 0;
            for (ArrayList<Double> frame : frames) {
                if(count == 0 || count == 300){
                    myWriter.write(String.valueOf(N)+'\n');
                    myWriter.write(String.valueOf(frameNum)+'\n');
                    frameNum++;
                    count = 0;
                }
                double vx = cos(frame.get(3)) * frame.get(4);
                double vy = sin(frame.get(3)) * frame.get(4);
                myWriter.write(frame.get(0) + "\t" + frame.get(1) + "\t" + frame.get(2)  + "\t" + "0.0" + "\t" + vx + "\t" + vy + "\n");
                count++;

            }
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
