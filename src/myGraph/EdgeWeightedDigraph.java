package myGraph;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2022/2/28 9:27
 * @Version 1.0
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EdgeWeightedDigraph {
    private final int V;
    private int E;
    private List<DirectedEdge> adj;
//    private Bag<DirectedEdge>[] adj;
    public EdgeWeightedDigraph(int V){
        this.V = V;
        this.E = 0;
        adj = new ArrayList<DirectedEdge>();
    }
    public EdgeWeightedDigraph(int nodeNum,String[][] edges){
        this.V = nodeNum;
        this.E = edges.length;
        this.adj = new ArrayList<DirectedEdge>();
        for(int e = 0; e < E; e++){
            int v = Integer.parseInt(edges[e][0]);
            int w = Integer.parseInt(edges[e][1]);
            double weight = Double.parseDouble(edges[e][2]);
            adj.add(new DirectedEdge(v, w, weight));
        }
    }
    public void addEdge(DirectedEdge e){
        adj.add(e);
        E++;
    }
//    public Iterable<DirectedEdge> adj(int v){
//        return adj.get(v);
//    }
//    public Iterable<DirectedEdge> edges(){
//        Bag<DirectedEdge> bag = new Bag<DirectedEdge>();
//        for(int v = 0; v < V; v++)
//            for(DirectedEdge e : adj[v])
//                bag.add(e);
//        return bag;
//    }
    public double[][] getAdjTable(){
        double[][] adjTable = new double[V][V];
        for (int i = 0; i < adjTable.length; i++) {
            Arrays.fill(adjTable[i],99);
        }
        for (DirectedEdge edge:adj
             ) {
            adjTable[edge.from()][edge.to()] = edge.weight();
        }

        return adjTable;
    }

    public int getV(){
        return this.V;
    }
    public int getE(){
        return this.E;
    }
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(int v = 0; v < V; v++){
            sb.append(v);
            sb.append(" : ");
            for(DirectedEdge e : adj){
                sb.append(e.toString());
                sb.append("  ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public double[][] floyd(double[][] graph,String[] names,List<List<String>> paths) {
        // init
        double[][] dist = graph.clone();
//        List<List<String>> paths = new ArrayList<>();
        //init
        for (int i = 0; i < graph.length; i++) {
            List<String> listString = new ArrayList<>();
            for (int j = 0; j < graph.length; j++) {
                listString.add("");
                if (dist[i][j] == 0) {
                    dist[i][j] = Integer.MAX_VALUE;
                }
            }
            paths.add(listString);
        }
        for (int k = 0; k < graph.length; k++) {
            for (int i = 0; i < graph.length; i++) {
                for (int j = 0; j < graph.length; j++) {
                    // different path
                    if (dist[i][k] == Integer.MAX_VALUE || dist[k][j] == Integer.MAX_VALUE) {
                        continue;
                    }
                    double haveChangeLen = dist[i][k] + dist[k][j];
                    // if go to k nodes,path will short
                    if (dist[i][j] > haveChangeLen) {
                        dist[i][j] = haveChangeLen;
                        paths.get(i).set(j, paths.get(i).get(k) + names[k] + "--");
                    }
                }
            }
        }
//        // print
//        for (int i = 0; i < graph.length; i++) {
//            System.out.println("from" + names[i] + "shortest path:");
//            for (int j = 0; j < graph.length; j++) {
//                System.out.print("to" + names[j] + "shortest distance" + dist[i][j] + "   ");
//                System.out.println("path:" + names[i] + "--" + paths.get(i).get(j) + names[j]);
//            }
//        }
        return dist;
    }

    public static int[] getMidPath(String path){
        String[] str = path.split("--");
        int[] result = new int[str.length];
        for (int i = 0; i < str.length; i++) {
            if ("".equals(str[i])){
                continue;
            }
            result[i] = Integer.parseInt(str[i]);
        }
        return result;
    }




}

