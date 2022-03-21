package Setup;

import CryptoSystem.DGK;
import myUtil.HashFounction;

import java.math.BigInteger;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2022/2/28 8:56
 * @Version 1.0
 */
public class setup_genSecretKey {


    //  secret key
    //  K1,K2
    public String[] K;

    //  sk,pk
    public BigInteger[] pk;
    public BigInteger[] sk;
    public DGK dgk;

    public setup_genSecretKey(){
        //  generate two secret keys K_1,K_2
        K = HashFounction.CreateSecretKey(2);
        //  generate a key pair (sk,pk) use DGK
        dgk = new DGK();
        pk = dgk.getPublicKey();
        sk = dgk.getSecretKey();
    }

}
