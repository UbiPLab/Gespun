package Setup;

import ReadFileData.ReadFiledata;
import myGraph.EdgeWeightedDigraph;
import myUtil.Pseudo;

import java.util.Arrays;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2022/3/1 10:07
 * @Version 1.0
 */
public class setup_createGraph {
    public String[][] edges;
    public int[] node_list;
    public String[] node_names;
    public EdgeWeightedDigraph EWD;
    /**
     * create graph from local disk
     * @param fileAddress   dataSet address
     * @param numberOfNodes     total number of nodes
     */
    public setup_createGraph(String fileAddress, int numberOfNodes) {
        //  read graph edges from dataset
        edges = new ReadFiledata().readArray_String(fileAddress);
        //  read graph nodes from dataset , node in range (0,numberOfNodes-1)
        node_list = Pseudo.randomArray(0,numberOfNodes-1,numberOfNodes);
        Arrays.sort(node_list);
        //  change to type of string
        node_names = new String[node_list.length];
        for (int i = 0; i < node_names.length; i++) {
            node_names[i] = String.valueOf(node_list[i]);
        }
        //  according to node-list and edges, create EdgeWeightedDigraph
        EWD = new EdgeWeightedDigraph(node_list.length,edges);

    }
}
