import java.io.IOException;
import java.util.ArrayList;
import static java.lang.Math.*;

public class Simulator {

    public static int N = 0;
    public static int L = 0;
    public static double n = 0;
    public static double r_max = 0;
    static int time = 0;

    static ArrayList<Particle> birdsArray = new ArrayList<>();
    // radio velocidad x y

    public static void main(String[] args) throws IOException {

        Parser.ParseParameters(args[0], args[1], r_max, birdsArray);
        System.out.println("N: " + N);
        System.out.println("L: " + L);
        System.out.println("n: " + n);
        System.out.println("r_max: " + r_max);
        System.out.println("particulas: " + birdsArray);

        int rc = 1;
        int M = (int) Math.floor(L/(rc + 2*r_max));
        System.out.println("M optimo: " + M);

        double p = N/(pow(L,2));
        int dt = 1;

        System.out.println("orden " + GenerateOrder());

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
