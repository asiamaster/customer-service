package com.dili.customer.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.Security;
import java.util.Arrays;

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
    /**
     * key
     */
    private static Key key;
    private static Cipher cipher;

    public static void init(byte[] keyBytes) {
        try {
            int base = 16;
            if (keyBytes.length % base != 0) {
                int groups = keyBytes.length / base
                        + (keyBytes.length % base != 0 ? 1 : 0);
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyBytes, 0, temp, 0, keyBytes.length);
                keyBytes = temp;
            }
            Security.addProvider(new BouncyCastleProvider());
            key = new SecretKeySpec(keyBytes, KEY_ALGORITHM);
            // 初始化cipher
            cipher = Cipher.getInstance(ALGORITHM);
        } catch (Exception e) {
            log.error("init Applet Key error", e);
        }
    }

    /**
     * 解密方法
     *
     * @param encryptedDataStr 要解密的字符串
     * @param keyBytesStr      解密密钥
     * @return
     */
    public static String decrypt(String encryptedDataStr, String keyBytesStr, String ivStr) {
        String decryStr = "";
        try {
            byte[] sessionKey = Base64.decodeBase64(keyBytesStr);
            byte[] encryptedData = Base64.decodeBase64(encryptedDataStr);
            byte[] iv = Base64.decodeBase64(ivStr);
            init(sessionKey);
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
            byte[] encryptedText = cipher.doFinal(encryptedData);
            if (encryptedText != null) {
                decryStr = new String(encryptedText, "UTF-8");
            }
        } catch (Exception e) {
            log.error(String.format("数据【%s】解密失败:%s", encryptedDataStr, e.getMessage()), e);
        }
        return decryStr;
    }
}
