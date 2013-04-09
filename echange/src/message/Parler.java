/**
 * 
 */
package message;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author lumiru
 *
 */
@XmlRootElement(name="parler")
public class Parler extends Message {
	private int perso;
	private String message;
	
	/**
	 * Obtient l'identifiant du personnage
	 * @return
	 */
	public int getPerso() {
		return perso;
	}
	/**
	 * Définit l'identifiant du personnage
	 * @param perso
	 */
	public void setPerso(int perso) {
		this.perso = perso;
	}

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	public Parler() {
		this(0, "");
	}

	public Parler(String message) {
		this(0, message);
	}

	public Parler(int perso, String message) {
		super();
		
		this.perso = perso;
		this.message = message;
	}

}
