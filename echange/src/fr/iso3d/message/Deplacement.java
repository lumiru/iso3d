/**
 * 
 */
package fr.iso3d.message;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author lumiru
 *
 */
@XmlRootElement(name="deplacement")
public class Deplacement extends Message {
	private Personnage perso;
	private Direction[] dirs;
	private int endpos;
	
	public Personnage getPerso() {
		return perso;
	}
	public void setPerso(Personnage perso) {
		this.perso = perso;
	}
	
	@XmlElement(name="item")
	public Direction[] getDirs() {
		return dirs;
	}
	public void setDirs(Direction[] dirs) {
		this.dirs = dirs;
	}
	
	/**
	 * Obtient la position du personnage à la fin du déplacement.
	 */
	public int getEndpos() {
		return endpos;
	}
	/**
	 * Définit la position du personnage à la fin du déplacement.
	 */
	public void setEndpos(int endpos) {
		this.endpos = endpos;
	}

	public Deplacement() {
		this(new Personnage(), new Direction[0], -1);
	}
	
	public Deplacement(Personnage perso, Direction[] dirs, int endpos) {
		super();
		
		this.perso = perso;
		this.dirs = dirs;
		this.endpos = endpos;
	}

}
