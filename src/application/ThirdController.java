package application;

import java.io.IOException;

import javafx.fxml.FXML;

public class ThirdController {

//Ez csak azért kellett, mert ha nem frissül a darabszám a tableben, nem frissítette sehogy



	@FXML
	private void switchToPrimary() throws IOException {
		Main.setRoot("primary");
	}



}
