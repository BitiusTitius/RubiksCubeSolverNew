package rubikscubesolvernew;

public class CubeMoves {
    public static final CubeCubie[] MOVE_CUBIES = new CubeCubie[18];

    static {
        String[] moveNames = {
            "U", "R", "F", "D", "L", "B", 
            "U'", "R'", "F'", "D'", "L'", "B'", 
            "U2", "R2", "F2", "D2", "L2", "B2"
        };

        for (int i = 0; i < 18; i++) {
            RubiksCube tempCube = new RubiksCube(); // Assumes clean solved state
            String move = moveNames[i];
            
            if (move.endsWith("2")) {
                // If it's a double move like "R2", turn "R" twice!
                String singleMove = move.substring(0, 1); 
                tempCube.rotate(singleMove);
                tempCube.rotate(singleMove);
            } else {
                tempCube.rotate(move);
            }
            
            MOVE_CUBIES[i] = tempCube.toCubie();
        }
    }
}
