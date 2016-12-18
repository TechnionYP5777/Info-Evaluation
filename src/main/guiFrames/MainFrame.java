package main.guiFrames;


import java.awt.EventQueue;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import main.database.MySQLConnector;
import javax.swing.JComboBox;


/**
 * This class implements the main windows of the GUI
 * @author viviansh
 */
 
public class MainFrame {

	private JFrame frame;
	private JTextField searchTxt;
	private JTable table;
	private JButton btnSearch;
	private RefineTable inputList;
	private JCheckBox chckbxName;
	private JCheckBox chckbxDate;
	private JCheckBox chckbxReason;
	private JMenuItem mntmAbout;
	private JMenu mnHelp;
	private JCheckBox chckbxSortby;
	private JCheckBox chckbxFilterBy;
	private  static MySQLConnector connector;
	private JComboBox <String> comboBox;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					MainFrame window = new MainFrame();
					window.frame.setVisible(true);
					window.onClickCheckBox(window.chckbxDate, window.chckbxName, window.chckbxReason);
					window.onClickCheckBox(window.chckbxName, window.chckbxDate, window.chckbxReason);
					window.onClickCheckBox(window.chckbxReason, window.chckbxName, window.chckbxDate);
					
					window.chckbxSortby.addActionListener(l->{
						window.removeSelected();
						window.comboBox.setVisible(false);
						window.chckbxFilterBy.setSelected(false);
						window.chckbxDate.setText("Date");
						if(window.chckbxSortby.isSelected()){
						window.setVisabilty(true);
						}
						else{
							window.setVisabilty(false);
						}
						
					});
					
					window.chckbxFilterBy.addActionListener(l->{
						window.removeSelected();
						window.comboBox.setVisible(false);
						window.chckbxSortby.setSelected(false);
						window.chckbxDate.setText("Year");
						if(window.chckbxFilterBy.isSelected()){
							window.setVisabilty(true);
							}
						else
							window.setVisabilty(false);
					});
					
					window.btnSearch.addActionListener(e -> {
							DefaultTableModel model = (DefaultTableModel) window.table.getModel();
							if(window.chckbxSortby.isSelected())	
							try{
							window.inputList.sortBy(connector, model, window.selected_chckbx());
							}
							catch (SQLException exc) {
								JOptionPane.showMessageDialog(null, "problem with sql connector","Error", JOptionPane.INFORMATION_MESSAGE);
							}
							if(window.chckbxFilterBy.isSelected()){
								try {
									window.inputList.filterBy(connector, (DefaultTableModel)window.table.getModel(), window.selected_chckbx(), (String)window.comboBox.getSelectedItem());
								} catch (SQLException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							}
							window.table.setVisible(true);	
					});
					window.mntmAbout.addActionListener(m->{
						JOptionPane.showMessageDialog(null,
								("Info Evaluation is a program that reads query results from google search and parses "
										+ "the data,formats it,\nthen analyzes according to the query "
										+ "and returns a structured list of info and interactively suggests"
										+ " new\nqueries in order to achieve the most exact results. "
										+ "\n\n\n\n\nBy: \nVivian Shehadeh\nGenia Shandalov\nOsher Hajaj"
										+ "\nWard Mattar\nMoshiko Elisof\nNetanel Felcher\n"),
								"About", JOptionPane.INFORMATION_MESSAGE);
						
					});
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				finally{
					connector.closeConnection();
				}
				
			}
		});
		
	}

	protected void removeSelected() {
		this.chckbxName.setSelected(false);
		this.chckbxReason.setSelected(false);
		this.chckbxDate.setSelected(false);
		
	}
	protected void setVisabilty(boolean flag) {
		this.chckbxName.setVisible(flag);
		this.chckbxReason.setVisible(flag);
		this.chckbxDate.setVisible(flag);
		
	}

	/**
	 * Create the application.
	 */
	public MainFrame() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Info Evaluation");
		frame.setResizable(false);
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		try{
		connector = new MySQLConnector();
		}
		catch (Exception e){
			JOptionPane.showMessageDialog(null, "problem2 with sql connector","Error", JOptionPane.INFORMATION_MESSAGE);
		}
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);

		mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);

		mntmAbout = new JMenuItem("About");
		mnHelp.add(mntmAbout);
		/*
		 * 
		 * initializing the table
		 * 
		 */
		table = new JTable();
		table.setShowVerticalLines(false);
		table.setCellSelectionEnabled(true);
		table.setColumnSelectionAllowed(true);
		table.setBorder(new LineBorder(null));
		for (int count = 1; count <= 10; ++count)
			table.setModel(new DefaultTableModel(new Object[][] { { "Name", "Date", "Reason" } },
					new String[] { "name", "date", "Reason" }));
		table.setBounds(30, 120, 330, 150);
		frame.getContentPane().add(table);
		table.setVisible(false);

		btnSearch = new JButton("Search");

		searchTxt = new JTextField();
		searchTxt.setText("Search");
		searchTxt.setColumns(10);

		 chckbxName = new JCheckBox("Name");
//		 chckbxName.setName("Name");
		 chckbxName.setVisible(false);

		 chckbxDate = new JCheckBox("Date");
		 chckbxDate.setVisible(false);
		 
		 chckbxReason= new JCheckBox("Reason");
		 chckbxReason.setVisible(false);
//		 chckbxReason.setName("Reason");
		 
		 chckbxSortby = new JCheckBox("Sort by");
		
		 chckbxFilterBy = new JCheckBox("Filter by");
		
		comboBox = new JComboBox();
		comboBox.setVisible(false);
		
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(searchTxt, GroupLayout.PREFERRED_SIZE, 281, GroupLayout.PREFERRED_SIZE)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(12)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(chckbxSortby)
								.addComponent(chckbxName))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(chckbxDate)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(chckbxReason))
								.addComponent(chckbxFilterBy))))
					.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnSearch))
					.addGap(68))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(20)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnSearch)
						.addComponent(searchTxt, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(4)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(chckbxSortby)
								.addComponent(chckbxFilterBy))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(chckbxName)
								.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
									.addComponent(chckbxDate)
									.addComponent(chckbxReason))))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(18)
							.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(180, Short.MAX_VALUE))
		);
		frame.getContentPane().setLayout(groupLayout);

	}
	
	public void onClickCheckBox(JCheckBox chckbx,JCheckBox clearbx1,JCheckBox clearbx2){
		chckbx.addActionListener(l->{
			if(chckbx.isSelected()){
				clearbx1.setSelected(false);
				clearbx2.setSelected(false);
			}
			if(this.chckbxFilterBy.isSelected()){
				try {
					this.inputList.getCategory(connector, this.comboBox, chckbx.getName());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				this.comboBox.setVisible(true);

			}
			
		});
	}
	public String selected_chckbx(){
		return chckbxName.isSelected() ? "name"
				: chckbxDate.isSelected() ? "date" : !chckbxReason.isSelected() ? "none" : "reason";
	}
}