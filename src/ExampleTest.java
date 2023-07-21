import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;

import java.util.*;

class ExampleTest {
    //Суммарное время запуска тестов ~40-50сек
    private static boolean sizeChecker(Map<String, Integer>A,
                                            Map<String, Integer> B,
                                            Map<String, Integer> S) {
        return (Math.abs(A.size() - B.size()) == S.size() % 2);
    }

    private static boolean similarityChecker(Map<String, Integer> A,
                                       Map<String, Integer> B,
                                       Map<String, Integer> S) {
        Map<String, Integer> A_B = new HashMap<>();
        A_B.putAll(A);
        A_B.putAll(B);
        return A_B.equals(S);
    }

    private static int resultChecker(Map<String, Integer> set, @Nullable List<Map<String, Integer>> res) {
        assert res != null;
        assert res.size() == 2;
        Map<String, Integer> a = res.get(0);
        Map<String, Integer> b = res.get(1);

        assert(sizeChecker(a, b, set));
        assert(similarityChecker(a, b, set));
        int diff = Math.abs(a.values().stream().mapToInt(x -> x).sum() - b.values().stream().mapToInt(x -> x).sum());
        return diff;
    }

    @Test
    public void basicTest1() {
        Map<String, Integer> set = new HashMap<String, Integer>();
        set.put("a", 3);
        set.put("b", 2);
        set.put("c", 1);
        set.put("d", 1);
        set.put("e", 2);

        List<Map<String, Integer>> res = Example.split(set);

        int diff = resultChecker(set, res);
        assert(diff == 1);
    }

    @Test
    public void basicTest2() {
        Map<String, Integer> set = new HashMap<String, Integer>();
        set.put("a", 3);
        set.put("b", 2);
        set.put("c", 2);
        set.put("d", 1);
        set.put("e", 1);
        set.put("f", 1);

        List<Map<String, Integer>> res = Example.split(set);

        int diff = resultChecker(set, res);
        assert(diff == 0);
    }

    @Test
    public void bigTest() {
        Map<String, Integer> set = new HashMap<String, Integer>();
        int sum_1 = 0;
        int sum_2 = 0;
        for (int ind = 0; ind < 100; ind++) {
            int value = (int) (Math.random() * 10);
            set.put(String.valueOf(ind), value);
            sum_1 += value;
        }
        for (int ind = 100; ind < 200; ind++) {
            int value = (int) (Math.random() * 10);
            set.put(String.valueOf(ind), value);
            sum_2 += value;
        }
        set.put("equalizer", Math.abs(sum_1 - sum_2));

        List<Map<String, Integer>> res = Example.split(set);

        int diff = resultChecker(set, res);
        assert(diff == 0);
    }

    @Test
    public void stressTest() {
        for (int test_num = 0; test_num < 50; test_num++) {
            Map<String, Integer> set = new HashMap<String, Integer>();
            int sum_1 = 0;
            int sum_2 = 0;
            for (int ind = 0; ind < 50; ind++) {
                int value = (int) (Math.random() * 5);
                set.put(String.valueOf(ind), value);
                sum_1 += value;
            }
            for (int ind = 50; ind < 100; ind++) {
                int value = (int) (Math.random() * 5);
                set.put(String.valueOf(ind), value);
                sum_2 += value;
            }
            set.put("equalizer", Math.abs(sum_1 - sum_2));

            List<Map<String, Integer>> res = Example.split(set);

            int diff = resultChecker(set, res);
            assert (diff == 0);
        }
    }

    @Test
    public void evenStressTest() {
        for (int test_num = 0; test_num < 50; test_num++) {
            Map<String, Integer> set = new HashMap<String, Integer>();
            int sum_1 = 0;
            int sum_2 = 0;
            for (int ind = 0; ind < 50; ind++) {
                int value = (int) (Math.random() * 5);
                set.put(String.valueOf(ind), value);
                sum_1 += value;
            }
            for (int ind = 50; ind < 101; ind++) { // 100 -> 101
                int value = (int) (Math.random() * 5);
                set.put(String.valueOf(ind), value);
                sum_2 += value;
            }
            set.put("equalizer", Math.abs(sum_1 - sum_2));

            List<Map<String, Integer>> res = Example.split(set);

            int diff = resultChecker(set, res);
            assert (diff == 0);
        }
    }

    @Test
    public void bigDiffTest() {
        Map<String, Integer> set = new HashMap<String, Integer>();
        for (int ind = 0; ind < 100; ind++) {
            set.put(String.valueOf(ind), 1);
        }
        set.put("equalizer", 100);


        List<Map<String, Integer>> res = Example.split(set);
        int diff = resultChecker(set, res);
        assert (diff == 98);
    }

    @Test
    public void smallTest1() {
        Map<String, Integer> set = new HashMap<String, Integer>();
        set.put("1", 1);

        List<Map<String, Integer>> res = Example.split(set);

        int diff = resultChecker(set, res);
        assert (diff == 1);
    }

    @Test
    public void smallTest2() {
        Map<String, Integer> set = new HashMap<String, Integer>();
        set.put("1", 1);
        set.put("2", 2);

        List<Map<String, Integer>> res = Example.split(set);

        int diff = resultChecker(set, res);
        assert (diff == 1);
    }

    @Test
    public void smallTest3() {
        Map<String, Integer> set = new HashMap<String, Integer>();
        set.put("1", 1);
        set.put("2", 1);
        set.put("3", 4);

        List<Map<String, Integer>> res = Example.split(set);

        int diff = resultChecker(set, res);
        assert (diff == 2);
    }


}