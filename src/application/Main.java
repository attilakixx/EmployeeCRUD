package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


public class Main extends Application {

	private static Scene scene;

	@Override
	public void start(Stage primaryStage) {


		try {
			@SuppressWarnings("unused")
			GridPane root = (GridPane) FXMLLoader.load(getClass().getResource("primary.fxml"));
			scene = new Scene(loadFXML("primary"), 1200, 600); // át kellett állítani szélesre
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.setTitle("Employee CRUD"); // ide megy az App címe
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	private static Parent loadFXML(String fxml) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxml + ".fxml"));
		return fxmlLoader.load();
	}

	static void setRoot(String fxml) throws IOException {
		scene.setRoot(loadFXML(fxml));
	}

	public static void main(String[] args) {


		launch(args);

	}

}
