/**
 * Class that creates the instructions window.
 * Text for the instructions window is stored as a .htm in the resources folder
 * 
 * @author Josh Pomeroy
 */
package edu.wm.cs.cs301.connectn.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class InstructionsDialog extends JDialog {
	
	private static final long serialVersionUID = 1L;
	
	private JEditorPane editorPane;
	
	public InstructionsDialog(ConnectNFrame view) {
		super(view.getFrame(), "Instructions", true);
		
		add(createMainPanel(), BorderLayout.CENTER);
		add(createButtonPanel(), BorderLayout.SOUTH);
		
		pack();
		setLocationRelativeTo(view.getFrame());
		setVisible(true);
	}
	
	/**
	 * Creates the main panel by taking and displaying what's in instructions.htm
	 * @return - the panel
	 */
	private JPanel createMainPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
		
		URL url = InstructionsDialog.class.getResource("/instructions.htm");
		
		editorPane = new JEditorPane();
		editorPane.setEditable(false);
		editorPane.setContentType("text/html");
		try {
			editorPane.setPage(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		JScrollPane scrollPane = new JScrollPane(editorPane);
		scrollPane.setPreferredSize(new Dimension(600, 480));
		panel.add(scrollPane, BorderLayout.CENTER);
		
		return panel;
	}
	
	/**
	 * Creates the button to close the instructions window
	 * @return - panel containing the button
	 */
	private JPanel createButtonPanel() {
		JPanel panel = new JPanel(new FlowLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
		
		JButton button = new JButton("Close");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				dispose();
			}});
		panel.add(button);
		
		return panel;
	}
}