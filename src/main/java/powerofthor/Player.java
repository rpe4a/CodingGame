import java.awt.Point;
import java.util.Scanner;

enum Moved {
    NONE,
    N, NE,
    E, SE,
    S, SW,
    W, NW
}

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 * ---
 * Hint: You can use the debug stream to print initialTX and initialTY, if Thor seems not follow your orders.
 **/
class Player {

    public static final Point TOP_LEFT_POINT = new Point(0, 0);
    public static final Point BOTTOM_RIGHT_POINT = new Point(39, 17);

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int lightX = in.nextInt(); // the X position of the light of power
        int lightY = in.nextInt(); // the Y position of the light of power
        int initialTx = in.nextInt(); // Thor's starting X position
        int initialTy = in.nextInt(); // Thor's starting Y position
        Moved move = Moved.NONE;
        int thorTx = initialTx;
        int thorTy = initialTy;

        // game loop
        while (true) {
            int remainingTurns = in.nextInt(); // The remaining amount of turns Thor can move. Do not remove this line.

            if (move != Moved.NONE) {
                var movePoint = getMovePoint(move);
                thorTx = thorTx + movePoint.x;
                thorTy = thorTy - movePoint.y;
            }

            System.err.println("thorTx: " + thorTx);
            System.err.println("thorTy: " + thorTy);
            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");

            int movedX = thorTx - lightX;
            int movedY = thorTy - lightY;

            System.err.println("movedX: " + movedX);
            System.err.println("movedY: " + movedY);

            if (movedX > 0) {
                if (movedY > 0) {
                    move = Moved.NW;
                } else if (movedY == 0) {
                    move = Moved.W;
                } else {
                    move = Moved.SW;
                }
            } else if (movedX == 0) {
                if (movedY > 0) {
                    move = Moved.N;
                } else {
                    move = Moved.S;
                }
            } else {
                if (movedY > 0) {
                    move = Moved.SE;
                } else if (movedY == 0) {
                    move = Moved.E;
                } else {
                    move = Moved.NE;
                }
            }

            // A single line providing the move to be made: N NE E SE S SW W or NW
            System.out.println(move);
        }
    }

    private static Point getMovePoint(final Moved move) {
        Point point = null;
        switch (move) {
            case N -> point = new Point(0, -1);
            case NE -> point = new Point(1, -1);
            case E -> point = new Point(1, 0);
            case SE -> point = new Point(1, 1);
            case S -> point = new Point(0, 1);
            case SW -> point = new Point(-1, 1);
            case W -> point = new Point(-1, 0);
            case NW -> point = new Point(-1, -1);
        }

        return point;
    }
}