package methods;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CIM {

    public static void SearchNeighbours(Integer[] neighbours, Integer[] currentIds, Map<Integer, List<Integer>> cim, int rc, ArrayList<Particle> particlesArray){
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

    public static void SearchNeighboursPeriodic(Integer[] neighbours, Integer[] currentIds, Map<Integer, List<Integer>> cim, int rc, ArrayList<Particle> particlesArray, int L){
        for (Integer particleId : neighbours) {
            Particle neighbourParticle = particlesArray.get(particleId);
            for (Integer ids : currentIds) {
                Particle currentParticle = particlesArray.get(ids);
                double edge = GetEdge(neighbourParticle, currentParticle, L);
                if (edge <= rc) {
                    cim.putIfAbsent(ids, new ArrayList<Integer>());
                    if(!cim.get(ids).contains(particleId)) {
                        cim.get(ids).add(particleId);
                    }
                }
            }
        }
    }

    public static double GetEdge(Particle neighbourParticle,Particle currentParticle, int L){
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

    public static void CellIndexMethod(int M, int rc, Map<Integer, List<Integer>> cim, Integer[][][] matrix, boolean periodic,int L, ArrayList<Particle> particlesArray){

        for(int i = 0; i < M; i++){ //fila
            for(int j = 0; j < M; j++){ //columna
                Integer[] currentIds = matrix[i][j];
                if(currentIds == null){
                    continue;
                }

                // agrego de vecino a los que estan en la misma celda que cumplen con la condicion
                SearchNeighbours(currentIds, currentIds, cim, rc, particlesArray);

                // vecino arriba
                if( i != 0 ) {  // si no es la primer fila
                    Integer[] neighbours = matrix[i - 1][j];
                    if (neighbours != null) {
                        SearchNeighbours(neighbours, currentIds, cim, rc, particlesArray);
                    }
                }

                // vecino abajo
                if(i < M-1) { // si no es extremo de abajo
                    Integer[] neighboursDown = matrix[i + 1][j];
                    if (neighboursDown != null) {
                        SearchNeighbours(neighboursDown, currentIds, cim, rc, particlesArray);
                    }
                }

                // vecino izquierda
                if(j != 0) { // si no es la primera columna
                    Integer[] neighboursLeft = matrix[i][j - 1];
                    if (neighboursLeft != null) {
                        SearchNeighbours(neighboursLeft, currentIds, cim, rc, particlesArray);
                    }
                }

                // vecino derecha
                if (j < M-1) { // si no es un extremo
                    Integer[] neighboursRight = matrix[i][j + 1];
                    if (neighboursRight != null) {
                        SearchNeighbours(neighboursRight, currentIds, cim, rc, particlesArray);
                    }
                }

                // vecino abajo izquierda
                if(i < M-1 && j != 0){ // si no es extremo
                    Integer[] neighboursDownLeft = matrix[i + 1][j - 1];
                    if(neighboursDownLeft != null) {
                        SearchNeighbours(neighboursDownLeft, currentIds, cim, rc, particlesArray);
                    }
                }

                // vecino abajo derecha
                if(j != M-1 && i != M-1) { // si no es un extremo // i = 9
                    Integer[] neighboursDownRight = matrix[i + 1][j + 1];
                    if (neighboursDownRight != null) {
                        SearchNeighbours(neighboursDownRight, currentIds, cim, rc, particlesArray);
                    }
                }

                // vecino arriba izquierda
                if(i != 0 && j != 0) { //si no es la primer fila
                    Integer[] neighboursUpLeft = matrix[i - 1][j - 1];
                    if (neighboursUpLeft != null) {
                        SearchNeighbours(neighboursUpLeft, currentIds, cim, rc, particlesArray);
                    }
                }

                // vecino arriba derecha
                if(i != 0) { // si no es la primera fila
                    if (j < M - 1) { //si no es extremo
                        Integer[] neighboursUpRight = matrix[i - 1][j + 1];
                        if (neighboursUpRight != null) {
                            SearchNeighbours(neighboursUpRight, currentIds, cim, rc, particlesArray);
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
                            SearchNeighbours(neighboursUpLeft, currentIds, cim, rc, particlesArray);
                        }
                    }

                    // arriba
                    Integer[] neighboursUp = matrix[aux_i][j];
                    if (neighboursUp != null) {
                        SearchNeighboursPeriodic(neighboursUp, currentIds, cim, rc, particlesArray, L);
                        if(i==M-1 && j==M-1){
                            SearchNeighboursPeriodic(neighboursUp, currentIds, cim, rc, particlesArray, L);
                        }
                        if((i==0 && j==M-1) || (i==0 && j<M-1 && j!=0)){
                            SearchNeighboursPeriodic(neighboursUp, currentIds, cim, rc, particlesArray, L);
                        }
                        if(i==0 && j==0){
                            SearchNeighboursPeriodic(neighboursUp, currentIds, cim, rc, particlesArray, L);
                        }
                    }

                    // arriba derecha
                    if(j != M-1) {
                        Integer[] neighboursUpRight = matrix[aux_i][j + 1];
                        if (neighboursUpRight != null) {
                            SearchNeighbours(neighboursUpRight, currentIds, cim, rc, particlesArray);
                            SearchNeighboursPeriodic(neighboursUpRight,currentIds,cim,rc, particlesArray, L);
                        }
                    }
                }

                // si es la primera columna o la ultima && condiciones periodicas
                if(periodic && (j == 0 || j == M-1) && i != 0){
                    int aux_j = (j == 0)? M-1 : 0;

                    //izquierda
                    Integer[] neighboursL = matrix[i][aux_j];
                    if(neighboursL != null) {
                        SearchNeighbours(neighboursL, currentIds, cim, rc, particlesArray);
                        if(i<(M-1) && (j==M-1 || j==0)){
                            SearchNeighboursPeriodic(neighboursL, currentIds, cim, rc, particlesArray, L);
                        }
                    }

                    //arriba
                    Integer[] neighboursD = matrix[i-1][aux_j];
                    if(neighboursD != null) {
                        SearchNeighbours(neighboursD, currentIds, cim, rc, particlesArray);
                    }

                    //abajo
                    if(i != M-1) {
                        Integer[] neighbours3 = matrix[i + 1][aux_j];
                        if (neighbours3 != null) {
                            SearchNeighbours(neighbours3, currentIds, cim, rc, particlesArray);
                            if(j==M-1){
                                SearchNeighboursPeriodic(neighbours3, currentIds, cim, rc, particlesArray, L);
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
                        SearchNeighboursPeriodic(neighbours3, currentIds, cim, rc, particlesArray, L);
                    }
                    Integer[] neighbours4 = matrix[i+1][aux_j];
                    if(neighbours4 != null) {
                        SearchNeighboursPeriodic(neighbours4, currentIds, cim, rc, particlesArray, L);
                    }
                    Integer[] neighbours5 = matrix[aux_i][aux_j];
                    if(neighbours5 != null) {
                        SearchNeighboursPeriodic(neighbours5, currentIds, cim, rc, particlesArray, L);
                    }
                }

                //extremo esquina abajo izquierda
                if(periodic && j==0 && i==M-1){
                    Integer[] neighboursAbI3 = matrix[i][M-1];
                    if(neighboursAbI3 != null) {
                        SearchNeighboursPeriodic(neighboursAbI3, currentIds, cim, rc, particlesArray, L);
                    }
                    Integer[] neighboursAbI4 = matrix[i-1][M-1];
                    if(neighboursAbI4 != null) {
                        SearchNeighboursPeriodic(neighboursAbI4, currentIds, cim, rc, particlesArray, L);
                    }
                    Integer[] neighboursAbI5 = matrix[0][M-1];
                    if(neighboursAbI5 != null) {
                        SearchNeighboursPeriodic(neighboursAbI5, currentIds, cim, rc, particlesArray, L);
                    }
                }

                //extremo esquina arriba derecha
                if(periodic && i==0 && j==M-1){
                    int aux_i = M-1;
                    Integer[] neighboursArD3 = matrix[i][0];
                    if(neighboursArD3 != null) {
                        SearchNeighboursPeriodic(neighboursArD3, currentIds, cim, rc, particlesArray, L);
                    }
                    Integer[] neighboursArD4 = matrix[1][0];
                    if(neighboursArD4 != null) {
                        SearchNeighboursPeriodic(neighboursArD4, currentIds, cim, rc, particlesArray, L);
                    }
                    Integer[] neighboursArD5 = matrix[aux_i][0];
                    if(neighboursArD5 != null) {
                        SearchNeighboursPeriodic(neighboursArD5, currentIds, cim, rc, particlesArray, L);
                    }
                }

                //extremo esquina abajo derecha
                if(periodic && i==M-1 && j==M-1){
                    Integer[] neighboursAbD3 = matrix[i][0];
                    if(neighboursAbD3 != null) {
                        SearchNeighboursPeriodic(neighboursAbD3, currentIds, cim, rc, particlesArray, L);
                    }
                    Integer[] neighboursAbD4 = matrix[i-1][0];
                    if(neighboursAbD4 != null) {
                        SearchNeighboursPeriodic(neighboursAbD4, currentIds, cim, rc, particlesArray, L);
                    }
                    Integer[] neighboursAbD5 = matrix[0][0];
                    if(neighboursAbD5 != null) {
                        SearchNeighboursPeriodic(neighboursAbD5, currentIds, cim, rc, particlesArray, L);
                    }
                }
            }
        }
    }
}
