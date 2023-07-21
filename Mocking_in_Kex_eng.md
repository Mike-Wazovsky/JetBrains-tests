# Mocking in Kex

## Task #1  

_Can you think of a way to solve the described problem without using mocking frameworks?_

An option comes to my mind in which to build a symbolic execution tree, you can rewrite the code of the program under test. You can remove the use of the foo class. For example, how in the provided case to rewrite the calls of abstract methods of the class, replacing them with unknown variables.

For the example provided , the following would result:

```java
class FooCopy { 
    int x;
    int y;
    int foo;
}

class BarCopy {
    void bar(FooCopy f) {
        int res = f.foo; 
        if (res > 0) {
            System.out.println("Success"); 
        } else {
            System.out.println("Failure"); 
        }
    } 
}
```

I can assume that such a solution violates some holy rules of testing, for example, that it is impossible to change the code of the tested object itself. This definitely has a problem related to the fact that you can only make more bugs / the process of directly testing the source code becomes more difficult.

________________________________

Another solution may be to use dummy. Insert a simple, hard-coded value. For example, for objects from the function, return null, and for any primitive, return the default value.

```java
class FooCopy { 
    int x;
    int y;
    
    public int foo() { return 0};
}

class BarCopy {
    void bar(FooCopy f) {
        int res = f.foo(); 
        if (res > 0) {
            System.out.println("Success"); 
        } else {
            System.out.println("Failure"); 
        }
    } 
}
```

The main problem with this approach is that the test tree coverage of all program paths can be severely cut off, for example, in the provided case, the program will not be able to test the "Success" case.

## Task #2

Probably the biggest problem will arise due to the fact that mock usually creates stubs on methods, that is, it gives class functionality that Kex does not imply. Just as I read, for example Mockito puts stubs as specific values, which can greatly affect the quality of the result.

In this regard, you will probably have to either teach Kex to perceive methods in the process of the algorithm and also branch according to these methods, or teach Mockers to create mockups in which the results of methods depend on fields in some way. For example, create an unknown variable _foo_res_ in the class, which will be returned when the _foo method is called. So probably the Beam will be able to air out by the output of the function method, that is, as if by a variable.



I'm sorry, I didn't have enough time to finish the third test before the end of the week:)











