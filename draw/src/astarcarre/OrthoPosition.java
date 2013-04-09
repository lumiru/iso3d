/**
 * 
 */
package astarcarre;

/**
 * @author lumiru
 *
 */
public class OrthoPosition extends Position {

	/**
	 * @param x
	 * @param y
	 */
	public OrthoPosition(int x, int y) {
		super(x, y);
		// TODO Stub du constructeur généré automatiquement
	}
	
	public GraphicalPosition toGraphical() {
		return new GraphicalPosition(x/2, y);
	}
	
	public int distance(OrthoPosition pos) {
		int dx = pos.x - x;
		int dy = pos.y - y;

		if(dx < 0) {
			dx = -dx;
		}
		if(dy < 0) {
			dy = -dy;
		}
		
		return dx + dy;
	}
	
	public void cpy(OrthoPosition pos) {
		x = pos.x;
		y = pos.y;
	}

	/* (non-Javadoc)
	 * @see astarcarre.Position#exists()
	 */
	@Override
	public boolean exists()  {
		return x >= 0 && y >= 0 && x < 25 && y < 37;
	}

	/* (non-Javadoc)
	 * @see astarcarre.Position#toNumero()
	 */
	@Override
	public int toNumero() {
		return toGraphical().toNumero();
	}

	public static OrthoPosition fromNumero(int i) {
		return GraphicalPosition.fromNumero(i).toOrtho();
	}
}
