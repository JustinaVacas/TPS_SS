package methods;

import utilsNasa.GeneratorFiles;

import java.util.ArrayList;
import java.util.List;

public class VenusMission {

    private static final Double G = 6.693 * Math.pow(10, -11) / Math.pow(10, 9); //Divido para pasarla a km
    private static final double[] alpha = {3.0/16, 251.0/360, 1, 11.0/18, 1.0/6, 1.0/60};

    private static double dEstacionEspacial = 1500;
    private static double vDespegue = 8;
    private static double vOrbitalEstacion = 7.12;
    private static double mNave = 200000;
    // 0 Sol 1 Tierra 2 Venus
    private static final ArrayList<Planet> planetsArray = new ArrayList<>();
    private static List<List<ArrayList<Double>>> Rs = new ArrayList<>(); // 0 Sol 1 Tierra 2 Venus

    public static void main(String[] args) {
        Parser.ParseParameters(args[0], planetsArray);

        addNave();

        System.out.println("elementos: " + planetsArray);

        Rs = initialRs();

        System.out.println("Rs: " + Rs);

        double dt = 300;
        double tf = 365 * 24 * 60 * 60;
        List<ArrayList<Double>> states = gear(dt, tf);
        GeneratorFiles.output(states);
    }

    public static void addNave(){
        Planet tierra = planetsArray.get(1);
        Planet sol = planetsArray.get(0);

        double d = Math.sqrt((Math.pow((tierra.getX() - sol.getX()), 2) + Math.pow((tierra.getY() - sol.getY()), 2)));
        double rx = (tierra.getX() - sol.getX()) / d; // normal a la orbita
        double ry = (tierra.getY() - sol.getY()) / d;
        double oX = -ry; //componentes tangenciales a las orbitas
        double oY = rx;
        double naveX = (dEstacionEspacial + tierra.getR()) * -rx + tierra.getX();
        double naveY = (dEstacionEspacial + tierra.getR()) * -ry + tierra.getY();
        double vT = -7.12 - 8 + tierra.getVx() * oX + tierra.getVy() * oY;
        double naveVx = oX * vT;
        double naveVy = oY * vT;

        Planet nave = new Planet(3, 100, mNave);
        nave.setX(naveX);
        nave.setY(naveY);
        nave.setVx(naveVx);
        nave.setVy(naveVy);
        planetsArray.add(nave);
    }

    public static List<List<ArrayList<Double>>> initialRs(){
        List<List<ArrayList<Double>>> R = new ArrayList<>();
//       [Sol: [ [r0x r0y] [r1x r1y] ] [ [r0x r0y] [r1x r1y] ], Tierra: [ [r0x r0y] [r1x r1y] ] [ [r0x r0y] [r1x r1y] ]]
        ArrayList<Double> r;
        for (Planet planet: planetsArray) {
            List<ArrayList<Double>> auxR = new ArrayList<>();
            //r0
            r = new ArrayList<>();
            r.add(planet.getX());
            r.add(planet.getY());
            auxR.add(r);
            //r1
            r = new ArrayList<>();
            r.add(planet.getVx());
            r.add(planet.getVy());
            auxR.add(r);
            //r2
            r = new ArrayList<>();
            ArrayList<Double> a = calculateA(planet, true);
            r.add(a.get(0));
            r.add(a.get(1));
            auxR.add(r);
            //r3
            r = new ArrayList<>();
            r.add(0.0);
            r.add(0.0);
            auxR.add(r);
            //r4
            auxR.add(r);
            //r5
            auxR.add(r);

            R.add(auxR);
        }
        return R;
    }

    public static List<ArrayList<Double>> gear(double dT, double tf){

        List<ArrayList<Double>> states = new ArrayList<>();
        double t = 0;

        List<List<ArrayList<Double>>> currentRs = Rs;

        while(t <= tf) {
            // me guardo el estado
            for (Planet p: planetsArray ) {
                ArrayList<Double> state = new ArrayList<>();
                ArrayList<Double> position = currentRs.get(p.getId()).get(0);
                ArrayList<Double> velocities = currentRs.get(p.getId()).get(1);

                state.add(t);
                state.add((double) p.getId());
                state.add(position.get(0));
                state.add(position.get(1));
                state.add(velocities.get(0));
                state.add(velocities.get(1));
                states.add(state);
            }

            //predicciones
            List<List<ArrayList<Double>>> newDerivatives = gearPredictor(currentRs, dT);

            //evaluar
            List<ArrayList<Double>> deltasR2 = getR2(newDerivatives,dT);

            //correccion
            currentRs = gearCorrector(newDerivatives, dT, deltasR2);

            t += dT;

            System.out.println("dps de una vuelta" + currentRs);
        }
        return states;
    }

