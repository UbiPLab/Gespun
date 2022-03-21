package AdvanceTest;


import LocalParameter.LocalSetting;
import Setup.setup_createGraph;
import Setup.setup_FloydWarshall;

/**
 * @Author DELL
 * @Date 2022/3/21 9:45
 * @Version 1.0
 */
public class test_FloydWarshall {
    public static void main(String[] args) {
        int numberOfNodes = 1005;
        setup_createGraph myGraph = new setup_createGraph(LocalSetting.dataSetAddress_1005, numberOfNodes);

        long start_floyd_time = System.nanoTime();
        setup_FloydWarshall myfloydWarrshall = new setup_FloydWarshall(myGraph.EWD, myGraph.node_names);
        long end_floyd_time = System.nanoTime();
        long floyd_time = end_floyd_time - start_floyd_time;
        System.out.println("floyd_time:" + floyd_time);
    }
}
