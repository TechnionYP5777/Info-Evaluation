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

public class Frame1 {

	private JFrame frame;
	 private JTextField searchTxt;
	 private JTable table;

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
//		
//		JButton searchBtn = new JButton("Search");
//		frame.getContentPane().add(searchBtn, BorderLayout.NORTH);
//		searchBtn.setBounds(146, 36, 89, 19);
//		searchBtn.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent arg0) {
//
//            }
//        });
		
		
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
        
        JButton btnSearch = new JButton("Search");
        
        searchTxt = new JTextField();
        searchTxt.setText("Search");
        searchTxt.setColumns(10);
        GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
        groupLayout.setHorizontalGroup(
        	groupLayout.createParallelGroup(Alignment.TRAILING)
        		.addGroup(groupLayout.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(searchTxt, GroupLayout.PREFERRED_SIZE, 281, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        			.addComponent(btnSearch)
        			.addGap(74))
        );
        groupLayout.setVerticalGroup(
        	groupLayout.createParallelGroup(Alignment.LEADING)
        		.addGroup(groupLayout.createSequentialGroup()
        			.addGap(20)
        			.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
        				.addComponent(searchTxt, Alignment.TRAILING)
        				.addComponent(btnSearch, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        			.addContainerGap(234, Short.MAX_VALUE))
        );
        frame.getContentPane().setLayout(groupLayout);

        table = new JTable();
        table.setShowVerticalLines(false);
        table.setCellSelectionEnabled(true);
        table.setColumnSelectionAllowed(true);
        table.setBorder(new LineBorder(null));
        for (int count = 1; count <= 10; ++count)
			table.setModel(new DefaultTableModel(new Object[][] { { "Name", "Date", "Reason" } },
					new String[] { "name", "date", "Reason" }));
        table.setBounds(30, 80, 250, 180);
        frame.getContentPane().add(table);
	}
}


