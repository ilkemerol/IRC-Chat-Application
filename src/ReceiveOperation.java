import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;

/*
 * This is global broadcast class,
 * listening 8888 port and this port carry encryption packet.
 */
public class ReceiveOperation implements Runnable{
	DatagramSocket socket;
	ChatGUI chatroomreceiver;
	public ReceiveOperation(ChatGUI receiverchatGUI) throws Exception{
		chatroomreceiver = receiverchatGUI;
		socket = new DatagramSocket(8888);
	}
	public void run(){
		//System.out.println("Receive Operation is working!");
		System.out.println("Listening PortNO: 8888 (Standard encrytion message line!)");
		while(true){
			try{
				int i=0;
				while(i == 0){
					byte[] buff = new byte[1024];
					DatagramPacket packet = new DatagramPacket(buff, buff.length);
					socket.receive(packet);
					byte[] sendDecode = new byte[1024];
					sendDecode = packet.getData();
					int sendLength = packet.getLength();
					try{
						Decode decodethread = new Decode(sendDecode, sendLength, chatroomreceiver);
						Thread decodeT = new Thread(decodethread);
						decodeT.start();
						/*
						 * decodeT need to no freeze application.
						 */
						//System.out.println("Try to decode!");
					}
					catch(Exception except){
						except.printStackTrace();
					}
					i++;
				}
			}
			catch (IOException except) {
				except.printStackTrace();
			}
		}
	}
}