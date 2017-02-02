import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.security.Key;
import java.security.PublicKey;
import java.util.HashMap;
/*
 * broadcast port 5555!
 */
public class SendNickandKey implements Runnable{
	Key broadcastKey;
	String broadcastNick;
	public SendNickandKey(String nickName, Key comingKey){
		//PUBLIC KEY BROADCAST HERE.
		broadcastKey = comingKey;//PublicKey
		broadcastNick = nickName;
		//System.out.println("Key and Nick Broadcast every 1 sec!");
	}
	public void run(){
		while(true){
			try{
				DatagramSocket mysocket = new DatagramSocket();
				mysocket.setBroadcast(true);
				byte[] sendNick = broadcastNick.getBytes();
				//sendNick = broadcastNick.getBytes();
				//System.out.println("Nick = " + sendNick.length);
				byte[] sendKey = broadcastKey.getEncoded();//new byte[];
				//sendKey = broadcastKey.getEncoded();
				//System.out.println("Key = " + sendKey);
				byte[] wholeBroadcast = new byte[25 + sendKey.length];
				System.arraycopy(sendNick, 0, wholeBroadcast, 0, sendNick.length);
				System.arraycopy(sendKey, 0, wholeBroadcast, 25, sendKey.length);
				try{
					DatagramPacket sendWhole = new DatagramPacket(wholeBroadcast, wholeBroadcast.length, InetAddress.getByName("255.255.255.255"), 5555);
					mysocket.send(sendWhole);
				}
				catch(Exception except){
					except.printStackTrace();
				}
			}
			catch(IOException except){
				except.printStackTrace();
			}
			try{
				Thread.sleep(1000);
			}
			catch(InterruptedException except){
				except.printStackTrace();
			}
		}
	}
}
