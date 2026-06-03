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
    public static final String[] NOTATIONS = {
        "U", "R", "F", "D", "L", "B", 
        "U'", "R'", "F'", "D'", "L'", "B'",
        "U2", "R2", "F2", "D2", "L2", "B2"
    };
    private Cube cube3D;
    private ArrayList<String> moveHistory = new ArrayList<>();

    public RubiksCube() { // Create an unscrambled Rubiks cube
        this(SOLVED_STATE.clone());
    }

    public RubiksCube(int[] state) { // Create a Rubiks cube with a predetermined scramble
        if (state.length != 54) {
            throw new IllegalArgumentException("State must have 54 elements.");
        }
        this.state = state.clone();
        this.cube3D = new Cube();
        this.cube3D.buildCube(state);
    }

    public Cube getCube3D() {
        return this.cube3D;
    }

    public int[] getState() {
        return state.clone();
    }

    public void printState() {
        System.out.println("Current Cube State:");
        System.out.println(java.util.Arrays.toString(state));
    }

    public String getMoveHistory() {
        return String.join(" ", moveHistory);
    }

    public void rotate(String notation) {
        int moveIndex = java.util.Arrays.asList(NOTATIONS).indexOf(notation);
        if (moveIndex == -1) throw new IllegalArgumentException("Invalid notation: " + notation);

        int face = moveIndex % 6;
        if (moveIndex >= 12) {
            // Double move — apply twice
            rotate(face, 1);
            rotate(face, 1);
        } else {
            int direction = (moveIndex < 6) ? 1 : -1;
            rotate(face, direction);
        }
    }

    public void rotate(int face, int direction) { // face: 0=U,1=R,2=F,3=D,4=L,5=B; direction: 1=clockwise, -1=counter-clockwise
        rotateFace(face, direction);
        rotateSides(face, direction);

        int moveIndex = face + (direction == 1 ? 0 : 6);
        moveHistory.add(NOTATIONS[moveIndex]);

        if (this.cube3D != null) {
            this.cube3D.buildCube(state);
        }
    }

    public void rotateFace(int face, int direction) {
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

    public void rotateSides(int face, int direction) {
        int[] tempState = state.clone();

        // THIS TOOK FORVER FUCK YOU BRUHHHHHH
        int[][] cycles = {
            {  9, 10, 11,  18, 19, 20,  36, 37, 38,  45, 46, 47 }, // U: R-top, F-top, L-top, B-top DONE
            {  45, 48, 51,  35, 32, 29,  26, 23, 20,  8, 5, 2 }, // R: U-right, F-right, D-right, B-left(inv) DONE
            {  6,  7,  8,  9,  12, 15,  29, 28, 27,  44, 41, 38 }, // F: U-bot, R-left, D-top, L-right(inv) DONE
            { 42, 43, 44,  24, 25, 26,  15, 16, 17,  51, 52, 53 }, // D: F-bot, L-bot, B-bot, R-bot(inv) -- verify DONE
            {  0,  3,  6,  18, 21, 24,  27, 30, 33,  53, 50, 47 }, // L: U-left, F-left, D-left, B-right(inv) DONE
            {  2,  1,  0,  36,  39, 42,  33, 34, 35,  17, 14, 11 }, // B: U-top(inv), R-top, D-bot, L-bot(inv)
        };

        int[] cycle = cycles[face];

        if (direction == 1) { // Clockwise: group i gets group i+3 (mod 12)
            for (int i = 0; i < 12; i++) {
                state[cycle[i]] = tempState[cycle[(i + 9) % 12]];
            }
        } else { // Counter-clockwise: group i gets group i+3 (mod 12)
            for (int i = 0; i < 12; i++) {
                state[cycle[i]] = tempState[cycle[(i + 3) % 12]];
            }
        }
    }

    public void printCube() {
        System.out.println("Current Cube State:");
        System.out.println(java.util.Arrays.toString(state));

        java.util.function.BiFunction<String,Integer,String> center = (s,w) -> {
            if (s == null) return "";
            if (s.length() >= w) return s;
            int left = (w - s.length()) / 2;
            int right = w - s.length() - left;
            return " ".repeat(left) + s + " ".repeat(right);
        };

        int tileWidth = 6;
        int faceWidth = tileWidth * 3;
        String indent = " ".repeat(18);

        System.out.print(indent);
        System.out.println(center.apply("Up", faceWidth));

        for (int row = 0; row < 3; row++) {
            System.out.print("                  ");
            for (int col = 0; col < 3; col++) {
                int idx = 0 * 9 + row * 3 + col;
                System.out.print(String.format("%d(%02d) ", state[idx], idx));
            }
            System.out.println();
        }

        String[] midLabels = new String[]{"Left", "Front", "Right", "Back"};
        StringBuilder midLabelLine = new StringBuilder();
        for (String l : midLabels) midLabelLine.append(center.apply(l, faceWidth));
        System.out.println(midLabelLine.toString());

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                int idx = 4 * 9 + row * 3 + col;
                System.out.print(String.format("%d(%02d) ", state[idx], idx));
            }
            for (int col = 0; col < 3; col++) {
                int idx = 2 * 9 + row * 3 + col;
                System.out.print(String.format("%d(%02d) ", state[idx], idx));
            }
            for (int col = 0; col < 3; col++) {
                int idx = 1 * 9 + row * 3 + col;
                System.out.print(String.format("%d(%02d) ", state[idx], idx));
            }
            for (int col = 0; col < 3; col++) {
                int idx = 5 * 9 + row * 3 + col;
                System.out.print(String.format("%d(%02d) ", state[idx], idx));
            }
            System.out.println();
        }

        System.out.print(indent);
        System.out.println(center.apply("Down", faceWidth));

        for (int row = 0; row < 3; row++) {
            System.out.print("                  ");
            for (int col = 0; col < 3; col++) {
                int idx = 3 * 9 + row * 3 + col;
                System.out.print(String.format("%d(%02d) ", state[idx], idx));
            }
            System.out.println();
        }
    }

    public CubeCubie toCubie() {
        CubeFace face = new CubeFace();
        for (int i = 0; i < 54; i++) {
            face.face[i] = (byte) this.state[i];
        }
        return face.toCubie();
    }

    public static int[] fromCubie(CubeCubie cubie) {
        CubeFace face = cubie.toFacelet();
        int[] state = new int[54];
        for (int i = 0; i < 54; i++) {
            state[i] = face.face[i];
        }
        return state;
    }

    public CubeFace toFacelet() {
        CubeFace face = new CubeFace();
        for (int i = 0; i < 54; i++) {
            face.face[i] = (byte) this.state[i];
        }
        return face;
    }
}