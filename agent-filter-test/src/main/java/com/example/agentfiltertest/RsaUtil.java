package com.example.agentfiltertest;


import org.junit.jupiter.api.Test;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yueqiuhong
 * @date 2022/5/22
 */
public class RsaUtil {
    public static final String KEY_TYPE_PRIVATE_KEY = "privateKey";
    public static final String KEY_TYPE_PUBLIC_KEY = "publicKey";

    /**
     * 生成公钥和私钥
     *
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static Map<String, String> generateKey() throws NoSuchAlgorithmException {
        Map<String, String> resultMap = new HashMap<>();
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        Base64.Encoder encoder = Base64.getEncoder();
        resultMap.put(KEY_TYPE_PRIVATE_KEY, encoder.encodeToString(keyPair.getPrivate().getEncoded()));
        resultMap.put(KEY_TYPE_PUBLIC_KEY, encoder.encodeToString(keyPair.getPublic().getEncoded()));
        return resultMap;
    }

    /**
     * RSA加密
     *
     * @param key
     * @param content
     * @param keyType
     * @return
     * @throws Exception
     */
    public static String rsaEncrypt(String key, String content, String keyType) throws Exception {
        return rsa(key, content.getBytes(), keyType, Cipher.ENCRYPT_MODE);
    }

    /**
     * RSA解密
     *
     * @param key
     * @param content
     * @param keyType
     * @return
     * @throws Exception
     */
    public static String rsaDecrypt(String key, String content, String keyType) throws Exception {
        return rsa(key, Base64.getDecoder().decode(content), keyType, Cipher.DECRYPT_MODE);
    }

