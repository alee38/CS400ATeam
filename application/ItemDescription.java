package application;

import java.awt.TextArea;
import java.util.List;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;


public class ItemDescription extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {


            GridPane grid = new GridPane();
            Scene scene = new Scene(grid, 300, 200);

            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            // Label label = new Label("Complete Inventory");
            primaryStage.setTitle("Complete Inventory");

            Button returnButton = new Button("Return");

            // hard-coded now, should be changed later
            Label itemName = new Label("Computer  ");
            Label location = new Label("Location  ");
            Label t = new Label("Placed There  ");

            HBox name = new HBox(itemName, new Text("101"));
            HBox loc = new HBox(location, new Text("9313"));
            HBox time = new HBox(t, new Text("16 Feb 2019"));


            grid.add(name, 0, 0);
            grid.add(loc, 0, 1);
            grid.add(time, 0, 2);
            grid.setAlignment(Pos.CENTER);
            grid.add(returnButton, 0, 3);
            grid.setHgap(10);
            grid.setVgap(10);

            // grid.add(hbButtons, 0, 2, 2, 1);

            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}