import java.security.Key;
import java.security.PublicKey;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;

public class Decode implements Runnable{
	byte[] insideMsg;
	int insideLength;
	ChatGUI insideGUI;
	ReceiveNickandKey accessMap;
	public Decode(byte[] mypacket, int mypacketLength, ChatGUI chatroomreceiver) throws Exception{
		insideLength = mypacketLength;
		insideMsg = new byte[insideLength];
		System.arraycopy(mypacket, 0, insideMsg, 0, insideLength);
		insideGUI = chatroomreceiver;
	}
	public void run(){
		//System.out.println("Decode Starting!");
		/*
		 * Try all public key in hashmap.
		 */
		for(String myuser: accessMap.userMap.keySet()){
			try{
				final Cipher cipher = Cipher.getInstance("RSA");
				cipher.init(Cipher.DECRYPT_MODE, accessMap.userMap.get(myuser));
				byte[] decodeText = cipher.doFinal(insideMsg);
				//Control message!
				if(new String(decodeText).equals("disconn")){
					accessMap.userMap.clear();
				}
				else{
					insideGUI.chatLog.append(new String(decodeText));
					insideGUI.chatLog.append("\n");
				}
			}
			catch(Exception except){
				except.printStackTrace();
			}
		}
	}
}
