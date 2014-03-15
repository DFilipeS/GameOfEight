/**
 * Jogo dos Oito
 * Daniel Filipe - 201106486
 * Tiago Duarte - 201105251
 **/

import java.util.LinkedList;
import java.util.List;

public class SolFinder {
    static Board configFinal = new Board();

    public static void enqueue(LinkedList<Board> queue, List<Board> possiblePlays, String type) {
        if ("BP".equals(type)) {
            enqueueBP(queue, possiblePlays);
        } else if ("BL".equals(type)) {
            enqueueBL(queue, possiblePlays);
        } else if ("BPI".equals(type)) {
            enqueueBPI(queue, possiblePlays);
        } else if ("ASTAR".equals(type)) {
            enqueueASTAR(queue, possiblePlays);
        } else if ("GULOSA".equals(type)) {
            enqueueGULOSA(queue, possiblePlays);
        }
    }

    public static void enqueueBP(LinkedList<Board> queue, List<Board> possiblePlays) {
        for (Board item : possiblePlays)
            if (!checkRepeatedInPath(item))
                queue.addFirst(item);
    }

    public static void enqueueBL(LinkedList<Board> queue, List<Board> possiblePlays) {
        for (Board item : possiblePlays)
            if (!checkRepeatedInPath(item))
                queue.addLast(item);
    }

    public static void enqueueBPI(LinkedList<Board> queue, List<Board> possiblePlays) {
        for (Board item : possiblePlays)
            if (!checkRepeatedInPath(item))
                queue.addFirst(item);
    }

    public static void enqueueASTAR(LinkedList<Board> queue, List<Board> possiblePlays) {
        for (Board item : possiblePlays) {
            int heuristica = 0;

            for (int row = 0; row < 3; row++) {
                for (int column = 0; column < 3; column++)
                    heuristica = heuristica + getDistanceToFinal(item, row, column);
            }

            item.cost = sumOfLevels(item.nodeLevel) + heuristica;
            sortQueue(queue, item);
        }
    }

    public static void enqueueGULOSA(LinkedList<Board> queue, List<Board> possiblePlays) {
        for (Board item : possiblePlays) {
            if (checkRepeatedInPath(item)) continue;

            int heuristica = 0;

            for (int row = 0; row < 3; row++) {
                for (int column = 0; column < 3; column++)
                    heuristica = heuristica + getDistanceToFinal(item, row, column);
            }

            item.cost = heuristica;
            sortQueue(queue, item);
        }
    }

    private static void sortQueue(LinkedList<Board> queue, Board obj) {
        for (int i = 0; i < queue.size(); i++) {
            if (obj.cost <= queue.get(i).cost) {
                queue.add(i, obj);
                return;
            }
        }
        queue.add(obj);
    }

    public static int sumOfLevels (int level) {
        int sum = 0;
        for (int i = 0; i <= level; i++)
            sum += i;
        return sum;
    }

    public static boolean checkRepeatedInPath (Board obj) {
        Board parentNode = obj.parent;

        while (parentNode != null) {
            if (obj.equals(parentNode)) return true;
            parentNode = parentNode.parent;
        }

        return false;
    }

    public static int getDistanceToFinal(Board board, int x, int y) {
        int val = board.cells[x][y];

        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                if (configFinal.cells[row][column] == val) {
                    return (Math.abs(row - x) + Math.abs(column - y));
                }
            }
        }

        return 0;
    }
}
