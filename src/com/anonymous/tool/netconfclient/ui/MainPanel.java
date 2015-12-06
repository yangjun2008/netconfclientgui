package com.anonymous.tool.netconfclient.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import com.anonymous.tool.netconfclient.NetConfClient;
import com.anonymous.tool.netconfclient.execption.LoginFailedException;
import com.anonymous.tool.netconfclient.execption.RequestSendFailedException;

@SuppressWarnings("serial")
public class MainPanel extends JPanel {
	
	private TitledBorder loginPanelBorder;
	private JTextField serverInputField;
	private JTextField userInputField;
	private JPasswordField passwordInputField;
	private JTextField portInputField;
	private JButton loginButton;
	private JButton sendButton;
	private JTextArea requestArea;
	private JTextArea responseArea;
	
	
	private NetConfClient client;
	/**
	 * Create the panel.
	 */
	public MainPanel() {
		client = new NetConfClient();
		initUI();
	}
	
	private void initUI() {
		
		this.setBorder(new EmptyBorder(10, 10, 10, 10));
		this.setLayout(new BorderLayout());
		
		JPanel loginPanel = new JPanel();
		initLoginPanelUI(loginPanel);
		
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BorderLayout());
		
		initRequestPanelUI(centerPanel);
		initResponsePanelUI(centerPanel);
		
		JPanel operationPanel = new JPanel();
		initOperationPanelUI(operationPanel);
		
		this.add(loginPanel, BorderLayout.NORTH);
		this.add(centerPanel, BorderLayout.CENTER);
		this.add(operationPanel, BorderLayout.EAST);
		
		
		
	}
	
	private void initLoginPanelUI(JPanel container) {
		loginPanelBorder = BorderFactory.createTitledBorder("Please login first");

		container.setBorder(loginPanelBorder);
		
		JLabel serverInputLabel = new JLabel("Server IP: ");
		JLabel userInputLabel = new JLabel("User Name: ");
		JLabel passwordLabel = new JLabel("Password: ");
		JLabel portLabel = new JLabel("Port: ");
		
		serverInputField = new JTextField();		
		userInputField = new JTextField();		
		passwordInputField = new JPasswordField();
		portInputField = new JTextField();
		serverInputField.setPreferredSize(new Dimension(120, 24));
		userInputField.setPreferredSize(new Dimension(120, 24));
		passwordInputField.setPreferredSize(new Dimension(120, 24));
		portInputField.setPreferredSize(new Dimension(60, 24));
		
		loginButton = new JButton("Login");
		loginButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				loginButtonActionPerformed(serverInputField.isEnabled());
			}
			
		});
		loginButton.setPreferredSize(new Dimension(100, 24));
		
		GridBagLayout loginPanelLayout = new GridBagLayout(); 
		container.setLayout(loginPanelLayout);
		container.add(serverInputLabel);
		container.add(serverInputField);
		
		container.add(userInputLabel);
		container.add(userInputField);
		
		container.add(passwordLabel);
		container.add(passwordInputField);
		
		container.add(portLabel);
		container.add(portInputField);
		
		container.add(loginButton);
		GridBagConstraints loginPanelLayoutConstraints = new GridBagConstraints(); 
		loginPanelLayoutConstraints.fill = GridBagConstraints.HORIZONTAL; 
		
		loginPanelLayoutConstraints.gridwidth=1; 
		loginPanelLayoutConstraints.weightx = 0; 
		loginPanelLayoutConstraints.weighty=0; 
		loginPanelLayoutConstraints.insets = new Insets(5,5,5,5);
		loginPanelLayout.setConstraints(serverInputLabel, loginPanelLayoutConstraints); 
		loginPanelLayout.setConstraints(userInputLabel, loginPanelLayoutConstraints); 
		loginPanelLayout.setConstraints(passwordLabel, loginPanelLayoutConstraints); 
		loginPanelLayout.setConstraints(loginButton, loginPanelLayoutConstraints); 
		
		loginPanelLayoutConstraints.weightx = 1; 
		loginPanelLayoutConstraints.weighty=1; 
		loginPanelLayout.setConstraints(serverInputField, loginPanelLayoutConstraints); 
		loginPanelLayout.setConstraints(userInputField, loginPanelLayoutConstraints); 
		loginPanelLayout.setConstraints(passwordInputField, loginPanelLayoutConstraints); 
		loginPanelLayoutConstraints.weightx = 0.5; 
		loginPanelLayout.setConstraints(portInputField, loginPanelLayoutConstraints); 
	}
	
	private void initRequestPanelUI(JPanel parent) {
		JPanel requestPanel = new JPanel();
		TitledBorder requestPanelBorder = BorderFactory.createTitledBorder("Input the request");

		requestPanel.setBorder(requestPanelBorder);
		
		requestArea = new JTextArea();
		requestArea.setLineWrap(true);
		requestArea.setWrapStyleWord(true);
		
		requestPanel.setLayout(new BorderLayout());
		requestPanel.add(new JScrollPane(requestArea), BorderLayout.CENTER);
		requestPanel.setPreferredSize(new Dimension(0, 240));
		parent.add(requestPanel, BorderLayout.NORTH);
	}
	
	private void initResponsePanelUI(JPanel parent) {
		JPanel responsePanel = new JPanel();
		TitledBorder responsePanelBorder = BorderFactory.createTitledBorder("Response");

		responsePanel.setBorder(responsePanelBorder);
		
		responseArea = new JTextArea();
		responseArea.setLineWrap(true);
		responseArea.setWrapStyleWord(true);
		responseArea.setEditable(false);
		responsePanel.setLayout(new BorderLayout());
		responsePanel.add(new JScrollPane(responseArea), BorderLayout.CENTER);
		
		parent.add(responsePanel, BorderLayout.CENTER);
	}
	
	private void initOperationPanelUI(JPanel container) {
		
		sendButton = new JButton("Send");
		sendButton.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				sendButtonActionPerformed();
			}
			
		});
		sendButton.setPreferredSize(new Dimension(80, 24));
		sendButton.setEnabled(false);
		container.setLayout(new FlowLayout(FlowLayout.LEADING, 15, 10));
		container.add(sendButton);
	}
	
	private void loginButtonActionPerformed(boolean isLogin) {
		try {
			if(isLogin) {
				client.login(serverInputField.getText().trim(),userInputField.getText().trim(), String.valueOf(passwordInputField.getPassword()),
						portInputField.getText());
				loginButton.setText("Logout");
				serverInputField.setEnabled(false);
				userInputField.setEnabled(false);
				passwordInputField.setEnabled(false);
				sendButton.setEnabled(true);
				loginPanelBorder.setTitle("User logined");
			}
			else {
				client.logout();
				loginButton.setText("Login");
				serverInputField.setEnabled(true);
				userInputField.setEnabled(true);
				passwordInputField.setEnabled(true);
				sendButton.setEnabled(false);
				loginPanelBorder.setTitle("Please login first");
			}
			this.updateUI();
		} catch (LoginFailedException ex) {
			JOptionPane.showMessageDialog(MainPanel.this.getRootPane(), "Login failed", ex.getMessage(), JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void sendButtonActionPerformed() {
		try {
			String result = client.send(requestArea.getText().trim());
			requestArea.setText("");
			responseArea.setText(result);
			this.updateUI();
		} catch (RequestSendFailedException ex) {
			JOptionPane.showMessageDialog(MainPanel.this.getRootPane(), "Send failed", ex.getMessage(), JOptionPane.ERROR_MESSAGE);
		}
	}

}
