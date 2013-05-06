/**
 * 
 */
package fr.iso3d.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fr.iso3d.mapsData.MapList;
import fr.iso3d.message.ChangerMap;
import fr.iso3d.message.Connecte;
import fr.iso3d.message.Connexion;
import fr.iso3d.message.Deplacement;
import fr.iso3d.message.DeplacementFin;
import fr.iso3d.message.Erreur;
import fr.iso3d.message.Inscription;
import fr.iso3d.message.Message;
import fr.iso3d.message.Parler;
import fr.iso3d.message.Personnage;
import fr.iso3d.message.PersonnageQuit;


/**
 * @author lumiru
 *
 */
public class Client extends Thread {
	private static final int defaultMap = 5;
	
	private Serveur serveur;
	private Socket socket;
	private Personnage perso;
	private int map;

	public int getMap() {
		return map;
	}
	
	/**
	 * Identifiant du client connecté.
	 * @return 0 si non identifié.
	 */
	public int getConnecteId() {
		return isConnecte() ? perso.getId() : 0;
	}
	
	/**
	 * Position du personnage dirigé.
	 * @return -1 si non identifié.
	 */
	public int getPos() {
		return isConnecte() ? perso.getPos() : -1;
	}
	
	/**
	 * Vérifie si le client est identfié.
	 */
	public boolean isConnecte() {
		return perso != null;
	}

	public Client(Serveur srv, Socket s) {
		super();

		serveur = srv;
		socket = s;
		perso = null;
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		System.out.println("Client : " + socket.getInetAddress());
		BufferedReader bfr = null;
		String line;
		
		try {
			bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			while((line = bfr.readLine()) != null) {
				// Receptionne et lance le traitement
				traiter(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			if(bfr != null) {
				System.out.println("Fermeture de la connexion avec " + socket.getInetAddress());
				try {
					bfr.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			serveur.supprimer(this);
		}
	}
	
	/**
	 * Envoie un message au client.
	 * @param str Chaine à envoyer
	 * @throws IOException
	 */
	public void envoyer(String str) throws IOException {
		PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
		pw.println(str);
	}
	
	/**
	 * Envoie un message au client.
	 * @param msg Message à envoyer
	 * @throws IOException
	 */
	public void envoyer(Message msg) throws IOException {
		envoyer(msg.toXML());
	}
	
	private void traiter(String str) {
		Message msg = Message.fromXML(str);
		
		if(msg instanceof Connexion) { // Inscription et Connexion
			Connexion c = (Connexion) msg;
			int id;
			
			if(c instanceof Inscription) {
				id = serveur.inscription(c.getLogin(), c.getPass());
				
				if(id < 0) {
					try {
						envoyer(new Erreur(id == -2 ? 2 : 0));
					} catch (IOException e) {
						// TODO Bloc catch généré automatiquement
						e.printStackTrace();
					}
					
					return;
				}
			}
			else {
				id = serveur.identifiantsOK(c.getLogin(), c.getPass());
			}
			
			if(id > 0 && !serveur.contientClient(id)) {
				map = defaultMap;
				perso = new Personnage(id, c.getLogin(), serveur.getRandomDispo(map));
				
				try {
					envoyer(new Connecte(perso, map, serveur.getPersosOnMap(this)));
				} catch (IOException e) {
					// TODO Bloc catch généré automatiquement
					e.printStackTrace();
				}
				
				serveur.deployer(this, perso);
			}
			else {
				try {
					envoyer(new Erreur(id > 0 ? 3 : 1));
				} catch (IOException e) {
					// TODO Bloc catch généré automatiquement
					e.printStackTrace();
				}
			}
		}
		else if(msg instanceof Parler) {
			Parler parle = (Parler) msg;
			parle.setPerso(perso.getId());
			
			try {
				// On renvoie le message à l'expéditeur pour qu'il sache que ça a fonctionné.
				envoyer(parle);
			} catch (IOException e) {
				// TODO Bloc catch généré automatiquement
				e.printStackTrace();
			}
			serveur.deployer(this, parle);
		}
		else if(msg instanceof Deplacement) {
			Deplacement d = (Deplacement) msg;
			perso.setPos(d.getPerso().getPos());
			d.setPerso(perso);
			
			if(serveur.isDispo(map, d.getEndpos())) {
				try {
					// On renvoie le message à l'expéditeur pour qu'il sache que ça a fonctionné.
					envoyer(d);
				} catch (IOException e) {
					// TODO Bloc catch généré automatiquement
					e.printStackTrace();
				}
				
				serveur.deployer(this, d);
				perso.setPos(d.getEndpos());
			}
			else {
				try {
					envoyer(new Erreur(4));
				} catch (IOException e) {
					// TODO Bloc catch généré automatiquement
					e.printStackTrace();
				}
			}
		}
		else if(msg instanceof DeplacementFin) {
			byte type;
			DeplacementFin ad = (DeplacementFin) msg;
			ad.setId(perso.getId());
			
			if(perso.getPos() == ad.getPos() || serveur.isDispo(map, ad.getPos())) {
				/*try {
					// On renvoie le message à l'expéditeur pour qu'il sache que ça a fonctionné.
					envoyer(ad);
				} catch (IOException e) {
					// TODO Bloc catch généré automatiquement
					e.printStackTrace();
				}*/
				
				serveur.deployer(this, ad);
				perso.setPos(ad.getPos());
				
				type = MapList.load().get(map).get(ad.getPos());
				
				if(type >= 2) {
					serveur.deployer(this, new PersonnageQuit(perso.getId()));
					
					switch(type) {
					case 2:
						map--;
					break;
					case 3:
						map -= 3; // TODO
					break;
					case 4:
						map++;
					break;
					case 5:
						map += 3; // TODO
					break;
					}
					
					perso.setPos(serveur.getEntree(map, type));
					//System.out.println(perso.getPos());
					
					try {
						envoyer(new ChangerMap(map, perso.getPos(), serveur.getPersosOnMap(this)));
					} catch (IOException e) {
						// TODO Bloc catch généré automatiquement
						e.printStackTrace();
					}
					
					serveur.deployer(this, perso);
				}
				
			}
		}
		else {
			System.out.println(msg);
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		Client c;
		
		if(obj instanceof Client) {
			c = (Client) obj;
			return socket.getInetAddress() == c.socket.getInetAddress() && socket.getPort() == c.socket.getPort();
		}
		else {
			return false;
		}
	}
	
	/**
	 * Liste les personnages de la MAP courante.
	 * @param clients Tous les clients connectés
	 * @param c Le client courant
	 * @return La liste de ces personnages
	 */
	public static Personnage[] toListPerso(List<Client> clients, Client c) {
		List<Personnage> persos = new ArrayList<Personnage>();
		Client client;
		
		for (Iterator<Client> iterator = clients.iterator(); iterator.hasNext();) {
			client = iterator.next();
			
			if(client.map == c.map && !client.equals(c)) {
				persos.add(client.perso);
			}
		}
		
		return persos.toArray(new Personnage[0]);
	}
}
