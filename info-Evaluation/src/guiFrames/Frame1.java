/*
 * 
 * @author viviansh
 * 
 */


package guiFrames;

import java.awt.EventQueue;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JLabel;
import javax.swing.JCheckBox;

public class Frame1 {

	private JFrame frame;
	 private JTextField searchTxt;
	 private JTable table;
	 private JButton btnSearch; 

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					Frame1 window = new Frame1();
					window.frame.setVisible(true);
					window.btnSearch.addActionListener(e -> {
						if(!window.table.isVisible())
							window.table.setVisible(true);
				    });
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Frame1() {
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
		
		
		JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        JMenu mnFile = new JMenu("File");
        menuBar.add(mnFile);


        JMenu mnEdit = new JMenu("Edit");
        menuBar.add(mnEdit);

        JMenu mnHelp = new JMenu("Help");
        menuBar.add(mnHelp);

        JMenuItem mntmHelp = new JMenuItem("Help");
        mnHelp.add(mntmHelp);
        /*
         * 
         *  initializing the table 
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
        
        JLabel lblNewLabel = new JLabel("Sort by:");
        
        JCheckBox chckbxName = new JCheckBox("Name");
        
        JCheckBox chckbxReason = new JCheckBox("Date");
        
        JCheckBox chckbxNewCheckBox = new JCheckBox("Reason");
        GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
        groupLayout.setHorizontalGroup(
        	groupLayout.createParallelGroup(Alignment.LEADING)
        		.addGroup(groupLayout.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
        				.addGroup(groupLayout.createSequentialGroup()
        					.addGap(12)
        					.addComponent(chckbxName)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(chckbxReason)
        					.addPreferredGap(ComponentPlacement.UNRELATED)
        					.addComponent(chckbxNewCheckBox)
        					.addContainerGap())
        				.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
        					.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
        						.addComponent(searchTxt, GroupLayout.PREFERRED_SIZE, 281, GroupLayout.PREFERRED_SIZE)
        						.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        						.addComponent(btnSearch)
        						.addGap(74))
        					.addGroup(groupLayout.createSequentialGroup()
        						.addComponent(lblNewLabel)
        						.addContainerGap(372, Short.MAX_VALUE)))))
        );
        groupLayout.setVerticalGroup(
        	groupLayout.createParallelGroup(Alignment.LEADING)
        		.addGroup(groupLayout.createSequentialGroup()
        			.addGap(20)
        			.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(btnSearch)
        				.addComponent(searchTxt, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(lblNewLabel)
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(chckbxName)
        				.addComponent(chckbxReason)
        				.addComponent(chckbxNewCheckBox))
        			.addContainerGap(152, Short.MAX_VALUE))
        );
        frame.getContentPane().setLayout(groupLayout);

       
	}
}


