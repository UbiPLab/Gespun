package myUtil;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2022/3/7 15:14
 * @Version 1.0
 */
public class DataFormat {


    public byte[] toByteArr(int i) {
        byte[] b = new byte[4];
        b[0] = (byte) ((i >>> 24) & 0xFF);
        b[1] = (byte) ((i >>> 16) & 0xFF);
        b[2] = (byte) ((i >>> 8) & 0xFF);
        b[3] = (byte) (i & 0xFF);

        return b;
    }


    public   int toInt(byte[] b) {
        return (b[0] & 0xFF) << 24 | (b[1] & 0xFF) << 16 | (b[2] & 0xFF) << 8 | (b[3] & 0xFF);
    }

    public byte[] combineByteArr(byte[] h_v,byte[] enc,byte[] P_k,byte[] add){
        byte[] res = new byte[h_v.length+enc.length+P_k.length+add.length];
        for (int i = 0; i < h_v.length; i++) {
            res[i] = h_v[i];
        }
        for (int i = 0; i < enc.length; i++) {
            res[i + h_v.length] = enc[i];
        }
        for (int i = 0; i < P_k.length; i++) {
            res[i + h_v.length + enc.length] = P_k[i];
        }
        for (int i = 0; i < add.length; i++) {
            res[i + res.length - add.length] = add[i];
        }
        return res;
    }

    public byte[] addBytes(byte[] bytes, byte[] bytes1) {
        byte[] res = new byte[bytes.length + bytes1.length];
        for (int i = 0; i < bytes.length; i++) {
            res[i] = bytes[i];
        }
        for (int i = 0; i < bytes1.length; i++) {
            res[i+bytes.length]= bytes1[i];
        }
        return res;
    }

    public byte[] xor(byte[] left, byte[] h_kvi_rj) {
        byte[] res = new byte[left.length];
        //  right.length shorter, double
        int time = left.length / h_kvi_rj.length;
        for (int i = 0; i < time; i++) {
            for (int j = 0; j < h_kvi_rj.length; j++) {
                res[i * h_kvi_rj.length + j] = (byte) (left[i * h_kvi_rj.length + j] ^ h_kvi_rj[j]);
            }
        }
        for (int i = time * h_kvi_rj.length; i < left.length; i++) {
            res[i] = (byte) (left[i] ^ h_kvi_rj[i %  h_kvi_rj.length]);
        }
        return res;
    }


    public  long factorial(long number) {
        if (number <= 1)
            return 1;
        else
            return number * factorial(number - 1);
    }

    public boolean compareTwoByteArr(byte[] a, byte[] b){
        if (a.length != b.length){
            return false;
        }else {
            for (int i = 0; i < a.length; i++) {
                if (a[i] != b[i]){
                    return false;
                }
            }
        }
        return true;
    }
}
