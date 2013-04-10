/**
 * 
 */
package astarcarre;

import java.util.HashMap;
import java.util.Map;

import message.Direction;

/**
 * @author lumiru
 *
 */
public class OrthoPosition extends Position {
	private static final int xlimit = 25;
	private static final int ylimit = 37; 

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
		return x >= 0 && y >= 0 && x < xlimit && y < ylimit;
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

	/**
	 * Obtient les cases adjacentes à la case courante.
	 * @return
	 */
	public Map<Direction, OrthoPosition> neighboursRev() {
		Map<Direction, OrthoPosition> map = new HashMap<Direction, OrthoPosition>();

		if(x > 0 && y > 0) {
			map.put(Direction.SUD_EST, new OrthoPosition(x-1, y-1));
		}
		if(x+1 < xlimit && y > 0) {
			map.put(Direction.SUD_OUEST, new OrthoPosition(x+1, y-1));
		}
		if(x > 0 && y+1 < ylimit) {
			map.put(Direction.NORD_EST, new OrthoPosition(x-1, y+1));
		}
		if(x+1 < xlimit && y+1 < ylimit) {
			map.put(Direction.NORD_OUEST, new OrthoPosition(x+1, y+1));
		}
		
		return map;
	}
}
