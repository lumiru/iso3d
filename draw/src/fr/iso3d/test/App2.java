package fr.iso3d.test;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import java.awt.Color;
import javax.swing.border.LineBorder;
import java.awt.Component;

import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class App2 extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1202720198457913506L;

	private JPanel contentPane;
	private JTextField txtTest;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					App2 frame = new App2();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public App2() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
		
		JPanel panel = new JPanel();
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblUneJolieBulle = new JLabel("Une jolie bulle même si elle est moche.");
		lblUneJolieBulle.setHorizontalAlignment(SwingConstants.CENTER);
		lblUneJolieBulle.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblUneJolieBulle.setBounds(83, 106, 338, 31);
		lblUneJolieBulle.setOpaque(true);
		lblUneJolieBulle.setBorder(new LineBorder(Color.BLACK, 2, true));
		lblUneJolieBulle.setBackground(Color.WHITE);
		panel.add(lblUneJolieBulle);
		
		txtTest = new JTextField();
		txtTest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(null, "Clic", "Erreur", JOptionPane.OK_OPTION);
			}
		});
		txtTest.setText("TEst");
		txtTest.setBounds(163, 236, 201, 19);
		panel.add(txtTest);
		txtTest.setColumns(10);
	}
}
