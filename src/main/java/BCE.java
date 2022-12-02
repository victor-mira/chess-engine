import com.github.bhlangonijr.chesslib.*;
import com.github.bhlangonijr.chesslib.move.Move;

public class BCE {
    public static int depth = 3;
    public static final int MATE_SCORE = Short.MAX_VALUE/2;
    public static void main(String[] args) {
/*        for (int i = 0; i < 1000; i++) {
            int temp = (int) Math.floor(Math.random() * 36);
            if (temp == 36) {
                System.out.println(temp);
                System.out.println("equal 36...");
                break;
            }
            System.out.println(temp);
        }*/
        UCI.startUCI();
    }

}
