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
                double randomX = r + ((width/2 - 2*r) * Math.random());
                double randomY = r + ((height - 2*r) * Math.random());
                boolean flag = false;
                for(Pair particle : particles){
                    double dist = Math.pow(randomX - (double) particle.getKey(),2) + Math.pow(randomY - (double) particle.getValue(), 2);
                    if(dist <= Math.pow(r + r,2)*1.2){
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



    public static int getN() {
        return N;
    }

    public static int getL() {
        return L;
    }

    public static double getWidth() {
        return width;
    }

    public static double getHeight() {
        return height;
    }

    public static double getR() {
        return r;
    }

    public static double getM() {
        return m;
    }

    public static double getV() {
        return v;
    }

    public static List<Pair<Double, Double>> getParticles() {
        return particles;
    }

    public static void outputFrames(List<ArrayList<Double>> frames){
        //x y vx vy
        try {
            FileWriter myWriter = new FileWriter("./frames.txt");
            int frameNum = 0;
            int count = 0;
            for (ArrayList<Double> frame : frames) {
                if(frame.size() == 0){
                    continue;
                }
                else if(frame.size() == 1){
                    myWriter.write(String.format("%g\n", frame.get(0)));
//                    if(frame.get(1)==1){   //choco pared
//                        Double d1 = frame.get(2);
//                        Integer id1 = d1.intValue();
//                        Double d2 = frame.get(3);
//                        Integer id2 = d2.intValue();
//                        myWriter.write(String.format("%s\t%d\t%d\n\n", "WC", id1, id2));
//                    }
//                    else{   //choco particula
//                        Double d1 = frame.get(2);
//                        Integer id1 = d1.intValue();
//                        Double d2 = frame.get(3);
//                        Integer id2 = d2.intValue();
//                        myWriter.write(String.format("%s\t%d\t%d\n\n", "PC", id1, id2));
//                    }
                    continue;

                } else {
                    Double d = frame.get(0);
                    Integer id = d.intValue();
                    myWriter.write(id + "\t" + frame.get(1) + "\t" + frame.get(2) + "\t" + frame.get(3) + "\t" + frame.get(4) + "\t" + 0.0015 + "\n");
                }
                count++;
            }
            myWriter.close();
            System.out.println("Successfully wrote frames.txt.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

}
