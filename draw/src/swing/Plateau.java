/**
 * 
 */
package swing;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import astarcarre.GraphicalPosition;
import astarcarre.Position;


import mapsData.MapList;

import collision.Point;
import collision.Tuile;
import java.awt.event.MouseAdapter;

/**
 * @author lumiru
 *
 */
public class Plateau extends JImage {
	private static final long serialVersionUID = 3620320736790611058L;
	public static final int nbTuiles = 463;
	
	/**
	 * Contient la liste des tuiles.
	 */
	private JTuile[] jtuiles;
	
	/**
	 * Tuiles activées
	 */
	private boolean[] tuilesActivees;
	
	/**
	 * Le personnage piloté
	 */
	private PersonnagePilote perso;
	
	private List<Personnage> personnagesPresents;
	
	public void setMap(int map) {
		boolean[] ta = MapList.load().get(map).getActivees();
		
		if(ta.length == nbTuiles) {
			tuilesActivees = ta;
			
			for (Personnage perso : personnagesPresents) {
				remove(perso);
			}
			personnagesPresents.clear();
			
			try {
				setImage(ImageIO.read(new File("maps/"+map+".png")));
				repaint();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(this, "Impossible d'ouvrir l'image du décor.", "Erreur", JOptionPane.OK_OPTION);
				System.exit(-1);
			}
		}
		else {
			System.err.println("Une erreur ("+ta.length+") a empêché de charger la MAP #"+map+".");
			JOptionPane.showMessageDialog(this, "Données de la MAP corrompues.", "Erreur fatale", JOptionPane.ERROR_MESSAGE);
			System.exit(-1);
		}
	}
	
	public void setPersoPos(int pos) {
		Position p = new GraphicalPosition(pos);
		perso.setPosition(p.x, p.y);
	}
	
	/**
	 * @return les tuiles activées et non utilisées
	 */
	public boolean[] getTuilesDisponibles() {
		boolean[] dispos = tuilesActivees.clone();
		int n;
		Personnage p;
		Position pos = new GraphicalPosition(0,0);
		
		if(perso != null) {
			pos.x = perso.getPosX();
			pos.y = perso.getPosY();
			n = pos.toNumero();
			
			if(n >= 0 && n < nbTuiles) {
				dispos[n] = false;
			}
		}
		
		for (int i = 0, l = personnagesPresents.size(); i < l; i++) {
			p = personnagesPresents.get(i);
			pos.x = p.getPosX();
			pos.y = p.getPosY();
			n = pos.toNumero();

			if(n >= 0 && n < nbTuiles) {
				dispos[n] = false;
			}
		}
		
		return dispos;
	}

	/**
	 * Construit le plateau
	 * @throws IOException 
	 */
	public Plateau(int id, String pseudo) {
		super(null);
		
		personnagesPresents = new ArrayList<Personnage>();
		
		jtuiles = new JTuile[nbTuiles];

		tuilesActivees = new boolean[nbTuiles];
		
		perso = new PersonnagePilote(this, id, pseudo);
		
		initialize();
	}
	
	public int getTuileNumero(int x, int y) {
		return (new GraphicalPosition(x,y)).toNumero();
	}
	
