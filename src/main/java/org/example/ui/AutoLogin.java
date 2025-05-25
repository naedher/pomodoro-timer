package org.example.ui;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;
import java.io.*;
import java.nio.file.*;

public class AutoLogin {

    private String secretKey;
    private String filePath;
    private static final String CHARSET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public void autoLogin(String email, String pass) {
        String home = System.getProperty("user.home");
        this.filePath = Paths.get(home, ".myapp", "login.txt").toString(); // T.ex. /Users/dittnamn/.myapp/login.txt
        createDirectory();
        saveLogin(email,pass);
    }

    private void createDirectory() {
        try {
            Path parentDir = Paths.get(filePath).getParent();
            if (!Files.exists(parentDir)) {
                Files.createDirectories(parentDir);
            }
        } catch (IOException e) {
            System.err.println("Failed to create directory: " + e.getMessage());
        }
    }

    public void saveLogin(String email, String pass) {
        generateKey();
        pass = encrypt(pass);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(email);
            writer.newLine();
            writer.write(pass);
            writer.newLine();
            writer.write(secretKey);
            System.out.println("Login info saved.");
        } catch (IOException e) {
            System.err.println("Failed to save login info: " + e.getMessage());
        }
    }

    public String[] loadLogin() {
        try {
            Path path = Paths.get(filePath);
            if (!Files.exists(path)) return null;

            String[] lines = Files.readAllLines(path).toArray(new String[0]);
            if (lines.length >= 3) {
                secretKey = lines[2];
                String decrypted = decrypt(lines[1]);

                return new String[]{lines[0], decrypted};
            }
        } catch (IOException e) {
            System.err.println("Failed to load login info: " + e.getMessage());
        }

        return null;
    }

    public void logOut() {
        try {
            Files.deleteIfExists(Paths.get(filePath));
            System.out.println("Login info cleared.");
        } catch (IOException e) {
            System.err.println("Failed to clear login info: " + e.getMessage());
        }
    }

    public String encrypt(String strToEncrypt) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] encryptedBytes = cipher.doFinal(strToEncrypt.getBytes());

            return Base64.getEncoder().encodeToString(encryptedBytes);

        } catch (Exception e) {
            throw new RuntimeException("Encryption error: " + e.getMessage(), e);
        }
    }

    public String decrypt(String encryptedStr) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] decodedBytes = Base64.getDecoder().decode(encryptedStr);
            byte[] originalBytes = cipher.doFinal(decodedBytes);

            return new String(originalBytes);

        } catch (Exception e) {
            throw new RuntimeException("Decryption error: " + e.getMessage(), e);
        }
    }

    // 16 bokstävers generator
    public void generateKey() {
        SecureRandom random = new SecureRandom();
        StringBuilder key = new StringBuilder(16);

        for (int i = 0; i < 16; i++) {
            int index = random.nextInt(CHARSET.length());
            key.append(CHARSET.charAt(index));
        }
        secretKey = key.toString();
    }

}
