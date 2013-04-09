/**
 * 
 */
package message;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author lumiru
 *
 */
@XmlRootElement(name="message")
abstract public class Message {

	public Message() {
		// TODO Stub du constructeur généré automatiquement
	}
	
	final public String toXML() {
		JAXBContext jc;
		Marshaller m;
		StringWriter sw;

		try {
			jc = JAXBContext.newInstance(getClass());
			m = jc.createMarshaller();
			sw = new StringWriter();
			m.marshal(this,sw);
			
			return sw.toString();
		} catch (JAXBException e) {
			// TODO Bloc catch généré automatiquement
			e.printStackTrace();
		}
		
		return null;
	}
	
	final public static Message fromXML(String xml) {
		JAXBContext jc;
		Unmarshaller u;
		Object retour;
		
		try {
			jc = JAXBContext.newInstance("message");
			u = jc.createUnmarshaller();
			retour = u.unmarshal(new StringReader(xml));
			
			if(retour instanceof Message) {
				return (Message) retour;
			}
			else {
				System.err.println("Un message de type inconnu ("+retour.getClass().getName()+") a été reçu : \n"+xml);
			}
		} catch (JAXBException e) {
			// TODO Bloc catch généré automatiquement
			e.printStackTrace();
			
			System.err.println(xml);
		}
		
		return null;
	}

}
