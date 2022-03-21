package myUtil;

import java.util.Arrays;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2022/3/9 14:50
 * @Version 1.0
 */
public class Permut {
    public  int[][] permute(int[] array,int start){
        int[][] res = new int[(int) new DataFormat().factorial(array.length)][array.length];
        Arrays.sort(array);
        int index = 0;
        int count = 0;
        while(count < res.length) {
//        while(true) {
            printArray(res,array,count);
            count++;

            for(int i = array.length-2;i>=0;i--) {
                if(array[i]<array[i+1]) {
                    index=i;
                    break;
                }else if(i<=0) {
//                    return;
                }
            }

            for(int i=array.length-1;i>=0;i--) {
                if(array[i]>array[index]) {
                    swap(array,i,index);
                    break;
                }
            }

            reverse(array,index+1);
        }
        return res;
    }


    public  void reverse(int array[],int i)
    {
        int k=i,j=array.length-1;
        while(k<j)
        {
            swap(array,k,j);
            k++;
            j--;
        }
    }

    private  void swap(int[] array,int s,int i){
        int t=array[s];
        array[s]=array[i];
        array[i]=t;
    }

    private  void printArray(int[][] per,int[] array , int count) {
        for(int i=0;i<array.length;i++) {
            per[count][i] = array[i];
//            System.out.print(array[i]);
        }
//        System.out.print("\n");
    }
}
