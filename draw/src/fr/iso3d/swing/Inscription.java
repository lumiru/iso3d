/**
 * 
 */
package fr.iso3d.swing;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * @author lumiru
 *
 */
public class Inscription extends JDialog implements ActionListener {
	private static final long serialVersionUID = -7026332631168301876L;
	
	private final JPanel contentPanel = new JPanel();
	private JTextField login_input;
	private JPasswordField confpass_input;
	private JPasswordField pass_input;

	/**
	 * Create the dialog.
	 */
	public Inscription(JFrame owner) {
		super(owner, true);
		
		setTitle("Inscription");
		setBounds(100, 100, 450, 180);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		{
			JLabel lblIdentifiant = new JLabel("Identifiant :");
			contentPanel.add(lblIdentifiant, "2, 2, right, default");
		}
		{
			login_input = new JTextField();
			contentPanel.add(login_input, "4, 2, fill, default");
			login_input.setColumns(10);
		}
		{
			JLabel lblMotDePasse = new JLabel("Mot de passe :");
			contentPanel.add(lblMotDePasse, "2, 4, right, default");
		}
		{
			pass_input = new JPasswordField();
			contentPanel.add(pass_input, "4, 4, fill, default");
		}
		{
			JLabel lblConfirmationDuMot = new JLabel("Confirmation du mot de passe : ");
			contentPanel.add(lblConfirmationDuMot, "2, 6, right, default");
		}
		{
			confpass_input = new JPasswordField();
			contentPanel.add(confpass_input, "4, 6, fill, default");
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(this);
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Annuler");
				cancelButton.addActionListener(this);
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		Application app;
		String login;
		String pass;
		String conf;
		
		
		if(e.getActionCommand() == "OK") {
			app = Application.getCurrent();
			login = login_input.getText();
			pass = new String(pass_input.getPassword());
			conf = new String(confpass_input.getPassword());
			
			if(login.length() == 0 || pass.length() == 0) {
				JOptionPane.showMessageDialog(this, "Vous devez remplir tous les champs.",
						"Erreur", JOptionPane.CANCEL_OPTION);
			}
			else if(pass.equals(conf)) {
				app.getServeur().envoyer(new fr.iso3d.message.Inscription(login, pass));
			}
			else {
				System.out.println(pass + " <> " + conf);
				JOptionPane.showMessageDialog(this, "Le mot de passe et sa confirmation ne sont pas identiques.",
						"Erreur", JOptionPane.CANCEL_OPTION);
			}
		}
		else {
			setVisible(false);
		}
	}

}