    public static List<List<ArrayList<Double>>> gearPredictor(List<List<ArrayList<Double>>> der, double dT){
//       [ Sol:[ [r0x r0y], [r1x r1y].. ] Tierra:[ [r0x r0y], [r1x r1y].. ] ]
        List<List<ArrayList<Double>>> newDerivatives = new ArrayList<>();

        for(List<ArrayList<Double>> rs : der) {  // para cada planeta
            List<ArrayList<Double>> auxNewDerivatives = new ArrayList<>();

            double r0x = rs.get(0).get(0) + rs.get(1).get(0) * dT + rs.get(2).get(0) * dT * dT / 2 + rs.get(3).get(0) * dT * dT * dT / 6 + rs.get(4).get(0) * dT * dT * dT * dT / 24 + rs.get(5).get(0) * dT * dT * dT * dT * dT / 120;
            double r0y = rs.get(0).get(1) + rs.get(1).get(1) * dT + rs.get(2).get(1) * dT * dT / 2 + rs.get(3).get(1) * dT * dT * dT / 6 + rs.get(4).get(1) * dT * dT * dT * dT / 24 + rs.get(5).get(1) * dT * dT * dT * dT * dT / 120;
            ArrayList<Double> r0 = new ArrayList<>();
            r0.add(r0x);
            r0.add(r0y);
            auxNewDerivatives.add(r0);

            double r1x = rs.get(1).get(0) + rs.get(2).get(0) * dT + rs.get(3).get(0) * dT * dT / 2 + rs.get(4).get(0) * dT * dT * dT / 6 + rs.get(5).get(0) * dT * dT * dT * dT / 24;
            double r1y = rs.get(1).get(1) + rs.get(2).get(1) * dT + rs.get(3).get(1) * dT * dT / 2 + rs.get(4).get(1) * dT * dT * dT / 6 + rs.get(5).get(1) * dT * dT * dT * dT / 24;
            ArrayList<Double> r1 = new ArrayList<>();
            r1.add(r1x);
            r1.add(r1y);
            auxNewDerivatives.add(r1);

            double r2x = rs.get(2).get(0) + rs.get(3).get(0) * dT + rs.get(4).get(0) * dT * dT / 2 + rs.get(5).get(0) * dT * dT * dT / 6;
            double r2y = rs.get(2).get(1) + rs.get(3).get(1) * dT + rs.get(4).get(1) * dT * dT / 2 + rs.get(5).get(1) * dT * dT * dT / 6;
            ArrayList<Double> r2 = new ArrayList<>();
            r2.add(r2x);
            r2.add(r2y);
            auxNewDerivatives.add(r2);

            double r3x = rs.get(3).get(0) + rs.get(4).get(0) * dT + rs.get(5).get(0) * dT * dT / 2;
            double r3y = rs.get(3).get(1) + rs.get(4).get(1) * dT + rs.get(5).get(1) * dT * dT / 2;
            ArrayList<Double> r3 = new ArrayList<>();
            r3.add(r3x);
            r3.add(r3y);
            auxNewDerivatives.add(r3);

            double r4x = rs.get(4).get(0) + rs.get(5).get(0) * dT;
            double r4y = rs.get(4).get(1) + rs.get(5).get(1) * dT;
            ArrayList<Double> r4 = new ArrayList<>();
            r4.add(r4x);
            r4.add(r4y);
            auxNewDerivatives.add(r4);

            double r5x = rs.get(5).get(0);
            double r5y = rs.get(5).get(1);
            ArrayList<Double> r5 = new ArrayList<>();
            r5.add(r5x);
            r5.add(r5y);
            auxNewDerivatives.add(r5);
            System.out.println("aux new derivatives "+auxNewDerivatives);
            newDerivatives.add(auxNewDerivatives);
        }

        return newDerivatives;
    }

