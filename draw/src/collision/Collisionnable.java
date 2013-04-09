/**
 * 
 */
package collision;

/**
 * Interface pour les collisions
 * @author lumiru
 *
 */
public interface Collisionnable {
	/**
	 * Calcule s'il y a collision
	 * @param p Le point pour lequel on doit vérifier s'il est dans la forme.
	 * @return Vrai si le point est à l'intérieur de la forme.
	 */
	public boolean collision(Point p);
}
