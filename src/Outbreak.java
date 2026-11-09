import java.util.*;

/**
 * Mission 5. Dynamic programming on the tree of infections. Root the tree in person 0.
 * Input.Tree.neighbours() gives the neighbours of every person. The number of people is at most about 100, so a recursive DFS is fine here.
 */
public class Outbreak {

    /**
     * Choose people to test so that for every link (somebody infected somebody) at least one of the two is tested,
     * and the total cost of testing is as small as possible. cost[v] is the price of testing person v.
     * Returns tested[v] = true if person v is tested.
     *
     * State for every person v (looking only at v and everything below it): the smallest cost when v IS tested, and
     * the smallest cost when v is NOT tested. What must then be true about the children of v in each case?
     * Do not forget to rebuild the answer (who exactly is tested), not only the cost: the picture needs it.
     * Main calls this twice: with all costs equal to 1 (the line "Tests needed") and with the real costs ("Cheapest testing").
     */
    public static boolean[] cheapestTests(int n, int[][] nb, int[] cost) {
        // TODO Mission 5a
        int[] take = new int[n];
        int[] skip = new int[n];
        int[] parent = new int[n];
        boolean[] tested = new boolean[n];
        Arrays.fill(parent, -1);

        List<Integer> order = new ArrayList<>();
        order.add(0);
        int head = 0;
        while (head < order.size()) {
            int u = order.get(head++);
            for (int v : nb[u]) {
                if (v != parent[u]) {
                    parent[v] = u;
                    order.add(v);
                }
            }
        }

        for (int i = n - 1; i >= 0; i--) {
            int u = order.get(i);
            take[u] = cost[u];
            skip[u] = 0;
            for (int v : nb[u]) {
                if (v != parent[u]) {
                    take[u] += Math.min(take[v], skip[v]);
                    skip[u] += take[v];
                }
            }
        }

        tested[0] = take[0] <= skip[0];
        for (int i = 1; i < n; i++) {
            int u = order.get(i);
            int p = parent[u];
            if (!tested[p]) {
                tested[u] = true;
            } else {
                tested[u] = take[u] <= skip[u];
            }
        }
        return tested;
    }

    /**
     * The number of links on the longest chain of infections, that is the longest path in the tree (counted in links).
     * A path goes up from one person and down to another; for every person you need the two longest ways down.
     * Example: a line 0-1-2-3 has chain 3; a star with centre 0 and leaves 1, 2, 3 has chain 2.
     */
    public static int longestChain(int n, int[][] nb) {
        // TODO Mission 5b
        int[] top1 = new int[n];
        int[] top2 = new int[n];
        int[] parent = new int[n];
        Arrays.fill(parent, -1);

        List<Integer> order = new ArrayList<>();
        order.add(0);
        int head = 0;
        while (head < order.size()) {
            int u = order.get(head++);
            for (int v : nb[u]) {
                if (v != parent[u]) {
                    parent[v] = u;
                    order.add(v);
                }
            }
        }

        int chain = 0;
        for (int i = n - 1; i >= 0; i--) {
            int u = order.get(i);
            for (int v : nb[u]) {
                if (v != parent[u]) {
                    int h = top1[v] + 1;
                    if (h > top1[u]) {
                        top2[u] = top1[u];
                        top1[u] = h;
                    } else if (h > top2[u]) {
                        top2[u] = h;
                    }
                }
            }
            chain = Math.max(chain, top1[u] + top2[u]);
        }
        return chain;
    }
}