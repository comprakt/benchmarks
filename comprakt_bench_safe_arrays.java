
class Main {
    public void div_by_constant() {
        int i = 0;
        int prod = 0;
        while (i < 1024 * 400) {
            SafeIntMatrix dividend = new SafeIntMatrix(); dividend.init(16, 16);

            {
                int x = 0;
                while (x < dividend.len) {
                    int y = 0;
                    while (y < dividend.len2(x)) {
                        LehmerRandom gen = new LehmerRandom().initWithDefault();
                        dividend.set(x, y, gen.random());
                        y = y + 1;
                    }
                    x = x + 1;
                }
            }

            LehmerRandom gen = new LehmerRandom().initWithDefault();

            int x = gen.random() % dividend.len;
            int y = gen.random() % dividend.len2(0);
            int divisor = dividend.get(x, y);

            dividend.div_by(divisor);

            prod = prod * dividend.get(x, y);
            i = i + 1;
        }

        System.out.println(prod); /* Should be 1 */
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.div_by_constant();
    }
}

/* Adapted from: https://en.wikipedia.org/wiki/Lehmer_random_number_generator */
class LehmerRandom {
  public int M; /* 2^31 - 1 (A large prime number) */
  public int A;      /* Prime root of M, passes statistical tests and produces a full cycle */
  public int Q; /* M / A (To avoid overflow on A * seed) */
  public int R; /* M % A (To avoid overflow on A * seed) */
  public int seed;

  public LehmerRandom init(int seed){
    this.M = 2147483647;
    this.A = 16807;
    this.Q = 127773;
    this.R = 2836;
    this.seed = seed;
    return this;
  }

  public LehmerRandom initWithDefault(){
    return init(2147480677);
  }

  public int random() {
    int hi = seed / Q;
    int lo = seed % Q;
    int test = A * lo - R * hi;
    if (test <= 0)
      test = test + M;
    seed = test;
    return test;
  }

  public int next(){
    return random();
  }

  /**
   * @param min minimum number
   * @param max exclusive range end
   */
  public int nextRange(int min, int max){
    return next() % (max - min) + min;
  }

  public int[] intArray(int size, int min, int maxEx){
    int[] arr = new int[size];
    int i = 0;
    while (i < size){
      arr[i] = nextRange(min, maxEx);
      i = i + 1;
    }
    return arr;
  }

  public boolean nextBoolean(){
    return next() % 2 == 0;
  }

  public boolean[] booleanArray(int size){
    boolean[] arr = new boolean[size];
    int i = 0;
    while (i < size){
      arr[i] = nextBoolean();
      i = i + 1;
    }
    return arr;
  }

  public void shuffleIntArray(int[] arr, int size) {
    int i = size - 1;
    while (i > 0){
      int index = nextRange(0, i + 1);
      int a = arr[index];
      arr[index] = arr[i];
      arr[i] = a;
      i = i - 1;
    }
  }
}
class Error {
    public void raise() {
        /* This results in an abort by our runtime */
        int[] panic = new int[-1];
    }
}

class SafeIntArray {
    public int len;
    public int[] data;

    public SafeIntArray init(int len) {
        this.data = new int[len];
        this.len = len;
        return this;
    }

    public int get(int idx) {
        if (0 <= idx && idx < this.len) {
            return data[idx];
        } else {
            new Error().raise();
            return -1; /* Unreachable */
        }
    }

    public void set(int idx, int value) {
        if (0 <= idx && idx < this.len) {
            data[idx] = value;
        } else {
            new Error().raise();
        }
    }
}

class SafeIntMatrix {
    public int len;
    public SafeIntArray[] data;

    public SafeIntMatrix init(int len, int len2) {
        this.data = new SafeIntArray[len];
        this.len = len;
        int i = 0;
        while (i < len) {
            this.data[i] = new SafeIntArray();
            this.data[i].init(len2);
            i = i+1;
        }

        return this;
    }

    public int get(int idx, int idx2) {
        if (0 <= idx && idx < this.len) {
            return data[idx].get(idx2);
        } else {
            new Error().raise();
            return -1; /* Unreachable */
        }
    }

    public int len2(int idx) {
        if (0 <= idx && idx < this.len) {
            return data[idx].len;
        } else {
            new Error().raise();
            return -1; /* Unreachable */
        }
    }

    public void set(int idx, int idx2, int value) {
        if (0 <= idx && idx < this.len) {
            data[idx].set(idx2, value);
        } else {
            new Error().raise();
        }
    }

    public void div_by(int divisor) {
        int x = 0;
        while (x < len) {
            int y = 0;
            while (y < len2(x)) {
                set(x, y, get(x, y) / divisor);
                y = y + 1;
            }
            x = x + 1;
        }
    }

    public void add(SafeIntMatrix that) {
        if (this.len != that.len) {
            new Error().raise();
        }

        int x = 0;
        while (x < this.len) {
            if (this.len != that.len) {
                new Error().raise();
            }

            int y = 0;
            while (y < this.len2(x)) {
                this.set(x, y, this.get(x, y) + that.get(x, y));
                y = y + 1;
            }
            x = x + 1;
        }
    }

}
