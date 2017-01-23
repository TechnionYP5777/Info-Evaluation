package main.guiFrames;

import main.database.*;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.text.SimpleDateFormat;

import javax.swing.DefaultCellEditor;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
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
		btnSubmit = new JButton("Submit");

		addEventButtonOnClick();
		submitBtnOnClick();

		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout
				.createSequentialGroup().addGap(32)
				.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createSequentialGroup().addComponent(btnSubmit).addContainerGap(485,
								Short.MAX_VALUE))
						.addGroup(groupLayout.createSequentialGroup().addGroup(groupLayout
								.createParallelGroup(Alignment.TRAILING)
								.addComponent(table, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 539, Short.MAX_VALUE)
								.addGroup(groupLayout.createSequentialGroup()
										.addComponent(txtEnterYourEvent, GroupLayout.DEFAULT_SIZE, 416, Short.MAX_VALUE)
										.addGap(18).addComponent(btnAddEvent)))
								.addGap(29)))));
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

	private static java.sql.Date utilDateToSQLDateConvertor(final java.util.Date utilDate) {
		return java.sql.Date.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(utilDate));
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
			for (InteractiveTableTuple itt : events.getInteractiveData()) {
				Object[] e = new Object[3];
				e[0] = itt.getName();
				e[1] = utilDateToSQLDateConvertor(itt.getRegularDate());
				JComboBox<String> reasonsOptions = new JComboBox<>();
				itt.getReasons().stream().forEach(x -> reasonsOptions.addItem(x.getReason()));
				reasonsOptions.setVisible(true);
				reasonsOptions.setSelectedItem("Click to choose reason");
				table.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(reasonsOptions));
				e[2] = itt.getReasons().get(0).getReason();
				((DefaultTableModel) table.getModel()).addRow(e);
			}

		});

	}

	public void submitBtnOnClick() {
		btnSubmit.addActionListener(l -> {
			DataList dl = new DataList();
			final DefaultTableModel model = (DefaultTableModel) table.getModel();
			for (int ¢ = 0; ¢ < model.getRowCount(); ++¢)
				dl.insert(new TableTuple((String) model.getValueAt(¢, 0), model.getValueAt(¢, 1) + "",
						(String) model.getValueAt(¢, 2)));
			// TODO: how to add the new events to auto-complete?
			MySQLConnector.addEvents(dl);
			// try {
			// addAllKeywords(dl);
			// } catch (SQLException ¢) {
			// ¢.printStackTrace();
			// }

			frame.setVisible(false);
			setVisible(false);
			dispose();
		});
	}
}
