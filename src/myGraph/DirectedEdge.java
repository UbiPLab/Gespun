package myGraph;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2022/2/28 9:28
 * @Version 1.0
 */
/*
 * 带权重的有向边数据类型
 * */
public class DirectedEdge implements Comparable<DirectedEdge>{
    //  start point
    private final int v;
    //  end point
    private final int w;
    //  edge weight
    private final double weight;
    //  init
    public DirectedEdge(int v, int w, double weight){
        this.v = v;
        this.w = w;
        this.weight = weight;
    }
    //  get start point
    public int from(){
        return v;
    }
    //  get end point
    public int to(){
        return w;
    }
    //  get edge weight
    public double weight(){
        return this.weight;
    }

    //  compare edges,  weight
    @Override
    public int compareTo(DirectedEdge that) {
        double cmp = this.weight - that.weight;
        if(cmp < 0.0)      return -1;
        else if(cmp > 0.0) return +1;
        return 0;
    }
    //  output edge
    public String toString() {
        return String.format("%d-%d %.2f", v, w, weight);
    }


    public static void main(String[] args) {
        DirectedEdge edge1 = new DirectedEdge(0, 1, 0.5);
        System.out.println(edge1.v);
        System.out.println(edge1.from());
        System.out.println(edge1.to());
        System.out.println(edge1);
        DirectedEdge edge2 = new DirectedEdge(1, 2, 0.7);
        System.out.println(edge1.compareTo(edge2));
    }
}

