package rubikscubesolvernew;

import java.util.ArrayList;
import java.util.List;

public class KociembaAlgorithm {

    private static final int MAX_PHASE1 = 12;
    private static final int MAX_PHASE2 = 18;
    private static final int SOLVED_UDSLICE = 220;

    // Full solution found
    private static int[] bestSolution;

    public static int[] solve(CubeCubie cube) {
        PruningTables.init();
        bestSolution = null;

        int twist    = CubeCoord.getTwist(cube);
        int flip     = CubeCoord.getFlip(cube);
        int udslice  = CubeCoord.getUDSlice(cube);
        int cPerm    = CubeCoord.getCornerPerm(cube);
        int ePerm    = CubeCoord.getEdgePerm(cube);
        int udSliceP = CubeCoord.getUDSlicePerm(cube);

        // IDA* for Phase 1
        for (int depth = 0; depth <= MAX_PHASE1; depth++) {
            int[] moves = new int[depth];
            if (searchPhase1(twist, flip, udslice, cPerm, ePerm, udSliceP, 0, depth, moves)) {
                break;
            }
        }

        return bestSolution;
    }

    private static boolean searchPhase1(
            int twist, int flip, int udslice,
            int cPerm, int ePerm, int udSliceP,
            int depth, int maxDepth, int[] moves) {

        // Check if Phase 1 is solved
        if (twist == 0 && flip == 0 && udslice == SOLVED_UDSLICE) {
            // Try to solve Phase 2 from here
            return searchPhase2(cPerm, ePerm, udSliceP, depth, moves);
        }

        // Pruning
        int prune = Math.max(
            PruningTables.twistFlipPrune[twist * MoveTables.N_FLIP + flip],
            PruningTables.udSliceTwistPrune[udslice * MoveTables.N_TWIST + twist]
        );
        if (depth + prune > maxDepth) return false;

        // Try all 18 moves
        for (int m = 0; m < 18; m++) {
            // Avoid redundant moves (same face as last move)
            if (depth > 0 && sameAxis(moves[depth - 1], m)) continue;

            moves[depth] = m;
            if (searchPhase1(
                    MoveTables.twistMove[twist][m],
                    MoveTables.flipMove[flip][m],
                    MoveTables.udSliceMove[udslice][m],
                    MoveTables.cornerPermMove[cPerm][m],
                    MoveTables.edgePermMove[ePerm][m],
                    MoveTables.udSlicePermMove[udSliceP][m],
                    depth + 1, maxDepth, moves)) {
                return true;
            }
        }
        return false;
    }

    private static boolean searchPhase2(
            int cPerm, int ePerm, int udSliceP,
            int phase1Length, int[] phase1Moves) {

        // Check if Phase 2 is already solved
        if (cPerm == 0 && ePerm == 0 && udSliceP == 0) {
            bestSolution = java.util.Arrays.copyOf(phase1Moves, phase1Length);
            return true;
        }

        int[] moves = java.util.Arrays.copyOf(phase1Moves, phase1Length + MAX_PHASE2);

        for (int depth = 1; depth <= MAX_PHASE2 - phase1Length; depth++) {
            if (searchPhase2IDA(cPerm, ePerm, udSliceP, phase1Length, phase1Length, depth, moves)) {
                return true;
            }
        }
        return false;
    }

    private static boolean searchPhase2IDA(
            int cPerm, int ePerm, int udSliceP,
            int depth, int phase1Length, int maxDepth, int[] moves) {

        if (cPerm == 0 && ePerm == 0 && udSliceP == 0) {
            bestSolution = java.util.Arrays.copyOf(moves, depth);
            return true;
        }

        int prune = Math.max(
            PruningTables.cornerPermUDSlicePermPrune[cPerm * MoveTables.N_UDSLICEP + udSliceP],
            PruningTables.edgePermUDSlicePermPrune[ePerm * MoveTables.N_UDSLICEP + udSliceP]
        );
        if ((depth - phase1Length) + prune > maxDepth) return false;

        for (int m : PruningTables.PHASE2_MOVES) {
            // Avoid redundant moves
            if (depth > phase1Length && sameAxis(moves[depth - 1], m)) continue;

            moves[depth] = m;
            if (searchPhase2IDA(
                    MoveTables.cornerPermMove[cPerm][m],
                    MoveTables.edgePermMove[ePerm][m],
                    MoveTables.udSlicePermMove[udSliceP][m],
                    depth + 1, phase1Length, maxDepth, moves)) {
                return true;
            }
        }
        return false;
    }

    // Moves on the same face or opposite face in same axis shouldn't repeat
    // Move axes: U/D=0, R/L=1, F/B=2
    // Move indices: 0-5 = U,R,F,D,L,B; 6-11 = inverses; 12-17 = doubles
    private static boolean sameAxis(int m1, int m2) {
        int face1 = m1 % 6;
        int face2 = m2 % 6;
        // Same face
        if (face1 == face2) return true;
        // Opposite faces — only prune if same axis AND second move's face < first
        // to avoid duplicate sequences like D then U (already covered by U then D)
        int axis1 = face1 % 3;
        int axis2 = face2 % 3;
        if (axis1 == axis2 && face2 < face1) return true;
        return false;
    }

    // Convert move index array to notation strings
    public static List<String> toNotation(int[] moves) {
        String[] names = {"U","R","F","D","L","B","U'","R'","F'","D'","L'","B'","U2","R2","F2","D2","L2","B2"};
        List<String> result = new ArrayList<>();
        for (int m : moves) result.add(names[m]);
        return result;
    }
}