/**
 * 
 */
package fr.iso3d.mapsData;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

/**
 * @author lumiru
 *
 */
public class Map {
	private int id;
	private char[] rawData;

	@XmlAttribute(name="id")
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	@XmlValue
	public String getRawData() {
		return new String(rawData);
	}
	public void setRawData(String rawData) {
		this.rawData = rawData.toCharArray();
	}
	
	public boolean isActivee(int i) {
		return rawData[i] != '0';
	}
	public boolean[] getActivees() {
		int l = rawData.length;
		boolean[] all = new boolean[l];
		
		for (int i = 0; i < l; i++) {
			all[i] = isActivee(i);
		}
		
		return all;
	}
	public byte get(int i) {
		return Convert.charToByte(rawData[i]);
	}
	public byte[] getAll() {
		int l = rawData.length;
		byte[] all = new byte[l];
		
		for (int i = 0; i < l; i++) {
			all[i] = get(i);
		}
		
		return all;
	}
	/**
	 * Obtient le nombre de cases enregistrées.
	 */
	public int size() {
		return rawData.length;
	}

	public Map() {
		id = 0;
		rawData = null;
	}

	/**
	 * Obtient la position de la première case du type indiqué.
	 * @return -1 si le tableau n'en comporte pas.
	 */
	public int getFirstTypePos(byte type) {
		char c = Convert.byteToChar(type); 
		
		for (int i = 0; i < rawData.length; i++) {
			if(rawData[i] == c) {
				return i;
			}
		}
		
		return -1;
	}
}
