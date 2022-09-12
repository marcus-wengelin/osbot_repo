package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.osbot.utility.Logger;

import constants.Ore;

import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import java.awt.Choice;
import java.awt.Button;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;
import java.awt.Component;

public class App extends JFrame {
	public static class ExceptionHandler implements Thread.UncaughtExceptionHandler {
		public Logger logger;
	    public void uncaughtException(Thread thread, Throwable thrown) {
	    	if (thrown instanceof IllegalArgumentException && thrown.getStackTrace()[0].getMethodName().equals("getSelectedText")) {
	    		// Ignore exceptions in `getSelectedText`
	    	} else {
	    		logger.error(thrown);
	    	}
	    }
	}
	
	private static final long serialVersionUID = 4503120621888717390L;
	public static final int DROP_ALL = 1;
	public static final int DROP_ORE = 2;
	public static final int DEPO_ORE = 3;
	
	@SuppressWarnings("unused")
	private Logger logger;
	
	private Ore targetOre = null;
	private int dropBehaviour = 0;
	private boolean noMove = false;
	
	private Choice oreChoice;
	
	private JPanel contentPane;
	private JTextField tfRuntime;
	private JTextField tfLevel;
	private JTextField tfXpGains;
	private JTextField tfXpGainsH;

	/**
	 * Launch the application.
	 */
	public static App startApp(Logger logger) {
		App frame = new App(logger);
		frame.setVisible(true);
		return frame;		
	}
	
	public void setStatistics(String runtime, String level,
							   String xpGains, String xpGainsH) {
		try {
			tfRuntime.setText(runtime);
			tfLevel.setText(level);
			tfXpGains.setText(xpGains);
			tfXpGainsH.setText(xpGainsH);
		} catch (IllegalArgumentException e) {}
	}
	
	private void updateTargetOre(String oreName) {
		for (Ore o : Ore.values()) {
			if (oreName.equals(o.getName())) {
				targetOre = o;
				return;
			}
		}
		targetOre = null;
	}
	
	private void updateDropBehaviour(int dropBehaviour) {
		this.dropBehaviour = dropBehaviour;
	}
	
	private void updateNoMove(boolean noMove) {
		this.noMove = noMove;
	}
	
	public Ore getTargetOre() {
		return targetOre;
	}
	
	public int getDropBehaviour() {
		return dropBehaviour;
	}
	
	public boolean getNoMove() {
		return noMove;
	}

	private void addStatisticsComponents(JPanel statisticsPanel) {
		/* Total runtime */
		JLabel lblRuntime = new JLabel("Runtime: ");
		statisticsPanel.add(lblRuntime);
		tfRuntime = new JTextField();
		tfRuntime.setHighlighter(null);
		tfRuntime.setHorizontalAlignment(JTextField.CENTER);
		statisticsPanel.add(tfRuntime);
		tfRuntime.setColumns(60);
		
		/* Total level and levels gained */
		JLabel lblMiningLeve = new JLabel("Mining lvl:");
		statisticsPanel.add(lblMiningLeve);
		tfLevel = new JTextField();
		tfLevel.setHorizontalAlignment(JTextField.CENTER);
		tfLevel.setHighlighter(null);
		statisticsPanel.add(tfLevel);
		tfLevel.setColumns(60);
		
		/* Total XP gains */
		JLabel lblXpGained = new JLabel("XP gains:");
		statisticsPanel.add(lblXpGained);
		tfXpGains = new JTextField();
		tfXpGains.setHorizontalAlignment(JTextField.CENTER);
		tfXpGains.setHighlighter(null);
		statisticsPanel.add(tfXpGains);
		tfXpGains.setColumns(60);
		
		/* Total XP/h gained */
		JLabel lblXph = new JLabel("XP/h:      ");
		statisticsPanel.add(lblXph);
		tfXpGainsH = new JTextField();
		tfXpGainsH.setHorizontalAlignment(JTextField.CENTER);
		tfXpGainsH.setHighlighter(null);
		statisticsPanel.add(tfXpGainsH);
		tfXpGainsH.setColumns(60);
	}
	
