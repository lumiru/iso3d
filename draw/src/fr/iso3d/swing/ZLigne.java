/**
 * 
 */
package fr.iso3d.swing;

/**
 * @author lumiru
 *
 */
public interface ZLigne extends Comparable<ZLigne> {
	/**
	 * Obtient la position sur l'axe Z (numéro de la couche graphique (Z)).
	 * @return
	 */
	public int getLigne();
}
