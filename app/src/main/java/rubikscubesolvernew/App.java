package rubikscubesolvernew;

import java.util.Arrays;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        int[] solvedState = {1, 1, 1, 1, 1, 1, 3, 3, 3, 1, 6, 6, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 4, 3, 3, 4, 4, 4, 6, 4, 4, 6, 4, 4, 6, 3, 3, 4, 5, 5, 5, 5, 5, 5, 5, 5, 5, 1, 6, 6, 1, 6, 6};
        int[] scramble = {4, 5, 6, 2, 1, 2, 1, 6, 3, 4, 6, 5, 3, 2, 6, 5, 5, 6, 6, 1, 5, 6, 3, 1, 4, 4, 3, 6, 3, 1, 5, 4, 1, 1, 1, 2, 3, 3, 2, 2, 5, 5, 3, 3, 5, 1, 4, 2, 4, 6, 4, 4, 2, 2};
        // 1. Initialize your Rubik's Cube core logic
        PruningTables.init();
        RubiksCube cube = new RubiksCube();

        /**RubiksCube fc = new RubiksCube();
        fc.rotate("R"); fc.rotate("U");
        CubeCubie fcc = fc.toCubie();
        int flip = CubeCoord.getFlip(fcc);
        int twist = CubeCoord.getTwist(fcc);
        int udslice = CubeCoord.getUDSlice(fcc);
        System.out.println("Initial: twist=" + twist + " flip=" + flip + " udslice=" + udslice);

        // Apply U' via move table
        int m = 6; // U' index
        System.out.println("After U' via table: twist=" + MoveTables.twistMove[twist][m] 
            + " flip=" + MoveTables.flipMove[flip][m]
            + " udslice=" + MoveTables.udSliceMove[udslice][m]);

        // Apply U' via actual rotation
        fc.rotate("U'");
        fcc = fc.toCubie();
        System.out.println("After U' via rotate: twist=" + CubeCoord.getTwist(fcc) 
            + " flip=" + CubeCoord.getFlip(fcc)
            + " udslice=" + CubeCoord.getUDSlice(fcc));

        System.out.println("=== Phase 2 move flip effects ===");
        String[] names = {"U","R","F","D","L","B","U'","R'","F'","D'","L'","B'","U2","R2","F2","D2","L2","B2"};
        int[] phase2 = {0, 3, 6, 9, 12, 13, 14, 15, 16, 17};
        for (int mo : phase2) {
            // Apply to solved state (flip=0)
            int newFlip = MoveTables.flipMove[0][mo];
            System.out.println(names[mo] + ": flip 0 -> " + newFlip);
        }**/

        cube.rotate("R");
        cube.rotate("U");

        int[] original = cube.getState();
        int[] recovered = RubiksCube.fromCubie(cube.toCubie());

        System.out.println(Arrays.equals(original, recovered));

        Test.testSolver();
        //Test.diagnoseMoveOrientations();
        //Test.diagnoseFlipConsistency();
        Test.diagnoseStateSides();

        BorderPane root = new BorderPane();
        root.setCenter(cube.getCube3D());

        FlowPane buttonPanel = new FlowPane();
        buttonPanel.setAlignment(Pos.CENTER);
        buttonPanel.setHgap(10);
        buttonPanel.setVgap(10);
        buttonPanel.setStyle("-fx-padding: 20; -fx-background-color: #333333;");

        String[] notations = {
            "U", "R", "F", "D", "L", "B", 
        };

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

        root.setBottom(buttonPanel);

        Scene scene = new Scene(root, 900, 700);
        primaryStage.setTitle("3D Rubik's Cube Simulator");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) { launch(args); }
}