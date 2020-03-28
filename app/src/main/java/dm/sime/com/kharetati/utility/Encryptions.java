/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dm.sime.com.kharetati.utility;
//import java.util.Base64;
import android.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
/**
 *
 * @author hasham
 */
public class Encryptions {
    private static String sharedkey = "%467IsB^54,8Encryption#2"; //multiple of 8
    private static String encoding = "UTF-8";
    private static String encryption = "DESede";

  private static String sharedvector = "34g@67!5";

  public static void main(String... argv)
    throws Exception
  {
    String plaintext = "textToEncrypt";
    //String ciphertext = encrypt(plaintext);
   /* System.out.println(ciphertext);
    System.out.println(decrypt(ciphertext));*/
  }

  /*public static String encrypt(String plaintext)
    throws Exception
  {
      byte[] keyBytes = sharedkey.getBytes(encoding);
      byte[] vectorBytes = sharedvector.getBytes(encoding);
    Cipher c = Cipher.getInstance("DESede/CBC/PKCS5Padding");
    c.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(keyBytes, encryption), new IvParameterSpec(vectorBytes));
    byte[] encrypted = c.doFinal(plaintext.getBytes("UTF-8"));
    return Base64.getEncoder().encodeToString(encrypted); //in android you will use Base64.encodeToString(encryptedText, Base64.DEFAULT);
  }*/

  public static String decrypt(String ciphertext)
  {
      try {
          byte[] keyBytes = sharedkey.getBytes(encoding);
          byte[] vectorBytes = sharedvector.getBytes(encoding);
          Cipher c = Cipher.getInstance("DESede/CBC/PKCS5Padding");
          c.init(Cipher.DECRYPT_MODE, new SecretKeySpec(keyBytes, encryption), new IvParameterSpec(vectorBytes));
          //byte[] decrypted = c.doFinal(Base64.getDecoder().decode(ciphertext)); // in android you will use Base64.decode(encryptedString, Base64.DEFAULT);
          byte[] decrypted = c.doFinal(Base64.decode(ciphertext, Base64.NO_WRAP)); // in android you will use Base64.decode(encryptedString, Base64.DEFAULT);
          return new String(decrypted, "UTF-8");
      } catch (Exception e){

          System.out.println("Error while decrypting: " + e.toString());
      }
      return null;
  }

}
