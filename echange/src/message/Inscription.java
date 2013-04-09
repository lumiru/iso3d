/**
 * 
 */
package message;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author lumiru
 *
 */
@XmlRootElement(name="inscription")
public class Inscription extends Connexion {

	/**
	 * 
	 */
	public Inscription() {
		// TODO Stub du constructeur généré automatiquement
	}

	/**
	 * @param login
	 * @param pass
	 */
	public Inscription(String login, String pass) {
		super(login, pass);
		// TODO Stub du constructeur généré automatiquement
	}

}
