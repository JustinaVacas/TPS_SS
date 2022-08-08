import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.*;

public class Simulator {

    public static int N = 0;
    public static int L = 0;
    public static double r_max = 0;

    static int time = 0;

    static ArrayList<Double[]> particlesArray = new ArrayList<>();
    // radio propiedad x y

    public static void main(String[] args) {
        Parser.ParseParameters(args[0], args[1], L, N, r_max, particlesArray, time);

//        for (Double[] particle : particlesArray){
//            System.out.println("particlesArray: " + Arrays.toString(particle));
//        }

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
                if(i < M-1) { // si no es extremo de abajo
                    Integer[] neighboursDown = matrix[i + 1][j];
                    System.out.println("vecinos abajo: " + Arrays.toString(neighboursDown));
                    if (neighboursDown != null) {
                        SearchNeighbours(neighboursDown, currentIds, cim, rc);
                    }
                }

                // vecino izquierda
                if(j != 0) { // si no es la primera columna
                    Integer[] neighboursLeft = matrix[i][j - 1];
                    System.out.println("vecinos izquierda: " + Arrays.toString(neighboursLeft));
                    if (neighboursLeft != null) {
                        SearchNeighbours(neighboursLeft, currentIds, cim, rc);
                    }
                }

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
                if(i != 0 && j != 0) { //si no es la primer fila
                    Integer[] neighboursUpLeft = matrix[i - 1][j - 1];
                    System.out.println("vecinos arriba izquierda: " + Arrays.toString(neighboursUpLeft));
                    if (neighboursUpLeft != null) {
                        SearchNeighbours(neighboursUpLeft, currentIds, cim, rc);
                    }
                }

                // vecino arriba derecha
                if(i != 0) { // si no es la primera fila
                    if (j < M - 1) { //si no es extremo
                        Integer[] neighboursUpRight = matrix[i - 1][j + 1];
                        System.out.println("vecinos arriba derecha: " + Arrays.toString(neighboursUpRight));
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
                        System.out.println("periodic vecinos arriba izquierda: " + Arrays.toString(neighboursUpLeft));
                        if (neighboursUpLeft != null) {
                            SearchNeighbours(neighboursUpLeft, currentIds, cim, rc);
                        }
                    }

                    // arriba
                    Integer[] neighboursUp = matrix[aux_i][j];
                    System.out.println("periodic vecinos arriba: " + Arrays.toString(neighboursUp));
                    if (neighboursUp != null) {
                        SearchNeighbours(neighboursUp, currentIds, cim, rc);
                    }

                    if(j != M-1) {
                        // arriba derecha
                        Integer[] neighboursUpRight = matrix[aux_i][j + 1];
                        System.out.println("periodic vecinos arriba derecha: " + Arrays.toString(neighboursUpRight));
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
                    System.out.println("periodic vecinos izquierda: " + Arrays.toString(neighboursL));
                    if(neighboursL != null) {
                        SearchNeighbours(neighboursL, currentIds, cim, rc);
                    }

                    //arriba
                    Integer[] neighboursD = matrix[i-1][aux_j];
                    System.out.println("periodic vecinos arriba: " + Arrays.toString(neighboursD));
                    if(neighboursD != null) {
                        SearchNeighbours(neighboursD, currentIds, cim, rc);
                    }

                    //abajo
                    if(i != M-1) {
                        Integer[] neighbours3 = matrix[i + 1][aux_j];
                        System.out.println("periodic vecinos abajo: " + Arrays.toString(neighbours3));
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
                    System.out.println("periodic vecinos izquierda: " + Arrays.toString(neighboursAbI3));
                    if(neighboursAbI3 != null) {
                        SearchNeighbours(neighboursAbI3, currentIds, cim, rc);
                    }
                    Integer[] neighboursAbI4 = matrix[i-1][M-1];
                    System.out.println("periodic vecinos arriba izquierda: " + Arrays.toString(neighboursAbI4));
                    if(neighboursAbI4 != null) {
                        SearchNeighbours(neighboursAbI4, currentIds, cim, rc);
                    }
                    Integer[] neighboursAbI5 = matrix[0][M-1];
                    System.out.println("periodic vecinos abajo izquierda: " + Arrays.toString(neighboursAbI5));
                    if(neighboursAbI5 != null) {
                        SearchNeighbours(neighboursAbI5, currentIds, cim, rc);
                    }
                }

                //extremo esquina arriba derecha
                if(periodic && i==0 && j==M-1){
                    int aux_i = M-1;
                    Integer[] neighboursArD3 = matrix[i][0];
                    System.out.println("vecino derecha" + Arrays.toString(neighboursArD3));
                    if(neighboursArD3 != null) {
                        SearchNeighbours(neighboursArD3, currentIds, cim, rc);
                    }
                    Integer[] neighboursArD4 = matrix[i+1][0];
                    System.out.println("vecino derecha abajo" + Arrays.toString(neighboursArD4));
                    if(neighboursArD4 != null) {
                        SearchNeighbours(neighboursArD4, currentIds, cim, rc);
                    }
                    Integer[] neighboursArD5 = matrix[aux_i][0];
                    System.out.println("vecino arriba derecha" + Arrays.toString(neighboursArD5));
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
