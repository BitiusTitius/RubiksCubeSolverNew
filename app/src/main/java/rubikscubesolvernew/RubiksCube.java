package rubikscubesolvernew;

import java.util.ArrayList;

public class RubiksCube {
    private int[] state = new int[54];
    private static final int[] SOLVED_STATE = {
        1, 1, 1, 1, 1, 1, 1, 1, 1, // Up face (White)
        2, 2, 2, 2, 2, 2, 2, 2, 2, // Right face (Red)
        3, 3, 3, 3, 3, 3, 3, 3, 3, // Front face (Green)
        4, 4, 4, 4, 4, 4, 4, 4, 4, // Down face (Yellow)
        5, 5, 5, 5, 5, 5, 5, 5, 5, // Left face (Orange)
        6, 6, 6, 6, 6, 6, 6, 6, 6   // Back face (Blue)
    };
    public static final String[] NOTATIONS = {"U", "R", "F", "D", "L", "B", "U'", "R'", "F'", "D'", "L'", "B'"};
    private Cube cube3D;
    private ArrayList<String> moveHistory = new ArrayList<>();

    public RubiksCube() { // Create an unscrambled Rubiks cube
        this.state = SOLVED_STATE.clone();
        this.cube3D = new Cube();
        this.cube3D.buildCube(state);
    }

    public RubiksCube(int[] scramble) { // Create a Rubiks cube with a predetermined scramble
        if (scramble.length != 54) {
            throw new IllegalArgumentException("State must have 54 elements.");
        }
        this.state = scramble.clone();
        this.cube3D = new Cube();
        this.cube3D.buildCube(scramble);
    }

    public Cube getCube3D() {
        return this.cube3D;
    }

    public int[] getState() {
        return state.clone();
    }

    public void rotateFace(int face, int direction) { 
        rotateFaceOnly(face, direction);
        rotateAdjacentSides(face, direction);

        int moveIndex = face + (direction == 1 ? 0 : 6);
        moveHistory.add(NOTATIONS[moveIndex]);

        if (this.cube3D != null) {
            this.cube3D.buildCube(state);
        }

        printCube();
    }

    public void rotateFace(String notation) {
        int moveIndex = java.util.Arrays.asList(NOTATIONS).indexOf(notation);

        if (moveIndex == -1) {
            throw new IllegalArgumentException("Invalid notation: " + notation);
        }

        int face = moveIndex % 6;
        int direction = (moveIndex < 6) ? 1 : -1;

        rotateFace(face, direction);
    }

    private static final int[][] ADJACENT_FACE_CYCLES = {
        {9, 20, 36, 47, 10, 19, 37, 46, 11, 18, 38, 45}, // U
        {2, 45, 29, 26, 5, 48, 32, 23, 8, 51, 35, 20},     // R
        {0, 11, 35, 42, 1, 14, 34, 39, 2, 17, 33, 36},     // F
        {15, 53, 42, 26, 16, 52, 43, 25, 17, 51, 44, 24},  // D
        {0, 24, 27, 47, 3, 21, 30, 50, 6, 18, 33, 53},     // L
        {6, 44, 29, 9, 7, 41, 28, 12, 8, 38, 27, 15}       // B
    };

    private void rotateFaceOnly(int face, int direction) {
        int start = face * 9;
        int[] tempState = state.clone();

        if (direction == 1) { // Clockwise
            state[start + 0] = tempState[start + 6];
            state[start + 1] = tempState[start + 3];
            state[start + 2] = tempState[start + 0];
            state[start + 3] = tempState[start + 7];
            state[start + 5] = tempState[start + 1];
            state[start + 6] = tempState[start + 8];
            state[start + 7] = tempState[start + 5];
            state[start + 8] = tempState[start + 2];
        } else if (direction == -1) { // Counter-clockwise
            state[start + 0] = tempState[start + 2];
            state[start + 1] = tempState[start + 5];
            state[start + 2] = tempState[start + 8];
            state[start + 3] = tempState[start + 1];
            state[start + 5] = tempState[start + 7];
            state[start + 6] = tempState[start + 0];
            state[start + 7] = tempState[start + 3];
            state[start + 8] = tempState[start + 6];
        } else {
            throw new IllegalArgumentException("Direction must be 1 (clockwise) or -1 (counter-clockwise).");
        }
    }

    private void rotateAdjacentSides(int face, int direction) {
        int[] tempState = state.clone();
        int[] cycle = ADJACENT_FACE_CYCLES[face];

        if (direction == 1) {
            for (int i = 0; i < cycle.length; i += 4) {
                state[cycle[i + 1]] = tempState[cycle[i]];
                state[cycle[i + 2]] = tempState[cycle[i + 1]];
                state[cycle[i + 3]] = tempState[cycle[i + 2]];
                state[cycle[i]] = tempState[cycle[i + 3]];
            }
        } else if (direction == -1) {
            for (int i = 0; i < cycle.length; i += 4) {
                state[cycle[i]] = tempState[cycle[i + 1]];
                state[cycle[i + 1]] = tempState[cycle[i + 2]];
                state[cycle[i + 2]] = tempState[cycle[i + 3]];
                state[cycle[i + 3]] = tempState[cycle[i]];
            }
        } else {
            throw new IllegalArgumentException("Direction must be 1 (clockwise) or -1 (counter-clockwise).");
        }
    }

    public void printCube() {
        System.out.println("Current Cube State:");
        System.out.println(java.util.Arrays.toString(state));

        for (int row = 0; row < 3; row++) {
            System.out.print("      ");
            for (int col = 0; col < 3; col++) {
                System.out.print(state[0 * 9 + row * 3 + col] + " ");
            }
            System.out.println();
        }

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) System.out.print(state[4 * 9 + row * 3 + col] + " ");
            for (int col = 0; col < 3; col++) System.out.print(state[2 * 9 + row * 3 + col] + " ");
            for (int col = 0; col < 3; col++) System.out.print(state[1 * 9 + row * 3 + col] + " ");
            for (int col = 0; col < 3; col++) System.out.print(state[5 * 9 + row * 3 + col] + " ");
            System.out.println();
        }

        for (int row = 0; row < 3; row++) {
            System.out.print("      ");
            for (int col = 0; col < 3; col++) {
                System.out.print(state[3 * 9 + row * 3 + col] + " ");
            }
            System.out.println();
        }
    }
}