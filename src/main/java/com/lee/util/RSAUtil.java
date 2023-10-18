package com.lee.util;


import org.apache.commons.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

public class RSAUtil {
    /**
     * web端公钥(用来加密数据)
     */
    public static final String WEB_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCpCnxEAhVsl0rnKUvMAdD83I7p+eTO3XqtYUfza1B5MNOPR/P42CBqw1hH204HwBeuM0T9RcqiLKbjRTETnBHvkL3HDmGqJ4RPkMpgoDcVZA73IwjP+iz5I59POS2d+Nazw18ALIh4TVHxkTxn1mcLTaaOXVtokeaYywrBWDN1EwIDAQAB";
    /**
     * web端私钥(用来解密数据)
     */
    public static final String WEB_PRIVATE_KEY = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAKkKfEQCFWyXSucpS8wB0Pzcjun55M7deq1hR/NrUHkw049H8/jYIGrDWEfbTgfAF64zRP1FyqIspuNFMROcEe+QvccOYaonhE+QymCgNxVkDvcjCM/6LPkjn085LZ341rPDXwAsiHhNUfGRPGfWZwtNpo5dW2iR5pjLCsFYM3UTAgMBAAECgYEApkFa0OSfv6rZj5ttIhvTOS+QHA2cfd0VUDsysfWlM1zTZsOk7+VZlMfGJv5Js5O5dqqUOuoOjwyNo0+lIh1JAQ43BT2PO6CAZZ9puKkNMuP9aO5lQu8Rq5Rbu1Hy2irqqaeCwU9gkR06xd4vSJ2cDs2Bv34bfQzsPKX5aB7XijECQQD8I6szoZa/RS9gsJyVwCe/oI8wjcJ0ChxyDYZCWC5FklbLRqM9aysEfSBPLPbX6y+SJL3LCTyHjuCPZOtBFyprAkEAq6EW0fOECDVQttI/iC/4oIq2VQ5W/G/gpzrlDo9D0o9tyc/gmOwjlEDSYdc9T8G82cfc0A9pf/k6ZQxGw8RZ+QJBAMFZWN+u+jqSg1JSHMF5KQH5zdQd4Q+68TVEyb/2fwU6wvP+pw9iLwqyKSSOBnxtDX20TxZzbsdE+U78xKqVaSECQGcG23kK8m4u+xXUHjk1es18lzl10zfUPQqGKyxTSgZi/SfRduS1lCIRy3jaST7QcPpWpLHtHoqo5Kn19aDdBJkCQQD1H+JTaPuKz+kGCkvfliKnaf13M8ibc3Rk2wx6GLmZOteFaPPujyWxuDAyrCdY0tlZX7cdR1zAe0eFD+kMASVK";
    /**
     * 服务端公钥(用来加密数据)
     */
    public static final String SERVICE_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCG2tCWBe/I1gZYJvNzy3JPUDEP3O+v4sFD3xtBvAz7/wV25BcTEYAfdkjylhw2QHjIsqFN9IhONbminQzznnueg0KBxUYpfqeC0hva8ag831uCbP1r8AYYhzYJAgkWWLLOtLbFNbL9eeDkSaf29MVyzq4cdEW5pRbAijjiId4ilQIDAQAB";
    /**
     * 服务端私钥(用来解密数据)
     */
    public static final String SERVICE_PRIVATE_KEY = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIba0JYF78jWBlgm83PLck9QMQ/c76/iwUPfG0G8DPv/BXbkFxMRgB92SPKWHDZAeMiyoU30iE41uaKdDPOee56DQoHFRil+p4LSG9rxqDzfW4Js/WvwBhiHNgkCCRZYss60tsU1sv154ORJp/b0xXLOrhx0RbmlFsCKOOIh3iKVAgMBAAECgYAI5b8FZvbfJV/dyRQe+7lbCktyZH1b+XYO6FWADMw+hCD0KzBqjakKal99wfGykqZld4O1BQFJR7orxHjqSfaV8kCJkRNR7rxtVdbYAeWa5w6Ai4hD8RXUXRndaUqSjw7S1xYHbFewukOmHVA7XV5iQsOTzaANLR++Mi5NR+m6AQJBAMILmeJipKmmlMtroaU4vxBhRWFdPgFTo3QYRg02RSr5ofSJ5wvqlCfnMJSImP3eGXUwSYMaN8yqDLW4aNrnxSECQQCx6T2E0dD3M9dk7mXGxlsxpbfVWLEMqwe6ahrdncdfX4uZ/wNwpNNMgZzqgSH0bpgJz9kHM93l3mpNfHrNxTr1AkBlO0QmtKuOzhbmINqNpZMX6ocVil1kt8uYL2msTtm618zEFIVy1AhC4PuZIcIe/xVs66oOOUlO/o/u5aveN/fBAkBLxkSecmLnNtxRr7wHempS7fsrUhguhhXHjvOcWlwVQfOKhnPnoTwhxL6ZnqBgpE37N9x+1dd31VA/ano/c/aVAkA0xcuuehyeG9A3Y9wWlkDrbe6RCjHZv2qTSZoOtWsfykng55XLmlo7jF+WFFX1Tc2+JrnaOlA3O28HVAXLhnYd";


