package Query;

import CryptoSystem.DGK;
import myUtil.DataFormat;
import myUtil.HashFounction;
import myUtil.Pseudo;
import myUtil.pi;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.List;




/**
 * @Author UbiP Lab Laptop 02
 * @Date 2022/3/8 10:54
 * @Version 1.0
 */
public class Query_noUnorderedNode {

    public boolean normal_query(int q_1, int q_2, byte[] q_3,
                                byte[][] DX, byte[][] Arr, String[] Arr_r, pi pi,
                                List<BigInteger> shortestDistance, List<byte[]> shortestPath){
        //  step 1. a = DX[q_1]
        byte[] a = DX[q_1];
        //  step 2. b = a xor q_2   equal with  R_1||k_s
        byte[] b = new DataFormat().xor(a,new DataFormat().toByteArr(q_2));
        //  R_1||k_s = re_add_s || k_s
        //  step 3.1. due to length is 4, reload byte re_add
        byte[] re_add = new byte[4];
        for (int i = 0; i < re_add.length; i++) {
            re_add[i] = b[i];
        }
        //  step 3.2.others is K_s
        byte[] k_s = new byte[b.length - 4];
        for (int i = 0; i < k_s.length; i++) {
            k_s[i] = b[i+4];
        }
        //  step 3.3. change type to int, get the index of Arr, R_1
        int re_add_start = new DataFormat().toInt(re_add) ;
//        int re_add_start = pi.Permutation_from(new DataFormat().toInt(re_add)) ;
        //  judge index is valid or not

        boolean isNUll = false;
        if (re_add_start == -1) {
            isNUll = true;
        }
        //  judge search end_point or not
        boolean Tag_same = false;
        if (!isNUll) {
            //  step 4. H(K_s||r_s)
            byte[] H_ks_rs = HashFounction.H_512.digest(new DataFormat().addBytes(k_s, Arr_r[re_add_start].getBytes()));
            //  step 5. x_s = Arr[index] xor H_ks_rs
            byte[] x_s = new DataFormat().xor(Arr[re_add_start], H_ks_rs);
            //  step 6.1.   due to length is 32, reload h_i
            byte[] h_i = new byte[32];
            for (int i = 0; i < h_i.length; i++) {
                h_i[i] = x_s[i];
            }
            //  step 6.2.   judge search end_point or not
            Tag_same = new DataFormat().compareTwoByteArr(h_i, q_3);
            //  if not, continue to search
            while (!Tag_same) {
                //  step 7. due to length is 4, reload byte re_add_i
                byte[] re_add_i = new byte[4];
                for (int i = 0; i < re_add_i.length; i++) {
                    re_add_i[i] = x_s[x_s.length - 4 + i];
                }
                re_add_start = new DataFormat().toInt(re_add_i);
//                re_add_start = pi.Permutation_from(new DataFormat().toInt(re_add_i));


                //  if index out of bound
                if (re_add_start < 0 || re_add_start > Arr.length) {
                    isNUll = true;
                    break;
                }
                //  step 8. H(K_s||r_s)
                H_ks_rs = HashFounction.H_512.digest(new DataFormat().addBytes(k_s, Arr_r[re_add_start].getBytes()));
                //  step 9. x_s = Arr[index] xor H_ks_rs
                x_s = new DataFormat().xor(Arr[re_add_start], H_ks_rs);
                //  step 10. due to length is 32, reload h_i
                h_i = new byte[32];
                for (int i = 0; i < h_i.length; i++) {
                    h_i[i] = x_s[i];
                }
                //  step 11. judge search end_point or not
                Tag_same = new DataFormat().compareTwoByteArr(h_i, q_3);
            }

            //  get true end_point
            if (Tag_same){

                //  Enc(sd_start_end)
                byte[] enc = new byte[128];
                for (int i = 0; i < enc.length; i++) {
                    enc[i] = x_s[32 + i];
                }
                BigInteger enc_sd = new BigInteger(enc);
                shortestDistance.add(enc_sd);
                //  midPath
                byte[] sp = new byte[x_s.length - 32 - 128 -4];
                for (int i = 0; i < sp.length - 4; i++) {
                    sp[i] = x_s[i + 32 + 128];
                }
                shortestPath.add(sp);

                return true;
            }else {
                return false;
            }
        }else {
            return false;
        }
    }

}
