package rubikscubesolvernew;

public class CubeFace {
    public byte[] face = new byte[54];
    public static final int[][] CORNER_FACELETS = {
        { 8,  9, 20}, // URF: U[8],  R[9],  F[20]
        { 6, 18, 38}, // UFL: U[6],  F[18], L[38]
        { 0, 36, 47}, // ULB: U[0],  L[36], B[47]
        { 2, 45, 11}, // UBR: U[2],  B[45], R[11]
        {29, 17, 26}, // DFR: swap → D[29], R[17], F[26]
        {27, 24, 44}, // DLF: swap → D[27], F[24], L[44]
        {35, 42, 53}, // DBL: swap → D[35], L[42], B[53]
        {33, 51, 15}, // DRB: swap → D[33], B[51], R[15]
    };
    public static final int[][] CORNER_COLORS = {
        {1, 2, 3},   // URF
        {1, 3, 5},   // UFL
        {1, 5, 6},   // ULB
        {1, 6, 2},   // UBR
        {4, 2, 3},   // DFR
        {4, 3, 5},   // DLF
        {4, 5, 6},   // DBL
        {4, 6, 2}    // DRB
    };
    public static final int[][] EDGE_FACELETS = {
        { 5, 10}, // UR: U[5]=1, R[1]=10→2
        { 7, 19}, // UF: U[7]=1, F[1]=19→3
        { 3, 37}, // UL: U[3]=1, L[1]=37→5
        { 1, 46}, // UB: U[1]=1, B[1]=46→6
        {32, 16}, // DR: D[5]=4, R[7]=16→2
        {28, 25}, // DF: D[1]=4, F[7]=25→3
        {30, 43}, // DL: D[3]=4, L[7]=43→5
        {34, 52}, // DB: D[7]=4, B[7]=52→6
        {23, 12}, // FR: F[5]=23→3, R[3]=12→2
        {21, 41}, // FL: F[3]=21→3, L[5]=41→5
        {39, 50}, // BL: L[3]=39→5, B[5]=50→6
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

        for (int i = 0; i < 8; i++) { // For each corner position
            byte ori = 0;

            for (ori = 0; ori < 3; ori++) {
                if(face[CORNER_FACELETS[i][0]] == 1 && face[CORNER_FACELETS[i][ori]] == 4) {
                    break;
                }

                byte color1 = face[CORNER_FACELETS[i][(ori) % 3]];
                byte color2 = face[CORNER_FACELETS[i][(ori + 1) % 3]];
                byte color3 = face[CORNER_FACELETS[i][(ori + 2) % 3]];

                for (byte pieceId = 0; pieceId < 8; pieceId++) {
                    if (color1 == CORNER_COLORS[pieceId][0] && color2 == CORNER_COLORS[pieceId][1] && color3 == CORNER_COLORS[pieceId][2]) {
                        cubie.cornerPerm[i] = pieceId;
                        cubie.cornerOri[i] = ori;
                        break;
                    }
                }
            }
        }

        for (int i = 0; i < 12; i++) {
            boolean found = false;

            for (byte ori = 0; ori < 2 && !found; ori++) {
                byte color1 = face[EDGE_FACELETS[i][ori % 2]];
                byte color2 = face[EDGE_FACELETS[i][(ori + 1) % 2]];

                for (byte pieceId = 0; pieceId < 12; pieceId++) {
                    if (color1 == EDGE_COLORS[pieceId][0] && color2 == EDGE_COLORS[pieceId][1]) {
                        cubie.edgePerm[i] = pieceId;
                        cubie.edgeOri[i] = 0; // matched in natural orientation
                        found = true;
                        break;
                    }
                    if (color1 == EDGE_COLORS[pieceId][1] && color2 == EDGE_COLORS[pieceId][0]) {
                        cubie.edgePerm[i] = pieceId;
                        cubie.edgeOri[i] = 1; // flipped
                        found = true;
                        break;
                    }
                }
            }
        }

        return cubie;
    }
}
