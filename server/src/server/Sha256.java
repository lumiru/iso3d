/**
 * 
 */
package server;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author lumiru
 *
 */
public class Sha256 {

	public static String hash(String str) {
        MessageDigest md;
        byte[] byteData;
        StringBuffer sb = new StringBuffer();
        
		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
        md.update(str.getBytes());
 
        byteData = md.digest();
 
        // Convertir les octets en chaîne hexadécimale
        for (int i = 0; i < byteData.length; i++) {
        	sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
    	
    	return sb.toString();
	}

}
