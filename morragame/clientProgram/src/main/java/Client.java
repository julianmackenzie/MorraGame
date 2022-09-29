import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.Vector;
import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;

public class Client extends Thread{

		
	public Socket socketClient;
	public ObjectOutputStream out;
	public ObjectInputStream in;
	boolean canConnect = true;
	
	public String ipField;
	public String portField;
	
	MorraInfo info = new MorraInfo();
	
	public int playerNumber = 0;
	
	
	private Consumer<Serializable> callback;
	
	
	Client(String ipField, String portField, Consumer<Serializable> call) {
		this.ipField = ipField;
		this.portField = portField;
		callback = call;
	}
	
	public void send() {
		
		try {
			out.writeObject(info);
			out.reset();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
		
	public void run() {
		
		try {
		socketClient= new Socket(ipField,Integer.valueOf(portField));
	    out = new ObjectOutputStream(socketClient.getOutputStream());
	    in = new ObjectInputStream(socketClient.getInputStream());
	    socketClient.setTcpNoDelay(true);
		}
		catch(Exception e) {
			canConnect = false;
			return;
		}

		
		boolean newData = false;
		
		while(true) {
			try {
			info = (MorraInfo) in.readObject();
			
			newData = true;
			
			}
			catch(Exception e) {newData = false;}
			
			if (newData) {
				
				// assign player numbers (happens upon connecting)
				
				if (info.cmd == 1) { // code 1 = assignP1
					playerNumber = 1;
					callback.accept(info);
					info.cmd = 0;
				} else if (info.cmd == 2) { // code 2 = assignP2
					playerNumber = 2;
					callback.accept(info);
					info.cmd = 0;
				} 
				
				else if (info.cmd == 9000) { // code 9000 = play again
					callback.accept(info);
					info.cmd = 0;
				}
				
				// process opponent joining, leaving, or turns being played
				else {
					if (info.cmd == 5) { // code 5 = connect
						callback.accept(info);
						info.cmd = 0;
					}
						
					else if (info.cmd == -5) { // code -5 = disconnect
						callback.accept(info);
						info.cmd = 0;
					}
						
					else if (info.playing == true) {
						info.cmd = 50; // code 50 = showPlays
						callback.accept(info);
						info.cmd = 0;
					}
				}
			}
			
			
		}
	
	}
			

}



