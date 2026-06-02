package rubikscubesolvernew;

import java.util.ArrayList;

public class RubiksCube {
    public int[] state = new int[54];
    public static final int[] SOLVED_STATE = {
        1, 1, 1, 1, 1, 1, 1, 1, 1, // Up face (White)
        2, 2, 2, 2, 2, 2, 2, 2, 2, // Right face (Red)
        3, 3, 3, 3, 3, 3, 3, 3, 3, // Front face (Green)
        4, 4, 4, 4, 4, 4, 4, 4, 4, // Down face (Yellow)
        5, 5, 5, 5, 5, 5, 5, 5, 5, // Left face (Orange)
        6, 6, 6, 6, 6, 6, 6, 6, 6   // Back face (Blue)
    };
    private static final int[][] ADJACENT_MAP = {
        // Face 0 (U): Affects top rows of Left(4), Back(5), Right(1), Front(2)
        {36, 37, 38,  45, 46, 47,  9, 10, 11,  18, 19, 20},
        // Face 1 (R): Affects Right edges of Front, Up, Back, Down
        {20, 23, 26,  2, 5, 8,     47, 50, 53, 29, 32, 35},
        // Face 2 (F): Affects bottom of Up, left of Right, top of Down, right of Left
        {6, 7, 8,     9, 12, 15,   29, 28, 27, 44, 41, 38},
        // Face 3 (D): Affects bottom rows of Front, Right, Back, Left
        {24, 25, 26,  15, 16, 17,  51, 52, 53, 42, 43, 44},
        // Face 4 (L): Affects left edges of Back, Down, Front, Up
        {53, 50, 47,  33, 30, 27,  18, 21, 24, 0, 3, 6},
        // Face 5 (B): Affects top of Up, left of Left, bottom of Down, right of Right
        {2, 1, 0,     36, 39, 42,  35, 34, 33, 17, 14, 11}
    };
    public static final String[] NOTATIONS = {"U", "R", "F", "D", "L", "B", "U'", "R'", "F'", "D'", "L'", "B'"};
    public ArrayList<String> moveHistory = new ArrayList<>();

    public RubiksCube() { // Create an unscrambled Rubiks cube
        this.state = SOLVED_STATE.clone();
    }

    public RubiksCube(int[] state) { // Create a Rubiks cube with a predetermined scramble
        if (state.length != 54) {
            throw new IllegalArgumentException("State must have 54 elements.");
        }

        // Additional validation for state values can be added here (e.g., check for valid colors and counts)

        this.state = state.clone();
    }

    public void rotateFace(int face, int direction) { // Rotates face. 0 for up, 1 for right, 2 for front, 3 for down, 4 for left, 5 for back; direction: 1 for clockwise, -1 for counter-clockwise
        rotateFaceOnly(face, direction);
        rotateAdjacentSides(face, direction);

        int moveIndex = face + (direction == 1 ? 0 : 6);
        moveHistory.add(NOTATIONS[moveIndex]);

        printCube();
    }

    public void rotateFace(String notation) {
        int moveIndex = java.util.Arrays.asList(NOTATIONS).indexOf(notation);

        if (moveIndex == -1) {
            throw new IllegalArgumentException("Invalid notation: " + notation);
        }

        int face = moveIndex % 6;
        int direction = (moveIndex < 6) ? 1 : -1;

        moveHistory.add(notation);

        rotateFace(face, direction);
    }

    private void rotateFaceOnly(int face, int direction) {
        int start = face * 9;
        int[] tempState = state.clone();

        if (direction == 1) { // Clockwise
            state[start + 0] = tempState[start + 6];
            state[start + 1] = tempState[start + 3];
            state[start + 2] = tempState[start + 0];
            state[start + 3] = tempState[start + 7];
            // 4 is skipped as it is the center piece, which doesn't move
            state[start + 5] = tempState[start + 1];
            state[start + 6] = tempState[start + 8];
            state[start + 7] = tempState[start + 5];
            state[start + 8] = tempState[start + 2];
        } else if (direction == -1) { // Counter-clockwise
            state[start + 0] = tempState[start + 2];
            state[start + 1] = tempState[start + 5];
            state[start + 2] = tempState[start + 8];
            state[start + 3] = tempState[start + 1];
            // 4 is skipped as it is the center piece, which doesn't move
            state[start + 5] = tempState[start + 7];
            state[start + 6] = tempState[start + 0];
            state[start + 7] = tempState[start + 3];
            state[start + 8] = tempState[start + 6];
        } else {
            throw new IllegalArgumentException("Direction must be 1 (clockwise) or -1 (counter-clockwise).");
        }
    }

    private void rotateAdjacentSides(int face, int direction) {
        int[] affectedIndices = ADJACENT_MAP[face];
        int[] tempState = state.clone();

        // Shift 12 items in blocks of 3
        for (int i = 0; i < 12; i++) {
            // If clockwise (1): target takes from source 3 steps behind (-3)
            // If counter-clockwise (-1): target takes from source 3 steps ahead (+3)
            int sourceIndexInMap = (i - (direction * 3) + 12) % 12;
            int targetActualIndex = affectedIndices[i];
            int sourceActualIndex = affectedIndices[sourceIndexInMap];

            state[targetActualIndex] = tempState[sourceActualIndex];
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
