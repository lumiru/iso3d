/**
 * 
 */
package astarcarre;

import java.util.Map;
import java.util.HashMap;

import swing.Plateau;

/**
 * @author lumiru
 *
 */
public class AStar {

	/**
	 * 
	 */
	public AStar() {
		// TODO Stub du constructeur généré automatiquement
	}
	
	public static void main(String[] args) {
		Map<OrthoPosition, Integer> listeOuverte = new HashMap<OrthoPosition, Integer>();
		Map<OrthoPosition, Integer> listeFermee = new HashMap<OrthoPosition, Integer>();
		boolean[] tuilesDispo = new boolean[Plateau.nbTuiles];
		int[] distances = new int[Plateau.nbTuiles];
		int from = 200, to = 90;
		OrthoPosition current = OrthoPosition.fromNumero(from);
		OrthoPosition testing = new OrthoPosition(0,0);
		OrthoPosition tmp;
		int indice;

		for (int i = 0; i < tuilesDispo.length; i++) {
			tuilesDispo[i] = true;
		}
		
		tuilesDispo[99] = false;
		tuilesDispo[199] = false;
		tuilesDispo[49] = false;
		
		listeOuverte.put(current, 0);
		
		do {
			indice = listeOuverte.get(current);
			listeOuverte.remove(current);
			listeFermee.put(current, indice);
			
			
		} while(false);
		
		System.out.println("test");
	}

}
