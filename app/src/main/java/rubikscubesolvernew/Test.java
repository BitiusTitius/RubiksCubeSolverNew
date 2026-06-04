package rubikscubesolvernew;

import java.util.Arrays;
import java.util.List;

public class Test {

    static final byte[] SOLVED_PERM_CORNER = {0,1,2,3,4,5,6,7};
    static final byte[] SOLVED_PERM_EDGE   = {0,1,2,3,4,5,6,7,8,9,10,11};
    static final byte[] ZERO_ORI_8         = new byte[8];
    static final byte[] ZERO_ORI_12        = new byte[12];

    public static void main(String[] args) {
        testMoveOrder();
        testInverses();
        testRoundTrip();
    }

    // Applying a move 4x should return to identity
    static void testMoveOrder() {
        System.out.println("=== Move Order Test (move^4 = identity) ===");
        String[] names = {"U","R","F","D","L","B"};
        for (int i = 0; i < 6; i++) {
            CubeCubie result = new CubeCubie();
            for (int k = 0; k < 4; k++) result.multiply(CubeMoves.MOVE_CUBIES[i]);
            boolean ok = isIdentity(result);
            System.out.println(names[i] + "^4 = identity: " + (ok ? "PASS" : "FAIL"));
        }
    }

    // Applying move then inverse should give identity
    static void testInverses() {
        System.out.println("\n=== Inverse Test (move * inverse = identity) ===");
        String[] names = {"U","R","F","D","L","B"};
        for (int i = 0; i < 6; i++) {
            CubeCubie result = new CubeCubie();
            result.multiply(CubeMoves.MOVE_CUBIES[i]);
            result.multiply(CubeMoves.MOVE_CUBIES[i + 6]); // inverse
            boolean ok = isIdentity(result);
            System.out.println(names[i] + " * " + names[i] + "' = identity: " + (ok ? "PASS" : "FAIL"));
        }
    }

    // RubiksCube state-based rotation should match cubie-based multiply
    static void testRoundTrip() {
        System.out.println("\n=== Round Trip Test (state rotate matches cubie multiply) ===");
        String[] names = {"U","R","F","D","L","B"};
        for (int i = 0; i < 6; i++) {
            RubiksCube cube = new RubiksCube();
            cube.rotate(names[i]);
            int[] stateAfter = cube.getState();

            CubeCubie cubie = new CubeCubie();
            cubie.multiply(CubeMoves.MOVE_CUBIES[i]);
            int[] cubieAfter = RubiksCube.fromCubie(cubie);

            boolean ok = Arrays.equals(stateAfter, cubieAfter);
            System.out.println(names[i] + " round trip: " + (ok ? "PASS" : "FAIL"));
        }
    }

    static boolean isIdentity(CubeCubie c) {
        return Arrays.equals(c.cornerPerm, SOLVED_PERM_CORNER)
            && Arrays.equals(c.cornerOri,  ZERO_ORI_8)
            && Arrays.equals(c.edgePerm,   SOLVED_PERM_EDGE)
            && Arrays.equals(c.edgeOri,    ZERO_ORI_12);
    }

    static void diagnoseMove(String name, int moveIdx) {
        System.out.println("\n--- Diagnosing " + name + " ---");

        // State-based
        RubiksCube cube = new RubiksCube();
        cube.rotate(name);
        CubeCubie fromState = cube.toCubie();

        // Cubie-based
        CubeCubie fromMul = new CubeCubie();
        fromMul.multiply(CubeMoves.MOVE_CUBIES[moveIdx]);

        System.out.println("cornerPerm state : " + java.util.Arrays.toString(fromState.cornerPerm));
        System.out.println("cornerPerm cubie : " + java.util.Arrays.toString(fromMul.cornerPerm));
        System.out.println("cornerOri  state : " + java.util.Arrays.toString(fromState.cornerOri));
        System.out.println("cornerOri  cubie : " + java.util.Arrays.toString(fromMul.cornerOri));
        System.out.println("edgePerm   state : " + java.util.Arrays.toString(fromState.edgePerm));
        System.out.println("edgePerm   cubie : " + java.util.Arrays.toString(fromMul.edgePerm));
        System.out.println("edgeOri    state : " + java.util.Arrays.toString(fromState.edgeOri));
        System.out.println("edgeOri    cubie : " + java.util.Arrays.toString(fromMul.edgeOri));
    }

