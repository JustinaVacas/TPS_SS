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

    //TODO revisar vx inicial, condicion de corte, t!=0 para verlet

    public static void main(String[] args) {

        double deltaT = 1;
        Particle particle = new Particle(1,0,-(100.0 / 140),0,m);

        List<ArrayList<Double>> finalStates = new ArrayList<>();
        finalStates = verlet(particle,deltaT);
//        finalStates = beeman(particle,deltaT);

        GeneratorFiles.outputStates(finalStates);
    }

    public static List<ArrayList<Double>> verlet(Particle particle,double deltaT){

        double force = -k*particle.getX() - gamma*particle.getVx();
        double xBefore = eulerX(particle.getX(), particle.getVx(),-deltaT,force,particle.getM());

        double t = 0;
        double x;
        double v;
        List<ArrayList<Double>> states = new ArrayList<>();

        while(t <= tf){
            // me guardo el estado
            ArrayList<Double> state = new ArrayList<>();
            state.add(t);
            state.add(particle.getX());
            state.add(particle.getVx());
            states.add(state);

            x = verletX(particle.getX(), xBefore,force, m,deltaT);

            // hay que hacerlo si t!=0 ?
            v = verletV(x,xBefore,deltaT);
            particle.setVx(v);

            //update
            xBefore = particle.getX();
            particle.setX(x);
            t+=deltaT;
        }
        return states;
    }

    public static List<ArrayList<Double>> beeman(Particle particle,double deltaT){

        double force = -k*particle.getX() - gamma*particle.getVx();
        double xBefore = eulerX(particle.getX(), particle.getVx(),-deltaT,force,m);
        double vBefore = eulerV(particle.getVx(),force,m,-deltaT);
        double aBefore = (-k*xBefore - gamma*vBefore)/m;

        double t = 0;
        double x;
        double v;
        double aAfter;
        List<ArrayList<Double>> states = new ArrayList<>();

        while(t <= tf){
            // me guardo el estado
            ArrayList<Double> state = new ArrayList<>();
            state.add(t);
            state.add(particle.getX());
            state.add(particle.getVx());
            states.add(state);

            force = -k*particle.getX() - gamma*particle.getVx();
            x = beemanX(particle.getX(), particle.getVx(), force/m,aBefore,deltaT);

            force = -k*x - gamma*particle.getVx();
            v = beemanVPredicted(particle.getVx(),force/m,aBefore,deltaT);
            aAfter = (-k*x- gamma*v)/m;

            force = -k* particle.getX() - gamma*particle.getVx();
            v = beemanVCorrected(particle.getVx(),aBefore,force/m,aAfter,deltaT);

            aBefore = (-k*particle.getX() - gamma*particle.getVx())/m;

            //update
            particle.setVx(v);
            particle.setX(x);
            t+=deltaT;
        }
        return states;

    }

    public static double eulerX(double x, double v, double deltaT, double f, double mass){
        return x + v * deltaT + deltaT*deltaT * (f/(2*mass));
    }

    public static double eulerV(double v, double f, double m, double deltaT){
        return v + deltaT * f / m;
    }

    public static double verletX(double x, double xBefore,double f, double mass, double deltaT){
        return (2 * x) - xBefore + ((deltaT*deltaT) * (f/mass));
    }

    public static double verletV(double xAfter, double xBefore, double deltaT){
        return (xAfter - xBefore) / (2 * deltaT);
    }

    public static double beemanX(double x, double v, double a1, double aBefore, double deltaT) {
        return x + v * deltaT + (double)2/3*a1*deltaT*deltaT-(double)1/6*aBefore*deltaT*deltaT;
    }

    public static double beemanVCorrected(double v,double aBefore, double a, double aAfter, double deltaT ) {
        return v + (double)1/3*aAfter*deltaT + (double)5/6*a*deltaT - (double)1/6*aBefore*deltaT;
    }

    public static double beemanVPredicted(double v, double a, double aBefore, double deltaT) {
        return v + (double)3/2*a*deltaT - (double)1/2*aBefore*deltaT;
    }

    public static void gearPredictorCorrector(){

    }

}