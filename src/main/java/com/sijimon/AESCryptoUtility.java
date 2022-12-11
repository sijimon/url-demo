package com.sijimon;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;

@Service
public class AESCryptoUtility {


        private static final String ALGORITHM = "AES/GCM/NoPadding"; //"AES/GCM/NoPadding" AES/GCM/PKCS5Padding
        private static final String PROTOCOL = "PBKDF2WithHmacSHA256";
        private static final int TAG_LENGTH_BIT = 128;
        private static final int IV_LENGTH_BYTE = 12;
        private static final int SALT_LENGTH_BYTE = 16;
        private static final String ALGORITHM_TYPE = "AES";
        private static final int KEY_LENGTH = 256;
        private static final int ITERATION_COUNT = 65536;
        private static final Charset UTF_8 = StandardCharsets.UTF_8;



        public String encrypt(String password, String plainMessage) throws Exception {
           
        	//16 byte salt
        	byte[] salt = getRandomNonce(SALT_LENGTH_BYTE);
            
        	// GCM recommends 12 bytes iv
            byte[] iv = getRandomNonce(IV_LENGTH_BYTE);
            
            SecretKey secretKey = getSecretKey(password, salt);

            
          //  Cipher cipher = initCipher(Cipher.ENCRYPT_MODE, secretKey, iv);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            
            cipher.init(Cipher.ENCRYPT_MODE, secretKey,new GCMParameterSpec(TAG_LENGTH_BIT, iv));
            
            byte[] encryptedMessageByte = cipher.doFinal(plainMessage.getBytes(UTF_8));

            // prefix IV and Salt to cipher text
            byte[] cipherByte = ByteBuffer.allocate(iv.length + salt.length + encryptedMessageByte.length)
                    .put(iv)
                    .put(salt)
                    .put(encryptedMessageByte)
                    .array();
            
            return Base64.getEncoder().encodeToString(cipherByte);
        }

        public String decrypt(String password, String cipherMessage) throws Exception {
        	
            byte[] decodedCipherByte = Base64.getDecoder().decode(cipherMessage.getBytes(UTF_8));
            
            //get back the iv and salt from the cipher text
            ByteBuffer byteBuffer = ByteBuffer.wrap(decodedCipherByte);

            byte[] iv = new byte[IV_LENGTH_BYTE];
            byteBuffer.get(iv);

            byte[] salt = new byte[SALT_LENGTH_BYTE];
            byteBuffer.get(salt);

            byte[] encryptedByte = new byte[byteBuffer.remaining()];
            byteBuffer.get(encryptedByte);

            SecretKey secretKey = getSecretKey(password, salt);
            
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            

            cipher.init(Cipher.DECRYPT_MODE, secretKey,new GCMParameterSpec(TAG_LENGTH_BIT, iv));
            
            //Cipher cipher = initCipher(Cipher.DECRYPT_MODE, secretKey, iv);

            byte[] decryptedMessageByte = cipher.doFinal(encryptedByte);
            
            return new String(decryptedMessageByte, UTF_8);
        }
        
        public byte[] getRandomNonce(int length) {
            byte[] nonce = new byte[length];
            new SecureRandom().nextBytes(nonce);
            return nonce;
        }

        public SecretKey getSecretKey(String password, byte[] salt)
                throws NoSuchAlgorithmException, InvalidKeySpecException {
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH);

            SecretKeyFactory factory = SecretKeyFactory.getInstance(PROTOCOL);
            return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), ALGORITHM_TYPE);
        }
        
/*
        
        private  Cipher initCipher(int mode, SecretKey secretKey, byte[] iv) throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchPaddingException, NoSuchAlgorithmException {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(mode, secretKey, new GCMParameterSpec(TAG_LENGTH_BIT, iv));
            return cipher;
        }
        
  */
        
}