/**
 * 
 */
package fr.iso3d.swing;

import java.awt.Dimension;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Stack;

import javax.imageio.ImageIO;



import fr.iso3d.astarcarre.AStar;
import fr.iso3d.astarcarre.GraphicalPosition;
import fr.iso3d.astarcarre.Position;
import fr.iso3d.collision.Point;
import fr.iso3d.message.Direction;
import fr.iso3d.utils.Timeout;

/**
 * @author lumiru
 *
 */
public class Personnage extends JImage implements ZLigne {
	private static final long serialVersionUID = -3023595544126000115L;
	
	private int posX;
	private int posY;
	
	private int id;
	private String pseudo;

	private static int delaiAnim = 30;
	private static int nbEtapes = 10;
	private Timeout animation;
	private Timeout deplacement;
	
	private Plateau plateau;
	private BulleParole bulle;
	private Bulle bullePseudo;
	private Stack<Direction> pile;
	
	public int getPosX() {
		return posX;
	}
	public int getPosY() {
		return posY;
	}
	public boolean setPosition(int x, int y) {
		Point p = calculPersoPos(x, y);
		
		if(deplacementEn(x, y)) {
			posX = x;
			posY = y;
			plateau.updateZ();
			setBounds(p);
			
			return true;
		}
		else {
			return false;
		}
	}
	public int getId() {
		return id;
	}
	protected Stack<Direction> getPile() {
		return pile;
	}
	protected Plateau getPlateau() {
		return plateau;
	}
	public BulleParole getBulle() {
		return bulle;
	}
	public Bulle getBullePseudo() {
		return bullePseudo;
	}
	
	public Personnage(Plateau plateau, int id, String pseudo) {
		super(null);
		
		animation = null;
		deplacement = null;
		bulle = null;
		bullePseudo = null;
		pile = new Stack<Direction>();
		
		this.plateau = plateau;
		this.pseudo = pseudo;
		this.id = id;
		
		try {
			setImage(ImageIO.read(new File("joueur.png")));
		} catch (IOException e) {
			// TODO Bloc catch généré automatiquement
			e.printStackTrace();
		}
		
		setPosition(0, 0);
	}
	
