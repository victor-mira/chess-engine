import com.github.bhlangonijr.chesslib.*;
import com.github.bhlangonijr.chesslib.move.Move;

public class Main {
    public static void main(String[] args) {
        UCI.startUCI();

        Board board = new Board();
        board.doMove("e2e4");
        System.out.println(board.toString());
    }

}
