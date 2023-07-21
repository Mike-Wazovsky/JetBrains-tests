# Test suite minimization for    SE/CT

## Task #1

_What do you think are pros and cons of the two proposed ways of addressing the problem?_

Начну с того, какое у меня сложилось видение о двух подходах по описанию и предложенным ссылкам.    

### Первое: 
Тут вероятно надо будет присоединить к kex существующую библиотеку и обрабатывать выход kex. А после чего посмотреть на метрики покрытия кода и качества тестов, у полученных тестов/тестов до обработки, чтобы оценить качество полученного решения.

pros: Вероятно реализация не так сложна, потому что надо будет найти/использовать существующие решения для test suite minimization и разобраться как к ним обращаться. Так же судя по статье, алгоритмы минимизации обладают высокой эффективностью.

cons: Как было сказано в предложенной для прочтения работе разные алгоритмы минимизации обладают своими минусами и могут приводить к ухудшению тестов. Трудно оценить, как эти алгоритмы подействуют в нашем случае, требуется больше информации, однако стоит предполагать, что решение приведет к некоторым негативным эффектам.

### Второе:  
В данном случае придется лезть в логику kex, что может вызвать сложности в реализации. Для более точного плана и оценки требуется разбираться с kex глубже. 
Базово Sumbolic Execution алгоритм идет по программе и строит дерево вариантов. Вероятно нужно будет в процессе построения дерева вариантов, проверять дублирующиеся условия в разных вершинах, или какие-то другие коллизии.

pros: Теоретически может оптимизировать время выполнения и память, из-за того, что ненужные тест-кейсы будут определяться заранее. Решение будет заточено под kex, из-за чего возможно ухудшение качества тестов будет меньше/его не будет вовсе.

cons: Вероятно более сложный подохд. Сложнее оценить время, которого потребует такая реализация, потребуется больше разбираться с внутренностями kex.

## Task #2

_Which of the two methods, in your opinion, will perform better in terms of efficiency and quality? What could the efficiency and quality of them possibly depend on?_

Как было описано в приложенной работе о test suite minimization: "the effectiveness appears to be dependent on the project in question and is not strongly correlated with the size reduction percentage". Поэтому предположить качество первого решения применительно к kex сложно. Однако Алгоритмы минимизации демонстрируют высокую эффективность, существенно уменьшая количество тестов.

Качество и эффективность второго предложенного решения мне трудно оценить, потому что я не погружен в спицифику тестов, которые он предоставляет. Что можно в нём оптимизировать и насколько часто это встречается я на данном уровне приближения не знаю.

## Taks #3

Условие _a_ задает размеры двух списков, которые должны делить исходный список по полам с точностью до нечестности.

Второе условие задает критерий, что суммарные веса двух списков должны быть близки.

Задача звучит как одна из вариаций задачи о рюкзаке: Нам дано N предметов с натуральными весами 𝑎0, 𝑎1, . . . , 𝑎𝑛−1 суммарного веса S.
Требуется выбрать подмножество из K предметов, какого суммарного веса может быть это подмножество?

Задача NP сложная. Существует простое переборное решение за O(2^N). 
Помимо него существует динамическое решение, работающее за псевдополином: O(N * K * S), пользующееся предположением, что sum(S) не слишком велика.

Помимо самого решения, я написал юнит-тесты для задачи, если хотите, то можете найти их в архиве, приложенном к тестовому.

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