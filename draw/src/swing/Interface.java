package swing;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextField;

import message.Parler;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Interface extends JFrame {
	private static final long serialVersionUID = 5009439653975904608L;
	
	/**
	 * Dimensions de la MAP
	 */
	public static final Dimension dimensions = new Dimension(781, 571);
	
	private JTextField chat_message_input;
	private Plateau plateau;
	
	public Plateau getPlateau() {
		return plateau;
	}

	public Interface(String titre, int id, String pseudo) throws IOException {
		super(titre);
		
		initialize(id, pseudo);
	}

	/**
	 * Initialize the contents of the frame.
	 * @param pos 
	 * @param pseudo 
	 * @param id 
	 * @throws IOException 
	 */
	private void initialize(int id, String pseudo) throws IOException {
		setSize(new Dimension(dimensions.width + 64, dimensions.height + 128));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getRootPane().setPreferredSize(dimensions);
		getRootPane().setMinimumSize(dimensions);
		getContentPane().setMinimumSize(dimensions);
		setBackground(Color.BLACK);

		getContentPane().setBackground(Color.BLACK);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{1, 0};
		gridBagLayout.rowHeights = new int[]{1, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		
		JPanel cadre = new JPanel();
		cadre.setBackground(Color.BLACK);
		cadre.setPreferredSize(new Dimension(dimensions.width, dimensions.height + 40));
		GridBagConstraints gbc_cadre = new GridBagConstraints();
		gbc_cadre.gridx = 0;
		gbc_cadre.gridy = 0;
		getContentPane().add(cadre, gbc_cadre);
		GridBagLayout gbl_cadre = new GridBagLayout();
		gbl_cadre.columnWidths = new int[] {0};
		gbl_cadre.rowHeights = new int[] {571, 0};
		gbl_cadre.columnWeights = new double[]{1.0};
		gbl_cadre.rowWeights = new double[]{0.0, 0.0};
		cadre.setLayout(gbl_cadre);
		
		plateau = new Plateau(id, pseudo);
		GridBagConstraints gbc_plateau = new GridBagConstraints();
		gbc_plateau.fill = GridBagConstraints.HORIZONTAL;
		gbc_plateau.anchor = GridBagConstraints.NORTH;
		gbc_plateau.insets = new Insets(0, 0, 0, 0);
		gbc_plateau.gridx = 0;
		gbc_plateau.gridy = 0;
		cadre.add(plateau, gbc_plateau);
		
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 1;
		cadre.add(panel, gbc_panel);
		panel.setBackground(Color.BLACK);
		panel.setPreferredSize(new Dimension(10, 100));
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] {275, 30, 117};
		gbl_panel.rowHeights = new int[] {40};
		gbl_panel.columnWeights = new double[]{1.0, 0.0, 0.0};
		gbl_panel.rowWeights = new double[]{0.0};
		panel.setLayout(gbl_panel);
		
		ActionListener act = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(Application.getCurrent().getServeur().envoyer(new Parler(chat_message_input.getText()))) {
					//plateau.parlerPerso(chat_message_input.getText());
					
					chat_message_input.setText("");
				}
				else {
					JOptionPane.showMessageDialog(null, "Une erreur s'est produite pendant l'envoie du message et a empêcher l'opération.",
							"Erreur", JOptionPane.ERROR_MESSAGE);
				}
			}
		};
		
		chat_message_input = new JTextField();
		GridBagConstraints gbc_chat_message_input = new GridBagConstraints();
		gbc_chat_message_input.fill = GridBagConstraints.HORIZONTAL;
		gbc_chat_message_input.insets = new Insets(0, 0, 0, 5);
		gbc_chat_message_input.gridx = 0;
		gbc_chat_message_input.gridy = 0;
		panel.add(chat_message_input, gbc_chat_message_input);
		chat_message_input.setColumns(10);
		chat_message_input.addActionListener(act);
		
		JButton send_button = new JButton("Envoyer");
		
		send_button.addActionListener(act);
		
		GridBagConstraints gbc_send_button = new GridBagConstraints();
		gbc_send_button.anchor = GridBagConstraints.WEST;
		gbc_send_button.gridx = 2;
		gbc_send_button.gridy = 0;
		panel.add(send_button, gbc_send_button);
	}
}
