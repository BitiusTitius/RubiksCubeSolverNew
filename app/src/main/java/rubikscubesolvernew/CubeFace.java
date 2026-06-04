package rubikscubesolvernew;

public class CubeFace {
    public byte[] face = new byte[54];
    public static final int[][] CORNER_FACELETS = {
        { 8,  9, 20}, // URF: U=8,  R=9,  F=20
        { 6, 18, 38}, // UFL: U=6,  F=18, L=38
        { 0, 36, 47}, // ULB: U=0,  L=36, B=47
        { 2, 45, 11}, // UBR: U=2,  B=45, R=11
        {29, 26, 15}, // DFR: D=29, F=26, R=15
        {27, 44, 24}, // DLF: D=27, L=44, F=24  ← swap F and L
        {33, 53, 42}, // DBL: D=33, B=53, L=42  ← swap B and L
        {35, 17, 51}, // DRB: D=35, R=17, B=51  ← swap R and B
    };

    public static final int[][] CORNER_COLORS = {
        {1, 2, 3},   // URF: U=White, R=Red,    F=Green
        {1, 3, 5},   // UFL: U=White, F=Green,  L=Orange
        {1, 5, 6},   // ULB: U=White, L=Orange, B=Blue
        {1, 6, 2},   // UBR: U=White, B=Blue,   R=Red
        {4, 3, 2},   // DFR: D=Yellow, F=Green, R=Red
        {4, 5, 3},   // DLF: D=Yellow, L=Orange, F=Green  ← swap L and F
        {4, 6, 5},   // DBL: D=Yellow, B=Blue,  L=Orange  ← swap B and L
        {4, 2, 6},   // DRB: D=Yellow, R=Red,   B=Blue    ← swap R and B
    };
    public static final int[][] EDGE_FACELETS = {
        {10,  5}, // UR: R-face=10 first, U-face=5 second
        { 7, 19}, // UF: U=7, F=19  (unchanged)
        { 3, 37}, // UL: U=3, L=37  (unchanged)
        { 1, 46}, // UB: U=1, B=46  (unchanged)
        {16, 32}, // DR: R-face=16 first, D-face=32 second
        {28, 25}, // DF: D=28, F=25 (unchanged)
        {30, 43}, // DL: D=30, L=43 (unchanged)
        {34, 52}, // DB: D=34, B=52 (unchanged)
        {12, 23}, // FR: R-face=12 first, F-face=23 second
        {21, 41}, // FL: F=21, L=41 (unchanged)
        {50, 39}, // BL: B-face=50 first, L-face=39 second
        {14, 48}, // BR: R-face=14 first, B-face=48 second (was already R-first)
    };
    public static final int[][] EDGE_COLORS = {
        {2, 1},   // UR: R=Red, U=White
        {1, 3},   // UF: U=White, F=Green  (unchanged)
        {1, 5},   // UL: U=White, L=Orange (unchanged)
        {1, 6},   // UB: U=White, B=Blue   (unchanged)
        {2, 4},   // DR: R=Red, D=Yellow
        {4, 3},   // DF: D=Yellow, F=Green (unchanged)
        {4, 5},   // DL: D=Yellow, L=Orange(unchanged)
        {4, 6},   // DB: D=Yellow, B=Blue  (unchanged)
        {2, 3},   // FR: R=Red, F=Green
        {3, 5},   // FL: F=Green, L=Orange (unchanged)
        {6, 5},   // BL: B=Blue, L=Orange
        {2, 6},   // BR: R=Red, B=Blue     (unchanged)
    };

    public CubeFace() {
        face[4]  = 1; // U center (White)
        face[13] = 2; // R center (Red)
        face[22] = 3; // F center (Green)
        face[31] = 4; // D center (Yellow)
        face[40] = 5; // L center (Orange)
        face[49] = 6; // B center (Blue)
    }

    public CubeCubie toCubie() {
        CubeCubie cubie = new CubeCubie();

        // 1. Process corners (Fixed facelets compared against shifted piece colors)
        for (int i = 0; i < 8; i++) {
            boolean found = false;
            
            // Read the three physical facelets at this corner position natively
            byte color1 = face[CORNER_FACELETS[i][0]];
            byte color2 = face[CORNER_FACELETS[i][1]];
            byte color3 = face[CORNER_FACELETS[i][2]];

            // Try to match against all corner pieces and orientations
            for (byte pieceId = 0; pieceId < 8 && !found; pieceId++) {
                for (byte ori = 0; ori < 3; ori++) {
                    // Check cyclic shifts on the color values directly
                    if (color1 == CORNER_COLORS[pieceId][(0 + ori) % 3] && 
                        color2 == CORNER_COLORS[pieceId][(1 + ori) % 3] && 
                        color3 == CORNER_COLORS[pieceId][(2 + ori) % 3]) {
                        cubie.cornerPerm[i] = pieceId;
                        cubie.cornerOri[i] = ori;
                        found = true;
                        break;
                    }
                }
            }
            
            if (!found) {
                System.err.println("WARNING: Could not identify corner at position " + i);
                System.err.println("  Facelets: " + CORNER_FACELETS[i][0] + "=" + face[CORNER_FACELETS[i][0]] + 
                                   ", " + CORNER_FACELETS[i][1] + "=" + face[CORNER_FACELETS[i][1]] + 
                                   ", " + CORNER_FACELETS[i][2] + "=" + face[CORNER_FACELETS[i][2]]);
            }
        }

        // 2. Process edges (Fixed facelets compared against shifted piece colors)
        for (int i = 0; i < 12; i++) {
            boolean found = false;
            
            byte color1 = face[EDGE_FACELETS[i][0]];
            byte color2 = face[EDGE_FACELETS[i][1]];

            for (byte ori = 0; ori < 2 && !found; ori++) {
                for (byte pieceId = 0; pieceId < 12; pieceId++) {
                    if (color1 == EDGE_COLORS[pieceId][ori % 2] && 
                        color2 == EDGE_COLORS[pieceId][(ori + 1) % 2]) {
                        cubie.edgePerm[i] = pieceId;
                        cubie.edgeOri[i] = ori;
                        found = true;
                        break;
                    }
                }
            }
            
            if (!found) {
                System.err.println("WARNING: Could not identify edge at position " + i);
            }
        }

        return cubie;
    }
}
