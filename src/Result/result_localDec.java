package Result;

import CryptoSystem.DGK;
import myUtil.DataFormat;
import myUtil.Permut;
import myUtil.Pseudo;
import myUtil.show;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;



/**
 * @Author DELL
 * @Date 2022/3/20 23:41
 * @Version 1.0
 */
public class result_localDec {
    public void getResult_un(int start_point, int end_point,DGK dgk,BigInteger[] pk,BigInteger[] sk,Pseudo pseudo, String[] k_vi_star,List<BigInteger> shortestDistance, List<byte[]> shortestPath){
        for (BigInteger ciper:shortestDistance) {
            int shortest = dgk.Decryption(ciper, pk, sk).intValue();
        }

        for (byte[] path:shortestPath) {
            int[] sp_sd = new int[path.length / 4];
            for (int i = 0; i < sp_sd.length; i++) {
                sp_sd[i] = pseudo.PRP_from(k_vi_star[start_point],reloadInt(path,i));
            }
        }
    }

    public void getResult_k_ordered_nodes(int start_point, int end_point,int[] k_midNodes,DGK dgk,BigInteger[] pk,BigInteger[] sk, Pseudo pseudo,String[] k_vi_star, BigInteger[] shortestDistance, List<List<byte[]>> shortestPath){
        int[] shortest = new int[shortestDistance.length];
        int index = 0;
        for (BigInteger ciper:shortestDistance) {
            shortest[index++] = dgk.Decryption(ciper, pk, sk).intValue();
        }
        int MinIndex = Integer.MAX_VALUE;
        int MinDistance = Integer.MAX_VALUE;
        for (int i = 0; i < shortest.length; i++) {
            if (shortest[i] != 1 && MinDistance > shortest[i]){
                MinDistance = shortest[i];
                MinIndex = i;
            }
        }

        if (MinIndex != Integer.MAX_VALUE && MinIndex != 1) {
            int[] nodes_index = new int[k_midNodes.length];
            for (int i = 0; i < nodes_index.length; i++) {
                nodes_index[i] = i;
            }
            int[][] full_permut = new Permut().permute(nodes_index, 0);
            int[] shortPath = new int[k_midNodes.length];
            for (int i = 0; i < shortPath.length; i++) {
                shortPath[i] = k_midNodes[nodes_index[full_permut[MinIndex][i]]];
            }

            int start = start_point;
            List<Integer> res = new ArrayList<>();
            for (int i = 0; i < shortestPath.get(MinIndex).size(); i++) {
                int[] sp_sd = new int[shortestPath.get(MinIndex).get(i).length / 4];
                for (int j = 0; j < sp_sd.length; j++) {
                    sp_sd[j] = pseudo.PRP_from(k_vi_star[start],reloadInt(shortestPath.get(MinIndex).get(i), j));
                    res.add(sp_sd[j]);
                }
                if (i > 0){
                    start = shortPath[i-1];
                    res.add(start);
                }
            }
        }
    }



    public static int reloadInt(byte[] sp, int i) {
        byte[] res = new byte[4];
        for (int j = 0; j < res.length; j++) {
            res[j] = sp[i * 4 + j];
        }
        return new DataFormat().toInt(res);
    }
}
