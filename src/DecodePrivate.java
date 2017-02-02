import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.crypto.Cipher;
import javax.swing.JFrame;

public class DecodePrivate  implements Runnable{
	byte[] insideMsg;
	int insideLength;
	PrivateGUI insideGUI;
	ChatGUI accessKey;
	Key insideprivate;
	public DecodePrivate(byte[] mypacket, int mypacketLength) throws Exception{
		insideLength = mypacketLength;
		insideMsg = new byte[insideLength];
		System.arraycopy(mypacket, 0, insideMsg, 0, insideLength);
		String strMsg = new String(insideMsg);
		insideprivate = accessKey.myprivate;
	}
	/*
	 * Every user try to decrypt with own private.(no need to hashmap)
	 */
	public void run(){
		try{
			final Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, insideprivate);
			byte[] decodeText = cipher.doFinal(insideMsg);
			//IF SUCCESS
			StringTokenizer justNick = new StringTokenizer(new String(decodeText), ":");
			String nick = (String) justNick.nextElement();
			if(ReceiveNickandKey.privateFrame.containsKey(nick)){
				//Nick exists in hashmap, use onBoard(aldreadt exists) frame.
				ReceiveNickandKey.privateFrame.get(nick).chatText.append(new String(decodeText));
				ReceiveNickandKey.privateFrame.get(nick).chatText.append("\n");
			}
			else{
				//Nick not exists in hashmap, create new frame, and add hashmap.
				//System.out.println("Add privateFrame Maps!");
				System.out.println("Private message coming and create frame!");
				PrivateGUI privateroom = new PrivateGUI(nick, "unknown", ReceiveNickandKey.userMap.get(nick));
				privateroom.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				privateroom.setVisible(true);
				privateroom.setResizable(false);
				ReceiveNickandKey.privateFrame.put(nick, privateroom);
			}
		}
		catch(Exception except){
			except.printStackTrace();
		}
	}

}
