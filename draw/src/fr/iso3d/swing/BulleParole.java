/**
 * 
 */
package fr.iso3d.swing;

import java.awt.Color;

import javax.swing.SwingConstants;

import fr.iso3d.utils.Timeout;


/**
 * @author lumiru
 *
 */
public class BulleParole extends Bulle {
	private static final long serialVersionUID = -6239670018785271184L;
	private Timeout to;

	public BulleParole(String texte) {
		super(texte);
		to = new Timeout(6000) {
			@Override
			protected boolean callback() {
				cacher();
				
				return false;
			}
		};
		
		to.start();
		
		setBackground(Color.WHITE);
		setForeground(Color.BLACK);
		setHorizontalAlignment(SwingConstants.CENTER);
	}
	
	public void cacher() {
		if(to.isAlive()) {
			to.interrupt();
		}

		super.cacher();
	}
}
