package com.naddiaz.tfg.bletasker.utils;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.widget.BaseAdapter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import static javax.crypto.Cipher.*;

/**
 * Created by nad on 20/05/15.
 */
public class RSACrypt {

    private static String PUBLIC_KEY = "PUBLIC_KEY";
    private static final String PUBLIC_KEY_HEADER = "-----BEGIN PUBLIC KEY-----";
    private static final String PUBLIC_KEY_FOOTER = "-----END PUBLIC KEY-----";
    private static String PRIVATE_KEY = "PRIVATE_KEY";
    private static final String PRIVATE_KEY_HEADER = "-----BEGIN PRIVATE KEY-----";
    private static final String PRIVATE_KEY_FOOTER = "-----END PRIVATE KEY-----";

    private int id_airport;
    private String id_person;

    public RSACrypt(int id_airport, String id_person){
        this.id_airport = id_airport;
        this.id_person = id_person;
    }

    public String crypt(String plain){

        Cipher pkCipher = null;
        try {
            pkCipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
        try {
            pkCipher.init(Cipher.ENCRYPT_MODE, getPublicKey());
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        byte[] encryptedInByte = new byte[0];
        try {
            encryptedInByte = pkCipher.doFinal(plain.getBytes("UTF-8"));
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return Base64.encodeToString(encryptedInByte,Base64.DEFAULT);
    }

    public String decrypt(String encode){
        Cipher pkCipher = null;
        try {
            pkCipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
        try {
            pkCipher.init(Cipher.DECRYPT_MODE, getPrivateKey());
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        byte[] encryptedInByte = null;
        try {
            encryptedInByte = pkCipher.doFinal(Base64.decode(encode,Base64.DEFAULT));
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        try {
            return new String(encryptedInByte,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private PrivateKey getPrivateKey(){
        PrivateKey privateKey = null;
        try {
            privateKey = PrivateKeyReader.get(Environment.getExternalStorageDirectory().getAbsolutePath() + "/ssl/server.private.der");
            //privateKey = PrivateKeyReader.get(Environment.getExternalStorageDirectory().getAbsolutePath() + "/ssl/a" + String.valueOf(id_airport) + "e" + id_person + ".private.der");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return privateKey;
    }

    private PublicKey getPublicKey(){
        PublicKey publicKey = null;
        try {
            publicKey = PublicKeyReader.get(Environment.getExternalStorageDirectory().getAbsolutePath() + "/ssl/server.public.der");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return publicKey;
    }

}
