package ling.yuze.mymoviememoir.formatting;

import java.math.BigInteger;
import java.security.MessageDigest;

public class Encryption {
    public static String md5_encryption(String password) {
        String passwordHash = "";
        try {
            MessageDigest md5 = MessageDigest.getInstance("md5");
            byte[] bytes = password.getBytes("UTF-8");
            md5.update(bytes);
            byte[] md5_bytes = md5.digest();
            passwordHash = new BigInteger(1, md5_bytes).toString(16);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return passwordHash;
    }
}
