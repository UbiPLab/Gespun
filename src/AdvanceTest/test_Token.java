package AdvanceTest;


import Token.token_generation;
import myUtil.HashFounction;
import myUtil.Pseudo;
import myUtil.show;

/**
 * @Author DELL
 * @Date 2022/3/21 10:11
 * @Version 1.0
 */
public class test_Token {
    public static void main(String[] args) {
        int numberOfNodes = 1005;
        Pseudo pseudo = new Pseudo(numberOfNodes);
        String[] K = HashFounction.CreateSecretKey(2);
        //  without k ordered nodes
        int start_point = 0;
        int end_point = 8;
        token_generation mytoken = new token_generation();
        mytoken.token_generation_un_midnode(start_point,end_point,pseudo,K);
        System.out.println("q_1:"+mytoken.q_1[0]);
        System.out.println("q_2:"+mytoken.q_2[0]);
        System.out.println("q_3:");
        new show().show_ByteArr(mytoken.q_3[0]);
        //  with k ordered nodes
        int k = 2;
        int[] nodes = mytoken.createInt_k(start_point,end_point,k);
        mytoken.token_generation_k_midnode(start_point,end_point,nodes,pseudo,K);
        System.out.println("q_1:");
        new show().show_IntArr(mytoken.q_1);
        System.out.println("q_2:");
        new show().show_IntArr(mytoken.q_2);
        System.out.println("q_3:");
        new show().show_ByteMatrix(mytoken.q_3);
    }
}
