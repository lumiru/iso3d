/**
 * 
 */
package swing;

import java.awt.Dimension;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Stack;

import javax.imageio.ImageIO;

import astarcarre.GraphicalPosition;
import astarcarre.OrthoPosition;
import astarcarre.Position;

import message.Direction;
import utils.Timeout;

import collision.Point;

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
	
	private boolean deplacer(Direction d) {
		int startX = getX(), startY = getY();
		Position p = deplacementVers(posX, posY, d);
		
		if(p != null && setPosition(p.x, p.y)) {
			try {
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

	protected boolean calculDeplacement(final int x, final int y) {
		boolean[] acts = plateau.getTuilesDisponibles();
		
		// Gestion de la pile
		Direction d = null;
		Direction dtmp = null;
		
		// Position finale
		final int endX = x * 2 + y % 2; // Transforme en tableau avec cases intermédiaires (non traversables).
		final int endY = y;
		
		int tmp;
		
		// Position courante dans le calcul
		int tmpX, tmpY;
		
		// Vecteur position courante > arrivée
		int cx, cy;
		
		int lastX = 0, lastY = 0;
		
		// Notes voisines
		int noteMax;
		
		// Itérateurs
		int i, k = 0;
		
		// Notes
		final int defaultVal = Integer.MAX_VALUE;
		int[] note = new int[acts.length];
		
		tmp = (new GraphicalPosition(x, y)).toNumero();
		if(tmp < 0 || !acts[tmp]) {
			return false;
		}
		
		for (int j = 0, l = note.length; j < l; j++) {
			note[j] = defaultVal - (acts[j] ? 0 : 1);
		}
		
		// On arrête le déplacement précédent s'il est en cours
		stopDeplacement();
		
		// Position courante dans le calcul
		tmpX = posX * 2 + posY % 2;
		tmpY = posY;
		
		// Vecteur position courante > arrivée
		cx = endX - tmpX - tmpY % 2;
		cy = endY - tmpY;
		
		// Détermine la direction première à tester
		if(cx > 0 && cy > 0) {
			lastX++;
			lastY++;
		}
		else if(cx > 0 && cy <= 0) {
			lastX++;
			lastY--;
		}
		else if(cx <= 0 && cy <= 0) {
			lastX--;
			lastY--;
		}
		else if(cx <= 0 && cy > 0) {
			lastX--;
			lastY++;
		}
		
		// C'était censé être un A* et finalement non.
		// TODO Implémenter A* !
		do {
			tmpX = posX * 2 + posY % 2;
			tmpY = posY;
			i = 0;
			
			do {
				cx = endX - tmpX;
				cy = endY - tmpY;
				
				note[(new OrthoPosition(tmpX, tmpY).toNumero())] = i++;
				
				if(cx > 0 && cy > 0) {
					lastX = 1;
					lastY = 1;
				}
				else if(cx > 0 && cy < 0) {
					lastX = 1;
					lastY = -1;
				}
				else if(cx < 0 && cy < 0) {
					lastX = -1;
					lastY = -1;
				}
				else if(cx < 0 && cy > 0) {
					lastX = -1;
					lastY = 1;
				}
				
				if(!deplacementEn((tmpX+lastX)/2, tmpY+lastY) ||
						(deplacementEn((tmpX+lastX)/2, tmpY+lastY) && note[(new OrthoPosition(tmpX+lastX, tmpY+lastY)).toNumero()] != defaultVal)) {
					if(deplacementEn((tmpX+1)/2, tmpY+1) && note[(new OrthoPosition(tmpX+1, tmpY+1)).toNumero()] == defaultVal) {
						lastX = 1;
						lastY = 1;
					}
					else if(deplacementEn((tmpX-1)/2, tmpY-1) && note[(new OrthoPosition(tmpX-1, tmpY-1)).toNumero()] == defaultVal) {
						lastX = -1;
						lastY = -1;
					}
					else if(deplacementEn((tmpX+1)/2, tmpY-1) && note[(new OrthoPosition(tmpX+1, tmpY-1)).toNumero()] == defaultVal) {
						lastX = 1;
						lastY = -1;
					}
					else if(deplacementEn((tmpX-1)/2, tmpY+1) && note[(new OrthoPosition(tmpX-1, tmpY+1)).toNumero()] == defaultVal) {
						lastX = -1;
						lastY = 1;
					}
					else if(deplacementEn((tmpX+1)/2, tmpY+1)) {
						lastX = 1;
						lastY = 1;
					}
					else if(deplacementEn((tmpX-1)/2, tmpY-1)) {
						lastX = -1;
						lastY = -1;
					}
					else if(deplacementEn((tmpX+1)/2, tmpY-1)) {
						lastX = 1;
						lastY = -1;
					}
					else if(deplacementEn((tmpX-1)/2, tmpY+1)) {
						lastX = -1;
						lastY = 1;
					}
				}
	
				tmpX += lastX;
				tmpY += lastY;
			} while(deplacementEn(tmpX/2, tmpY) && (tmpX / 2 != x || tmpY != y) && i < 300);
		} while((tmpX / 2 != x || tmpY != y) && k++ < 100);
		
		if(tmpX / 2 == x && tmpY == y) {
			// Se positionner sur l'arrivée (dans la pile)
			tmpX = endX;
			tmpY = endY;
			i = 0;
			// Tant qu'on est pas sur la case de départ
			while ((tmpX / 2 != posX || tmpY != posY) && ++i < 300) {
				// Chercher la case avec la note la plus basse autour
				
				noteMax = defaultVal;
				for (int j = 0; j < 4; j++) {
					switch (j) {
					case 0:
						cx = tmpX + 1;
						cy = tmpY + 1;
						dtmp = Direction.NORD_OUEST;
						break;
					case 1:
						cx = tmpX + 1;
						cy = tmpY - 1;
						dtmp = Direction.SUD_OUEST;
						break;
					case 2:
						cx = tmpX - 1;
						cy = tmpY + 1;
						dtmp = Direction.NORD_EST;
						break;
					case 3:
						cx = tmpX - 1;
						cy = tmpY - 1;
						dtmp = Direction.SUD_EST;
						break;

					default:
						dtmp = null;
						break;
					}
					
					tmp = (new OrthoPosition(cx, cy)).toNumero();
					if(tmp >= 0 && note[tmp] < noteMax) {
						noteMax = note[tmp];
						lastX = cx;
						lastY = cy;
						d = dtmp;
					}
					//System.out.println("[" + cx + "," + cy + "] #" + tmp + " " + (tmp >= 0 ? note[tmp] : ""));
				}
				//System.out.println("["+lastX+","+lastY+"] " + noteMax);
				// Se positionner sur cette case (dans la pile)
				tmpX = lastX;
				tmpY = lastY;
				pile.push(d);
			}
			
			if(tmpX / 2 == posX && tmpY == posY) {
				return true;
			}
			else {
				System.err.println("Une erreur s'est produite pendant le traçage du chemin.");
			}
		}
		else {
			System.err.println("Impossible de trouver de chemin.");
		}
		
		return false;
	}

	public void stopDeplacement() {
		if(deplacement != null) {
			deplacement.setRunning(false);
		}
		
		pile.clear();
	}
	
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

	private Point calculPersoPos(int x, int y) {
		Point p = Plateau.getTuilePos(x, y);
		Dimension d = getDimensions();
		
		p.setX(p.getX() - d.width/2);
		p.setY(p.getY() - d.height + 15);
		
		return p;
	}
	
	private void setBounds(Point p) {
		setBounds(p.getX(), p.getY());
	}
	
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
