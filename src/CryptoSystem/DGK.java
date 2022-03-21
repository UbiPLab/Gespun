package CryptoSystem;

import java.math.BigInteger;
import java.util.Random;

/**
 * @Author UbiP Lab Laptop 02
 * @Date 2022/1/17 15:05
 * @Version 1.0
 */
public class DGK {
    /**
     * The RSA modulus N, the public key
     */
    public BigInteger n;
    /**
     * the public key g
     */
    public BigInteger g;
    /**
     * the public key h
     */
    public BigInteger h;
    /**
     * the public key u
     */
    public BigInteger u;

    /**
     * the secret key vp
     */
    private BigInteger vp;
    /**
     * the secret key vq
     */
    private BigInteger vq;
    /**
     * the secret key p
     */
    private BigInteger p;
    /**
     * the secret key q
     */
    private BigInteger q;
    /*-------------------- all following parameters is used to faster the de-/encryption -----------*/
    /**
     * true: the following parameters have been pre-computed.
     * false: the following parameters haven't been pre-computed.
     */
    private Boolean PreSwitch;
    /**
     * the inverse of p * p
     */
    private BigInteger pp_inv;
    /**
     * the inverser of q * q
     */
    private BigInteger qq_inv;
    /**
     * the multiplication of vp and vq
     */
    private BigInteger vpvq;
    /*--------------------- the table is pre-computed only for faster the decryption ---------*/
    /**
     * the table is used to decrypt cipher, which contains values of (g^v)^m mod n and corresponding values of m.
     */
    private BigInteger[] decrypttable;

    /**
     * Construct an instance of DGK. And we take as input parameters k, t and l, where k>t>l.
     * @param k
     * @param t
     * @param l
     */
    public DGK(int k, int t, int l, int certainty) {
        if(l<8 || l>16)
            throw new IllegalArgumentException("Choose parameter l from the range 8 to 16.");
        if(t <= 1)
            throw new IllegalArgumentException("Parameter t must be greater than 1.");
        if(k <= t)
            throw new IllegalArgumentException("Parameter k must be greater than t.");
        if(k % 2 != 0)
            throw new IllegalArgumentException("Parameter k must be an even number.");
        if(k/2 < l+t+10)
            throw new IllegalArgumentException("Choose k,t,l so that k/2 >= l+t+10.");
        PreSwitch = false;
        KeyGeneration(k,t,l,certainty);
    }

    /**
     * Construct an instance of DGK.
     * @param l
     */
    public DGK(int l){
        if(l<8 || l>16)
            throw new IllegalArgumentException("Choose parameter l from the range 8 to 16.");
        PreSwitch = false;
        KeyGeneration(1024,160,l,64);
    }

    /**
     * Construct an instance of DGK.
     * @param
     */
    public DGK(){
        PreSwitch = false;
        KeyGeneration(1024,160,16,64);
    }

    public DGK(int l, int K){
        PreSwitch = false;
        KeyGeneration(K,160,l,64);
    }

