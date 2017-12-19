package Client;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.security.Security;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import Utils.FileIO;

public class LoginWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtName;
	private JPasswordField txtPass;
	
	private JCheckBox cbRememberMe;
	public static void main(String[] args) {
		Security.setProperty("crypto.policy", "unlimited");
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginWindow frame = new LoginWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	public LoginWindow() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		} 
		setTitle("Login");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 320, 285);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		cbRememberMe = new JCheckBox();
		cbRememberMe.setText("Remember me");
		cbRememberMe.setBounds(200,43, 116, 22);
		contentPane.add(cbRememberMe);
		txtName = new JTextField();
		String username = FileIO.readFile("user.txt");
		if (!username.equals("")){
			txtName.setText(username);
			cbRememberMe.setSelected(true);
		}
		txtName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
				login();
			}
		});
		txtName.setBounds(87, 43, 116, 22);
		contentPane.add(txtName);
		txtName.setColumns(10);
		
		JLabel lblName = new JLabel("Name");
		lblName.setBounds(127, 13, 35, 16);
		contentPane.add(lblName);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				login();
			}
		});
		btnLogin.setBounds(87, 212, 116, 25);
		contentPane.add(btnLogin);
		
		txtPass = new JPasswordField();
		txtPass.setBounds(87, 108, 116, 22);
		contentPane.add(txtPass);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(112, 78, 66, 16);
		contentPane.add(lblPassword);
		
		
		JButton btnCreateAccount = new JButton("Create Account");
		btnCreateAccount.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ClientCreateAccount creator = new ClientCreateAccount();
				creator.initialize();
			}
		});
		btnCreateAccount.setBounds(81, 166, 127, 25);
		contentPane.add(btnCreateAccount);
		txtPass.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				login();
			}
		});
		
		
	}
	public void login(){
		saveUser();
		Config config = new Config("config.cfg");
		dispose();
		String name;
		char[] password = txtPass.getPassword();
		name  = txtName.getText();
		new Client(name,password,true,config);
	}
	public void saveUser(){
		if (cbRememberMe.isSelected()){
			FileIO.writeToFile("user.txt",txtName.getText());
		}else {
			FileIO.writeToFile("user.txt", "");
		}
	}
}
