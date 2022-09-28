package methods;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Parser {

    public static void ParseParameters(String planets, ArrayList<Planet> planetsArray){
        // PLANETS
        try {
            File planetsFile = new File(planets);
            Scanner myReaderPlanets = new Scanner(planetsFile);

            int id = 0;
            while (myReaderPlanets.hasNextLine()) {
                String data = myReaderPlanets.nextLine();
                data = data.trim().replaceAll("\\s+", " ");
                String[] dataPlanets = data.split(" ");
                if(dataPlanets.length == 2 ){
                    Planet planet = new Planet(id, Double.parseDouble(dataPlanets[0]), Double.parseDouble(dataPlanets[1]));
                    planetsArray.add(planet);
                } else if (dataPlanets.length == 4) {
                    Planet planet = planetsArray.get(id);
                    planet.setX(Double.parseDouble(dataPlanets[0]));
                    planet.setY(Double.parseDouble(dataPlanets[1]));
                    planet.setVx(Double.parseDouble(dataPlanets[2]));
                    planet.setVy(Double.parseDouble(dataPlanets[3]));
                    id++;
                }
            }
            myReaderPlanets.close();

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
