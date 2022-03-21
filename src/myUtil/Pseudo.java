package myUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2022/FuctionKey/FuctionKey 10:14
 * @Version 1.0
 */
public class Pseudo {
    public int[][] array;
    public int[][] array_B;
    public int N;
    public int FuctionKey = 2;

    public Pseudo(int totalNumber){
        N = totalNumber;
        array = new int[FuctionKey][totalNumber];
        for (int i = 0; i < FuctionKey; i++) {
            array[i] = randomArray(0, totalNumber-1, totalNumber);
        }

        array_B = new int[FuctionKey][totalNumber];

        for (int i = 0; i < FuctionKey; i++) {
            for (int j = 0; j < totalNumber; j++) {
                array_B[i][array[i][j]] = j;
            }
        }
    }

    public Pseudo(int totalNumber, int[][] array, int[][] array_B){
        N = totalNumber;
        this.array = array;
        this.array_B = array_B;

    }

    public int PRP_to(String key,int index){
        return array[Integer.valueOf(key.charAt(FuctionKey)) % FuctionKey][index];
    }

    public int PRP_from(String key,int index){
        return array_B[Integer.valueOf(key.charAt(FuctionKey)) % FuctionKey][index];
    }

    public int PRF(String key,int index){
        return array[Integer.valueOf(key.charAt(FuctionKey)) % FuctionKey][index];
    }

    public static int[] randomArray(int min,int max,int n){
        int len = max-min+1;
        if(max < min || n > len){
            return null;
        }
        int[] source = new int[len];
        for (int i = min; i < min+len; i++){
            source[i-min] = i;
        }
        int[] result = new int[n];
        Random rd = new Random();
        int index = 0;
        for (int i = 0; i < result.length; i++) {
            index = Math.abs(rd.nextInt() % len--);
            result[i] = source[index];
            source[index] = source[len];
        }
        return result;
    }
}
