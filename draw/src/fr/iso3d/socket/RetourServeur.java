/**
 * 
 */
package fr.iso3d.socket;

import fr.iso3d.message.Message;

/**
 * @author lumiru
 *
 */
public interface RetourServeur {
	
	/**
	 * Traitement graphique du retour serveur.
	 * @param msg Message de retour
	 */
	public void retour(Message msg);
	
}
