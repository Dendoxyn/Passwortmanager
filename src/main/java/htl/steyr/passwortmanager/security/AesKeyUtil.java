package htl.steyr.passwortmanager.security;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.spec.KeySpec;

public class AesKeyUtil {

    private static final int ITERATIONS = 150_000;
    private static final int KEY_LENGTH = 256;

    public static SecretKey deriveKey(char[] password, byte[] salt) throws Exception {

        SecretKeyFactory factory =
                SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

        KeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);

        byte[] keyBytes = factory.generateSecret(spec).getEncoded();

        return new SecretKeySpec(keyBytes, "AES");
    }
}
