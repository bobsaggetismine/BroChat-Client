package Client;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;

public class ClientCreateAccount extends JFrame{

	private static final long serialVersionUID = 1L;
	private JTextField txtUser;
	private JLabel lblPassword;
	private JPasswordField txtPassword;
	private JButton btnCreate;
	private JLabel lblUsername;
	
	public void create() {
		Client client = new Client(txtUser.getText(), txtPassword.getPassword(),false,null);
		String password = new String(txtPassword.getPassword());
		client.send("/cu/" + txtUser.getText() + "/n/" + password);
		JLabel label = new JLabel(client.waitForCreateReply());
		JOptionPane.showMessageDialog(null,label,"Status", JOptionPane.PLAIN_MESSAGE);
		client.getSocket().close();
		dispose();
	}
	
	public void initialize() {
		setResizable(false);
		setBounds(100, 100, 277, 342);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);
		setTitle("Create");
		btnCreate = new JButton("Create");
		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				create();
			}
		});
		btnCreate.setBounds(87, 245, 97, 25);
		getContentPane().add(btnCreate);

		txtUser = new JTextField();
		txtUser.setBounds(77, 108, 116, 22);
		getContentPane().add(txtUser);
		txtUser.setColumns(10);

		lblUsername = new JLabel("Username:");
		lblUsername.setBounds(96, 70, 79, 16);
		getContentPane().add(lblUsername);

		lblPassword = new JLabel("Password:");
		lblPassword.setBounds(96, 143, 79, 16);
		getContentPane().add(lblPassword);

		txtPassword = new JPasswordField();
		txtPassword.setBounds(77, 172, 116, 22);

		txtPassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				create();
			}

		});
		getContentPane().add(txtPassword);
		setVisible(true);
	}
	
}
