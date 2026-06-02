package rubikscubesolvernew;

import javafx.scene.Group;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.scene.PerspectiveCamera;

public class Cube extends StackPane {
    private static final Color[] FACE_COLORS = {
        Color.BLACK,  // value 0: internal (not visible)
        Color.WHITE,  // value 1: Up
        Color.RED,    // value 2: Right
        Color.GREEN,  // value 3: Front
        Color.YELLOW, // value 4: Down
        Color.ORANGE, // value 5: Left
        Color.BLUE    // value 6: Back
    };
    private Group cubeGroup;

    public Cube() {
        cubeGroup = new Group();
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setTranslateZ(-15);

        SubScene subScene = new SubScene(cubeGroup, 800, 600, true, SceneAntialiasing.BALANCED);
        subScene.setCamera(camera);
        subScene.setFill(Color.GRAY);

        initMouseControl(cubeGroup, subScene);

        this.getChildren().add(subScene);
    }

    public void buildCube(int[] state) {
        cubeGroup.getChildren().clear();

        double tileSize = 0.95;
        double offset = 1.0;

        for (int i = 0; i < 54; i++) {
            int face = i / 9;
            int positionOnFace = i % 9;
            
            // Calculate grid x, y on a 2D face (-1, 0, 1)
            int row = positionOnFace / 3 - 1;
            int col = positionOnFace % 3 - 1;

            // Create a thin box to act as a colored tile
            Box tile = new Box(tileSize, tileSize, 0.05); 
            PhongMaterial material = new PhongMaterial(FACE_COLORS[state[i]]);
            tile.setMaterial(material);

            // Position and rotate the tile based on which face it belongs to
            Translate translate = new Translate();
            Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
            Rotate rotateY = new Rotate(0, Rotate.Y_AXIS);

            // Map face index to 3D space
            switch (face) {
                case 0: // Up (White) - facing -Y
                    translate.setX(col * offset);
                    translate.setY(-1.5 * offset);
                    translate.setZ(row * offset);
                    rotateX.setAngle(90);
                    break;
                case 1: // Right (Red) - facing +X
                    translate.setX(1.5 * offset);
                    translate.setY(row * offset);
                    translate.setZ(-col * offset); // Reverse Z to match standard mapping
                    rotateY.setAngle(90);
                    break;
                case 2: // Front (Green) - facing -Z
                    translate.setX(col * offset);
                    translate.setY(row * offset);
                    translate.setZ(-1.5 * offset);
                    break;
                case 3: // Down (Yellow) - facing +Y
                    translate.setX(col * offset);
                    translate.setY(1.5 * offset);
                    translate.setZ(-row * offset);
                    rotateX.setAngle(90);
                    break;
                case 4: // Left (Orange) - facing -X
                    translate.setX(-1.5 * offset);
                    translate.setY(row * offset);
                    translate.setZ(col * offset);
                    rotateY.setAngle(90);
                    break;
                case 5: // Back (Blue) - facing +Z
                    translate.setX(-col * offset);
                    translate.setY(row * offset);
                    translate.setZ(1.5 * offset);
                    break;
            }

            tile.getTransforms().addAll(translate, rotateX, rotateY);
            cubeGroup.getChildren().add(tile);
        }
    }

    private void initMouseControl(Group group, SubScene scene) {
        Rotate xRotate = new Rotate(0, Rotate.X_AXIS);
        Rotate yRotate = new Rotate(0, Rotate.Y_AXIS);
        group.getTransforms().addAll(xRotate, yRotate);

        final double[] anchorAngle = new double[2];
        final double[] anchorPos = new double[2];

        scene.setOnMousePressed(event -> {
            anchorPos[0] = event.getSceneX();
            anchorPos[1] = event.getSceneY();
            anchorAngle[0] = xRotate.getAngle();
            anchorAngle[1] = yRotate.getAngle();
        });

        scene.setOnMouseDragged(event -> {
            xRotate.setAngle(anchorAngle[0] - (anchorPos[1] - event.getSceneY()));
            yRotate.setAngle(anchorAngle[1] + (anchorPos[0] - event.getSceneX()));
        });
    }
}