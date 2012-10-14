package impl;

import interfaces.Gruppe;
import interfaces.Kind;
import interfaces.Kita;
import interfaces.Logic;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JComboBox;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.DefaultComboBoxModel;

import java.awt.AWTEvent;
import java.awt.Event;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import javax.swing.Action;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.SwingConstants;
import java.awt.Font;

public class ElternGUI {

	private JFrame frame;
	private JTextField textFieldVorname;
	private JTextField textFieldNachname;
	private JTextField textFieldGehalt;
	// private Logic l;
	private Gruppe dummiGroup = new GruppeImpl("", 9999, "nachts");
	private Kita dummiKita = new KitaImpl("", 9999);
	private Kind dummiKid = new KindImpl("", "", 0, 9999);
	private Kind currentChild = dummiKid;
	private Gruppe currentGroup = dummiGroup;
	private Kita currentKita = dummiKita;
	private Gruppe currentGroup_eintr = dummiGroup;
	private Kita currentKita_eintr = dummiKita;
	final JList list = new JList();
	private boolean geprüft = false;
	private boolean warteschlange = false;
	private JTextField textFieldMitglieder;
	private JTextField textInsertKindID;
	private JTextField textFieldDauer;

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

		/**
		 * Initialize the contents of the frame.
		 */
		frame = new JFrame();
		frame.setBounds(100, 100, 319, 458);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		final JLabel lblKindID = new JLabel(
				"<html><FONT SIZE=2>Kind ID eintragen</FONT></html>");
		lblKindID.setBounds(9, 18, 100, 14);
		frame.getContentPane().add(lblKindID);



		textInsertKindID = new JTextField();
		textInsertKindID.setColumns(10);
		textInsertKindID.setBounds(9, 43, 86, 20);
		frame.getContentPane().add(textInsertKindID);
		textInsertKindID.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				warteschlange = false;
				geprüft = false;
			}

			@Override
			public void keyPressed(KeyEvent arg0) {
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
			}
		});
		
				
		final JLabel lblNummer = new JLabel(
				"<html><FONT SIZE=2>-</FONT></html>");
		lblNummer.setBounds(193, 43, 89, 14);
		frame.getContentPane().add(lblNummer);
		
		final JLabel lblPlatz = new JLabel(
				"<html><FONT SIZE=2>Platz des Kindes:</FONT></html>");
		lblPlatz.setBounds(106, 43, 89, 14);
		frame.getContentPane().add(lblPlatz);
		
		JButton btnKindByID = new JButton("Platz ermitteln");
		btnKindByID.setFont(new Font("Dialog", Font.BOLD, 10));
		btnKindByID.setBounds(9, 72, 139, 23);
		frame.getContentPane().add(btnKindByID);
		btnKindByID.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand() == "Platz ermitteln") {
					lblNummer.setText("<html><FONT SIZE=2>666</FONT></html>");
				}
			}
		});
	}
}