	/**
	 * Initialise les éléments graphiques.
	 */
	protected void initialize() {
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Point p = new Point(e.getX(),e.getY());
				Position pos;
				
				for (int i = 0; i < jtuiles.length; i++) {
					if(jtuiles[i].getTuile().collision(p)) {
						pos = new GraphicalPosition(i);
						perso.deplacer(pos.x, pos.y);
						return;
					}
				}
			}
		});
		
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				showTuile(e.getX(),e.getY());
			}
		});
		
		setLayout(null);
		
		for (int i = 0; i < 463; i++) {
			jtuiles[i] = addTuile(i);
			jtuiles[i].setVisible(false);
			add(jtuiles[i], 1, 0);
			//setComponentZOrder(jtuiles[i], 1);
		}

		add(perso, 60, 0);
		
		repaint();
	}
	
	/**
	 * Retourne la position d'une tuile en fonction d'un pixel
	 * @param x Position horizontale du pixel 
	 * @param y Position verticale du pixel
	 * @return Numéro de la tuile
	 */
	private Tuile showTuile(int x, int y) {
		Tuile t = null;
		Point p = new Point(x, y);
		Personnage pers;
		
		for (int i = 0; i < nbTuiles; i++) {
			if(tuilesActivees[i] && jtuiles[i].getTuile().collision(p)) {
				//add(jtuiles[i]);
				jtuiles[i].setVisible(true);
				t = jtuiles[i].getTuile();
				
				if(perso != null && getTuileNumero(perso.getPosX(),perso.getPosY()) == i) {
					perso.afficherPseudo();
				}
				else if(perso != null) {
					perso.cacherPseudo();
				}
				
				for (int j = 0, l = personnagesPresents.size(); j < l; j++) {
					pers = personnagesPresents.get(j);

					if(pers != null && getTuileNumero(pers.getPosX(),pers.getPosY()) == i) {
						pers.afficherPseudo();
					}
					else if(pers != null) {
						pers.cacherPseudo();
					}
				}
			}
			else {
				jtuiles[i].setVisible(false);
				//remove(jtuiles[i]);
			}
		}

		repaint();
		return t;
	}
	
	/**
	 * Retourne une nouvelle tuile graphique
	 * @param i
	 * @return
	 */
	private JTuile addTuile(int i) {
		Position p = new GraphicalPosition(i);
		return new JTuile(new Tuile(getTuilePos(p.x,p.y), i));
	}
	
	/* *
	 * Obtient le numéro d'une tuile à partir de sa position dans le tableau 2D
	 * @param x
	 * @param y
	 * @return
	 * /
	public int getTuileNumero(int x, int y) {
		int n = x + y * 12 + (y + 1) / 2;
		return n >= 0 && x >= 0 && y >= 0 && n < nbTuiles && (x < 12 || (x < 13 && y % 2 == 0)) ? n : -1;
	}*/
	
	/* *
	 * Obtient la position dans le tableau (virtuellement en 2D) de la tuile.
	 * @param i Numéro de la tuile (Position dans le tableau 1D)
	 * @return
	 * /
	public static Position getTuileXY(int i) {
		Position p = new Position(i % 25, i / 25);
		
		// 0..12
		p.y = p.y << 1;
		
		// 13..24
		if(p.x >= 13) {
			p.x -= 13;
			p.y += 1;
		}
		
		return p;
	}*/
	
	/**
	 * Obtient la position en pixel de la tuile.
	 * @param x
	 * @param y
	 * @return
	 */
	public static Point getTuilePos(int x, int y) {
		return new Point((y % 2 == 0 ? 30 : 60) + 60 * x, 15 + 15 * y);
	}
	
	/**
	 * Fait parler le personnage courant.
	 * @param texte
	 */
	public void parlerPerso(String texte) {
		perso.parler(texte);
	}
	
	/**
	 * Ajoute un personnage à la carte courante.
	 * @param p
	 */
	public void ajouter(Personnage p) {
		personnagesPresents.add(p);
		add(p, 60, 0);
		
		repaint();
	}

	/**
	 * Ajoute un personnage au plateau
	 * @param id
	 * @param login
	 * @param pos Position du personnage
	 */
	public void ajouter(int id, String login, int pos) {
		Personnage perso = new Personnage(this, id, login);
		Position p = new GraphicalPosition(pos);
		ajouter(perso);
		perso.setPosition(p.x, p.y);
	}

	/**
	 * Supprime un personnage du plateau en fonction de son ID.
	 * @param id
	 */
	public void supprimer(int id) {
		Personnage perso = getPersonnage(id);
		remove(perso);
		personnagesPresents.remove(perso);
		
		repaint();
	}
	
	/**
	 * Obtient un personnage en fonction de son ID.
	 * @param id
	 * @return
	 */
	public Personnage getPersonnage(int id) {
		if(perso.getId() == id) {
			return perso;
		}
		
		for (int i = 0, l = personnagesPresents.size(); i < l; i++) {
			Personnage perso = personnagesPresents.get(i);
			
			if(perso.getId() == id) {
				return perso;
			}
		}
		
		return null;
	}
	
	/**
	 * Met à jour les couches graphiques pour afficher les objets au-dessus s'ils sont devant.
	 */
	public void updateZ() {
		List<ZLigne> tab = new ArrayList<ZLigne>();
		int i, j, l;
		Personnage p;

		tab.add(perso);
		
		for (i = 0, l = personnagesPresents.size(); i < l; ++i) {
			tab.add(personnagesPresents.get(i));
		}
		
		for (i = 0, l = jtuiles.length; i < l; ++i) {
			tab.add(jtuiles[i]);
		}
		
		Collections.sort(tab);
		
		for (i = 0, j = 0, l = tab.size(); j < l; ++i, ++j) {
			this.setComponentZOrder((Component) tab.get(j), i);
			if(tab.get(j) instanceof Personnage) {
				p = (Personnage) tab.get(j);
				if(p.getBulle() != null) {
					++i;
					this.setComponentZOrder(p.getBulle(), i);
				}
				if(p.getBullePseudo() != null) {
					++i;
					this.setComponentZOrder(p.getBullePseudo(), i);
				}
			}
		}
	}

}
