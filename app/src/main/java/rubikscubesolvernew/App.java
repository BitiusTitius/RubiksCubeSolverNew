package rubikscubesolvernew;

import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        RubiksCube cube = new RubiksCube();

        BorderPane root = new BorderPane();
        root.setCenter(cube.getCube3D());

        // Status label shown below the buttons
        Label statusLabel = new Label("Left-click = clockwise  |  Right-click = counter-clockwise");
        statusLabel.setStyle("-fx-text-fill: #cccccc; -fx-font-size: 12px;");

        FlowPane buttonPanel = new FlowPane();
        buttonPanel.setAlignment(Pos.CENTER);
        buttonPanel.setHgap(10);
        buttonPanel.setVgap(10);
        buttonPanel.setStyle("-fx-padding: 20; -fx-background-color: #333333;");

        String[] notations = { "U", "R", "F", "D", "L", "B" };

        for (String n : notations) {
            Button button = new Button(n);
            button.setStyle("-fx-font-size: 14px; -fx-min-width: 50px;");
            button.setOnMouseClicked(e -> {
                if (e.getButton() == MouseButton.PRIMARY) {
                    cube.rotate(n);
                } else if (e.getButton() == MouseButton.SECONDARY) {
                    cube.rotate(n + "'");
                }
            });
            buttonPanel.getChildren().add(button);
        }

        // Solve button — converts current state and runs SolveCube on a background thread
        Button solveButton = new Button("Solve");
        solveButton.setStyle("-fx-font-size: 14px; -fx-min-width: 80px; -fx-background-color: #4CAF50; -fx-text-fill: white;");
        solveButton.setOnAction(e -> {
            solveButton.setDisable(true);
            statusLabel.setText("Calculating solution...");

            Thread solverThread = new Thread(() -> {
                try {
                    SolveCube solver = new SolveCube();

                    // Convert the 3D cube's current state into the solver's byte[6][9] format
                    byte[][] solverState = cube.toSolverState();

                    // Run the solver — mapOrientation handles orientation normalisation internally
                    String solution = solver.mapOrientation(solverState, solver);

                    if (solution != null && !solution.isBlank()) {
                        // Convert solver notation (uses 'i') to standard notation (uses '\'')
                        String displaySolution = solution.trim().replace("i", "'");
                        System.out.println("Solution: " + displaySolution);

                        Platform.runLater(() -> {
                            statusLabel.setText("Solution: " + displaySolution);
                            // Apply each move to the live cube so the 3D view animates the solve
                            cube.applySolution(solution);
                            solveButton.setDisable(false);
                        });
                    } else {
                        Platform.runLater(() -> {
                            statusLabel.setText("Already solved!");
                            solveButton.setDisable(false);
                        });
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                    Platform.runLater(() -> {
                        statusLabel.setText("Error: could not load lookup tables (stage0-3.txt / testMoves.txt).");
                        solveButton.setDisable(false);
                    });
                }
            });
            solverThread.setDaemon(true);
            solverThread.start();
        });
        buttonPanel.getChildren().add(solveButton);

        FlowPane bottomPanel = new FlowPane();
        bottomPanel.setAlignment(Pos.CENTER);
        bottomPanel.setStyle("-fx-padding: 5 20 10 20; -fx-background-color: #333333;");
        bottomPanel.getChildren().add(statusLabel);

        BorderPane bottomWrapper = new BorderPane();
        bottomWrapper.setTop(buttonPanel);
        bottomWrapper.setBottom(bottomPanel);

        root.setBottom(bottomWrapper);

        Scene scene = new Scene(root, 900, 700);
        primaryStage.setTitle("3D Rubik's Cube Simulator");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) { launch(args); }
}