package dm.sime.com.kharetati.utility;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES {
    private static SecretKeySpec secretKey;
    private static byte[] key;

    public static void setKey(String myKey)
    {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            //sha = MessageDigest.getInstance("AES");
            key = sha.digest(key);
            key = Arrays.copyOf(key, myKey.length());
            secretKey = new SecretKeySpec(key, "AES");
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static String encrypt(String strToEncrypt, String secret)
    {
        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.encodeToString(strToEncrypt.getBytes("UTF-8"), Base64.DEFAULT);
            //return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        }
        catch (Exception e)
        {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }

    public static String decrypt1(String strToDecrypt, String secret)
    {
        try
        {
            byte[] bytekey = secret.getBytes("UTF-8");
            SecretKey currentKey = new SecretKeySpec(bytekey, "AES");
            //setKey(secret);
            //Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, currentKey);
            //byte[] data = android.util.Base64.decode(strToDecrypt, Base64.DEFAULT);
            //return new String(data, "UTF-8");
            //[] clearText = cipher.doFinal(fromHexString(strToDecrypt));
            byte[] clearText = cipher.doFinal(strToDecrypt.getBytes("utf-8"));
            return new String(clearText, "UTF-8");
            //return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        }
        catch (Exception e)
        {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }

    public static String decrypt(String strToDecrypt, String secret) {
        try {
            byte[] ivAndCipherText = Base64.decode(strToDecrypt, Base64.NO_WRAP);
            byte[] iv = Arrays.copyOfRange(ivAndCipherText, 0, 16);
            byte[] cipherText = Arrays.copyOfRange(ivAndCipherText, 16, ivAndCipherText.length);
            //String encode = Base64.encode(cipherText);
            //byte[] cipherText = strToDecrypt.getBytes();

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            //Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(secret.getBytes("utf-8"), "AES"), new IvParameterSpec(iv));
            //return new String(cipher.doFinal(ivAndCipherText), "utf-8");
            return new String(cipher.doFinal(cipherText), "utf-8");
        } catch (Exception e) {
            System.out.println("Error while decrypting: " + e.toString());
            return null;
        }
    }

    public static String decrypt3(String cipherText, String secret) {
        try {
            Cipher cipher = null;
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(secret.getBytes("utf-8"), "AES"));
            byte[] decode = Base64.decode(cipherText, Base64.NO_WRAP);
            String decryptString = new String(cipher.doFinal(decode), "UTF-8");
            return decryptString;
        } catch (Exception e) {
            System.out.println("Error while decrypting: " + e.toString());
            return null;
        }
    }

    public static byte[] fromHexString(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
}
