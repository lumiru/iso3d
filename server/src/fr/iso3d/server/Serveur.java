/**
 * 
 */
package fr.iso3d.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fr.iso3d.mapsData.MapList;
import fr.iso3d.message.Message;
import fr.iso3d.message.Personnage;
import fr.iso3d.message.PersonnageQuit;


/**
 * @author lumiru
 *
 */
public class Serveur {
	private static int port = 4444;
	
	private BaseDeDonnees bdd;
	private List<Client> clients;
	private MapList maps;

	public static void main(String[] args) {
		System.out.println("Lancement du serveur...");
		Serveur s = new Serveur();
		try {
			s.lancer();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	public Serveur() {
		clients = new ArrayList<Client>();
		bdd = new BaseDeDonnees();
		maps = MapList.load();
	}
	
	public void lancer() throws Exception {
		Client cthread;
		@SuppressWarnings("resource")
		ServerSocket serveur = new ServerSocket(port);
		
		System.out.println("Le serveur est lancé et en attente de connexions.");
		
		while(true) {
			Socket s = serveur.accept();
			
			cthread = new Client(this, s);
			
			clients.add(cthread);
			cthread.start();
		}
	}
	
	/**
	 * Déploie un message pour tous les connectés à la MAP dans laquelle est le client passé en paramètre à l'exception de celui-ci.
	 * @param c
	 * @param msg
	 */
	public void deployer(Client c, Message msg) {
		Client client;
		String xml = msg.toXML();
		int map = c.getMap();
		
		for (int i = 0, l = clients.size(); i < l; i++) {
			client = clients.get(i);
			
			if(map == client.getMap() && !client.equals(c)) {
				try {
					client.envoyer(xml);
				} catch (IOException e) {
					// TODO Bloc catch généré automatiquement
					e.printStackTrace();
				}
			}
		}
	}
	
	public Personnage[] getPersosOnMap(Client c) {
		return Client.toListPerso(clients, c);
	}

	/**
	 * Vérifie si un client est connecté en fonction de son ID.
	 * @param id
	 * @return
	 */
	public boolean contientClient(int id) {
		Client client;
		
		for (int i = 0, l = clients.size(); i < l; i++) {
			client = clients.get(i);
			
			if(client.getConnecteId() == id) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Supprime un client
	 */
	public void supprimer(Client c) {
		int id = c.getConnecteId();
		
		clients.remove(c);
		
		if(id > 0) {
			deployer(c, new PersonnageQuit(id));
		}
	}
	
	/**
	 * Vérifie si les identifiants de connexion sont corrects.
	 * @param login
	 * @param pass
	 * @return L'ID de l'utilisateur à connecter (0 si incorrect)
	 */
	public int identifiantsOK(String login, String pass) {
		PreparedStatement ps;
		ResultSet data = null;
		int id = 0;
		String hashPass = null;
		
		try {
			bdd.connexion();
			ps = bdd.preparer("SELECT id, pass FROM compte WHERE login = ?");
			ps.setString(1, login);
			
			data = ps.executeQuery();
			if(data.next()) {
				id = data.getInt("id");
				hashPass = data.getString("pass");
			}
		} catch (SQLException e) {
			bdd.fermer();
			e.printStackTrace();
			return 0;
		}
		
		bdd.fermer();
		
		return Sha256.hash(pass).equals(hashPass) ? id : 0;
	}
	
	/**
	 * Inscrit un membre
	 * @param login
	 * @param pass
	 * @return Identifiant de la personne enregistrée ou :
	 * 	<br/>-1 Si une erreur s'est produite.
	 *  <br/>-2 Si le login est déjà utilisé. 
	 */
	public int inscription(String login, String pass) {
		PreparedStatement ps;
		ResultSet data = null;
		// Valeur de retour
		int id = -1;
		
		try {
			bdd.connexion();
			ps = bdd.preparer("SELECT id FROM compte WHERE login = ?");
			ps.setString(1, login);
			
			data = ps.executeQuery();
			if(!data.next()) {
				pass = Sha256.hash(pass);
				
				ps = bdd.preparer("INSERT INTO compte (login, pass) VALUES(?,?)");
				ps.setString(1, login);
				ps.setString(2, pass);
				
				ps.executeUpdate();
				data = ps.getGeneratedKeys();
				
				if(data.next()) {
					id = data.getInt(1);
				}
			}
			else {
				id = -2;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		bdd.fermer();
		return id;
	}
	
	private boolean[] getCasesDisponibles(int map) {
		boolean[] data = maps.get(map).getActivees();
		int pos, lData = data.length;
		
		for (int i = 0, l = clients.size(); i < l; i++) {
			pos = clients.get(i).getPos();
			if(pos >= 0 && pos < lData) {
				data[pos] = false;
			}
		}
		
		return data;
	}
	
	public boolean isDispo(int map, int pos) {
		boolean[] data = getCasesDisponibles(map);
		return pos > 0 && pos < data.length ? data[pos] : false; 
	}
	
	public int getRandomDispo(int map) {
		Random r = new Random();
		boolean[] data = getCasesDisponibles(map);
		int pos, l = data.length;
		
		do {
			pos = r.nextInt(l);
		} while(pos < data.length ? !data[pos] : true);
		
		return pos;
	}
	
	/**
	 * Obtient la case d'entrée en fonction du type utilisé.
	 * @param map
	 * @param type
	 * @return -1 si le type est incorrect
	 */
	public int getEntree(int map, byte type) {
		int pos;
		
		switch(type) {
		case 2:
			type = 4;
		break;
		case 3:
			type = 5;
		break;
		case 4:
			type = 2;
		break;
		case 5:
			type = 3;
		break;
		default:
			return -1;
		}
		
		pos = maps.get(map).getFirstTypePos(type);
		
		if(pos >= 0) {
			switch(type) {
			case 2:
				++pos;
			break;
			case 3:
				pos += 13;
			break;
			case 4:
				--pos;
			break;
			case 5:
				pos -= 13;
			break;
			default:
				return -1;
			}
		}
		
		return isDispo(map, pos) ? pos : getRandomDispo(map);
	}
}
