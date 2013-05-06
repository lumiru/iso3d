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
@XmlRootElement(name="connecte")
public class Connecte extends Reponse {
	private Personnage perso;
	private int map;
	private Personnage[] persos;

	/**
	 * @return Le personnage nouvellement connecte
	 */
	public Personnage getPerso() {
		return perso;
	}
	/**
	 * @param perso Le personnage nouvellement connecte
	 */
	public void setPerso(Personnage perso) {
		this.perso = perso;
	}
	
	public int getMap() {
		return map;
	}
	public void setMap(int map) {
		this.map = map;
	}

	@XmlElement(name="item")
	public Personnage[] getPersos() {
		return persos;
	}
	public void setPersos(Personnage[] persos) {
		this.persos = persos;
	}

	public Connecte() {
		this(null, 0, new Personnage[0]);
	}

	public Connecte(Personnage perso, int map, Personnage[] persos) {
		super();
		this.perso = perso;
		this.map = map;
		this.persos = persos;
	}

}
