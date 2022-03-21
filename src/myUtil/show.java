package myUtil;

import java.math.BigInteger;
import java.util.List;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2022/2/28 10:48
 * @Version 1.0
 */
public class show {

    public void show_StringList(List<String> list){
        for (String str:list
             ) {
            System.out.print(str+",");
        }
        System.out.println();
    }

    public void show_StringMatrix(String[][] matrix){
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j]+" ");
            }
            System.out.println();
        }
    }

    public void show_IntMatrix(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j]+" ");
            }
            System.out.println();
        }
    }

    public void show_ByteMatrix(byte[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j]+" ");
            }
            System.out.println();
        }
    }

    public void show_ByteArr(byte[] arr){
        for (int i = 0; i < arr.length-1; i++) {
            System.out.print(arr[i]+",");
        }
        System.out.println(arr[arr.length-1]);
    }

    public void show_IntArr(int[] arr){
        for (int i = 0; i < arr.length-1; i++) {
            System.out.print(arr[i]+",");
        }
        System.out.println(arr[arr.length-1]);
    }

    public void show_arr(Object[] arr){
        for (int i = 0; i < arr.length-1; i++) {
            System.out.print(arr[i]+",");
        }
        System.out.println(arr[arr.length-1]);
    }

    public void doubleMatrix(double[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j]+" ");
            }
            System.out.println();
        }
    }

    public void show_StringArr(String[] arr) {
        for (int i = 0; i < arr.length-1; i++) {
            System.out.print(arr[i]+",");
        }
        System.out.println(arr[arr.length-1]);
    }

    public void show_BigintArr(BigInteger[] arr) {
        for (int i = 0; i < arr.length-1; i++) {
            System.out.print(arr[i]+",");
        }
        System.out.println(arr[arr.length-1]);
    }

    public long show_longArr(long[] arr) {
        long sum = 0;
        for (int i = 0; i < arr.length-1; i++) {
            sum = sum + arr[i];
            System.out.print(arr[i]+",");
        }
        System.out.println(arr[arr.length-1]);
        sum = sum + arr[arr.length-1];
        return sum;
    }
}
