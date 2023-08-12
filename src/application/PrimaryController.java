package application;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import application.DAO.EmployeeDao;
import application.DTO.Employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

public class PrimaryController {

	public static Employee modifiedEmployeeObject; // staticnek kell lennie, mert a dao midifynak
													// csak long paramétere van
	private long selectedId;
	@FXML
	TableView<Employee> employeeTableView;
	@FXML
	Button registerNewEmployee;

	@FXML
	TableColumn<Employee, Long> idColumn;
	@FXML
	TableColumn<Employee, String> firstNameColumn;
	@FXML
	TableColumn<Employee, String> lastNameColumn;
	@FXML
	TableColumn<Employee, String> emailColumn;
	@FXML
	TableColumn<Employee, String> genderColumn;
	@FXML
	TableColumn<Employee, String> jobTitleColumn;
	@FXML
	TableColumn<Employee, Integer> salaryColumn;
	@FXML
	TableColumn<Employee, String> languageColumn;
	@FXML
	Button listAllButton;
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
	Button modifySelectedButton;
	@FXML
	Button deleteSelectedButton;
	@FXML
	Button searchButton;
	@FXML
	Button modifyButton;
	@FXML
	Button cancelButton;
	@FXML
	Button refreshButton;
	@FXML
	Label genderLabel;
	@FXML
	Label salaryLabel;
	@FXML
	ToggleGroup genderToggleGroup;
	@FXML
	RadioButton maleRadioButton;
	@FXML
	RadioButton femaleRadioButton;
	@FXML
	RadioButton otherRadioButton;
	@FXML
	TextField otherGenderTF;
	@FXML
	VBox genderVbox;



	@FXML
	private void switchToSecondary() throws IOException {
		Main.setRoot("secondary");
	}

	@FXML
	private void switchToThird() throws IOException {
		Main.setRoot("third");

	}


//Listázza az összes employeet, láthatóvá teszi a módosít, töröl és keres gombokat, deaktiválja
//	a módosítás funkciót, törli a textfieldek tartalmát.
	@FXML
	private void listAllEmployeesInTableView() {
		// meghívja a dao getAll methodját, ez listába rakja az employeekat
		EmployeeDao employeeDaoObj = new EmployeeDao();
		List<Employee> allEmployees = employeeDaoObj.getAll();
		// Csak ObservableListet tud kezelni a tableview
		ObservableList<Employee> employeeObservableList = FXCollections.observableArrayList();

		// a kereséshez kell egy kis extra infó
		genderLabel.setText("Gender (exact match only):");
		salaryLabel.setText("Salary (>=) : ");

		// ez állítja be, hogy egy employee obj fieldjét mely oszlopban jelenítsük meg
		idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
		firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
		lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
		emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
		genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
		jobTitleColumn.setCellValueFactory(new PropertyValueFactory<>("jobTitle"));
		salaryColumn.setCellValueFactory(new PropertyValueFactory<>("salary"));
		languageColumn.setCellValueFactory(new PropertyValueFactory<>("language"));

		// Az observablelistbe beteszi az arraylistet
		employeeObservableList.addAll(allEmployees);
		employeeTableView.refresh();

		// A tableview feltöltése
		employeeTableView.setItems(employeeObservableList);


		modifySelectedButton.setVisible(true);
		deleteSelectedButton.setVisible(true);
		cancelButton.setVisible(false);
		modifyButton.setVisible(false);
		searchButton.setVisible(true);
		genderTF.setVisible(false);
		maleRadioButton.setVisible(true);
		femaleRadioButton.setVisible(true);
		otherRadioButton.setVisible(true);

		clearTextFields();
		salaryTF.setText("0"); // ez a keresésben segít


	}



