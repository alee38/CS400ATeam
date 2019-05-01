package application;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import org.json.simple.parser.ParseException;

import application.Inventory;
import application.Inventory.HashNode;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;


/*
 * This class creates a main page GUI that allows users to see a list
 * of all inventory items. From here, they can search for a specific item
 * and see a description page, or they can click "add" to be brought to the 
 * add page.
 */
public class Main extends Application implements EventHandler<ActionEvent> {

  private Inventory inventory = new Inventory();
  private TableView<Computer> table = new TableView<Computer>();
  private final ObservableList<Computer> data =
      FXCollections.observableArrayList(
);
  
  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) throws FileNotFoundException, IOException, ParseException {
    Scene scene = new Scene(new Group());
    stage.setTitle("CompHelp Inventory");
    stage.setWidth(450);
    stage.setHeight(575);

    // Read in JSON file and place data in observable list
    String file = "C:/Users/Andrew/eclipse-workspace/ATeam/CS400ATeam/output.json";
    inventory.readFile(file);
    HashMap<String,HashNode> table1 = inventory.getTable();
    for (String key:table1.keySet()) {
    	data.addAll(new Computer(table1.get(key).key, table1.get(key).location,table1.get(key).date ));
    }  		
    
    
    final Label label = new Label("Information Search");
    label.setFont(new Font("Arial", 20));

    table.setEditable(true);

    TableColumn itemCol = new TableColumn("Item");
    itemCol.setMinWidth(200);
    itemCol.setCellValueFactory(new PropertyValueFactory<Computer, String>("firstName"));

    TableColumn locationCol = new TableColumn("Location");
    locationCol.setMinWidth(100);
    locationCol.setCellValueFactory(new PropertyValueFactory<Computer, String>("lastName"));

    TableColumn dateCol = new TableColumn("Date");
    dateCol.setMinWidth(100);
    dateCol.setCellValueFactory(new PropertyValueFactory<Computer, String>("email"));

    FilteredList<Computer> fileComputer = new FilteredList(data, p -> true);// Pass the data to a
                                                                            // filtered list
    table.setItems(fileComputer);// Set the table's items using the filtered list
    table.getColumns().addAll(itemCol, locationCol, dateCol);


    ChoiceBox<String> choiceBox = new ChoiceBox();
    choiceBox.getItems().addAll("Item", "Location", "Date");
    choiceBox.setValue("Item");

    TextField textField = new TextField();
    textField.setPromptText("Search here!");
    textField.setOnKeyReleased(keyEvent -> {
      switch (choiceBox.getValue())// Switch on choiceBox value
      {
        case "Item":
          fileComputer.setPredicate(p -> p.getFirstName().toLowerCase()
              .contains(textField.getText().toLowerCase().trim()));// filter table by first name
          break;
        case "Location":
          fileComputer.setPredicate(p -> p.getLastName().toLowerCase()
              .contains(textField.getText().toLowerCase().trim()));// filter table by first name
          break;
        case "Date":
          fileComputer.setPredicate(
              p -> p.getEmail().toLowerCase().contains(textField.getText().toLowerCase().trim()));
          break;
      }
    });

    choiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
      if (newVal != null) {
        textField.setText("");
        fileComputer.setPredicate(null);// This is same as saying flPerson.setPredicate(p->true);
      }
    });
    
    Button search = new Button("Search");
    Button close = new Button("Close");
    
    // Handle Close button
    close.setOnAction((ActionEvent e) -> {
    
    	BorderPane root = new BorderPane();
        Scene saveScene = new Scene(root, 200, 50);
        Stage save = new Stage();
        save.setScene(saveScene);
        save.setX(400);
        save.setY(50);
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
    
    HBox hBox = new HBox(choiceBox, textField, search);// Add choiceBox and textField to hBox
    hBox.setAlignment(Pos.CENTER);// Center HBox
    final VBox vbox = new VBox();
    vbox.setSpacing(5);
    vbox.setPadding(new Insets(10, 0, 0, 10));

    
    // Handle the Add button by going to add page
    Button b1 = new Button("Add");
    b1.setOnAction(new EventHandler<ActionEvent>() {

      @Override
      public void handle(ActionEvent event) {

        FlowPane pane = new FlowPane(Orientation.VERTICAL);
        pane.setHgap(5);
        pane.setAlignment(Pos.CENTER);
        pane.setPadding(new Insets(0, 0, 0, 0));

        TextField tf1 = new TextField("computer-1001");
        itemCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        TextField tf2 = new TextField("location");
        locationCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        TextField tf3 = new TextField("dd/mm/yyyy");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("email"));

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

        newWindow.setX(600);
        newWindow.setY(500);
        newWindow.show();

        Button button1 = new Button("Add");
        Button button2 = new Button("Cancel");
        button1.setOnAction((ActionEvent e) -> {
          data.add(new Computer(tf1.getText(), tf2.getText(), tf3.getText()));
          tf1.clear();
          tf2.clear();
          tf3.clear();  
          
          // Creates "Successfully Added" window on click
          BorderPane root = new BorderPane();
          Scene successScene = new Scene(root, 200, 200);
          Stage success = new Stage();
          success.setScene(successScene);
          success.setX(600);
          success.setY(500);
          Label successLabel = new Label("Successfully Added!");
          root.setCenter(successLabel);
          success.show();
          newWindow.close();
          
        });
        button2.setOnAction((ActionEvent e) -> {
          newWindow.close();
        });
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(button1, button2);
        vBox.setSpacing(20);

      }
    });
    vbox.getChildren().addAll(label, table, hBox, b1, close);

    ((Group) scene.getRoot()).getChildren().addAll(vbox);

    stage.setScene(scene);
    stage.show();
  }

  // Because we need to have it
  public void handle(ActionEvent event) {}

  public static class Computer {

    private final SimpleStringProperty item;
    private final SimpleStringProperty location;
    private final SimpleStringProperty date;

    private Computer(String itemName, String locationName, String dataName) {
      this.item = new SimpleStringProperty(itemName);
      this.location = new SimpleStringProperty(locationName);
      this.date = new SimpleStringProperty(dataName);
    }

    public String getFirstName() {
      return item.get();
    }

    public void setFirstName(String fName) {
      item.set(fName);
    }

    public String getLastName() {
      return location.get();
    }

    public void setLastName(String fName) {
      location.set(fName);
    }

    public String getEmail() {
      return date.get();
    }

    public void setEmail(String fName) {
      date.set(fName);
    }
  }
}