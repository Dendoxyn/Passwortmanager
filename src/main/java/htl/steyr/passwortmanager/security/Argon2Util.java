package htl.steyr.passwortmanager.security;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class Argon2Util {

    // Tuned for desktop apps (secure but not painfully slow)
    private static final int ITERATIONS = 3;
    private static final int MEMORY = 65536; // 64 MB
    private static final int PARALLELISM = 1;

    private static final Argon2 argon2 =
            Argon2Factory.create(
                    Argon2Factory.Argon2Types.ARGON2id
            );

    // HASH
    public static String hash(char[] password) {
        try {
            return argon2.hash(
                    ITERATIONS,
                    MEMORY,
                    PARALLELISM,
                    password
            );
        } finally {

            argon2.wipeArray(password);
        }
    }


    public static boolean verify(String hash, char[] password) {
        try {
            return argon2.verify(hash, password);
        } finally {
            argon2.wipeArray(password);
        }
    }
}
