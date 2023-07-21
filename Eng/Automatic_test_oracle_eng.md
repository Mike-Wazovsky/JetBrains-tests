

# Automatic test oracle generation for SE/CT

## Task #1

_Can we automatically compare two random objects using equals method? If not, try to come up with an example when equals will not work._

In the answer, we will proceed from the fact that the object has certain fields and for its comparison we want to check whether the two objects have the same state. Unfortunately, without overriding the _equals()_ method in the object class, it will not be possible to compare objects in this way. In the case of calling the obj1.equals(obj2) code, without overriding the method, java will take the method definition from the Object class, which looks like this:

```java
public boolean equals(Object obj) {
   return (this == obj);
}
```

In java, for objects, "==" means comparison by reference. For two objects of the same content, this will result in false. From here you can output an example in which equals will not work

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

The result of the program will be _false_.

## Task #2

_Do you have any alternative ideas of how we can compare two random objects for equality? Explain their pros and cons._

### 1. equals()

We can override the equals() method in the class, thereby completely defining how we want to compare objects.

Pros: The ability to customize the comparison for a specific class, determine the comparison criteria yourself.

Cons: Can take a lot of effort and code. It is necessary to be able to support the equals criteria, such as: null processing, reflexivity, symmetry, transitivity of comparison. Recursive structures, classes that have ancestors and heirs, can cause additional complexity.

### 2. Objects.equals(Object o1, Object o2)

You can also use the static Object.equals method to compare two objects. Inside it does null case handling and checks that o1 != null before calling the equals() method on the object. Does not add new logic to the comparison itself, but handles null cases for the user.

pros: handles cases when we want to have a non-strictly mandatory date in the class. Allows not to degrade the readability of the code by handling null cases.

cons: there are no special disadvantages other than the previous paragraph, since the method uses our implementation of equals() and wraps it by making preliminary checks.

### 3. Comparator

The comparator allows you to compare two objects on <, =, >. It can be conveniently used in the style of lambda programming to compare primitive fields.

```java
Comparator.comparing(Person::getFirstName)
          .thenComparing(Person::getLastName)
          .thenComparingInt(Person::getAge);
```

pros: Allows you to implement different comparison methods. Easy code readability. 

cons: It makes sense to use if the class fields consist only of primitive fields.

### 4. Serialize

In general, you can make class objects serializable and compare their serialization.

pros: Ease of implementation.

cons: Most likely an extra cost in memory / speed, in comparison with other options. It will only help if our case implies strict equality of all fields of the class.

__________

As I read, there are still all sorts of libraries that provide their own comparison methods, but I didn't find any ideologically new comparison methods there.

## Task #3

_Given a representation of a binary tree, write a function that will determine whether two binary trees have similar contents (for some definition of similarity, do not forget to also explain how you defined similarity)._

As indicated, it is possible to set the equality of two trees in different ways. Depending on whether the symmetry of the tree structure is important to us, or whether we only care about the identity of the left and right sides of the tree in any order.

For the first case, the solution is clear, you just need to consistently compare the vertex, the left tree, the right tree, and handle the final null cases.

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

The complexity of the algorithm will be O(the size of the tree)

In the second case, we will need to slightly change the algorithm to compare the branches in pairs, and then crosswise.

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

In this case, it is more difficult to estimate the theoretical complexity of the algorithm, because at each step of the algorithm, theoretically, there may be, so you will have to go down into both comparison options.

In practice, with meaningful trees, such an algorithm will work for the same O (tree size), only probably with a slightly larger coefficient.

In theory, an algorithm can break trees whose vertices are all the same. I was able to find a variant in which this tree will work in O(n^2): For example, a complete tree of depth x and a complete tree of depth x without a lower right vertex.
