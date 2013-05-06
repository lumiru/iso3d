/**
 * 
 */
package fr.iso3d.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author lumiru
 *
 */
public class BaseDeDonnees {
	private Connection connexion;
	
	public BaseDeDonnees() {
		connexion = null;
	}
	
	public void connexion() throws SQLException {
		String chaineConnex = "jdbc:sqlite:db.sqlite";
		
		try {
			// MySQL
			// Class.forName("com.mysql.jdbc.Driver");
			// chaineConnex = "jdbc:mysql://localhost/BDD_JAVA";
			
			// MSSQLServer
			// Class.forName("net.sourceforge.jtds.jdbs.Driver");
			// chaineConnex = "jdbc:jtds:sqlserver://192.168.1.1:1433/BDD_JAVA";
			
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			System.err.println("Impossible de trouver le driver.");
			System.exit(-1);
		}
		
		try {
			connexion = DriverManager.getConnection(chaineConnex, "", "");
		} catch (SQLException e) {
			System.err.println("Connexion impossible à la base de données.");
			throw e; // On renvoie l'exception pour laisser gérer la fermeture par l'objet appelant.
		}
	}

	public ResultSet requete(String sql) throws SQLException {
		Statement stat = connexion.createStatement();
		return stat.executeQuery(sql);
	}

	public PreparedStatement preparer(String sql) throws SQLException {
		return connexion.prepareStatement(sql);
	}
	
	public void fermer() {
		if(connexion != null) {
			try {
				connexion.close();
			} catch (SQLException e) {
				System.err.println("Une erreur s'est produite pendant la fermeture de la connexion à la base de données.");
			}
		}
	}
}
