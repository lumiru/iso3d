/**
 * 
 */
package fr.iso3d.message;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * @author lumiru
 *
 */

@XmlRegistry
public class ObjectFactory {
	public ObjectFactory() {
	}
	
	public Erreur createErreur() {
		return new Erreur();
	}
	
	public Connecte createConnecte() {
		return new Connecte();
	}
	
	public Connexion createConnexionRequete() {
		return new Connexion();
	}
	
	public Inscription createInscription() {
		return new Inscription();
	}
	
	public Personnage createPersonnage() {
		return new Personnage();
	}
	
	public PersonnageQuit createPersonnageQuit() {
		return new PersonnageQuit();
	}
	
	public Deplacement createDeplacement() {
		return new Deplacement();
	}
	
	public DeplacementFin createDeplacementFin() {
		return new DeplacementFin();
	}
	
	public AnnulerDeplacement createAnnulerDeplacement() {
		return new AnnulerDeplacement();
	}
	
	public ChangerMap createChangerMap() {
		return new ChangerMap();
	}
	
	public Parler createParler() {
		return new Parler();
	}

}
