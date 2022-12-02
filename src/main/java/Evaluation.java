import com.github.bhlangonijr.chesslib.*;
import com.github.bhlangonijr.chesslib.move.Move;
import com.github.bhlangonijr.chesslib.move.MoveList;

import static java.lang.Math.min;

public class Evaluation {
    static final int PAWN_VALUE = 100;
    static final int BISHOP_VALUE = 320;
    static final int KNIGHT_VALUE = 315;
    static final int ROOK_VALUE = 500;
    static final int QUEEN_VALUE = 900;
    static final int MAX_VALUE = 40000;
    static final int MATE_VALUE = 39000;

    static final int[] PAWN_PST = {
            0, 0, 0, 0, 0, 0, 0, 0,
            50, 50, 50, 50, 50, 50, 50, 50,
            10, 10, 20, 30, 30, 20, 10, 10,
            5, 5, 10, 25, 25, 10, 5, 5,
            0, 0, 0, 20, 20, 0, 0, 0,
            5, -5, -10, 0, 0, -10, -5, 5,
            5, 10, 10, -20, -20, 10, 10, 5,
            0, 0, 0, 0, 0, 0, 0, 0};

    static final int[] KNIGHT_PST = {
            -50, -40, -30, -30, -30, -30, -40, -50,
            -40, -20, 0, 0, 0, 0, -20, -40,
            -30, 0, 10, 15, 15, 10, 0, -30,
            -30, 5, 15, 20, 20, 15, 5, -30,
            -30, 0, 15, 20, 20, 15, 0, -30,
            -30, 5, 10, 15, 15, 10, 5, -30,
            -40, -20, 0, 5, 5, 0, -20, -40,
            -50, -40, -30, -30, -30, -30, -40, -50
    };

    static final int[] BISHOP_PST = {
            -20, -10, -10, -10, -10, -10, -10, -20,
            -10, 0, 0, 0, 0, 0, 0, -10,
            -10, 0, 5, 10, 10, 5, 0, -10,
            -10, 5, 5, 10, 10, 5, 5, -10,
            -10, 0, 10, 10, 10, 10, 0, -10,
            -10, 10, 10, 10, 10, 10, 10, -10,
            -10, 5, 0, 0, 0, 0, 5, -10,
            -20, -10, -10, -10, -10, -10, -10, -20
    };

    static final int[] ROOK_PST = {
            0, 0, 0, 0, 0, 0, 0, 0,
            5, 10, 10, 10, 10, 10, 10, 5,
            -5, 0, 0, 0, 0, 0, 0, -5,
            -5, 0, 0, 0, 0, 0, 0, -5,
            -5, 0, 0, 0, 0, 0, 0, -5,
            -5, 0, 0, 0, 0, 0, 0, -5,
            -5, 0, 0, 0, 0, 0, 0, -5,
            0, 0, 0, 5, 5, 0, 0, 0
    };

    static final int[] QUEEN_PST = {
            -20, -10, -10, -5, -5, -10, -10, -20,
            -10, 0, 0, 0, 0, 0, 0, -10,
            -10, 0, 5, 5, 5, 5, 0, -10,
            -5, 0, 5, 5, 5, 5, 0, -5,
            0, 0, 5, 5, 5, 5, 0, -5,
            -10, 5, 5, 5, 5, 5, 0, -10,
            -10, 0, 5, 0, 0, 0, 0, -10,
            -20, -10, -10, -5, -5, -10, -10, -20
    };

    static final int[] KING_OPENING_PST = {
            -30, -40, -40, -50, -50, -40, -40, -30,
            -30, -40, -40, -50, -50, -40, -40, -30,
            -30, -40, -40, -50, -50, -40, -40, -30,
            -30, -40, -40, -50, -50, -40, -40, -30,
            -20, -30, -30, -40, -40, -30, -30, -20,
            -10, -20, -20, -20, -20, -20, -20, -10,
            20, 20, 0, 0, 0, 0, 20, 20,
            20, 30, 10, 0, 0, 10, 30, 20
    };

    static final int[] KING_END_PST = {
            -50, -40, -30, -20, -20, -30, -40, -50,
            -30, -20, -10, 0, 0, -10, -20, -30,
            -30, -10, 20, 30, 30, 20, -10, -30,
            -30, -10, 30, 40, 40, 30, -10, -30,
            -30, -10, 30, 40, 40, 30, -10, -30,
            -30, -10, 20, 30, 30, 20, -10, -30,
            -30, -30, 0, 0, 0, 0, -30, -30,
            -50, -30, -30, -30, -30, -30, -30, -50
    };

