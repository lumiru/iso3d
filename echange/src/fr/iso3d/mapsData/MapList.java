/**
 * 
 */
package fr.iso3d.mapsData;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author lumiru
 *
 */
@XmlRootElement(name="maps")
public class MapList {
	final private static String filename = "maps.xml";
	
	private List<Map> items;

	@XmlElement(name="item")
	public List<Map> getItems() {
		return items;
	}
	public void setItems(List<Map> items) {
		this.items = items;
	}
	
	public MapList() {
		this(new ArrayList<Map>());
	}
	public MapList(List<Map> items) {
		this.items = items;
	}
	
	public int size() {
		return items.size();
	}
	
	public Map get(int k) {
		for (Map map : items) {
			if(map.getId() == k) {
				return map;
			}
		}
		//return items.get(k);
		return null;
	}
	
	private static String loadXML() {
		String line;
		String xml = null;
		BufferedReader bfr = null;
		
		try {
			bfr = new BufferedReader(new FileReader(filename));
			
			xml = "";
			while((line = bfr.readLine()) != null) {
				xml += line;
			}
			
			bfr.close();
		} catch (FileNotFoundException e) {
			// TODO Bloc catch généré automatiquement
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Bloc catch généré automatiquement
			e.printStackTrace();
		} finally {
			if(bfr != null) {
				try {
					bfr.close();
				} catch (IOException e) {
					// TODO Bloc catch généré automatiquement
					e.printStackTrace();
				}
			}
		}
		
		return xml;
	}
	
	private static MapList fromXML(String xml) {
		JAXBContext jc;
		Unmarshaller u;
		Object retour;
		
		try {
			jc = JAXBContext.newInstance(MapList.class);
			u = jc.createUnmarshaller();
			retour = u.unmarshal(new StringReader(xml));
			
			if(retour instanceof MapList) {
				return (MapList) retour;
			}
			else {
				System.err.println("Impossible d'ouvrir la MapList correctement : \n"+xml);
			}
		} catch (JAXBException e) {
			// TODO Bloc catch généré automatiquement
			e.printStackTrace();
			
			System.err.println(xml);
		}
		
		return null;
	}
	
	public static MapList load() {
		return fromXML(loadXML());
	}
}
