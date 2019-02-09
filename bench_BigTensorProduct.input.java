/**
 * Linear congruence generator for pseudo random numbers.
 */
class LCG {

  public int a;
  public int c;
  public int m;
  public int val;

  public LCG initWithDefault(int seed){
    return init(2147483629, 2147483587, 2147483647, seed);
  }

  public LCG initWithDefault2(int seed){
    return init(2147480707, 2147480707, 2147482367, seed);
  }

  public LCG init(int a_, int c_, int m_, int startVal) {
    a = a_;
    c = c_;
    m = m_;
    val = startVal;
    return this;
  }

  public int nextVal() {
    return val = (a * val + c) % m;
  }

  /* util functions */
  public int abs(int v) {
    if (v >= 0)
      return v;
    return -v;
  }

  public void runTest() {
    int i = 0;
    while (i < 100) {
      i = i + 1;
      System.out.println(abs(nextVal()));
    }
    /*System.out.println(-2147483648 / -1);*/
  }

  /**
   * @param min minimum number
   * @param max exclusive range end
   */
  public int nextRange(int min, int max){
    return nextVal() % (max - min) + min;
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

/**
 * Calculate the tensor product for a dimension of 7
 */
class BigTensorProduct {

  public static void main(String[] args) throws Exception {
    BigTensorProduct prod = new BigTensorProduct();
    prod.run(15, false, System.in.read());
  }

  public void run(int n, boolean outputMatrix, int seed){
    LCG random = new LCG().initWithDefault2(seed);
    int sum = 0;
    int[] indezes = new int[7];
    int j = 0;
    while (j < 7){
      indezes[j] = 0;
      j = j + 1;
    }
    int[][][][][][][] arr = randomIntArray(n, seed + 1);
    int[][] vectors = randomMatrix(7, n, seed + 2);
    while (indezes[0] < n) {
      while (indezes[1] < n) {
        while (indezes[2] < n) {
          while (indezes[3] < n) {
            while (indezes[4] < n) {
              while (indezes[5] < n) {
                while (indezes[6] < n) {
                  int val = 1;
                  int i = 0;
                  while (i < 7){
                    val = val * vectors[i][indezes[i]];
                    i = i + 1;
                  }
                  if (outputMatrix){
                    System.out.println(arr[indezes[0]][indezes[1]][indezes[2]][indezes[3]][indezes[4]][indezes[5]][indezes[6]]);
                  }
                  sum = sum + val * arr[indezes[0]][indezes[1]][indezes[2]][indezes[3]][indezes[4]][indezes[5]][indezes[6]];
                  indezes[6] = indezes[6] + 1;
                }
                indezes[6] = 0;
                indezes[5] = indezes[5] + 1;
              }
              indezes[5] = 0;
              indezes[4] = indezes[4] + 1;
            }
            indezes[4] = 0;
            indezes[3] = indezes[3] + 1;
          }
          indezes[3] = 0;
          indezes[2] = indezes[2] + 1;
        }
        indezes[2] = 0;
        indezes[1] = indezes[1] + 1;
      }
      indezes[1] = 0;
      indezes[0] = indezes[0] + 1;
    }
    System.out.println(sum);
  }

  public void runWithNumbers(int n, boolean outputMatrix, int seed){
    LCG random = new LCG().initWithDefault2(seed);
    Number sum = new Number().init(0);
    int[] indezes = new int[7];
    int j = 0;
    while (j < 7){
      indezes[j] = 0;
      j = j + 1;
    }
    Number[][][][][][][] arr = randomNumberArray(n, seed + 1);
    Number[][] vectors = randomNumberMatrix(7, n, seed + 2);
    while (indezes[0] < n) {
      while (indezes[1] < n) {
        while (indezes[2] < n) {
          while (indezes[3] < n) {
            while (indezes[4] < n) {
              while (indezes[5] < n) {
                while (indezes[6] < n) {
                  Number val = new Number().init(1);
                  int i = 0;
                  while (i < 7){
                    val = val.mul(vectors[i][indezes[i]]);
                    i = i + 1;
                  }
                  if (outputMatrix){
                    System.out.println(arr[indezes[0]][indezes[1]][indezes[2]][indezes[3]][indezes[4]][indezes[5]][indezes[6]].val);
                  }
                  sum = sum.add(val.mul(arr[indezes[0]][indezes[1]][indezes[2]][indezes[3]][indezes[4]][indezes[5]][indezes[6]]));
                  indezes[6] = indezes[6] + 1;
                }
                indezes[6] = 0;
                indezes[5] = indezes[5] + 1;
              }
              indezes[5] = 0;
              indezes[4] = indezes[4] + 1;
            }
            indezes[4] = 0;
            indezes[3] = indezes[3] + 1;
          }
          indezes[3] = 0;
          indezes[2] = indezes[2] + 1;
        }
        System.out.println(sum.val);
        indezes[2] = 0;
        indezes[1] = indezes[1] + 1;
      }
      indezes[1] = 0;
      indezes[0] = indezes[0] + 1;
    }
    System.out.println(sum.val);
  }

	public int[][] randomMatrix(int number, int n, int seed){
    LehmerRandom random = new LehmerRandom().init(seed);
    int[][] ret = new int[number][];
    int i = 0;
    while (i < number){
      ret[i] = new int[n];
      int j = 0;
      while (j < n){
        ret[i][j] = random.next();
        j = j + 1;
      }
      i = i + 1;
    }
    return ret;
  }

	public Number[][] randomNumberMatrix(int number, int n, int seed){
    LehmerRandom random = new LehmerRandom().init(seed);
    Number[][] ret = new Number[number][];
    int i = 0;
    while (i < number){
      ret[i] = new Number[n];
      int j = 0;
      while (j < n){
        ret[i][j] = new Number().init(random.next());
        j = j + 1;
      }
      i = i + 1;
    }
    return ret;
  }

  public int[][][][][][][] randomIntArray(int n, int seed){
    LCG lcg = new LCG().initWithDefault(seed);
    int[][][][][][][] arr = new int[n][][][][][][];
    int[] indezes = new int[7];
    while (indezes[0] < n) {
      arr[indezes[0]] = new int[n][][][][][];
      while (indezes[1] < n) {
        arr[indezes[0]][indezes[1]] = new int[n][][][][];
        while (indezes[2] < n) {
          arr[indezes[0]][indezes[1]][indezes[2]] = new int[n][][][];
          while (indezes[3] < n) {
            arr[indezes[0]][indezes[1]][indezes[2]][indezes[3]] = new int[n][][];
            while (indezes[4] < n) {
              arr[indezes[0]][indezes[1]][indezes[2]][indezes[3]][indezes[4]] = new int[n][];
              while (indezes[5] < n) {
                arr[indezes[0]][indezes[1]][indezes[2]][indezes[3]][indezes[4]][indezes[5]] = new int[n];
                while (indezes[6] < n) {
                  arr[indezes[0]][indezes[1]][indezes[2]][indezes[3]][indezes[4]][indezes[5]][indezes[6]] = lcg.nextVal();
                  indezes[6] = indezes[6] + 1;
                }
                indezes[6] = 0;
                indezes[5] = indezes[5] + 1;
              }
              indezes[5] = 0;
              indezes[4] = indezes[4] + 1;
            }
            indezes[4] = 0;
            indezes[3] = indezes[3] + 1;
          }
          indezes[3] = 0;
          indezes[2] = indezes[2] + 1;
        }
        indezes[2] = 0;
        indezes[1] = indezes[1] + 1;
      }
      indezes[1] = 0;
      indezes[0] = indezes[0] + 1;
    }
    return arr;
  }

  public Number[][][][][][][] randomNumberArray(int n, int seed){
    LCG lcg = new LCG().initWithDefault(seed);
    Number[][][][][][][] arr = new Number[n][][][][][][];
    int[] indezes = new int[7];
    while (indezes[0] < n) {
      arr[indezes[0]] = new Number[n][][][][][];
      while (indezes[1] < n) {
        arr[indezes[0]][indezes[1]] = new Number[n][][][][];
        while (indezes[2] < n) {
          arr[indezes[0]][indezes[1]][indezes[2]] = new Number[n][][][];
          while (indezes[3] < n) {
            arr[indezes[0]][indezes[1]][indezes[2]][indezes[3]] = new Number[n][][];
            while (indezes[4] < n) {
              arr[indezes[0]][indezes[1]][indezes[2]][indezes[3]][indezes[4]] = new Number[n][];
              while (indezes[5] < n) {
                arr[indezes[0]][indezes[1]][indezes[2]][indezes[3]][indezes[4]][indezes[5]] = new Number[n];
                while (indezes[6] < n) {
                  arr[indezes[0]][indezes[1]][indezes[2]][indezes[3]][indezes[4]][indezes[5]][indezes[6]] = new Number().init(lcg.nextRange(-1000, 1000));
                  indezes[6] = indezes[6] + 1;
                }
                indezes[6] = 0;
                indezes[5] = indezes[5] + 1;
              }
              indezes[5] = 0;
              indezes[4] = indezes[4] + 1;
            }
            indezes[4] = 0;
            indezes[3] = indezes[3] + 1;
          }
          indezes[3] = 0;
          indezes[2] = indezes[2] + 1;
        }
        indezes[2] = 0;
        indezes[1] = indezes[1] + 1;
      }
      indezes[1] = 0;
      indezes[0] = indezes[0] + 1;
    }
    return arr;
  }
}

class Number {

  public int val;

  public Number init(int val){
    this.val = val;
    return this;
  }

  public Number mul(Number other){
    return new Number().init(other.val * val);
  }

  public Number add(Number other){
    return new Number().init(other.val + val);
  }
}
