
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.util.Pair;
import java.util.function.Consumer;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;


public class ServerController implements Initializable {

	// Menu
	@FXML
	private BorderPane root;
	
	@FXML
	private Button startServer;
	
	@FXML
	private TextField portField;
	
	@FXML
	private Label errorText;
	
	
	// Console
	@FXML
	private Label clientCount;
	
	
	@FXML
	private ListView<String> listItems;
	
	@FXML
	private Label p1Score;
	
	@FXML
	private Label p2Score;
	
	@FXML
	private Label winLabel;
	
	ServerController myctr;
	
	Server serverConnection;
	
	int count = 0;	
	
	boolean hasP1 = false;
	boolean hasP2 = false;
	
	
	MorraInfo info = new MorraInfo();
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
	}
	
	
	
	

	public class Server {
	
		
	Map<ClientThread, Integer> clients = new HashMap<>();
	TheServer server;
	private Consumer<Serializable> callback;
	
	Server(Consumer<Serializable> call){
		
		callback = call;
		server = new TheServer();
		server.start();
	}
	

	

	public class TheServer extends Thread{
		
		public void run() {

			try(ServerSocket mysocket = new ServerSocket(Integer.valueOf(portField.getText()), 0, InetAddress.getByName(null));){
		  
				
				while(true) {
					if (count < 2) {
						ClientThread c = new ClientThread(mysocket.accept());
						
						int playerNum = 0;
						
						if (hasP1 == false && hasP2 == false) {
							playerNum = 1;
							hasP1 = true;
						} else if (hasP1 == false && hasP2 == true) {
							playerNum = 1;
							hasP1 = true;
						} else if (hasP1 == true && hasP2 == false) {
							playerNum = 2;
							hasP2 = true;
						}
						
						clients.put(c, playerNum);
						c.start();
					} else {
						System.out.println(""); // TODO: Weird behavior. If I don't specify an else, the while(true) loop stops.
					}
			    }
			}//end of try
				catch(Exception e) {
					callback.accept("Server socket did not launch");
				}
			}//end of while
		}
	

		class ClientThread extends Thread{
		
			Socket connection;
			ObjectInputStream in;
			ObjectOutputStream out;
			
			
			
			ClientThread(Socket s){
				count++;
				this.connection = s;
			}
			
			public void updateClients(MorraInfo myinfo) {
				for(ClientThread key : clients.keySet()) {
					ClientThread t = key;
					try {
					 t.out.writeObject(myinfo);
					 t.out.reset();
					}
					catch(Exception e) {}
				}
			}
			
			public void run(){
				
				
				try {
					in = new ObjectInputStream(connection.getInputStream());
					out = new ObjectOutputStream(connection.getOutputStream());
					connection.setTcpNoDelay(true);	
				}
				catch(Exception e) {
					System.out.println("Streams not open");
				}
				
				// assign client a player number
				try {
					if (clients.get(this) == 1)  {
						info.cmd = 1;
					}
					if (clients.get(this) == 2) {
						info.cmd = 2;
					}
					
					this.out.writeObject(info);
					out.reset();
				}
				
				catch(Exception e) {}
				
				Platform.runLater(()->{myctr.listItems.getItems().add("Client connected, assigned Player " + clients.get(this));});
				
				int num = clients.get(this);
				
				info.playerCount++;
				Platform.runLater(()->{myctr.clientCount.setText("Connected clients: " + count);});

		    	if (count == 2) info.have2players = true;
		    	if (count != 2) info.have2players = false;
		    	info.cmd = 5;
				updateClients(info);
				info.cmd = 0;
					
				 while(true) {
					    try {
					    	MorraInfo clientInfo = (MorraInfo) in.readObject();
					    	
					    	// When receiving from either player, update their stats
					    	if (clients.get(this) == 1) {
					    		if (clientInfo.cmd == -5) {
					    			count--;
					    			Platform.runLater(()->{myctr.listItems.getItems().add("Player 1 quits");});
					    			Platform.runLater(()->{myctr.clientCount.setText("Connected clients: " + count);});
					    			updateClients(info);
					    			clientInfo.cmd = 0;
					    		}
					    		else if (info.win == false) {
					    			info.p1Guess = clientInfo.p1Guess;
					    			info.p1Plays = clientInfo.p1Plays;
							    	info.p1Moved = true;
							    	info.playing = true;
							    	Platform.runLater(()->{myctr.listItems.getItems().add("Player 1 plays " + info.p1Plays + " and guesses " + info.p1Guess);});
					    		} else {
					    			info.p1Again = true;
					    		}
						    	
					    	} else if (clients.get(this) == 2) {
					    		if (clientInfo.cmd == -5) {
					    			count--;
					    			Platform.runLater(()->{myctr.listItems.getItems().add("Player 2 quits");});
					    			Platform.runLater(()->{myctr.clientCount.setText("Connected clients: " + count);});
					    			updateClients(info);
					    			clientInfo.cmd = 0;
					    		}
					    		else if (info.win == false) {
					    			info.p2Guess = clientInfo.p2Guess;
							    	info.p2Plays = clientInfo.p2Plays;
							    	info.p2Moved = true;
							    	info.playing = true;
							    	Platform.runLater(()->{myctr.listItems.getItems().add("Player 2 plays " + info.p2Plays + " and guesses " + info.p2Guess);});
					    		} else {
					    			info.p2Again = true; 
					    		}
					    	}
					    	
					    	
					    	
					    	// When both have played their move (turn logic)
					    	if (info.p1Moved == true && info.p2Moved == true) {
					    		int total = info.p1Plays + info.p2Plays;
					    		
					    		if ((info.p1Guess != info.p2Guess) && info.p1Guess == total) {
					    			info.p1Score++;
					    			Platform.runLater(()->{myctr.listItems.getItems().add("Player 1 scores a point");});
					    			Platform.runLater(()->{myctr.p1Score.setText("Player 1's score: " + info.p1Score);});
					    		} else if ((info.p1Guess != info.p2Guess) && info.p2Guess == total) {
					    			info.p2Score++;
					    			Platform.runLater(()->{myctr.listItems.getItems().add("Player 2 scores a point");});
					    			Platform.runLater(()->{myctr.p2Score.setText("Player 2's score: " + info.p2Score);});
					    		}
					    		
					    		if (info.p1Score == 2 || info.p2Score == 2) {
					    			info.win = true;
					    			if (info.p1Score == 2) {
					    				Platform.runLater(()->{myctr.listItems.getItems().add("Player 1 wins");});
					    				Platform.runLater(()->{myctr.winLabel.setText("Winner: Player 1");});
					    			}
					    			if (info.p2Score == 2) {
					    				Platform.runLater(()->{myctr.listItems.getItems().add("Player 2 wins");});
					    				Platform.runLater(()->{myctr.winLabel.setText("Winner: Player 2");});
					    			}
					    		}
					    		
					    		info.cmd = 0;
					    		updateClients(info);
					    		
					    		info.p1Moved = false;
					    		info.p2Moved = false;
					    	}
					    	
					    	if (info.p1Again == true && info.p2Again == true) {
					    		
					    		
					    		info = new MorraInfo();
					    		info.cmd = 9000; // code 9000 = play again
					    		updateClients(info);
					    		Platform.runLater(()->{myctr.winLabel.setText("Winner: N/A");});
					    		Platform.runLater(()->{myctr.p1Score.setText("Player 1's score: " + info.p1Score);});
					    		Platform.runLater(()->{myctr.p2Score.setText("Player 2's score: " + info.p2Score);});
					    	}
					    	
					    	
					    	}
					    catch(Exception e) {
					    	
					    	Platform.runLater(()->{
					    	int toReport = 0;
					    	
					    	if (num == 1) {
					    		hasP1 = false;
					    		toReport = 1;
					    	} else if (num == 2) {
					    		hasP2 = false;
					    		toReport = 2;
					    	}
					    	myctr.listItems.getItems().add("Player " + toReport + " disconnected");
					    	});
					    	
					    	
					    	clients.remove(this);
					    	count -= 1;
					    	info.playerCount--;
					    	if (count == 2) info.have2players = true;
					    	if (count != 2) info.have2players = false;
					    	
					    	
					    	
					    	
					    	info.cmd = -5; // remove
					    	updateClients(info);
					    	info.cmd = 0;
					    	
					    	Platform.runLater(()->{
								myctr.clientCount.setText("Connected clients: " + count);
								
					    	});
					    	break;
					    }
					}
				}//end of run
			
			
		}//end of client thread
	}
	
	
	
	
	public void startServer(ActionEvent e) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/serverConsole.fxml"));
        Parent root2;
		root2 = loader.load();
		myctr = loader.getController();//get controller created by FXMLLoader
        root.getScene().setRoot(root2);//update scene graph
		
		serverConnection = new Server(data -> {
				Platform.runLater(()->{
						myctr.clientCount.setText("Connected clients: " + count);
				});
		});

	}
	
	
	
	
	
}
