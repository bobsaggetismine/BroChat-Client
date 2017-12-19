package Client;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import Utils.FileIO;
import Utils.Security;

public class Client {
	
	public static final int DEFAULT_PORT = 8192;
	
	//connection
	private DatagramSocket socket;
	
	//data about connection
	private int port;
	private String name;
	private InetAddress IP;
	private int id;
	private String password;
	
	//threads
	private Thread recieveThread;
	
	//window
	private ClientWindow window;
	
	//debugging
	private boolean debug = false;
	
	//is this an active client, or used for creating accounts?
	private boolean listening = true;
	
	private Config config;
	
	public Client(String name,char[] password,boolean establishConnection, Config config){
		this.name=name;
		this.password = "";
		this.config = config;
		listening = establishConnection;
		this.port = Client.DEFAULT_PORT;
		
		try {
			socket = new DatagramSocket();
			IP = InetAddress.getByName("192.168.0.13");
			for (char c : password) this.password+=c;
			if (listening){
				this.window = new ClientWindow(name,this,config);
				if (this.password == "") this.password = " ";
				send("/c/"+name+"/n/"+this.password);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		recieve();
	}
	
	//active client methods
	private String proccessMessage(String msg) {
		if (msg.equals("/cls")){
			window.clearConsole();
			return null;
		}else if(msg.equals("/u")){
			msg = "/u/"+id+"/e/";
		}else if (msg.equals("/id") && debug){
			window.writeConsole("Note, don't give away your ID! ID: "+id+'\n');
			return null;
		}else if (msg.equals("/id") && !debug){
			window.writeConsole("Enter debugging to enable\n");
			return null;
		}else if (msg.equals("/help")){
			window.writeConsole("/u for list of connected users\n");
			return null;
		}else if (msg.equals("/debug")){
			debug = true;
			return null;
		}else if (msg.startsWith("/kick")){
			window.writeConsole(msg+"\n");
			msg = "/k/" + msg.substring(6) + "/n/" + name +"/n/"+password+"/e/";
		}else if (msg.startsWith("/ban")){
			msg = "/b/" + msg.substring(5) + "/n/" + name +"/n/"+password+"/e/";
		}else if (msg.startsWith("/promote")){
			msg = "/p/" + msg.substring(9) + "/n/" + name +"/n/"+password+"/e/";
		}else if (msg.startsWith("/demote")){
			msg = "/l/" + msg.substring(8) + "/n/" + name +"/n/"+password+"/e/";
		}
		if (!msg.startsWith("/c/") && !msg.startsWith("/d/") && !msg.startsWith("/u/") && !msg.startsWith("/cu/") && !msg.startsWith("/k/") && !msg.startsWith("/b/") && !msg.startsWith("/p/")&& !msg.startsWith("/l/"))
			msg = "/m/"+ Security.encrypt(config.encryptionKey, msg)+"/n/"+id+"/n/"+name+"/e/";
		return msg;
	}
	private void recieve(){

		recieveThread = new Thread("Client recieve"){
			public void run(){
				while (listening){
				byte[] data = new byte[1024];
				DatagramPacket packet = new DatagramPacket(data, data.length);
				try {
					socket.receive(packet);
				} catch (IOException e) {
					e.printStackTrace();
				}
				String msg = new String(packet.getData(), packet.getOffset(), packet.getLength());
				handleReply(msg);
				}
			}
		};
		recieveThread.start();
	}
	private void handleReply(String msg){
		if (msg.startsWith("/c/")){
			id = Integer.parseInt(msg.split("/c/|/e/")[1]);
			window.writeConsole("Succesfully established a connection to server."+"\n");
			window.setTitle("Connected to :"+IP.toString() + " on port: "+port+" as: "+name);
		}else if (msg.startsWith("/m/")){
			String name = msg.split("/m/|/n/|/e/")[1];
			String message = msg.split("/m/|/n/|/e/")[2];
			
			try{
				window.writeConsole(name+": "+Security.decrypt(config.encryptionKey, message)+"\n");
			}catch(Exception e){
				window.writeConsole(name+": "+message+"\n");
			}
			if (config.LOG)
			{
				try {
					FileIO.log(config.logPath, name+": "+Security.decrypt(config.encryptionKey, message)+ "\n");
				} catch (Exception e) {
					FileIO.log(config.logPath, name+": "+message+"\n");
				}
			}
			
		}else{
			window.writeConsole(msg+'\n');
		}
	}
	
	//account creation methods
	public String waitForCreateReply() {
		byte[] data = new byte[1024];
		DatagramPacket packet = new DatagramPacket(data, data.length);
		try {
			socket.receive(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new String(packet.getData(), packet.getOffset(), packet.getLength());
	}
	//methods for all clients
	public void send(String msg){
		msg = proccessMessage(msg);
		if (msg == null) return;
		byte[] data = new byte[1024];
		data = msg.getBytes();
		DatagramPacket packet = new DatagramPacket(data, data.length, IP, port);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int getId(){
		return id;
	}
	public DatagramSocket getSocket(){
		return socket;
	}
	public Thread getListenThread(){
		return recieveThread;
	}
}