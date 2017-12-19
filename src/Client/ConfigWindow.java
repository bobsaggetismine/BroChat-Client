package Client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import Utils.FileIO;

import javax.swing.JCheckBox;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.security.spec.EncodedKeySpec;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JPasswordField;

public class ConfigWindow {

	private JFrame frame;
	private Config config;
	private JCheckBox cbAutoScroll,cbLog;
	private JTextField tbLogPath;
	private JPasswordField pwdKey;
	private JLabel lblScroll,lblLog,lblEncryptionKey;
	private JButton btnSave;
	
	public ConfigWindow(Config config)
	{
		this.config = config;
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setTitle("Properties");
		
		cbAutoScroll = new JCheckBox();
		cbAutoScroll.setBounds(145, 8, 129, 23);
		cbLog = new JCheckBox("Log");
		cbLog.setBounds(145, 60, 83, 25);
		
		lblScroll = new JLabel("AutoScroll: ");
		lblScroll.setBounds(60,15,140,15);
		lblLog = new JLabel("Log: ");
		lblLog.setBounds(60, 64, 140, 15);
		lblEncryptionKey = new JLabel("Encryption Key:");
		lblEncryptionKey.setBounds(57, 39, 140, 15);
		
		tbLogPath = new JTextField("log path");
		tbLogPath.setBounds(169, 60, 83, 25);
		pwdKey = new JPasswordField();
		pwdKey.setBounds(169, 39, 83, 19);
		
		btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent arg0) {saveConfig();}});
		btnSave.setBounds(165, 233, 117, 25);
		
		frame.getContentPane().add(lblScroll);
		frame.getContentPane().add(tbLogPath);
		frame.getContentPane().add(lblLog);
		frame.getContentPane().add(cbLog);
		frame.getContentPane().add(cbAutoScroll);
		frame.getContentPane().add(btnSave);
		frame.getContentPane().add(lblEncryptionKey);
		frame.getContentPane().add(pwdKey);
		frame.setVisible(true);
		
		if (config.autoScroll) cbAutoScroll.setSelected(true);
		if (config.LOG)
		{
			cbLog.setSelected(true);
			tbLogPath.setText(config.logPath);
		}
	}
	private void saveConfig()
	{
		String configText = "";
		
		if (cbAutoScroll.isSelected())
		{
			configText += "scroll = true"+System.lineSeparator();
			config.autoScroll = true;
		}
		else
		{
			configText += "scroll = false"+System.lineSeparator();
			config.autoScroll = false;
		}
		if (cbLog.isSelected())
		{
			configText += "log = true"+System.lineSeparator();
			configText+= "logPath = "+tbLogPath.getText() + System.lineSeparator();
		}
		config.encryptionKey = new String(pwdKey.getPassword());
		config.logPath = new String(tbLogPath.getText());
		FileIO.writeToFile("config.cfg", configText);
		JOptionPane.showMessageDialog(frame, "Changes saved.");
		frame.dispose();
	}
}
