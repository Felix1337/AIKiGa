package impl;

import interfaces.Logic;

import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;

import javax.swing.*;

public class ElternGUI {

	private JFrame frame;
	private JTextField textInsertKindID;
	private Logic l;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager
					.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) {
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ElternGUI window = new ElternGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ElternGUI() {

		l = new LogicImpl();

		/**
		 * Initialize the contents of the frame.
		 */
		frame = new JFrame();
		frame.setBounds(100, 100, 319, 150);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		final JLabel lblKindID = new JLabel(
				"<html><FONT SIZE=2>Kind ID eintragen</FONT></html>");
		lblKindID.setBounds(9, 18, 100, 14);
		frame.getContentPane().add(lblKindID);

		textInsertKindID = new JTextField();
		textInsertKindID.setColumns(10);
		textInsertKindID.setBounds(9, 43, 106, 20);
		frame.getContentPane().add(textInsertKindID);

		final JLabel lblNummer = new JLabel(
				"<html><FONT SIZE=2>-</FONT></html>");
		lblNummer.setBounds(213, 43, 89, 14);
		frame.getContentPane().add(lblNummer);

		final JLabel lblPlatz = new JLabel(
				"<html><FONT SIZE=2>Platz des Kindes:</FONT></html>");
		lblPlatz.setBounds(126, 43, 89, 14);
		frame.getContentPane().add(lblPlatz);

		JButton btnKindByID = new JButton("Platz ermitteln");
		btnKindByID.setFont(new Font("Dialog", Font.BOLD, 10));
		btnKindByID.setBounds(9, 72, 139, 23);
		frame.getContentPane().add(btnKindByID);
		btnKindByID.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand() == "Platz ermitteln") {
					try {
						lblNummer.setText("<html><FONT SIZE=2>"
								+ l.getPlatzByKindID(Integer
										.parseInt(textInsertKindID.getText()))
								+ "</FONT></html>");
					} catch (Exception ex) {
						lblNummer.setText("<html><FONT SIZE=2>-</FONT></html>");
						textInsertKindID.setText("ung\u00FCltige KindID");
					}
				}
			}
		});
	}
}
