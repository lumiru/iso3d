/**
 * 
 */
package astarcarre;

import swing.Plateau;

/**
 * @author lumiru
 *
 */
public class GraphicalPosition extends Position {

	/**
	 * @param i
	 */
	public GraphicalPosition(int i) {
		super(i % 25, i / 25);
		
		// 0..12
		y = y << 1;
		
		// 13..24
		if(x >= 13) {
			x -= 13;
			y += 1;
		}
	}
	
	/**
	 * @param x
	 * @param y
	 */
	public GraphicalPosition(int x, int y) {
		super(x, y);
		// TODO Stub du constructeur généré automatiquement
	}

	public OrthoPosition toOrtho() {
		return new OrthoPosition(x * 2 + y % 2, y);
	}

	/* (non-Javadoc)
	 * @see astarcarre.Position#exists()
	 */
	@Override
	public boolean exists() {
		return toOrtho().exists();
	}

	/* (non-Javadoc)
	 * @see astarcarre.Position#toNumero()
	 */
	@Override
	public int toNumero() {
		int n = x + y * 12 + (y + 1) / 2;
		return n >= 0 && x >= 0 && y >= 0 && n < Plateau.nbTuiles && (x < 12 || (x < 13 && y % 2 == 0)) ? n : -1;
	}

	public static GraphicalPosition fromNumero(int i) {
		return new GraphicalPosition(i);
	}

	/* (non-Javadoc)
	 * @see astarcarre.Position#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Position))
			return false;
		Position other = (Position) obj;
		if(obj instanceof GraphicalPosition) {
			if (x != other.x)
				return false;
			if (y != other.y)
				return false;
			return true;
		}
		else if(obj instanceof OrthoPosition) {
			return equals(((OrthoPosition) obj).toGraphical());
		}
		else {
			return false;
		}
	}
}
