package utilsNasa;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GeneratorFiles {
    public static void output(List<ArrayList<Double>> frames){
        try {
            FileWriter myWriter = new FileWriter("./states26.txt");
            int count = 0;
            for (ArrayList<Double> frame : frames) {
                if(count == 4|| count == 0){
                    myWriter.write(frame.get(0) + "\n");
                    count = 0;
                }
                myWriter.write(frame.get(1) + "\t" + frame.get(2) + "\t" + frame.get(3) + "\t" + frame.get(4) + "\t" + frame.get(5) + "\n");
                count++;
            }
            myWriter.close();
            System.out.println("Successfully wrote states26.txt.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

  /*  public static void output2(List<ArrayList<Double>> frames){
        try {
            FileWriter myWriter = new FileWriter("./states2.txt");
            for (ArrayList<Double> frame : frames) {
                myWriter.write(frame.get(0) + "\t" + frame.get(1) + "\t" + frame.get(2) + "\t" + frame.get(3) + "\t" + frame.get(4) + "\n");
            }
            myWriter.close();
            System.out.println("Successfully wrote states2.txt.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    } */
}
