import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.move.Move;

import java.util.*;

public class UCI {

    static Board board = new Board();

    static String ENGINE_NAME = "BCEv2";
    static String AUTHOR_NAME = "Victor Mira";
    static Random random = new Random();

    public static void startUCI() {
        Scanner input = new Scanner(System.in);

        while (true) {

            String inputString = input.nextLine();

            if("uci".equals(inputString)) {
                inputUCI();
            }
            else if (inputString.startsWith("setoption")) {

            }
            else if ("isready".equals(inputString)) {
                inputIsReady();
            }
            else if ("ucinewgame".equals(inputString)) {

            }
            else if (inputString.startsWith("position")) {
                inputPosition(inputString);
            }
            else if (inputString.contains("go")) {
                try {
                    inputGo();
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("err");
                    e.printStackTrace();
                }
            }
            else if ("print".equals(inputString)) {
                inputPrint();
            }
            else if ("quit".equals(inputString)) {
                inputQuit();
            }
            else if ("evaluate".equals(inputString)) {
                inputEvaluate();
            }
            else if ("bestscore".equals(inputString)) {
                inputBestScore();
            }
        }
    }

    private static void inputUCI() {
        System.out.println("id name " + ENGINE_NAME);
        System.out.println("id author " + AUTHOR_NAME);
        System.out.println("uciok");
    }

    private static void inputIsReady() {
        System.out.println("readyok");
    }

    private static void inputPosition(String input) {
        input = input.substring(9).concat(" ");
        if (input.contains("startpos ")) {
            input = input.substring(9);
            // Loading starting position from fen
            board.loadFromFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        } else if (input.contains("fen")) {

            input = input.substring(4);
            board.loadFromFen(input);
        }
        if (input.contains("moves")) {
            input = input.substring(input.indexOf("moves") + 6);
            // make moves
            String[] moves = input.split(" ");
            for (String moveString: moves) {
                Move move = new Move(moveString, board.getSideToMove());
                if (board.isMoveLegal(move, true)) {
                    board.doMove(move);
                }
            }

        }
    }

    private static void inputGo() {
        // Search Best solution
        // Random moves for now
        List<Move> possiblesMoves = board.legalMoves();
        Side playerSide = board.getSideToMove();
        Collections.sort(possiblesMoves, Comparator.comparingInt(move -> Evaluation.evaluateMove(board, move, playerSide)));
        // int rndIndex = (int) Math.floor(Math.random() * (possiblesMoves.size()));
/*        int rndIndex = random.nextInt(possiblesMoves.size() - 1);
        for (int i = 0; i < possiblesMoves.size(); i++) {
            if (rndIndex == i || rndIndex == possiblesMoves.size()) {
                System.out.println("bestmove " + possiblesMoves.get(i).toString());
                board.doMove(possiblesMoves.get(i));
            }
        }*/

        Board newBoard;
        long bestScore = Long.MIN_VALUE;
        SearchAlgorithm.bestMove = null;

        System.out.println("best score: " + SearchAlgorithm.minmax(board, true, 0, playerSide));

        /*for (Move move: board.legalMoves()) {
            newBoard = board.clone();
            newBoard.doMove(move);
*//*            long score = PrincipalVariation.pvs(Integer.MIN_VALUE, Integer.MAX_VALUE, newBoard, 1);*//*
            long score = PrincipalVariation.minmax(newBoard, true, 0, playerSide);
            System.out.println("move : " + move.toString() + " score : " + score);
            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }*/

        System.out.println("bestmove " + SearchAlgorithm.bestMove.toString());
        board.doMove(SearchAlgorithm.bestMove);
    }



    private static void inputPrint() {
        // print board
        System.out.println(board.toString());
    }

    private static void inputQuit() {
        System.exit(0);
    }

    private static void inputEvaluate() {
        System.out.println(Evaluation.evaluate(board, Side.WHITE));
    }

    private static void inputBestScore() {
/*        long bestScore = PrincipalVariation.pvs(Integer.MIN_VALUE, Integer.MAX_VALUE, board, 1);
        System.out.println(bestScore);*/
    }





}
