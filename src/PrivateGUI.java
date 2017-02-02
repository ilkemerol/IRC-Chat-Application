import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.Key;
import java.security.PublicKey;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.MenuListener;

public class PrivateGUI extends JFrame{
	private static int WIDTH = 420;
	private static int HEIGHT = 300;
	
	JPanel allPanel = new JPanel();
	JLabel background;
	JButton sendPrivate;
	JTextArea userText;
	JTextArea chatText;
	
	Icon backGIF;
	
	String privNick;
	
	PublicKey publicKey;
	public PrivateGUI(String nickname, String toUser, PublicKey pmKey){
		super("Private Room /w " + toUser);
		setLayout(new BorderLayout());
		setBounds(800, 0, WIDTH, HEIGHT);
		
		privNick = nickname;
		publicKey = pmKey;
		
		backGIF = new ImageIcon(getClass().getResource("ChatBack.jpg"));
		background = new JLabel(backGIF);
		background.setBounds(0, 0, WIDTH, HEIGHT);
		
		chatText = new JTextArea();
		chatText.setEditable(false);
		//chatText.setBounds(10, 10, 390, 190);
		JScrollPane scrollchatLog = new JScrollPane(chatText,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); 
		scrollchatLog.setBounds(10, 10, 390, 190);
		
		userText = new JTextArea("Enter Message", 3, 30);
		userText.setBounds(10, 210, 300, 40);
		
		sendPrivate = new JButton("SEND");
		sendPrivate.setBounds(320, 210, 80, 40);
		
		allPanel.setLayout(null);
		allPanel.add(sendPrivate);
		allPanel.add(scrollchatLog);
		allPanel.add(userText);
		allPanel.add(background);
		
		setResizable(false);
		setVisible(true);
		add(allPanel, BorderLayout.CENTER);
		
		MenuListener mListener = new MenuListener();
		sendPrivate.addActionListener(mListener);
		
	}
	private class MenuListener implements ActionListener{
		public void actionPerformed(ActionEvent click){
			if(click.getActionCommand() == "SEND"){
				String text = privNick + ": " + userText.getText();
				userText.setText("");
				PrivateSendOperation sendprivatemessage = new PrivateSendOperation(text,publicKey);
			}
		}
	}
}
