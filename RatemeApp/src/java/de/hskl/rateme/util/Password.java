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
    private static final int iterations = 2048;
    private static final int saltLength = 32;


    public static String getSaltedHash(String password) {
        try {
            byte[] salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(saltLength);
            return Base64.getEncoder().encodeToString(salt) + "$" + hashPassword(password, salt);
        } catch (NoSuchAlgorithmException e) {
            throw new RatemeDbException("Error generating Salt!", e);
        }
    }

    private static String hashPassword(String password, byte[] salt) {
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            SecretKey key = skf.generateSecret(new PBEKeySpec(password.toCharArray(), salt, iterations, 128));
            return Base64.getEncoder().encodeToString(key.getEncoded());
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RatemeDbException("Error hashing Password!", e);
        }
    }

    public static boolean checkPassword(String password, String storedPassword) {
        String[] pwParts = storedPassword.split("\\$");
        String hashedPW = hashPassword(password, Base64.getDecoder().decode(pwParts[0]));
        return Objects.equals(hashedPW, pwParts[1]);
    }

}