    /**
     * Set up the public key and private key.
     * @param k
     * @param t
     * @param l
     */
    public void KeyGeneration(int k, int t, int l, int certainty) {
        // generate u the minimal prime number greater than l+2.
        u = BigInteger.valueOf((1 << l) + 2).nextProbablePrime();
        // generate vp,vq as a random t bit prime.
        vp = BigInteger.probablePrime(t, new Random());
        vq = BigInteger.probablePrime(t, new Random());
        //store the product of vp and vq
        BigInteger vpvq = vp.multiply(vq);
        /* DGK style to generate p and q from u and v */
        //p is chosen as rp*u*vp+1 where rp is randomly chosen such that p has roughly k/2 bits.
        BigInteger rq,rp,tmp1,tmp2;
        int needBits;
        tmp1 = u.multiply(vp);
        needBits = (k >> 1) - tmp1.bitLength();
        do {
            rp = new BigInteger(needBits-2, new Random()).or(BigInteger.ONE.shiftLeft(needBits-2)).shiftLeft(1);
            p = rp.multiply(tmp1).add(BigInteger.ONE);
        }while(!p.isProbablePrime(certainty));
        //q is chosen as rq*u*vq+1 where rq is randomly chosen such that q has roughly k/2 bits.
        tmp2 = u.multiply(vq);
        needBits = (k >> 1) - tmp2.bitLength();
        do
        {
            rq = new BigInteger(needBits-2, new Random()).or(BigInteger.ONE.shiftLeft(needBits-2)).shiftLeft(1);
            q = rq.multiply(tmp2).add(BigInteger.ONE);
        } while (q.isProbablePrime(certainty));
        //RSA modulus n
        n = p.multiply(q);
        /* h must be random in Zn* and have order vp*vq. We choose it by setting h = h' ^{rp * rq * u}.
         * Seeing h as (hp, hq) and h' as (h'p, h'q) in Zp* x Zq*, we then have (hp^vpvq, hq^vpvq)
         *  = (h'p^{rp*u*vp}^(rq*vq), h'q^{rq*u*vq}^(rp*vp)) = (1^(rq*vq), 1^(rp*vp)) = (1, 1),
         *  which means that h^(vpvq) = 1 in Zn*.
         *  So we only need to check that h is not 1 and that it really is in Zn*.*/
        BigInteger r;
        BigInteger tmp = rp.multiply(rq).multiply(u);
        while(true) {
            r = new BigInteger(k, new Random()).mod(n);
            h = r.modPow(tmp, n);
            if(h.compareTo(BigInteger.ONE) == 0) continue;
            if(h.gcd(n).compareTo(BigInteger.ONE) == 0) break;
        }
        /* g is chosen at random in Zn* such that it has order uv. This is done in much the same way as for h,
         *  but the order of power of the random number might be u, v or uv. We therefore also check that g^u
         *  and g^v are different from 1. */
        BigInteger rprq = rp.multiply(rq);
        while (true){
            r = new BigInteger(k, new Random()).mod(n);
            g = r.modPow(rprq, n);
            // test if g is "good":
            if (g.compareTo(BigInteger.ONE) == 0) continue;
            if (g.gcd(n).compareTo(BigInteger.ONE) != 0) continue;
            if (g.modPow(u, n).compareTo(BigInteger.ONE) == 0) continue;      // test if ord(g) == u
            if (g.modPow(vp, n).compareTo(BigInteger.ONE) == 0) continue;     // test if ord(g) == vp
            if (g.modPow(vq, n).compareTo(BigInteger.ONE) == 0) continue;     // test if ord(g) == vq
            if (g.modPow(tmp1, n).compareTo(BigInteger.ONE) == 0) continue; // test if ord(g) == u*vp
            if (g.modPow(tmp2, n).compareTo(BigInteger.ONE) == 0) continue; // test if ord(g) == u*vq
            if (g.modPow(vpvq, n).compareTo(BigInteger.ONE) == 0) continue;   // test if ord(g) == vp*vq
            break;  // g has passed all tests
        }
    }

    /**
     * DGK encryption using CRT from the owner of sk (p, q, vp, vq).
     * @param m
     * @return
     */
    public BigInteger EncryptionCRT(BigInteger m) {
        if(m.compareTo(u) == 1)
            throw new IllegalArgumentException("Message is bigger than u will produce a wrong result.");
        if(PreSwitch){
            // Use Zv instead of a 2t bit number:
            BigInteger r = new BigInteger(vpvq.bitLength(), new Random()).mod(vpvq);
            // Calculate in Zp and Zq instead of Zn:
            BigInteger tmp_cp = g.modPow(m, p).multiply(h.modPow(r, p));
            BigInteger tmp_cq = g.modPow(m, q).multiply(h.modPow(r, q));
            return (tmp_cp.multiply(qq_inv).add(tmp_cq.multiply(pp_inv))).mod(n);
        }
        else{
            // Use Zv instead of a 2t bit number:
            BigInteger vpvq = vp.multiply(vq);
            BigInteger r = new BigInteger(vpvq.bitLength(), new Random()).mod(vpvq);
            // Calculate in Zp and Zq instead of Zn:
            BigInteger tmp_cp = g.modPow(m, p).multiply(h.modPow(r, p));
            BigInteger tmp_cq = g.modPow(m, q).multiply(h.modPow(r, q));
            BigInteger qq_inv = q.modInverse(p).multiply(q);
            BigInteger pp_inv = p.modInverse(q).multiply(p);
            return (tmp_cp.multiply(qq_inv).add(tmp_cq.multiply(pp_inv))).mod(n);
        }
    }

