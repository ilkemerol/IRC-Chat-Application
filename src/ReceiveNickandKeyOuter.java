import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.security.PublicKey;
import java.util.HashMap;

/*
 * This class design for access other subnet's user(hashmap),
 * Convert byte to hashmap for sync own hashmap.
 * Listening 5557, run when app run.
 */

public class ReceiveNickandKeyOuter implements Runnable{
	DatagramSocket socket;
	HashMap <String, PublicKey> myhashmap;
	public ReceiveNickandKeyOuter() throws Exception{
		//System.out.println("Other Hash Map is Listening!");
		socket = new DatagramSocket(5557);
	}
	public void run(){
		System.out.println("Listening PortNO: 5557 (Other Subnet's Map!)");
		while(true){
			byte[] hashmap = new byte[15000];
			DatagramPacket packet = new DatagramPacket(hashmap, hashmap.length);
			try{
				socket.receive(packet);
			}
			catch (IOException except) {
				except.printStackTrace();
			}

			System.arraycopy(packet, 0, hashmap, 0, packet.getLength());
			
			ByteArrayInputStream bis = new ByteArrayInputStream(hashmap);
			ObjectInput in = null;
			try{
				try{
					in = new ObjectInputStream(bis);
				}
				catch (IOException except) {
				except.printStackTrace();
				}
					try{
						Object o = in.readObject();
						myhashmap = (HashMap <String,PublicKey>) o;
						ReceiveNickandKey.userMap.putAll(myhashmap);
					}
					catch (ClassNotFoundException except){
						except.printStackTrace();
					}
				catch (IOException except){
					except.printStackTrace();
				} 
			}
			finally {
				try{
			    bis.close();
				}
				catch (IOException ex) {
			    // ignore close exception
				}
				try{
					if(in != null){
						in.close();
					}
				}
				catch (IOException ex) {
			    // ignore close exception
				}
			}
		}
	}
}
