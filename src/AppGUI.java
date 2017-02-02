import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.MenuListener;

public class AppGUI extends JFrame{
	private static int WIDTH = 800;
	private static int HEIGHT = 600;
	int validconn = 0;
	
	String userNickname;
	
	JPanel GUI_Panel = new JPanel();
	JLabel GUI_Gif;
	JLabel GUI_Img;
	Icon cycle_Gif;
	Icon welcome_Img;
	
	Key publicKey;
	Key privateKey;
	
	public AppGUI(String highlight){
		super(highlight);
		setLayout(new BorderLayout());
		setBounds(0, 0, WIDTH, HEIGHT);
		
		JMenuBar menubar = new JMenuBar();
		JMenu File = new JMenu("FILE");
		menubar.add(File);
		JMenuItem GenerateKey = new JMenuItem("Generate Key");
		JMenuItem Connect = new JMenuItem("Connect");
		JMenuItem Exit = new JMenuItem("EXIT");
		File.add(GenerateKey);
		File.add(Connect);
		File.addSeparator();
		File.add(Exit);
		setJMenuBar(menubar);
		
		cycle_Gif = new ImageIcon(getClass().getResource("ChatOuterBack.gif"));
		GUI_Gif = new JLabel(cycle_Gif);
		GUI_Gif.setBounds(0, 0, WIDTH, HEIGHT);
		welcome_Img = new ImageIcon(getClass().getResource("Welcome_Image.png"));
		GUI_Img = new JLabel(welcome_Img);
		GUI_Img.setBounds(210, 130, 383, 288);
		
		GUI_Panel.setLayout(new BorderLayout());
		
		GUI_Panel.add(GUI_Img);
		GUI_Panel.add(GUI_Gif);
		
		add(GUI_Panel,BorderLayout.CENTER);
		
		MenuListener mListener = new MenuListener();
		GenerateKey.addActionListener(mListener);
		Connect.addActionListener(mListener);
		Exit.addActionListener(mListener);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	private class MenuListener implements ActionListener{
		public void actionPerformed(ActionEvent click){
			if(click.getActionCommand() == "Generate Key"){
				validconn = 1;
				//Generate Key
				try{
					KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA");
					keygen.initialize(512);
					KeyPair keypair = keygen.genKeyPair();
					publicKey = keypair.getPublic();
					privateKey = keypair.getPrivate();
				} 
				catch(NoSuchAlgorithmException except){
					except.printStackTrace();
				}
				
				JOptionPane.showMessageDialog(GUI_Panel,"Your key has been created!\nNow you are anonnymus!");
			}
			if(click.getActionCommand() == "Connect"){
				if(validconn == 1){
					userNickname = JOptionPane.showInputDialog(GUI_Panel, "Enter Nickname");
					while(userNickname.isEmpty()){
						userNickname = JOptionPane.showInputDialog(GUI_Panel, "Enter Nickname");
					}
					JOptionPane.showMessageDialog(GUI_Panel,"You can enter chat room!\nHave Fun!");
					ChatGUI chatroom = new ChatGUI(userNickname, publicKey, privateKey);
					chatroom.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					chatroom.setVisible(true);
					chatroom.setResizable(false);
					dispose();
				}
				else{
					JOptionPane.showMessageDialog(GUI_Panel,"Opss! You must be first generate key.\nClick FILE and click Generate Key!");
				}
			}
			if(click.getActionCommand() == "EXIT"){
				System.exit(0);
			}
		}
	}
	public static void main(String[] args) throws Exception{
		new AppGUI("IRC Chat Application(Beta)");
	}
}