    /**
     * DGK encryption using CRT from the owner of sk (p, q, vp, vq).
     * @param m
     * @param sk
     * @return
     */
    public static BigInteger EncryptionCRT(BigInteger m, BigInteger[] pk, BigInteger[] sk){
        BigInteger n = pk[0], u = pk[1], g = pk[2], h = pk[3];
        BigInteger p = sk[0], q = sk[1], vp = sk[2], vq = sk[3];
        if(m.compareTo(u) == 1)
            throw new IllegalArgumentException("Message is bigger than u will produce a wrong result.");
        // Use Zv instead of a 2t bit number:
        BigInteger vpvq = vp.multiply(vq);
        BigInteger r = new BigInteger(vpvq.bitLength(), new Random()).mod(vpvq);
        // Calculate in Zp and Zq instead of Zn:
        BigInteger tmp_cp = g.modPow(m, p).multiply(h.modPow(r, p));
        BigInteger tmp_cq = g.modPow(m, q).multiply(h.modPow(r, q));
        BigInteger qq_inv = q.modInverse(p).multiply(q);
        BigInteger pp_inv = p.modInverse(q).multiply(p);
        return (tmp_cp.multiply(qq_inv).add(tmp_cq.multiply(pp_inv))).mod(n);
    }

    /**
     * DGK encryption using CRT from the owner of sk (p, q, vp, vq).
     * Using pre-computed parameters to speed up the encryption.
     * @param m
     * @param pk
     * @param sk
     * @param PrePara
     * @return
     */
    public static BigInteger EncryptionCRT(BigInteger m, BigInteger[] pk, BigInteger[] sk, BigInteger[] PrePara){
        BigInteger n = pk[0], u = pk[1], g = pk[2], h = pk[3];
        BigInteger p = sk[0], q = sk[1], vp = sk[2], vq = sk[3];
        if(m.compareTo(u) == 1)
            throw new IllegalArgumentException("Message is bigger than u will produce a wrong result.");
        // Use Zv instead of a 2t bit number:
        BigInteger vpvq = PrePara[0];
        BigInteger r = new BigInteger(vpvq.bitLength(), new Random()).mod(vpvq);
        // Calculate in Zp and Zq instead of Zn:
        BigInteger tmp_cp = g.modPow(m, p).multiply(h.modPow(r, p));
        BigInteger tmp_cq = g.modPow(m, q).multiply(h.modPow(r, q));
        BigInteger qq_inv = PrePara[2];
        BigInteger pp_inv = PrePara[1];
        return (tmp_cp.multiply(qq_inv).add(tmp_cq.multiply(pp_inv))).mod(n);
    }

//	/**
//	 * DGK encryption using CRT from the owner of sk (p, q, vp, vq).
//	 * The partial parameters is pre-computed.
//	 * @param m
//	 * @return
//	 */
//	public BigInteger EncryptionCRT1(BigInteger m) {
//		if(m.compareTo(u) == 1)
//			throw new IllegalArgumentException("Message is bigger than u will produce a wrong result.");
//		// Use Zv instead of a 2t bit number:
////		BigInteger vpvq = vp.multiply(vq);
//		BigInteger r = new BigInteger(vpvq.bitLength(), new Random()).mod(vpvq);
//		// Calculate in Zp and Zq instead of Zn:
//		BigInteger tmp_cp = g.modPow(m, p).multiply(h.modPow(r, p));
//		BigInteger tmp_cq = g.modPow(m, q).multiply(h.modPow(r, q));
////		BigInteger qq_inv = q.modInverse(p).multiply(q);
////		BigInteger pp_inv = p.modInverse(q).multiply(p);
//		return (tmp_cp.multiply(qq_inv).add(tmp_cq.multiply(pp_inv))).mod(n);
//	}

