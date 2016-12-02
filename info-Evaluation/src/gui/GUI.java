package gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


/**
 * This class implements our GUI
 * 
 * @author Ward
 * @since 2/12/2016
 */
public class GUI extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		
		Button btn = new Button();
		btn.setText("Search");
		btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent __) {
				System.out.println("<Update Search results>");
			}
		});
		
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		StackPane root = new StackPane(grid,btn);

		primaryStage.setTitle("Info-Envaluation - Query Advisor");
		primaryStage.setScene(new Scene(root, 600, 500));
		primaryStage.show();
	}
}