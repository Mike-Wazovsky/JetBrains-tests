# Test suite minimization for    SE/CT

## Task #1

_What do you think are pros and cons of the two proposed ways of addressing the problem?_

I'll start with what my vision is about two approaches to the description and the proposed links. 

### Первое: 
Here it will probably be necessary to attach an existing library to the beam and process the output of the beam. And then look at the metrics of code coverage and test quality, from the received tests/tests before processing, in order to assess the quality of the resulting solution.

pros: Probably the implementation is not so difficult, because it will be necessary to find/use existing solutions for test suite minimization and figure out how to access them. Also, judging by the article, minimization algorithms have high efficiency.

cons: As it was said in the paper proposed for reading, different minimization algorithms have their disadvantages and can lead to deterioration of tests. It is difficult to assess how these algorithms will work in our case, more information is required, but it is worth assuming that the solution will lead to some negative effects.

### Второе:  
In this case, you will have to get into the kex logic, which can cause difficulties in implementation. For a more accurate plan and assessment, it is necessary to understand kex more deeply. 
The basic Symbolic Execution algorithm follows the program and builds a tree of options. It will probably be necessary to check duplicate conditions at different vertices, or some other collisions during the construction of the tree of options.

pros: Theoretically, it can optimize execution time and memory, due to the fact that unnecessary test cases will be determined in advance. The solution will be sharpened for kex, which is why there may be less deterioration in the quality of tests / it will not be at all.

cons: Probably a more complex approach. It is more difficult to estimate the time that such an implementation will require, it will take more to understand the internals of kex.

## Task #2

_Which of the two methods, in your opinion, will perform better in terms of efficiency and quality? What could the efficiency and quality of them possibly depend on?_

As described in the attached paper on test suite minimization: "the effectiveness appears to be dependent on the project in question and is not strongly correlated with the size reduction percentage". Therefore, it is difficult to assume the quality of the first solution in relation to kex. However, minimization algorithms demonstrate high efficiency, significantly reducing the number of tests.

It is difficult for me to assess the quality and effectiveness of the second proposed solution, because I am not immersed in the specifics of the tests that it provides. What can be optimized in it and how often it occurs, I do not know at this level of approximation.

## Taks #3

The _a_ condition specifies the sizes of two lists that should divide the original list in half to the point of dishonesty.

The second condition sets the criterion that the total weights of the two lists should be close.

The task sounds like one of the variations of the backpack problem: We are given N items with natural weights 𝑎0, 𝑎1, . . . , 𝑎𝑛-1 the total weight of S.
It is required to select a subset of K items, what total weight can this subset be?

The task is NP-hard. There is a simple overkill solution in O(2^N). 
In addition to it, there is a dynamic solution that works behind a pseudopoline: O(N * K * S), using the assumption that sum(S) is not too large.

In addition to the solution itself, I wrote unit tests for the problem, if you want, you can find them in the archive attached to the test.

```java
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

```