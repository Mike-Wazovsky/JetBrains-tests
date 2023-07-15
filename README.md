# JetBrains-tests

# Automatic test oracle generation for SE/CT

## Task #1

_Can we automatically compare two random objects using equals method? If not, try to come up with an example when equals will not work._

В ответе будем исходить из того, что объект обладает определенными полями и для его сравнения мы желаем, проверить, одинаковое ли состояние у двух объектов. Без переопределения метода _equals()_ в классе объекта, к сожалению не получится таким образом сравнить объекты. В случае вызова кода obj1.equals(obj2), без переопределения метода, джава возьмет определение метода из класса Object, который выглядит следующим образом:

```java
public class Hello {
    public static void main(String[] args) {
        System.out.println("Hello!");
    }
}
```
