# JetBrains-tests

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
