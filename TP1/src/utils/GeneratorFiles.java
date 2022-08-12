package utils;

import java.io.FileWriter;
import java.io.IOException;

public class GeneratorFiles {

    public static int N = 1000;
    public static int L = 20;

    public static void main(String[] args) {
        try {
            FileWriter myWriter = new FileWriter("src/utils/dynamic.txt");
            myWriter.write("   0\n");
            for(int i=0; i < N;i++){
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
            FileWriter myWriter = new FileWriter("src/utils/static.txt");
            myWriter.write("   1000\n");
            myWriter.write("   20\n");
            for(int i=0; i < N;i++){
                double randomX = 0.2500;
                double randomY = 1.0000;
                myWriter.write("   " + randomX + "   " + randomY + "\n");
            }
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
