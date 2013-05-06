/**
 * 
 */
package fr.iso3d.swing;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;


/**
 * @author lumiru
 *
 */
public class Bulle extends JLabel implements ZLigne {
	private static final long serialVersionUID = 1743617783783920378L;
	
	private int ligne;

	public Bulle(String texte) {
		super(texte);

		setOpaque(true);
		setBorder(new LineBorder(Color.BLACK, 2, true));
		setHorizontalAlignment(SwingConstants.CENTER);
		setBackground(new Color(0,0,0,80));
		setForeground(Color.WHITE);
	}
	
	/**
	 * Positionne le coin haut gauche de l'objet graphique aux coordonnées du point.
	 * @param x
	 * @param y
	 */
	public void setBounds(int x, int y) {
		int w = getText().length() * 12;
		int h = 20; 
		
		x = x - w / 2;
		x = x < 0 ? 0 : x;
		x = x > Interface.dimensions.width - w ? Interface.dimensions.width - w : x;
		
		setBounds(x, y - h, w, h);
	}

	public void setLigne(int ligne) {
		this.ligne = ligne;
	}
	
	public void afficher() {
		setVisible(true);
	}
	
	public void cacher() {
		setVisible(false);
		//getParent().remove(this);
	}

	/* (non-Javadoc)
	 * @see utils.ZLigne#getLigne()
	 */
	@Override
	public int getLigne() {
		return ligne;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(ZLigne o) {
		return o.getLigne() - getLigne();
	}
}