    static void testCoords() {
        System.out.println("\n=== Coordinate Round Trip Test ===");
        String[] names = {"U","R","F","D","L","B"};

        for (String name : names) {
            RubiksCube cube = new RubiksCube();
            cube.rotate(name);
            CubeCubie c = cube.toCubie();

            int twist      = CubeCoord.getTwist(c);
            int flip       = CubeCoord.getFlip(c);
            int udslice    = CubeCoord.getUDSlice(c);
            int cornerPerm = CubeCoord.getCornerPerm(c);
            int edgePerm   = CubeCoord.getEdgePerm(c);

            // Decode back and re-encode — should get same value
            CubeCubie t1 = new CubeCubie(); CubeCoord.setTwist(t1, twist);
            CubeCubie t2 = new CubeCubie(); CubeCoord.setFlip(t2, flip);
            CubeCubie t3 = new CubeCubie(); CubeCoord.setUDSlice(t3, udslice);
            CubeCubie t4 = new CubeCubie(); CubeCoord.setCornerPerm(t4, cornerPerm);
            CubeCubie t5 = new CubeCubie(); CubeCoord.setEdgePerm(t5, edgePerm);

            boolean ok = CubeCoord.getTwist(t1)      == twist
                    && CubeCoord.getFlip(t2)        == flip
                    && CubeCoord.getUDSlice(t3)     == udslice
                    && CubeCoord.getCornerPerm(t4)  == cornerPerm
                    && CubeCoord.getEdgePerm(t5)    == edgePerm;

            System.out.println("After " + name + ": " + (ok ? "PASS" : "FAIL")
                + "  twist=" + twist + " flip=" + flip + " udslice=" + udslice
                + " cPerm=" + cornerPerm + " ePerm=" + edgePerm);
        }
    }

    static void diagnoseCoord(String name, int moveIdx) {
        RubiksCube cube = new RubiksCube();
        cube.rotate(name);
        CubeCubie c = cube.toCubie();

        int twist      = CubeCoord.getTwist(c);
        int flip       = CubeCoord.getFlip(c);
        int udslice    = CubeCoord.getUDSlice(c);
        int cornerPerm = CubeCoord.getCornerPerm(c);
        int edgePerm   = CubeCoord.getEdgePerm(c);

        CubeCubie t1 = new CubeCubie(); CubeCoord.setTwist(t1, twist);
        CubeCubie t2 = new CubeCubie(); CubeCoord.setFlip(t2, flip);
        CubeCubie t3 = new CubeCubie(); CubeCoord.setUDSlice(t3, udslice);
        CubeCubie t4 = new CubeCubie(); CubeCoord.setCornerPerm(t4, cornerPerm);
        CubeCubie t5 = new CubeCubie(); CubeCoord.setEdgePerm(t5, edgePerm);

        System.out.println("After " + name + ":");
        System.out.println("  twist      " + (CubeCoord.getTwist(t1)     == twist      ? "PASS" : "FAIL"));
        System.out.println("  flip       " + (CubeCoord.getFlip(t2)      == flip       ? "PASS" : "FAIL"));
        System.out.println("  udslice    " + (CubeCoord.getUDSlice(t3)   == udslice    ? "PASS" : "FAIL"));
        System.out.println("  cornerPerm " + (CubeCoord.getCornerPerm(t4)== cornerPerm ? "PASS" : "FAIL"));
        System.out.println("  edgePerm   " + (CubeCoord.getEdgePerm(t5)  == edgePerm   ? "PASS" : "FAIL"));
    }

    static void testMoveTables() {
        System.out.println("\n=== Move Table Test ===");
        MoveTables.init();

        // In the solved state all coordinates are 0, except udslice which is 220
        // Applying a move then its inverse should return to the same value
        int solvedTwist   = 0;
        int solvedFlip    = 0;
        int solvedUDSlice = 220; // C(12,4)-1 combination where edges 8-11 are at positions 8-11
        
        String[] names = {"U","R","F","D","L","B","U'","R'","F'","D'","L'","B'","U2","R2","F2","D2","L2","B2"};
        boolean allPass = true;
        for (int m = 0; m < 18; m++) {
            int inv = (m < 6) ? m + 6 : (m < 12) ? m - 6 : m; // inverse move index
            
            int t  = MoveTables.twistMove  [MoveTables.twistMove  [solvedTwist][m]][inv];
            int f  = MoveTables.flipMove   [MoveTables.flipMove   [solvedFlip][m]][inv];
            int ud = MoveTables.udSliceMove[MoveTables.udSliceMove[solvedUDSlice][m]][inv];

            boolean pass = t == solvedTwist && f == solvedFlip && ud == solvedUDSlice;
            if (!pass) {
                System.out.println(names[m] + " then inverse: FAIL (twist=" + t + " flip=" + f + " udslice=" + ud + ")");
                allPass = false;
            }
        }
        if (allPass) System.out.println("All move table round trips: PASS");
    }

