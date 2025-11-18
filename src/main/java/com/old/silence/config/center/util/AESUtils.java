package com.old.silence.config.center.util;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import com.old.silence.core.context.CommonErrors;

/**
 * @author moryzang
 */
public final class AESUtils {

    private static final String AES_TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final byte[] IV = "pidms20180327!@#".getBytes(StandardCharsets.UTF_8);

    private AESUtils() {
        throw new AssertionError();
    }

    public static String encrypt(String plainText, String appKey) {
        try {
            Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
            SecretKeySpec secretKeySpec = new SecretKeySpec(appKey.getBytes(StandardCharsets.UTF_8), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, new IvParameterSpec(IV));
            byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            return Hex.encodeHexString(encrypted);
        } catch (GeneralSecurityException e) {
            throw CommonErrors.INVALID_PARAMETER.createException("加密失败");
        }
    }

}
