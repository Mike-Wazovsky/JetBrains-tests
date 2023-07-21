

# Automatic test oracle generation for SE/CT

## Task #1

_Can we automatically compare two random objects using equals method? If not, try to come up with an example when equals will not work._

В ответе будем исходить из того, что объект обладает определенными полями и для его сравнения мы желаем, проверить, одинаковое ли состояние у двух объектов. Без переопределения метода _equals()_ в классе объекта, к сожалению не получится таким образом сравнить объекты. В случае вызова кода obj1.equals(obj2), без переопределения метода, джава возьмет определение метода из класса Object, который выглядит следующим образом:

```java
public boolean equals(Object obj) {
   return (this == obj);
}
```

В языке java для объектов ''=='' означает сравнение по ссылке. Для двух одинковых по наполнению определенных объектов это приведет, к false. Отсюда можно вывести пример, при котором equals не сработает

```java
public class Foo {
    int x;
    String str;


    public static void main(String[] args) {

        Foo foo1 = new Foo();
        foo1.x = 1;
        foo1.str = "Pipi-pupi-papi";

        Foo foo2 = new Foo();
        foo2.x = 1;
        foo2.str = "Pipi-pupi-papi";

        System.out.println(foo1.equals(foo2));
    }
}
```

Результатом программы будет _false_.

## Task #2

_Do you have any alternative ideas of how we can compare two random objects for equality? Explain their pros and cons._

### 1. equals()

Мы можем переопределить метод equals() в классе, тем самым полностью определять, каким образом мы хотим сравнить объекты.

Pros: Возможность настраивать сравнение под конкретный класс, определять критерии сравнения самому.

Cons: Может занимать много усилий и кода. Необходимо уметь поддерживать критерии equals, такие как: обработка null, рефлексивность, симметричность, транзитивность сравнения. Дополнительную сложность могут вызывать рекурсивные структуры, классы у которых есть предки и наследники.

### 2. Objects.equals(Object o1, Object o2)

Можно так же воспользоваться статическим методом Objects.equals для сравнения двух объектов. Внутри он делает обработку null случаев и проверяет что o1 != null перед вызовом equals() метода у объекта. Не добавляет новой логики в само сравнение, но обрабатывает null кейсы за пользователя.

pros: хэндлит случаи, когда мы хотим иметь не строго обязательную дату в классе. Позволяет не ухудшать читаемость кода, обработками null случаев.

cons: какие-то особые минусы, отличные от прошлого пункта отсутствуют, так как метод использует нашу реализацию equals() и оборачивает её, делая предварительные проверки.

### 3. Comparator

Компаратор позволяет сравнить два объекта на <, =, >. Можно удобно использовать в стиле лямбда программирования для сравнения примитивных полей.

```java
Comparator.comparing(Person::getFirstName)
          .thenComparing(Person::getLastName)
          .thenComparingInt(Person::getAge);
```

pros: Позволяет реализовывать разные способы сравнения. Легкая читаемость кода. 

cons: Имеет смысл в использовании, если поля класса состоят только из примитивных полей.

### 4. Serialize

В целом, можно сделать объекты класса сериализуемыми и сравнивать их сериализацию.

pros: Легкость реализации.

cons: Скорее всего лишняя затратность по памяти/скорости, в сравнении с другими вариантами. Поможет только, если наш кейс подразумевает строгое равенство всех полей класса.

__________

Как я почитал есть ещё всякие библиотеки, предоставляющие свои методы сравнения, но идейно новых способов сравнения я там не нашел.

## Task #3

_Given a representation of a binary tree, write a function that will determine whether two binary trees have similar contents (for some definition of similarity, do not forget to also explain how you defined similarity)._

Как было указано, можно по-разному задавать равенство двух деревьев. В зависимости от того, важна ли нам симметричность структуры дерева, или же нам важна только идентичность левой и правой сторон дерева в любом порядке. 

Для первого случая решение понятно, нужно просто последовательно сравнивать вершину, левое дерево, правое дерево, и обрабатывать конечные случаи null.

```java
class BinaryTree {

    int value;
    BinaryTree left;
    BinaryTree right;

    static boolean contentsSimilar(BinaryTree lhv, BinaryTree rhv) {
        if (lhv == null & rhv == null)
            return true;
        if (lhv == null)
            return false;
        if (rhv == null)
            return false;

        // Now then both lhv and rhv are not Null
        return ((lhv.value == rhv.value) &&
                contentsSimilar(lhv.left, rhv.left) &&
                contentsSimilar(lhv.right, rhv.right));
    }
    

    // You can consider that these methods are implemented // and you can use them if needed
    boolean contains(int value);
    boolean add(int value);
    boolean remove(int value);
    int size();
}
```

Сложность алгоритма будет O(размер дерева)

Во втором случае, нам потребуется немного изменить алгоритма, чтобы сравнивать ветки попарно, а потом перекрестно.

```java
class BinaryTree {

    int value;
    BinaryTree left;
    BinaryTree right;

    static boolean contentsSimilar(BinaryTree lhv, BinaryTree rhv) {
        if (lhv == null & rhv == null)
            return true;
        if (lhv == null)
            return false;
        if (rhv == null)
            return false;

        // Now then both lhv and rhv are not Null
        return ((lhv.value == rhv.value) &&
                (contentsSimilar(lhv.left, rhv.left) && contentsSimilar(lhv.right, rhv.right)) ||
                (contentsSimilar(lhv.left, rhv.right) && contentsSimilar(lhv.right, rhv.left))
                );
    }


    // You can consider that these methods are implemented // and you can use them if needed
    boolean contains(int value);
    boolean add(int value);
    boolean remove(int value);
    int size();
}
```

В данном случае оценить теоретическую сложность алгоритма более затруднительно, потому что на каждом шагу алгоритма теоретически может быть, так что придется спускаться в оба варианта сравнения.

На практике с осмысленными деревьями такой алгоритм будет работать за те же O(размера дерева), только вероятно с немного большим коэффициентом.

В теории алгоритм могут ломать деревья, все вершины которого одинаковы. Я смог найти вариант, при котором данное дерево будет работать за O(n^2): Например полное дерево глубины x и полное дерево глубины x без нижней правой вершины.
