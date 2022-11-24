import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.move.Move;

import java.util.List;
import java.util.Scanner;

public class UCI {

    static Board board = new Board();

    static String ENGINE_NAME = "BCE";
    static String AUTHOR_NAME = "Victor Mira";

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
                inputGo();
            }
            else if ("print".equals(inputString)) {
                inputPrint();
            }
            else if ("quit".equals(inputString)) {
                inputQuit();
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
        int rndIndex = (int) Math.floor(Math.random() * (possiblesMoves.size() + 1));
        board.doMove(possiblesMoves.get(rndIndex));
        System.out.println("bestmove " + possiblesMoves.get(rndIndex).toString());
    }

    private static void inputPrint() {
        // print board
        System.out.println(board.toString());
    }

    private static void inputQuit() {
        System.exit(0);
    }





}
