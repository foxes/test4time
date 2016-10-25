package package1;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class GUI extends JFrame{

	private JPanel mainPanel;
	private JPanel topPanel;
	private JPanel toptopPanel;
	private JPanel topBottomPanel;
	private JPanel centerPanel;
	private JPanel centerTopPanel;
	private JPanel centerBottomPanel;
	private JPanel bottomPanel;
	private JPanel bottomTopPanel;
	private JPanel bottomBottomPanel;
	
	private JLabel serverHostnameLabel;
	private JTextField serverHostnameField;
	private JLabel portLabel;
	private JTextField portField;
	private JLabel usernameLabel;
	private JTextField usernameField;
	private JLabel hostnameLabel;
	private JTextField hostnameField;
	private JLabel speedLabel;
	private JComboBox speedComboBox;
	private String[] speedList = {"Modem", "Ethernet", "T1", "T3"};
	private JButton connectButton;
	
	private JLabel keywordLabel;
	private JTextField keywordField;
	private JButton searchButton;
	
	private JLabel enterCommandLabel;
	private JTextField enterCommandField;
	private JButton goButton;
	private JTextArea ftpArea;
	private JScrollPane ftpAreaScrollPane;
	
	public GUI(){
		
		mainPanel = new JPanel();
		mainPanel.setOpaque(true);
		mainPanel.setBackground(Color.DARK_GRAY);
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		mainPanel.setLayout(new BorderLayout(5, 5));
		
		

		//Top Panel Components
		topPanel = new JPanel(new BorderLayout());
		topPanel.setOpaque(true);
		topPanel.setBackground(Color.WHITE);
		toptopPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		toptopPanel.setSize(200, 500);
		toptopPanel.setOpaque(true);
		toptopPanel.setBackground(Color.WHITE);
		topBottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		topBottomPanel.setOpaque(true);
		topBottomPanel.setBackground(Color.WHITE);
		topPanel.setBorder(BorderFactory.createTitledBorder("Connection"));
		serverHostnameLabel = new JLabel("Server Hostname:");
		serverHostnameField = new JTextField(10);
		portLabel = new JLabel("Port:");
		portField = new JTextField(5);
		connectButton = new JButton("Connect");	
		usernameLabel = new JLabel("Username:");
		usernameField = new JTextField(10);
		hostnameLabel = new JLabel("Hostname:");
		hostnameField = new JTextField(10);
		speedLabel = new JLabel("Speed:");
		speedComboBox = new JComboBox(speedList);
		toptopPanel.add(serverHostnameLabel);
		toptopPanel.add(serverHostnameField);
		toptopPanel.add(portLabel);
		toptopPanel.add(portField);
		toptopPanel.add(connectButton);
		topBottomPanel.add(usernameLabel);
		topBottomPanel.add(usernameField);
		topBottomPanel.add(hostnameLabel);
		topBottomPanel.add(hostnameField);
		topBottomPanel.add(speedLabel);
		topBottomPanel.add(speedComboBox);
		topPanel.add(BorderLayout.PAGE_START, toptopPanel);
		topPanel.add(BorderLayout.PAGE_END, topBottomPanel);
		
		//Center Panel Components
		centerTopPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		centerBottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		centerTopPanel.setOpaque(true);
		centerBottomPanel.setOpaque(true);
		centerTopPanel.setBackground(Color.WHITE);
		centerBottomPanel.setBackground(Color.WHITE);
		centerPanel = new JPanel(new BorderLayout());
		centerPanel.setOpaque(true);
		centerPanel.setBackground(Color.WHITE);		
		centerPanel.setBorder(BorderFactory.createTitledBorder("Search"));
		keywordLabel = new JLabel("Keyword:");
		keywordField = new JTextField(15);
		searchButton = new JButton("Search");
		searchButton.setEnabled(false);
		centerTopPanel.add(keywordLabel);
		centerTopPanel.add(keywordField);
		centerTopPanel.add(searchButton);
		
		centerPanel.add(BorderLayout.NORTH, centerTopPanel);
		centerPanel.add(BorderLayout.SOUTH, centerBottomPanel);
		
		//Bottom Panel Components
		bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.setOpaque(true);
		bottomPanel.setBackground(Color.WHITE);
		bottomPanel.setBorder(BorderFactory.createTitledBorder("FTP"));
		bottomTopPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		bottomTopPanel.setOpaque(true);
		bottomTopPanel.setBackground(Color.WHITE);
		bottomBottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		bottomBottomPanel.setOpaque(true);
		bottomBottomPanel.setBackground(Color.WHITE);
		enterCommandLabel = new JLabel("Enter Command:");
		enterCommandField = new JTextField(30);
		goButton = new JButton("Go");
		ftpArea = new JTextArea(10, 50);
		ftpArea.setBackground(Color.LIGHT_GRAY);
		ftpArea.setEditable(false);
		ftpAreaScrollPane = new JScrollPane(ftpArea);
		bottomTopPanel.add(enterCommandLabel);
		bottomTopPanel.add(enterCommandField);
		bottomTopPanel.add(goButton);
		bottomBottomPanel.add(ftpArea);
		bottomPanel.add(BorderLayout.NORTH, bottomTopPanel);
		bottomPanel.add(BorderLayout.SOUTH, bottomBottomPanel);
		
		
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));		
		
		add(mainPanel);
		mainPanel.add(topPanel);
		mainPanel.add(centerPanel);
		mainPanel.add(bottomPanel);
		
		setTitle("GV-NAP File Sharing System");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		//setSize(1000,1000);
		pack();
		setVisible(true);
	}
}
