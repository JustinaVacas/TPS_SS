package methods;

import utils.GeneratorFiles;

import java.util.ArrayList;
import java.util.List;

public class Amortiguado {

    // parametros
    public static double m = 70;
    public static double k = 10000;
    public static double gamma = 100;
    public static double tf = 5;
    // condiciones iniciales
    public static double r = 1;
    // v = -A*gama/2*m

    //TODO condicion de corte, t!=0 para verlet

    public static void main(String[] args) {

        double dT = 0.00002;
        Particle particle = new Particle(1,0,-(100.0 / (2*m)),0,m);

        List<ArrayList<Double>> finalStates = new ArrayList<>();
//        finalStates = verlet(particle,dT);
        finalStates = beeman(particle,dT);
//        finalStates = gear(particle, dT);
        GeneratorFiles.outputStates(finalStates);
    }

    public static List<ArrayList<Double>> verlet(Particle particle, double dT){

        double force = -k*particle.getX() - gamma*particle.getVx();
        //calculo el valor anterior con euler con -dT
        double xBefore = eulerX(particle.getX(), particle.getVx(), -dT, force, particle.getM());

        double x;
        double v;
        double t = 0;
        List<ArrayList<Double>> states = new ArrayList<>();

        while(t <= tf){
            // me guardo el estado
            //t x vx
            ArrayList<Double> state = new ArrayList<>();
            state.add(t);
            state.add(particle.getX());
            state.add(particle.getVx());
            states.add(state);

            force = -k*particle.getX() - gamma*particle.getVx();
            x = verletX(particle.getX(), xBefore, force, particle.getM(), dT);

            // hay que hacerlo si t!=0 ?
            if(t != 0) {
                v = verletV(x, xBefore, dT);
                particle.setVx(v);
            }

            //update
            xBefore = particle.getX();
            particle.setX(x);
            t += dT;
        }
        return states;
    }

    public static List<ArrayList<Double>> beeman(Particle particle, double dT){

        double force = -k*particle.getX() - gamma*particle.getVx();
        double xBefore = eulerX(particle.getX(), particle.getVx(),-dT,force,particle.getM());
        double vBefore = eulerV(particle.getVx(),force,particle.getM(),-dT);
        double aBefore = (-k*xBefore - gamma*vBefore)/particle.getM();

        double x;
        double v;
        double newV;
        double t = 0;
        double aAfter;
        List<ArrayList<Double>> states = new ArrayList<>();

        while(t <= tf){
            // me guardo el estado
            ArrayList<Double> state = new ArrayList<>();
            state.add(t);
            state.add(particle.getX());
            state.add(particle.getVx());
            states.add(state);

            // x
            force = -k*particle.getX() - gamma*particle.getVx();
            x = beemanX(particle.getX(), particle.getVx(), force/particle.getM(),aBefore,dT);
            // v prediccion
            force = -k*x - gamma*particle.getVx();
            v = beemanVPredicted(particle.getVx(),force/particle.getM(),aBefore,dT);
            aAfter = (-k*x - gamma*v)/particle.getM();
            // v correcto
            force = -k* particle.getX() - gamma*particle.getVx();
            newV = beemanVCorrected(particle.getVx(),aBefore,force/particle.getM(),aAfter,dT);

            aBefore = (-k*particle.getX() - gamma*particle.getVx())/particle.getM();

            //update
            particle.setVx(newV);
            particle.setX(x);
            t += dT;
        }
        return states;

    }
    
