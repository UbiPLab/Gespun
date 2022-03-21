package Setup;

import myGraph.EdgeWeightedDigraph;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author DELL
 * @Date 2022/3/20 21:58
 * @Version 1.0
 */
public class setup_FloydWarshall {
    public double[][] adjTable;
    public List<List<String>> shortestPathMatrix;
    public double[][] shortestDistanceMatrix;

    /**
     * floyd-warshall
     * @param EWD   graph
     * @param node_names    name of nodes
     */
    public setup_FloydWarshall(EdgeWeightedDigraph EWD,String[] node_names) {
        //  generate adjTable of the graph
        adjTable = EWD.getAdjTable();
        //  use floyd-warshall to get: shortestDistanceMatrix,shortestPathMatrix
        //  shortestPathMatrix
        shortestPathMatrix = new ArrayList<>();
        //  shortestDistanceMatrix
        shortestDistanceMatrix = EWD.floyd(adjTable,node_names,shortestPathMatrix);

    }
}
