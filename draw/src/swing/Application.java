/**
 * 
 */
package swing;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import astarcarre.GraphicalPosition;
import astarcarre.Position;

import socket.RetourServeur;
import socket.Serveur;

import message.ChangerMap;
import message.DeplacementFin;
import message.Connecte;
import message.Deplacement;
import message.Erreur;
import message.Message;
import message.Parler;
import message.Personnage;
import message.PersonnageQuit;

/**
 * @author lumiru
 *
 */
public class Application implements RetourServeur {
	private static Application current;
	
	private Serveur serveur;

	/**
	 * Titre global de l'application
	 */
	public static final String TITLE = "Chat iso3D";
	
	private JFrame window;
	private Interface wInt;
	
	public static Application getCurrent() {
		return current;
	}
	
	public Serveur getServeur() {
		if(serveur == null) {
			connecter();
		}
		
		return serveur;
	}
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		// Ce try/catch est extrèmement crade.
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
		} catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e1) {
			try {
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			} catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e2) {
				// TODO Bloc catch généré automatiquement
				// C'est pas grave, on peut s'en passer !
			}
		}

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					current = new Application();
					current.window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Application() {
		window = new Identification();
		
		serveur = null;
	}

	/* (non-Javadoc)
	 * @see socket.RetourServeur#retour(message.Message)
	 */
	@Override
	public void retour(Message msg) {
		Erreur err;
		
		if(msg instanceof Connecte) {
			if(window instanceof Identification) {
				Connecte c = (Connecte) msg;
				
				window.setVisible(false);
				connexionOK(c.getPerso(), c.getMap(), c.getPersos());
			}
			else {
				System.err.println("Un message de confirmation de connexion a été reçu alors que l'on est déjà connecté.");
			}
		}
		else if(msg instanceof Erreur) {
			err = (Erreur) msg;
			
			JOptionPane.showMessageDialog(window, err.getErreurMessage(),
					"Erreur", JOptionPane.OK_OPTION);
			
			if(window instanceof Identification) {
				((Identification) window).enableBtnConnexion();
			}
		}
		else if(window instanceof Interface) {
			
			if(msg instanceof Parler) {
				Parler parle = (Parler) msg;
				swing.Personnage perso = wInt.getPlateau().getPersonnage(parle.getPerso());
				
				if(perso != null) {
					perso.parler(parle.getMessage());
				}
			}
			else if(msg instanceof Personnage) { // Connexion/Apparition d'un autre personnage
				Personnage p = (Personnage) msg;
				
				wInt.getPlateau().ajouter(p.getId(), p.getLogin(), p.getPos());
			}
			else if(msg instanceof Deplacement) {
				Deplacement depl = (Deplacement) msg;
				Personnage p = depl.getPerso();
				Plateau plateau = wInt.getPlateau();
				Position pos;
				swing.Personnage perso = plateau.getPersonnage(p.getId());
				
				if(perso != null) {
					pos = GraphicalPosition.fromNumero(p.getPos());
					perso.setPosition(pos.x, pos.y);
					perso.deplacer(depl.getDirs(), depl.getEndpos());
				}
			}
			else if(msg instanceof DeplacementFin) {
				DeplacementFin ad = (DeplacementFin) msg;
				Plateau plateau = wInt.getPlateau();
				Position pos;
				swing.Personnage perso = plateau.getPersonnage(ad.getId());
				
				if(perso != null) {
					pos = GraphicalPosition.fromNumero(ad.getPos());
					perso.stopDeplacement();
					perso.setPosition(pos.x, pos.y);
				}
				
			}
			else if(msg instanceof PersonnageQuit) {
				wInt.getPlateau().supprimer(((PersonnageQuit) msg).getId());
			}
			else if(msg instanceof ChangerMap) {
				ChangerMap cm = (ChangerMap) msg;
				Plateau plateau = wInt.getPlateau();
				Personnage[] personnages = cm.getPersos();
				Personnage perso;
				
				plateau.setMap(cm.getMap());
				plateau.setPersoPos(cm.getPos());
				
				for (int j = 0, l = personnages.length; j < l; j++) {
					perso = personnages[j];
					plateau.ajouter(perso.getId(), perso.getLogin(), perso.getPos());
				}
			}
			
		}
		else {
			System.err.println("Message de type inapproprié ("+msg.getClass().getName()+").");
		}
	}
	
	/**
	 * Lance la connexion au serveur.
	 */
	private void connecter() {
		if(this.serveur == null) {
			serveur = new Serveur();
			if(!serveur.connexion("localhost", 4444)) {
				JOptionPane.showMessageDialog(null, "Impossible de s'identifier, le serveur ne répond pas.",
						"Erreur", JOptionPane.OK_OPTION);
				serveur.fermer();
				serveur = null;
				
				return;
			}
			serveur.setReceveur(this);
			serveur.start();
		}
	}
	
	/**
	 * Lance la fenêtre de conversation.
	 * @param personnage Le personnage connecté.
	 * @param map Numéro de la carte (map) où on est connecté.
	 * @param personnages Liste des personnages présent sur la carte courante.
	 */
	private void connexionOK(final Personnage personnage, final int map, final Personnage[] personnages) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				Plateau plateau;
				Personnage personnage2;
				
				try {
					window = wInt = new Interface(TITLE, personnage.getId(), personnage.getLogin());
					plateau = wInt.getPlateau();
					plateau.setMap(map);
					plateau.setPersoPos(personnage.getPos());
					for (int j = 0, l = personnages.length; j < l; j++) {
						personnage2 = personnages[j];
						plateau.ajouter(personnage2.getId(), personnage2.getLogin(), personnage2.getPos());
					}
					
					window.setVisible(true);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(window, "Impossible de lancer l'application.\n"+e.getMessage(),
							"Erreur", JOptionPane.OK_OPTION);
					e.printStackTrace();
					System.exit(-1);
				}
			}
		});
	}
}
