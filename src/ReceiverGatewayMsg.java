import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
/*
 * This class design for receiver side gateway,
 * Listen 8889 port and broadcast coming packet 8888 port.(broadcast for own subnet)
 */

public class ReceiverGatewayMsg implements Runnable{
	ServerSocket receiveMsg;
	public ReceiverGatewayMsg() throws IOException{
		receiveMsg = new ServerSocket(8889);
	}
	public void run(){
		System.out.println("Listening PortNO: 8889 (Gateway Message!)");
		while(true){
			try{
				Socket connSocket = receiveMsg.accept();
				DataInputStream inTo = new DataInputStream(connSocket.getInputStream());
				byte[] insideMsg = new byte [inTo.available()];
				inTo.readFully(insideMsg);
				DatagramSocket socket = new DatagramSocket();
				socket.setBroadcast(true);
				DatagramPacket inBroad = new DatagramPacket(insideMsg, insideMsg.length, InetAddress.getByName("255.255.255.255"), 8888);
				socket.send(inBroad);
			}
			catch(IOException except){
				except.printStackTrace();
			}
		}
	}
}
