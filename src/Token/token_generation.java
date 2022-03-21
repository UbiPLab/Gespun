package Token;

import myUtil.HashFounction;
import myUtil.Pseudo;

import java.nio.charset.StandardCharsets;

/**
 * @Author DELL
 * @Date 2022/3/20 23:02
 * @Version 1.0
 */
public class token_generation {

    public int[] q_1;
    public int[] q_2;
    public byte[][] q_3;

    public void token_generation_k_midnode(int start_point, int end_point, int[] k_midNodes, Pseudo pseudo,String[] K) {
        //  q_1 = PRP(k_2,start_point),PRP(k_2,k_midNods)
        q_1 = new int[k_midNodes.length + 1];
        q_1[0] = pseudo.PRP_to(K[1], start_point);
        for (int i = 0; i < k_midNodes.length; i++) {
            q_1[i + 1] = pseudo.PRP_to(K[1], k_midNodes[i]);
        }
        //  q_2 = PRF(k_1,start_point),PRF(k_1,k_midNodes)
        q_2 = new int[k_midNodes.length + 1];
        q_2[0] = pseudo.PRF(K[0], start_point);
        for (int i = 0; i < k_midNodes.length; i++) {
            q_2[i + 1] = pseudo.PRF(K[0], k_midNodes[i]);
        }
        //  q_3 = h(k_midNodes),h(end_point)
        q_3 = new byte[k_midNodes.length + 1][];
        for (int i = 0; i < k_midNodes.length; i++) {
            q_3[i] = HashFounction.H_256.digest(String.valueOf(k_midNodes[i]).getBytes(StandardCharsets.UTF_8));
        }
        q_3[k_midNodes.length] = HashFounction.H_256.digest(String.valueOf(end_point).getBytes(StandardCharsets.UTF_8));

    }

    public void token_generation_un_midnode(int start_point, int end_point, Pseudo pseudo,String[] K) {
        //  q_1 = PRP(k_2,start_point)
        q_1 = new int[1];
        q_1[0] = pseudo.PRP_to(K[1], start_point);
        //  q_2 = PRF(k_1,start_point)
        q_2 = new int[1];
        q_2[0] = pseudo.PRF(K[0], start_point);
        //  q_3 = h(end_point)
        q_3 = new byte[1][];
        q_3[0] = HashFounction.H_256.digest(String.valueOf(end_point).getBytes(StandardCharsets.UTF_8));
    }

    public  int[] createInt_k(int start,int end,int temp_k) {
        int[] temp = Pseudo.randomArray(start + 1, end - 1, temp_k);

        int[] result = new int[temp_k];
        for (int i = 0; i < temp_k; i++) {
            result[i] = temp[i];
        }
        return result;
    }
}
