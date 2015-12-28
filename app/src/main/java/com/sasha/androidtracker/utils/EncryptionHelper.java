package com.sasha.androidtracker.utils;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Cipher;

/**
 * Created by thijs on 21-12-15.
 */
public class EncryptionHelper {

    private Context context;

    // constructor takes context parameter so we can call getResources
    public EncryptionHelper(Context context){
        this.context = context;
    }

    /**
     * String to hold name of the encryption algorithm.
     */
    public static final String ALGORITHM = "RSA";

    /**
     * String to hold the name of the private key file.
     */
    public static final String PRIVATE_KEY_FILE = "/Users/thijs/private.key";

    /**
     * String to hold name of the enc key file.
     */
    public static final String PUBLIC_KEY_FILE = "res/key/public.key";

    /**
     * Generate key which contains a pair of private and public key using 1024
     * bytes. Store the set of keys in Private.key and Public.key files.
     *
     * @throws NoSuchAlgorithmException
     * @throws IOException
     * @throws FileNotFoundException
     */
    public static void generateKey() {
        try {

            // set algorithm to be used for key
            final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
            // set encryption strength
            keyGen.initialize(1024);
            // generate keypair
            final KeyPair key = keyGen.generateKeyPair();

            File privateKeyFile = new File(PRIVATE_KEY_FILE);
            File publicKeyFile = new File(PUBLIC_KEY_FILE);

            // Create files to store enc and private key
            if (privateKeyFile.getParentFile() != null) {
                privateKeyFile.getParentFile().mkdirs();
            }
            privateKeyFile.createNewFile();

            if (publicKeyFile.getParentFile() != null) {
                publicKeyFile.getParentFile().mkdirs();
            }
            publicKeyFile.createNewFile();

            // save the Public key to a file
            ObjectOutputStream publicKeyOS = new ObjectOutputStream(
                    new FileOutputStream(publicKeyFile));
            publicKeyOS.writeObject(key.getPublic());
            publicKeyOS.close();

            // save the Private key to a file
            ObjectOutputStream privateKeyOS = new ObjectOutputStream(
                    new FileOutputStream(privateKeyFile));
            privateKeyOS.writeObject(key.getPrivate());
            privateKeyOS.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * The method checks if the pair of enc and private key has been generated.
     *
     * @return flag indicating if the pair of keys were generated.
     */
    public static boolean areKeysPresent() {

        File privateKey = new File(PRIVATE_KEY_FILE);
        File publicKey = new File(PUBLIC_KEY_FILE);

        if (privateKey.exists() && publicKey.exists()) {
            return true;
        }
        return false;
    }

    /**
     * Encrypt the plain text using enc key.
     *
     * @param text
     *          : original plain text
     * @param key
     *          :The enc key
     * @return Encrypted text
     * @throws java.lang.Exception
     */
    public static byte[] encrypt(String text, PublicKey key) {
        byte[] cipherText = null;
        try {
            // get an RSA cipher object and print the provider
            final Cipher cipher = Cipher.getInstance(ALGORITHM+"/ECB/PKCS1Padding");
            // encrypt the plain text using the enc key
            cipher.init(Cipher.ENCRYPT_MODE, key);
            cipherText = cipher.doFinal(text.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cipherText;
    }

    /**
     * Decrypt text using private key.
     *
     * @param text
     *          :encrypted text
     * @param key
     *          :The private key
     * @return plain text
     * @throws java.lang.Exception
     */
    public static String decrypt(byte[] text, PrivateKey key) {
        byte[] decryptedText = null;
        try {
            // get an RSA cipher object and print the provider
            final Cipher cipher = Cipher.getInstance(ALGORITHM+"/ECB/PKCS1Padding");

            // decrypt the text using the private key
            cipher.init(Cipher.DECRYPT_MODE, key);
            decryptedText = cipher.doFinal(text);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return new String(decryptedText);
    }

    public byte[] testEncrypt(String input){
        // Check if the pair of keys are present else generate those.
        //if (!this.areKeysPresent()) {
        // Method generates a pair of keys using the RSA algorithm and stores it
        // in their respective files
        //    this.generateKey();
        //}

        // get enc key from resources as InputStream

        InputStream ins = this.context.getResources().openRawResource(
                this.context.getResources().getIdentifier("raw/enc",
                        "raw", this.context.getPackageName()));



        ObjectInputStream inputStream = null;
        byte[] cipherText = null;
        try {
            //inputStream = new ObjectInputStream(new FileInputStream(PUBLIC_KEY_FILE));
            InputStream iss = new BufferedInputStream(ins);
            inputStream = new ObjectInputStream(iss);
            final PublicKey publicKey = (PublicKey) inputStream.readObject();
            cipherText = encrypt(input, publicKey);
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(EncryptionHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cipherText;
    }

    public String testDecrypt(byte[] cipherText){
        ObjectInputStream inputStream = null;

        try {
            inputStream = new ObjectInputStream(new FileInputStream(PRIVATE_KEY_FILE));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(EncryptionHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(EncryptionHelper.class.getName()).log(Level.SEVERE, null, ex);
        }

        PrivateKey privateKey = null;
        try {
            privateKey = (PrivateKey) inputStream.readObject();
        } catch (IOException ex) {
            Logger.getLogger(EncryptionHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(EncryptionHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        String plainText = decrypt(cipherText, privateKey);
        return plainText;
    }

}

