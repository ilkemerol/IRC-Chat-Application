import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.security.Key;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatGUI extends JFrame implements KeyListener{
	private static int WIDTH = 800;
	private static int HEIGHT = 600;
	String chatGUInick = "";
	Key mypublic;
	static Key myprivate;
	
	JPanel allPanel = new JPanel();
	JLabel GUI_Img;
	JLabel GUI_GIF;
	JLabel GUI_Log;
	JLabel GUI_On;
	JLabel GUI_Gateway;
	Icon chatback;
	Icon GUI_Gif;
	Icon Logs;
	Icon Onlines;
	Icon Gateway;
	
	JTextArea userText;
	JButton SendButton;
	
	JTextArea chatLog;
	JList onlines;
	
	int gatewaycontrol = 0;
	
	DefaultListModel <String> listModel;
	
	Thread sendnickandkey;
	
	public ChatGUI(String userNick, Key publickey, Key privatekey){
		super("IRC Chat Applcation(Beta)");
		setLayout(new BorderLayout());
		setBounds(0, 0, WIDTH, HEIGHT);
		addKeyListener(this);
		chatGUInick = userNick;
		mypublic = publickey;
		myprivate = privatekey;
		
		JMenuBar menubar = new JMenuBar();
		JMenu File = new JMenu("FILE");
		menubar.add(File);
		JMenuItem GenerateKey = new JMenuItem("Generate Key");
		JMenuItem Disconnect = new JMenuItem("Disconnect");
		JMenuItem RunAsGateway = new JMenuItem("Run As Gateway");
		JMenuItem StopGateway = new JMenuItem("Stop Gateway Mode");
		JMenuItem Help = new JMenuItem("Help");
		JMenuItem Exit = new JMenuItem("EXIT");
		File.add(GenerateKey);
		File.add(Disconnect);
		File.add(RunAsGateway);
		File.add(StopGateway);
		File.addSeparator();
		File.add(Help);
		File.add(Exit);
		setJMenuBar(menubar);
		
		Logs = new ImageIcon(getClass().getResource("Logs.png"));
		GUI_Log = new JLabel(Logs);
		GUI_Log.setBounds(10, 10, 272, 72);
		
		GUI_Gif = new ImageIcon(getClass().getResource("ChatLeftSide.gif"));
		GUI_GIF = new JLabel(GUI_Gif);
		GUI_GIF.setBounds(700, 50, 75, 500);
		
		Onlines = new ImageIcon(getClass().getResource("OnlineImg.png"));
		GUI_On = new JLabel(Onlines);
		GUI_On.setBounds(530, 70, 122, 27);
		
		Gateway = new ImageIcon(getClass().getResource("GatewayNULL.png"));
		GUI_Gateway = new JLabel(Gateway);
		GUI_Gateway.setBounds(600, 5, 177, 31);
		
		chatback = new ImageIcon(getClass().getResource("ChatBack.jpg"));
		GUI_Img = new JLabel(chatback);
		GUI_Img.setBounds(0, 0, WIDTH, HEIGHT);
		
		userText = new JTextArea("Enter Message", 3, 30);
		userText.setBounds(10, 470, 500, 60);
		
		SendButton = new JButton("SEND");
		SendButton.setBounds(530, 470, 150, 60);
		
		chatLog = new JTextArea();
		chatLog.setEditable(false);
		JScrollPane scrollchatLog = new JScrollPane(chatLog,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); 
		scrollchatLog.setBounds(10, 100, 500, 350);
		
		listModel = new DefaultListModel();
		onlines = new JList(listModel);
		onlines.setBounds(530, 100, 150, 350);
		
		allPanel.setLayout(new BorderLayout());
		allPanel.add(GUI_Log);
		allPanel.add(GUI_On);
		allPanel.add(GUI_Gateway);
		allPanel.add(userText);
		allPanel.add(SendButton);
		allPanel.add(scrollchatLog);
		allPanel.add(onlines);
		allPanel.add(GUI_GIF);
		allPanel.add(GUI_Img);
		
		add(allPanel,BorderLayout.CENTER);
		
		MenuListener mListener = new MenuListener();
		GenerateKey.addActionListener(mListener);
		Disconnect.addActionListener(mListener);
		RunAsGateway.addActionListener(mListener);
		StopGateway.addActionListener(mListener);
		Help.addActionListener(mListener);
		Exit.addActionListener(mListener);
		
		SendButton.addActionListener(mListener);
		
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		/*
		 * When start application, start receivemsg, receivenickey and receivenickkeyOUTER(for other subnets).
		 */
		try{
			ReceiveOperation RO = new ReceiveOperation(this);
			Thread receiver = new Thread(RO);
			receiver.start();
			ReceiveNickandKey receiveNK = new ReceiveNickandKey(this);
			Thread receivenickandkey = new Thread(receiveNK);
			receivenickandkey.start();
			SendNickandKey sendNK = new SendNickandKey(chatGUInick, mypublic);
			sendnickandkey = new Thread(sendNK);
			sendnickandkey.start();
			ReceiveNickandKeyOuter RNKO = new ReceiveNickandKeyOuter();
			Thread otherAccess = new Thread(RNKO);
			otherAccess.start();
			
		}
		catch (Exception except){
			except.printStackTrace();
		}
	}
	private class MenuListener implements ActionListener{
		public void actionPerformed(ActionEvent click){
			if(click.getActionCommand() == "Generate Key"){
				JOptionPane.showMessageDialog(allPanel,"No need to create keys!");
			}
			if(click.getActionCommand() == "Disconnect"){
				JOptionPane.showMessageDialog(allPanel, "Disconnect from chat applicaiton!");
				AppGUI chatroom = new AppGUI("Anonymous Chat Room");
				chatroom.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				chatroom.setVisible(true);
				chatroom.setResizable(false);
				dispose();
				sendnickandkey.stop();
				if(gatewaycontrol == 1){
					GatewayMSG gatewaymsg = new GatewayMSG("disconn", myprivate);
				}
				else{
					GlobalSendOperation sendmessage = new GlobalSendOperation("disconn", myprivate);
				}
			}
			Thread gateway = null;
			Thread receivegateway = null;
			Thread receiveMsg = null;
			if(click.getActionCommand() == "Run As Gateway"){
				gatewaycontrol = 1;
				JOptionPane.showMessageDialog(allPanel,"Your machine set up gateway!");
				Icon setGateway = new ImageIcon(getClass().getResource("Gateway.png"));
				GUI_Gateway.setIcon(setGateway);
				try{	
					GatewayAppNickandKey GANK = new GatewayAppNickandKey(ReceiveNickandKey.userMap);
					gateway = new Thread(GANK);
					gateway.start();
					ReceiveGatewayNickandKey RGNK = new ReceiveGatewayNickandKey();
					receivegateway = new Thread(RGNK);
					receivegateway.start();
					ReceiverGatewayMsg RGM = new ReceiverGatewayMsg();
					receiveMsg = new Thread(RGM);
					receiveMsg.start();
				}
				catch (IOException except){
					except.printStackTrace();
				}
				
			}
			if(click.getActionCommand() == "Stop Gateway Mode"){
				if(gatewaycontrol == 1){
					GUI_Gateway.setIcon(new ImageIcon(getClass().getResource("GatewayNULL.png")));
					gatewaycontrol = 0;
					gateway.stop();
					receivegateway.stop();
					receiveMsg.stop();
				}
				else{
					JOptionPane.showMessageDialog(allPanel,"You are not aldreay gateway!");
				}
			}
			if(click.getActionCommand() == "Help"){
				JOptionPane.showMessageDialog(allPanel,"This application is designed for CSE 471 term project.\nDepartment Page: cse.yeditepe.edu.tr\nProect Maneger: Tacha Serif\nProject Assistant: Osman Kerem Perente\nProject Designer: Ýlkem Erol");
			}
			if(click.getActionCommand() == "EXIT"){
				sendnickandkey.stop();
				if(gatewaycontrol == 1){
					GatewayMSG gatewaymsg = new GatewayMSG("disconn", myprivate);
				}
				else{
					GlobalSendOperation sendmessage = new GlobalSendOperation("disconn", myprivate);
				}
				System.exit(0);
			}
			
			if(click.getActionCommand() == "SEND"){
				String text = chatGUInick + ": " + userText.getText();
				userText.setText("");
				/*
				Create object SendOperation.
				*/
				if(gatewaycontrol == 1){
					GatewayMSG gatewaymsg = new GatewayMSG(text, myprivate);
				}
				else{
					GlobalSendOperation sendmessage = new GlobalSendOperation(text, myprivate);
				}
			}
		}
	}
	
	//BUTTON HANDLER
	public void keyPressed(KeyEvent event){
		if(event.getKeyCode() == event.VK_ENTER){
			String text = userText.getText();
			chatLog.setText("You: " + text);
			userText.setText("");
		}
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub	
	}
}
