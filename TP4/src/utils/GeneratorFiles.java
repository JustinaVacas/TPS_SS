package utils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GeneratorFiles {

    public static void outputStates(List<ArrayList<Double>> frames){
        try {
            FileWriter myWriter = new FileWriter("./statesBeeman.txt");
            for (ArrayList<Double> frame : frames) {
                myWriter.write(frame.get(0) + "\t" + frame.get(1) + "\t" + frame.get(2) + "\n");
            }
            myWriter.close();
            System.out.println("Successfully wrote statesBeeman.txt.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
