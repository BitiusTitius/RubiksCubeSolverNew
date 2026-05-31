package rubikscubesolvernew;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Translate;

import java.util.ArrayList;
import java.util.List;

public class CubeVisuals extends Group {
    private final Box cubie;
    private final List<Box> stickers = new ArrayList<>();

    public CubeVisuals(int gridX, int gridY, int gridZ, double size) {
        cubie = new Box(size, size, size); // Create a cube with the specified size
        PhongMaterial material = new PhongMaterial(Color.web("#1A1A1A"));
        cubie.setMaterial(material);
        this.getChildren().add(cubie);

        double thickness = size * 0.1; // Thickness of the stickers
        double offset = size / 2 + thickness / 2; // Offset for sticker placement

        if (gridY == 2) {
            Box upSticker = new Box(size, thickness, size); // Up face
            upSticker.setTranslateY(-offset); // Move up
            upSticker.setMaterial(new PhongMaterial(Color.YELLOW));
            stickers.add(upSticker);
        } else if (gridY == 0) {
            Box downSticker = new Box(size, thickness, size); // Down face
            downSticker.setTranslateY(offset); // Move down
            downSticker.setMaterial(new PhongMaterial(Color.WHITE));
            stickers.add(downSticker);
        }

        if (gridZ == 2) {
            Box frontSticker = new Box(size, size, thickness); // Front face
            frontSticker.setTranslateZ(-offset); // Move forward
            frontSticker.setMaterial(new PhongMaterial(Color.GREEN));
            stickers.add(frontSticker);
        } else if (gridZ == 0) {
            Box backSticker = new Box(size, size, thickness); // Back face
            backSticker.setTranslateZ(offset); // Move backward
            backSticker.setMaterial(new PhongMaterial(Color.BLUE));
            stickers.add(backSticker);
        }

        if (gridX == 2) {
            Box rightSticker = new Box(thickness, size, size); // Right face
            rightSticker.setTranslateX(offset); // Move right
            rightSticker.setMaterial(new PhongMaterial(Color.RED));
            stickers.add(rightSticker);
        } else if (gridX == 0) {
            Box leftSticker = new Box(thickness, size, size); // Left face
            leftSticker.setTranslateX(-offset); // Move left
            leftSticker.setMaterial(new PhongMaterial(Color.ORANGE));
            stickers.add(leftSticker);
        }

        this.getChildren().addAll(stickers);
    }
}