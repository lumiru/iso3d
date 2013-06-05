/**
 * 
 */
package fr.iso3d.utils;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

/**
 * @author lumiru
 *
 */
public class Config {
	private String name;
	private String value;

	/**
	 * @return le name
	 */
	@XmlAttribute
	public String getName() {
		return name;
	}

	/**
	 * @param name le name à définir
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return le value
	 */
	@XmlValue
	public String getValue() {
		return value;
	}

	/**
	 * @param value le value à définir
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * 
	 */
	public Config() {
		// TODO Stub du constructeur généré automatiquement
	}

}
