/**
 * 
 */
package fr.iso3d.message;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author lumiru
 *
 */
@XmlRootElement(name="fin-deplacement")
public class DeplacementFin extends Message {
	private int pos;
	private int id;

	public int getPos() {
		return pos;
	}
	public void setPos(int pos) {
		this.pos = pos;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public DeplacementFin() {
		this(-1);
	}
	public DeplacementFin(int pos) {
		this.pos = pos;
		id = 0;
	}

}
