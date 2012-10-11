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
import java.util.ArrayList;
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

public class Gui {

	private JFrame frame;
	private JTextField textFieldVorname;
	private JTextField textFieldNachname;
	private JTextField textFieldGehalt;
	private Logic l;
	private ArrayList<Kita> kitas = new ArrayList<Kita>();
	private ArrayList<Gruppe> gruppen = new ArrayList<Gruppe>();
	private ArrayList<Kind> kinder = new ArrayList<Kind>();
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
	private boolean geprüft =false;
	private boolean warteschlange =false;
	final JButton btnPrüfenEintragen = new JButton("Pr\u00FCfen");
	JLabel Preis = new JLabel("");

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
    try {
        UIManager.setLookAndFeel(
                UIManager.getSystemLookAndFeelClassName());
    } 
    catch (Exception e) {
       e.printStackTrace();
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
		
		
		kitas.add(new KitaImpl("test1",0));
		kitas.add(new KitaImpl("test2",1));
		gruppen.add(new GruppeImpl("testg1",0,"nachts"));
		gruppen.add(new GruppeImpl("testg2",1,"nachts"));
		kinder.add(new KindImpl("testk1","nn1",12.000,0));
		kinder.add(new KindImpl("testk2","nn2",1000000.00,1));
		
	/**
	 * Initialize the contents of the frame.
	 */
		frame = new JFrame();
		frame.setBounds(100, 100, 319, 395);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblVorname = new JLabel("Vorname");
		lblVorname.setBounds(10, 14, 46, 14);
		frame.getContentPane().add(lblVorname);
		
		JLabel lblNachname = new JLabel("Nachname");
		lblNachname.setBounds(106, 14, 86, 14);
		frame.getContentPane().add(lblNachname);
		
		JLabel lblGehaltDerEltern = new JLabel("Gehalt der Eltern");
		lblGehaltDerEltern.setBounds(205, 14, 86, 14);
		frame.getContentPane().add(lblGehaltDerEltern);
		
		JLabel lblGruppe_pr = new JLabel("Gruppe");
		lblGruppe_pr.setBounds(106, 67, 86, 14);
		frame.getContentPane().add(lblGruppe_pr);
		
		JLabel lblKita = new JLabel("Kita");
		lblKita.setBounds(10, 147, 46, 14);
		frame.getContentPane().add(lblKita);
		
		JLabel lblGruppe = new JLabel("Gruppe");
		lblGruppe.setBounds(10, 193, 46, 14);
		frame.getContentPane().add(lblGruppe);
		
		JLabel lblKind = new JLabel("Kind");
		lblKind.setBounds(10, 242, 46, 14);
		frame.getContentPane().add(lblKind);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(131, 164, 160, 115);
		frame.getContentPane().add(scrollPane);
		
		JPanel panel = new JPanel();
		scrollPane.setViewportView(panel);
		panel.setLayout(new BorderLayout(0, 0));
		
		list.setModel(new AbstractListModel() {
			String[] values = new String[] {"1k", "2k", "3k", "4k", "5k", "6k", "7k", "8k", "9k", "10k"};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		panel.add(list);
		
		final JLabel lblMeldung = new JLabel(" ");
		lblMeldung.setBounds(10, 116, 282, 23);
		frame.getContentPane().add(lblMeldung);
		
		JLabel lblKita_pr = new JLabel("Kita");
		lblKita_pr.setBounds(10, 67, 46, 14);
		frame.getContentPane().add(lblKita_pr);
		
		comboBoxKitas.setBounds(10, 164, 111, 20);
		frame.getContentPane().add(comboBoxKitas);
//		kitas = l.getKitas();
		comboBoxKitas.setModel(new DefaultComboBoxModel(kitas.toArray()));
		comboBoxKitas.insertItemAt(dummiKita, 0);
		comboBoxKitas.setSelectedIndex(0);
		list.setModel(new DefaultComboBoxModel(kitas.toArray()));
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
						comboBoxGruppen.setModel(new DefaultComboBoxModel(gruppen.toArray()));
						comboBoxGruppen.insertItemAt(dummiGroup, 0);
						comboBoxGruppen.setSelectedIndex(0);
						list.setModel(new DefaultComboBoxModel(gruppen.toArray()));
					}else if(currentKita == dummiKita){
						list.setModel(new DefaultComboBoxModel(kitas.toArray()));
					}
				}
			}
		});
		
		comboBoxGruppen.setBounds(10, 211, 111, 20);
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
						comboBoxKinder.setModel(new DefaultComboBoxModel(kinder.toArray()));
						comboBoxKinder.insertItemAt(dummiKid, 0);
						comboBoxKinder.setSelectedIndex(0);
						list.setModel(new DefaultComboBoxModel(kinder.toArray()));
					}else if(currentGroup == dummiGroup){
						list.setModel(new DefaultComboBoxModel(gruppen.toArray()));
					}
				}
			}
		});
		
		comboBoxKinder.setBounds(10, 259, 111, 20);
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
						list.setModel(new DefaultComboBoxModel(kinder.toArray()));
					}
				}
			}
		});
		
		comboBoxKita_eintr.setBounds(10, 85, 86, 20);
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
					geprüft = false;
					btnPrüfenEintragen.setText("Pr\u00FCfen");
					if(currentKita_eintr != null && currentKita_eintr != dummiKita){
//						gruppen = l.getGruppen(currentKita.getId());
						
						comboBoxGruppe_eintr.setEnabled(true);
						comboBoxGruppe_eintr.setModel(new DefaultComboBoxModel(gruppen.toArray()));
						comboBoxGruppe_eintr.insertItemAt(dummiGroup, 0);
						comboBoxGruppe_eintr.setSelectedIndex(0);
						currentGroup_eintr = dummiGroup;
					}
				}
			}
		});
		
		comboBoxGruppe_eintr.setBounds(106, 85, 86, 20);
		frame.getContentPane().add(comboBoxGruppe_eintr);
		comboBoxGruppe_eintr.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getActionCommand() == "comboBoxChanged" && ((Gruppe)comboBoxGruppe_eintr.getSelectedItem()) != currentGroup_eintr){
					currentGroup_eintr = ((Gruppe)comboBoxGruppe_eintr.getSelectedItem());
					warteschlange = false;
					geprüft = false;
					btnPrüfenEintragen.setText("Pr\u00FCfen");
				}
			}
		});
		
		textFieldVorname = new JTextField();
		textFieldVorname.setBounds(10, 36, 86, 20);
		frame.getContentPane().add(textFieldVorname);
		textFieldVorname.setColumns(10);
		textFieldVorname.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				warteschlange=false;
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
		textFieldNachname.setBounds(106, 36, 86, 20);
		frame.getContentPane().add(textFieldNachname);
		textFieldNachname.setColumns(10);
		textFieldNachname.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				warteschlange=false;
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
		textFieldGehalt.setBounds(205, 36, 86, 20);
		frame.getContentPane().add(textFieldGehalt);
		textFieldGehalt.setColumns(10);
		textFieldGehalt.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				warteschlange=false;
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
		
		
		JButton btnRechnungDrucken = new JButton("Rechnung Drucken");
		btnRechnungDrucken.setBounds(10, 323, 123, 23);
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
		btnPreisErmitteln.setBounds(10, 290, 123, 23);
		frame.getContentPane().add(btnPreisErmitteln);
		btnPreisErmitteln.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getActionCommand() == "Preis Ermitteln"){
					if(currentChild.getId() != dummiKid.getId()){
						Preis.setText("Preis: " + String.valueOf(l.preisErmitteln(currentChild.getId())) + "€");
					}
				}
			}
		});
		
		Preis.setBounds(146, 290, 145, 23);
		frame.getContentPane().add(Preis);
		
		
		
		btnPrüfenEintragen.setBounds(202, 84, 89, 23);
		frame.getContentPane().add(btnPrüfenEintragen);
		btnPrüfenEintragen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getActionCommand() == "Prüfen" || e.getActionCommand() == "Eintragen"){

					if(!geprüft){
						try{
						if(textFieldVorname.getText() != null && textFieldVorname.getText() != " " && textFieldNachname.getText() != null && textFieldNachname.getText() != " " && Double.parseDouble(textFieldGehalt.getText()) >= 0){
							if(currentKita_eintr.getId() != 9999 && currentGroup_eintr.getId() != 9999){
								if(l.isPlatzFrei(currentKita_eintr.getId(),currentGroup_eintr.getId())){
									geprüft = true;
									lblMeldung.setText("Platz verfügbar.");
									warteschlange = false;
								}else{
									geprüft = true;
									lblMeldung.setText("Kein Platz verfügbar, Kind in Warteschlange einreihen?");
									warteschlange = true;
								}
							}else{
								geprüft = false;
								lblMeldung.setText("Bitte Gruppe auswählen.");
							}
						}else{
							geprüft = false;
							lblMeldung.setText("Ungültige Angaben");
						}	
						if(geprüft)
							btnPrüfenEintragen.setText("Eintragen");
						else
							btnPrüfenEintragen.setText("Pr\u00FCfen");
						return;
						}catch(Exception ec){
							lblMeldung.setText("Ungültige Angaben");
						}
					}
					
					
					if(geprüft){
						l.kindEintragen(textFieldVorname.getText(),textFieldNachname.getText(),currentKita_eintr.getId(), currentGroup_eintr.getId(), warteschlange,Double.parseDouble(textFieldGehalt.getText()));
						warteschlange = false;
						geprüft = false;
						btnPrüfenEintragen.setText("Pr\u00FCfen");
						lblMeldung.setText("Kind Eingetragen.");
					}
				}
				
			}
		});
		
	}
}
