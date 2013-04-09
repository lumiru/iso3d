/**
 * 
 */
package message;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author lumiru
 *
 */
@XmlRootElement(name="perso")
public class Personnage extends Message {
	private int id;
	private String login;
	private int pos;

	@XmlAttribute(name="id")
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@XmlAttribute(name="login")
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	
	@XmlAttribute(name="pos")
	public int getPos() {
		return pos;
	}
	public void setPos(int pos) {
		if(pos >= 0 && pos < 463) {
			this.pos = pos;
		}
		else {
			this.pos = -1;
		}
	}

	public Personnage() {
		this(0,"",0);
	}

	public Personnage(int id, String login, int pos) {
		super();
		
		this.id = id;
		this.login = login;
		this.pos = pos;
	}
}
