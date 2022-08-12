package utils;

import java.io.FileWriter;
import java.io.IOException;

public class GeneratorFiles {

    public static int N = 100;
    public static int L = 20;

    public static void main(String[] args) {
        try {
            FileWriter myWriter = new FileWriter("dynamic.txt");
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
    }
}
