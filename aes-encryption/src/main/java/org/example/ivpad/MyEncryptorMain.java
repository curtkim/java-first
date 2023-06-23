package org.example.ivpad;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

public class MyEncryptorMain {

  public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
    String password = "baeldung";
    String salt = "12345678";
    String plainText = "www.baeldung.com";

    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
    KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);
    byte[] bytes = factory.generateSecret(spec).getEncoded();
    SecretKey secret = new SecretKeySpec(bytes, "AES");

    MyEncryptor encryptor = new MyEncryptor(secret);
    String cipherText = encryptor.encrypt(plainText);
    String decryptedCipherText = encryptor.decrypt(cipherText);
    System.out.println(plainText);
    System.out.println(cipherText);
    System.out.println(decryptedCipherText);

  }
}
