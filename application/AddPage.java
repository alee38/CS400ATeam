package application;

 import javafx.application.Application;
 import javafx.geometry.Pos;
 import javafx.scene.Scene;
 import javafx.scene.control.Button;
 import javafx.scene.control.Label;
 import javafx.scene.control.TextField;
 import javafx.scene.layout.BorderPane;
 import javafx.scene.layout.FlowPane;
 import javafx.scene.layout.HBox;   
 import javafx.scene.layout.VBox;
 import javafx.stage.Stage;
 import javafx.geometry.Insets;
 import javafx.geometry.Orientation;

 public class AddPage extends Application {


 public static void main(String[] args) {

Application.launch(args);
 }@Override

public void start(Stage primaryStage) throws Exception {

FlowPane pane = new FlowPane(Orientation.VERTICAL);
 pane.setHgap(5);
 pane.setAlignment(Pos.CENTER);
 pane.setPadding(new Insets(0,0,0,0)); // set top, right, bottom, left

 TextField tf1 = new TextField("computer-1001");
 TextField tf2 = new TextField("Location");
 TextField tf3 = new TextField("dd/mm/yyyy");



 tf1.setPrefColumnCount(14);
 tf2.setPrefColumnCount(14);
 tf3.setPrefColumnCount(14);


 Label l1 = new Label("Item Name: ");
 Label l2 = new Label("Previous Room: ");
 Label l3 = new Label("Date Item Was Moved: ");


 pane.getChildren().addAll(l1, tf1, l2, tf2, l3, tf3);

 HBox vBox = new HBox();
 Button button1 = new Button("Add");
 Button button2 = new Button("Cancel");

 vBox.setAlignment(Pos.CENTER);
 vBox.getChildren().addAll(button1, button2);
 vBox.setSpacing(20);

 BorderPane borderPane = new BorderPane();
 borderPane.setCenter(pane);
 borderPane.setBottom(vBox);
 BorderPane.setAlignment(vBox,Pos.CENTER);

 Scene scene = new Scene(borderPane, 400, 400);
 primaryStage.setTitle("Add Item");
 primaryStage.setScene(scene);
 primaryStage.show();

 }}