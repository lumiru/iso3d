/**
 * 
 */
package message;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author lumiru
 *
 */
@XmlRootElement(name="annuler-deplacement")
public class AnnulerDeplacement extends DeplacementFin {

	/**
	 * 
	 */
	public AnnulerDeplacement() {
		// TODO Stub du constructeur généré automatiquement
	}

	/**
	 * @param pos
	 */
	public AnnulerDeplacement(int pos) {
		super(pos);
		// TODO Stub du constructeur généré automatiquement
	}

}
