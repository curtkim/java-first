package org.example.ivpad;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.io.*;
import java.security.*;
import java.util.Base64;

public class MyEncryptor {

  private static final String AES_TRANSFORMATION_MODE = "AES/CBC/PKCS5Padding";

  private Key secretAes256Key;

  public MyEncryptor(Key secretAes256Key) {
    this.secretAes256Key = secretAes256Key;
  }

  public String encrypt(String data) {
    String encryptedText = "";

    if (data == null || secretAes256Key == null) {
      return encryptedText;
    }

    try {
      Cipher encryptCipher = Cipher.getInstance(AES_TRANSFORMATION_MODE);
      encryptCipher.init(Cipher.ENCRYPT_MODE, secretAes256Key, new SecureRandom());//new IvParameterSpec(getIV()) - if you want custom IV

      //encrypted data:
      byte[] encryptedBytes = encryptCipher.doFinal(data.getBytes("UTF-8"));

      //take IV from this cipher
      byte[] iv = encryptCipher.getIV();
      System.out.println(String.format("iv.length=%d", iv.length));

      //append Initiation Vector as a prefix to use it during decryption:
      byte[] combinedPayload = new byte[iv.length + encryptedBytes.length];

      //populate payload with prefix IV and encrypted data
      System.arraycopy(iv, 0, combinedPayload, 0, iv.length);
      System.arraycopy(encryptedBytes, 0, combinedPayload, iv.length, encryptedBytes.length);

      encryptedText = Base64.getEncoder().encodeToString(combinedPayload);

    } catch (NoSuchAlgorithmException | BadPaddingException | NoSuchPaddingException | IllegalBlockSizeException |
             UnsupportedEncodingException | InvalidKeyException e) {
      e.printStackTrace();
    }

    return encryptedText;
  }

  public String decrypt(String encryptedString) {
    String decryptedText = "";

    if (encryptedString == null || secretAes256Key == null) {
      return decryptedText;
    }

    try {
      //separate prefix with IV from the rest of encrypted data
      byte[] encryptedPayload = Base64.getDecoder().decode(encryptedString);
      byte[] iv = new byte[16];
      byte[] encryptedBytes = new byte[encryptedPayload.length - iv.length];

      //populate iv with bytes:
      System.arraycopy(encryptedPayload, 0, iv, 0, 16);

      //populate encryptedBytes with bytes:
      System.arraycopy(encryptedPayload, iv.length, encryptedBytes, 0, encryptedBytes.length);

      Cipher decryptCipher = Cipher.getInstance(AES_TRANSFORMATION_MODE);
      decryptCipher.init(Cipher.DECRYPT_MODE, secretAes256Key, new IvParameterSpec(iv));

      byte[] decryptedBytes = decryptCipher.doFinal(encryptedBytes);
      decryptedText = new String(decryptedBytes);

    } catch (NoSuchAlgorithmException | BadPaddingException | NoSuchPaddingException | IllegalBlockSizeException |
             InvalidAlgorithmParameterException | InvalidKeyException e) {
      e.printStackTrace();
    }

    return decryptedText;
  }
}
