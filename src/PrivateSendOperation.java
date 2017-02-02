import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.security.Key;
import java.util.StringTokenizer;
import javax.crypto.Cipher;
import org.apache.commons.codec.binary.Base64;

/*
 * Using 8887 to send private message.
 */
public class PrivateSendOperation {
	Key mypublickey;
	public PrivateSendOperation(String privateusertext, Key publickey){
		System.out.println("PrivateSendOperation is working!");
		mypublickey = publickey;
		StringTokenizer justNick = new StringTokenizer(privateusertext, ":");
		String nick = (String) justNick.nextElement();
		ReceiveNickandKey.privateFrame.get(nick).chatText.append(privateusertext);
		ReceiveNickandKey.privateFrame.get(nick).chatText.append("\n");
		//Encryption with destination publicKey
		byte[] cipherText = null;
		try{
			final Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, mypublickey);
			cipherText = cipher.doFinal(privateusertext.getBytes());
			System.out.println("Your private text is encrypted!");
		}
		catch(Exception except){
			except.printStackTrace();
		}
		//Encryption End
		
		//Broadcast
		String spoofText = Base64.encodeBase64String(cipherText);
		//-----------------------------------
		//PROCESS SPOOF CODE.
		//try{
		//	System.out.println("Process Run!");
		//	Process proc = Runtime.getRuntime().exec("echo 2206 | sudo -kS ./a.out " + spoofText, null, new File(getClass().getResource("AppGUI.class").getPath()).getParentFile());
		//}
		//catch (IOException except){
		//	except.printStackTrace();
		//}
		//------------------------------------
		//------------------------------------
		//NATIVE SPOOF CODE.
		//(here active native part!)
		//NativeSpoofing NS = new NativeSpoofing();
		//String spoofText = Base64.encodeBase64String(cipherText);
		//NS.sender(spoofText);
		//-------------------------------------
		//-------------------------------------
		//NO SPOOF CODE.
		try{
			DatagramSocket mysocket = new DatagramSocket();
			mysocket.setBroadcast(true);
			try{
				DatagramPacket sendPacket = new DatagramPacket(cipherText, cipherText.length, InetAddress.getByName("255.255.255.255"), 8887);
				mysocket.send(sendPacket);
				System.out.println("Your private packet is broadcasting!");
			}
			catch(Exception except){
				except.printStackTrace();
			}
		}
		catch(IOException except){
			except.printStackTrace();
		}
		//--------------------------------------
		//BroadcastEnd
	}
}
