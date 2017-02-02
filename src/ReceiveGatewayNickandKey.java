import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.PublicKey;
import java.util.HashMap;

/*
 * This class design to receive other subnet's user.
 */
public class ReceiveGatewayNickandKey implements Runnable{
	ServerSocket receiveSocket;
	public ReceiveGatewayNickandKey() throws IOException{
		receiveSocket = new ServerSocket(5554);
	}
	public void run(){
		System.out.println("Listening PortNO: 5554 (Gateway Online Map!)");
		while(true){
			try{
				Socket connSocket = receiveSocket.accept();
				HashMap <String, PublicKey> outerMap = null;
				ObjectInputStream inTo = new ObjectInputStream(connSocket.getInputStream());
				try{
					outerMap = (HashMap <String, PublicKey>)inTo.readObject();
				}
				catch (ClassNotFoundException except) {
					except.printStackTrace();
				}
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				ObjectOutput out = null;
				byte[] yourBytes;
				try{
				  out = new ObjectOutputStream(bos);   
				  out.writeObject(outerMap);
				  yourBytes = bos.toByteArray();
				} 
				finally{
				  try {
				    if (out != null) {
				      out.close();
				    }
				  }
				  catch (IOException ex) {
				    // ignore close exception
				  }
				  try {
				    bos.close();
				  } catch (IOException ex) {
				    // ignore close exception
				  }
				}
				DatagramSocket socket = new DatagramSocket();
				socket.setBroadcast(true);
				DatagramPacket inBroad = new DatagramPacket(yourBytes, yourBytes.length, InetAddress.getByName("255.255.255.255"), 5557);
				socket.send(inBroad);
				ReceiveNickandKey.userMap.putAll(outerMap);
				
			}
			catch (IOException except){
				except.printStackTrace();
			}
		}	
	}
}
