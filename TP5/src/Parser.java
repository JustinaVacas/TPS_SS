import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Parser {

    public static void ParseParameters(String staticF, String dynamicF, ArrayList<Particle> humans,ArrayList<Particle> zombies) {
        // DYNAMIC
        try {
            File dynamicFile = new File(dynamicF);
            Scanner myReaderDynamic = new Scanner(dynamicFile);

            //guardar info zombie
            String dataZ = myReaderDynamic.nextLine();
            dataZ = dataZ.trim().replaceAll("\\s+", " ");
            String[] dataDynamic = dataZ.split(" ");
            int zid = Integer.parseInt(dataDynamic[0]);
            double zx = Double.parseDouble(dataDynamic[1]);
            double zy = Double.parseDouble(dataDynamic[2]);
            double zvx = Double.parseDouble(dataDynamic[3]);
            double zvy = Double.parseDouble(dataDynamic[4]);
            Particle zombie = new Particle(zid,zx,zy,zvx,zvy);
            zombies.add(zombie);

            while (myReaderDynamic.hasNextLine()) {
                String data = myReaderDynamic.nextLine();
                data = data.trim().replaceAll("\\s+", " ");
                String[] dataStatic = data.split(" ");
                int id = Integer.parseInt(dataStatic[0]);
                double x = Double.parseDouble(dataStatic[1]);
                double y = Double.parseDouble(dataStatic[2]);
                double vx = Double.parseDouble(dataStatic[3]);
                double vy = Double.parseDouble(dataStatic[4]);
                Particle human = new Particle(id,x,y,vx,vy);
                humans.add(human);
            }

            myReaderDynamic.close();

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        // STATIC
        try {
            File staticFile = new File(staticF);
            Scanner myReaderStatic = new Scanner(staticFile);

            // guardo primera linea
            if (myReaderStatic.hasNextLine()) {
                String data = myReaderStatic.nextLine();
                data = data.trim().replaceAll("\\s+", " ");
                String[] dataStatic = data.split(" ");
                Simulator.Nh = Integer.parseInt(dataStatic[0]) - 1 ;
                Simulator.Rmax = Double.parseDouble(dataStatic[1]);
                Simulator.R = Double.parseDouble(dataStatic[2]);
            }

            // guardo los radios para zombies
            String zdata = myReaderStatic.nextLine();
            zdata = zdata.trim().replaceAll("\\s+", " ");
            String[] zdataStatic = zdata.split(" ");
            int zid = Integer.parseInt(zdataStatic[0]);
            double zradio = Double.parseDouble(zdataStatic[1]);
            Particle zombie = zombies.get(zid);
            zombie.setRadio(zradio);

            Simulator.Rmin = zradio;


            // guardo los radios para humanos
            while (myReaderStatic.hasNextLine()) {
                String data = myReaderStatic.nextLine();
                data = data.trim().replaceAll("\\s+", " ");
                String[] dataStatic = data.split(" ");
                int id = Integer.parseInt(dataStatic[0]);
                double radio = Double.parseDouble(dataStatic[1]);
                for (Particle human: humans) {
                    if(human.getId() == id)
                        human.setRadio(radio);
                }
            }
            myReaderStatic.close();

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }

}
