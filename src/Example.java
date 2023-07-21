import java.util.*;

public class Example {
    // we expect that you will return a list of size 2
    static List<Map<String, Integer>> split(Map<String, Integer> set) {
        int s_sum = set.values().stream().mapToInt(a -> a).sum();
        int s_size = set.size();
        int a_size = s_size / 2;
        List<String> keys = set.keySet().stream().toList();

        // d[префикс, количество, сумма]
        //
        //База:
        //d[0, 0, 0] = []
        //d[0, 1, elem[0]] =[elem[0]]
        //
        // необходимые для ответа значения:
        // d[n, size / 2, _]
        //
        // d[i, k, x] -> d[i+1, k, x]
        // d[i, k, x] -> d[i+1, k+1, x + elem[i+1]]
        Object[][][] d = new Object[2][a_size + 3][s_sum + 1];

        d[0][0][0] = new HashMap<String, Integer>();
        d[0][1][set.get(keys.get(0))] = new HashMap<String, Integer>();
        ((HashMap<String, Integer>) d[0][1][set.get(keys.get(0))]).put(keys.get(0), set.get(keys.get(0)));

        for (int i=0; i < s_size - 1; i++) {
            for (int k=0; k <= a_size + 1; k++) {
                for (int x=0; x <= s_sum / 2; x++) {
                    if (d[i % 2][k][x] != null) {
                        String key = keys.get(i + 1);
                        int elem = set.get(key);
                        HashMap<String, Integer> hmap = (HashMap<String, Integer>) d[i % 2][k][x];

                        d[(i + 1) % 2][k][x] = new HashMap<String, Integer>(hmap);
                        d[(i + 1) % 2][k + 1][x + elem] = new HashMap<String, Integer>(hmap);
                        ((HashMap<String, Integer>) d[(i + 1)  % 2][k + 1][x + elem]).put(keys.get(i + 1), elem);
                    }
                }
            }
        }

        // если s_size - нечетный - то надо смотреть как s_size / 2, так и вариант с s_size / 2 + 1, потому что в качестве кандидата на A
        // мы рассматриваем только множества суммы <= s_sum / 2.
        // см. smallTest3
        for (int x = s_sum / 2; x >= 0; x--) {
            if (d[(s_size - 1) % 2][a_size][x] != null) {
                return prepareResult((HashMap<String, Integer>) d[(s_size - 1) % 2][a_size][x], set);
            }

            if (s_size % 2 == 1 && d[(s_size - 1) % 2][a_size + 1][x] != null) {
                return prepareResult((HashMap<String, Integer>) d[(s_size - 1) % 2][a_size + 1][x], set);
            }
        }
        return null;
    }

    private static List<Map<String, Integer>> prepareResult(Map<String, Integer> a_map, Map<String, Integer> set) {
        Set<Map.Entry<String, Integer>> a_set = new HashSet<>(a_map.entrySet());
        Set<Map.Entry<String, Integer>> b_set = new HashSet<>(set.entrySet());
        b_set.removeAll(a_set);
        Map<String, Integer> b_map = new HashMap<>();
        for (Map.Entry<String, Integer> ob: b_set) {
            b_map.put(ob.getKey(), ob.getValue());
        }

        List<Map<String, Integer>> result = (new ArrayList<>());
        result.add(a_map);
        result.add(b_map);
        return result;
    }
}