    /**
     * Standard DGK encryption
     * @param m
     * @return the encrypted message
     */
    public BigInteger Encryption(BigInteger m) {
        if(m.compareTo(u) == 1)
            throw new IllegalArgumentException("Message is bigger than u will produce a wrong result.");
        BigInteger r = new BigInteger(n.bitLength()*2, new Random()).mod(n.multiply(n));
        return g.modPow(m, n).multiply(h.modPow(r, n)).mod(n);
    }

    /**
     * Standard DGK encryption
     * @param m
     * @param pk
     * @param //sk
     * @return
     */
    public static BigInteger Encryption(BigInteger m, BigInteger[] pk){
        BigInteger n = pk[0], u = pk[1], g = pk[2], h = pk[3];
        if(m.compareTo(u) == 1)
            throw new IllegalArgumentException("Message is bigger than u will produce a wrong result.");
        BigInteger r = new BigInteger(n.bitLength()*2, new Random()).mod(n.multiply(n));
        return g.modPow(m, n).multiply(h.modPow(r, n)).mod(n);
    }

    /**
     * DGK Decryption
     * This method can be used for decryption.
     * For better performance...
     * ... instead of running the loop each time one can use a HashTable for fast lookup
     * ... one can use CRT as in isZeroDecryption
     * @param c
     * @return
     */
    public BigInteger Decryption(BigInteger c) {
        BigInteger message = c.modPow(vp, p);
        if(decrypttable != null){
            for(int m = 0; m < u.intValue(); m++){
                if(message.compareTo(decrypttable[m]) == 0)
                    return BigInteger.valueOf(m);
            }
        }
        else{
            BigInteger gv = g.modPow(vp, p);
            for (int m = 0; m < u.intValue(); m++)
                if (message.compareTo(gv.modPow(BigInteger.valueOf(m), p)) == 0)
                    return BigInteger.valueOf(m);
        }
        return null;
    }

    /**
     * DGK Decryption
     * @param c
     * @param pk
     * @param sk
     * @return
     */
    public static BigInteger Decryption(BigInteger c, BigInteger[] pk, BigInteger[] sk){
        BigInteger n = pk[0], u = pk[1], g = pk[2], h = pk[3];
        BigInteger p = sk[0], q = sk[1], vp = sk[2], vq = sk[3];
        BigInteger message = c.modPow(vp, p);
        BigInteger gv = g.modPow(vp, p);
        for (int m = 0; m < u.intValue(); m++)
            if (message.compareTo(gv.modPow(BigInteger.valueOf(m), p)) == 0)
                return BigInteger.valueOf(m);
        return null;
    }

