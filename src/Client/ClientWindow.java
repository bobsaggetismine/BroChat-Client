package Client;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class ClientWindow extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtData;
	private JTextArea history;
	private JScrollPane scroll;
	
	private Client client;
	
	private Config config;
	
	private JMenuBar menuBar;
	
	private JMenu settingsMenu;
	
	public ClientWindow(String name, Client client, Config config) {
		this.client = client;
		this.config = config;
		initWindow();
	}
	
	public void initWindow(){
		setResizable(false);
		setTitle("Client");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800,800);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		menuBar = new JMenuBar();
		
		settingsMenu = new JMenu("Settings");
		
		JMenuItem properties = new JMenuItem("Properties");
		properties.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				new ConfigWindow(config);
			}
			
		});
		settingsMenu.add(properties);
		menuBar.add(settingsMenu);
		menuBar.setBackground(Color.gray);
		this.setJMenuBar(menuBar);
		
		history = new JTextArea(5,20);
		history.setEditable(false);
		//history.setBounds(12, 13, 770, 650);
		scroll = new JScrollPane(history);
		scroll.setVisible(true);
		scroll.setBounds(12,12, 770, 650);
		contentPane.add(scroll);
		txtData = new JTextField();
		txtData.setBounds(12, 705, 649, 22);
		txtData.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER){
					if (!txtData.getText().equals("")){
						client.send(txtData.getText());
						txtData.setText("");
						txtData.requestFocusInWindow();
						if (config.autoScroll)
						scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getMaximum());
					}
				}
			}
		});
		
		contentPane.add(txtData);
		txtData.setColumns(10);
		
		JButton btnSend = new JButton("send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (!txtData.getText().equals("")){

					client.send(txtData.getText());
					txtData.setText("");
					txtData.requestFocusInWindow();
					if (config.autoScroll)
					scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getMaximum());
				}
			}
		});
		btnSend.setBounds(685, 704, 97, 25);
		contentPane.add(btnSend);
		setVisible(true);
		txtData.requestFocusInWindow();
		this.addWindowListener(new java.awt.event.WindowAdapter() {
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		       client.send("/d/"+client.getId()+"/e/");
		    }
		});
	}
	public void writeConsole(String msg){
		if (config.autoScroll)
		scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getMaximum());
		history.append(msg);
	}
	public void clearConsole(){
		history.setText("");
	}
	
	public JTextArea getHistory(){
		return history;
	}
}
