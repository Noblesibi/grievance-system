package com.saintgits.sgs.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utility for password hashing and verification using BCrypt.
 */
public class PasswordUtil {

    private static final int WORK_FACTOR = 12;

    /** Returns the BCrypt hash of the given plain-text password. */
    public static String hash(String plainText) {
        return BCrypt.hashpw(plainText, BCrypt.gensalt(WORK_FACTOR));
    }

    /**
     * Returns true if the plain-text password matches the stored hash.
     * Handles null/empty values safely.
     */
    public static boolean verify(String plainText, String storedHash) {
        if (plainText == null || storedHash == null || storedHash.isEmpty()) {
            return false;
        }
        try {
            return BCrypt.checkpw(plainText, storedHash);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
