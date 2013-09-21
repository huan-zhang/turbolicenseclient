package com.turbo_license.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.AllPermission;
import java.security.KeyFactory;
import java.security.Permissions;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public class Decrypter {
	static public String decrypt(String publicKeyFile, String encryptedLicenseFile) {
		String result = "";
		System.out.println("in decrypt " + publicKeyFile);
    	try {
    		byte[] pubBytes = readFileToBytes(publicKeyFile);
    		byte[] encryptedLicenseBytes = readFileToBytes(encryptedLicenseFile);
    		byte[] b = Base64.base64Encode(encryptedLicenseBytes);
	        //cipher.init(Cipher.DECRYPT_MODE, privateKey);
	        PublicKey DPublicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(pubBytes));

		    Cipher cipher = Cipher.getInstance("RSA");
	        cipher.init(Cipher.DECRYPT_MODE, DPublicKey);
	        try {
	            byte[] decryptedData = cipher.doFinal(Base64.base64Decode(b));
	            result = new String(decryptedData);
	        } catch (Exception e1) {
	            System.err.println("Decrypt error: " + e1.getMessage() );
	        }
   	 	} catch (Exception e) {
   	 		result = "failed to verify license: ";
   	 		System.err.println("failed to verify license: " + e.getMessage());
   	 	}
    	return result;
		
	}
	
	static private byte[] readFileToBytes(String filename) {
		File file = new File(filename);
		try {
		    FileInputStream fin = new FileInputStream(file);
		    byte fileContent[] = new byte[(int)file.length()];
		    fin.read(fileContent);
		    String strFileContent = new String(fileContent); 
		    System.out.println("File content : ");
		    System.out.println(strFileContent);
		    return fileContent;
	    } catch(FileNotFoundException e) {
	    	System.out.println("File not found" + e);
	    } catch(IOException ioe) {
	    	System.out.println("Exception while reading the file " + ioe);
	    }
	    return null;
	}
}