    public static List<ArrayList<Double>> gear(Particle particle, double dT){

        List<ArrayList<Double>> states = new ArrayList<>();
        double t = 0;
        double[] alpha = {3.0/16, 251.0/360, 1, 11.0/18, 1.0/6, 1.0/60};
        double dA;
        double dR2;
        double force = -k*particle.getX() - gamma*particle.getVx();
        List<Double> derivatives = new ArrayList<>();
        derivatives.add(particle.getX());
        derivatives.add(particle.getVx());
        derivatives.add(force/particle.getM());
        derivatives.add((-k *derivatives.get(1) - gamma*derivatives.get(2))/particle.getM());
        derivatives.add((-k *derivatives.get(2) - gamma*derivatives.get(3))/particle.getM());
        derivatives.add((-k *derivatives.get(3) - gamma*derivatives.get(4))/particle.getM());

        while(t <= tf) {
            // me guardo el estado
            ArrayList<Double> state = new ArrayList<>();
            state.add(t);
            state.add(particle.getX());
            state.add(particle.getVx());
            states.add(state);
            
            //predicciones
            List<Double> newDerivatives = gearPredictor(derivatives, dT);
            //evaluar
            dA = (-k*newDerivatives.get(0) - gamma*newDerivatives.get(1))/particle.getM() - newDerivatives.get(2);
            dR2 = dA * dT*dT / 2;
            //correccion
            derivatives = gearCorrector(newDerivatives, dT, alpha, dR2);
            //TODO lo pisara bien?

            particle.setX(derivatives.get(0));
            particle.setVx(derivatives.get(1));

            t += dT;
        }
        return states;
    }


    public static double eulerX(double x, double v, double dT, double f, double mass){
        return x + v * dT + dT*dT * (f/(2*mass));
    }

    public static double eulerV(double v, double f, double m, double dT){
        return v + dT * f / m;
    }

    public static double verletX(double x, double xBefore,double f, double mass, double dT){
        return (2 * x) - xBefore + ((dT*dT) * (f/mass));
    }

    public static double verletV(double xAfter, double xBefore, double dT){
        return (xAfter - xBefore) / (2 * dT);
    }

    public static double beemanX(double x, double v, double a, double aBefore, double dT) {
        return x + v * dT + (2f/3)*a*dT*dT-(1f/6)*aBefore*dT*dT;
    }

    public static double beemanVCorrected(double v,double aBefore, double a, double aAfter, double dT ) {
        return v + (1f/3)*aAfter*dT + (5f/6)*a*dT - (1f/6)*aBefore*dT;
    }

    public static double beemanVPredicted(double v, double a, double aBefore, double dT) {
        return v + (3f/2)*a*dT - (1f/2)*aBefore*dT;
    }

    public static List<Double> gearPredictor(List<Double> der, double dT){
        List<Double> newDerivatives = new ArrayList<>();
        newDerivatives.add(der.get(0) + der.get(1)*dT + der.get(2)*dT*dT/2 + der.get(3)*dT*dT*dT/6 + der.get(4)*dT*dT*dT*dT/24 + der.get(5)*dT*dT*dT*dT*dT/120);
        newDerivatives.add(der.get(1) + der.get(2)*dT + der.get(3)*dT*dT/2 + der.get(4)*dT*dT*dT/6 + der.get(5)*dT*dT*dT*dT/24);
        newDerivatives.add(der.get(2) + der.get(3)*dT + der.get(4)*dT*dT/2 + der.get(5)*dT*dT*dT/6);
        newDerivatives.add(der.get(3) + der.get(4)*dT + der.get(5)*dT*dT/2);
        newDerivatives.add(der.get(4) + der.get(5)*dT);
        newDerivatives.add(der.get(5));
        return newDerivatives;
    }

    public static List<Double> gearCorrector(List<Double> der, double dT, double[] alpha, double dR2){
        List<Double> newDerivatives = new ArrayList<>();
        newDerivatives.add(der.get(0) + (alpha[0] * dR2 * 1));
        newDerivatives.add(der.get(1) + (alpha[1] * dR2 * 1) / (dT));
        newDerivatives.add(der.get(2) + (alpha[2] * dR2 * 2) / (dT*dT));
        newDerivatives.add(der.get(3) + (alpha[3] * dR2 * 6) / (dT*dT*dT));
        newDerivatives.add(der.get(4) + (alpha[4] * dR2 * 24) / (dT*dT*dT*dT));
        newDerivatives.add(der.get(5) + (alpha[5] * dR2 * 120) / (dT*dT*dT*dT*dT));
        return newDerivatives;
    }

}