package com.dili.customer.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.tuple.Pair;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.Security;
import java.util.Arrays;
import java.util.Objects;

/**
 * 微信小程序解密工具类
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/12/10 11:23
 */
@Slf4j
public class WeChatAppletAesUtil {
    /**
     * 算法名称
     */

    private static final String KEY_ALGORITHM = "AES";

    /**
     * 加解密算法/模式/填充方式
     */
    private static final String ALGORITHM = "AES/CBC/PKCS7Padding";


    public static Pair<Key, Cipher> init(byte[] keyBytes) {
        try {
            Key key;
            Cipher cipher;
            int base = 16;
            if (keyBytes.length % base != 0) {
                int groups = keyBytes.length / base + (keyBytes.length % base != 0 ? 1 : 0);
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyBytes, 0, temp, 0, keyBytes.length);
                keyBytes = temp;
            }
            Security.addProvider(new BouncyCastleProvider());
            key = new SecretKeySpec(keyBytes, KEY_ALGORITHM);
            // 初始化cipher
            cipher = Cipher.getInstance(ALGORITHM);
            return Pair.of(key, cipher);
        } catch (Exception e) {
            log.error("init AppletRequest Key error", e);
            return null;
        }
    }


    /**
     * 解密方法
     *
     * @param encryptedDataStr 要解密的字符串
     * @param sessionKeyStr      解密密钥
     * @return
     */
    public static String decrypt(String encryptedDataStr, String sessionKeyStr, String ivStr) throws BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException, InvalidAlgorithmParameterException, InvalidKeyException {
        String decryStr = "";
        byte[] sessionKey = Base64.decodeBase64(sessionKeyStr);
        byte[] encryptedData = Base64.decodeBase64(encryptedDataStr);
        byte[] iv = Base64.decodeBase64(ivStr);
        Pair<Key, Cipher> pair = init(sessionKey);
        if (Objects.isNull(pair)) {
            return null;
        }
        Key key = pair.getLeft();
        Cipher cipher = pair.getRight();
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
        byte[] encryptedText = cipher.doFinal(encryptedData);
        if (encryptedText != null) {
            decryStr = new String(encryptedText, "UTF-8");
        }
        return decryStr;
    }
}
