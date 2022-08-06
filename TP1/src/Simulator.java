import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner; // Import the Scanner class to read text files

public class Simulator {

    static int N = 0;
    static int L = 0;

    static int time = 0;

    static ArrayList<Double[]> particlesArray = new ArrayList<>();
    // radio propiedad x y

    public static void main(String[] args) {
        ParseParameters(args[0], args[1]);

        //chequeo de parametros
        System.out.println(N);
        System.out.println(L);
        System.out.println(time);
        for (Double[] doubles : particlesArray) {
            System.out.print(Arrays.toString(doubles));
        }
        System.out.println();

        //calcular M
        double ancho = Math.sqrt(L);
        System.out.println("ancho: " + ancho);

        int M = 10; //TODO chequear M

        //L/M > rc  //TODO chequear que se cumpla la condicion

        //ArrayList<ArrayList<Integer>> matrix = new ArrayList<ArrayList<Integer>>();
        //ArrayList<Integer>[][] matrix = new ArrayList[M][M];

        Integer[][][] matrix = new Integer[M][M][];

        //asignar particulas a celdas segun su ubicacion
        int id = 0;
        for (Double[] doubles : particlesArray){
            double x = doubles[2];
            double y = doubles[3];
            System.out.println("x" + x);
            System.out.println("y" + y);
            int row;
            int col ;

            if( x <= ancho ) {
                col = 0;
                System.out.println("columna: " + col);
            }
            else {
                col = (int) (x / ancho);
                System.out.println("columna: " + col);
            }

            if( y <= ancho ){
                row = 0;
                System.out.println("fila: " + row);
            }
            else{
                row = (int) (y / ancho);
                System.out.println("fila: " + row);
            }

            Integer[] current = matrix[row][col];
            if(current != null) {
                Integer[] aux = {id};
                int aLen = current.length;
                int bLen = aux.length;
                Integer[] result = new Integer[aLen + bLen];
                System.arraycopy(current, 0, result, 0, aLen);
                System.arraycopy(aux, 0, result, aLen, bLen);

                matrix[row][col] = result;

                //System.out.println(Arrays.toString(result));
            }
            else {
                Integer[] aux = {id};
                matrix[row][col] = aux;
                //System.out.println(Arrays.toString(aux));
            }

            id++;
        }

        // imprimo matrix
        for (Integer[][] integers : matrix) {
            for (Integer[] integer : integers) {
                System.out.print(Arrays.toString(integer) + " ");
            }
            System.out.println();
        }

        //calcular distancias soloentre partículas de celdas vecinas

    }

    public static void CellIndexMethod(int N, int L, int M, int rc, ArrayList<Double[]> particlesArray){

    }

    public static void ParseParameters(String static100, String dynamic100){

        // STATIC
        try {
            File staticFile = new File(static100);
            Scanner myReaderStatic = new Scanner(staticFile);

            // guardo N
            if (myReaderStatic.hasNextLine()) {
                String data = myReaderStatic.nextLine();
                data = data.replaceAll(" ", "");
                N = Integer.parseInt(data);
            }

            // guardo L
            if (myReaderStatic.hasNextLine()) {
                String data = myReaderStatic.nextLine();
                data = data.replaceAll(" ", "");
                L = Integer.parseInt(data);
            }

            // guardo el resto
            int id = 0;
            while (myReaderStatic.hasNextLine()) {
                String data = myReaderStatic.nextLine();
                data = data.trim().replaceAll("\\s+", " ");
                String[] dataStatic = data.split(" ");
                double radio = Double.parseDouble(dataStatic[0]);
                double propiedad = Double.parseDouble(dataStatic[1]);
                Double[] aux = {radio, propiedad};
                particlesArray.add(aux);
                id++;

            }
            myReaderStatic.close();

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        // DYNAMIC
        try {
            File dynamicFile = new File(dynamic100);
            Scanner myReaderDynamic = new Scanner(dynamicFile);

            // guardo t0
            if (myReaderDynamic.hasNextLine()) {
                String data = myReaderDynamic.nextLine();
                data = data.replaceAll(" ", "");
                time = Integer.parseInt(data);
            }

            // guardo el resto
            int id = 0;
            while (myReaderDynamic.hasNextLine()) {
                String data = myReaderDynamic.nextLine();
                data = data.trim().replaceAll("\\s+", " ");
                String[] dataStatic = data.split(" ");
                double x = Double.parseDouble(dataStatic[0]);
                double y = Double.parseDouble(dataStatic[1]);
                Double[] current = particlesArray.get(id);
                Double[] aux = {current[0], current[1],x,y};
                particlesArray.set(id,aux);
                id++;

            }
            myReaderDynamic.close();

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

}
