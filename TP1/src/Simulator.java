import java.io.IOException;  // Import the IOException class to handle errors
import java.util.*;
import java.io.FileWriter;   // Import the FileWriter class

public class Simulator {

    public static int N = 0;
    public static int L = 0;
    public static double r_max = 0;
    static int time = 0;
    static boolean bruteForce = false;

    static ArrayList<Particle> particlesArray = new ArrayList<>();
    // radio propiedad x y

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        Parser.ParseParameters(args[0], args[1], r_max, particlesArray);


        int rc = Integer.parseInt(args[3]);
        int M = (int) Math.floor(L/(rc + 2*r_max));
        //int M = Integer.parseInt(args[2]);
        System.out.println("M optimo: " + M);

//        if((double) L/M <= (rc + 2*r_max)){
//            System.out.println("Invalid value M: "+M);
//            return;
//        }

        Integer[][][] matrix = new Integer[M][M][];

        // asignar particulas a celdas segun su ubicacion
        FillMatrix(particlesArray, matrix, (double) L/M, M);

        // imprimo matrix
        for (Integer[][] integers : matrix) {
            for (Integer[] integer : integers) {
                System.out.print(Arrays.toString(integer) + " ");
            }
            System.out.println();
        }

        //calcular distancias solo entre partículas de celdas vecinas
        Map<Integer, List<Integer>> cim = new HashMap<Integer, List<Integer>>();
        boolean periodic = Boolean.parseBoolean(args[4]); //true;
        CellIndexMethod(M, rc, cim, matrix, periodic);

        System.out.println();
        System.out.println("Vecinos finales " + cim);

        // asigno los vecinos a cada instancia
        for(Map.Entry entry: cim.entrySet()) {
            Particle part = particlesArray.get( (int) entry.getKey());
            part.setNeighbours((List<Integer>) entry.getValue());
        }

        try {
            FileWriter myWriter = new FileWriter("output.txt");
            for (Map.Entry entry: cim.entrySet()) {
                myWriter.write(entry.getKey() + "\t");
                List<Integer> aux = (List<Integer>) entry.getValue();
                for(Integer cur : aux){
                    myWriter.write(" " + cur);
                }
                myWriter.write("\n");
            }
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println("Execution time: " + elapsedTime + " seconds");

        if(bruteForce) {
            long startTime2 = System.currentTimeMillis();
            Map<Integer, List<Integer>> particlesNeighbours = new HashMap<Integer, List<Integer>>();
            BruteForce(particlesArray,rc,particlesNeighbours);
            System.out.println("Vecinos " + particlesNeighbours);
            long stopTime2 = System.currentTimeMillis();
            long elapsedTime2 = stopTime2 - startTime2;
            System.out.println("Execution time of brute-force: " + elapsedTime2 + " seconds");
        }
    }

    public static void BruteForce(ArrayList<Particle> particlesArray, int rc, Map<Integer, List<Integer>> particlesNeighbours){
        for (Particle currentParticle: particlesArray) { //1 2 3 4 5 6
            for(Particle neighbourParticle : particlesArray){
                if (currentParticle.getId() != neighbourParticle.getId()) {
                    double distance = Math.sqrt(Math.pow(neighbourParticle.getX() - currentParticle.getX(), 2) + Math.pow(neighbourParticle.getY() - currentParticle.getY(), 2));
                    double edge = distance - neighbourParticle.getRadio() - currentParticle.getRadio();
                    if(edge <= rc){
                        particlesNeighbours.putIfAbsent(currentParticle.getId(), new ArrayList<Integer>());
                        particlesNeighbours.get(currentParticle.getId()).add(neighbourParticle.getId());
                    }
                }
            }
        }
    }

    public static void SearchNeighbours(Integer[] neighbours, Integer[] currentIds, Map<Integer, List<Integer>> cim, int rc){
        for (Integer particleId : neighbours) {
            Particle neighbourParticle = particlesArray.get(particleId);
            for (Integer ids : currentIds) {
                Particle currentParticle = particlesArray.get(ids);
                double distance = Math.sqrt(Math.pow(neighbourParticle.getX() - currentParticle.getX(), 2) + Math.pow(neighbourParticle.getY() - currentParticle.getY(), 2));
                double edge = distance - neighbourParticle.getRadio() - currentParticle.getRadio();
                if (edge <= rc) {
                    cim.putIfAbsent(ids, new ArrayList<Integer>());
                    cim.get(ids).add(particleId);
                }
            }
        }
    }

