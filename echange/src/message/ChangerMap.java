/**
 * 
 */
package message;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author lumiru
 *
 */
@XmlRootElement(name="change-map")
public class ChangerMap extends Reponse {
	private int map;
	private int pos;
	private Personnage[] persos;

	public int getMap() {
		return map;
	}
	public void setMap(int map) {
		this.map = map;
	}
	public int getPos() {
		return pos;
	}
	public void setPos(int pos) {
		this.pos = pos;
	}
	@XmlElement(name="item")
	public Personnage[] getPersos() {
		return persos;
	}
	public void setPersos(Personnage[] persos) {
		this.persos = persos;
	}
	
	public ChangerMap() {
		this(0, -1, new Personnage[0]);
	}
	public ChangerMap(int map, int pos, Personnage[] persos) {
		this.map = map;
		this.pos = pos;
		this.persos = persos;
	}

}
