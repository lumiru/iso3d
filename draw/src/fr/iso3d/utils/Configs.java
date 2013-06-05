/**
 * 
 */
package fr.iso3d.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
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
@XmlRootElement(name="configurations")
public class Configs {
	private static Configs instance = fromXML(loadXML());
	private final static String filename = "conf.xml";
	
	private List<Config> confs;

	@XmlElement(name="item")
	public List<Config> getConfs() {
		return confs;
	}

	public void setConfs(List<Config> confs) {
		this.confs = confs;
	}

	/**
	 * 
	 */
	public Configs() {
		// TODO Stub du constructeur généré automatiquement
	}
	
	public String get(String key) {
		for (int i = 0, l = confs.size(); i < l; ++i) {
			if(confs.get(i).getName().equals(key)) {
				return confs.get(i).getValue();
			}
		}
		
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
			System.err.println("Le fichier « "+filename+" » n'a pas été trouvé.");
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
	
	private static Configs fromXML(String xml) {
		JAXBContext jc;
		Unmarshaller u;
		Object retour;
		
		try {
			jc = JAXBContext.newInstance(Configs.class);
			u = jc.createUnmarshaller();
			retour = u.unmarshal(new StringReader(xml));
			
			if(retour instanceof Configs) {
				return (Configs) retour;
			}
			else {
				System.err.println("Impossible d'ouvrir les configurations correctement : \n"+xml);
			}
		} catch (JAXBException e) {
			e.printStackTrace();
			
			System.err.println(xml);
		}
		
		return null;
	}

	public static Configs getInstance() {
		return instance;
	}
}