    public static void FillMatrix(ArrayList<Particle> particlesArray, Integer[][][] matrix, double ancho, int M){
        int id = 0;
        for (Particle particle : particlesArray){
            double x = particle.getX();
            double y = particle.getY();
            int row;
            int col ;
            row = (int) Math.floor(y / ancho);
            col = (int) Math.floor(x / ancho);
            Integer[] current = matrix[row][col];
            Integer[] aux = {id};
            if(current != null) {
                int aLen = current.length;
                int bLen = aux.length;
                Integer[] result = new Integer[aLen + bLen];
                System.arraycopy(current, 0, result, 0, aLen);
                System.arraycopy(aux, 0, result, aLen, bLen);
                matrix[row][col] = result;
            }
            else {
                matrix[row][col] = aux;
            }
            id++;
        }
    }

    public static void CellIndexMethod(int M, int rc, Map<Integer, List<Integer>> cim, Integer[][][] matrix, boolean periodic){

        for(int i = 0; i < M; i++){
            for(int j = 0; j < M; j++){
                Integer[] currentIds = matrix[i][j]; // 2 14
                if(currentIds == null){
                    continue;
                }
                // vecino arriba
                if( i != 0 ) {  // si no es la primer fila
                    Integer[] neighbours = matrix[i - 1][j];
                     if (neighbours != null) {
                        SearchNeighbours(neighbours, currentIds, cim, rc);
                    }
                }

                // vecino abajo
                if(i < M-1) { // si no es extremo de abajo
                    Integer[] neighboursDown = matrix[i + 1][j];
                    if (neighboursDown != null) {
                        SearchNeighbours(neighboursDown, currentIds, cim, rc);
                    }
                }

                // vecino izquierda
                if(j != 0) { // si no es la primera columna
                    Integer[] neighboursLeft = matrix[i][j - 1];
                    if (neighboursLeft != null) {
                        SearchNeighbours(neighboursLeft, currentIds, cim, rc);
                    }
                }

                // vecino derecha
                if (j < M-1) { // si no es un extremo
                    Integer[] neighboursRight = matrix[i][j + 1];
                    if (neighboursRight != null) {
                        SearchNeighbours(neighboursRight, currentIds, cim, rc);
                    }
                }

                // vecino abajo izquierda
                if(i < M-1 && j != 0){ // si no es extremo
                    Integer[] neighboursDownLeft = matrix[i + 1][j - 1];
                    if(neighboursDownLeft != null) {
                        SearchNeighbours(neighboursDownLeft, currentIds, cim, rc);
                    }
                }

                // vecino abajo derecha
                if(j != M-1 && i != M-1) { // si no es un extremo // i = 9
                    Integer[] neighboursDownRight = matrix[i + 1][j + 1];
                    if (neighboursDownRight != null) {
                        SearchNeighbours(neighboursDownRight, currentIds, cim, rc);
                    }
                }

                // vecino arriba izquierda
                if(i != 0 && j != 0) { //si no es la primer fila
                    Integer[] neighboursUpLeft = matrix[i - 1][j - 1];
                    if (neighboursUpLeft != null) {
                        SearchNeighbours(neighboursUpLeft, currentIds, cim, rc);
                    }
                }

                // vecino arriba derecha
                if(i != 0) { // si no es la primera fila
                    if (j < M - 1) { //si no es extremo
                        Integer[] neighboursUpRight = matrix[i - 1][j + 1];
                        if (neighboursUpRight != null) {
                            SearchNeighbours(neighboursUpRight, currentIds, cim, rc);
                        }
                    }
                }

                // si es la primera fila o la ultima && condiciones periodicas
                if(periodic && (i == 0 || i == M-1)){
                    int aux_i = (i == 0)? M-1 : 0;
                    // arriba izquierda
                    if(j != 0) { //i != 0 && i != M-1 ||
                        Integer[] neighboursUpLeft = matrix[aux_i][j - 1];
                        if (neighboursUpLeft != null) {
                            SearchNeighbours(neighboursUpLeft, currentIds, cim, rc);
                        }
                    }

                    // arriba
                    Integer[] neighboursUp = matrix[aux_i][j];
                    if (neighboursUp != null) {
                        SearchNeighbours(neighboursUp, currentIds, cim, rc);
                    }

                    if(j != M-1) {
                        // arriba derecha
                        Integer[] neighboursUpRight = matrix[aux_i][j + 1];
                        if (neighboursUpRight != null) {
                            SearchNeighbours(neighboursUpRight, currentIds, cim, rc);
                        }
                    }
                }

                // si es la primera columna o la ultima && condiciones periodicas
                if(periodic && (j == 0 || j == M-1) && i != 0){
                    int aux_j = (j == 0)? M-1 : 0;

                    //izquierda
                    Integer[] neighboursL = matrix[i][aux_j];
                    if(neighboursL != null) {
                        SearchNeighbours(neighboursL, currentIds, cim, rc);
                    }

                    //arriba
                    Integer[] neighboursD = matrix[i-1][aux_j];
                    if(neighboursD != null) {
                        SearchNeighbours(neighboursD, currentIds, cim, rc);
                    }

                    //abajo
                    if(i != M-1) {
                        Integer[] neighbours3 = matrix[i + 1][aux_j];
                        if (neighbours3 != null) {
                            SearchNeighbours(neighbours3, currentIds, cim, rc);
                        }
                    }
                }

                //extremo esquina arriba izquierda
                if(periodic && j==0 && i==0){
                    int aux_j = M-1 ;
                    int aux_i = M-1;
                    Integer[] neighbours3 = matrix[i][aux_j];
                    if(neighbours3 != null) {
                        SearchNeighbours(neighbours3, currentIds, cim, rc);
                    }
                    Integer[] neighbours4 = matrix[i+1][aux_j];
                    if(neighbours4 != null) {
                        SearchNeighbours(neighbours4, currentIds, cim, rc);
                    }
                    Integer[] neighbours5 = matrix[aux_i][aux_j];
                    if(neighbours5 != null) {
                        SearchNeighbours(neighbours5, currentIds, cim, rc);
                    }
                }

                //extremo esquina abajo izquierda
                if(periodic && j==0 && i==M-1){
                    Integer[] neighboursAbI3 = matrix[i][M-1];
                    if(neighboursAbI3 != null) {
                        SearchNeighbours(neighboursAbI3, currentIds, cim, rc);
                    }
                    Integer[] neighboursAbI4 = matrix[i-1][M-1];
                    if(neighboursAbI4 != null) {
                        SearchNeighbours(neighboursAbI4, currentIds, cim, rc);
                    }
                    Integer[] neighboursAbI5 = matrix[0][M-1];
                    if(neighboursAbI5 != null) {
                        SearchNeighbours(neighboursAbI5, currentIds, cim, rc);
                    }
                }

                //extremo esquina arriba derecha
                if(periodic && i==0 && j==M-1){
                    int aux_i = M-1;
                    Integer[] neighboursArD3 = matrix[i][0];
                    if(neighboursArD3 != null) {
                        SearchNeighbours(neighboursArD3, currentIds, cim, rc);
                    }
                    Integer[] neighboursArD4 = matrix[i+1][0];
                    if(neighboursArD4 != null) {
                        SearchNeighbours(neighboursArD4, currentIds, cim, rc);
                    }
                    Integer[] neighboursArD5 = matrix[aux_i][0];
                    if(neighboursArD5 != null) {
                        SearchNeighbours(neighboursArD5, currentIds, cim, rc);
                    }
                }
                //extremo esquina abajo derecha
                if(periodic && i==M-1 && j==M-1){
                    Integer[] neighboursAbD3 = matrix[i][0];
                    if(neighboursAbD3 != null) {
                        SearchNeighbours(neighboursAbD3, currentIds, cim, rc);
                    }
                    Integer[] neighboursAbD4 = matrix[i-1][j-1];
                    if(neighboursAbD4 != null) {
                        SearchNeighbours(neighboursAbD4, currentIds, cim, rc);
                    }
                    Integer[] neighboursAbD5 = matrix[0][0];
                    if(neighboursAbD5 != null) {
                        SearchNeighbours(neighboursAbD5, currentIds, cim, rc);
                    }
                }
            }
        }
    }
}
