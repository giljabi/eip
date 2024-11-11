package kr.giljabi.eip.util;

import org.jasypt.util.text.BasicTextEncryptor;

public class JasyptEncryptionExample {
    public static void main(String[] args) {
        // 암호화 키 설정
        String encryptionKey = args[0]; // 여기에 사용할 암호화 키를 입력하세요.
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPassword(encryptionKey); // 암호화 키 설정

        String encryptedText = textEncryptor.encrypt("jdbc:postgresql://localhost:5432/helloworlddb?currentSchema=public&useSSL=false");
        System.out.println("url: " + encryptedText);

        encryptedText = textEncryptor.encrypt("hello");
        System.out.println("username: " + encryptedText);

        encryptedText = textEncryptor.encrypt("world");
        System.out.println("password: " + encryptedText);

        // 복호화 예제 (테스트용)
/*        String decryptedText = textEncryptor.decrypt(encryptedText);
        System.out.println("Decrypted Text: " + decryptedText);*/
    }
}


