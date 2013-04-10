/**
 * 
 */
package astarcarre;

/**
 * @author lumiru
 *
 */
public abstract class Position {
	public int x;
	public int y;

	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Vérifie que la position testée est dans les limites fixées.
	 * @return
	 */
	abstract public boolean exists();
	
	/**
	 * Obtient le numéro de la case correspondante.
	 * @return
	 */
	abstract public int toNumero();
	
	@Override
	public String toString() {
		return "["+x+","+y+"]";
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	abstract public boolean equals(Object obj);
}
