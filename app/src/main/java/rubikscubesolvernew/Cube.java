package rubikscubesolvernew;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.transform.Translate;

public class Cube extends Group {

    private static final double SPACING = 1.05; // slight gap between cubies

    // State values 1-6 map to: Up, Right, Front, Down, Left, Back
    // FACE_COLORS indexed by (value - 1)
    private static final Color[] FACE_COLORS = {
        Color.WHITE,  // value 1: Up
        Color.RED,    // value 2: Right
        Color.GREEN,  // value 3: Front
        Color.YELLOW, // value 4: Down
        Color.ORANGE, // value 5: Left
        Color.BLUE    // value 6: Back
    };

    private Cubie[][][] cubies = new Cubie[3][3][3];

    public Cube() {
        buildCube(RubiksCube.SOLVED_STATE);
    }

    public Cube(int[] state) {
        buildCube(state);
    }

    public void buildCube(int[] state) {
        // x: Left→Right (0=Left, 2=Right)
        // y: Top→Bottom (0=Up,   2=Down)
        // z: Front→Back (0=Front,2=Back)
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                for (int z = 0; z < 3; z++) {
                    Color[] faceColors = resolveFaceColors(x, y, z, state);
                    Cubie cubie = new Cubie(faceColors);

                    // Center the whole cube at origin
                    double tx = (x - 1) * SPACING;
                    double ty = (y - 1) * SPACING;
                    double tz = (z - 1) * SPACING;
                    cubie.getTransforms().add(new Translate(tx, ty, tz));

                    cubies[x][y][z] = cubie;
                    getChildren().add(cubie);
                }
            }
        }
    }

    /**
     * Maps your 1D state array → per-cubie face colors.
     * Adjust index math to match your own array layout.
     */
    private Color[] resolveFaceColors(int x, int y, int z, int[] state) {
        Color[] c = new Color[6]; // null = hidden internal face

        if (y == 0) c[0] = colorForStateValue(state[getStateIndex(0, x, z)]); // Up    (face 0)
        if (y == 2) c[1] = colorForStateValue(state[getStateIndex(3, x, z)]); // Down  (face 3)
        if (z == 0) c[2] = colorForStateValue(state[getStateIndex(2, y, x)]); // Front (face 2)
        if (z == 2) c[3] = colorForStateValue(state[getStateIndex(5, y, x)]); // Back  (face 5)
        if (x == 0) c[4] = colorForStateValue(state[getStateIndex(4, y, z)]); // Left  (face 4)
        if (x == 2) c[5] = colorForStateValue(state[getStateIndex(1, y, z)]); // Right (face 1)

        return c;
    }

    private Color colorForStateValue(int value) {
        if (value == 0) {
            return null; // Internal face, not visible
        }
        if (value < 1 || value > FACE_COLORS.length) {
            throw new IllegalArgumentException("Invalid cube state value: " + value);
        }
        return FACE_COLORS[value - 1];
    }

    /** Converts face + row + col → index in your 1D state array. Adapt to your layout. */
    private int getStateIndex(int face, int row, int col) {
        return face * 9 + row * 3 + col;
    }

    /** Call this after a move to refresh all cubie colors from new state */
    public void updateFromState(int[] state) {
        getChildren().clear();
        buildCube(state);
    }
}