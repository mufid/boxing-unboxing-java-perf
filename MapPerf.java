import java.util.*;

public class MapPerf {

  protected static final int MAX = 5 * 1000 * 1000;

  public static void main(String[] ar) {
    Benchmark b1 = new Benchmark() {
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
    Benchmark b2 = new Benchmark() {
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

    long result1 = b1.benchmark();
    long result2 = b2.benchmark();

    System.out.println("Boxing/unboxing: " + result1 + " ms");
    System.out.println("Encapsulation: " + result2 + " ms");

  }
}

abstract class Benchmark implements Runnable {
  protected int hack;
  public long benchmark() {
    long start = System.currentTimeMillis();
    run();
    long end = System.currentTimeMillis();
    return end - start;
  }
}

class EncapsulatedInteger {
  int v;
  public EncapsulatedInteger(int v) {
    this.v = v;
  }
}
