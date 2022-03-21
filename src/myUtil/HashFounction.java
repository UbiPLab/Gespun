package myUtil;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class HashFounction {
    public static MessageDigest H_256;

    static {
        try {
            H_256 = MessageDigest.getInstance("sha-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static MessageDigest H_384;

    static {
        try {
            H_384 = MessageDigest.getInstance("sha-384");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static MessageDigest H_512;

    static {
        try {
            H_512 = MessageDigest.getInstance("sha-512");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static byte[] HmacSHA256Encrypt(String encryptText, String encryptKey) throws Exception {
        byte[] data=encryptKey.getBytes("utf-8");
        SecretKey secretKey = new SecretKeySpec(data, "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(secretKey);
        byte[] text = encryptText.getBytes("utf-8");
        return mac.doFinal(text);
    }

    public static String[] CreateSecretKey(int keylength) {
        String[] keylist = new String[keylength];

        Random random = new Random();
        int length = 1024;//key length
        for (int j = 0; j < keylist.length; j++) {
            StringBuffer bigstring = new StringBuffer();
            for (int i = 0; i < length; i++) {
                bigstring.append(random.nextInt(10));
            }
            keylist[j] = bigstring.toString();
        }
        return keylist;
    }

    public static String byteToHexString(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String strHex = Integer.toHexString(bytes[i]);
            if (strHex.length() > 3) {
                sb.append(strHex.substring(6));
            } else {
                if (strHex.length() < 2) {
                    sb.append("0" + strHex);
                } else {
                    sb.append(strHex);
                }
            }
        }
        return sb.toString();
    }
    public static String toHexString(byte[] byteArray) {
        final StringBuilder hexString = new StringBuilder("");
        if (byteArray == null || byteArray.length <= 0)
            return null;
        for (int i = 0; i < byteArray.length; i++) {
            int v = byteArray[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                hexString.append(0);
            }
            hexString.append(hv);
        }
        return hexString.toString().toLowerCase();
    }
}
