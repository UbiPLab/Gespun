package myUtil;

import java.util.Random;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2022/3/3 15:55
 * @Version 1.0
 */
public class pi {

    public int[] array;
    public int[] array_B ;

    public pi(int totalNumber){
        array = new int[totalNumber];
        array = randomArray(0, totalNumber-1, totalNumber);


        array_B = new int[totalNumber];
        for (int i = 0; i < totalNumber; i++) {
            array_B[array[i]] = i;
        }
    }

    public int Permutation_to(int index){
        return array[index];
    }

    public int Permutation_from(int index){
        return array_B[index];
    }

    public int Permutation(int index){
        return array[index];
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
