package impl;

import interfaces.Gruppe;
import interfaces.Logic;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

public class ElternGUI {

	private JFrame frame;
	private JTextField textInsertKindID;
	final JList list = new JList();
	private Logic l;
	private ArrayList<String> gruppeUndPlatz;
	private Map<Gruppe,Integer> listenPlatz;
	
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
		listenPlatz = new HashMap<Gruppe,Integer>();

		/**
		 * Initialize the contents of the frame.
		 */
		frame = new JFrame();
		frame.setBounds(100, 100, 319, 350);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		final JLabel lblKindID = new JLabel(
				"<html><FONT SIZE=2>Kind ID eintragen:</FONT></html>");
		lblKindID.setBounds(9, 18, 100, 14);
		frame.getContentPane().add(lblKindID);

		textInsertKindID = new JTextField();
		textInsertKindID.setColumns(10);
		textInsertKindID.setBounds(9, 43, 139, 20);
		frame.getContentPane().add(textInsertKindID);



		final JLabel lblPlatz = new JLabel(
				"<html><FONT SIZE=2>Platz des Kindes:</FONT></html>");
		lblPlatz.setBounds(9, 104, 89, 14);
		frame.getContentPane().add(lblPlatz);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(9, 140, 282, 155);
		frame.getContentPane().add(scrollPane);
		
		JPanel panel = new JPanel();
		scrollPane.setViewportView(panel);
		panel.setLayout(new BorderLayout(0, 0));
		list.setFont(new Font("Dialog", Font.BOLD, 10));
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
		list.setModel(new DefaultComboBoxModel(listenPlatz.values().toArray()));
		
		JButton btnKindByID = new JButton("Platz ermitteln");
		btnKindByID.setFont(new Font("Dialog", Font.BOLD, 10));
		btnKindByID.setBounds(9, 72, 139, 23);
		frame.getContentPane().add(btnKindByID);
		btnKindByID.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand() == "Platz ermitteln") {
					try {
						listenPlatz = new HashMap<Gruppe,Integer>();
//						listenPlatz.put(new GruppeImpl("hi", 1, "4"),7);
//						listenPlatz.put(new GruppeImpl("ho", 2, "4"),8);
//						listenPlatz.put(new GruppeImpl("hu", 3, "4"),3);
						
						listenPlatz = l.getWartelistePosition(Integer.parseInt(textInsertKindID.getText()));
						gruppeUndPlatz = new ArrayList<String>();
						for(Gruppe g : listenPlatz.keySet()){
							gruppeUndPlatz.add("Gruppe "+g.toString() + " - Platz " + listenPlatz.get(g));
						}
						
						list.setModel(new DefaultComboBoxModel(gruppeUndPlatz.toArray()));
					} catch (Exception ex) {

						textInsertKindID.setText("ung\u00FCltige KindID");
					}
				}
			}
		});
	}
}