	/**
	 * Exécute un déplacement d'une case.
	 * @param d La direction du déplacement à effectuer.
	 * @return Vrai si la case est libre et le déplacement est lancé. 
	 */
	private boolean deplacer(Direction d) {
		int startX = getX(), startY = getY();
		Position p = deplacementVers(posX, posY, d);
		
		if(p != null && setPosition(p.x, p.y)) {
			try {
				// Change l'image affichée du joueur en fonction de la
				// direction dans laquelle il se déplace.
				switch (d) {
				case NORD_OUEST:
					setImage(ImageIO.read(new File("joueur-no.png")));
					break;
				case NORD_EST:
					setImage(ImageIO.read(new File("joueur-ne.png")));
					break;
				case SUD_OUEST:
					setImage(ImageIO.read(new File("joueur-so.png")));
					break;
				case SUD_EST:
					setImage(ImageIO.read(new File("joueur.png")));
					break;
	
				default:
					break;
				}
			} catch (IOException e) {
				// TODO Bloc catch généré automatiquement
				e.printStackTrace();
			}
			
			animerDeplacement(startX, startY);
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Lance le déplacement du personnage à partir de la case courante dans les directions passée en paramètres.
	 * @param dirs Directions à suivre.
	 * @param endpos Case sur laquelle doit finir le déplacement.
	 * @return Vrai si la case est disponible et le déplacement est lancé.
	 */
	public boolean deplacer(Direction[] dirs, int endpos) {
		Position p;
		
		if(deplacementEn(endpos)) {
			stopDeplacement();
			
			p = new GraphicalPosition(endpos);
			Collections.addAll(pile, dirs);
			
			executerDeplacement(p.x, p.y);
			
			return true;
		}
		
		return false;
	}

	/**
	 * Calcul le chemin de déplacement entre la case courante et celle passée en paramètre.
	 * @param x
	 * @param y
	 * @return
	 */
	protected boolean calculDeplacement(int x, int y) {
		boolean[] acts = plateau.getTuilesDisponibles();
		Position from = new GraphicalPosition(posX, posY);
		Position to = new GraphicalPosition(x, y);
		
		Stack<Direction> rpile = AStar.pathFinding(acts, from.toNumero(), to.toNumero());
		
		if(rpile != null) {
			stopDeplacement();
			pile.addAll(rpile);
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * Annule le déplacement en cours du personnage.
	 */
	public void stopDeplacement() {
		if(deplacement != null) {
			deplacement.setRunning(false);
		}
		
		pile.clear();
	}
	
	/**
	 * Execute le déplacement entre la case courante et la case passée en paramètre
	 * à partir de la pile précédemment calculée (dans l'attribut pile).
	 * @param x
	 * @param y
	 */
	private void executerDeplacement(final int x, final int y) {
		if(deplacement != null) {
			deplacement.setRunning(false);
		}
		
		deplacement = new Timeout(delaiAnim * nbEtapes + 10) {
			@Override
			protected boolean callback() {
				boolean empty = pile.isEmpty();
				
				if(!empty && deplacer(pile.pop())) {
					if(!pile.isEmpty()) {
						return true;
					}
					else if((posX != x || posY != y) && calculDeplacement(x, y)) {
						executerDeplacement(x, y);
					}
					else {
						// deplacementOK();
						return true;
					}
				}
				else if(!empty && calculDeplacement(x, y)) {
					executerDeplacement(x, y);
				}
				else if(empty) {
					if(animation != null) {
						animation.setRunning(false);
					}
					
					deplacementOK();
				}
				
				return false;
			}
		};
		deplacement.start();
	}
	
	/**
	 * Anime le déplacement entre la case courante et la case passée en paramètre.
	 * @param x
	 * @param y
	 */
	private void animerDeplacement(int x, int y) {
		final Point start = new Point(x, y);
		final Point end = new Point(getX(), getY());
		
		setBounds(start);
		
		if(animation != null) {
			animation.setRunning(false);
		}
		
		animation = new Timeout(delaiAnim) {
			int i = nbEtapes;
			
			@Override
			protected boolean callback() {
				Point tmp = end.difference(new Point(getX(), getY()));

				setBounds(getX() + tmp.getX() / i, getY() + tmp.getY() / i);
				repaint();
				
				if(--i > 0) {
					return true;
				}
				
				return false;
			}
		};
		animation.start();
	}

	/**
	 * Obtient la position en pixel d'un personnage placé sur la tuile.
	 * @param x Absisse de la tuile.
	 * @param y Ordonnée de la tuile.
	 * @return
	 */
	private Point calculPersoPos(int x, int y) {
		Point p = Plateau.getTuilePos(x, y);
		Dimension d = getDimensions();
		
		p.setX(p.getX() - d.width/2);
		p.setY(p.getY() - d.height + 15);
		
		return p;
	}
	
	/**
	 * Positionne le coin haut gauche de l'objet graphique aux coordonnées du point.
	 * @param p
	 */
	private void setBounds(Point p) {
		setBounds(p.getX(), p.getY());
	}
	
	/**
	 * Positionne le coin haut gauche de l'objet graphique aux coordonnées du point.
	 * @param x
	 * @param y
	 */
	private void setBounds(int x, int y) {
		final Dimension d = getDimensions();
		setBounds(x, y, d.width, d.height);
	}
	
	/**
	 * Calcul le déplacement d'un point [x,y] dans une direction donnée
	 * @param x
	 * @param y
	 * @param d La direction
	 * @return La position après le déplacement
	 */
	private Position deplacementVers(int x, int y, Direction d) {
		switch(d) {
		case NORD_OUEST:
			if(y % 2 == 0) {
				--x;
			}
			--y;
			//System.out.print("'\\ ");
		break;
		case NORD_EST:
			if(y % 2 == 1) {
				++x;
			}
			--y;
			//System.out.print("/' ");
		break;
		case SUD_OUEST:
			if(y % 2 == 0) {
				--x;
			}
			++y;
			//System.out.print(",/ ");
		break;
		case SUD_EST:
			if(y % 2 == 1) {
				++x;
			}
			++y;
			//System.out.print("\\, ");
		break;
		}
		
		if(x < 0 || x > 12 || (y % 2 == 1 && x > 11) || y < 0 || y > 37) {
			return null;
		}
		else {
			return new GraphicalPosition(x, y);
		}
	}
	
	/**
	 * Vérifie qu'un déplacement est possible.
	 * @param ntuile
	 * @return
	 */
	private boolean deplacementEn(int ntuile) {
		boolean[] acts = plateau.getTuilesDisponibles();
		
		return ntuile >= 0 && ntuile < acts.length && acts[ntuile];
	}
	
	/**
	 * Vérifie qu'un déplacement est possible.
	 * @param x
	 * @param y
	 * @return
	 */
	private boolean deplacementEn(int x, int y) {
		return deplacementEn((new GraphicalPosition(x, y)).toNumero());
	}

	protected void deplacementOK() {
		// Nothing to do!
	}
	
	public void parler(String texte) {
		if(bulle != null) {
			bulle.cacher();
		}
		
		bulle = new BulleParole(texte);
		bulle.setBounds(getX() + getWidth() / 2, getY());
		bulle.setLigne(posY);
		
		plateau.add(bulle, 60, 0);
		
		repaint();
	}
	
	public void afficherPseudo() {
		if(bullePseudo == null) {
			bullePseudo = new Bulle(pseudo);
			
			plateau.add(bullePseudo, 60, 0);
			
			repaint();
		}
		else {
			bullePseudo.afficher();
		}
		
		bullePseudo.setBounds(getX() + getWidth() / 2, getY() - 20);
		bullePseudo.setLigne(posY);
	}
	
	public void cacherPseudo() {
		if(bullePseudo != null) {
			bullePseudo.cacher();
		}
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		if(bulle != null) {
			bulle.setBounds(getX() + getWidth() / 2, getY());
			bulle.repaint();
		}
	}

	/* (non-Javadoc)
	 * @see utils.ZLigne#getLigne()
	 */
	@Override
	public int getLigne() {
		return posY;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(ZLigne o) {
		return o.getLigne() - getLigne();
	}
}
