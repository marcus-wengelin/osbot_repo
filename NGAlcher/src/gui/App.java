package gui;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JTextPane;
import java.awt.Color;

public class App extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private JTextField textField;
	private JTextPane textPane;
	private String itemNames;

	public static App startApp() {
		try {
			App app = new App();
			app.setVisible(true);
			return app;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void setStatistics(String runtime) {
		textPane.setText(String.format("Runtime: %s", runtime));
	}
	public String[] getItemNames() {
		if (itemNames == null) return null;
		return itemNames.split("\\s*,\\s*");
	}
	public boolean hasApplied() {
		return itemNames != null;
	}
	/**
	 * Create the application.
	 */
	public App() {
		
		initialize();
		getContentPane().setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 0, 434, 262);
		getContentPane().add(tabbedPane);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Statistics", null, panel, null);
		panel.setLayout(null);
		
		textPane = new JTextPane();
		textPane.setBounds(10, 11, 409, 212);
		panel.add(textPane);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Settings", null, panel_1, null);
		panel_1.setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(10, 37, 409, 39);
		panel_1.add(textField);
		textField.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Items (name1, name2, ...) :");
		lblNewLabel.setBounds(10, 11, 409, 20);
		panel_1.add(lblNewLabel);
		
		JButton btnApply = new JButton("Apply");
		btnApply.setForeground(Color.BLACK);
		btnApply.setBounds(330, 200, 89, 23);
		panel_1.add(btnApply);
		
		btnApply.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				itemNames = textField.getText();
			}
		});
		
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		setTitle("NGAutoAlcher");
		setBounds(100, 100, 450, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
