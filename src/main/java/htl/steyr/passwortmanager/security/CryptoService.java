package htl.steyr.passwortmanager.security;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.security.SecureRandom;
import java.util.Arrays;

public class CryptoService {

    private static final int IV_LENGTH = 12;   // recommended for GCM
    private static final int TAG_LENGTH = 128; // authentication tag length

    private static CryptoService instance;

    private SecretKey aesKey;

    private CryptoService() {}

    public static CryptoService getInstance() {
        if (instance == null) instance = new CryptoService();
        return instance;
    }

    // called once after login
    public void init(SecretKey key) {
        this.aesKey = key;
    }

    private void checkInit() {
        if (aesKey == null)
            throw new IllegalStateException("CryptoService not initialized");
    }

    // ================= ENCRYPT =================

    public byte[] encrypt(byte[] plain) throws Exception {
        checkInit();

        byte[] iv = new byte[IV_LENGTH];
        SecureRandom.getInstanceStrong().nextBytes(iv);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH, iv);
        cipher.init(Cipher.ENCRYPT_MODE, aesKey, spec);

        byte[] cipherText = cipher.doFinal(plain);

        // store as: [IV | CIPHERTEXT]
        byte[] out = new byte[iv.length + cipherText.length];
        System.arraycopy(iv, 0, out, 0, iv.length);
        System.arraycopy(cipherText, 0, out, iv.length, cipherText.length);

        return out;
    }

    // ================= DECRYPT =================

    public byte[] decrypt(byte[] encrypted) throws Exception {
        checkInit();

        byte[] iv = Arrays.copyOfRange(encrypted, 0, IV_LENGTH);
        byte[] cipherText = Arrays.copyOfRange(encrypted, IV_LENGTH, encrypted.length);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH, iv);
        cipher.init(Cipher.DECRYPT_MODE, aesKey, spec);

        return cipher.doFinal(cipherText);
    }
}
