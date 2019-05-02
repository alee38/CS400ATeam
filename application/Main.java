package application;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.zip.CheckedInputStream;
import org.json.simple.parser.ParseException;
import application.Inventory;
// import application.Inventory.HashNode;
import application.Main.Computer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;


/*
 * This class creates a main page GUI that allows users to see a list of all inventory items. From
 * here, they can search for a specific item and see a description page, or they can click "add" to
 * be brought to the add page.
 */
public class Main extends Application implements EventHandler<ActionEvent> {

	private Inventory inventory = new Inventory(); // Instance of hash table
	private TableView<Computer> table = new TableView<Computer>();
	private final ObservableList<Computer> data = FXCollections.observableArrayList();
	Comparator<Computer> comparator = Comparator.comparing(Computer::getName);

	public static void main(String[] args) {
		launch(args);
	}

	/*
	 * check input in add page follows the following format: 
	 * name: "itemname-000" (must have a string and a number connected by "-" ) 
	 * location: can only be numbers 
	 * date: dd/mm/yyyy
	 */
	public void checkInput(String inputname, String inputlocation, String inputdate)
			throws Exception {

		try {
			if (inventory.getTable().containsKey(inputname)) {
				throw new Exception();
			}
			String[] name = inputname.split("-");
			Integer.valueOf(name[1]);

		} catch (Exception e) {
			throw new Exception();
		}
		try {
			String[] date = inputdate.split("/");
			String day = date[0];
			String month = date[1];
			String year = date[2];
			if (year.length() != 4) {
				throw new Exception();
			} else if (month.length() != 2 || Integer.valueOf(month) > 12
					|| Integer.valueOf(month) < 1) {
				throw new Exception();
			} else if (day.length() != 2 || Integer.valueOf(day) > 31
					|| Integer.valueOf(month) < 1) {
				throw new Exception();
			}
		} catch (Exception e) {
			throw new Exception();
		}
		try {
			int room = Integer.valueOf(inputlocation);
			if (room < 1) {
				throw new Exception();
			}
		} catch (Exception e) {
			throw new Exception();
		}

	}