	@FXML
	private void searchAndListEmployeesInTableView() {
		EmployeeDao employeeDaoObj = new EmployeeDao();
		// parseolás miatt kell a trycatch
		try {
			String firstName = firstNameTF.getText();
			String lastName = lastNameTF.getText();
			String email = emailTF.getText();
			String gender = getGenderFromRadioButton(); // külön method kellett rá
			String jobTitle = jobTitleTF.getText();
			String language = languageTF.getText();
			int salary = Integer.parseInt(salaryTF.getText());

			// listába szedjük a TF-ek tartalma alapján való keresés eredményét
			List<Employee> foundEmployees = employeeDaoObj.searchEmployees(firstName, lastName,
					email, gender, jobTitle, language, salary);
			// a listát observablelistté kell alakítani, hogy a tableviewba be lehessen
			// tölteni
			ObservableList<Employee> employeeObservableList = FXCollections.observableArrayList();
			employeeObservableList.addAll(foundEmployees);

			// ez állítja be, hogy egy employee obj fieldjét mely oszlopban jelenítsük meg
			idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
			firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
			lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
			emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
			genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
			jobTitleColumn.setCellValueFactory(new PropertyValueFactory<>("jobTitle"));
			salaryColumn.setCellValueFactory(new PropertyValueFactory<>("salary"));
			languageColumn.setCellValueFactory(new PropertyValueFactory<>("language"));

			// observablelist betöltése a tableviewba
			employeeTableView.refresh();
			employeeTableView.setItems(employeeObservableList);

			modifySelectedButton.setVisible(true);
			deleteSelectedButton.setVisible(true);
			cancelButton.setVisible(false);
			modifyButton.setVisible(false);
			searchButton.setVisible(true);

			// System.out.println(foundEmployees);



		} catch (NumberFormatException e) {
			showErrorMessage("The given format is not acceptable at the salary field.");
			e.printStackTrace();
		}



	}



//A táblázatban kijelölt alkalmazott adatait kiteszi a textfieldekbe a modify lenyomásakor
//megjeleníti a módosít és vissza gombokat
	@FXML
	private void getSelectedEmployeeDetailsToTextFields() {
		// Létrehoz egy employee objectet a kijelölt sorból
		Employee selectedEmployee = employeeTableView.getSelectionModel().getSelectedItem();

		// Ez azért kell mert már nem keresőmezőként funkicionál a két TF
		genderLabel.setText("Gender:");
		salaryLabel.setText("Salary: ");
		genderTF.setVisible(true);
		maleRadioButton.setVisible(false);
		femaleRadioButton.setVisible(false);
		otherRadioButton.setVisible(false);


		if (selectedEmployee != null) {
			selectedId = selectedEmployee.getId(); // Ezt fieldként hoztam létre, mert a
													// modifySelectedEmployee methodban is használom

			// System.out.println(selectedId); //debug

			// Kirakja a textFieldekbe a kiválasztott employee adatait
			firstNameTF.setText(selectedEmployee.getFirstName());
			lastNameTF.setText(selectedEmployee.getLastName());
			emailTF.setText(selectedEmployee.getEmail());
			genderTF.setText(selectedEmployee.getGender());
			jobTitleTF.setText(selectedEmployee.getJobTitle());
			languageTF.setText(selectedEmployee.getLanguage());
			salaryTF.setText(selectedEmployee.getSalary() + "");

			// System.out.println(firstName);


			searchButton.setVisible(false);
			modifyButton.setVisible(true);
			cancelButton.setVisible(true);
		}
	}



// Ez maga a módosítás methodja, a bal alsó modify gomb hívja meg
	@FXML
	private void modifySelectedEmployee() throws IOException {
		EmployeeDao daoObj = new EmployeeDao();


		modifiedEmployeeObject = getEmployeeDetailsFromTextFields(); // befrissíti a static mezőt


		// Meghívjuk a DAO-ból a módosítást, paraméterként az id, amit módosítani kell
		// és az új, módosított employee

		daoObj.updateById(selectedId);

		// át kell váltani a harmadik képernyőre, csak így lehet forceolni a tábla
		// frissítését modify esetén
		switchToThird();
	}


//A textfieldek aktuális tartalmából létrehoz egy employee objectet.
	public Employee getEmployeeDetailsFromTextFields() {
		Employee modifiedEmployee = null;
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
				showErrorMessage("None of the text fields can be left empty");
			else {
				salary = Integer.parseInt(salaryTF.getText());

				// A TF-ek alapján létrejön a módosított employee object

				modifiedEmployee = new Employee(firstName, lastName, email, gender,
						jobTitle,
						salary, language);
			}
		} catch (NumberFormatException e) {
			showErrorMessage("The given format is not acceptable at the salary field");
		}



		return modifiedEmployee;
	}


//Ez a delete gombos id alapján való törlés methodja	
	@FXML
	private void deleteSelectedEmployee() {

		EmployeeDao daoObj = new EmployeeDao();
		Employee selectedEmployee = employeeTableView.getSelectionModel().getSelectedItem();

		// feldog egy megerősítőablakot, csak akkor megy tovább, ha megerősíti
		boolean userConfirmed = showConfirmationDialog(
				"Are you sure, you want to delete employee " + selectedEmployee.getFirstName() + " "
						+ selectedEmployee.getLastName() + " from the database?");


		if (userConfirmed) {
			try {
				daoObj.deleteById(selectedEmployee.getId());
				listAllEmployeesInTableView(); // itt nem kell átdobni a 3. képernyőre, ha módosul a
												// listában a darabszám befrissíti a tableviewt.
			} catch (Exception e) {
				e.printStackTrace();
				showErrorMessage("Failed to delete employee!");
			}
		}


	}



//hibaablakot dob egyedi szöveggel
	private void showErrorMessage(String message) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("Error, action needed");
		alert.setContentText(message);
		alert.setHeaderText("Error happened during the registration.");
		alert.showAndWait();
	}

//confirmation ablakot dob
	private boolean showConfirmationDialog(String message) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Confirmation");
		alert.setContentText(message);
		alert.setHeaderText("Please Confirm!");
		Optional<ButtonType> result = alert.showAndWait();

		// alert.showAndWait();
		return result.isPresent() && result.get() == ButtonType.OK;
	}

//Törli a textfieldek tartalmát	
	private void clearTextFields() {
		firstNameTF.setText("");
		lastNameTF.setText("");
		emailTF.setText("");
		genderTF.setText("");
		jobTitleTF.setText("");
		languageTF.setText("");
		salaryTF.setText("");

	}

//Megadja a Gendert szövegesen radiobutton alapján
	public String getGenderFromRadioButton() {
		String genderFromRB = "";
		String genderFromUserData = (String) genderToggleGroup.getSelectedToggle().getUserData();

		if (genderFromUserData.equals("Other")) {
			genderFromRB = otherGenderTF.getText();
		} else {
			genderFromRB = genderFromUserData;
		}
		System.out.println(genderFromRB);
		return genderFromRB;

	}



}