    static void testPruning() {
        System.out.println("\n=== Pruning Table Test ===");
        PruningTables.init();

        // Solved state should have distance 0 in all tables
        boolean p1a = PruningTables.twistFlipPrune[0 * MoveTables.N_FLIP + 0] == 0;
        boolean p1b = PruningTables.udSliceTwistPrune[220 * MoveTables.N_TWIST + 0] == 0;
        boolean p2a = PruningTables.cornerPermUDSlicePermPrune[0] == 0;
        boolean p2b = PruningTables.edgePermUDSlicePermPrune[0] == 0;

        System.out.println("twistFlip solved = 0:             " + (p1a ? "PASS" : "FAIL"));
        System.out.println("udSliceTwist solved = 0:          " + (p1b ? "PASS" : "FAIL"));
        System.out.println("cornerPermUDSlicePerm solved = 0: " + (p2a ? "PASS" : "FAIL"));
        System.out.println("edgePermUDSlicePerm solved = 0:   " + (p2b ? "PASS" : "FAIL"));
    }

    static void testSolver() {
        System.out.println("\n=== Solver Test ===");
        RubiksCube cube = new RubiksCube();
        cube.rotate("R"); cube.rotate("U");
        CubeCubie c = cube.toCubie();
        int[] solution = KociembaAlgorithm.solve(c);
        List<String> notation = KociembaAlgorithm.toNotation(solution);
        System.out.println("Raw solution moves: " + notation);

        RubiksCube verify = new RubiksCube();
        verify.rotate("R"); verify.rotate("U");
        for (String m : notation) {
            verify.rotate(m);
            System.out.println("After " + m + " - solved? " +
                java.util.Arrays.equals(verify.getState(), new RubiksCube().getState()));
        }

        RubiksCube p1check = new RubiksCube();
        p1check.rotate("R"); p1check.rotate("U");
        CubeCubie p1c = p1check.toCubie();
        System.out.println("\nBefore solution:");
        System.out.println("  twist=" + CubeCoord.getTwist(p1c) +
                        " flip=" + CubeCoord.getFlip(p1c) +
                        " udslice=" + CubeCoord.getUDSlice(p1c));
        for (String m : notation) {
            p1check.rotate(m);
            p1c = p1check.toCubie();
            System.out.println("After " + m +
                ": twist=" + CubeCoord.getTwist(p1c) +
                " flip=" + CubeCoord.getFlip(p1c) +
                " udslice=" + CubeCoord.getUDSlice(p1c) +
                " cPerm=" + CubeCoord.getCornerPerm(p1c) +
                " ePerm=" + CubeCoord.getEdgePerm(p1c) +
                " udSliceP=" + CubeCoord.getUDSlicePerm(p1c));
    }
        /*String[][] scrambles = {
            {"R", "U"},                          // 2 moves
            {"R", "U", "R'", "U'"},             // 4 moves
            {"R", "U", "R'", "U'",
            "R", "U", "R'", "U'",
            "R", "U", "R'", "U'"},             // sexy move x6 = 20 moves
        };

        for (String[] scramble : scrambles) {
            RubiksCube cube = new RubiksCube();
            for (String m : scramble) cube.rotate(m);

            CubeCubie c = cube.toCubie();
            long start = System.currentTimeMillis();
            int[] solution = KociembaAlgorithm.solve(c);
            long elapsed = System.currentTimeMillis() - start;

            if (solution == null) {
                System.out.println("Scramble: " + java.util.Arrays.toString(scramble) + " → NO SOLUTION FOUND");
                continue;
            }

            // Verify solution actually solves the cube
            RubiksCube verify = new RubiksCube();
            for (String m : scramble) verify.rotate(m);
            List<String> notation = KociembaAlgorithm.toNotation(solution);
            for (String m : notation) verify.rotate(m);

            boolean solved = java.util.Arrays.equals(verify.getState(), new RubiksCube().getState());
            System.out.println("Scramble length " + scramble.length 
                + " → solution: " + notation 
                + " (" + solution.length + " moves, " + elapsed + "ms) " 
                + (solved ? "PASS" : "FAIL - cube not solved!"));
        }*/
    }
}