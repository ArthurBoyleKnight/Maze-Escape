/**
 * Missions 1 and 2. Building the maze out of the walls.
 * A wall k joins the cells w.a()[k] and w.b()[k] and costs w.cost()[k]. Indexes start from 0,
 * the line number in walls.txt is the index + 1. The cells are connected by DisjointSet.
 */
public class Maze {

    /**
     * Mission 1. Take the walls in the order of the file. Break a wall when its two cells are in different sets.
     * Returns the indexes of the broken walls, in the order of breaking.
     */
public static int[] breakWalls(Input.Walls w) {
    int[] a = w.a();
    int[] b = w.b();

    int cells = 0;
    for (int i = 0; i < a.length; i++) {
        cells = Math.max(cells, Math.max(a[i], b[i]) + 1);
    }

    DisjointSet ds = new DisjointSet(cells);
    int[] broken = new int[a.length];
    int count = 0;

    for (int i = 0; i < a.length; i++) {
        if (ds.union(a[i], b[i])) {
            broken[count] = i;
            count++;
        }
    }

    return java.util.Arrays.copyOf(broken, count);
}

    /**
     * Mission 2. Kruskal: the same, but take the walls from the cheapest to the most expensive.
     * Returns the indexes of the broken walls.
     */
public static int[] cheapestMaze(Input.Walls w) {
    int[] a = w.a();
    int[] b = w.b();
    int[] costs = w.cost();

    int cells = 0;
    for (int i = 0; i < a.length; i++) {
        cells = Math.max(cells, Math.max(a[i], b[i]) + 1);
    }

    Integer[] order = new Integer[a.length];
    for (int i = 0; i < order.length; i++) {
        order[i] = i;
    }

    java.util.Arrays.sort(order,
            (i, j) -> Integer.compare(costs[i], costs[j]));

    DisjointSet ds = new DisjointSet(cells);
    int[] result = new int[Math.max(0, cells - 1)];
    int count = 0;

    for (int index : order) {
        if (ds.union(a[index], b[index])) {
            result[count++] = index;
            if (count == cells - 1) {
                break;
            }
        }
    }

    return java.util.Arrays.copyOf(result, count);
}

    /**
     * Mission 2. Prim with a priority queue. Returns the total cost of the cheapest maze.
     * It must be equal to the total cost of cheapestMaze.
     */
public static long primCost(Input.Walls w) {
    int[] a = w.a();
    int[] b = w.b();
    int[] costs = w.cost();

    int cells = 0;
    for (int i = 0; i < a.length; i++) {
        cells = Math.max(cells, Math.max(a[i], b[i]) + 1);
    }

    if (cells <= 1) {
        return 0;
    }

    java.util.List<int[]>[] graph = new java.util.ArrayList[cells];

    for (int i = 0; i < cells; i++) {
        graph[i] = new java.util.ArrayList<>();
    }

    for (int i = 0; i < a.length; i++) {
        graph[a[i]].add(new int[]{b[i], costs[i]});
        graph[b[i]].add(new int[]{a[i], costs[i]});
    }

    boolean[] visited = new boolean[cells];
    java.util.PriorityQueue<int[]> pq =
            new java.util.PriorityQueue<>(
                    java.util.Comparator.comparingInt(edge -> edge[1]));

    pq.add(new int[]{0, 0});
    long totalCost = 0;
    int visitedCount = 0;

    while (!pq.isEmpty() && visitedCount < cells) {
        int[] edge = pq.poll();
        int cell = edge[0];
        int cost = edge[1];

        if (visited[cell]) {
            continue;
        }

        visited[cell] = true;
        visitedCount++;
        totalCost += cost;

        for (int[] next : graph[cell]) {
            if (!visited[next[0]]) {
                pq.add(new int[]{next[0], next[1]});
            }
        }
    }

    if (visitedCount != cells) {
        throw new IllegalArgumentException("Maze graph is disconnected");
    }

    return totalCost;
}
}
