package methods;

import java.util.ArrayList;

public class VenusMission {

    private static double dEstacionEspacial = 1500;
    private static double vDespegue = 8;
    private static double vOrbitalEstacion = 7.12;
    private static double mNave = 200000;

    private static final ArrayList<Planet> planetsArray = new ArrayList<>();

    public static void main(String[] args) {
        Parser.ParseParameters(args[0], planetsArray);
        System.out.println(planetsArray);
        boolean arrives = arrivesToVenus();
    }

    public static boolean arrivesToVenus() {
        double distanciaNave = dEstacionEspacial + planetsArray.get(1).getR();
        double velocidadTierra = Math.sqrt(Math.pow(planetsArray.get(1).getVx(),2) + Math.pow(planetsArray.get(1).getVy(),2) );

        double velocidadNave = vDespegue + velocidadTierra + vOrbitalEstacion;

        return true;
    }
}
