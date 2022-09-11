package methods;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Parser {
    public static void ParseParameters(String static100, String dynamic100, ArrayList<Particle> birdsArray){
        // STATIC
        try {
            File staticFile = new File(static100);
            Scanner myReaderStatic = new Scanner(staticFile);

            // guardo N
            if (myReaderStatic.hasNextLine()) {
                String data = myReaderStatic.nextLine();
                data = data.replaceAll(" ", "");
                Simulator.N = Integer.parseInt(data);
            }

            // guardo L
            if (myReaderStatic.hasNextLine()) {
                String data = myReaderStatic.nextLine();
                data = data.replaceAll(" ", "");
                Simulator.L = Integer.parseInt(data);
            }

            // guardo el resto
            int id = 0;
            while (myReaderStatic.hasNextLine()) {
                String data = myReaderStatic.nextLine();
                data = data.trim().replaceAll("\\s+", " ");
                String[] dataStatic = data.split(" ");
                double radio = Double.parseDouble(dataStatic[0]);
                double velocidad = Double.parseDouble(dataStatic[1]);
                double masa = Double.parseDouble(dataStatic[2]);
                double angle = Double.parseDouble(dataStatic[3]);
                double vx = Math.cos(angle)*velocidad;
                double vy = Math.sin(angle)*velocidad;
                Particle particle = new Particle(id,radio,vx,vy,masa,angle);
                Simulator.particlesArray.add(particle);
                id++;

            }
            myReaderStatic.close();

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        // DYNAMIC
        try {
            File dynamicFile = new File(dynamic100);
            Scanner myReaderDynamic = new Scanner(dynamicFile);

            // guardo t0
            if (myReaderDynamic.hasNextLine()) {
                String data = myReaderDynamic.nextLine();
                data = data.replaceAll(" ", "");
                Simulator.time = Integer.parseInt(data);
            }

            // guardo el resto
            int id = 0;
            while (myReaderDynamic.hasNextLine()) {
                String data = myReaderDynamic.nextLine();
                data = data.trim().replaceAll("\\s+", " ");
                String[] dataStatic = data.split(" ");
                double x = Double.parseDouble(dataStatic[0]);
                double y = Double.parseDouble(dataStatic[1]);
                Particle particle = birdsArray.get(id);
                particle.setX(x);
                particle.setY(y);
                id++;

            }
            myReaderDynamic.close();

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

}
