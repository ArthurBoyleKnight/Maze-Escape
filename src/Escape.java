/**
 * Mission 3. Escape: DFS from cell 0 to the last cell (rows * cols - 1) in the maze of Mission 1.
 * Two neighbouring cells are connected when the wall between them is broken.
 * The neighbours are tried in this order: up, right, down, left (cell - cols, cell + 1, cell + cols, cell - 1).
 * The ready method Graphs.open(w, broken) gives you the open neighbours of every cell, in no particular order:
 * to visit them in the required order, go through the four cells above one by one and take those that are in the list.
 * The program must not overflow the stack on a maze of 1000 by 1000 cells.
 */
public class Escape {

    /**
     * path: the cells of the way out, from 0 to the last cell.
     * visited: how many cells are marked as visited at the moment the exit is marked (the start and the exit are counted).
     * deadEnds: how many cells of the maze have exactly one open neighbour.
     */
    public record Result(int[] path, int visited, int deadEnds) {}


public static Result escape(Input.Walls w, int[] broken) {
    int[][] open = Graphs.open(w, broken);
    int rows = w.rows();
    int cols = w.cols();
    int n = rows * cols;
    int exit = n - 1;

    boolean[] visited = new boolean[n];
    int[] parent = new int[n];
    int[] stack = new int[n];

    java.util.Arrays.fill(parent, -1);

    int top = 0;
    int visitedCount = 0;
    int[] path = new int[n];

    stack[top++] = 0;
    visited[0] = true;
    visitedCount++;

    while (top > 0 && !visited[exit]) {
        int cell = stack[top - 1];
        int[] neighbours = {
            cell - cols,
            cell + 1,
            cell + cols,
            cell - 1
        };

        boolean moved = false;

        for (int next : neighbours) {
            if (next < 0 || next >= n) {
                continue;
            }

            if ((next == cell + 1 && cell / cols != next / cols)
                    || (next == cell - 1 && cell / cols != next / cols)) {
                continue;
            }

            boolean isOpen = false;
            for (int neighbour : open[cell]) {
                if (neighbour == next) {
                    isOpen = true;
                    break;
                }
            }

            if (isOpen && !visited[next]) {
                visited[next] = true;
                visitedCount++;
                parent[next] = cell;
                stack[top++] = next;
                moved = true;
                break;
            }
        }

        if (!moved) {
            top--;
        }
    }

    int length = 0;
    for (int cell = exit; cell != -1; cell = parent[cell]) {
        path[length++] = cell;
    }

    int[] resultPath = new int[length];
    for (int i = 0; i < length; i++) {
        resultPath[i] = path[length - 1 - i];
    }

    int deadEnds = 0;
    for (int cell = 0; cell < n; cell++) {
        if (open[cell].length == 1) {
            deadEnds++;
        }
    }

    return new Result(resultPath, visitedCount, deadEnds);
}

}
