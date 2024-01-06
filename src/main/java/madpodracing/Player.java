package madpodracing;

import java.util.*;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class Player {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);

        boolean boost = true;
        CyrcalList checkPoints = new CyrcalList();
        String thrust = "0";
        // game loop
        while (true) {
            int x = in.nextInt();
            int y = in.nextInt();
            int nextCheckpointX = in.nextInt(); // x position of the next check point
            int nextCheckpointY = in.nextInt(); // y position of the next check point
            int nextCheckpointDist = in.nextInt(); // distance to the next checkpoint
            int nextCheckpointAngle = in.nextInt(); // angle between your pod orientation and the direction of the next checkpoint
            int opponentX = in.nextInt();
            int opponentY = in.nextInt();

            // Constance
            int slowDistance = 2000;
            int nextCheckpointRadius = 300;
            int minSpeed = 10;

            // Counted
            int opponentDist = getDistance(opponentX, opponentY, x, y);

            // Local
            int pointX = nextCheckpointX;
            int pointY = nextCheckpointY;

//            System.err.println("x: " + x);
//            System.err.println("y: " + y);
//            System.err.println("nextCheckpointX: " + nextCheckpointX);
//            System.err.println("nextCheckpointY: " + nextCheckpointY);
//            System.err.println("nextCheckpointAngle: " + nextCheckpointAngle);
//            System.err.println("absNextCheckpointAngle: " + Math.abs(nextCheckpointAngle));
//            System.err.println("nextCheckpointDist: " + nextCheckpointDist);
//            System.err.println("opponentX: " + opponentX);
//            System.err.println("opponentY: " + opponentY);
//            System.err.println("opponentDist: " + opponentDist);

            var localHead = checkPoints.head;
            if (localHead != null) {
                do {
                    System.err.println("checkpoint: " + localHead.checkPoint.getX() + " " + localHead.checkPoint.getY());
                    localHead = localHead.next;
                } while (localHead != checkPoints.head);
            }

            int absNextCheckpointAngle = Math.abs(nextCheckpointAngle);
            if (absNextCheckpointAngle >= 80 && nextCheckpointDist > 600) {
                double sinThrust = Math.sin(Math.toRadians(absNextCheckpointAngle)) * 100;
                System.err.println("SIN thrust: " + sinThrust);
                thrust = Integer.toString(Math.max((int) sinThrust - 40, 30));
                //thrust = Integer.toString((int) sinThrust);
            } else if (absNextCheckpointAngle < 80 && absNextCheckpointAngle > 20 && nextCheckpointDist > 600) {
                double cosThrust = Math.cos(Math.toRadians(absNextCheckpointAngle)) * 100;
                System.err.println("COS thrust: " + cosThrust);
                thrust = Integer.toString(Math.max((int) cosThrust - 40, 30));
                //thrust = Integer.toString((int) cosThrust);
            } else {
                if (opponentDist > slowDistance * 2 && nextCheckpointDist > slowDistance * 3 && boost) {
                    thrust = "BOOST";
                    boost = false;
                } else if (opponentDist <= 1000 && thrust.equals("100") && nextCheckpointDist > slowDistance ) {
                    pointX = opponentX;
                    pointY = opponentY;
                    thrust = "100";
                } else {
                    CheckPoint nextNextCheckPoint = checkPoints.findNextCheckPoint(new CheckPoint(nextCheckpointX, nextCheckpointY));
                    if (nextNextCheckPoint != null) {
                        if (nextCheckpointDist <= slowDistance) {
                            pointX = nextNextCheckPoint.getX();
                            pointY = nextNextCheckPoint.getY();
                            System.err.println("GO to next next point");
                            var nextNextCheckpointDist = getDistance(nextNextCheckPoint.getX(), x, nextNextCheckPoint.getY(), y);
                            thrust = Integer.toString(Math.max(100 * Math.min(nextNextCheckpointDist - nextCheckpointRadius, slowDistance) / slowDistance, minSpeed));
                        } else {
                            thrust = Integer.toString(Math.max(100 * Math.min(nextCheckpointDist - nextCheckpointRadius, slowDistance) / slowDistance, minSpeed));
                        }
                    } else {
                        if (!checkPoints.contains(new CheckPoint(nextCheckpointX, nextCheckpointY))) {
                            System.err.println("add checkpoint: " + nextCheckpointX + " " + nextCheckpointY);
                            checkPoints.add(new CheckPoint(nextCheckpointX, nextCheckpointY));
                        }
                        thrust = Integer.toString(Math.max(100 * Math.min(nextCheckpointDist - nextCheckpointRadius, slowDistance) / slowDistance, minSpeed));
                    }
                }
            }
            

            System.out.println(pointX + " " + pointY + " " + thrust);
        }
    }

    public static int getDistance(int x1, int y1, int x2, int y2) {
        return (int) Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
}

class CheckPoint {
    private final int x;
    private final int y;

    CheckPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CheckPoint that = (CheckPoint) o;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}

class CyrcalList {

    Node head;
    Node tail;

    private Node find(final CheckPoint checkPoint) {
        Node currentNode = head;

        if (head == null) {
            return null;
        } else {
            do {
                if (currentNode.checkPoint.equals(checkPoint)) {
                    return currentNode;
                }
                currentNode = currentNode.next;
            } while (currentNode != head);
            return null;
        }
    }

    public CheckPoint findNextCheckPoint(final CheckPoint checkPoint) {
        Node node = find(checkPoint);
        return node != null && node.next != null ? node.next.checkPoint : null;
    }

    public boolean contains(final CheckPoint checkPoint) {
        return find(checkPoint) != null;
    }

    public void add(final CheckPoint checkPoint) {
        Node newNode = new Node(checkPoint);

        if (head == null) {
            head = newNode;
        } else {
            tail.next = newNode;
        }

        tail = newNode;
        tail.next = head;
    }

    // static inner class
    static class Node {
        CheckPoint checkPoint;

        // connect each node to next node
        Node next;

        Node(CheckPoint checkPoint) {
            this.checkPoint = checkPoint;
            next = null;
        }
    }
}