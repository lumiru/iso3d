/**
 * 
 */
package fr.iso3d.collision;

/**
 * @author lumiru
 *
 */
public class Vecteur extends Point {

	public Vecteur(int x, int y) {
		super(x, y);
	}

	/**
	 * Crée un vecteur à partir d'un autre
	 * @param p
	 */
	public Vecteur(Point p) {
		super(p);
	}

	public Vecteur() {
		super();
	}
	
	/**
	 * Calcule un déterminant
	 * @param v Le vecteur par rapport auquel faire le calcul
	 * @return Le déterminant
	 */
	public float det(Vecteur v) {
		return x * v.y - y * v.x;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Vecteur) {
			Vecteur p = (Vecteur) obj;
			return x == p.x && y == p.y;
		}
		else {
			return false;
		}
	}
}
