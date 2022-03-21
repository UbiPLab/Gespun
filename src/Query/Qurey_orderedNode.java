package Query;

import CryptoSystem.DGK;
import myUtil.*;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



/**
 * @Author DELL
 * @Date 2022/3/16 16:16
 * @Version 1.0
 */
public class Qurey_orderedNode {

    public boolean k_query(int[] q_1, int[] q_2, byte[][] q_3,
                           byte[][] DX, byte[][] Arr, String[] Arr_r, pi pi, BigInteger[] pk, DGK dgk,
                           BigInteger[] SD,List<List<byte[]>> SP
                           ) {
        boolean result = false;
//        //  init shortest distance
//        SD = new BigInteger[(int) new DataFormat().factorial(q_1.length - 1)];
//        Arrays.fill(SD, dgk.Encryption(new BigInteger(String.valueOf(1)),pk));
//        //  init shortest path
//        SP = new ArrayList<>();
        //  full permutation of the k unordered nodes
        int[] nodes_index = new int[q_1.length - 1];
        for (int i = 0; i < nodes_index.length; i++) {
            nodes_index[i] = i+1;
        }
        int[][] full_permut = new Permut().permute(nodes_index, 0);

        for (int i = 0; i < full_permut.length; i++) {
            List<BigInteger> Esd = new ArrayList<>();
            List<byte[]> Esp = new ArrayList<>();
            boolean temp_tag = false;
            if (new Query_noUnorderedNode().normal_query(q_1[0], q_2[0],q_3[full_permut[i][0] - 1], DX, Arr, Arr_r,pi,Esd, Esp)) {

                for (int j = 0; j < nodes_index.length - 1; j++) {
                    temp_tag = new Query_noUnorderedNode().normal_query(q_1[full_permut[i][j]], q_2[full_permut[i][j]], q_3[full_permut[i][j + 1] - 1], DX, Arr, Arr_r, pi,Esd, Esp);
                    if (!temp_tag) {
                        break;
                    }
                }
                temp_tag =  new Query_noUnorderedNode().normal_query(q_1[full_permut[i][nodes_index.length - 1]], q_2[full_permut[i][nodes_index.length - 1]], q_3[q_3.length - 1], DX, Arr, Arr_r,pi, Esd, Esp);

            }
            if (temp_tag){
                BigInteger add = Esd.get(0);
                for (int j = 1; j < Esd.size(); j++) {
                    add = add.multiply(Esd.get(j)).mod(pk[0]);
                }
                SD[i] = add;
                SP.add(Esp);
                result = true;
            }
        }
        return result;
    }

}
