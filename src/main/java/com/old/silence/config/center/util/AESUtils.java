package com.old.silence.config.center.util;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.codec.binary.Hex;
import com.old.silence.core.context.CommonErrors;

/**
 * @author moryzang
 */
public final class AESUtils {

    private static final String AES_TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final int AES_BLOCK_SIZE = 16;
    private static final int IV_HEX_LENGTH = AES_BLOCK_SIZE * 2;
    private static final String CIPHER_VERSION_V2_PREFIX = "02";
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private AESUtils() {
        throw new AssertionError();
    }

    public static String encrypt(String plainText, String appKey) {
        if (StringUtils.isBlank(plainText)) {
            throw CommonErrors.INVALID_PARAMETER.createException("加密内容为空");
        }
        validateAesKeyLength(appKey);
        try {
            Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
            SecretKeySpec secretKeySpec = new SecretKeySpec(appKey.getBytes(StandardCharsets.UTF_8), "AES");
            byte[] iv = generateRandomIv();
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, new IvParameterSpec(iv));
            byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            // Output format: versionPrefix + ivHex + cipherHex, still a pure hex string for transport compatibility.
            return CIPHER_VERSION_V2_PREFIX + Hex.encodeHexString(iv) + Hex.encodeHexString(encrypted);
        } catch (GeneralSecurityException e) {
            throw CommonErrors.INVALID_PARAMETER.createException("加密失败");
        }
    }

    public static boolean isV2CipherText(String cipherText) {
        return StringUtils.isNotBlank(cipherText)
                && cipherText.startsWith(CIPHER_VERSION_V2_PREFIX)
                && cipherText.length() > CIPHER_VERSION_V2_PREFIX.length() + IV_HEX_LENGTH;
    }

    public static String extractV2IvHex(String cipherText) {
        validateV2CipherText(cipherText);
        int start = CIPHER_VERSION_V2_PREFIX.length();
        return cipherText.substring(start, start + IV_HEX_LENGTH);
    }

    public static String extractV2CipherHex(String cipherText) {
        validateV2CipherText(cipherText);
        int start = CIPHER_VERSION_V2_PREFIX.length() + IV_HEX_LENGTH;
        return cipherText.substring(start);
    }

    private static byte[] generateRandomIv() {
        byte[] iv = new byte[AES_BLOCK_SIZE];
        SECURE_RANDOM.nextBytes(iv);
        return iv;
    }

    private static void validateAesKeyLength(String appKey) {
        if (StringUtils.isBlank(appKey)) {
            throw CommonErrors.INVALID_PARAMETER.createException("appKey为空");
        }
        int keyLength = appKey.getBytes(StandardCharsets.UTF_8).length;
        if (keyLength != 16 && keyLength != 24 && keyLength != 32) {
            throw CommonErrors.INVALID_PARAMETER.createException("appKey长度非法，需为16/24/32字节");
        }
    }

    private static void validateV2CipherText(String cipherText) {
        if (!isV2CipherText(cipherText)) {
            throw CommonErrors.INVALID_PARAMETER.createException("非法V2密文格式");
        }
    }

}
