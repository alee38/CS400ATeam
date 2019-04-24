package application;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application
{

    private TableView<Computer> table = new TableView<Computer>();
    private final ObservableList<Computer> data
            = FXCollections.observableArrayList(
                    new Computer("Computer-1001", "9313", "03/15/2019"),
                    new Computer("Computer-1002", "9313", "03/16/2019"),
                    new Computer("Computer-1003", "1315", "03/13/2019")
            );

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage stage)
    {
        Scene scene = new Scene(new Group());
        stage.setTitle("CompHelp Inventory");
        stage.setWidth(450);
        stage.setHeight(550);

        final Label label = new Label("Information Search");
        label.setFont(new Font("Arial", 20));

        table.setEditable(true);

        TableColumn itemCol = new TableColumn("Item");
        itemCol.setMinWidth(200);
        itemCol.setCellValueFactory(
                new PropertyValueFactory<Computer, String>("firstName"));

        TableColumn locationCol = new TableColumn("Location");
        locationCol.setMinWidth(100);
        locationCol.setCellValueFactory(
                new PropertyValueFactory<Computer, String>("lastName"));

        TableColumn dateCol = new TableColumn("Date");
        dateCol.setMinWidth(100);
        dateCol.setCellValueFactory(
                new PropertyValueFactory<Computer, String>("email"));

        FilteredList<Computer> fileComputer = new FilteredList(data, p -> true);//Pass the data to a filtered list
        table.setItems(fileComputer);//Set the table's items using the filtered list
        table.getColumns().addAll(itemCol, locationCol, dateCol);
        
 
        //Adding ChoiceBox and TextField here!
        ChoiceBox<String> choiceBox = new ChoiceBox();
        choiceBox.getItems().addAll("Item", "Location", "Date");
        choiceBox.setValue("Item");

        TextField textField = new TextField();
        textField.setPromptText("Search here!");
        textField.setOnKeyReleased(keyEvent ->
        {
            switch (choiceBox.getValue())//Switch on choiceBox value
            {
                case "Item":
                    fileComputer.setPredicate(p -> p.getFirstName().toLowerCase().contains(textField.getText().toLowerCase().trim()));//filter table by first name
                    break;
                case "Location":
                    fileComputer.setPredicate(p -> p.getLastName().toLowerCase().contains(textField.getText().toLowerCase().trim()));//filter table by first name
                    break;
                case "Date":
                    fileComputer.setPredicate(p -> p.getEmail().toLowerCase().contains(textField.getText().toLowerCase().trim()));//filter table by first name
                    break;
            }
        });

        choiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) ->
        {//reset table and textfield when new choice is selected
            if (newVal != null)
            {
                textField.setText("");
                fileComputer.setPredicate(null);//This is same as saying flPerson.setPredicate(p->true);
            }
        });
        HBox hBox = new HBox(choiceBox, textField);//Add choiceBox and textField to hBox
        hBox.setAlignment(Pos.CENTER);//Center HBox
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        Button b1 = new Button("Add");
        vbox.getChildren().addAll(label, table, hBox, b1);

        ((Group) scene.getRoot()).getChildren().addAll(vbox);
        
        final TextField addItemName = new TextField();
        addItemName.setPromptText("Item Name");
        addItemName.setMaxWidth(itemCol.getPrefWidth());
        final TextField addlocation = new TextField();
        addlocation.setMaxWidth(locationCol.getPrefWidth());
        addlocation.setPromptText("Location");
        final TextField addDate = new TextField();
        addDate.setMaxWidth(dateCol.getPrefWidth());
        addDate.setPromptText("date");
 
        final Button addButton = new Button("Add");
        addButton.setOnAction((ActionEvent e) -> {
            data.addAll(new Computer(
                    addItemName.getText(),
                    addlocation.getText(),
                    addDate.getText()));
            addItemName.clear();
            addlocation.clear();
            addDate.clear();
        });
//        Button b1 = new Button("Add");
//        HBox hBox1 = new HBox(b1, textField);
//        hBox1.setAlignment(Pos.BOTTOM_RIGHT);
////        final HBox hb = new HBox();
//        hb.setAlignment(Pos.CENTER);
//        hb.getChildren().addAll(addItemName, addlocation, addDate, addButton);
//        hb.setSpacing(3);
//        final VBox v1 = new VBox();
//        v1.setSpacing(5);
//        v1.setPadding(new Insets(10, 0, 0, 10));
//        v1.getChildren().addAll(label, table, hb);
//        
//        ((Group) scene.getRoot()).getChildren().addAll(v1);
//        Button b1 = new Button("Add");

//        ((Group) scene.getRoot()).getChildren().addAll(b1);
        stage.setScene(scene);
        stage.show();
    }

    public static class Computer
    {

        private final SimpleStringProperty item;
        private final SimpleStringProperty location;
        private final SimpleStringProperty date;

        private Computer(String itemName, String locationName, String dataName)
        {
            this.item = new SimpleStringProperty(itemName);
            this.location = new SimpleStringProperty(locationName);
            this.date = new SimpleStringProperty(dataName);
        }

        public String getFirstName()
        {
            return item.get();
        }

        public void setFirstName(String fName)
        {
            item.set(fName);
        }

        public String getLastName()
        {
            return location.get();
        }

        public void setLastName(String fName)
        {
            location.set(fName);
        }

        public String getEmail()
        {
            return date.get();
        }

        public void setEmail(String fName)
        {
            date.set(fName);
        }
    }
}