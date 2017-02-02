import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;
/*
 * Listening 8887 to receive private message,
 * always run!
 */
public class PrivateReceiverOperation implements Runnable{
	DatagramSocket socket;
	PrivateGUI privateroom;
	public PrivateReceiverOperation() throws Exception{
		socket = new DatagramSocket(8887);
	}
	public void run(){
		//System.out.println("Private Receive is working!");
		System.out.println("Listening PortNO: 8887 (Private Message Line!)");
		while(true){
			try{
				int i = 0;
				while(i == 0){
					byte[] buff = new byte[1024];
					DatagramPacket packet = new DatagramPacket(buff, buff.length);
					socket.receive(packet);
					byte[] sendDecode = new byte[packet.getLength()];
					sendDecode = packet.getData();
					int sendLength = packet.getLength();
					try{
						DecodePrivate decodepriv = new DecodePrivate(sendDecode, sendLength);
						Thread decodeP = new Thread(decodepriv);
						decodeP.start();
						//System.out.println("Private packet try to decode!");
					}
					catch(Exception except){
						except.printStackTrace();
					}
					i++;
				}
			}
			catch(IOException except){
				except.printStackTrace();
			}
		}	
	}
}