    /**
     * 密钥算法RSA
     */
    public static final String KEY_ALGORITHM = "RSA";

    /**
     * 加密算法RSA
     */
    public static final String CIPHER_ALGORITHM = "RSA/ECB/PKCS1Padding";

    /**
     * 签名算法
     */
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    /**
     * 获取公钥的key
     */
    public static final String PUBLIC_KEY = "LeePublicKey";

    /**
     * 获取私钥的key
     */
    public static final String PRIVATE_KEY = "LeePrivateKey";

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    private static final int INITIALIZE = 1024;

    /**
     * 生成密钥对(公钥和私钥)
     */
    public static Map<String, Key> genKeyPair() {
        KeyPairGenerator keyPairGen;
        try {
            keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        keyPairGen.initialize(INITIALIZE);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        Map<String, Key> keyMap = new HashMap<String, Key>();
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }

    /**
     * 私钥解密
     *
     * @param srcData    已加密数据
     * @param privateKey 私钥(BASE64编码)
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws UnsupportedEncodingException
     */
    public static String decryptByPrivateKey(String srcData, String privateKey)
            throws InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        ByteArrayOutputStream out = null;
        try {
            byte[] keyBytes = Base64.decodeBase64(privateKey.getBytes());
            byte[] encryptedData = Base64.decodeBase64(srcData);
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, privateK);
            int inputLen = encryptedData.length;
            out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_DECRYPT_BLOCK;
            }
            byte[] decryptedData = out.toByteArray();
            return new String(decryptedData, "utf-8");
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 公钥解密
     *
     * @param srcData   已加密数据
     * @param publicKey 公钥(BASE64编码)
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws UnsupportedEncodingException
     */
    public static String decryptByPublicKey(String srcData, String publicKey)
            throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        ByteArrayOutputStream out = null;
        try {
            byte[] keyBytes = Base64.decodeBase64(publicKey.getBytes());
            byte[] encryptedData = Base64.decodeBase64(srcData.getBytes());
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            Key publicK = keyFactory.generatePublic(x509KeySpec);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, publicK);
            int inputLen = encryptedData.length;
            out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_DECRYPT_BLOCK;
            }
            byte[] decryptedData = out.toByteArray();
            return new String(decryptedData, "utf-8");
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 公钥加密
     *
     * @param srcData   源数据
     * @param publicKey 公钥(BASE64编码)
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws UnsupportedEncodingException
     */
    public static String encryptByPublicKey(String srcData, String publicKey) throws Exception {
        ByteArrayOutputStream out = null;
        try {
            byte[] keyBytes = Base64.decodeBase64(publicKey.getBytes());
            byte[] data = srcData.getBytes("utf-8");
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            Key publicK = keyFactory.generatePublic(x509KeySpec);
            // 对数据加密
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, publicK);
            int inputLen = data.length;
            out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段加密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_ENCRYPT_BLOCK;
            }
            byte[] encryptedData = out.toByteArray();
            return Base64.encodeBase64String(encryptedData);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 私钥加密
     *
     * @param srcData    源数据
     * @param privateKey 私钥(BASE64编码)
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws UnsupportedEncodingException
     */
    public static String encryptByPrivateKey(String srcData, String privateKey) throws Exception {
        ByteArrayOutputStream out = null;
        try {
            byte[] keyBytes = Base64.decodeBase64(privateKey.getBytes());
            byte[] data = srcData.getBytes("utf-8");
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, privateK);
            int inputLen = data.length;
            out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段加密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_ENCRYPT_BLOCK;
            }
            byte[] encryptedData = out.toByteArray();
            return Base64.encodeBase64String(encryptedData);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取私钥
     *
     * @param keyMap 密钥对
     */
    public static String getPrivateKey(Map<String, Key> keyMap) {
        Key key = keyMap.get(PRIVATE_KEY);
        return Base64.encodeBase64String(key.getEncoded());
    }

    /**
     * 获取公钥
     *
     * @param keyMap 密钥对
     */
    public static String getPublicKey(Map<String, Key> keyMap) {
        Key key = (Key) keyMap.get(PUBLIC_KEY);

        System.out.println("转换前长度" + key.getEncoded().length);

        return Base64.encodeBase64String(key.getEncoded());
    }

    /**
     * RSA-公钥解密
     *
     * @param ensrc
     * @param publicKey
     * @return
     */
    public static String getDecryptByPublicKeyChnlcdByRSA(String ensrc, String publicKey) {
        try {
            return decryptByPublicKey(ensrc, publicKey);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("解密失败！");
        }
    }

    /**
     * RSA-私钥解密
     *
     * @param ensrc
     * @param privateKey
     * @return
     */
    public static String getDecryptByPrivateKeyChnlcdByRSA(String ensrc, String privateKey) {
        try {
            return decryptByPrivateKey(ensrc, privateKey);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("解密失败！");
        }
    }

    /**
     * 加密RSA
     *
     * @param str
     * @return
     */
    public static String getEncryptByPrivateKeyByRSA(String str) {
        String encryStr = "";
        try {
            encryStr = encryptByPrivateKey(str, "私钥");
            encryStr =  java.util.Base64.getEncoder().encodeToString(encryStr.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("加密失败！");
        }
        return encryStr;
    }

    /**
     * RSA-私钥加密
     *
     * @param str
     * @param privateKey
     * @return
     */
    public static String getEncryptByPrivateKeyByRSA(String str, String privateKey) {
        try {
            return encryptByPrivateKey(str, privateKey);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("加密失败！");
        }
    }

    /**
     * RSA-公钥加密
     *
     * @param str
     * @param publicKey
     * @return
     */
    public static String getEncryptByPublicKeyByRSA(String str, String publicKey) {
        try {
            return encryptByPublicKey(str, publicKey);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("加密失败！");
        }
    }

    /**
     * 加密RSA
     *
     * @param str
     * @return
     */
    public static String getEncryptByPrivateKeyByRSA1(String str) {
        String encryStr = "";
        try {
            encryStr = encryptByPrivateKey(str, "私钥");
            str = java.util.Base64.getEncoder().encodeToString(encryStr.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("加密失败！");
        }
        return str;
    }


    public static void main(String[] args) throws Exception {
//        Map<String, Key> keymap = genKeyPair();
//        String privateKey = getPrivateKey(keymap);
//        String publicKey = getPublicKey(keymap);
//        System.out.println("Public Key:" + publicKey);
//        System.out.println("Private Key:" + privateKey);

        String serviceEncryptStr = "YAGJOm+yB0u8eQAnySpkjy+B/GbrQYc3lcS4sLWnR63NKUzOlLMqdYcf55IAm1wkCxqmYa1qGKqZdMLZY1F12VFFpSAP0LasuNTqLF2tJmR89F3kTfIeNeB6Ru9x315ft6iuVs1wieCRY3UTCx+VDNDZg+R9LOpjTELFTrEYIK1N1p0WwRqlXJfPxi6aZPW8pv0CN4RB2A//p+++YOQ0xk+0Dv9PHgjwqYERmU/7gCcbmja5YYNYkX4+2OR74T2ypdgsz9JC42xTuyg80MpZkhvUL8fnxtvo1Br0HApCu6a4UqbvaT5oao/21g5cl6PW3VmPjQcAm2XpN61Z+5csvA==";
        System.out.println("前端解密:" + RSAUtil.decryptByPrivateKey(serviceEncryptStr, RSAUtil.WEB_PRIVATE_KEY));

        String webSrcStr = "{\"childrenName\":\"李忆如\",\"childrenPhone\":\"16666666666\",\"parentPhone\":\"18888888888\",\"parentWife\":{\"wifeName\":\"赵灵儿\",\"wifePhone\":\"19999999999\"},\"parentName\":\"李逍遥\"}";
        System.out.println("前端加密:" + RSAUtil.encryptByPublicKey(webSrcStr, RSAUtil.SERVICE_PUBLIC_KEY));

    }
}
