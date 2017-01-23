package main.guiFrames;

import main.database.*;

import static main.database.MySQLConnector.addAllKeywords;
import java.awt.ComponentOrientation;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import java.sql.SQLException;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import main.Analyze.AnalyzeSources;
import main.database.MySQLConnector;

import javax.swing.JTable;

/*
 * 
 * @author viviansh
 * @author ward-mattar
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
			@Override
			public void run() {
				try {
					new InteractiveFrame().setVisible(true);
					// window.addEventButtonOnClick();
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
		frame.getContentPane().setEnabled(true);
		frame.setBounds(100, 100, 600, 450);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

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
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup().addGap(32)
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addComponent(btnSubmit)
								.addGroup(groupLayout.createSequentialGroup()
										.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
												.addComponent(table, Alignment.LEADING, GroupLayout.DEFAULT_SIZE,
														GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(txtEnterYourEvent, Alignment.LEADING,
														GroupLayout.DEFAULT_SIZE, 348, Short.MAX_VALUE))
										.addPreferredGap(ComponentPlacement.RELATED).addComponent(btnAddEvent)))
						.addContainerGap(109, Short.MAX_VALUE)));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup().addGap(35)
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(txtEnterYourEvent, GroupLayout.PREFERRED_SIZE, 31,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(btnAddEvent))
						.addGap(40).addComponent(table, GroupLayout.PREFERRED_SIZE, 248, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(btnSubmit)
						.addContainerGap(59, Short.MAX_VALUE)));
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
		btnAddEvent.addActionListener(l -> {
			String input = txtEnterYourEvent.getText();
			if (!"".equals(input))
				try {
					txtEnterYourEvent.setText("Loading please wait...");
					events.addSource(input);
					txtEnterYourEvent.setText(input);
				} catch (Exception e) {
					txtEnterYourEvent.setText("Could not parse, please try again");
				}
			final DefaultTableModel model = (DefaultTableModel) table.getModel();
			List<InteractiveTableTuple> newEvents = events.getInteractiveData();
			for (InteractiveTableTuple itt : newEvents) {
				Object[] e = new Object[3];
				e[0] = itt.getName();
				e[1] = itt.getDate();
				JComboBox<String> reasonsOptions = new JComboBox<>();
				itt.getReasons().stream().forEach(x -> reasonsOptions.addItem(x.getReason()));
				reasonsOptions.setVisible(true);
				reasonsOptions.setSelectedIndex(0);
				table.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(reasonsOptions));
				e[2] = itt.getReasons().get(0).getReason();
				model.addRow(e);
			}
			scrollPane.setVisible(true);
		});

	}

	public void submitBtnOnClick() {
		btnSubmit.addActionListener(l -> {
			DataList dl = new DataList();
			final DefaultTableModel model = (DefaultTableModel) table.getModel();
			for (int ¢ = 0; ¢ < model.getRowCount(); ++¢)
				dl.insert(new TableTuple((String) model.getValueAt(¢, 0), (String) model.getValueAt(¢, 1),
						(String) model.getValueAt(¢, 2)));
			// TODO: how to add the new events to auto-complete? 
			MySQLConnector.addEvents(dl);
			try {
				addAllKeywords(dl);
			} catch (SQLException ¢) {
				¢.printStackTrace();
			}
/*
			scrollPane.setVisible(false);
			frame.setVisible(false);
			setVisible(false);
			dispose();
*/		});
	}
}
