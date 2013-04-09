/**
 * 
 */
package mapsData;

/**
 * @author lumiru
 *
 */
public class Convert {
	private static char[] chars = {
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
		'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
		'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
		'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D'
	};

	public static byte charToByte(char c) {
		for (byte i = 0, l = (byte) chars.length; i < l; i++) {
			if(chars[i] == c) {
				return i;
			}
		}
		
		return -1;
	}

	public static char byteToChar(byte b) {
		if(b >= 0 && b < chars.length) {
			return chars[b];
		}
		
		return 0;
	}
	
}
