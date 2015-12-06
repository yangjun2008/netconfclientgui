package com.anonymous.tool.netconfclient.ui;

import java.awt.EventQueue;
import java.util.Locale;

import javax.swing.JFrame;

public class NetConfGUIClient {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		Locale.setDefault(new Locale("en", "US"));  
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NetConfGUIClient window = new NetConfGUIClient();
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
	public NetConfGUIClient() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("NetConf GUI Client");
		frame.setBounds(100, 100, 800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(new MainPanel());
	}

}
