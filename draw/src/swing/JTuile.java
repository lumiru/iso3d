/**
 * 
 */
package swing;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import astarcarre.GraphicalPosition;


import collision.Point;
import collision.Tuile;

/**
 * Image de tuile SWING
 * @author lumiru
 *
 */
public class JTuile extends JCanvas implements ZLigne {
	private static final long serialVersionUID = -5291250818349162439L;
	
	private Tuile tuile;
	
	public Tuile getTuile() {
		return tuile;
	}

	public JTuile(Tuile t) {
		super();
		
		tuile = t;
		
		setXPos(t.getX()-28);
		setYPos(t.getY()-14);
		setDimensions(Interface.dimensions);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		Point[] pts = tuile.getSommets();
		int l = pts.length;
		int[] xpoints = new int[l];
		int[] ypoints = new int[l];
		
		for (int i = 0; i < l; i++) {
			xpoints[i] = pts[i].getX();
			ypoints[i] = pts[i].getY();
		}
		
		g2.setPaint(new Color(0, 81, 0));
		g2.fillPolygon(xpoints, ypoints, l);
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof JTuile) {
			JTuile t = (JTuile) obj;
			return tuile.equals(t.tuile);
		}
		else {
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see utils.ZLigne#getLigne()
	 */
	@Override
	public int getLigne() {
		return GraphicalPosition.fromNumero(tuile.getN()).y;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(ZLigne o) {
		return o.getLigne() - getLigne();
	}
}
