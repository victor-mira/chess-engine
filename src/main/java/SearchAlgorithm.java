import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.move.Move;

import java.util.*;

public class SearchAlgorithm {
    public static Move bestMove;
    /*public static long pvs(int alpha, int beta, Board board, int depth) {
        long bestScore;
        int bestMoveIndex = -1;

        if (depth == BCE.depth) {
            bestScore = LazyEvaluation.evaluate(board);
            return bestScore;
        }

        List<Move> moves = board.legalMoves();

        if (moves.isEmpty()) {
            // TODO redo
            return board.isMated() ? LazyEvaluation.MATE_VALUE : -BCE.MATE_SCORE;
        }

        Collections.sort(moves, Comparator.comparingInt(move -> LazyEvaluation.evaluateMove(board, move)));

        Board newBoard = board.clone();
        newBoard.doMove(moves.get(0));

        bestScore = -pvs(-beta, -alpha, newBoard, depth+1);

        if (Math.abs(bestScore) == LazyEvaluation.MATE_VALUE) {
            return bestScore;
        }

        if (bestScore > alpha) {
            if (bestScore >= beta) {
                return bestScore;
            }
            alpha = (int) bestScore;
        }

        bestMoveIndex = 0;

        for (int i = 1; i < moves.size(); i++) {
            long score;
            newBoard = board.clone();
            newBoard.doMove(moves.get(i));
            score = -zWsearch(-alpha, board, depth + 1);
            if (score > alpha && score < beta) {
                score = -pvs(-beta, -alpha, newBoard, depth+1);
                if (score > alpha) {
                    bestMoveIndex = i;
                    alpha = (int) score;
                }
            }
            if (score > bestScore) {
                if (score >= beta) {

                    return score;
                }
                bestScore = score;
                if (Math.abs(bestScore) == LazyEvaluation.MATE_VALUE) {
                    return bestScore;
                }
            }
        }
        return bestScore;
    }
    public static long zWsearch(int beta, Board board, int depth) {
        long score;
        if (depth == BCE.depth) {
            score = LazyEvaluation.evaluate(board);
            return score;
        }

        List<Move> moves = board.legalMoves();

        Collections.sort(moves, Comparator.comparingInt(move -> LazyEvaluation.evaluateMove(board, move)));
        Board newBoard;

        for (int i = 0; i < moves.size(); i++) {
            newBoard = board.clone();
            newBoard.doMove(moves.get(i));

            score = -zWsearch(1 - beta, newBoard, depth + 1);
            if (score > beta) {
                return score;
            }

        }

        return beta - 1;
    }*/
/*    private static List<Move> sortMoves(Board board, List<Move> moves) {
        Map<Move, Long> moveScoreMap = new LinkedHashMap<>();
        for (Move move: moves) {
            moveScoreMap.put(move, LazyEvaluation.evaluateMove(board, move));
        }

        moves.sort(Comparator.comparing((board) -> {LazyEvaluation.evaluateMove(board, )});
    }*/
    public static int alphaBeta(int alpha, int beta, Board board, boolean maximazingPlayer, int depth, Side playerSide) {
        if (depth == BCE.depth) {
            System.out.println("max : " + maximazingPlayer + " depth : " + depth + " eval : " + Evaluation.evaluate(board, playerSide));
            System.out.println(board.toString());
            return (int) Evaluation.evaluate(board, playerSide);
        }
        if (maximazingPlayer) {
            int best = Integer.MIN_VALUE;
            List<Move> moves = board.legalMoves();

            for (Move move :
                    moves) {
                Board newBoard = board.clone();
                newBoard.doMove(move);
                int val = alphaBeta(alpha, beta, newBoard, false, depth+1, playerSide);
                if (val >= best) {
                    best = val;
                    if (depth == 0)
                    bestMove = move;
                }
                alpha = Math.max(alpha, val);
                if (beta <= alpha) {
                    break;
                }
            }
            System.out.println("max : " + maximazingPlayer + " depth : " + depth + " eval : " + best);
            return best;
        }
        else {
            int best = Integer.MAX_VALUE;

            List<Move> moves = board.legalMoves();

            for (Move move :
                    moves) {
                Board newBoard = board.clone();
                newBoard.doMove(move);
                int val = alphaBeta(alpha, beta, newBoard, true, depth+1, playerSide);
                if (val < best) {
                    best = val;
                }
                beta = Math.min(beta, val);
                if (beta <= alpha)
                    break;
            }
            System.out.println("max : " + maximazingPlayer + " depth : " + depth + " eval : " + best);
            return best;
        }
    }

    public static int minmax(Board board, boolean maximazingPlayer, int depth, Side playerSide) {
        if (depth == BCE.depth) {
            System.out.println("max : " + maximazingPlayer + " depth : " + depth + " eval : " + Evaluation.evaluate(board, playerSide));
            System.out.println(board.toString());
            return (int) Evaluation.evaluate(board, playerSide);
        }
        if (maximazingPlayer) {
            int best = Integer.MIN_VALUE;
            List<Move> moves = board.legalMoves();

            for (Move move :
                    moves) {
                Board newBoard = board.clone();
                newBoard.doMove(move);
                int val = minmax(newBoard, false, depth+1, playerSide);
                if (val >= best) {
                    best = val;
                    if (depth == 0)
                    bestMove = move;
                }
            }
            System.out.println("max : " + maximazingPlayer + " depth : " + depth + " eval : " + best);
            return best;
        }
        else {
            int best = Integer.MAX_VALUE;

            List<Move> moves = board.legalMoves();

            for (Move move :
                    moves) {
                Board newBoard = board.clone();
                newBoard.doMove(move);
                int val = minmax(newBoard, true, depth+1, playerSide);
                if (val < best) {
                    best = val;
                }
            }
            System.out.println("max : " + maximazingPlayer + " depth : " + depth + " eval : " + best);
            return best;
        }
    }

//    public static int zeroWindow(int beta, Board board, boolean maximazingPlayer, int depth) {
//        int score;
//        if (depth == BCE.depth) {
//            score = (int) LazyEvaluation.evaluate(board);
//            return score;
//        }
//
//        int best = Integer.MIN_VALUE;
//        List<Move> moves = board.legalMoves();
//
//        if (maximazingPlayer) {
//            for (Move move :
//                    moves) {
//                Board newBoard = board.clone();
//                newBoard.doMove(move);
//                int val = zeroWindow(beta, newBoard, false, depth+1);
//                best = Math.max(best, val);
//                beta = 1 - Math.max(1-beta, best);
//
//                if (beta <= alpha) {
//
//                    return best;
//                }
//            }
//            return best;
//        }
//
//    }
}
