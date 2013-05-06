/**
 * 
 */
package fr.iso3d.message;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author lumiru
 *
 */
@XmlRootElement(name="message")
abstract public class Reponse extends Message {
	
	public Reponse() {
		super();
	}

}
