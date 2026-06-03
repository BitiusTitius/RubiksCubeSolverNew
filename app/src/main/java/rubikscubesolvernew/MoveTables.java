package rubikscubesolvernew;

public class MoveTables {
    public static int[][] twistMove = new int[2187][18];
    public static int[][] flipMove = new int[2048][18];
    private static boolean initialized = false;

    public static void init() {
        if (initialized) return;

        System.out.println("Generating Twist and Flip move tables...");
        CubeCubie temp = new CubeCubie();

        for (int twist = 0; twist < 2187; twist++) {
            for (int move = 0; move < 18; move++) {
                CubeCoord.setTwist(temp, twist);
                CubeCubie result = new CubeCubie();

                result.cornerPerm = temp.cornerPerm.clone();
                result.cornerOri = temp.cornerOri.clone();
                result.edgePerm = temp.edgePerm.clone();
                result.edgeOri = temp.edgeOri.clone();

                result.multiply(CubeMoves.MOVE_CUBIES[move]);

                twistMove[twist][move] = CubeCoord.getTwist(result);
            }
        }

        for (int flip = 0; flip < 2048; flip++) {
            for (int move = 0; move < 18; move++) {
                CubeCoord.setFlip(temp, flip);
                CubeCubie result = new CubeCubie();

                result.cornerPerm = temp.cornerPerm.clone();
                result.cornerOri = temp.cornerOri.clone();
                result.edgePerm = temp.edgePerm.clone();
                result.edgeOri = temp.edgeOri.clone();

                result.multiply(CubeMoves.MOVE_CUBIES[move]);

                // Save to flipMove using getFlip!
                flipMove[flip][move] = CubeCoord.getFlip(result);
            }
        }

        initialized = true;
        System.out.println("Move tables initialized successfully.");
    }
}
