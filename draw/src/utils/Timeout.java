/**
 * 
 */
package utils;

/**
 * @author lumiru
 *
 */
abstract public class Timeout extends Thread {
	private int delay;
	private boolean running;

	public boolean isRunning() {
		return running;
	}
	public void setRunning(boolean running) {
		this.running = running;
	}

	/**
	 * Crée un Timeout
	 * @param ms Temps d'attente en millisecondes
	 */
	public Timeout(int ms) {
		delay = ms;
		running = true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		super.run();
		
		do {
			try {
				if(running)
					sleep(delay);
			} catch (InterruptedException e) {
				// TODO Bloc catch généré automatiquement
				//e.printStackTrace();
			}
		} while(running && callback());
	}
	
	abstract protected boolean callback();
}
