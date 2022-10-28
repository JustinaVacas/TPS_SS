import java.util.ArrayList;

public class Simulator {

    public static double vdH = 4;
    public static double vdZ = 3;
    public static int Nh;
    public static double Rmax;
    public static double Rmin;
    public static double R;

    public static ArrayList<Particle> humans = new ArrayList<>();
    public static ArrayList<Particle> zombies = new ArrayList<>();

    public static void main(String[] args) {
        //static dynamic
        Parser.ParseParameters(args[0], args[1], humans, zombies);

        System.out.println("Nh: " + Nh);
        System.out.println("Rmax: " + Rmax);
        System.out.println("Rmin: " + Rmin);
        System.out.println("R: " + R);
        System.out.println("humans: " + humans);
        System.out.println("humans size: " + humans.size());
        System.out.println("zombies: " + zombies);
        System.out.println("zombies size: " + zombies.size());

        CPM.run(Rmax,Rmin,R,vdH,vdZ,humans,zombies);
    }
}
