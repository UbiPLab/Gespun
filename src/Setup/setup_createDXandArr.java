package Setup;

import CryptoSystem.DGK;
import myUtil.DataFormat;
import myUtil.HashFounction;
import myUtil.pi;
import myUtil.Pseudo;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2022/3/1 15:39
 * @Version 1.0
 */
public class setup_createDXandArr {
    public byte[][] DX;
    public byte[][] Arr;
    public String[] Arr_r;
    public Pseudo pseudo;
    public pi pi;
    public String[] K_vi;
    public String[] K_vi_star;
    public setup_createDXandArr(int numberOfnodes,String[] K, DGK dgk, BigInteger[] pk, List<List<String>> shortestPathMatrix, double[][] shortestDistanceMatrix) {
        DX = new byte[numberOfnodes][];
        Arr = new byte[numberOfnodes * (numberOfnodes)][];
        Arr_r = new String[numberOfnodes * (numberOfnodes)];
        K_vi = HashFounction.CreateSecretKey(numberOfnodes);
        K_vi_star = HashFounction.CreateSecretKey(numberOfnodes);
        String[] randomNumber_vi = HashFounction.CreateSecretKey(numberOfnodes);
        pseudo = new Pseudo(numberOfnodes);
        pi = new pi(numberOfnodes*(numberOfnodes-1) + 1);
        int num = 0;
        for (int i = 0; i < shortestDistanceMatrix.length; i++) {
            // step 1. add_vi||k_vi xor PRF-k_1(vi)
            //  index of each node in Arr
            byte[]  add_vi;
            add_vi = new DataFormat().toByteArr(i * (numberOfnodes ));
//            if (i==0){
//                //  except 0
//                add_vi = new DataFormat().toByteArr(i+1);
//            }else {
//                add_vi = new DataFormat().toByteArr(i * (numberOfnodes - 1));
//            }
            //  change type of k_vi
            byte[] k_vi = K_vi[i].getBytes(StandardCharsets.UTF_8);
            //  conbine add_vi||k_vi
            byte[] left_i = new DataFormat().addBytes(add_vi,k_vi);
            //  generate PRF-k_1(vi)
            byte[] right = new DataFormat().toByteArr(pseudo.PRF(K[0],i));
            //  compute add_vi||k_vi xor PRF-k_1(vi) to DX[PRP-k_2(v_i)]
            DX[pseudo.PRP_to(K[1],i)] = new DataFormat().xor(left_i,right);

            for (int j = 0; j < shortestDistanceMatrix[i].length ; j++) {
//                if (i==j){
//                    continue;
//                }
                //  step 2. h(v_j)||sd_vivj||mid||add_v
                //  compute h(v_j)    length = 32
                byte[] h_vj = HashFounction.H_256.digest(String.valueOf(j).getBytes(StandardCharsets.UTF_8));
                //  encrypt shortest distance from v_i to v_j in shortestDistanceMatrix    length = 128
                BigInteger sd_ij = dgk.Encryption(new BigInteger(String.valueOf((int) shortestDistanceMatrix[i][j])),pk);
                byte[] sd_vivj = sd_ij.toByteArray();
                //  PRP mid node in shortest path    length = midNode * 4
                int[] midPath = getMidPath(shortestPathMatrix.get(i).get(j));
                //  change type of mid node in shortest path
                byte[] mid = new byte[midPath.length * 4];
                for (int k = 0; k < midPath.length; k++) {
                    byte[] temp = new DataFormat().toByteArr(pseudo.PRP_to(K_vi_star[i],midPath[k]));
                    for (int l = 0; l < temp.length; l++) {
                        mid[l + k*4] = temp[l];
                    }
                }
                //  compute index of next node    length = 4
                byte[] add_v = new byte[4];
                if (j == shortestDistanceMatrix[i].length-1 ){
                    //  last node in the neighbour node of v_i, set index -1
                    add_v = new DataFormat().toByteArr(-1);
                }else {
                    int add;
                    //  index to next neighbour node
                    add = num + 1;
//                    if (j==(i-1)){
//                        //  jump the node of itself
//                        add = num + 1;
//                    }else {
//                        //  index to next neighbour node
//                        add = num + 1;
//                    }
                    add_v = new DataFormat().toByteArr(add);
//                    add_v = new DataFormat().toByteArr(pi.Permutation_to(add));
                }
                //  total left
                byte[] left = new DataFormat().combineByteArr(h_vj,sd_vivj,mid,add_v);
                //  step 3. H(k_vi||random_vj)
                //  length 64
                byte[] H_kvi_rj = HashFounction.H_512.digest(new DataFormat().addBytes(K_vi[i].getBytes(),randomNumber_vi[j].getBytes()));
                //  step 4. left xor right
                Arr[num] = new DataFormat().xor(left,H_kvi_rj);
                Arr_r[num] = randomNumber_vi[j];
                num++;
            }
        }
    }

    /**
     * from shortest path get mid node
     * @param path  shortest path
     * @return
     */
    public  int[] getMidPath(String path){
        String[] str = path.split("--");
        int[] result = new int[str.length];
        for (int i = 0; i < str.length; i++) {
            if ("".equals(str[i])){
                continue;
            }
            result[i] = Integer.parseInt(str[i]);
        }
        return result;
    }
}
