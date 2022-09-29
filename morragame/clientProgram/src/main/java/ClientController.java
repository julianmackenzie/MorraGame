
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Vector;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
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
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;


public class ClientController implements Initializable {

	// Menu
	@FXML
	private BorderPane root;
	
	@FXML
	private Button connectClient;
	
	@FXML
	private TextField ipField, portField;
	
	@FXML
	private Label errorMessage;
	
	
	// Game
	@FXML
	private ListView<String> listItems2;
	
	@FXML
	private Button playButton, newButton, quitButton;
	
	@FXML
	private ToggleButton b0, b1, b2, b3, b4, b5, g0, g1, g2, g3, g4, g5, g6, g7, g8, g9, g10;
	
	@FXML
	private Label p1ScoreLabel, p2ScoreLabel;
	

	
	MorraInfo info;
	
	
	
	
	
	Client clientConnection;
	
	int playerNumber = 0;
	

	ClientController myctr;
	
	
	
	public void playPressed(ActionEvent e) throws IOException {
		
			
			if (playerNumber == 1) {
				if (b0.isSelected()) info.p1Plays = 0;
				else if (b1.isSelected()) info.p1Plays = 1;
				else if (b2.isSelected()) info.p1Plays = 2;
				else if (b3.isSelected()) info.p1Plays = 3;
				else if (b4.isSelected()) info.p1Plays = 4;
				else if (b5.isSelected()) info.p1Plays = 5;
				else info.p1Plays = 0;
				
				if (g0.isSelected()) info.p1Guess = 0;
				else if (g1.isSelected()) info.p1Guess = 1;
				else if (g2.isSelected()) info.p1Guess = 2;
				else if (g3.isSelected()) info.p1Guess = 3;
				else if (g4.isSelected()) info.p1Guess = 4;
				else if (g5.isSelected()) info.p1Guess = 5;
				else if (g6.isSelected()) info.p1Guess = 6;
				else if (g7.isSelected()) info.p1Guess = 7;
				else if (g8.isSelected()) info.p1Guess = 8;
				else if (g9.isSelected()) info.p1Guess = 9;
				else if (g10.isSelected()) info.p1Guess = 10;
				else info.p1Guess = 0;
			}
			else if (playerNumber == 2) {
				if (b0.isSelected()) info.p2Plays = 0;
				else if (b1.isSelected()) info.p2Plays = 1;
				else if (b2.isSelected()) info.p2Plays = 2;
				else if (b3.isSelected()) info.p2Plays = 3;
				else if (b4.isSelected()) info.p2Plays = 4;
				else if (b5.isSelected()) info.p2Plays = 5;
				else info.p2Plays = 0;
				
				if (g0.isSelected()) info.p2Guess = 0;
				else if (g1.isSelected()) info.p2Guess = 1;
				else if (g2.isSelected()) info.p2Guess = 2;
				else if (g3.isSelected()) info.p2Guess = 3;
				else if (g4.isSelected()) info.p2Guess = 4;
				else if (g5.isSelected()) info.p2Guess = 5;
				else if (g6.isSelected()) info.p2Guess = 6;
				else if (g7.isSelected()) info.p2Guess = 7;
				else if (g8.isSelected()) info.p2Guess = 8;
				else if (g9.isSelected()) info.p2Guess = 9;
				else if (g10.isSelected()) info.p2Guess = 10;
				else info.p2Guess = 0;
			}
		
		clientConnection.send();
		Platform.runLater(()->{playButton.setDisable(true);});
			
		
	}
	

	public void newPressed() throws IOException {
		if (playerNumber == 1) {
			info.cmd = 9001; // code 9001 = p1Again
			info.p1Again = true;
		}
		else if (playerNumber == 2) {
			info.cmd = 9002; // code 9002 = p2Again
			info.p2Again = true;
		}
		clientConnection.send();
		
		Platform.runLater(()->{
			newButton.setDisable(true);
			quitButton.setDisable(true);
		});

	}
	
