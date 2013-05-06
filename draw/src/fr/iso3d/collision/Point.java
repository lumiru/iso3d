/**
 * Gestion des collisions
 */
package fr.iso3d.collision;

/**
 * Représente un point
 * @author lumiru
 *
 */
public class Point {
	protected int x;
	protected int y;
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Point(Point p) {
		this(p.x,p.y);
	}
	
	public Point() {
		this(0,0);
	}

	public Point somme(Point p) {
		return new Point(x + p.x, y + p.y);
	}
	
	public Point difference(Point p) {
		return new Point(x - p.x, y - p.y);
	}
	
	public float distance(Point p) {
		Point a = difference(p);
		return (float) Math.sqrt( (a.x) * (a.x) + (a.y) * (a.y) );
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Point) {
			Point p = (Point) obj;
			return x == p.x && y == p.y;
		}
		else {
			return false;
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "["+x+","+y+"]";
	}
}
