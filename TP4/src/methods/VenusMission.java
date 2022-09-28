package methods;

import java.util.ArrayList;

public class VenusMission {

    private static double estacionEspacial = 1500;
    private static double vNave = 8;
    private static double vOrbital = 7.12;
    private static final ArrayList<Planet> planetsArray = new ArrayList<>();

    public static void main(String[] args) {
        Parser.ParseParameters(args[0], planetsArray);
        System.out.println(planetsArray);

        double distancia = estacionEspacial + planetsArray.get(1).getR();
        double velocidadTierra = Math.sqrt(Math.pow(planetsArray.get(1).getVx(),2) + Math.pow(planetsArray.get(1).getVy(),2) );
        double velocidad = vNave + velocidadTierra + vOrbital;

    }
}
