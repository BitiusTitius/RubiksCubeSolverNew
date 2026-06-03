package rubikscubesolvernew;

public class MoveTables {
    public static final int N_TWIST     = 2187;  // 3^7
    public static final int N_FLIP      = 2048;  // 2^11
    public static final int N_UDSLICE   = 495;   // C(12,4)
    public static final int N_CPERM     = 40320; // 8!
    public static final int N_EPERM     = 40320; // 8!
    public static final int N_UDSLICEP  = 24;    // 4!
    public static final int N_MOVES     = 18;

    public static int[][] twistMove     = new int[N_TWIST][N_MOVES];
    public static int[][] flipMove      = new int[N_FLIP][N_MOVES];
    public static int[][] udSliceMove   = new int[N_UDSLICE][N_MOVES];
    public static int[][] cornerPermMove= new int[N_CPERM][N_MOVES];
    public static int[][] edgePermMove  = new int[N_EPERM][N_MOVES];
    public static int[][] udSlicePermMove = new int[N_UDSLICEP][N_MOVES];

    private static boolean initialized = false;

    public static void init() {
        if (initialized) return;
        System.out.println("Generating move tables...");

        // Twist
        for (int i = 0; i < N_TWIST; i++) {
            for (int m = 0; m < N_MOVES; m++) {
                CubeCubie c = new CubeCubie();
                CubeCoord.setTwist(c, i);
                c.multiply(CubeMoves.MOVE_CUBIES[m]);
                twistMove[i][m] = CubeCoord.getTwist(c);
            }
        }
        System.out.println("  twist done");

        // Flip
        for (int i = 0; i < N_FLIP; i++) {
            for (int m = 0; m < N_MOVES; m++) {
                CubeCubie c = new CubeCubie();
                CubeCoord.setFlip(c, i);
                c.multiply(CubeMoves.MOVE_CUBIES[m]);
                flipMove[i][m] = CubeCoord.getFlip(c);
            }
        }
        System.out.println("  flip done");

        // UDSlice
        for (int i = 0; i < N_UDSLICE; i++) {
            for (int m = 0; m < N_MOVES; m++) {
                CubeCubie c = new CubeCubie();
                CubeCoord.setUDSlice(c, i);
                c.multiply(CubeMoves.MOVE_CUBIES[m]);
                udSliceMove[i][m] = CubeCoord.getUDSlice(c);
            }
        }
        System.out.println("  udSlice done");

        // Corner permutation
        for (int i = 0; i < N_CPERM; i++) {
            for (int m = 0; m < N_MOVES; m++) {
                CubeCubie c = new CubeCubie();
                CubeCoord.setCornerPerm(c, i);
                c.multiply(CubeMoves.MOVE_CUBIES[m]);
                cornerPermMove[i][m] = CubeCoord.getCornerPerm(c);
            }
        }
        System.out.println("  cornerPerm done");

        // Edge permutation (Phase 2 only — valid when slice edges are in slice)
        for (int i = 0; i < N_EPERM; i++) {
            for (int m = 0; m < N_MOVES; m++) {
                CubeCubie c = new CubeCubie();
                CubeCoord.setEdgePerm(c, i);
                // Place slice edges in their home positions so multiply works correctly
                c.edgePerm[8]  = 8;
                c.edgePerm[9]  = 9;
                c.edgePerm[10] = 10;
                c.edgePerm[11] = 11;
                c.multiply(CubeMoves.MOVE_CUBIES[m]);
                edgePermMove[i][m] = CubeCoord.getEdgePerm(c);
            }
        }
        System.out.println("  edgePerm done");

        // UDSlice permutation (Phase 2)
        for (int i = 0; i < N_UDSLICEP; i++) {
            for (int m = 0; m < N_MOVES; m++) {
                CubeCubie c = new CubeCubie();
                // Need slice edges in slice positions first, then permute them
                c.edgePerm[8]  = 8;
                c.edgePerm[9]  = 9;
                c.edgePerm[10] = 10;
                c.edgePerm[11] = 11;
                CubeCoord.setUDSlicePerm(c, i);
                c.multiply(CubeMoves.MOVE_CUBIES[m]);
                udSlicePermMove[i][m] = CubeCoord.getUDSlicePerm(c);
            }
        }
        System.out.println("  udSlicePerm done");

        initialized = true;
        System.out.println("All move tables ready.");
    }
}
