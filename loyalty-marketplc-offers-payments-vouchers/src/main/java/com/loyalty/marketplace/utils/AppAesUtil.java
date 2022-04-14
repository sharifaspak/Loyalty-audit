package com.loyalty.marketplace.utils;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppAesUtil {
	protected AppAesUtil() {

	}
	static final Logger LOG = LoggerFactory.getLogger(AppAesUtil.class);
    public static String encrypt(String privateString, SecretKey skey)  {
    	try {
        byte[] iv = new byte[12];
        (new SecureRandom()).nextBytes(iv);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec ivSpec = new GCMParameterSpec(16 * Byte.SIZE, iv);
        cipher.init(Cipher.ENCRYPT_MODE, skey, ivSpec);

        byte[] ciphertext = cipher.doFinal(privateString.getBytes(StandardCharsets.UTF_8));
        byte[] encrypted = new byte[iv.length + ciphertext.length];
        System.arraycopy(iv, 0, encrypted, 0, iv.length);
        System.arraycopy(ciphertext, 0, encrypted, iv.length, ciphertext.length);

        return Base64.getEncoder().encodeToString(encrypted);
    	}catch (Exception ex) {
            LOG.error("Error while encrypting {}",ex.getMessage());
        }

        return null;

    }

    public static String decrypt(String encrypted, SecretKey skey)   {
    	try {
        byte[] decoded = Base64.getDecoder().decode(encrypted);
        byte[] iv = Arrays.copyOfRange(decoded, 0, 12);
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec ivSpec = new GCMParameterSpec(16 * Byte.SIZE, iv);
        cipher.init(Cipher.DECRYPT_MODE, skey, ivSpec);

        byte[] ciphertext = cipher.doFinal(decoded, 12, decoded.length - 12);

        return new String(ciphertext, StandardCharsets.UTF_8);
    }catch (Exception ex) {
        LOG.error("Error while decrypting {}",ex.getMessage());
    }

    return null;
    }

}

