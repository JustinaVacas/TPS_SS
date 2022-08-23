import utils.GeneratorFiles;

import java.io.IOException;
import java.util.*;

import static java.lang.Math.*;

public class Simulator {

    public static int N = 0;
    public static int L = 0;
    public static double n = 0;
    public static double r_max = 0;
    static int time = 0;

    public static ArrayList<Particle> birdsArray = new ArrayList<>();
    public static List<ArrayList<Particle>> frames = new ArrayList<>();
    // radio velocidad x y

    public static void main(String[] args) throws IOException {

        Parser.ParseParameters(args[0], args[1], r_max, birdsArray);
        System.out.println("N: " + N);
        System.out.println("L: " + L);
        System.out.println("n: " + n);
        System.out.println("r_max: " + r_max);
        System.out.println("particulas: " + birdsArray);

        int rc = 1;
        int M = (int) Math.floor(L/rc);
        // TODO ver la formula
        System.out.println("M optimo: " + M);

        double p = N/(pow(L,2));
        int dt = 1;
        boolean periodic = false;

        Integer[][][] matrix = new Integer[M][M][];

        // asignar particulas a celdas segun su ubicacion
        CIM.FillMatrix(birdsArray, matrix, (double) L/M, M);

        // imprimo matrix
        for (Integer[][] integers : matrix) {
            for (Integer[] integer : integers) {
                System.out.print(Arrays.toString(integer) + " ");
            }
            System.out.println();
        }

        Map<Integer, List<Integer>> cim = new HashMap<Integer, List<Integer>>();

        List<Double> orders = OffLatice(M, rc, cim, matrix, periodic, dt, L);

        GeneratorFiles.output(orders);
    //public static List<ArrayList<Particle>> frames = new ArrayList<>();
//        for(ArrayList<Particle> particles : frames){
//            System.out.println("frame ------------------------------ ");
//            for(Particle particle : particles){;
//                System.out.println(particle);
//            }
//        }
    }

    public static List<Double> OffLatice(int M, int rc, Map<Integer,List<Integer>> cim, Integer[][][] matrix, boolean periodic, double dt, int L){
        int iter = 0;
        int max = 40;
        //lista de double de los orders
        List<Double> order = new ArrayList<>();
        while (iter < max) {

            //guardamos el frame
            frames.add(birdsArray);

            //agregar order
            order.add(GenerateOrder());

            //busco vecinos y se guardan en cim
            CIM.CellIndexMethod(M, rc, cim, matrix, periodic, L, birdsArray);

            updatePosition(cim, dt, L);

            iter++;

        }
        return order;
    }

    public static void updatePosition(Map<Integer,List<Integer>> cim, double dt, int L){

       for (Map.Entry entry: cim.entrySet()){
           Particle current = birdsArray.get((int) entry.getKey());
//           System.out.println("current particle " + current);
           double vx = cos(current.getAngle()) * current.getV();
           double vy = sin(current.getAngle()) * current.getV();
           double newX = ((current.getX() + vx ) * dt ) % L;
           newX = (newX < 0) ? (newX + L) : newX;
           double newY = ((current.getY() + vy ) * dt ) % L;
           newY = (newY < 0) ? (newY + L) : newY;

           List<Integer> valuesIds = (List<Integer>) entry.getValue();
           double sumcos = 0;
           double sumsin = 0;
           int count = 0;
           for(Integer currentId : valuesIds){
               Particle currentNeighbour = birdsArray.get(currentId);
               sumcos += cos(currentNeighbour.getAngle());
               sumsin += sin(currentNeighbour.getAngle());
               count++;
           }
           sumcos += cos(current.getAngle());
           sumsin += sin(current.getAngle());
           count++;

           double newAngle = atan2(sumsin/count, sumcos/count) + GenerateNoise();

           current.setX(newX);
           current.setY(newY);
           current.setAngle(newAngle);

//           System.out.println("current particle dps del cambio " + current);
       }

    }

    public static double GenerateNoise() {
        double max = n/2;
        double min = -n/2;
        return (double) ((Math.random() * (max - min)) + min);
    }


    public static double GenerateOrder(){
        double v = birdsArray.get(0).getV();
        double vx = 0;
        double vy = 0;
        for(Particle bird : birdsArray){
            vx += cos(bird.getAngle()) * bird.getV();
            vy += sin(bird.getAngle()) * bird.getV();
        }
        double v_sum = sqrt(pow(vx, 2) + pow(vy, 2));
        return (1 / (N * v)) * v_sum;
    }

}
