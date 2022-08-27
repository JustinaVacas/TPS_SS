package utils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeneratorFiles {

    public static int N = 300;
    public static int L = 7;
    public static double n = 0.5;
    public static double v = 0.03;

    public static void main(String[] args) {
        try {
            FileWriter myWriter = new FileWriter("./dynamic1_4.txt");
            myWriter.write("   0\n");
            for(int i=0; i < N; i++){
                double randomX = ((Math.random() * (L)) + 0);
                double randomY = ((Math.random() * (L)) + 0);
                myWriter.write("   " + randomX + "   " + randomY + "\n");
            }
            myWriter.close();
            System.out.println("Successfully wrote dynamic1_4.txt");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        try {
            final Random random = new Random();
            FileWriter myWriter = new FileWriter("./static1_4.txt");
            myWriter.write("   " + N + "\n");
            myWriter.write("   " + L + "\n");
            myWriter.write("   " + n + "\n");
            for(int i = 0; i < N; i++){
                double ratio = 0.1;
                double velocity = v;
                double angle = random.nextDouble() * (2 * Math.PI);
                myWriter.write("   " + ratio + "   " + velocity + "   " + angle + "\n");
            }
            myWriter.close();
            System.out.println("Successfully wrote static1_4.txt");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void output(List<Double> orders){
        try {
            FileWriter myWriter = new FileWriter("./orders1_4.txt");
            for (Double order: orders) {
                myWriter.write(String.valueOf(order)+'\n');
            }
            myWriter.close();
            System.out.println("Successfully wrote orders1_4.txt");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void outputFrames(List<ArrayList<Double>> frames){
        try {
            FileWriter myWriter = new FileWriter("./frames1_4.txt");
            int frameNum = 0;
            int count = 0;
            for (ArrayList<Double> frame : frames) {
                if(count == 0 || count == 300){
                    myWriter.write(String.valueOf(frameNum)+'\n');
                    frameNum++;
                    count = 0;
                }
                Double d = frame.get(0);
                Integer id = d.intValue();
                myWriter.write( id + "\t" + frame.get(1) + "\t" + frame.get(2)  + "\t" + "0.0" + "\t" + frame.get(4) + "\t" + frame.get(3) + "\t" + frame.get(5) +"\n");
                count++;

            }
            myWriter.close();
            System.out.println("Successfully wrote frames1_4.txt.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
