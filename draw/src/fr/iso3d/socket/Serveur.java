/**
 * 
 */
package fr.iso3d.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import fr.iso3d.message.Message;


/**
 * @author lumiru
 *
 */
public class Serveur extends Thread {
	private Socket connexion;
	private RetourServeur receveur;
	
	public void setReceveur(RetourServeur app) {
		this.receveur = app;
	}
	
	public Serveur() {
		this.receveur = null;
		this.connexion = null;
	}

	public Serveur(String ip, int port) {
		this();
		
		connexion(ip, port);
	}
	
	/**
	 * Ouvre une connexion
	 * @param ip
	 * @param port
	 * @return Renvoie vrai si la connexion a réussi.
	 */
	public boolean connexion(String ip, int port) {
		try {
			connexion = new Socket(ip, port);
		} catch (UnknownHostException e) {
			// TODO Bloc catch généré automatiquement
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Bloc catch généré automatiquement
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	/**
	 * Traite le message reçu
	 * @param str Message reçu
	 */
	private void traiter(String str) {
		receveur.retour(Message.fromXML(str));
	}
	
	/**
	 * Envoie un message au serveur
	 * @param evrs Objet à envoyer
	 * @return Vrai si l'envoie a réussi.
	 */
	public boolean envoyer(Message evrs) {
		PrintWriter pw;
		
		if (connexion == null) {
			return false;
		}
		
		try {
			pw = new PrintWriter(connexion.getOutputStream(), true);
			pw.println(evrs.toXML());
		} catch (IOException e1) {
			e1.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	/**
	 * Ferme la connexion
	 */
	public void fermer() {
		try {
			if(connexion != null) {
				connexion.close();
				connexion = null;
			}
		} catch (IOException e) {
			// TODO Bloc catch généré automatiquement
			e.printStackTrace();
		}
		
		if(isAlive()) {
			interrupt();
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		String line;
		BufferedReader reader;
		
		try {
			reader = new BufferedReader(new InputStreamReader(connexion.getInputStream()));
			
			while((line = reader.readLine()) != null) {
				traiter(line);
			}
		} catch (SocketException e) {
			System.out.println("La connexion s'est fermée.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
