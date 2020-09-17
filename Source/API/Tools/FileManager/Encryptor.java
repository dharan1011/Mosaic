package API.Tools.FileManager;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.AlgorithmParameters;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.io.*;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import API.SHA256;

final class Encryptor {
    private String nm, Pass, pw, Key, cpw, cKey;
    private String Username = "";
	
    private String newDir = "";
    Console console = System.console();
	API.SHA256 sha=new API.SHA256();
	
    protected void encr(String U, String dir) throws Exception {
        Username = U;
		newDir=dir;
        welcome();
    }
    private void welcome() throws Exception {

        System.out.println("DISCLAIMER: FILE CANNOT BE RECOVERED IF THE CREDENTIALS ARE LOST.\n");
        System.out.print("Enter the name of the file to be encrypted (with extension): ");
        nm = console.readLine();

        File f = new File(newDir);
        if (f.exists() == false) {
            System.out.println("The file cannot be found. Please try again.");
            console.readLine();
            return;
        } else {
            System.out.println("Enter the password and key to encrypt the file.");
            pw   = sha.encodedString(String.valueOf(console.readPassword("File Password    : ")));
            cpw  = sha.encodedString(String.valueOf(console.readPassword("Confirm Password : ")));
            Key  = sha.encodedString(String.valueOf(console.readPassword("Security Key     : ")));
            cKey = sha.encodedString(String.valueOf(console.readPassword("Confirm Key      : ")));
            if (cpw.equals(pw) & cKey.equals(Key)) {
                Pass = pw + Key;
                Encr();
            } else {
                System.out.println("Credentials do not match.");
                console.readLine();
            }
        }
    }
    void Encr() throws Exception {

        //File to be encrypted
        FileInputStream inFile = new FileInputStream(newDir+nm);
        //Encrypted Output of the fractal file
        FileOutputStream outFile = new FileOutputStream(newDir + nm + ".LOCK");
        /* Description:
         * Password, iv and salt should be transferred to the other end securely
         * salt is for encoding, written to file and must be transferred to the recipient securely for decryption
         */
        byte[] salt = new byte[8];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(salt);
        FileOutputStream saltOutFile = new FileOutputStream(newDir + nm +".salt");
        saltOutFile.write(salt);
        saltOutFile.close();
        SecretKeyFactory factory = SecretKeyFactory
            .getInstance("PBKDF2WithHmacSHA1");
        KeySpec keySpec = new PBEKeySpec(Pass.toCharArray(), salt, 65536,
            256);
        SecretKey secretKey = factory.generateSecret(keySpec);
        SecretKey secret = new SecretKeySpec(secretKey.getEncoded(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        AlgorithmParameters params = cipher.getParameters();
        /*Description
         * iv adds randomness to the text and just makes the mechanism more secure
         * used while initializing the cipher
         */
        FileOutputStream ivOutFile = new FileOutputStream(newDir + nm + ".iv");
        byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
        ivOutFile.write(iv);
        ivOutFile.close();

        //file encryption
        byte[] input = new byte[64];
        int bytesRead;

        while ((bytesRead = inFile.read(input)) != -1) {
            byte[] output = cipher.update(input, 0, bytesRead);
            if (output != null)
                outFile.write(output);
        }

        byte[] output = cipher.doFinal();
        if (output != null)
            outFile.write(output);
        inFile.close();
        outFile.flush();
        outFile.close();
        File del = new File(newDir + nm);
        del.delete();
		System.gc();
        System.out.println("File has been successfully encrypted in the current directory.");
    }
}