	/**
	 * Create the frame.
	 */
	public App(Logger logger) {
		this.logger = logger;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 510, 360);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		JPanel statisticsPanel = new JPanel();
		tabbedPane.addTab("Statistics", null, statisticsPanel, null);
		
		addStatisticsComponents(statisticsPanel);
		
		JPanel settingsPanel = new JPanel();
		tabbedPane.addTab("Settings", null, settingsPanel, null);
		settingsPanel.setLayout(new BoxLayout(settingsPanel, FlowLayout.LEADING));
		
		JLabel lblOreChoice = new JLabel("Target ore:");
		lblOreChoice.setVerticalAlignment(SwingConstants.TOP);
		lblOreChoice.setHorizontalAlignment(SwingConstants.LEFT);
		settingsPanel.add(lblOreChoice);
		
		oreChoice = new Choice();
		for (Ore o : Ore.values()) {
			if (!"None".equals(o.getName()))
				oreChoice.add(o.getName());
		}
		settingsPanel.add(oreChoice);
		
		JRadioButton rdbtnDropOre = new JRadioButton("Drop ore");
		rdbtnDropOre.setHorizontalAlignment(SwingConstants.LEFT);
		rdbtnDropOre.setAlignmentY(Component.TOP_ALIGNMENT);
		JRadioButton rdbtnDropAll = new JRadioButton("Drop all");
		rdbtnDropAll.setHorizontalAlignment(SwingConstants.LEFT);
		rdbtnDropAll.setAlignmentY(Component.TOP_ALIGNMENT);
		rdbtnDropAll.setVerticalAlignment(SwingConstants.TOP);
		JRadioButton rdbtnDeposit = new JRadioButton("Deposit ore");
		rdbtnDeposit.setHorizontalAlignment(SwingConstants.LEFT);
		rdbtnDeposit.setAlignmentY(Component.TOP_ALIGNMENT);

		
		JToggleButton tglbtnDontMove = new JToggleButton("No move");
		tglbtnDontMove.setHorizontalAlignment(SwingConstants.LEFT);
		tglbtnDontMove.setAlignmentY(Component.TOP_ALIGNMENT);
		tglbtnDontMove.setVerticalAlignment(SwingConstants.TOP);
		settingsPanel.add(tglbtnDontMove);
		
		JLabel lblDropBehaviour = new JLabel("Drop behaviour:");
		lblDropBehaviour.setHorizontalAlignment(SwingConstants.LEFT);
		lblDropBehaviour.setAlignmentY(Component.TOP_ALIGNMENT);
		settingsPanel.add(lblDropBehaviour);
		settingsPanel.add(rdbtnDropOre);
		settingsPanel.add(rdbtnDropAll);
		settingsPanel.add(rdbtnDeposit);
		rdbtnDropOre.setSelected(true);	
		
		ButtonGroup dropBehaviour = new ButtonGroup();
		dropBehaviour.add(rdbtnDropOre);
		dropBehaviour.add(rdbtnDropAll);
		dropBehaviour.add(rdbtnDeposit);
		
		Button button = new Button("Apply");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateTargetOre(oreChoice.getSelectedItem());
				if (rdbtnDropAll.isSelected())
					updateDropBehaviour(DROP_ALL);
				else if (rdbtnDropOre.isSelected())
					updateDropBehaviour(DROP_ORE);
				else if (rdbtnDeposit.isSelected()) {
					updateDropBehaviour(DEPO_ORE);
				}
				
				if (tglbtnDontMove.isSelected()) {
					updateNoMove(true);
				}
			}
		});
		
		settingsPanel.add(button);
		
		if (SwingUtilities.isEventDispatchThread()) {
    		ExceptionHandler h = new ExceptionHandler();
    		h.logger = logger;
        	Thread.currentThread().setUncaughtExceptionHandler(h);
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
			    	public void run() {
			    		ExceptionHandler h = new ExceptionHandler();
			    		h.logger = logger;
			        	Thread.currentThread().setUncaughtExceptionHandler(h);
			    	}
				});
			} catch (InvocationTargetException e1) {
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
		
	}

}
