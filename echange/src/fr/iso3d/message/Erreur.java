/**
 * 
 */
package fr.iso3d.message;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author lumiru
 *
 */
@XmlRootElement(name="erreur")
public class Erreur extends Reponse {
	private static String[] codeStr = {
		"Indéfini",
		"Connexion échouée.",
		"Inscription échouée, cet identifiant est déjà utilisé.",
		"Vous êtes déjà connecté.",
		"La cible choisie est indisponible."
	};
	
	private int code;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
	public String getErreurMessage() {
		return codeStr[code];
	}

	public Erreur() {
		super();
		this.code = 0;
	}

	public Erreur(int code) {
		super();
		this.code = code;
	}
}
