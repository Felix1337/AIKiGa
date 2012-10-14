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
	final JComboBox comboBoxKitas = new JComboBox();
	final JComboBox comboBoxGruppen = new JComboBox();
	final JComboBox comboBoxKinder = new JComboBox();
	final JComboBox comboBoxGruppe_eintr = new JComboBox();
	final JComboBox comboBoxKita_eintr = new JComboBox();
	final JList list = new JList();
	private boolean geprüft = false;
	private boolean warteschlange = false;
	final JButton btnPrüfenEintragen = new JButton("Pr\u00FCfen");
	JLabel Preis = new JLabel("");
	private JTextField textFieldMitglieder;
	private JTextField textFieldDatum;
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
		// l = new LogicImpl();

		// kitas.add(new KitaImpl("test1",0));
		// kitas.add(new KitaImpl("test2",1));
		// gruppen.add(new GruppeImpl("testg1",0,"nachts"));
		// gruppen.add(new GruppeImpl("testg2",1,"nachts"));
		// kinder.add(new KindImpl("testk1","nn1",12.000,0));
		// kinder.add(new KindImpl("testk2","nn2",1000000.00,1));

		/**
		 * Initialize the contents of the frame.
		 */
		frame = new JFrame();
		frame.setBounds(100, 100, 319, 458);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lbl = new JLabel(
				"<html><FONT SIZE=2>Kind ID eintragen</FONT></html>");
		lbl.setBounds(9, 18, 100, 14);
		frame.getContentPane().add(lbl);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(130, 227, 160, 115);
		frame.getContentPane().add(scrollPane);

		JPanel panel = new JPanel();
		scrollPane.setViewportView(panel);
		panel.setLayout(new BorderLayout(0, 0));
		list.setFont(new Font("Dialog", Font.BOLD, 10));
		panel.add(list, BorderLayout.CENTER);

		final JLabel lblMeldung = new JLabel("xxx ");
		lblMeldung.setFont(new Font("Dialog", Font.BOLD, 10));
		lblMeldung.setBounds(9, 179, 282, 23);
		frame.getContentPane().add(lblMeldung);

		comboBoxKitas.setBounds(9, 227, 111, 20);
		frame.getContentPane().add(comboBoxKitas);
		// kitas = l.getKitas();

		comboBoxKita_eintr.setBounds(9, 148, 86, 20);
		frame.getContentPane().add(comboBoxKita_eintr);

		comboBoxGruppe_eintr.setBounds(105, 148, 86, 20);
		frame.getContentPane().add(comboBoxGruppe_eintr);
		comboBoxGruppe_eintr.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand() == "comboBoxChanged"
						&& ((Gruppe) comboBoxGruppe_eintr.getSelectedItem()) != currentGroup_eintr) {
					currentGroup_eintr = ((Gruppe) comboBoxGruppe_eintr
							.getSelectedItem());
					warteschlange = false;
					geprüft = false;
					btnPrüfenEintragen.setText("Pr\u00FCfen");
				}
			}
		});

		textFieldVorname = new JTextField();
		textFieldVorname.setBounds(10, 43, 86, 20);
		frame.getContentPane().add(textFieldVorname);
		textFieldVorname.setColumns(10);
		textFieldVorname.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				warteschlange = false;
				geprüft = false;
				btnPrüfenEintragen.setText("Pr\u00FCfen");
			}

			@Override
			public void keyPressed(KeyEvent arg0) {
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
			}
		});

		textFieldNachname = new JTextField();
		textFieldNachname.setBounds(106, 43, 86, 20);
		frame.getContentPane().add(textFieldNachname);
		textFieldNachname.setColumns(10);
		textFieldNachname.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				warteschlange = false;
				geprüft = false;
				btnPrüfenEintragen.setText("Pr\u00FCfen");
			}

			@Override
			public void keyPressed(KeyEvent arg0) {
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
			}
		});

		textFieldGehalt = new JTextField();
		textFieldGehalt.setBounds(9, 99, 86, 20);
		frame.getContentPane().add(textFieldGehalt);
		textFieldGehalt.setColumns(10);
		textFieldGehalt.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				warteschlange = false;
				geprüft = false;
				btnPrüfenEintragen.setText("Pr\u00FCfen");
			}

			@Override
			public void keyPressed(KeyEvent arg0) {
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
			}
		});

		JButton btnKindByID = new JButton("Platz ermitteln");
		btnKindByID.setFont(new Font("Dialog", Font.BOLD, 10));
		btnKindByID.setBounds(9, 386, 139, 23);
		frame.getContentPane().add(btnKindByID);
		btnKindByID.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand() == "Rechnung Drucken") {
				
				}
			}
		});

		Preis.setFont(new Font("Dialog", Font.BOLD, 10));

		Preis.setBounds(155, 353, 132, 23);
		frame.getContentPane().add(Preis);
		btnPrüfenEintragen.setFont(new Font("Dialog", Font.BOLD, 10));

		btnPrüfenEintragen.setBounds(201, 147, 89, 23);
		frame.getContentPane().add(btnPrüfenEintragen);

		JLabel lblAnzahl = new JLabel(
				"<html><FONT SIZE=2>Familiengröße</FONT></html>");
		lblAnzahl.setBounds(106, 74, 89, 14);
		frame.getContentPane().add(lblAnzahl);

		textFieldMitglieder = new JTextField();
		textFieldMitglieder.setColumns(10);
		textFieldMitglieder.setBounds(106, 99, 86, 20);
		frame.getContentPane().add(textFieldMitglieder);
		textFieldMitglieder.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				warteschlange = false;
				geprüft = false;
				btnPrüfenEintragen.setText("Pr\u00FCfen");
			}

			@Override
			public void keyPressed(KeyEvent arg0) {
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
			}
		});

		JLabel lblGeburtsdatum = new JLabel(
				"<html><FONT SIZE=2>Geburtsdatum</FONT></html>");
		lblGeburtsdatum.setBounds(202, 18, 101, 14);
		frame.getContentPane().add(lblGeburtsdatum);

		textFieldDatum = new JTextField();
		textFieldDatum.setColumns(10);
		textFieldDatum.setBounds(202, 43, 86, 20);
		frame.getContentPane().add(textFieldDatum);
		textFieldDatum.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				warteschlange = false;
				geprüft = false;
				btnPrüfenEintragen.setText("Pr\u00FCfen");
			}

			@Override
			public void keyPressed(KeyEvent arg0) {
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
			}
		});

		JLabel lblBetreuung = new JLabel(
				"<html><FONT SIZE=2>Betreuung</FONT></html>");
		lblBetreuung.setBounds(201, 74, 89, 14);
		frame.getContentPane().add(lblBetreuung);

		textFieldDauer = new JTextField();
		textFieldDauer.setHorizontalAlignment(SwingConstants.RIGHT);
		textFieldDauer.setText("...in Stunden");
		textFieldDauer.setColumns(10);
		textFieldDauer.setBounds(201, 99, 86, 20);
		frame.getContentPane().add(textFieldDauer);
		textFieldDauer.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				warteschlange = false;
				geprüft = false;
				btnPrüfenEintragen.setText("Pr\u00FCfen");
			}

			@Override
			public void keyPressed(KeyEvent arg0) {
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
			}
		});

		btnPrüfenEintragen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});

	}
}
