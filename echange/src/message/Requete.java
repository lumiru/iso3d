/**
 * 
 */
package message;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author lumiru
 *
 */
@XmlRootElement(name="requete")
abstract public class Requete extends Message {

	public Requete() {
		super();
	}

}
