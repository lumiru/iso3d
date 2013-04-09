package swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

public abstract class JCanvas extends JPanel {
	private static final long serialVersionUID = -9103911430599172234L;
	
	public static Dimension defaultDims = new Dimension(781, 571);
	
	private int x;
	private int y;
	private Dimension dimensions;

	public int getXPos() {
		return x;
	}
	/**
	 * Penser à actualiser
	 * @param x
	 */
	public void setXPos(int x) {
		this.x = x;
	}
	public int getYPos() {
		return y;
	}
	/**
	 * Penser à actualiser
	 * @param x
	 */
	public void setYPos(int y) {
		this.y = y;
	}
	public Dimension getDimensions() {
		return dimensions;
	}
	/**
	 * Penser à actualiser
	 * @param x
	 */
	public void setDimensions(Dimension dimensions) {
		this.dimensions = dimensions;
	}
	
	// Constructeurs
	
	public JCanvas()
	{
	    x = 0;
	    y = 0;
	    dimensions = defaultDims;
	    
	    setOpaque(false);
	    setBackground(new Color(0, 0, 0, 0));
	    setSize(dimensions);
	    setPreferredSize(dimensions);
	}
	
	// Méthodes
	
	/**
	 * Actualise l'affichage
	 */
	public void actualisePosEtDims() {
		setBounds(x, y, dimensions.width, dimensions.height);
	}
	
	@Override
	protected abstract void paintComponent(Graphics g);
}
