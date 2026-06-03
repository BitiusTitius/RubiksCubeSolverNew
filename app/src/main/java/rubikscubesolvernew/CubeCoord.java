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
}
