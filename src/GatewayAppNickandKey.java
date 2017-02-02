import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.Key;
import java.util.HashMap;

/*
 * Send own subnet's users to other subnets.
 */
public class GatewayAppNickandKey implements Runnable{
	HashMap sendMap;
	public GatewayAppNickandKey(HashMap innerMap){
		sendMap = innerMap;
	}
	public void run(){
		//System.out.println("TCP Broadcast Start!");
		while(true){
			try{
				File ipfile = new File ("config.txt");
				FileReader fileReader = new FileReader(ipfile);
				BufferedReader bufferedReader = new BufferedReader(fileReader);
				String ipAddress;
				while((ipAddress = bufferedReader.readLine()) != null){
					//System.out.println(ipAddress);
					//----------------------------------------------------------------------------
					//File Configurate.
					Socket gatewaySocket = new Socket(ipAddress, 5554);
					ObjectOutputStream outTo = new ObjectOutputStream(gatewaySocket.getOutputStream());
					outTo.writeObject(sendMap);
					//----------------------------------------------------------------------------
					//-----------------------------------------------------------------------------
					//Manuel Configurate.
					//Socket gatewaySocket = new Socket("Hard Coded IP",5554);
					//ObjectOutputStream outTo = new ObjectOutputStream(gatewaySocket.getOutputStream());
					//outTo.writeObject(sendMap);
					//------------------------------------------------------------------------------
				}
			}
			catch (IOException except) {
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
