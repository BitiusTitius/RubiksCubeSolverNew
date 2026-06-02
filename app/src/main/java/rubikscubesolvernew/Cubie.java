package rubikscubesolvernew;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Translate;

import java.util.ArrayList;
import java.util.List;

public class Cubie extends Group {

    private static final double SIZE = 1.0;      // body size
    private static final double STICKER = 0.85;  // sticker face size
    private static final double OFFSET = 0.501;  // just outside the body face

    // face index → Color (null = internal face, not visible)
    // Face order: 0=Up, 1=Down, 2=Front, 3=Back, 4=Left, 5=Right
    public Cubie(Color[] faceColors) {
        // Creates the base cube
        Box body = new Box(SIZE, SIZE, SIZE);
        PhongMaterial black = new PhongMaterial(Color.BLACK);
        body.setMaterial(black);
        getChildren().add(body);

        double[][] offsets = {
            { 0,  -OFFSET,  0 }, // Up
            { 0,   OFFSET,  0 }, // Down
            { 0,  0,  -OFFSET }, // Front
            { 0,  0,   OFFSET }, // Back
            {-OFFSET,  0,  0  }, // Left
            { OFFSET,  0,  0  }, // Right
        };
        double[][] stickerSizes = {
            { STICKER, 0.05, STICKER }, // Up
            { STICKER, 0.05, STICKER }, // Down
            { STICKER, STICKER, 0.05 }, // Front
            { STICKER, STICKER, 0.05 }, // Back
            { 0.05, STICKER, STICKER }, // Left
            { 0.05, STICKER, STICKER }, // Right
        };

        for (int i = 0; i < 6; i++) {
            if (faceColors[i] == null) continue; // internal, skip

            Box sticker = new Box(stickerSizes[i][0], stickerSizes[i][1], stickerSizes[i][2]);
            PhongMaterial mat = new PhongMaterial(faceColors[i]);
            sticker.setMaterial(mat);
            sticker.getTransforms().add(
                new Translate(offsets[i][0], offsets[i][1], offsets[i][2])
            );
            getChildren().add(sticker);
        }
    }
}