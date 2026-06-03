package rubikscubesolvernew;

public class Testing {
    public static void testMoveTablePeriodicity() {
        System.out.println("\nRunning Move Table Periodicity Tests...");
        
        // Map your move indices based on your Moves array layout
        // Assuming: 0=U, 1=R, 2=F, 3=D, 4=L, 5=B, ..., 12=U2, 13=R2...
        int[] singleMoves = {0, 1, 2, 3, 4, 5}; 
        int[] doubleMoves = {12, 13, 14, 15, 16, 17};

        boolean passed = true;

        // Test a few distinct starting states (e.g., state 0, state 100, state 2000)
        int[] testStates = {0, 100, 500, 2000};

        for (int startState : testStates) {
            // Check single moves (4-cycle)
            for (int m : singleMoves) {
                int state = startState;
                for (int i = 0; i < 4; i++) {
                    state = MoveTables.twistMove[state][m];
                }
                if (state != startState) {
                    System.err.println("Periodicity Failure: Move " + m + " repeated 4x on Twist " + startState + " resulted in " + state);
                    passed = false;
                }
            }

            // Check double moves (2-cycle)
            for (int m : doubleMoves) {
                int state = startState;
                for (int i = 0; i < 2; i++) {
                    state = MoveTables.twistMove[state][m];
                }
                if (state != startState) {
                    System.err.println("Periodicity Failure: Move " + m + " repeated 2x on Twist " + startState + " resulted in " + state);
                    passed = false;
                }
            }
        }

        if (passed) {
            System.out.println("Move Table Periodicity: PASSED");
        }
    }

    public static void testScrambleTracking() {
        System.out.println("\nRunning Scramble Parallel Track Test...");
        
        // 1. Create a facelet cube and execute a sequence
        RubiksCube realCube = new RubiksCube();
        String[] scramble = {"R", "U", "R'", "F", "R", "R", "U'"};
        
        for (String move : scramble) {
            realCube.rotate(move);
        }
        
        // Get the ground-truth ending coordinates from the actual physical state
        CubeCubie finalCubie = realCube.toCubie();
        int trueTwist = CubeCoord.getTwist(finalCubie);
        int trueFlip = CubeCoord.getFlip(finalCubie);

        // 2. Do the exact same sequence using only integer Lookups starting from 0 (solved)
        // Translate string moves to your 0-17 indices
        int[] scrambleIndices = {1, 0, 7, 8, 13, 6}; // R, U, R', F, R2, U'
        
        int tableTwist = 0;
        int tableFlip = 0;
        for (int m : scrambleIndices) {
            tableTwist = MoveTables.twistMove[tableTwist][m];
            tableFlip = MoveTables.flipMove[tableFlip][m];
        }

        System.out.println("True Twist: " + trueTwist + " | Table Twist: " + tableTwist);
        System.out.println("True Flip:  " + trueFlip + "  | Table Flip:  " + tableFlip);
        
        if (trueTwist == tableTwist && trueFlip == tableFlip) {
            System.out.println("Parallel Scramble Tracking: PASSED! Your systems are perfectly synchronized.");
        } else {
            System.out.println("Parallel Scramble Tracking: FAILED.");
        }
    }
}
