/**
 * 
 */
package astarcarre;

import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.Stack;

import message.Direction;

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
	
	public static Stack<Direction> pathFinding(final boolean[] tuilesDispo, final int from, final int to) {
		Stack<Direction> path = null;
		Map<OrthoPosition, Integer> listeOuverte = new HashMap<OrthoPosition, Integer>();
		Map<OrthoPosition, Integer> listeFermee = new HashMap<OrthoPosition, Integer>();
		//boolean[] tuilesDispo = new boolean[Plateau.nbTuiles];
		int[] distances = new int[Plateau.nbTuiles];
		//int from = 200, to = 90;
		int indice, i, l, value;
		final OrthoPosition target = OrthoPosition.fromNumero(to);
		OrthoPosition current = OrthoPosition.fromNumero(from);
		OrthoPosition testing;
		OrthoPosition optimum;
		Map<Direction, OrthoPosition> neighbours;
		Direction tmpDir;
		Direction choosenDir;
		
		for (i = 0, l = distances.length; i < l; i++) {
			distances[i] = Integer.MAX_VALUE;
		}
		
		distances[current.toNumero()] = current.distance(target);
		listeOuverte.put(current, 0);
		
		do {
			indice = listeOuverte.get(current);
			listeOuverte.remove(current);
			listeFermee.put(current, indice);
			++indice;
			
			for (Iterator<OrthoPosition> it = current.neighboursRev().values().iterator(); it.hasNext();) {
				testing = it.next();
				i = testing.toNumero();
				
				if(tuilesDispo[i] && !listeFermee.containsKey(testing)) {
					if(listeOuverte.containsKey(testing)) {
						if (listeOuverte.get(testing) > indice) {
							listeOuverte.put(testing, indice);
						}
					}
					else {
						distances[i] = testing.distance(target);
						listeOuverte.put(testing, indice);
					}
				}
			}
			
			optimum = null;
			value = Integer.MAX_VALUE;
			for (Iterator<OrthoPosition> it = listeOuverte.keySet().iterator(); it.hasNext();) {
				testing = it.next();
				i = testing.toNumero();
				
				if(distances[i] < value) {
					value = distances[i];
					optimum = testing;
				}
			}
			
			if(optimum != null) {
				current = optimum;
			}
		} while(optimum != null && value != 0);
		
		if(value == 0) {
			value = Integer.MAX_VALUE;
			path = new Stack<Direction>();
			do {
				neighbours = current.neighboursRev();
				choosenDir = null;
				optimum = null;
				for (Iterator<Direction> it = neighbours.keySet().iterator(); it.hasNext();) {
					tmpDir = it.next();
					testing = neighbours.get(tmpDir);
					
					if(listeFermee.containsKey(testing) && listeFermee.get(testing) < value) {
						value = listeFermee.get(testing);
						choosenDir = tmpDir;
						optimum = testing;
					}
				}
				
				if(choosenDir != null && optimum != null) {
					current = optimum;
					path.push(choosenDir);
				}
			} while(choosenDir != null && optimum != null && current.toNumero() != to);
		}
		else if(listeOuverte.size() == 0) {
			System.err.println("Liste ouverte vide, tous les chemins semblent avoir été testés.");
		}
		
		if(current.toNumero() == from) {
			return path;
		}
		else {
			return null;
		}
	}

}
