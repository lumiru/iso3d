/**
 * 
 */
package fr.iso3d.collision;

/**
 * @author lumiru
 *
 */
public class PolygoneConvexe extends Point implements Collisionnable {
	protected Point[] sommets;
	
	public Point[] getSommets() {
		return sommets;
	}
	
	protected void setSommets(Point[] sommets) {
		int l = sommets.length;

		this.sommets = sommets;
		
		for (int i = 0; i < l; i++) {
			Point p = sommets[i];
			x += p.x;
			y += p.y;
		}
		
		x /= l;
		y /= l;
	}
	
	/**
	 * Construit un polygone
	 * @param p
	 */
	public PolygoneConvexe(Point[] sommets) {
		super();
		
		setSommets(sommets);
	}
	
	/**
	 * Constructeur par défaut
	 */
	protected PolygoneConvexe() {
		super();
		sommets = null;
	}
	
	/**
	 * Calcule s'il y a collision
	 * @param p Le point pour lequel on doit vérifier s'il est dans la forme.
	 * @return Vrai si le point est à l'intérieur du polygone.
	 */
	public boolean collision(Point p) {
		Point a, b;
		Vecteur d, v;
		int l = sommets.length;
		
		for (int i = 0; i < l; i++) {
			a = sommets[i];
			if(i == l - 1) {
				b = sommets[0];
			}
			else {
				b = sommets[i+1]; 
			}
			
			d = new Vecteur(b.getX() - a.getX(), b.getY() - a.getY());
			v = new Vecteur(p.getX() - a.getX(), p.getY() - a.getY());
			
			if(d.det(v) < 0) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * TODO non implémentée
	 */
	@Override
	public boolean equals(Object obj) {
		return false;
	}
}
