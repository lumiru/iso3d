/**
 * 
 */
package collision;


/**
 * @author lumiru
 *
 */
public class Tuile extends Parallelogramme {
	private int n;
	
	/**
	 * @return Numéro de la tuile
	 */
	public int getN() {
		return n;
	}

	/**
	 * Construit une tuile (losange debout 47x29).
	 * @param centre Centre de la tuile.
	 */
	public Tuile(Point centre, int n) {
		super(centre, new Point(centre.getX() - 28, centre.getY()), new Point(centre.getX(), centre.getY() - 14));
		
		this.n = n;
	}

	@Override
	public String toString() {
		return "Tuile no. "+n+" " + super.toString();
	}
	
	/**
	 * Test si les deux tuiles ont le même numéro.
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Tuile) {
			Tuile t = (Tuile) obj;
			return n == t.n;
		}
		else {
			return false;
		}
	}
}
