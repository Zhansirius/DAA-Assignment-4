import java.util.*;

/**
 * Missions 3 and 4. Find a word in a long text.
 */
public class Marker {

    /**
     * Mission 3a. The prefix function of p. pi[i] = the length of the longest proper prefix of p[0..i] that is
     * also a suffix of p[0..i] ("proper" means shorter than p[0..i] itself).
     * Example: p = "ACGAC" gives [0, 0, 0, 1, 2]. The last number is the "Border length" line.
     */
    public static int[] prefixFunction(String p) {
        // TODO Mission 3a
        int[] pi = new int[p.length()];
        for (int i = 1; i < p.length(); i++) {
            int j = pi[i - 1];
            while (j > 0 && p.charAt(i) != p.charAt(j)) {
                j = pi[j - 1];
            }
            if (p.charAt(i) == p.charAt(j)) {
                j++;
            }
            pi[i] = j;
        }
        return pi;
    }

    /**
     * Mission 3b. KMP. All start positions (counting from 0, in increasing order) where p occurs in text.
     * Overlapping occurrences count: "ACG" occurs in "ACGACG" at 0 and 3, and "AA" in "AAA" at 0 and 1.
     * Use the prefix function: the text pointer never goes back. The number of positions is "Marker hits",
     * the first one is "First hit" (-1 when there is none).
     */
    public static int[] findAll(String text, String p) {
        // TODO Mission 3b
        if (p.isEmpty()) return new int[0];
        int[] pi = prefixFunction(p);
        List<Integer> result = new ArrayList<>();
        int j = 0;
        for (int i = 0; i < text.length(); i++) {
            while (j > 0 && text.charAt(i) != p.charAt(j)) {
                j = pi[j - 1];
            }
            if (text.charAt(i) == p.charAt(j)) {
                j++;
            }
            if (j == p.length()) {
                result.add(i - p.length() + 1);
                j = pi[j - 1];
            }
        }
        int[] hits = new int[result.size()];
        for (int i = 0; i < result.size(); i++) hits[i] = result.get(i);
        return hits;
    }

    /**
     * Mission 4. Rabin-Karp. The number of positions i in a such that the word of k letters starting at i
     * (a[i..i+k-1]) also occurs somewhere in b. Count positions, not different words. This is the "Shared words" line.
     * The idea: give every window of k letters a number (a rolling hash, updated in O(1) when the window moves by one),
     * look the number up among the windows of b, and when it is found COMPARE THE LETTERS, because two different words
     * can have the same hash.
     * Example: a = "ACGTAC", b = "TTACGT", k = 4: the words of a are ACGT, CGTA, GTAC; only ACGT is in b, so the answer is 1.
     */
    public static int sharedWords(String a, String b, int k) {
        // TODO Mission 4
        if (a.length() < k || b.length() < k) return 0;
        long mod = 1000000007;
        long base = 131;
        long top = 1;
        for (int i = 1; i < k; i++) top = (top * base) % mod;

        Map<Long, List<Integer>> bHashes = new HashMap<>();
        long hb = 0;
        for (int i = 0; i < k; i++) hb = (hb * base + b.charAt(i)) % mod;
        bHashes.computeIfAbsent(hb, x -> new ArrayList<>()).add(0);

        for (int i = 1; i <= b.length() - k; i++) {
            hb = (hb - (b.charAt(i - 1) * top) % mod + mod) % mod;
            hb = (hb * base + b.charAt(i + k - 1)) % mod;
            bHashes.computeIfAbsent(hb, x -> new ArrayList<>()).add(i);
        }

        long ha = 0;
        for (int i = 0; i < k; i++) ha = (ha * base + a.charAt(i)) % mod;
        int count = 0;

        if (bHashes.containsKey(ha)) {
            for (int idx : bHashes.get(ha)) {
                if (a.substring(0, k).equals(b.substring(idx, idx + k))) {
                    count++;
                    break;
                }
            }
        }

        for (int i = 1; i <= a.length() - k; i++) {
            ha = (ha - (a.charAt(i - 1) * top) % mod + mod) % mod;
            ha = (ha * base + a.charAt(i + k - 1)) % mod;
            if (bHashes.containsKey(ha)) {
                for (int idx : bHashes.get(ha)) {
                    if (a.substring(i, i + k).equals(b.substring(idx, idx + k))) {
                        count++;
                        break;
                    }
                }
            }
        }
        return count;
    }
}

