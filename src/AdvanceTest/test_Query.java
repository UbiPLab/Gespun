package AdvanceTest;

import LocalParameter.LocalSetting;
import Query.Query_noUnorderedNode;
import Query.Qurey_orderedNode;
import Setup.setup_FloydWarshall;
import Setup.setup_createDXandArr;
import Setup.setup_createGraph;
import Setup.setup_genSecretKey;
import Token.token_generation;
import myUtil.DataFormat;
import myUtil.show;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author DELL
 * @Date 2022/3/21 10:25
 * @Version 1.0
 */
public class test_Query {
    public static void main(String[] args) {
        int numberOfNodes = 1005;
        setup_createGraph myGraph = new setup_createGraph(LocalSetting.dataSetAddress_1005, numberOfNodes);
        setup_FloydWarshall myfloydWarrshall = new setup_FloydWarshall(myGraph.EWD,myGraph.node_names);

        setup_genSecretKey mygenSecretKey = new setup_genSecretKey();

        setup_createDXandArr mycreateDXandArr = new setup_createDXandArr(numberOfNodes,mygenSecretKey.K,mygenSecretKey.dgk,mygenSecretKey.pk,myfloydWarrshall.shortestPathMatrix,myfloydWarrshall.shortestDistanceMatrix);

        //  without k ordered nodes

        int start_point = 0;
        int end_point = 8;
        token_generation mytoken = new token_generation();
        mytoken.token_generation_un_midnode(start_point,end_point,mycreateDXandArr.pseudo, mygenSecretKey.K);

        long start_query_un_time = System.nanoTime();
        List<BigInteger> shortestDistance = new ArrayList<>();
        List<byte[]> shortestPath = new ArrayList<>();
        new Query_noUnorderedNode().normal_query(mytoken.q_1[0],mytoken.q_2[0],mytoken.q_3[0],mycreateDXandArr.DX,mycreateDXandArr.Arr,mycreateDXandArr.Arr_r,mycreateDXandArr.pi,shortestDistance,shortestPath );
        long end_query_un_time = System.nanoTime();
        long query_un_time = end_query_un_time -start_query_un_time;
        System.out.println("query_un_time:"+query_un_time);

        //  with k ordered nodes
        int k = 2;
        int[] nodes = mytoken.createInt_k(start_point,end_point,k);
        mytoken.token_generation_k_midnode(start_point,end_point,nodes,mycreateDXandArr.pseudo, mygenSecretKey.K);

        long start_query_k_time = System.nanoTime();
        BigInteger[] SD = new BigInteger[(int) new DataFormat().factorial(k)];
        Arrays.fill(SD, mygenSecretKey.dgk.Encryption(new BigInteger(String.valueOf(1)),mygenSecretKey.pk));
        List<List<byte[]>> SP = new ArrayList<>();
        new Qurey_orderedNode().k_query(mytoken.q_1, mytoken.q_2, mytoken.q_3,mycreateDXandArr.DX,mycreateDXandArr.Arr,mycreateDXandArr.Arr_r,mycreateDXandArr.pi,mygenSecretKey.pk,mygenSecretKey.dgk,SD,SP);
        long end_query_k_time = System.nanoTime();
        long query_k_time = end_query_k_time -start_query_k_time;
        System.out.println("query_k_time:"+query_k_time);
    }
}
