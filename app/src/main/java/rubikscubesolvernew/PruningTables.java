package rubikscubesolvernew;

import java.util.ArrayDeque;

public class PruningTables {
    // Phase 1
    public static byte[] twistFlipPrune;
    public static byte[] udSliceTwistPrune;

    // Phase 2
    public static byte[] cornerPermUDSlicePermPrune;
    public static byte[] edgePermUDSlicePermPrune;

    private static boolean initialized = false;

    public static void init() {
        if (initialized) return;
        MoveTables.init();
        System.out.println("Generating pruning tables...");

        twistFlipPrune            = buildPrune1_TwistFlip();
        udSliceTwistPrune         = buildPrune1_UDSliceTwist();
        cornerPermUDSlicePermPrune= buildPrune2_CornerPerm();
        edgePermUDSlicePermPrune  = buildPrune2_EdgePerm();

        initialized = true;
        System.out.println("Pruning tables ready.");
    }

    // Phase 1: twist + flip
    private static byte[] buildPrune1_TwistFlip() {
        int SIZE = MoveTables.N_TWIST * MoveTables.N_FLIP;
        byte[] table = new byte[SIZE];
        java.util.Arrays.fill(table, (byte) -1);

        // Solved state: twist=0, flip=0
        int solvedFlip = 0, solvedTwist = 0;
        table[solvedTwist * MoveTables.N_FLIP + solvedFlip] = 0;

        ArrayDeque<Integer> queue = new ArrayDeque<>();
        queue.add(solvedTwist * MoveTables.N_FLIP + solvedFlip);

        while (!queue.isEmpty()) {
            int state = queue.poll();
            int twist = state / MoveTables.N_FLIP;
            int flip  = state % MoveTables.N_FLIP;
            byte dist = table[state];

            for (int m = 0; m < MoveTables.N_MOVES; m++) {
                int newTwist = MoveTables.twistMove[twist][m];
                int newFlip  = MoveTables.flipMove[flip][m];
                int newState = newTwist * MoveTables.N_FLIP + newFlip;
                if (table[newState] == -1) {
                    table[newState] = (byte)(dist + 1);
                    queue.add(newState);
                }
            }
        }
        System.out.println("  twistFlip done");
        return table;
    }

    // Phase 1: udSlice + twist
    private static byte[] buildPrune1_UDSliceTwist() {
        int SIZE = MoveTables.N_UDSLICE * MoveTables.N_TWIST;
        byte[] table = new byte[SIZE];
        java.util.Arrays.fill(table, (byte) -1);

        // Solved: udslice=220, twist=0
        int solvedUDSlice = 220, solvedTwist = 0;
        table[solvedUDSlice * MoveTables.N_TWIST + solvedTwist] = 0;

        ArrayDeque<Integer> queue = new ArrayDeque<>();
        queue.add(solvedUDSlice * MoveTables.N_TWIST + solvedTwist);

        while (!queue.isEmpty()) {
            int state   = queue.poll();
            int udSlice = state / MoveTables.N_TWIST;
            int twist   = state % MoveTables.N_TWIST;
            byte dist   = table[state];

            for (int m = 0; m < MoveTables.N_MOVES; m++) {
                int newUDSlice = MoveTables.udSliceMove[udSlice][m];
                int newTwist   = MoveTables.twistMove[twist][m];
                int newState   = newUDSlice * MoveTables.N_TWIST + newTwist;
                if (table[newState] == -1) {
                    table[newState] = (byte)(dist + 1);
                    queue.add(newState);
                }
            }
        }
        System.out.println("  udSliceTwist done");
        return table;
    }

    // Phase 2: cornerPerm + udSlicePerm
    private static byte[] buildPrune2_CornerPerm() {
        int SIZE = MoveTables.N_CPERM * MoveTables.N_UDSLICEP;
        byte[] table = new byte[SIZE];
        java.util.Arrays.fill(table, (byte) -1);

        // Solved: cornerPerm=0, udSlicePerm=0
        table[0] = 0;
        ArrayDeque<Integer> queue = new ArrayDeque<>();
        queue.add(0);

        while (!queue.isEmpty()) {
            int state      = queue.poll();
            int cPerm      = state / MoveTables.N_UDSLICEP;
            int udSlicePerm= state % MoveTables.N_UDSLICEP;
            byte dist      = table[state];

            // Phase 2 only uses G1 moves: U,D,R2,L2,F2,B2
            for (int m : PHASE2_MOVES) {
                int newCPerm       = MoveTables.cornerPermMove[cPerm][m];
                int newUDSlicePerm = MoveTables.udSlicePermMove[udSlicePerm][m];
                int newState       = newCPerm * MoveTables.N_UDSLICEP + newUDSlicePerm;
                if (table[newState] == -1) {
                    table[newState] = (byte)(dist + 1);
                    queue.add(newState);
                }
            }
        }
        System.out.println("  cornerPermUDSlicePerm done");
        return table;
    }

    // Phase 2: edgePerm + udSlicePerm
    private static byte[] buildPrune2_EdgePerm() {
        int SIZE = MoveTables.N_EPERM * MoveTables.N_UDSLICEP;
        byte[] table = new byte[SIZE];
        java.util.Arrays.fill(table, (byte) -1);

        table[0] = 0;
        ArrayDeque<Integer> queue = new ArrayDeque<>();
        queue.add(0);

        while (!queue.isEmpty()) {
            int state      = queue.poll();
            int ePerm      = state / MoveTables.N_UDSLICEP;
            int udSlicePerm= state % MoveTables.N_UDSLICEP;
            byte dist      = table[state];

            for (int m : PHASE2_MOVES) {
                int newEPerm       = MoveTables.edgePermMove[ePerm][m];
                int newUDSlicePerm = MoveTables.udSlicePermMove[udSlicePerm][m];
                int newState       = newEPerm * MoveTables.N_UDSLICEP + newUDSlicePerm;
                if (table[newState] == -1) {
                    table[newState] = (byte)(dist + 1);
                    queue.add(newState);
                }
            }
        }
        System.out.println("  edgePermUDSlicePerm done");
        return table;
    }

    // G1 moves: U(0), R2(13), F2(14), D(3), L2(16), B2(17), U'(6), D'(9)
    // Indices into MOVE_CUBIES: 0=U,1=R,2=F,3=D,4=L,5=B,6=U',7=R',8=F',9=D',10=L',11=B',12=U2,13=R2,14=F2,15=D2,16=L2,17=B2
    public static final int[] PHASE2_MOVES = {0, 3, 6, 9, 12, 13, 14, 15, 16, 17};
}