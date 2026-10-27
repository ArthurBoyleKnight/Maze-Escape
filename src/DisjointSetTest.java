/**
 * Checks of Mission 0. Run:  java -cp out DisjointSetTest
 * Add your own checks below: a large chain, a union of a set with itself, count after many unions.
 */
public class DisjointSetTest {

    static int failed = 0;

    static void check(boolean ok, String what) {
        System.out.println((ok ? "ok    " : "FAIL  ") + what);
        if (!ok) failed++;
    }

    public static void main(String[] args) {
        DisjointSet d = new DisjointSet(6);
        check(d.count() == 6, "6 sets at the start");
        check(!d.connected(0, 1), "0 and 1 are apart at the start");
        check(d.union(0, 1), "union(0, 1) joins two sets");
        check(d.connected(0, 1), "0 and 1 are together");
        check(!d.union(1, 0), "union(1, 0) again returns false");
        check(d.count() == 5, "5 sets after one union");
        d.union(2, 3); d.union(1, 3);
        check(d.connected(0, 2), "0 and 2 are together through 1 and 3");
        check(d.count() == 3, "3 sets: {0,1,2,3} {4} {5}");
        check(d.find(4) == 4 && d.find(5) == 5, "a single cell is its own root");

                DisjointSet self = new DisjointSet(5);

        check(!self.union(2, 2), "union with itself returns false");
        check(self.count() == 5, "count unchanged after union with itself");
        check(self.connected(2, 2), "element is connected to itself");

        int size = 1000;
        DisjointSet chain = new DisjointSet(size);

        for (int i = 0; i < size - 1; i++) {
            chain.union(i, i + 1);
        }

        check(chain.count() == 1, "1000 elements form one set");
        check(chain.connected(0, size - 1), "first and last elements are connected");
        check(!chain.union(0, size - 1), "repeated union returns false");
        check(chain.count() == 1, "count stays 1 after repeated union");

        DisjointSet groups = new DisjointSet(100);

        for (int i = 0; i < 50; i++) {
            groups.union(i, i + 1);
        }

        check(groups.count() == 50, "count is correct after 50 unions");
        check(groups.connected(0, 50), "elements 0 to 50 are connected");
        check(!groups.connected(0, 51), "element 51 remains separate");

        boolean invalidFind = false;
        try {
            groups.find(-1);
        } catch (IndexOutOfBoundsException e) {
            invalidFind = true;
        }

        check(invalidFind, "find(-1) throws an exception");

        boolean invalidUnion = false;
        try {
            groups.union(0, 100);
        } catch (IndexOutOfBoundsException e) {
            invalidUnion = true;
        }

        check(invalidUnion, "union with an invalid index throws an exception");

        System.out.println(failed == 0 ? "All checks passed." : failed + " checks failed.");
    }
}
