package com.qzt.ump.server.controllerBack;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Arrays;

public class SM4Util {

    static {
        // Try to register BouncyCastle without compile-time dependency
        try {
            Class<?> cls = Class.forName("org.bouncycastle.jce.provider.BouncyCastleProvider");
            Security.addProvider((java.security.Provider) cls.newInstance());
        } catch (Throwable ignore) {
            // Provider will still be available at runtime if configured globally
        }
    }

    private static final String ENCODING = "UTF-8";
    public static final String ALGORITHM_NAME = "SM4";
    // 加密算法/分组加密模式/分组填充方式
    // PKCS5Padding-以8个字节为一组进行分组加密
    // 定义分组加密模式使用：PKCS5Padding
    public static final String ALGORITHM_NAME_ECB_PADDING = "SM4/ECB/PKCS5Padding";
    // 128-32位16进制；256-64位16进制
    public static final int DEFAULT_KEY_SIZE = 128;

    // --- hex helpers (replace ByteUtils) ---
    private static byte[] fromHexString(String hex) {
        if (hex == null) return new byte[0];
        String s = hex.trim();
        if ((s.length() & 1) != 0) {
            throw new IllegalArgumentException("Hex string must have even length");
        }
        int len = s.length();
        byte[] out = new byte[len / 2];
        for (int i = 0, j = 0; i < len; i += 2) {
            int hi = Character.digit(s.charAt(i), 16);
            int lo = Character.digit(s.charAt(i + 1), 16);
            if (hi < 0 || lo < 0) {
                throw new IllegalArgumentException("Invalid hex character");
            }
            out[j++] = (byte) ((hi << 4) + lo);
        }
        return out;
    }

    private static String toHexString(byte[] bytes) {
        if (bytes == null) return "";
        char[] hexArray = "0123456789abcdef".toCharArray();
        char[] chars = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            chars[i * 2] = hexArray[v >>> 4];
            chars[i * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(chars);
    }

    /**
     * 生成ECB暗号
     *
     * @param algorithmName 算法名称
     * @param mode          模式
     * @param key           密钥字节
     * @return Cipher 实例
     * @throws Exception 初始化失败时抛出
     */
    private static Cipher generateEcbCipher(String algorithmName, int mode, byte[] key) throws Exception {
        Cipher cipher = Cipher.getInstance(algorithmName, "BC");
        Key sm4Key = new SecretKeySpec(key, ALGORITHM_NAME);
        cipher.init(mode, sm4Key);
        return cipher;
    }

    /**
     * 自动生成密钥
     *
     * @return 16进制字符串密钥
     * @throws Exception 生成失败抛出
     */
    public static String generateKey() throws Exception {
        return toHexString(generateKey(DEFAULT_KEY_SIZE));
    }

    /**
     * @param keySize 密钥长度
     * @return 原始密钥字节数组
     * @throws Exception 生成失败抛出
     */
    public static byte[] generateKey(int keySize) throws Exception {
        KeyGenerator kg = KeyGenerator.getInstance(ALGORITHM_NAME, "BC");
        kg.init(keySize, new SecureRandom());
        return kg.generateKey().getEncoded();
    }

    /**
     * sm4加密
     *
     * @param hexKey   16进制密钥（忽略大小写）
     * @param paramStr 待加密字符串
     * @return 返回16进制的加密字符串
     * @throws Exception 加密失败抛出
     */
    public static String encryptEcb(String hexKey, String paramStr) throws Exception {
        String cipherText = "";
        // 16进制字符串-->byte[]
        byte[] keyData = fromHexString(hexKey);
        // String-->byte[]
        byte[] srcData = paramStr.getBytes(ENCODING);
        // 加密后的数组
        byte[] cipherArray = encrypt_Ecb_Padding(keyData, srcData);
        // byte[]-->hexString
        cipherText = toHexString(cipherArray);
        return cipherText;
    }

    /**
     * 加密模式之Ecb
     *
     * @param key  密钥字节
     * @param data 明文字节
     * @return 密文字节
     * @throws Exception 加密失败抛出
     */
    public static byte[] encrypt_Ecb_Padding(byte[] key, byte[] data) throws Exception {
        Cipher cipher = generateEcbCipher(ALGORITHM_NAME_ECB_PADDING, Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data);
    }

    /**
     * sm4解密
     *
     * @param hexKey     16进制密钥
     * @param cipherText 16进制的加密字符串（忽略大小写）
     * @return 解密后的字符串
     * @throws Exception 解密失败抛出
     */
    public static String decryptEcb(String hexKey, String cipherText) throws Exception {
        // 用于接收解密后的字符串
        String decryptStr = "";
        // hexString-->byte[]
        byte[] keyData = fromHexString(hexKey);
        // hexString-->byte[]
        byte[] cipherData = fromHexString(cipherText);
        // 解密
        byte[] srcData = decrypt_Ecb_Padding(keyData, cipherData);
        // byte[]-->String
        decryptStr = new String(srcData, ENCODING);
        return decryptStr;
    }

    /**
     * 解密
     *
     * @param key        密钥字节
     * @param cipherText 密文字节
     * @return 明文字节
     * @throws Exception 解密失败抛出
     */
    public static byte[] decrypt_Ecb_Padding(byte[] key, byte[] cipherText) throws Exception {
        Cipher cipher = generateEcbCipher(ALGORITHM_NAME_ECB_PADDING, Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(cipherText);
    }

    /**
     * 校验加密前后的字符串是否为同一数据
     *
     * @param hexKey     16进制密钥（忽略大小写）
     * @param cipherText 16进制加密后的字符串
     * @param paramStr   加密前的字符串
     * @return 是否为同一数据
     * @throws Exception 解密失败抛出
     */
    public static boolean verifyEcb(String hexKey, String cipherText, String paramStr) throws Exception {
        // 用于接收校验结果
        boolean flag = false;
        // hexString-->byte[]
        byte[] keyData = fromHexString(hexKey);
        // 将16进制字符串转换成数组
        byte[] cipherData = fromHexString(cipherText);
        // 解密
        byte[] decryptData = decrypt_Ecb_Padding(keyData, cipherData);
        // 将原字符串转换成byte[]
        byte[] srcData = paramStr.getBytes(ENCODING);
        // 判断2个数组是否一致
        flag = Arrays.equals(decryptData, srcData);
        return flag;
    }

    public static void main(String[] args) {
        try {
            String data = "2023-07-12,下午三点";
            //生成key
            String key = generateKey();
            System.out.println("key:" + key);
            //加密
            String cipher = SM4Util.encryptEcb(key, data);
            System.out.println("加密后："+cipher);
            //判断是否正确
            System.out.println(SM4Util.verifyEcb(key, cipher, data));// true
            //解密
            String res = SM4Util.decryptEcb(key, cipher);
            System.out.println("解密后："+res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
