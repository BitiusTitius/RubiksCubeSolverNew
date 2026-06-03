package rubikscubesolvernew;

public class CubeCubie {
    public byte[] cornerPerm = new byte[8]; // 8 corner pieces
    public byte[] cornerOri = new byte[8]; // Orientation of each corner piece (0, 1, or 2)
    public byte[] edgePerm = new byte[12]; // 12 edge pieces
    public byte[] edgeOri = new byte[12]; // Orientation of each edge piece (0 or 1)

    public CubeCubie() {
        for (byte i = 0; i < 8; i++) {
            this.cornerPerm[i] = i;
            this.cornerOri[i] = 0;
        }

        for (byte i = 0; i < 12; i++) {
            this.edgePerm[i] = i;
            this.edgeOri[i] = 0;
        }
    }

    public void multiply(CubeCubie other) {
        cornerMultiply(other);
        edgeMultiply(other);
    }

    public void cornerMultiply(CubeCubie other) {
        byte[] newCornerPerm = new byte[8];
        byte[] newCornerOri = new byte[8];

        for (int dst = 0; dst < 8; dst++) {
            int src = other.cornerPerm[dst];
            newCornerPerm[dst] = this.cornerPerm[src];
            newCornerOri[dst] = (byte) ((this.cornerOri[src] + other.cornerOri[dst]) % 3);
        }

        this.cornerPerm = newCornerPerm;
        this.cornerOri = newCornerOri;
    }

    public void edgeMultiply(CubeCubie other) {
        byte[] newEdgePerm = new byte[12];
        byte[] newEdgeOri = new byte[12];

        for (int dst = 0; dst < 12; dst++) {
            int src = other.edgePerm[dst];
            newEdgePerm[dst] = this.edgePerm[src];
            newEdgeOri[dst] = (byte) ((this.edgeOri[src] + other.edgeOri[dst]) % 2);
        }

        this.edgePerm = newEdgePerm;
        this.edgeOri = newEdgeOri;
    }

    public CubeFace toFacelet() {
        CubeFace face = new CubeFace();

        for (int i = 0; i < 8; i++) {
            int pieceId = this.cornerPerm[i];
            int ori = this.cornerOri[i];

            for (int j = 0; j < 3; j++) {
                face.face[CubeFace.CORNER_FACELETS[i][j]] = (byte) CubeFace.CORNER_COLORS[pieceId][(j + ori) % 3];
            }
        }

        for (int i = 0; i < 12; i++) {
            int pieceId = this.edgePerm[i];
            int ori = edgeOri[i];

            for (int j = 0; j < 2; j++) {
                face.face[CubeFace.EDGE_FACELETS[i][j]] = (byte) CubeFace.EDGE_COLORS[pieceId][(j + ori) % 2];
            }
        }

        return face;
    }
}