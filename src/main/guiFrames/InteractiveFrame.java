package main.guiFrames;



import static main.database.MySQLConnector.addAllKeywords;

import java.awt.ComponentOrientation;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import java.sql.SQLException;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import main.Analyze.AnalyzeSources;
import main.database.MySQLConnector;

import javax.swing.JTable;

/*
 * 
 * @author viviansh
 * 
 */



@SuppressWarnings("serial")
public class InteractiveFrame extends JFrame {

	private JFrame frame;
	private JTextField txtEnterYourEvent;
	private JTable table;
	private JScrollPane scrollPane;
	private JButton btnAddEvent;
	private AnalyzeSources events;
	private RefineTable inputList;
	


	private JButton btnSubmit;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new InteractiveFrame().setVisible(true);
//					window.addEventButtonOnClick();
				} catch (Exception ¢) {
					¢.printStackTrace();
				}
				
			}
		});
	}

	/**
	 * Create the application.
	 */
	public InteractiveFrame() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 600, 450);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		txtEnterYourEvent = new JTextField();
		txtEnterYourEvent.setText("Enter your event");
		txtEnterYourEvent.setColumns(10);
		
		btnAddEvent = new JButton("Add event");
		table = new JTable();
		table.setModel(new DefaultTableModel(new Object[][] { { "Name", "Date", "Reason" } },
				new String[] { "Name", "Date", "Reason" }));
		table.setBorder(new LineBorder(null));
		scrollPane = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		scrollPane.setVisible(true);

		frame.getContentPane().add(scrollPane);
		
		addEventButtonOnClick();
		
		btnSubmit = new JButton("Submit");
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(32)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(btnSubmit)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(table, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(txtEnterYourEvent, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 348, Short.MAX_VALUE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnAddEvent)))
					.addContainerGap(109, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(35)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(txtEnterYourEvent, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnAddEvent))
					.addGap(40)
					.addComponent(table, GroupLayout.PREFERRED_SIZE, 248, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnSubmit)
					.addContainerGap(59, Short.MAX_VALUE))
		);
		frame.getContentPane().setLayout(groupLayout);

		events = new AnalyzeSources();

		/*
		 * initializing the table
		 *
		 */
		inputList = new RefineTable();
		inputList.addField("Date");
		inputList.addField("Name");
		inputList.addField("Reason");
		inputList.addField("Year");
		
		frame.setVisible(true);

		
	}

	public void addEventButtonOnClick() {
		btnAddEvent.addActionListener(l->{
			String input = txtEnterYourEvent.getText();
			if (!"".equals(input)) {
				events.addSource(input);
				MySQLConnector.addEvents(events.getData());
				try {
					addAllKeywords(events.getData());
				} catch (SQLException ¢) {
					¢.printStackTrace();
				}
				
				}
				final DefaultTableModel model = (DefaultTableModel)table.getModel();
				try {
					inputList.sortBy(model, "none");
					scrollPane.setVisible(true);
				} catch (SQLException ¢) {
					¢.printStackTrace();
				}

			
		});
		
	}
	
	public void submitBtnOnClick(){
		btnSubmit.addActionListener(l->{
			scrollPane.setVisible(false);
			frame.setVisible(false);
			this.setVisible(false);
			
		});
	}
}
