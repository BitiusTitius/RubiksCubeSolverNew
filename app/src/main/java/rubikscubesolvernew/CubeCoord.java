package rubikscubesolvernew;

public class CubeCoord {
    public static int getTwist(CubeCubie cubie) {
        int twist = 0;

        for (int i = 0; i < 7; i++) {
            twist = twist * 3 + cubie.cornerOri[i];
        }

        return twist;
    }

    public static void setTwist(CubeCubie cubie, int twist) {
        int twistSum = 0;

        for (int i = 6; i >= 0; i--) {
            cubie.cornerOri[i] = (byte) (twist % 3);
            twistSum += cubie.cornerOri[i];
            twist /= 3;
        }

        cubie.cornerOri[7] = (byte) ((3 - (twistSum % 3)) % 3);
    }

    public static int getFlip(CubeCubie cubie) {
        int flip = 0;

        for (int i = 0; i < 11; i++) {
            flip = flip * 2 + cubie.edgeOri[i];
        }

        return flip;
    }

    public static void setFlip(CubeCubie cubie, int flip) {
        int flipSum = 0;

        for (int i = 10; i >= 0; i--) {
            cubie.edgeOri[i] = (byte) (flip % 2);
            flipSum += cubie.edgeOri[i];
            flip /= 2;
        }

        cubie.edgeOri[11] = (byte) ((2 - (flipSum % 2)) % 2);
    }

    // -- for up-down slice (UD) -- //

    public static int getUDSlice(CubeCubie cubie) {
        // Collect positions of the 4 slice edges (IDs >= 8)
        int[] pos = new int[4];
        int k = 0;
        for (int i = 0; i < 12; i++) {
            if (cubie.edgePerm[i] >= 8) pos[k++] = i;
        }
        // Enumerate combinations in same order as setUDSlice and return index
        int count = 0;
        for (int a = 0; a <= 8; a++) {
            for (int b = a + 1; b <= 9; b++) {
                for (int c = b + 1; c <= 10; c++) {
                    for (int d = c + 1; d <= 11; d++) {
                        if (a == pos[0] && b == pos[1] && c == pos[2] && d == pos[3]) return count;
                        count++;
                    }
                }
            }
        }
        return -1; // should never happen
    }

    public static void setUDSlice(CubeCubie cubie, int slice) {
        // Unrank combination by enumerating all 4-combinations of 12 positions
        // and placing the 4 slice edges at the combination corresponding to "slice".
        int[] edge = new int[12];
        java.util.Arrays.fill(edge, -1);

        int count = 0;
        boolean placed = false;
        for (int a = 0; a <= 8 && !placed; a++) {
            for (int b = a + 1; b <= 9 && !placed; b++) {
                for (int c = b + 1; c <= 10 && !placed; c++) {
                    for (int d = c + 1; d <= 11 && !placed; d++) {
                        if (count == slice) {
                            edge[a] = 8; // assign slice piece ids 8..11
                            edge[b] = 9;
                            edge[c] = 10;
                            edge[d] = 11;
                            placed = true;
                            break;
                        }
                        count++;
                    }
                }
            }
        }

        // Fill remaining positions with non-slice edges in order (0..7)
        int nonSlice = 0;
        for (int i = 0; i < 12; i++) {
            if (edge[i] == -1) edge[i] = nonSlice++;
        }
        for (int i = 0; i < 12; i++) cubie.edgePerm[i] = (byte) edge[i];
    }

    // -- for corner and edge perm -- //

    // Corner permutation: 0 to 40319 (8!)
    public static int getCornerPerm(CubeCubie cubie) {
        return lehmerEncode(cubie.cornerPerm, 8);
    }

    public static void setCornerPerm(CubeCubie cubie, int idx) {
        cubie.cornerPerm = lehmerDecode(idx, 8);
    }

    // Edge permutation of U/D edges only (edges 0-7): 0 to 40319 (8!)
    public static int getEdgePerm(CubeCubie cubie) {
        // Collect the 8 U/D edge piece IDs in positional order across all 12 edges
        byte[] udEdges = new byte[8];
        int k = 0;
        for (int i = 0; i < 12 && k < 8; i++) {
            if (cubie.edgePerm[i] < 8) udEdges[k++] = cubie.edgePerm[i];
        }
        return lehmerEncode(udEdges, 8);
    }

    public static void setEdgePerm(CubeCubie cubie, int idx) {
        byte[] decoded = lehmerDecode(idx, 8);
        // Place the decoded UD-edge piece IDs back into the positions that hold U/D edges
        int k = 0;
        for (int i = 0; i < 12 && k < 8; i++) {
            if (cubie.edgePerm[i] < 8) cubie.edgePerm[i] = decoded[k++];
        }
    }

    // UD-slice edge permutation: 0 to 23 (4!)
    public static int getUDSlicePerm(CubeCubie cubie) {
        // Remap slice edges to 0-3 for encoding
        byte[] sliceEdges = new byte[4];
        int k = 0;
        for (int i = 0; i < 12; i++) {
            if (cubie.edgePerm[i] >= 8) sliceEdges[k++] = (byte)(cubie.edgePerm[i] - 8);
        }
        return lehmerEncode(sliceEdges, 4);
    }

    public static void setUDSlicePerm(CubeCubie cubie, int idx) {
        byte[] decoded = lehmerDecode(idx, 4);
        int k = 0;
        for (int i = 0; i < 12; i++) {
            if (cubie.edgePerm[i] >= 8) cubie.edgePerm[i] = (byte)(decoded[k++] + 8);
        }
    }

    // --- Helpers ---
    private static int lehmerEncode(byte[] perm, int n) {
        boolean[] used = new boolean[n];
        int result = 0;
        for (int i = 0; i < n; i++) {
            int cnt = 0;
            for (int j = 0; j < perm[i]; j++) if (!used[j]) cnt++;
            result = result * (n - i) + cnt;
            used[perm[i]] = true;
        }
        return result;
    }

    private static byte[] lehmerDecode(int idx, int n) {
        byte[] perm = new byte[n];
        boolean[] used = new boolean[n];
        for (int i = 0; i < n; i++) {
            int fact = factorial(n - 1 - i);
            int k = idx / fact;
            idx %= fact;
            int cnt = 0;
            for (int j = 0; j < n; j++) {
                if (!used[j] && cnt++ == k) { perm[i] = (byte) j; used[j] = true; break; }
            }
        }
        return perm;
    }

    private static int factorial(int n) {
        int f = 1;
        for (int i = 2; i <= n; i++) f *= i;
        return f;
    }
}
