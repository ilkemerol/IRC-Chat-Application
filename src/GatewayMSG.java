import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.security.Key;
import javax.crypto.Cipher;

/*
 * This class design to receive other subnet's packet with 8889 port,
 * open (decrypt) for gateway client and broadcast own subnet(encryption form).
 */
public class GatewayMSG{
	Key myprivatekey;
	public GatewayMSG(String userText, Key privatekey){
		myprivatekey = privatekey;
		//Encryption
		byte[] cipherText = null;
		try{
			final Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, myprivatekey);
			cipherText = cipher.doFinal(userText.getBytes());
		}
		catch(Exception except){
			except.printStackTrace();
		}
		//EncryptionEnd
		//TCP
		try{
			File ipfile = new File ("config.txt");
			FileReader fileReader = new FileReader(ipfile);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String ipAddress;
			while((ipAddress = bufferedReader.readLine()) != null){
				//System.out.println(ipAddress);
				//----------------------------------------------------------------------------
				//File Configurate.
				Socket gatewayMSG = new Socket(ipAddress,8889);
				DataOutputStream outToMsg = new DataOutputStream(gatewayMSG.getOutputStream());
				outToMsg.write(cipherText);
				//----------------------------------------------------------------------------
				//-----------------------------------------------------------------------------
				//Manuel Configurate.
				//Socket gatewayMSG = new Socket("Hard Coded IP",8889);
				//DataOutputStream outToMsg = new DataOutputStream(gatewayMSG.getOutputStream());
				//outToMsg.write(cipherText);
				//------------------------------------------------------------------------------
			}
		}
		catch(IOException except){
			except.printStackTrace();
		}
		//TCPEnd
		//Broadcast Inner
		try{
			DatagramSocket mysocket = new DatagramSocket();
			mysocket.setBroadcast(true);
			try{
				DatagramPacket sendPacket = new DatagramPacket(cipherText, cipherText.length, InetAddress.getByName("255.255.255.255"), 8888);
				mysocket.send(sendPacket);
				System.out.println("Your packet is broadcasting!");
			}
			catch(Exception except){
				except.printStackTrace();
			}
		}
		catch(IOException except){
			except.printStackTrace();
		}
		//BroadcastInnerEnd
	}
}
