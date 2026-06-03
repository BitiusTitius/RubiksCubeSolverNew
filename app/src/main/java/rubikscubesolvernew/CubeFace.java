package rubikscubesolvernew;

public class CubeFace {
    public byte[] face = new byte[54];
    public static final int[][] CORNER_FACELETS = {
        { 8,  9, 20}, // URF
        { 6, 18, 38}, // UFL
        { 0, 36, 47}, // ULB
        { 2, 45, 11}, // UBR
        {29, 26, 15}, // DFR
        {27, 44, 24}, // DLF
        {33, 53, 42}, // DBL
        {35, 17, 51}, // DRB
    };

    public static final int[][] CORNER_COLORS = {
        {1, 2, 3},   // URF
        {1, 3, 5},   // UFL
        {1, 5, 6},   // ULB
        {1, 6, 2},   // UBR
        {4, 3, 2},   // DFR
        {4, 5, 3},   // DLF
        {4, 6, 5},   // DBL
        {4, 2, 6}    // DRB 
    };
    public static final int[][] EDGE_FACELETS = {
        { 5, 10}, // UR
        { 7, 19}, // UF
        { 3, 37}, // UL
        { 1, 46}, // UB
        {32, 16}, // DR
        {28, 25}, // DF
        {30, 43}, // DL
        {34, 52}, // DB
        {23, 12}, // FR
        {21, 41}, // FL
        {39, 50}, // BL
        {14, 48},
    };
    public static final int[][] EDGE_COLORS = {
        {1, 2},   // UR
        {1, 3},   // UF
        {1, 5},   // UL
        {1, 6},   // UB
        {4, 2},   // DR
        {4, 3},   // DF
        {4, 5},   // DL
        {4, 6},   // DB
        {3, 2},   // FR
        {3, 5},   // FL
        {5, 6},   // BL
        {2, 6}    // BR
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
