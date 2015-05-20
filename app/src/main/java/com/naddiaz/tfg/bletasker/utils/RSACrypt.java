package com.naddiaz.tfg.bletasker.utils;

import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
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

    public String RSAEncrypt(final String plain) throws NoSuchAlgorithmException, InvalidKeySpecException,
            NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(Base64.decode(getStringFromFile(PUBLIC_KEY),Base64.DEFAULT));
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PublicKey pkPublic = kf.generatePublic(publicKeySpec);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pkPublic);
        byte[] encryptedBytes = cipher.doFinal(plain.getBytes());
        System.out.println("EEncrypted????? : " + Base64.encodeToString(encryptedBytes,Base64.DEFAULT));
        return Base64.encodeToString(encryptedBytes,Base64.DEFAULT);
    }

    public String RSADecrypt(final String crypt) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Log.i("RSA",Base64.decode(getStringFromFile(PRIVATE_KEY),Base64.DEFAULT).toString());
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(Base64.decode(getStringFromFile(PRIVATE_KEY),Base64.DEFAULT));
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PrivateKey pkPrivate = kf.generatePrivate(privateKeySpec);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, pkPrivate);
        byte[] decryptedBytes = cipher.doFinal(Base64.decode(crypt,Base64.DEFAULT));
        System.out.println("DDecrypted????? : " + new String(decryptedBytes));
        return new String(decryptedBytes);
    }

    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                switch (line.trim()){
                    case PUBLIC_KEY_HEADER:
                        break;
                    case PUBLIC_KEY_FOOTER:
                        break;
                    case PRIVATE_KEY_HEADER:
                        break;
                    case PRIVATE_KEY_FOOTER:
                        break;
                    default:
                        sb.append(line).append("\n");
                        break;
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public String getStringFromFile (String key_mode) {
        File storage = Environment.getExternalStorageDirectory();
        File file = null;
        if(key_mode.equals(PRIVATE_KEY)) {
            file = new File(storage, "/ssl/a" + String.valueOf(id_airport) + "e" + id_person + ".private.der");
        }
        else if(key_mode.equals(PUBLIC_KEY)){
            file = new File(storage, "/ssl/server.public.pem");
        }
        FileInputStream fin = null;
        String ret = null;
        try {
            fin = new FileInputStream(file);
            ret = convertStreamToString(fin);
            fin.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }
}
