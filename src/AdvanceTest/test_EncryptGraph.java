package AdvanceTest;

import LocalParameter.LocalSetting;
import Setup.setup_FloydWarshall;
import Setup.setup_createGraph;
import Setup.setup_genSecretKey;
import Setup.setup_createDXandArr;

/**
 * @Author DELL
 * @Date 2022/3/21 10:03
 * @Version 1.0
 */
public class test_EncryptGraph {
    public static void main(String[] args) {
        int numberOfNodes = 1005;
        setup_createGraph myGraph = new setup_createGraph(LocalSetting.dataSetAddress_1005, numberOfNodes);
        setup_FloydWarshall myfloydWarrshall = new setup_FloydWarshall(myGraph.EWD,myGraph.node_names);

        long start_encrypt_time = System.nanoTime();
        setup_genSecretKey mygenSecretKey = new setup_genSecretKey();

        setup_createDXandArr mycreateDXandArr = new setup_createDXandArr(numberOfNodes,mygenSecretKey.K,mygenSecretKey.dgk,mygenSecretKey.pk,myfloydWarrshall.shortestPathMatrix,myfloydWarrshall.shortestDistanceMatrix);
        long end_encrypt_time = System.nanoTime();
        long encrypt_time = end_encrypt_time - start_encrypt_time;
        System.out.println("encrypt_time:"+encrypt_time);


    }
}
