/**
 * 
 */
package message;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author lumiru
 *
 */
@XmlRootElement(name="connexion")
public class Connexion extends Requete {
	private String login;
	private String pass;

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public Connexion() {
		login = null;
		pass = null;
	}
	
	public Connexion(String login, String pass) {
		super();
		this.login = login;
		this.pass = pass;
	}

}
