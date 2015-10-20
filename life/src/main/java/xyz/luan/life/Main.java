package xyz.luan.life;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {
		stage.setTitle("Hello World!");
		stage.setScene(new Scene(rootPane(), 300, 250));
		stage.show();
	}

	private StackPane rootPane() {
		StackPane root = new StackPane();
		root.getChildren().add(simpleButton());
		return root;
	}

	private Button simpleButton() {
		Button btn = new Button();
		btn.setText("Say 'Hello World'");
		btn.setOnAction(e -> System.out.println("Hello World!"));
		return btn;
	}
}