    public static List<List<ArrayList<Double>>> gearCorrector(List<List<ArrayList<Double>>> der, double dT, List<ArrayList<Double>>  dR2){
       // [ [ [r0x r0y] [r1x r1y] ] [ [r2x r2y] [r3x r3y] ] ]
        List<List<ArrayList<Double>>> newDerivatives = new ArrayList<>();
        int count = 0;
        for(List<ArrayList<Double>> rs : der) { // por planeta

            List<ArrayList<Double>> auxNewDerivatives = new ArrayList<>();

            // dR2 = [ sol: [dr2x dr2y] ,tierra: [dr2x dr2y] ,venus: [dr2x dr2y], nave: [dr2x dr2y] ]
            double r0x = rs.get(0).get(0) + (alpha[0] * dR2.get(count).get(0));
            double r0y = rs.get(0).get(1) + (alpha[0] * dR2.get(count).get(1));
            ArrayList<Double> r0 = new ArrayList<>();
            r0.add(r0x);
            r0.add(r0y);
            auxNewDerivatives.add(r0);

            double r1x = rs.get(1).get(0) + (alpha[1] * dR2.get(count).get(0) * 1 ) / (dT);
            double r1y = rs.get(1).get(1) + (alpha[1] * dR2.get(count).get(0) * 1 ) / (dT);
            ArrayList<Double> r1 = new ArrayList<>();
            r1.add(r1x);
            r1.add(r1y);
            auxNewDerivatives.add(r1);


            double r2x = rs.get(2).get(0) + (alpha[2] * dR2.get(count).get(0) * 2) / (dT * dT);
            double r2y = rs.get(2).get(1) + (alpha[2] * dR2.get(count).get(1) * 2) / (dT * dT);
            ArrayList<Double> r2 = new ArrayList<>();
            r2.add(r2x);
            r2.add(r2y);
            auxNewDerivatives.add(r2);


            double r3x = rs.get(3).get(0) + (alpha[3] * dR2.get(count).get(0) * 6) / (dT * dT * dT);
            double r3y = rs.get(3).get(1) + (alpha[3] * dR2.get(count).get(1) * 6) / (dT * dT * dT);
            ArrayList<Double> r3 = new ArrayList<>();
            r3.add(r3x);
            r3.add(r3y);
            auxNewDerivatives.add(r3);


            double r4x = rs.get(4).get(0) + (alpha[4] * dR2.get(count).get(0) * 24) / (dT * dT * dT * dT);
            double r4y = rs.get(4).get(1) + (alpha[4] * dR2.get(count).get(1) * 24) / (dT * dT * dT * dT);
            ArrayList<Double> r4 = new ArrayList<>();
            r4.add(r4x);
            r4.add(r4y);
            auxNewDerivatives.add(r4);

            double r5x = rs.get(5).get(0) + (alpha[5] * dR2.get(count).get(0) * 120) / (dT * dT * dT * dT * dT);
            double r5y = rs.get(5).get(1) + (alpha[5] * dR2.get(count).get(1) * 120) / (dT * dT * dT * dT * dT);
            ArrayList<Double> r5 = new ArrayList<>();
            r5.add(r5x);
            r5.add(r5y);
            auxNewDerivatives.add(r5);

            count++;
            newDerivatives.add(auxNewDerivatives);
        }
        return newDerivatives;
    }

    private static ArrayList<Double> calculateA(final Planet currentPlanet, boolean flag) {
        double Fx = 0;
        double Fy = 0;

        for (Planet p : planetsArray) {
            if (p.getId() != currentPlanet.getId()) {
                System.out.println("current: " + currentPlanet.getId());
                System.out.println("con quien sumo: " + p.getId());
                ArrayList<Double> pPosition = new ArrayList<>();
                ArrayList<Double> currentPlanetPosition = new ArrayList<>();
                if(flag) {
                    pPosition.add(p.getX());
                    pPosition.add(p.getY());
                    currentPlanetPosition.add(currentPlanet.getX());
                    currentPlanetPosition.add(currentPlanet.getY());
                }
                else{
                    pPosition = Rs.get(p.getId()).get(0);
                    currentPlanetPosition = Rs.get(currentPlanet.getId()).get(0);
                }
                System.out.println("current positions: " + currentPlanetPosition);
                System.out.println("planet positions: " + pPosition);
                double deltaX = pPosition.get(0) - currentPlanetPosition.get(0);
                double deltaY = pPosition.get(1) - currentPlanetPosition.get(1);
                double distance = Math.sqrt((deltaX * deltaX) + (deltaY * deltaY));
                double ex = deltaX / distance;
                double ey = deltaY / distance;
                Fx += G * currentPlanet.getM() * p.getM() * ex / (distance * distance);
                Fy += G * currentPlanet.getM() * p.getM() * ey / (distance * distance);
            }
        }

        ArrayList<Double> result = new ArrayList<>();
        result.add(Fx / currentPlanet.getM());
        result.add(Fy / currentPlanet.getM());
        return result;
    }

    private static List<ArrayList<Double>> getR2(List<List<ArrayList<Double>>> newDerivatives, double dT){
        List<ArrayList<Double>> deltasR2 = new ArrayList<>();
        for(Planet planet : planetsArray){

            ArrayList<Double> A = calculateA(planet, false);
            ArrayList<Double> r2 = newDerivatives.get(planet.getId()).get(2);

            double dR2X = (A.get(0) - r2.get(0)) * dT*dT / 2;
            double dR2Y = (A.get(1) - r2.get(1)) * dT*dT / 2;

            ArrayList<Double> deltaR2 = new ArrayList<>();
            deltaR2.add(dR2X);
            deltaR2.add(dR2Y);
            deltasR2.add(deltaR2);
        }
        return deltasR2;
        // [ sol: [dr2x dr2y] ,tierra: [dr2x dr2y] ,venus: [dr2x dr2y], nave: [dr2x dr2y] ]
    }
}
