package rubikscubesolvernew;

import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

public class App extends Application {

    private double mouseX, mouseY;
    private final Rotate rotateX = new Rotate(25, Rotate.X_AXIS);
    private final Rotate rotateY = new Rotate(-35, Rotate.Y_AXIS);

    @Override
    public void start(Stage stage) {
        RubiksCube cube = new RubiksCube(); // creates a solved cube by default
        Cube visual = new Cube(cube.getState()); // pass your 1D array

        Group root = new Group(visual);
        root.getTransforms().addAll(rotateX, rotateY);

        // Add lighting to illuminate all faces
        AmbientLight ambient = new AmbientLight(Color.color(0.6, 0.6, 0.6));
        PointLight light1 = new PointLight(Color.WHITE);
        light1.setTranslateX(5);
        light1.setTranslateY(-5);
        light1.setTranslateZ(-5);
        PointLight light2 = new PointLight(Color.color(0.8, 0.8, 0.8));
        light2.setTranslateX(-5);
        light2.setTranslateY(5);
        light2.setTranslateZ(5);
        root.getChildren().addAll(ambient, light1, light2);

        // Mouse drag to rotate the whole cube
        SubScene sub = new SubScene(root, 600, 600, true, SceneAntialiasing.BALANCED);
        sub.setFill(Color.DARKGRAY);

        PerspectiveCamera cam = new PerspectiveCamera(true);
        cam.setTranslateZ(-8);
        sub.setCamera(cam);

        sub.setOnMousePressed(e -> { mouseX = e.getSceneX(); mouseY = e.getSceneY(); });
        sub.setOnMouseDragged(e -> {
            rotateY.setAngle(rotateY.getAngle() + (e.getSceneX() - mouseX) * 0.3);
            rotateX.setAngle(rotateX.getAngle() - (e.getSceneY() - mouseY) * 0.3);
            mouseX = e.getSceneX(); mouseY = e.getSceneY();
        });

        Scene scene = new Scene(new Group(sub));
        stage.setScene(scene);
        stage.setTitle("Rubik's Cube Solver");
        stage.setWidth(700);
        stage.setHeight(700);
        stage.show();
    }

    public static void main(String[] args) { launch(args); }
}