package rubikscubesolvernew;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        int[] scramble = {5, 5, 3, 1, 1, 6, 1, 1, 5, 3, 2, 2, 1, 2, 2, 1, 2, 2, 3, 3, 1, 3, 3, 1, 4, 4, 1, 4, 4, 2, 4, 4, 3, 5, 5, 6, 4, 2, 2, 4, 5, 5, 6, 5, 5, 4, 3, 3, 6, 6, 6, 6, 6, 6};
        // 1. Initialize your Rubik's Cube core logic
        RubiksCube cube = new RubiksCube(scramble);

        // 2. Create the main UI layout window
        BorderPane root = new BorderPane();

        // 3. Extract the 3D visual component from the cube and put it in the center
        root.setCenter(cube.getCube3D());

        // 4. Create a button control panel for testing standard notations (U, R, F, etc.)
        FlowPane buttonPanel = new FlowPane();
        buttonPanel.setAlignment(Pos.CENTER);
        buttonPanel.setHgap(10);
        buttonPanel.setVgap(10);
        buttonPanel.setStyle("-fx-padding: 20; -fx-background-color: #333333;");

        // Loop through your NOTATIONS array to dynamically generate buttons
        for (String notation : RubiksCube.NOTATIONS) {
            Button btn = new Button(notation);
            btn.setStyle("-fx-font-size: 14px; -fx-min-width: 50px;");
            
            // When clicked, rotate the logic cube (which automatically refreshes the 3D view)
            btn.setOnAction(e -> cube.rotateFace(notation));
            buttonPanel.getChildren().add(btn);
        }

        root.setBottom(buttonPanel);

        // 5. Setup and display the window stage
        Scene scene = new Scene(root, 900, 700);
        primaryStage.setTitle("3D Rubik's Cube Simulator");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) { launch(args); }
}