import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.*;

public class Simulator {

    static int N = 0;
    static int L = 0;

    static int time = 0;

    static ArrayList<Double[]> particlesArray = new ArrayList<>();
    // radio propiedad x y

    public static void main(String[] args) {
        ParseParameters(args[0], args[1]);

        //calcular M
        double ancho = Math.sqrt(L);

        int M = 10; //TODO chequear M

        //L/M > rc  //TODO chequear que se cumpla la condicion

        int rc = 1;

        Integer[][][] matrix = new Integer[M][M][];

        // asignar particulas a celdas segun su ubicacion
        FillMatrix(particlesArray, matrix, ancho);

        // imprimo matrix
        for (Integer[][] integers : matrix) {
            for (Integer[] integer : integers) {
                System.out.print(Arrays.toString(integer) + " ");
            }
            System.out.println();
        }

        //calcular distancias solo entre partículas de celdas vecinas
        Map<Integer, List<Integer>> cim = new HashMap<Integer, List<Integer>>();
        CellIndexMethod(M, rc, cim, matrix);

        System.out.println("mapa final" + cim);
    }

    public static void SearchNeighbours(Integer[] neighbours, Integer[] currentIds, Map<Integer, List<Integer>> cim, int rc){
        for (Integer particleId : neighbours) { //35 29 1 recorremos estos
            Double[] neighbourParticle = particlesArray.get(particleId);
            //System.out.println("particula vecina" + Arrays.toString(neighbourParticle));
            for (Integer ids : currentIds) { // 2 14
                Double[] currentParticle = particlesArray.get(ids);
                //System.out.println("particula en la celda seleccionada" + Arrays.toString(currentParticle));
                double distance = Math.sqrt(Math.pow(neighbourParticle[2] - currentParticle[2], 2) + Math.pow(neighbourParticle[3] - currentParticle[3], 2));
                //System.out.println("distancia: "+distance);
                if (distance <= rc) {
                    cim.putIfAbsent(ids, new ArrayList<Integer>());
                    cim.get(ids).add(particleId);
                    //System.out.println("mapa de cercanos" + cim);
                }
            }
        }
    }

    public static void FillMatrix(ArrayList<Double[]> particlesArray, Integer[][][] matrix, double ancho){
        int id = 0;
        for (Double[] doubles : particlesArray){
            double x = doubles[2];
            double y = doubles[3];
            int row;
            int col ;

            if( x <= ancho ) {
                col = 0;
            }
            else {
                col = (int) (x / ancho);
            }

            if( y <= ancho ){
                row = 0;
            }
            else{
                row = (int) (y / ancho);
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
            }
            else {
                Integer[] aux = {id};
                matrix[row][col] = aux;
            }

            id++;
        }
    }

    public static void CellIndexMethod(int M, int rc, Map<Integer, List<Integer>> cim, Integer[][][] matrix){

        for(int i = 0; i < M; i++){
            for(int j = 0; j < M; j++){
                if(i != 0 && j != 0){  //no es esquina
                    Integer[] currentIds = matrix[i][j]; // 2 14
                    if(currentIds == null){
                        continue;
                    }
                    System.out.println("particulas en la celda elegida" + Arrays.toString(currentIds));

                    // vecino arriba
                    System.out.println("vecinos arriba");
                    Integer[] neighbours = matrix[i - 1][j];
                    System.out.println("vecinos arriba" + Arrays.toString(neighbours));
                    if(neighbours == null) {
                        continue;
                    }
                    SearchNeighbours(neighbours, currentIds, cim, rc);

                    // vecino abajo
                    System.out.println("vecinos abajo");
                    if(i<M-1) { // si no es extremo
                        Integer[] neighboursDown = matrix[i + 1][j];
                        System.out.println("vecinos abajo" + Arrays.toString(neighboursDown));
                        if (neighboursDown == null) {
                            continue;
                        }
                        SearchNeighbours(neighboursDown, currentIds, cim, rc);
                    }

                    // vecino izquierda
                    System.out.println("vecinos izquierda");
                    Integer[] neighboursLeft = matrix[i][j - 1];
                    System.out.println("vecinos izquierda" + Arrays.toString(neighboursLeft));
                    if(neighboursLeft == null) {
                        continue;
                    }
                    SearchNeighbours(neighboursLeft, currentIds, cim, rc);

                    // vecino derecha
                    if (j < M-1) { // si no es un extremo
                        System.out.println("vecinos derecha");
                        Integer[] neighboursRight = matrix[i][j + 1];
                        System.out.println("vecinos arriba" + Arrays.toString(neighboursRight));
                        if (neighboursRight == null) {
                            continue;
                        }
                        SearchNeighbours(neighboursRight, currentIds, cim, rc);
                    }

                    // vecino abajo izquierda
                    if(i < M-1){ // si no es extremo
                        System.out.println("vecinos abajo izquierda");
                        Integer[] neighboursDownLeft = matrix[i + 1][j - 1];
                        System.out.println("vecinos abajo derecha" + Arrays.toString(neighboursDownLeft));
                        if(neighboursDownLeft == null) {
                            continue;
                        }
                        SearchNeighbours(neighboursDownLeft, currentIds, cim, rc);
                    }

                    // vecino abajo derecha
                    if(j < M-1 || i < M-1) { // si no es un extremo // i = 9
                        System.out.println("vecinos abajo derecha");
                        Integer[] neighboursDownRight = matrix[i + 1][j + 1];
                        System.out.println("vecinos abajo derecha" + Arrays.toString(neighboursDownRight));
                        if (neighboursDownRight == null) {
                            continue;
                        }
                        SearchNeighbours(neighboursDownRight, currentIds, cim, rc);
                    }

                    // vecino arriba izquierda
                    System.out.println("vecinos arriba izquierda");
                    Integer[] neighboursUpLeft = matrix[i - 1][j - 1];
                    System.out.println("vecinos arriba izquierda" + Arrays.toString(neighboursUpLeft));
                    if(neighboursUpLeft == null) {
                        continue;
                    }
                    SearchNeighbours(neighboursUpLeft, currentIds, cim, rc);

                    // vecino arriba derecha
                    if(j < M-1){ //si no es extremo
                        System.out.println("vecinos arriba derecha");
                        Integer[] neighboursUpRight = matrix[i - 1][j + 1];
                        System.out.println("vecinos arriba derecha" + Arrays.toString(neighboursUpRight));
                        if(neighboursUpRight == null) {
                            continue;
                        }
                        SearchNeighbours(neighboursUpRight, currentIds, cim, rc);
                    }

                }
            }
        }

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
