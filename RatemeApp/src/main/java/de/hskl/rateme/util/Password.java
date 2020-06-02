package de.hskl.rateme.util;

import de.hskl.rateme.model.RatemeDbException;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Objects;

public class Password {
    private static final int iterations = 4096;
    private static final int saltLength = 32;

    public static byte[] genSalt() {
        return new SecureRandom().generateSeed(saltLength);
    }

    public static String hashPassword(String password, byte[] salt) {
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            SecretKey key = skf.generateSecret(new PBEKeySpec(password.toCharArray(), salt, iterations, 128));
            String saltAsString = Base64.getEncoder().encodeToString(salt);
            String saltedPassword = Base64.getEncoder().encodeToString(key.getEncoded());
            return saltAsString + '$' + saltedPassword;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RatemeDbException("Error hashing Password!", e);
        }
    }

    public static boolean checkPassword(String password, String storedPassword) {
        String[] saltAndPassword = storedPassword.split("\\$");
        byte[] salt = Base64.getDecoder().decode(saltAndPassword[0]);
        String hashedPassword = hashPassword(password, salt);
        return Objects.equals(hashedPassword, storedPassword);
    }

}
