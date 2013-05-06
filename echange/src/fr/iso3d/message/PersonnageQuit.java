/**
 * 
 */
package fr.iso3d.message;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author lumiru
 *
 */

@XmlRootElement(name="perso-quit")
public class PersonnageQuit extends Message {

	private int id;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public PersonnageQuit() {
		this(0);
	}
	public PersonnageQuit(int id) {
		super();
		
		this.id = id;
	}

}
