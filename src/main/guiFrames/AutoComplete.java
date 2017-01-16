package main.guiFrames;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;

/*
 * @author viviansh
 * @since 16.1.2017
 * 
 */

public class AutoComplete {

    private static boolean isAdjusting(@SuppressWarnings("rawtypes") JComboBox cbInput) {
		return cbInput.getClientProperty("is_adjusting") instanceof Boolean
				&& (Boolean) cbInput.getClientProperty("is_adjusting");
	}

    private static void setAdjusting(@SuppressWarnings("rawtypes") JComboBox cbInput, boolean adjusting) {
        cbInput.putClientProperty("is_adjusting", adjusting);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static void setupAutoComplete(final JTextField txtInput, final ArrayList<String> items) {
        final DefaultComboBoxModel model = new DefaultComboBoxModel();
        @SuppressWarnings({ "serial" })
		final JComboBox cbInput = new JComboBox(model) {
            public Dimension getPreferredSize() {
                return new Dimension(super.getPreferredSize().width, 0);
            }
        };
        setAdjusting(cbInput, false);
        for (String item : items)
			model.addElement(item);
        cbInput.setSelectedItem(null);
        cbInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent __) {
                if (!isAdjusting(cbInput) && cbInput.getSelectedItem() != null)
					txtInput.setText((cbInput.getSelectedItem() + ""));
            }
        });

        txtInput.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent ¢) {
                setAdjusting(cbInput, true);
                if (¢.getKeyCode() == KeyEvent.VK_SPACE && cbInput.isPopupVisible())
					¢.setKeyCode(KeyEvent.VK_ENTER);
                if (¢.getKeyCode() == KeyEvent.VK_ENTER || ¢.getKeyCode() == KeyEvent.VK_UP || ¢.getKeyCode() == KeyEvent.VK_DOWN) {
                    ¢.setSource(cbInput);
                    cbInput.dispatchEvent(¢);
                    if (¢.getKeyCode() == KeyEvent.VK_ENTER) {
                        txtInput.setText((cbInput.getSelectedItem() + ""));
                        cbInput.setPopupVisible(false);
                    }
                }
                if (¢.getKeyCode() == KeyEvent.VK_ESCAPE)
					cbInput.setPopupVisible(false);
                setAdjusting(cbInput, false);
            }
        });
        txtInput.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent __) {
                updateList();
            }

            public void removeUpdate(DocumentEvent __) {
                updateList();
            }

            public void changedUpdate(DocumentEvent __) {
                updateList();
            }

            void updateList() {
                setAdjusting(cbInput, true);
                model.removeAllElements();
                String input = txtInput.getText();
                if (!input.isEmpty())
					for (String item : items)
						if (item.toLowerCase().startsWith(input.toLowerCase()))
							model.addElement(item);
                cbInput.setPopupVisible(model.getSize() > 0);
                setAdjusting(cbInput, false);
            }
        });
        txtInput.setLayout(new BorderLayout());
        txtInput.add(cbInput, BorderLayout.SOUTH);
    }
}