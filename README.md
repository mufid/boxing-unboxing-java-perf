# Box / Unboxing vs. Encapsulation: Java's `Map<Something, Integer>`

Problem: I want a map that store integer as its value. Now i
have two approaches:

1. Use anything as key and `Integer` (non-primitive integer)
   as value, or
1. Use anything as key and a class that contains `int` (primitive
   integer) as value.

Which should i use? The first solution seems pretty neat, nothing
to do. Just instatiate new `Integer` and done. At the other hand,
performance wise, it will do unnecessary boxing/unboxing if
not properly handled. Remember that we only able to use primitive
integer to do calculation and primitive integer to store value
to the map.

## The Test

    Benchmark withBoxing = new Benchmark() {
      public void run() {
        Map<String, Integer> a = new HashMap<String, Integer>();

        // Simulate much boxing
        for (int i = 0; i < MAX; i++) {
          a.put("" + i, i);
        }

        // Simulate much unboxing
        for (int i = 0; i < MAX; i++) {
          // A hack so that JVM does not optimize this code.
          hack += a.get("" + i); 
          hack = hack % 1000;
        }
      }
    };
    Benchmark encapsulate = new Benchmark() {
      public void run() {
        Map<String, EncapsulatedInteger> a = new HashMap<String, EncapsulatedInteger>();

        // No boxing/unboxing, but new class
        for (int i = 0; i < MAX; i++) {
          a.put("" + i, new EncapsulatedInteger(i));
        }

        for (int i = 0; i < MAX; i++) {
          // A hack so that JVM does not optimize this code.
          hack += a.get("" + i).v; 
          hack = hack % 1000;
        }
      }
    };

## The run command

    javac MapPerf.java; java -Xmx1024m -cp . MapPerf

## The Result

    $ javac MapPerf.java; java -Xmx1024m -cp . MapPerf
    Boxing/unboxing: 10973 ms
    Encapsulation: 7036 ms

## Summary

With `Integer` as value, the routine is 50% slower because of unnecessary
boxing/unboxing

