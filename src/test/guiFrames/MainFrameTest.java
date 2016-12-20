package test.guiFrames;

import javax.swing.JComboBox;
import javax.swing.table.DefaultTableModel;


/**
 * This class implements functions that we call from RefineTable to test the GUI
 * @author viviansh
 */

public class MainFrameTest {
	
	/*
	 * 
	 * 
	 */
	
	public void fillEventsStatic(DefaultTableModel m){
		String [][] tempTable = new String[][]{{"Name1","2015-03-07","Reason1"},
			{"Name2","2013-03-07","Reason2"},
			{"Name3","2014-03-07","Reason3"}
			};
			while(m.getRowCount()!=0)
				m.removeRow(0);
		int i =0;
		for (Object tempEvent[] = new Object[3]; i < tempTable.length;++i) {
			tempEvent[0] = tempTable[i][0];
			tempEvent[1] = tempTable[i][1];
			tempEvent[2] = tempTable[i][2];
			m.addRow(tempEvent);
		}
		
	}
	
public void fillMenuStatic(JComboBox<String> s, String categoryName)  {
		
		s.removeAllItems();
		String[] tempNames = new String[]{"Name1", 
				"Name2",
				"Name3",
				"Name4",
				"Name5",
				"Name6",
				"Name7",
				};
		int i = 0;
		for (String temp = "N/A"; i<tempNames.length;++i) {
			switch (categoryName) {
			case "Date":
				temp = tempNames[i];
				break;
			case "Name":
				temp = tempNames[i];
				break;
			case "Reason":
				break;
			}
			s.addItem(String.valueOf(temp));
		}
	}

}
