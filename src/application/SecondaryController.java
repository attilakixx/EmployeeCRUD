package application;

import java.io.IOException;

import application.DAO.EmployeeDao;
import application.DTO.Employee;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class SecondaryController {
	// Ez az új employee felvétele ablak
	@FXML
	TextField firstNameTF;
	@FXML
	TextField lastNameTF;
	@FXML
	TextField emailTF;
	@FXML
	TextField genderTF;
	@FXML
	TextField jobTitleTF;
	@FXML
	TextField salaryTF;
	@FXML
	TextField languageTF;
	@FXML
	Button saveButton;



	@FXML
	private void switchToPrimary() throws IOException {
		Main.setRoot("primary");
	}

	@FXML
	private void saveNewEmployee() throws IOException {
		try {

			String firstName = firstNameTF.getText();
			String lastName = lastNameTF.getText();
			String email = emailTF.getText();
			String gender = genderTF.getText();
			String jobTitle = jobTitleTF.getText();
			String language = languageTF.getText();
			int salary;
			if (firstName.isEmpty()
					|| lastName.isEmpty()
					|| email.isEmpty()
					|| gender.isEmpty()
					|| jobTitle.isEmpty()
					|| salaryTF.getText().isEmpty()
					|| language.isEmpty())
				showErrorMessage("None of the textfields can be left empty");
			else {


				salary = Integer.parseInt(salaryTF.getText());



				Employee newEmployee = new Employee(firstName, lastName, email, gender, jobTitle,
						salary,
						language);

				EmployeeDao daoObj = new EmployeeDao();
				daoObj.save(newEmployee);
				switchToThird();

			}
		} catch (NumberFormatException e) {
			showErrorMessage("The given format is not acceptable at the salary field");
			e.printStackTrace();
		}
	}

	@FXML
	private void switchToThird() throws IOException {
		Main.setRoot("third");

	}

	private void showErrorMessage(String message) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("Error, action needed");
		alert.setContentText(message);
		alert.setHeaderText("Error happened during the registration.");
		alert.showAndWait();
	}



}