	@Override
	public void start(Stage stage) throws FileNotFoundException, IOException, ParseException {

		Scene scene = new Scene(new Group());
		stage.setTitle("CompHelp Inventory");
		stage.setWidth(450);
		stage.setHeight(575);

		final Label label = new Label("Information Search");
		label.setFont(new Font("Arial", 20));

		TableColumn itemCol = new TableColumn("Item");
		itemCol.setMinWidth(200);
		itemCol.setCellValueFactory(new PropertyValueFactory<Computer, String>("name"));

		TableColumn locationCol = new TableColumn("Location");
		locationCol.setMinWidth(100);
		locationCol.setCellValueFactory(new PropertyValueFactory<Computer, String>("location"));

		TableColumn dateCol = new TableColumn("Date");
		dateCol.setMinWidth(100);
		dateCol.setCellValueFactory(new PropertyValueFactory<Computer, String>("date"));


		// Read in JSON file and place data in observable list
		String file = "output.json";
		inventory.readFile(file);
		HashMap<String, HashNode> table1 = inventory.getTable();
		for (String key : table1.keySet()) {
			data.addAll(new Computer(table1.get(key).key, table1.get(key).location,
					table1.get(key).date));
		}

		table.setEditable(true);


		/*
		 * DESCRIPTION PAGE
		 * 
		 * Table in center of Main page displays all items currently in the hash table.
		 * If a user clicks an item, a new page will show up with details about the item.
		 */

		FilteredList<Computer> fileComputer = new FilteredList(data, p -> true);// Pass the data to
		// a
		// filtered list
		FXCollections.sort(data, comparator);
		table.setItems(fileComputer);// Set the table's items using the filtered list
		table.getColumns().addAll(itemCol, locationCol, dateCol);
		table.addEventHandler(MouseEvent.MOUSE_PRESSED,
				new EventHandler<MouseEvent>() {
			public void handle(MouseEvent me) {
				try {

					Stage new1 = new Stage();
					GridPane grid = new GridPane();
					Scene scene = new Scene(grid, 300, 200);

					scene.getStylesheets().add(getClass().getResource("application.css")
							.toExternalForm());
					new1.setTitle("Complete Inventory");

					Button returnButton = new Button("Return");
					returnButton.setOnAction(new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent event) {
							returnButton.setOnAction((ActionEvent e) -> {
								new1.close();
							});
						}
					});

					Label itemName = new Label("Computer  ");
					Label location = new Label("Location  ");
					Label t = new Label("Placed There  ");

					HBox name = new HBox(itemName, new Text(table.getSelectionModel()
							.getSelectedItem().getName()));
					HBox loc = new HBox(location, new Text(table.getSelectionModel()
							.getSelectedItem().getLocation()));
					HBox time = new HBox(t, new Text(table.getSelectionModel()
							.getSelectedItem().getDate()));


					grid.add(name, 0, 0);
					grid.add(loc, 0, 1);
					grid.add(time, 0, 2);
					grid.setAlignment(Pos.CENTER);
					grid.add(returnButton, 0, 3);
					grid.setHgap(10);
					grid.setVgap(10);

					new1.setScene(scene);
					new1.show();
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		//////////////////////////////////////////////////////////////////////////


		/*
		 * SAVE PAGE
		 * 
		 * When the close button on the main page is clicked, a new page 
		 * is brought up with 2 buttons: save and don't save. Save will write
		 * to JSON, don't save will simply close the program.
		 */

		Button close = new Button("Close");
		close.setOnAction((ActionEvent e) -> {

			BorderPane root = new BorderPane();
			Scene saveScene = new Scene(root, 200, 50);
			Stage save = new Stage();
			save.setScene(saveScene);
			save.centerOnScreen();
			Button saveButton = new Button("Save");
			Button dontSaveButton = new Button("Exit without Saving");
			root.setLeft(saveButton);
			root.setRight(dontSaveButton);
			save.show();

			// handle save button by saving to JSON
			saveButton.setOnAction((ActionEvent saveAll) -> {
				inventory.writeJSON();
				save.close();
				stage.close();
			});

			// handle Dont Save by closing everything
			dontSaveButton.setOnAction((ActionEvent event) -> {
				save.close();
				stage.close();
			});
		});
		////////////////////////////////////////////////////////////////////////////////


		/*
		 * ADD PAGE
		 * 
		 * Clicking the add button on the main page will bring up a new page prompting
		 * the user to enter the details for the item they wish to add to the table.
		 * From this page, they can click "add" to finalize, or simply quit out to return 
		 * to the main page
		 */

		Button b1 = new Button("Add");
		b1.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				FlowPane pane = new FlowPane(Orientation.VERTICAL);
				pane.setHgap(5);
				pane.setAlignment(Pos.CENTER);
				pane.setPadding(new Insets(0, 0, 0, 0));

				TextField tf1 = new TextField("computer-1001");
				itemCol.setCellValueFactory(new PropertyValueFactory<>("name"));
				TextField tf2 = new TextField("location");
				locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
				TextField tf3 = new TextField("dd/mm/yyyy");
				dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

				tf1.setPrefColumnCount(14);
				tf2.setPrefColumnCount(14);
				tf3.setPrefColumnCount(14);

				Label l1 = new Label("Item Name: ");
				Label l2 = new Label("Previous Room: ");
				Label l3 = new Label("Date Item Was Moved: ");

				pane.getChildren().addAll(l1, tf1, l2, tf2, l3, tf3);

				HBox vBox = new HBox();
				BorderPane borderPane = new BorderPane();
				borderPane.setCenter(pane);
				borderPane.setBottom(vBox);
				BorderPane.setAlignment(vBox, Pos.CENTER);

				Scene secondScene = new Scene(borderPane, 350, 450);


				Stage newWindow = new Stage();
				newWindow.setTitle("Add Item");
				newWindow.setScene(secondScene);

				newWindow.centerOnScreen();
				newWindow.show();

				// Add button on the Add page. Checks for valid input and then adds to table.
				Button button1 = new Button("Add");
				button1.setOnAction((ActionEvent e) -> {
					try {
						checkInput(tf1.getText(), tf2.getText(), tf3.getText()); // update hashtable                    
						data.add(new Computer(tf1.getText(), tf2.getText(), tf3.getText()));
						FXCollections.sort(data, comparator);
						table1.put(tf1.getText(),
								new HashNode(tf1.getText(), tf2.getText(), tf3.getText()));
						tf1.clear();
						tf2.clear();
						tf3.clear();

						// Creates "Successfully Added" window on click
						BorderPane root = new BorderPane();
						Scene successScene = new Scene(root, 200, 200);
						Stage success = new Stage();
						success.setScene(successScene);
						success.centerOnScreen();
						Label successLabel = new Label("Successfully Added!");
						root.setCenter(successLabel);
						success.show();
						newWindow.close();
					} catch (Exception ex) {

						// Creates "Warning" window when invalid input detected
						GridPane grid = new GridPane();
						Scene warningscene = new Scene(grid, 200, 200);
						Stage warning = new Stage();
						warning.setTitle("Warning");
						warning.setScene(warningscene);
						warning.centerOnScreen();
						Label successLabel = new Label("Wrong Format!");
						Label namewarning = new Label("Item: \"ItemName-000\"");
						Label locationwarning = new Label("Location: \"0000\"");
						Label datewarning = new Label("Date: \"dd/mm/yyyy\"");

						grid.add(successLabel, 0, 0);
						grid.addRow(1, new Label(""));
						grid.add(namewarning, 0, 2);
						grid.add(locationwarning, 0, 3);
						grid.add(datewarning, 0, 4);
						grid.setAlignment(Pos.CENTER);

						warning.show();
						newWindow.close();
					}

				});

				// Cancel button on Add page. Closes window.
				Button button2 = new Button("Cancel");
				button2.setOnAction((ActionEvent e) -> {
					newWindow.close();
				});
				vBox.setAlignment(Pos.CENTER);
				vBox.getChildren().addAll(button1, button2);
				vBox.setSpacing(20);

			}
		});
		/////////////////////////////////////////////////////////////////////////

		
		// Finish setting up GUI with item underneath the table
		