	public void quitPressed() throws IOException {
		info.cmd = -5;
		clientConnection.send();
		Platform.exit();
	}
	
	
	public void startConnection(String ipField, String portField) {
		clientConnection = new Client(ipField, portField, data->{
			info = (MorraInfo) data;
			
			// if a player was just assigned a number
			if (info.cmd == 1 || info.cmd == 2) {
				if (info.cmd == 1) Platform.runLater(()->{listItems2.getItems().add("You are Player 1.");
				playerNumber = 1;});
				else if (info.cmd == 2) Platform.runLater(()->{listItems2.getItems().add("You are Player 2.");
				playerNumber = 2;});
			}
			
			// if a turn was just played
			else if (info.cmd == 50) {
				Platform.runLater(()->{
					listItems2.getItems().add("Player 1 played " + info.p1Plays + ", Player 2 played " + info.p2Plays);
					listItems2.getItems().add("Total fingers played was " + (info.p1Plays + info.p2Plays));
					listItems2.getItems().add("Player 1 guessed " + info.p1Guess + ", Player 2 guessed " + info.p2Guess);
				});
				if (info.p1Guess == (info.p1Plays + info.p2Plays) && info.p1Guess != info.p2Guess) {
					Platform.runLater(()->{listItems2.getItems().add("Player 1 scores a point!");});
				}
				else if (info.p2Guess == (info.p1Plays + info.p2Plays) && info.p1Guess != info.p2Guess) {
					Platform.runLater(()->{listItems2.getItems().add("Player 2 scores a point!");});
				}
				
				
				
				
				if (info.win == true) {
					Platform.runLater(()->{
						if (info.p1Score == 2) Platform.runLater(()->{listItems2.getItems().add("Player 1 wins!");});
						else if (info.p2Score == 2) Platform.runLater(()->{listItems2.getItems().add("Player 2 wins!");});
						newButton.setVisible(true);
						newButton.setDisable(false);
						quitButton.setVisible(true);
						quitButton.setDisable(false);
						playButton.setVisible(false);
					});
				} else {
					Platform.runLater(()->{playButton.setDisable(false);});
				}
				// update scores
				Platform.runLater(()->{
					p1ScoreLabel.setText("Player 1: " + info.p1Score);
					p2ScoreLabel.setText("Player 2: " + info.p2Score);
				});
			}
			
			// if someone's connecting
			else if (info.cmd == 5) {
				Platform.runLater(()->{listItems2.getItems().add("Player connected!");});
				if (info.have2players == false) {
					Platform.runLater(()->{playButton.setVisible(false);});
				}
				if (info.have2players == true) {
					Platform.runLater(()->{playButton.setVisible(true);});
				}
			}
			
			// if someone's disconnecting
			else if (info.cmd == -5) {
				Platform.runLater(()->{listItems2.getItems().add("Player disconnected!");});
				if (info.have2players == false) {
					Platform.runLater(()->{playButton.setVisible(false);});
				}
				if (info.have2players == true) {
					Platform.runLater(()->{playButton.setVisible(true);});
				}
			}
			
			// if both choose to play again (reset)
			else if (info.cmd == 9000) {
				info.p1Plays = 0;
				info.p2Plays = 0;
				info.p1Guess = 0;
				info.p2Guess = 0;
				info.p1Moved = false;
				info.p2Moved = false;
				info.p1Score = 0;
				info.p2Score = 0;
				info.p1Again = false;
				info.p2Again = false;
				info.bothPlayed = false;
				info.playing = false;
				info.have2players = true;
				info.win = false;
				Platform.runLater(()->{
					p1ScoreLabel.setText("Player 1: 0");
					p2ScoreLabel.setText("Player 2: 0");
					newButton.setVisible(false);
					newButton.setDisable(true);
					quitButton.setVisible(false);
					quitButton.setDisable(true);
					playButton.setVisible(true);
					playButton.setDisable(false);
				});
			}
			
			
			});
		clientConnection.start();
		
		if (clientConnection.canConnect == false) {
			errorMessage.setVisible(true);
			System.out.println("Error: could not connect client");
			return;
		}
		
		
		
	}
	
	
	public void connectClient(ActionEvent e) throws IOException {
	
		
		
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/clientGame.fxml"));
	        Parent root2;
			root2 = loader.load();
			myctr = loader.getController();//get controller created by FXMLLoader
			myctr.startConnection(ipField.getText(), portField.getText());
	        root.getScene().setRoot(root2);//update scene graph
        
	}





	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	
	
	
	
	
}
