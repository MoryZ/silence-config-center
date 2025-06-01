package com.old.silence.config.center.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author moryzang
 */
public final class Md5Utils {
    private Md5Utils() {
        throw new AssertionError();
    }
    /**
     * 计算字符串的MD5值
     * @param input 原始字符串
     * @return 32位小写MD5值
     */
    public static String md5(String input) {
        return hash(input, null);
    }

    /**
     * 计算带盐值的MD5
     * @param input 原始字符串
     * @param salt 盐值（可空）
     * @return 32位小写MD5值
     */
    public static String md5WithSalt(String input, String salt) {
        return hash(input, salt);
    }

    /**
     * 计算文件的MD5校验和
     * @param file 目标文件
     * @return 文件MD5值（全小写）
     * @throws IOException 文件读取异常
     */
    public static String md5File(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[8192];
            int read;
            while ((read = fis.read(buffer)) > 0) {
                digest.update(buffer, 0, read);
            }
            return bytesToHex(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not found", e);
        }
    }

    /**
     * 通用哈希计算方法
     */
    private static String hash(String input, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            String combined = (salt != null) ? salt + input : input;
            byte[] hashBytes = md.digest(combined.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not found", e);
        }
    }

    /**
     * 字节数组转十六进制字符串
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder(32);
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

}