    /**
     * DGK Decryption
     * Using the pre-computed decrypt table to speed up the decryption.
     * @param c
     * @param pk
     * @param sk
     * @param decrypttable
     * @return
     */
    public static BigInteger Decryption(BigInteger c, BigInteger[] pk, BigInteger[] sk, BigInteger[] decrypttable){
        BigInteger n = pk[0], u = pk[1], g = pk[2], h = pk[3];
        BigInteger p = sk[0], q = sk[1], vp = sk[2], vq = sk[3];
        BigInteger message = c.modPow(vp, p);
        for(int m = 0; m < u.intValue(); m++){
            if(message.compareTo(decrypttable[m]) == 0)
                return BigInteger.valueOf(m);
        }
        return null;
    }

//	/**
//	 * The method only decides whether c encrypts 0 or not.
//	 * The partial parameters of this method is pre-computed.
//	 * @param c
//	 * @return
//	 */
//	public boolean isZeroEncryption1(BigInteger c){
//		BigInteger tmp_cp = c.modPow(vpvq, p);
//		BigInteger tmp_cq = c.modPow(vpvq, q);
//		BigInteger tmp = tmp_cp.multiply(qq_inv).add(tmp_cq.multiply(pp_inv)).mod(p);
//		return (tmp.compareTo(BigInteger.ONE) == 0);
//	}

    /**
     * The method decides whether c encrypts 0 or not, which is usually used in comparison protocol.
     * @param c
     * @return
     */
    public boolean IsZeroEncryption(BigInteger c){
        BigInteger vpvq = vp.multiply(vq);
        return c.modPow(vpvq, p).compareTo(BigInteger.ONE) == 0;
    }

    /**
     * The method decides whether c encrypts 0 or not.
     * @param c
     * @param sk
     * @return
     */
    public static boolean IsZeroEncryption(BigInteger c, BigInteger[] sk){
        BigInteger p = sk[0], q = sk[1], vp = sk[2], vq = sk[3];
        BigInteger vpvq = vp.multiply(vq);
        return c.modPow(vpvq, p).compareTo(BigInteger.ONE) == 0;
    }

    /**
     * Pre-compute some parameters for faster the de-/encryption.
     * This method can only be used by user with sk.
     * The pre-compute parameters includes decrypt table, pp_inv , pp_inv and multiplication of vp and vq.
     */
    public void PrecomDecryptPara(){
        decrypttable = new BigInteger[u.intValue()];
        BigInteger gv = g.modPow(vp, p);
        for(int i = 0; i < u.intValue(); i++){
            decrypttable[i] = gv.modPow(BigInteger.valueOf(i), p);
        }
        vpvq = vp.multiply(vq);
        pp_inv = p.modInverse(q).multiply(p);
        qq_inv = q.modInverse(p).multiply(q);
        PreSwitch = true;
    }

    /**
     * Pre-compute the decrypt table which is used to faster the decryption
     * @param pk
     * @param sk
     * @return
     */
    public static BigInteger[] PreDecryptTable(BigInteger[] pk, BigInteger[] sk){
        BigInteger n = pk[0], u = pk[1], g = pk[2], h = pk[3];
        BigInteger p = sk[0], q = sk[1], vp = sk[2], vq = sk[3];
        BigInteger[] decrypttable = new BigInteger[u.intValue()];
        BigInteger gv = g.modPow(vp, p);
        for(int i = 0; i < u.intValue(); i++){
            decrypttable[i] = gv.modPow(BigInteger.valueOf(i), p);
        }
        return decrypttable;
    }

    /**
     * Pre-compute the partial parameteres which are used to fater the encryption and decryption by sk.
     * @param sk
     * @return
     */
    public static BigInteger[] PrePara(BigInteger[] sk){
        BigInteger p = sk[0], q = sk[1], vp = sk[2], vq = sk[3];
        BigInteger vpvq = vp.multiply(vq);
        BigInteger pp_inv = p.modInverse(q).multiply(p);
        BigInteger qq_inv = q.modInverse(p).multiply(q);
        return new BigInteger[]{vpvq, pp_inv, qq_inv};
    }

    /**
     * The public key of DGK: pk = (n,u,g,h)
     * @return
     */
    public BigInteger[] getPublicKey(){
        return new BigInteger[]{n,u,g,h};
    }

    /**
     * The secret key of DGK: sk = (p,q,v) = (p,q,vp,vq),
     * where v = vp * vq
     * @return
     */
    public BigInteger[] getSecretKey(){
        return new BigInteger[]{p,q,vp,vq};
    }
}
