package rubikscubesolvernew;

public class CubeMoves {
    public static final CubeCubie[] MOVE_CUBIES = new CubeCubie[18];

    static {
        // ==================== U ====================
        CubeCubie U = new CubeCubie();
        U.cornerPerm = new byte[]{3, 0, 1, 2,  4, 5, 6, 7};
        U.cornerOri  = new byte[]{0, 0, 0, 0,  0, 0, 0, 0};
        U.edgePerm   = new byte[]{3, 0, 1, 2,  4, 5, 6, 7,  8, 9, 10, 11};
        U.edgeOri    = new byte[]{0, 0, 0, 0,  0, 0, 0, 0,  0, 0,  0,  0};
        MOVE_CUBIES[0] = U;

        // ==================== R ====================
        // Corners cycle: URF(0)→DFR(4)→DRB(7)→UBR(3)→URF(0)
        // Twist pattern: +2, +1, +2, +1 (alternating as corner enters/leaves R face)
        CubeCubie R = new CubeCubie();
        R.cornerPerm = new byte[]{4, 1, 2, 0,  7, 5, 6, 3};
        R.cornerOri  = new byte[]{1, 0, 0, 2,  2, 0, 0, 1};
        // Edges cycle: UR(0)→FR(8)→DR(4)→BR(11)→UR(0), no flip
        R.edgePerm   = new byte[]{8, 1, 2, 3,  11, 5, 6, 7,  4, 9, 10, 0};
        R.edgeOri    = new byte[]{0, 0, 0, 0,  1, 0, 0, 0,  0, 0, 0, 1};
        MOVE_CUBIES[1] = R;

        // ==================== F ====================
        // Corners cycle: URF(0)→UFL(1)→DLF(5)→DFR(4)→URF(0)
        // Twist pattern: +1, +2, +1, +2
        CubeCubie F = new CubeCubie();
        F.cornerPerm = new byte[]{1, 5, 2, 3,  0, 4, 6, 7};
        F.cornerOri  = new byte[]{2, 1, 0, 0,  1, 2, 0, 0};
        // Edges cycle: UF(1)→FL(9)→DF(5)→FR(8)→UF(1), all flipped
        F.edgePerm   = new byte[]{0, 9, 2, 3,  4, 8, 6, 7,  1, 5, 10, 11};
        F.edgeOri    = new byte[]{0, 1, 0, 0,  0, 1, 0, 0,  1, 1,  0,  0};
        MOVE_CUBIES[2] = F;

        // ==================== D ====================
        // Corners cycle: DFR(4)→DLF(5)→DBL(6)→DRB(7)→DFR(4), no twist
        CubeCubie D = new CubeCubie();
        D.cornerPerm = new byte[]{0, 1, 2, 3,  5, 6, 7, 4};
        D.cornerOri  = new byte[]{0, 0, 0, 0,  0, 0, 0, 0};
        // Edges cycle: DF(5)→DL(6)→DB(7)→DR(4)→DF(5), no flip
        D.edgePerm   = new byte[]{0, 1, 2, 3,  5, 6, 7, 4,  8, 9, 10, 11};
        D.edgeOri    = new byte[]{0, 0, 0, 0,  0, 0, 0, 0,  0, 0,  0,  0};
        MOVE_CUBIES[3] = D;

        // ==================== L ====================
        // Corners cycle: UFL(1)→ULB(2)→DBL(6)→DLF(5)→UFL(1)
        // Twist pattern: +1, +2, +1, +2
        CubeCubie L = new CubeCubie();
        L.cornerPerm = new byte[]{0, 2, 6, 3,  4, 1, 5, 7};
        L.cornerOri  = new byte[]{0, 2, 1, 0,  0, 1, 2, 0};
        // Edges cycle: UL(2)→BL(10)→DL(6)→FL(9)→UL(2), no flip
        L.edgePerm   = new byte[]{0, 1, 10, 3,  4, 5, 9, 7,  8, 2, 6, 11};
        L.edgeOri    = new byte[]{0, 0, 1, 0,  0, 0, 0, 0,  0, 0, 1, 0};
        MOVE_CUBIES[4] = L;

        // ==================== B ====================
        // Corners cycle: ULB(2)→UBR(3)→DRB(7)→DBL(6)→ULB(2)
        // Twist pattern: +1, +2, +1, +2
        CubeCubie B = new CubeCubie();
        B.cornerPerm = new byte[]{0, 1, 3, 7,  4, 5, 2, 6};
        B.cornerOri  = new byte[]{0, 0, 2, 1,  0, 0, 1, 2};
        // Edges cycle: UB(3)→BR(11)→DB(7)→BL(10)→UB(3), all flipped
        B.edgePerm   = new byte[]{0, 1, 2, 11,  4, 5, 6, 10,  8, 9, 3, 7};
        B.edgeOri    = new byte[]{0, 0, 0, 0,  0, 0, 0, 0,  0, 0, 0, 0};
        MOVE_CUBIES[5] = B;

        // ==================== INVERSES (U', R', F', D', L', B') ====================
        for (int i = 0; i < 6; i++) {
            CubeCubie base = MOVE_CUBIES[i];
            CubeCubie inv = new CubeCubie();
            // Apply the move 3 times to get its inverse
            inv.multiply(base);
            inv.multiply(base);
            inv.multiply(base);
            MOVE_CUBIES[i + 6] = inv;
        }

        // ==================== DOUBLES (U2, R2, F2, D2, L2, B2) ====================
        for (int i = 0; i < 6; i++) {
            CubeCubie base = MOVE_CUBIES[i];
            CubeCubie dbl = new CubeCubie();
            dbl.multiply(base);
            dbl.multiply(base);
            MOVE_CUBIES[i + 12] = dbl;
        }
    }
}