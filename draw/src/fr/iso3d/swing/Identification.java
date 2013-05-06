/**
 * 
 */
package fr.iso3d.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import fr.iso3d.message.Connexion;
import fr.iso3d.socket.Serveur;

import javax.swing.JPanel;

/**
 * @author lumiru
 *
 */
public class Identification extends JFrame {
	private static final long serialVersionUID = 3164746615471597458L;
	
	private JTextField login_input;
	private JPasswordField pass_input;
	private JButton btnConnexion;
	private Inscription inscription_win;
	private JPanel panel;
	private JButton btnInscription;

	/**
	 * Create the frame.
	 */
	public Identification() {
		super();
		
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		setTitle("Identification");
		setBounds(100, 100, 450, 140);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JLabel lblIdentifiant = new JLabel("Identifiant :");
		getContentPane().add(lblIdentifiant, "2, 2, right, default");
		
		login_input = new JTextField();
		login_input.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pass_input.requestFocus();
			}
		});
		lblIdentifiant.setLabelFor(login_input);
		getContentPane().add(login_input, "4, 2, fill, default");
		login_input.setColumns(10);
		login_input.requestFocus();
		
		JLabel lblMotDePasse = new JLabel("Mot de passe :");
		getContentPane().add(lblMotDePasse, "2, 4, right, default");
		
		ActionListener act = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String login = login_input.getText();
				String pass = new String(pass_input.getPassword());
				Serveur serveur;
				
				if(login.length() > 0 && pass.length() > 0) {
					serveur = Application.getCurrent().getServeur();
					
					if(serveur != null) {
						btnConnexion.setEnabled(false);
						serveur.envoyer(new Connexion(login, pass));
					}
					
					pass_input.setText("");
				}
				else {
					JOptionPane.showMessageDialog(null, "Vous devez définir l'identifiant et le mot de passe pour vous connecter.",
							"Erreur", JOptionPane.CANCEL_OPTION);
				}
			}
		};
		
		pass_input = new JPasswordField();
		pass_input.addActionListener(act);
		lblMotDePasse.setLabelFor(pass_input);
		getContentPane().add(pass_input, "4, 4, fill, default");
		
		panel = new JPanel();
		getContentPane().add(panel, "4, 6, fill, fill");
		
		btnInscription = new JButton("Inscription");
		btnInscription.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				showInscriptionDialog();
			}
		});
		panel.add(btnInscription);
		
		btnConnexion = new JButton("Connexion");
		panel.add(btnConnexion);
		btnConnexion.addActionListener(act);
	}
	
	public void enableBtnConnexion() {
		btnConnexion.setEnabled(true);
	}
	
	/**
	 * 
	 */
	private void showInscriptionDialog() {
		inscription_win = new Inscription(this);
		inscription_win.setVisible(true);
		//inscription_win.
	}
}
