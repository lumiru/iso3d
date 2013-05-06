/**
 * 
 */
package fr.iso3d.collision;

/**
 * @author lumiru
 *
 */
public class Parallelogramme extends PolygoneConvexe {
	
	/**
	 * Construit un parallèlogramme
	 * @param centre Point central
	 * @param sommet Un sommet de l'une des diagonales
	 * @param sommet2 Un sommet de l'autre diagonale
	 */
	public Parallelogramme(Point centre, Point sommet, Point sommet2) {
		super();

		Point[] sommets = new Point[4];
		sommets[0] = sommet;
		sommets[2] = centre.somme(centre.difference(sommet));
		sommets[1] = sommet2;
		sommets[3] = centre.somme(centre.difference(sommet2));
		
		x = centre.x;
		y = centre.y;
		this.sommets = sommets;
	}

}
