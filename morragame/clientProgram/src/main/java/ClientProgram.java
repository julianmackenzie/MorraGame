

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class ClientProgram extends Application {

	
	@Override
	public void start(Stage primaryStage) throws Exception {
		 try {
	            // Read file fxml and draw interface.
	            Parent root = FXMLLoader.load(getClass()
	                    .getResource("/clientMenu.fxml"));
	 
	            primaryStage.setTitle("Client Program");
			 Scene s1 = new Scene(root, 640,480);
			 	s1.getStylesheets().add("playerStyle.css");
	            primaryStage.setScene(s1);
	            primaryStage.show();
	         
	        } catch(Exception e) {
	            e.printStackTrace();
	            System.exit(1);
	        }
		 
		 primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
	            @Override
	            public void handle(WindowEvent t) {
	                Platform.exit();
	                System.exit(0);
	            }
	        });
	}
	
	public static void main(String[] args) {
		launch(args);

	}

}