package mate.academy.util;

import java.security.MessageDigest;
import java.security.SecureRandom;

public class HashUtil {
    private static final String CRYPTO_ALGORITHM = "SHA-512";

    private HashUtil() {
    }

    public static String hashPassword(String password, byte[] salt) {
        StringBuilder hashedPassword = new StringBuilder();

        try {
            MessageDigest md = MessageDigest.getInstance(CRYPTO_ALGORITHM);
            md.update(salt);
            byte[] bytes = md.digest(password.getBytes());
            for (byte b : bytes) {
                hashedPassword.append(String.format("%02x", b));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return hashedPassword.toString();
    }

    public static byte[] getSalt() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[16];
        secureRandom.nextBytes(salt);

        return salt;
    }
}
