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

public class Gui {

	private JFrame frame;
	private JTextField textFieldVorname;
	private JTextField textFieldNachname;
	private JTextField textFieldGehalt;
	private Logic l;
	private ArrayList<Kita> kitas = new ArrayList<Kita>();
//	private ArrayList<Gruppe> gruppen = new ArrayList<Gruppe>();
//	private ArrayList<Kind> kinder = new ArrayList<Kind>();
	private Gruppe dummiGroup = new GruppeImpl("",9999,"nachts");
	private Kita dummiKita = new KitaImpl("",9999);
	private Kind dummiKid = new KindImpl("","",0,9999);
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
	private boolean gepr�ft =false;
	private boolean warteschlange =false;
	final JButton btnPr�fenEintragen = new JButton("Pr\u00FCfen");
	JLabel Preis = new JLabel("");
	private JTextField textFieldMitglieder;
	private JTextField textFieldDatum;
	private JTextField textFieldDauer;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
    try {
        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
    } 
    catch (Exception e) {
    }
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Gui window = new Gui();
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
	public Gui() {
		l = new LogicImpl();
		
		kitas.addAll(l.getKitas().values());
//		kitas.add(new KitaImpl("test1",0));
//		kitas.add(new KitaImpl("test2",1));
//		gruppen.add(new GruppeImpl("testg1",0,"nachts"));
//		gruppen.add(new GruppeImpl("testg2",1,"nachts"));
//		kinder.add(new KindImpl("testk1","nn1",12.000,0));
//		kinder.add(new KindImpl("testk2","nn2",1000000.00,1));
		
	/**
	 * Initialize the contents of the frame.
	 */
		frame = new JFrame();
		frame.setBounds(100, 100, 319, 458);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblVorname = new JLabel("Vorname");
		lblVorname.setBounds(9, 18, 46, 14);
		frame.getContentPane().add(lblVorname);
		
		JLabel lblNachname = new JLabel("Nachname");
		lblNachname.setBounds(106, 18, 86, 14);
		frame.getContentPane().add(lblNachname);
		
		JLabel lblGehaltDerEltern = new JLabel("Gehalt der Eltern");
		lblGehaltDerEltern.setBounds(9, 74, 86, 14);
		frame.getContentPane().add(lblGehaltDerEltern);
		
		JLabel lblGruppe_pr = new JLabel("Gruppe");
		lblGruppe_pr.setBounds(105, 130, 86, 14);
		frame.getContentPane().add(lblGruppe_pr);
		
		JLabel lblKita = new JLabel("Kita");
		lblKita.setBounds(9, 210, 46, 14);
		frame.getContentPane().add(lblKita);
		
		JLabel lblGruppe = new JLabel("Gruppe");
		lblGruppe.setBounds(9, 256, 46, 14);
		frame.getContentPane().add(lblGruppe);
		
		JLabel lblKind = new JLabel("Kind");
		lblKind.setBounds(9, 305, 46, 14);
		frame.getContentPane().add(lblKind);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(130, 227, 160, 115);
		frame.getContentPane().add(scrollPane);
		
		JPanel panel = new JPanel();
		scrollPane.setViewportView(panel);
		panel.setLayout(new BorderLayout(0, 0));
		panel.add(list, BorderLayout.CENTER);
		
		list.setModel(new AbstractListModel() {
			String[] values = new String[] {"1k", "2k", "3k", "4k", "5k", "6k", "7k", "8k", "9k", "10k"};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		list.setModel(new DefaultComboBoxModel(kitas.toArray()));
		
		final JLabel lblMeldung = new JLabel(" ");
		lblMeldung.setBounds(9, 179, 282, 23);
		frame.getContentPane().add(lblMeldung);
		
		JLabel lblKita_pr = new JLabel("Kita");
		lblKita_pr.setBounds(9, 130, 46, 14);
		frame.getContentPane().add(lblKita_pr);
		
		comboBoxKitas.setBounds(9, 227, 111, 20);
		frame.getContentPane().add(comboBoxKitas);
//		kitas = l.getKitas();
		comboBoxKitas.setModel(new DefaultComboBoxModel(kitas.toArray()));
		comboBoxKitas.insertItemAt(dummiKita, 0);
		comboBoxKitas.setSelectedIndex(0);
		comboBoxKitas.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getActionCommand() == "comboBoxChanged" && ((Kita)comboBoxKitas.getSelectedItem()) != currentKita){
					currentKita = ((Kita)comboBoxKitas.getSelectedItem());
					comboBoxGruppen.removeAllItems();
					comboBoxKinder.removeAllItems();
					comboBoxGruppen.setEnabled(false);
					comboBoxKinder.setEnabled(false);
					Preis.setText("");
					currentGroup = dummiGroup;
					currentChild = dummiKid;
					if(currentKita != null && currentKita != dummiKita){
//						gruppen = l.getGruppen(currentKita.getId());
						
						comboBoxGruppen.setEnabled(true);
						comboBoxKinder.setEnabled(true);
						comboBoxGruppen.setModel(new DefaultComboBoxModel(l.getGruppen(currentKita.getId()).values().toArray()));
						comboBoxGruppen.insertItemAt(dummiGroup, 0);
						comboBoxGruppen.setSelectedIndex(0);
						list.setModel(new DefaultComboBoxModel(l.getGruppen(currentKita.getId()).values().toArray()));
					}else if(currentKita == dummiKita){
						list.setModel(new DefaultComboBoxModel(kitas.toArray()));
					}
				}
			}
		});
		
		comboBoxGruppen.setBounds(9, 274, 111, 20);
		comboBoxGruppen.setEnabled(false);
		frame.getContentPane().add(comboBoxGruppen);
		comboBoxGruppen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getActionCommand() == "comboBoxChanged" && ((Gruppe)comboBoxGruppen.getSelectedItem()) != currentGroup){
					currentGroup = ((Gruppe)comboBoxGruppen.getSelectedItem());
					comboBoxKinder.removeAllItems();
					comboBoxKinder.setEnabled(false);
					Preis.setText("");
					currentChild = dummiKid;
					if(currentGroup != null && currentGroup != dummiGroup){
//						gruppen = l.getGruppen(currentKita.getId());
						
						comboBoxKinder.setEnabled(true);
						comboBoxKinder.setModel(new DefaultComboBoxModel(l.getKinder(currentGroup.getId()).values().toArray()));
						comboBoxKinder.insertItemAt(dummiKid, 0);
						comboBoxKinder.setSelectedIndex(0);
						list.setModel(new DefaultComboBoxModel(l.getKinder(currentGroup.getId()).values().toArray()));
					}else if(currentGroup == dummiGroup){
						list.setModel(new DefaultComboBoxModel(l.getGruppen(currentKita.getId()).values().toArray()));
					}
				}
			}
		});
		
		comboBoxKinder.setBounds(9, 322, 111, 20);
		comboBoxKinder.setEnabled(false);
		frame.getContentPane().add(comboBoxKinder);
		comboBoxKinder.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getActionCommand() == "comboBoxChanged" && ((Kind)comboBoxKinder.getSelectedItem()) != currentChild){
					currentChild = ((Kind)comboBoxKinder.getSelectedItem());
					Preis.setText("");
					if(currentChild != null && currentChild != dummiKid){
						list.setModel(new DefaultComboBoxModel(currentChild.getStats()));
					}else if(currentChild == dummiKid){
						list.setModel(new DefaultComboBoxModel(l.getKinder(currentGroup.getId()).values().toArray()));
					}
				}
			}
		});
		
		comboBoxKita_eintr.setBounds(9, 148, 86, 20);
		frame.getContentPane().add(comboBoxKita_eintr);
		comboBoxKita_eintr.setModel(new DefaultComboBoxModel(kitas.toArray()));
		comboBoxKita_eintr.insertItemAt(dummiKita, 0);
		comboBoxKita_eintr.setSelectedIndex(0);
		comboBoxKita_eintr.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getActionCommand() == "comboBoxChanged" && ((Kita)comboBoxKita_eintr.getSelectedItem()) != currentKita_eintr){
					currentKita_eintr = ((Kita)comboBoxKita_eintr.getSelectedItem());
					comboBoxGruppe_eintr.removeAllItems();
					comboBoxGruppe_eintr.setEnabled(false);
					warteschlange = false;
					gepr�ft = false;
					btnPr�fenEintragen.setText("Pr\u00FCfen");
					if(currentKita_eintr != null && currentKita_eintr != dummiKita){
//						gruppen = l.getGruppen(currentKita.getId());
						
						comboBoxGruppe_eintr.setEnabled(true);
						comboBoxGruppe_eintr.setModel(new DefaultComboBoxModel(l.getGruppen(currentKita.getId()).values().toArray()));
						comboBoxGruppe_eintr.insertItemAt(dummiGroup, 0);
						comboBoxGruppe_eintr.setSelectedIndex(0);
						currentGroup_eintr = dummiGroup;
					}
				}
			}
		});
		
		comboBoxGruppe_eintr.setBounds(105, 148, 86, 20);
		frame.getContentPane().add(comboBoxGruppe_eintr);
		comboBoxGruppe_eintr.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getActionCommand() == "comboBoxChanged" && ((Gruppe)comboBoxGruppe_eintr.getSelectedItem()) != currentGroup_eintr){
					currentGroup_eintr = ((Gruppe)comboBoxGruppe_eintr.getSelectedItem());
					warteschlange = false;
					gepr�ft = false;
					btnPr�fenEintragen.setText("Pr\u00FCfen");
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
				warteschlange=false;
				gepr�ft = false;
				btnPr�fenEintragen.setText("Pr\u00FCfen");
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
				warteschlange=false;
				gepr�ft = false;
				btnPr�fenEintragen.setText("Pr\u00FCfen");
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
				warteschlange=false;
				gepr�ft = false;
				btnPr�fenEintragen.setText("Pr\u00FCfen");
			}
			@Override
			public void keyPressed(KeyEvent arg0) {
			}
			@Override
			public void keyReleased(KeyEvent arg0) {
			}
		});
		
		
		JButton btnRechnungDrucken = new JButton("Rechnung Drucken");
		btnRechnungDrucken.setBounds(9, 386, 123, 23);
		frame.getContentPane().add(btnRechnungDrucken);
		btnRechnungDrucken.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getActionCommand() == "Rechnung Drucken"){
					if(currentChild.getId() != dummiKid.getId()){
						l.rechnungDrucken(currentChild.getId());
					}
				}
			}
		});
				
		
		
		JButton btnPreisErmitteln = new JButton("Preis Ermitteln");
		btnPreisErmitteln.setBounds(9, 353, 123, 23);
		frame.getContentPane().add(btnPreisErmitteln);
		btnPreisErmitteln.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getActionCommand() == "Preis Ermitteln"){
					if(currentChild.getId() != dummiKid.getId()){
						Preis.setText("Preis: " + String.valueOf(l.preisErmitteln(currentChild.getId())) + "�");
					}
				}
			}
		});
		
		Preis.setBounds(142, 353, 145, 23);
		frame.getContentPane().add(Preis);
		
		
		
		btnPr�fenEintragen.setBounds(201, 147, 89, 23);
		frame.getContentPane().add(btnPr�fenEintragen);
		
		JLabel lblAnzahl = new JLabel("Familienmitglieder");
		lblAnzahl.setBounds(106, 74, 89, 14);
		frame.getContentPane().add(lblAnzahl);
		
		textFieldMitglieder = new JTextField();
		textFieldMitglieder.setColumns(10);
		textFieldMitglieder.setBounds(106, 99, 86, 20);
		frame.getContentPane().add(textFieldMitglieder);
		
		JLabel lblGeburtsdatum = new JLabel("Geburtsdatum");
		lblGeburtsdatum.setBounds(202, 18, 101, 14);
		frame.getContentPane().add(lblGeburtsdatum);
		
		textFieldDatum = new JTextField();
		textFieldDatum.setColumns(10);
		textFieldDatum.setBounds(202, 43, 86, 20);
		frame.getContentPane().add(textFieldDatum);
		
		JLabel lblBetreuung = new JLabel("Betreuung");
		lblBetreuung.setBounds(201, 74, 89, 14);
		frame.getContentPane().add(lblBetreuung);
		
		textFieldDauer = new JTextField();
		textFieldDauer.setHorizontalAlignment(SwingConstants.RIGHT);
		textFieldDauer.setText("...in Stunden");
		textFieldDauer.setColumns(10);
		textFieldDauer.setBounds(201, 99, 86, 20);
		frame.getContentPane().add(textFieldDauer);
		btnPr�fenEintragen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getActionCommand() == "Pr�fen" || e.getActionCommand() == "Eintragen"){
					DateFormat formatter;
					Date date;
					Calendar calendar = null;
					if(!gepr�ft){
						try{
							try{	
								formatter = new SimpleDateFormat("MM.dd.yy");
								date = formatter.parse(textFieldDatum.getText());
								calendar = Calendar.getInstance();
								calendar.setTime(date);
							}catch(Exception ex){
								gepr�ft = false;
								lblMeldung.setText("Ung�ltiges Datum");
							}
							
						if(textFieldVorname.getText() != null && textFieldVorname.getText() != "" &&
								textFieldNachname.getText() != null && textFieldNachname.getText() != "" &&
								Double.parseDouble(textFieldGehalt.getText()) >= 0 && 
								Integer.parseInt(textFieldMitglieder.getText()) >= 0 && 
								Integer.parseInt(textFieldDauer.getText()) >= 0){
							if(currentKita_eintr.getId() != 9999 && currentGroup_eintr.getId() != 9999){
								double preis = l.preisErmitteln(Integer.parseInt(textFieldMitglieder.getText()), Double.parseDouble(textFieldGehalt.getText()), Integer.parseInt(textFieldDauer.getText()));
								if(l.isPlatzFrei(currentKita_eintr.getId(),currentGroup_eintr.getId())){
									
									gepr�ft = true;
									lblMeldung.setText("Preis: " + preis + "�. Platz verf�gbar.");
									warteschlange = false;
								}else{
									gepr�ft = true;
									lblMeldung.setText("Preis: " + preis + "�. Kein Platz verf�gbar, Kind in Warteschlange einreihen?");
									warteschlange = true;
								}
							}else{
								gepr�ft = false;
								lblMeldung.setText("Bitte Gruppe ausw�hlen.");
							}
						}else{
							gepr�ft = false;
							lblMeldung.setText("Ung�ltige Angaben");
						}	
						if(gepr�ft)
							btnPr�fenEintragen.setText("Eintragen");
						else
							btnPr�fenEintragen.setText("Pr\u00FCfen");
						return;
						}catch(Exception ec){
							gepr�ft = false;
							lblMeldung.setText("Ung�ltige Angaben");
						}
					}
					
					
					if(gepr�ft){
						if(l.isPlatzFrei(currentKita_eintr.getId(),currentGroup_eintr.getId())){
							l.kindEintragen(textFieldVorname.getText(),textFieldNachname.getText(),calendar,currentGroup_eintr.getId(), warteschlange,Double.parseDouble(textFieldGehalt.getText()),Integer.parseInt(textFieldMitglieder.getText()));
							warteschlange = false;
							gepr�ft = false;
							btnPr�fenEintragen.setText("Pr\u00FCfen");
							lblMeldung.setText("Kind Eingetragen.");
						}else{
							lblMeldung.setText("Platz nicht mehr verf�gbar");
							gepr�ft = false;
							btnPr�fenEintragen.setText("Pr\u00FCfen");
							
						}
					}
				}
				
			}
		});
		
	}
}
