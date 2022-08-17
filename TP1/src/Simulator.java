import java.io.IOException;  // Import the IOException class to handle errors
import java.util.*;
import java.io.FileWriter;   // Import the FileWriter class

public class Simulator {

    public static int N = 0;
    public static int L = 0;
    public static double r_max = 0;
    static int time = 0;
    static boolean bruteForce = true;

    static ArrayList<Particle> particlesArray = new ArrayList<>();
    // radio propiedad x y

    public static void main(String[] args) throws IOException {
        Parser.ParseParameters(args[0], args[1], r_max, particlesArray);
        long startTime = System.currentTimeMillis();

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
        System.out.println("Execution time: " + elapsedTime + " milliseconds");

        if(bruteForce) {
            long startTime2 = System.currentTimeMillis();
            Map<Integer, List<Integer>> particlesNeighbours = new HashMap<Integer, List<Integer>>();
            if(periodic){
                BruteForcePeriodic(particlesArray,rc,particlesNeighbours, L);
            }
            else {
                BruteForce(particlesArray, rc, particlesNeighbours);
            }
            System.out.println("Vecinos fuerza bruta" + particlesNeighbours);
            long stopTime2 = System.currentTimeMillis();
            long elapsedTime2 = stopTime2 - startTime2;
            System.out.println("Execution time of brute-force: " + elapsedTime2 + " milliseconds");
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

    public static void BruteForcePeriodic(ArrayList<Particle> particlesArray, int rc, Map<Integer, List<Integer>> particlesNeighbours, int L){
        for (Particle currentParticle: particlesArray) { //1 2 3 4 5 6
            for(Particle neighbourParticle : particlesArray){
                if (currentParticle.getId() != neighbourParticle.getId()) {
                    double edge = GetEdge(neighbourParticle, currentParticle);
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
                    if(!ids.equals(particleId)) {
                        cim.putIfAbsent(ids, new ArrayList<Integer>());
                        cim.get(ids).add(particleId);
                    }
                }
            }
        }
    }

    public static void SearchNeighboursPeriodic(Integer[] neighbours, Integer[] currentIds, Map<Integer, List<Integer>> cim, int rc, int num,int id){
        for (Integer particleId : neighbours) {
            Particle neighbourParticle = particlesArray.get(particleId);
            for (Integer ids : currentIds) {
                Particle currentParticle = particlesArray.get(ids);
                double edge = GetEdge(neighbourParticle, currentParticle);
                if (edge <= rc) {
                    cim.putIfAbsent(ids, new ArrayList<Integer>());
                    if(!cim.get(ids).contains(particleId)) {
                        cim.get(ids).add(particleId);
                    }
                }
            }
        }
    }

    public static double GetEdge(Particle neighbourParticle,Particle currentParticle){
        double auxX = Math.abs(neighbourParticle.getX() - currentParticle.getX());
        double X = Double.min(auxX, L - auxX);
        final double auxY = Math.abs(neighbourParticle.getY() - currentParticle.getY());
        final double Y = Double.min(auxY, L - auxY);
        double distance = Math.sqrt(Math.pow(X, 2) + Math.pow(Y, 2));
        return distance - neighbourParticle.getRadio() - currentParticle.getRadio();
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

        for(int i = 0; i < M; i++){ //fila
            for(int j = 0; j < M; j++){ //columna
                Integer[] currentIds = matrix[i][j];
                if(currentIds == null){
                    continue;
                }

                // agrego de vecino a los que estan en la misma celda que cumplen con la condicion
                SearchNeighbours(currentIds, currentIds, cim, rc);

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
                    int aux_i = (i == 0)? (M-1) : 0;
                    // arriba izquierda
                    if(j != 0) { //i != 0 && i != M-1 ||
                        Integer[] neighboursUpLeft = matrix[aux_i][j-1];
                        if (neighboursUpLeft != null) {
                            SearchNeighbours(neighboursUpLeft, currentIds, cim, rc);
                        }
                    }

                    // arriba
                    Integer[] neighboursUp = matrix[aux_i][j];
                    if (neighboursUp != null) {
                        SearchNeighboursPeriodic(neighboursUp, currentIds, cim, rc,2,4);
                        if(i==M-1 && j==M-1){
                            SearchNeighboursPeriodic(neighboursUp, currentIds, cim, rc,1,3);
                        }
                        if((i==0 && j==M-1) || (i==0 && j<M-1 && j!=0)){
                            SearchNeighboursPeriodic(neighboursUp, currentIds, cim, rc,4,2);
                        }
                        if(i==0 && j==0){
                            SearchNeighboursPeriodic(neighboursUp, currentIds, cim, rc,3,1);
                        }
                    }

                    // arriba derecha
                    if(j != M-1) {
                        Integer[] neighboursUpRight = matrix[aux_i][j + 1];
                        if (neighboursUpRight != null) {
                            SearchNeighbours(neighboursUpRight, currentIds, cim, rc);
                            SearchNeighboursPeriodic(neighboursUpRight,currentIds,cim,rc,1,3);
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
                        if(i<(M-1) && (j==M-1 || j==0)){
                            SearchNeighboursPeriodic(neighboursL, currentIds, cim, rc);
                        }
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
                            if(j==M-1){
                                SearchNeighboursPeriodic(neighbours3, currentIds, cim, rc,1,2);
                            }
                        }
                    }
                }

                //extremo esquina arriba izquierda
                if(periodic && j==0 && i==0){
                    int aux_j = M-1 ;
                    int aux_i = M-1;
                    Integer[] neighbours3 = matrix[i][aux_j];
                    if(neighbours3 != null) {
                        SearchNeighboursPeriodic(neighbours3, currentIds, cim, rc,2,1);
                    }
                    Integer[] neighbours4 = matrix[i+1][aux_j];
                    if(neighbours4 != null) {
                        SearchNeighboursPeriodic(neighbours4, currentIds, cim, rc,6,1);
                    }
                    Integer[] neighbours5 = matrix[aux_i][aux_j];
                    if(neighbours5 != null) {
                        SearchNeighboursPeriodic(neighbours5, currentIds, cim, rc,4,1);
                    }
                }

                //extremo esquina abajo izquierda
                if(periodic && j==0 && i==M-1){
                    Integer[] neighboursAbI3 = matrix[i][M-1];
                    if(neighboursAbI3 != null) {
                        SearchNeighboursPeriodic(neighboursAbI3, currentIds, cim, rc,4,3);
                    }
                    Integer[] neighboursAbI4 = matrix[i-1][M-1];
                    if(neighboursAbI4 != null) {
                        SearchNeighboursPeriodic(neighboursAbI4, currentIds, cim, rc,6,3);
                    }
                    Integer[] neighboursAbI5 = matrix[0][M-1];
                    if(neighboursAbI5 != null) {
                        SearchNeighboursPeriodic(neighboursAbI5, currentIds, cim, rc,2,3);
                    }
                }

                //extremo esquina arriba derecha
                if(periodic && i==0 && j==M-1){
                    int aux_i = M-1;
                    Integer[] neighboursArD3 = matrix[i][0];
                    if(neighboursArD3 != null) {
                        SearchNeighboursPeriodic(neighboursArD3, currentIds, cim, rc,1,2);
                    }
                    Integer[] neighboursArD4 = matrix[1][0];
                    if(neighboursArD4 != null) {
                        SearchNeighboursPeriodic(neighboursArD4, currentIds, cim, rc,5,2);
                    }
                    Integer[] neighboursArD5 = matrix[aux_i][0];
                    if(neighboursArD5 != null) {
                        SearchNeighboursPeriodic(neighboursArD5, currentIds, cim, rc,3,2);
                    }
                }

                //extremo esquina abajo derecha
                if(periodic && i==M-1 && j==M-1){
                    Integer[] neighboursAbD3 = matrix[i][0];
                    if(neighboursAbD3 != null) {
                        SearchNeighboursPeriodic(neighboursAbD3, currentIds, cim, rc,3,4);
                    }
                    Integer[] neighboursAbD4 = matrix[i-1][0];
                    if(neighboursAbD4 != null) {
                        SearchNeighboursPeriodic(neighboursAbD4, currentIds, cim, rc,5,4);
                    }
                    Integer[] neighboursAbD5 = matrix[0][0];
                    if(neighboursAbD5 != null) {
                        SearchNeighboursPeriodic(neighboursAbD5, currentIds, cim, rc,1,4);
                    }
                }
            }
        }
    }
}
