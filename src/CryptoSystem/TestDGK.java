package CryptoSystem;

import java.math.BigInteger;
import java.util.Random;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2022/1/17 15:06
 * @Version 1.0
 */
public class TestDGK {
    public static void main(String[] args) {
        DGK dgk = new DGK();
        BigInteger m = new BigInteger("1");
//		dgk.PrecomDecryptPara();
        /* test1 for encryption, decryption and isZero */
        BigInteger c1 = dgk.Encryption(m);
        BigInteger c11 = dgk.EncryptionCRT(m);
        BigInteger de1 = dgk.Decryption(c1);
        BigInteger de11 = dgk.Decryption(c11);
        Boolean isZero1 = dgk.IsZeroEncryption(c1);
        Boolean isZero11 = dgk.IsZeroEncryption(c11);
        System.out.println("test1: the encrypted data:" + c1 + " " + c11);
        System.out.println("test1: the decrypted data:" + de1 + " " + de11);
        System.out.println("test1: is zero:" + isZero1 + " " + isZero11 + "\n");
        /* test2 for static encryption, decryption and isZero without pre-computtation*/
        BigInteger[] pk = dgk.getPublicKey();
        BigInteger[] sk = dgk.getSecretKey();
        BigInteger c2 = DGK.Encryption(m, pk);
        BigInteger c22 = DGK.EncryptionCRT(m, pk, sk);
        BigInteger de2 = DGK.Decryption(c2, pk, sk);
        BigInteger de22 = DGK.Decryption(c22, pk, sk);
        Boolean isZero2 = DGK.IsZeroEncryption(c2, sk);
        Boolean isZero22 = DGK.IsZeroEncryption(c22, sk);
        System.out.println("test2: the encrypted data:" + c2 + " " + c22);
        System.out.println("test2: the decrypted data:" + de2 + " " + de22);
        System.out.println("test2: is zero:" + isZero2 + " " + isZero22 + "\n");
        /* test3 for static encryption, decryption and isZero without pre-computtation*/
        BigInteger[] prePara = DGK.PrePara(sk);
        BigInteger[] decrypttable = DGK.PreDecryptTable(pk, sk);
        BigInteger c3 = DGK.EncryptionCRT(m, pk, sk, prePara);
        BigInteger de3 = DGK.Decryption(c3, pk, sk, decrypttable);
        System.out.println("test3: the encrypted data:" + c3);
        System.out.println("test3: the decrypted data:" + de3);
        /* test4 for the different encryption time */
        // Conclusion: it's more effective to use CRT to encrypt while owning sk.
        long start1 = System.currentTimeMillis();
        for(int i = 0; i < 20; i++){
            BigInteger c41 = DGK.EncryptionCRT(m, pk, sk);}
        long end1 = System.currentTimeMillis();
        System.out.println("the encryption time with sk:" + (end1-start1));
        long start2 = System.currentTimeMillis();
        for(int i = 0; i < 20; i++){
            BigInteger c42 = DGK.Encryption(m, pk);}
        long end2 = System.currentTimeMillis();
        System.out.println("the encryption time with pk:" + (end2-start2));
        long start3 = System.currentTimeMillis();
        for(int i = 0; i < 20; i++){
            BigInteger c43 = DGK.EncryptionCRT(m, pk, sk, prePara);}
        long end3 = System.currentTimeMillis();
        System.out.println("the encryption time with sk and pre-computed parameters:" + (end3-start3));
        /* test5 for the different decryption time */
        // Conclusion: it's more effective to decrypt with decrypt table, which is pre-computed.
        long start4 = System.currentTimeMillis();
        for(int i = 0; i < 100; i++){
            BigInteger d51 = DGK.Decryption(c1, pk, sk);}
        long end4 = System.currentTimeMillis();
        System.out.println("the decryption time without decrypted table:" + (end4-start4));
        long start5 = System.currentTimeMillis();
        for(int i = 0; i < 100; i++){
            BigInteger d52 = DGK.Decryption(c1, pk, sk, decrypttable);}
        long end5 = System.currentTimeMillis();
        System.out.println("the decryption time with decrypttable:" + (end5-start5));

        /* test for additive homomorphism, E(a+b) is same as E(a)*E(b) mod n .*/
        BigInteger a = DGK.Encryption(new BigInteger("2"), pk);
        BigInteger b = DGK.Encryption(new BigInteger("13"), pk);
        BigInteger sum = a.multiply(b).mod(pk[0]);
        System.out.println(DGK.Decryption(sum, pk, sk, decrypttable));
        /* test for mutiplicative homorphism, E(a*b) is same as E(a)^b mod n. */
        BigInteger c = new BigInteger("5");
        BigInteger mul = a.modPow(c, pk[0]);
        System.out.println(DGK.Decryption(mul, pk, sk, decrypttable));
        /* test for re-encryption, i.e. E(a)*h^r mod n.*/
        BigInteger r = new BigInteger(pk[0].bitLength(), new Random());
        BigInteger aa = a.multiply(pk[3].modPow(r, pk[0])).mod(pk[0]);
        System.out.println(DGK.Decryption(aa, pk, sk, decrypttable));
        /* test for negative of a, i.e. E(a)*E(-a) =  E(a)*E(a)^(N-1) . */
        BigInteger z = a.multiply(a.modPow(pk[1].subtract(BigInteger.ONE), pk[0])).mod(pk[0]);
        System.out.println(DGK.Decryption(z, pk, sk, decrypttable));
    }
}
