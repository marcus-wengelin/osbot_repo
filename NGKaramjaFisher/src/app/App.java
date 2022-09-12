
package app;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JSeparator;
import javax.swing.JRadioButton;

public class App extends JFrame {
	private String fishingMethod = "";
	private boolean powerFishing = false;
	
	private static final long serialVersionUID = 3701225696297079485L;

	private Label labelRuntime;
	private Label labelXpGained;
	private Label labelXpPerHour;
	private Label labelFishingLevel;
	private Label labelFishGained;

	public void setStatistics(String runtime, String xpGained, String xpPerHour, String fishingLevel, String fishGained) {
		labelRuntime.setText(runtime);
		labelXpGained.setText(xpGained);
		labelXpPerHour.setText(xpPerHour);
		labelFishingLevel.setText(fishingLevel);
		labelFishGained.setText(fishGained);
		
		labelRuntime.repaint();
		labelXpGained.repaint();
		labelXpPerHour.repaint();
		labelFishingLevel.repaint();
		labelFishGained.repaint();
	}
	
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

	public String getFishingMethod() {
		return fishingMethod;
	}
	public boolean getPowerFishing() {
		return powerFishing;
	}
	public void updateFishingMethod(String method) {
		fishingMethod = method;
	}
	public void updatePowerFishing(boolean b) {
		powerFishing = b;
	}
	
	/**
	 * Create the application.
	 */
	public App() {
		initialize();
	}

	public void initializeSettingsPane(JTabbedPane tabbedPane) {
		JPanel panel = new JPanel();
		tabbedPane.addTab("Settings", null, panel, null);
		panel.setLayout(null);
		
		JToggleButton tglbtnPowerfish = new JToggleButton("PowerFish");
		tglbtnPowerfish.setToolTipText("enable/disable powerfishing");
		tglbtnPowerfish.setBounds(10, 11, 121, 23);
		panel.add(tglbtnPowerfish);
		
		Label label = new Label("Fishing method:");
		label.setBounds(10, 47, 159, 22);
		panel.add(label);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 45, 159, 2);
		panel.add(separator);
		
		ButtonGroup group = new ButtonGroup();
		JRadioButton rdbtnCage = new JRadioButton("Cage");
		rdbtnCage.setBounds(22, 75, 174, 23);
		rdbtnCage.setSelected(true);
		panel.add(rdbtnCage);
		group.add(rdbtnCage);
		
		JRadioButton rdbtnHarpoon = new JRadioButton("Harpoon");
		rdbtnHarpoon.setBounds(22, 101, 174, 23);
		panel.add(rdbtnHarpoon);
		group.add(rdbtnHarpoon);
		
		JButton btnApply = new JButton("Apply");
		btnApply.setBounds(10, 178, 89, 23);
		btnApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String method = rdbtnCage.isSelected() ? "Cage" : "Harpoon";
				updateFishingMethod(method);
				updatePowerFishing(tglbtnPowerfish.isSelected());
			}
		});
		panel.add(btnApply);
	}
	
	public void initializeStatsPane(JTabbedPane tabbedPane) {
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Statistics", null, panel_1, null);
		panel_1.setLayout(null);
		
		Label label_2 = new Label("gained xp:");
		label_2.setBounds(10, 40, 100, 20);
		panel_1.add(label_2);
		
		Label label_3 = new Label("xp/hour:");
		label_3.setBounds(10, 60, 100, 20);
		panel_1.add(label_3);
		
		Label label_4 = new Label("fishing level:");
		label_4.setBounds(10, 80, 100, 20);
		panel_1.add(label_4);
		
		Label label_5 = new Label("fish gained:");
		label_5.setBounds(10, 100, 100, 20);
		panel_1.add(label_5);
		
		Label label_6 = new Label("runtime:");
		label_6.setBounds(10, 0, 62, 22);
		panel_1.add(label_6);
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setBounds(0, 28, 200, 2);
		panel_1.add(separator_2);
		
		labelRuntime = new Label("_runtime");
		labelRuntime.setBounds(130, 2, 119, 20);
		panel_1.add(labelRuntime);
		
		labelXpGained = new Label("_xpgains");
		labelXpGained.setBounds(130, 40, 119, 20);
		panel_1.add(labelXpGained);
		
		labelXpPerHour = new Label("_xpperh");
		labelXpPerHour.setBounds(130, 60, 119, 20);
		panel_1.add(labelXpPerHour);
		
		labelFishingLevel = new Label("_fishlvl");
		labelFishingLevel.setBounds(130, 80, 119, 20);
		panel_1.add(labelFishingLevel);
		
		labelFishGained = new Label("_fishgain");
		labelFishGained.setBounds(130, 100, 119, 20);
		panel_1.add(labelFishGained);
		
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		setTitle("NGKaramjaFisher");
		setBounds(100, 100, 300, 350);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 11, 264, 260);
		getContentPane().add(tabbedPane);
		
		initializeSettingsPane(tabbedPane);
		initializeStatsPane(tabbedPane);
	}	
}