		ChoiceBox<String> choiceBox = new ChoiceBox();
		choiceBox.getItems().addAll("Item", "Location", "Date");
		choiceBox.setValue("Item");

		TextField textField = new TextField();
		textField.setPromptText("Search here!");
		textField.setOnKeyReleased(keyEvent -> {
			switch (choiceBox.getValue())// Switch on choiceBox value
			{
			case "Item":
				fileComputer.setPredicate(p -> p.getName().toLowerCase()
						.contains(textField.getText().toLowerCase().trim()));// filter
																	// table by item name
				break;
			case "Location":
				fileComputer.setPredicate(p -> p.getLocation().toLowerCase()
						.contains(textField.getText().toLowerCase().trim()));// filter
																		// table by location
				break;
			case "Date":
				fileComputer.setPredicate(p -> p.getDate().toLowerCase()
						.contains(textField.getText().toLowerCase().trim()));// filter
																		// table by date
				break;
			}
		});

		choiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal != null) {
				textField.setText("");
				fileComputer.setPredicate(null);// This is same as saying
												// flPerson.setPredicate(p->true);
			}
		});

		Label numItems = new Label("# Items = " + data.size());
		HBox hBox = new HBox(choiceBox, textField, numItems);
		hBox.setAlignment(Pos.CENTER);// Center HBox
		final VBox vbox = new VBox();
		vbox.setSpacing(5);
		vbox.setPadding(new Insets(10, 0, 0, 10));      
		vbox.getChildren().addAll(label, table, hBox, b1, close);

		((Group) scene.getRoot()).getChildren().addAll(vbox);

		stage.setScene(scene);
		stage.show();
	}

	// Because we need to have it
	public void handle(ActionEvent event) {}


	/*
	 * Represents the key:Node pairs in the hash table.
	 */
	public static class Computer {

		private final SimpleStringProperty item;
		private final SimpleStringProperty location;
		private final SimpleStringProperty date;

		private Computer(String itemName, String locationName, String dataName) {
			this.item = new SimpleStringProperty(itemName);
			this.location = new SimpleStringProperty(locationName);
			this.date = new SimpleStringProperty(dataName);
		}

		public String getName() {
			return item.get();
		}

		public void setName(String fName) {
			item.set(fName);
		}

		public String getLocation() {
			return location.get();
		}

		public void setLocation(String loc) {
			location.set(loc);
		}

		public String getDate() {
			return date.get();
		}

		public void setDate(String dat) {
			date.set(dat);
		}
	}
}
