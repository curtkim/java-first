package org.example;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class Main {
  public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
    String plainText = "www.baeldung.com";
    String password = "baeldung";
    String salt = "12345678";

    IvParameterSpec ivParameterSpec = AESUtil.generateIv();
    SecretKey secretKey = AESUtil.getKeyFromPassword(password, salt);

    String cipherText = AESUtil.encryptPasswordBased(plainText, secretKey, ivParameterSpec);
    System.out.println(plainText.length() + " : " + cipherText.length()); // 16 : 44
    String decryptedCipherText = AESUtil.decryptPasswordBased(cipherText, secretKey, ivParameterSpec);
    assert plainText.equals(decryptedCipherText);
  }

}