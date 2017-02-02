import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/*
 * Always listening port 5555 and update online list.
 */

public class ReceiveNickandKey implements Runnable, MouseListener{
	static HashMap <String,PublicKey> userMap = new HashMap <String,PublicKey>();
	static HashMap <String,PrivateGUI> privateFrame = new HashMap <String,PrivateGUI>();
	DatagramSocket socket;
	ChatGUI onlines;
	String nickname;
	public ReceiveNickandKey(ChatGUI onlinesGUI) throws Exception{
		onlines = onlinesGUI;
		nickname = onlines.chatGUInick;
		socket = new DatagramSocket(5555);
		MouseListener mouseListener = new MouseAdapter(){
			public void mouseClicked(MouseEvent mouseEvent){
				JList theList = (JList) mouseEvent.getSource();
				if(mouseEvent.getClickCount() == 2){
					int index = theList.locationToIndex(mouseEvent.getPoint());
					if(index >= 0){
						String obj = (String) theList.getModel().getElementAt(index);
						for(String keyUser: userMap.keySet()){
							if(keyUser.compareTo(obj) == 0){
								PrivateGUI privateroom = new PrivateGUI(nickname, keyUser, userMap.get(keyUser));
								privateroom.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
								privateroom.setVisible(true);
								privateroom.setResizable(false);
								privateFrame.put(nickname, privateroom);
							}
						}
					}
				}
			}
		};
		try{
			PrivateReceiverOperation PRO = new PrivateReceiverOperation();
			Thread receiver = new Thread(PRO);
			receiver.start();
		}
		catch(Exception except){
			except.printStackTrace();
		}
		onlines.onlines.addMouseListener(mouseListener);
	}
	public void run(){
		System.out.println("Listening PortNO: 5555 (Online List Sync!)");
		while(true){
			try{
				byte[] buff = new byte[1024];
				DatagramPacket packet = new DatagramPacket(buff, buff.length);
				socket.receive(packet);
				byte[] wholePacket = new byte[packet.getLength()];
				System.arraycopy(buff, 0, wholePacket, 0, packet.getLength());
				String name = "";
				byte[] nameByte = new byte[25];
				System.arraycopy(wholePacket, 0, nameByte, 0, 25);
				name = new String(nameByte);
				name= name.trim();
				/*
				 * Name and Key put same packet.
				 */
				//System.out.println("Name = " + name);
				byte[] publicKeyByte = new byte[wholePacket.length - 25];
				System.arraycopy(wholePacket, 25, publicKeyByte, 0, wholePacket.length-25);
				KeyFactory keyF = null;
				try{
					keyF = KeyFactory.getInstance("RSA");
				}
				catch(NoSuchAlgorithmException except){
					except.printStackTrace();
				}
				PublicKey publickeys = null;
				try{
					publickeys = keyF.generatePublic(new X509EncodedKeySpec(publicKeyByte));
				}
				catch(InvalidKeySpecException except){
					except.printStackTrace();
				}
				if(userMap.containsKey(name)){
					//do nothing.
				}
				else{
					System.out.println("Add new nick and key!");
					//No Duplicate.
					onlines.listModel.removeAllElements();
					userMap.put(name, publickeys);
					for(String user: userMap.keySet()){
						onlines.listModel.addElement(user.trim());
					}
					onlines.onlines.requestFocus();
					onlines.onlines.setSelectedIndex(-1);
				}
			}
			catch(IOException except){
				except.printStackTrace();
			}
		}
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