    static final int MAX_MATERIAL = PAWN_VALUE * 8 + KNIGHT_VALUE * 2 + BISHOP_VALUE * 2 + ROOK_VALUE * 2 + QUEEN_VALUE;

    public static int evaluateMove(Board board, Move move, Side playerSide) {
        Board newBoard = board.clone();
        newBoard.doMove(move);
        return (int) evaluate(newBoard, playerSide);
    }

    public static long evaluate(Board board, Side playerSide) {
        long materialScoreSide = countMaterial(board, playerSide);
        long materialScoreOtherSide =  countMaterial(board, playerSide.flip());
        long squareTableScoreSide = countSquareTable(board, playerSide, materialScoreSide);
        long squareTableScoreOtherSide = countSquareTable(board, playerSide.flip(), materialScoreOtherSide);
/*        System.out.println("material side : " + materialScoreSide);
        System.out.println("material othe side : " + materialScoreOtherSide);
        System.out.println("squareTable side : " + squareTableScoreSide);
        System.out.println("squareTable other side : " + squareTableScoreOtherSide);*/
        return materialScoreSide - materialScoreOtherSide + squareTableScoreSide - squareTableScoreOtherSide;
    }

/*    private static long materialEvaluation(Board board) {
        return countMaterial(board, board.getSideToMove()) - countMaterial(board, board.getSideToMove().flip());
    }*/


    private static long countMaterial(Board board, Side side) {
        long counter = bitCount(board.getBitboard(Piece.make(side, PieceType.PAWN))) * PAWN_VALUE +
                bitCount(board.getBitboard(Piece.make(side, PieceType.BISHOP))) * BISHOP_VALUE +
                bitCount(board.getBitboard(Piece.make(side, PieceType.KNIGHT))) * KNIGHT_VALUE +
                bitCount(board.getBitboard(Piece.make(side, PieceType.ROOK))) * ROOK_VALUE +
                bitCount(board.getBitboard(Piece.make(side, PieceType.QUEEN))) * QUEEN_VALUE;
        return counter;
    }

    private static long bitCount(Long bitBoard) {
        return java.lang.Long.bitCount(bitBoard);
    }

    private static long squareTableEvaluation(Board board, long material) {
        return countSquareTable(board, board.getSideToMove(), material) - countSquareTable(board, board.getSideToMove().flip(), material);
    }

    private static long countSquareTable(Board board, Side side, long material) {
        long phase = min(MAX_MATERIAL, material);
        long sum = 0;

        long pieces = board.getBitboard(side) & ~(board.getBitboard(Piece.make(side, PieceType.KING)));

        while (pieces != 0) {
            int index = Bitboard.bitScanForward(pieces);
            pieces = Bitboard.extractLsb(pieces);
            Square sq = Square.squareAt(index);
            sum += pieceSquareStaticValue(board.getPiece(sq), sq);
        }

        // Opening and end king PST interpolation
        for (Square tempSq :
                board.getPieceLocation(Piece.make(side, PieceType.KING))) {
            sum += (MAX_MATERIAL - phase) * KING_END_PST[getIndex(side, tempSq)] / MAX_MATERIAL +
                    phase * KING_OPENING_PST[getIndex(side, tempSq)] / MAX_MATERIAL;
        }

        return sum;

    }

    private static int getIndex(Side side, Square sq) {
        return side == Side.BLACK ? sq.ordinal() : 63 - sq.ordinal();
    }

    private static long pieceSquareStaticValue(Piece piece, Square square) {
        if (piece.getPieceType() == PieceType.PAWN)
            return PAWN_PST[getIndex(piece.getPieceSide(), square)];
        else if (piece.getPieceType() == PieceType.KNIGHT)
            return KNIGHT_PST[getIndex(piece.getPieceSide(), square)];
        else if (piece.getPieceType() == PieceType.BISHOP)
            return BISHOP_PST[getIndex(piece.getPieceSide(), square)];
        else if (piece.getPieceType() == PieceType.ROOK)
            return ROOK_PST[getIndex(piece.getPieceSide(), square)];
        else if (piece.getPieceType() == PieceType.QUEEN)
            return QUEEN_PST[getIndex(piece.getPieceSide(), square)];
        else if (piece.getPieceType() == PieceType.KING)
            return KING_END_PST[getIndex(piece.getPieceSide(), square)];
        else
            return 0;
    }
}
