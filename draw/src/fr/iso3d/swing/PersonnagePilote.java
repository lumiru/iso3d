/**
 * 
 */
package fr.iso3d.swing;

import javax.swing.JOptionPane;

import fr.iso3d.message.Deplacement;
import fr.iso3d.message.DeplacementFin;
import fr.iso3d.message.Direction;
import fr.iso3d.socket.Serveur;


/**
 * Représente le personnage piloté.
 * Permet de définir des actions à envoyer au serveur depuis le personnage.
 * @author lumiru
 *
 */
public class PersonnagePilote extends Personnage {
	private static final long serialVersionUID = 881310664606331666L;

	/**
	 * @param plateau
	 * @param id
	 * @param pseudo
	 */
	public PersonnagePilote(Plateau plateau, int id, String pseudo) {
		super(plateau, id, pseudo);
		// TODO Stub du constructeur généré automatiquement
	}

	/**
	 * Uniquement pour le personnage dirigé (envoie des informations au serveur).
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean deplacer(final int x, final int y) {
		Serveur srv = Application.getCurrent().getServeur();
		srv.envoyer(new DeplacementFin(getPlateau().getTuileNumero(getPosX(), getPosY())));
		
		if(calculDeplacement(x, y)) {
			if(srv.envoyer(new Deplacement(
					new fr.iso3d.message.Personnage(0, "", getPlateau().getTuileNumero(getPosX(), getPosY())),
					getPile().toArray(new Direction[0]), getPlateau().getTuileNumero(x, y)))) {
				// executerDeplacement(x, y);
				
				return true;
			}
			else {
				JOptionPane.showMessageDialog(null, "Une erreur s'est produite pendant l'envoie des informations de déplacement au serveur et a empêcher l'opération.",
						"Erreur", JOptionPane.ERROR_MESSAGE);
			}
		}

		return false;
	}
	
	/* (non-Javadoc)
	 * @see swing.Personnage#deplacementOK()
	 */
	@Override
	protected void deplacementOK() {
		super.deplacementOK();
		
		Application.getCurrent().getServeur().envoyer(new DeplacementFin(getPlateau().getTuileNumero(getPosX(), getPosY())));
	}
	
}