    private static String rsa(String key, byte[] content, String keyType, int mode) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        if (KEY_TYPE_PRIVATE_KEY.equals(keyType)) {
            cipher.init(mode, keyFactory.generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(key))));
        } else {
            cipher.init(mode, keyFactory.generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(key))));
        }
        byte[] bytes = cipher.doFinal(content);
        return mode == Cipher.DECRYPT_MODE ? new String(bytes) : Base64.getEncoder().encodeToString(bytes);
    }

    @Test
    public void test1() throws NoSuchAlgorithmException {
        //生成密钥对
        Map<String, String> keyMap = generateKey();
        System.out.println("私钥：" + keyMap.get(KEY_TYPE_PRIVATE_KEY));
        System.out.println("公钥：" + keyMap.get(KEY_TYPE_PUBLIC_KEY));
    }

    public static void main(String[] args) throws Exception {
        String content = "大王叫我来巡山呐123312";

        //生成密钥对
//        Map<String, String> keyMap = generateKey();
//        System.out.println("私钥：" + keyMap.get(KEY_TYPE_PRIVATE_KEY));
//        System.out.println("公钥：" + keyMap.get(KEY_TYPE_PUBLIC_KEY));

        String privateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCPqQY8f76MfGNFrdD+nAb9ykCVrCRZE1Nhulz5Pikp9YistVzCxqNAaP2cHzdUwLxPSqbZ0V5gaocsugtByzzM9h6eebtSD/72/hlNBHR/B7jwsLwLSwNeaXagMIqNv9Ndhf1EqAt10pXUnd2e7UNJ9nLgPY92cNxZeyQtEEm1muR9anmcOhJHJ58dultpTr+Z6IK2uOROcYH3vPA+qfYFEL/zPjMZ+XVyBz1mI06BpkADeMCCNn030LBAgIdH+3clahVr6MKV2Oc9JETwaPwGiiS5xGk3PBVc6MWC6iyUO7/9t24JESZBbaTDQu2pMUYe/TZ4jYodO2kdgUa2JqbdAgMBAAECggEBAIMDquRRu/1zEzXgptEMinO5l9zn+N4jec+OnmvP/Mp/Wf3oCIi5jviWx3MyJMvuWc4WTsQm5OMZQ93ObaW7K9/gNVxhuCvG2sd2RKywe22+JTOcemHyL0hS9hRxMGrFnoWURL3wY0M4odwdfJgL2WoCkBFhEwz5MHuKihtu10n+acPc7wN0zQclDqriNGKzHSOabMzG4CREqKDiHqLolsc9sKEi6ANUBI6cP65K07IDhU4ezgJn591zCj/Swn2PkoUuk4AOQpapg8hQ4Y0PuRoWKM/mZOfUP04Dl2xEG2UP8qi/ov1Qc+1Nxt0DIobm4ya6FP7dQef90jyNAsOowQECgYEA8IvUFlboa+cfbMxDyJ/7ZEIY0SCG3YM7WsCK0ZQGIbneBoIKam8cGwOeZmZDzqMf5ZtOlWBMrrRtTxsjPecmcUgOfbSmIDG0NOUrMrv6yByz1YEhH9p3YtUPPh5AM1n/xoEKYUk2hJSYMSdJFLRBVYT3JnaObiIYODAwy0I6c3kCgYEAmOO/59f12YRhDqXSaK/eRWsa1kEqiRfgveSadhdnK+mFehb3GBpqvAsajLwM2+LyzrK1paCr6bKGiZoEoYr4YoiLj9cwQawD9lp9Ft+vhXVWJJGPobURAtcPVlM95owvDrqVoVbQhI8Mq5ZzWyiAU8b1QZ7j3VrX1L3kyaUCsYUCgYEAy98nFPv5pyJPLw380ThOf83JbM1toKaOEr+nqDMV81WtyMREgarEHl01zkIUbDkzWo4Dz3S76UJAdtBgUSHtIgvZRE9skVKvEjm9IBXUWfCBto3n/jBSCTDc52AKN7uqnZpWqte7Xij2yIosVWDlEs9MDYxj8RHyhwTJBiyOj7kCgYAmsgXhHPSZX/rcpTVyz4g3E5uQPKASYJu+E/zQBK5VG95vfx582DrJaxoGIpYylOcVT3/P8rFwDYIlk3i2liC1d4+zESiihnHeSRq9zlFzOrZZvQmYSOaMTHgSRjE1h6dlV+4FDp7p3fa7+vEpUj/gdEuglyFe5d4YdxNPwdbDrQKBgGHZBnST/vKxITPYxQDrjFjaRCuFgiL/BjHOlABxed5qfi2P4Z0lsR3TwzuOcr/oY8a1t+H/iqqFGnLUUMdKl6LZWTg5HIQvf+ORfTFBPau3VkjgichvgFxLxYcdGONE9EaFw5hZSN1qJktUWVrQgqVtZoPy/qKG2Sqq3+ozHR7S";
        String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAj6kGPH++jHxjRa3Q/pwG/cpAlawkWRNTYbpc+T4pKfWIrLVcwsajQGj9nB83VMC8T0qm2dFeYGqHLLoLQcs8zPYennm7Ug/+9v4ZTQR0fwe48LC8C0sDXml2oDCKjb/TXYX9RKgLddKV1J3dnu1DSfZy4D2PdnDcWXskLRBJtZrkfWp5nDoSRyefHbpbaU6/meiCtrjkTnGB97zwPqn2BRC/8z4zGfl1cgc9ZiNOgaZAA3jAgjZ9N9CwQICHR/t3JWoVa+jCldjnPSRE8Gj8BookucRpNzwVXOjFguoslDu//bduCREmQW2kw0LtqTFGHv02eI2KHTtpHYFGtiam3QIDAQAB";

        //私钥加密，公钥解密
//        String privateKeyData = rsaEncrypt(privateKey, content, KEY_TYPE_PRIVATE_KEY);
        String privateKeyData = "auT32VJTrViX2sWsgQQgBZwQIhBvXoSN9onCejgHPtCEpifgUu+E6XS9z6kqE3r/xeNRhJpBIoruoAYnbvKmw8UoMjkfzpczvIqN3XPmlmomcG36N9dIeUfW/CxEmWVv5szaNPjmpOuQDVupnlPPf8IWw6mJa45dDPBIDQVEZ+CxbqsNt8dDanSsUsQYWWvZUxXbl8VD/60n1ulIjTg40f5XbvRT7bak1GzuLbI1DhJbgyuBML7dI9E8HCYQQg4vkNZPWHp2xrG02OzUgaw9h1XZiApOz9imfLbDeoNefYpuOCHT4a99DqpVmMfL6hy8by4Em0hoySJ1JgMwKNpU/w==";
        System.out.println("私钥加密：" + privateKeyData);
        System.out.println("公钥解密：" + rsaDecrypt(publicKey, privateKeyData, KEY_TYPE_PUBLIC_KEY));

        //公钥加密，私钥解密
//        String publicKeyData = rsaEncrypt(keyMap.get(KEY_TYPE_PUBLIC_KEY), content, KEY_TYPE_PUBLIC_KEY);
//        System.out.println("公钥加密：" + publicKeyData);
//        System.out.println("私钥解密：" + rsaDecrypt(keyMap.get(KEY_TYPE_PRIVATE_KEY), publicKeyData, KEY_TYPE_PRIVATE_KEY));
    }
}