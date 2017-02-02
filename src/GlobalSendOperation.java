import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.security.Key;
import javax.crypto.Cipher;
import org.apache.commons.codec.binary.Base64;

/*
 *Using port 8888 standard message,
 *this port carry encryption packet. 
 */
public class GlobalSendOperation{
	Key myprivatekey;
	public GlobalSendOperation(String userText, Key privatekey){
		System.out.println("GlobalSendOperation is working!");
		myprivatekey = privatekey;
		//Encryption (encrypt with own privatekey)
		byte[] cipherText = null;
		try{
			final Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, myprivatekey);
			cipherText = cipher.doFinal(userText.getBytes());
			System.out.println("Your text is encrypted!");
		}
		catch(Exception except){
			except.printStackTrace();
		}
		//EncryptionEnd
		
		//Broadcast
		String spoofText = Base64.encodeBase64String(cipherText);
		//-------------------------------------
		//PROCESS SPOOF CODE.
		//try{
		//	System.out.println("Process Run!");
		//	Process proc = Runtime.getRuntime().exec("echo 2206 | sudo -kS ./a.out " + spoofText, null, new File(getClass().getResource("AppGUI.class").getPath()).getParentFile());
		//}
		//catch (IOException except){
		//	except.printStackTrace();
		//}
		//--------------------------------------
		//--------------------------------------
		//NATIVE SPOOF CODE.
		//(here active native part!)
		//NativeSpoofing NS = new NativeSpoofing();
		//String spoofText = Base64.encodeBase64String(cipherText);
		//NS.sender(spoofText);
		//---------------------------------------
		//---------------------------------------
		//NO SPOOF CODE.
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
		//----------------------------------------
		//BroadcastEnd
	}
}
