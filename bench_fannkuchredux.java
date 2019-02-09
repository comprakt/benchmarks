class Math {
  public int pow(int v, int exp){
    if (exp < 0){
      return 1 / pow(v, -exp);
    } else if (exp == 0){
      return 1;
    } else {
      int ret = 1;
      while (exp > 0){
        if (exp % 2 == 0){
          v = v * v;
          exp = exp / 2;
        } else {
          ret = ret * v;
          exp = exp - 1;
        }
      }
      return ret;
    }
  }

  public int factorial(int val){
    int ret = 1;
    int sign = signum(val);
    if (val < 0){
      val = -val;
    }
    if (val == 0){
      return 1;
    }
    while (val > 0){
      ret = ret * val;
      val = val - 1;
    }
    return ret * sign;
  }

  public int min(int s, int t){
    if (s < t){
      return s;
    }
    return t;
  }

  public int max(int s, int t){
    if (s > t){
      return s;
    }
    return t;
  }

  public int lengthInChars(int num){
    int len = 1;
    if (num < 0){
      len = len + 1;
      num = -num;
    }
    while (num > 10){
      num = num / 10;
      len = len + 1;
    }
    return len;
  }

  public int signum(int num){
    if (num == 0){
      return 0;
    }
    if (num < 0){
      return -1;
    }
    return 1;
  }

  public int abs(int num){
    if (num < 0){
      return -num;
    }
    return num;
  }

}

/*
   Adapted from the following source:

   The Computer Language Benchmarks Game
   http://shootout.alioth.debian.org/

   contributed by Isaac Gouy
   converted to Java by Oleg Mazurov
*/
class fannkuchredux {
  public int maxFlipsCount;

  public void fannkuch(int n) {
    int[] perm = new int[n];
    int[] perm1 = new int[n];
    int[] count = new int[n];
    maxFlipsCount = 0;
    int permCount = 0;
    int checksum = 0;

    int i = 0;
    while (i < n){
      perm1[i] = i;
      i = i + 1;
    }
    int r = n;

    while (true) {
      while (r != 1){ count[r-1] = r; r = r - 1; }

      i = 0;
      while (i < n){
        perm[i] = perm1[i];
        i = i + 1;
      }
      int flipsCount = 0;
      int k = 0;

      while ( !((k=perm[0]) == 0) ) {
        int k2 = (k+1) / 2;
        int j = 0;
        while (j < k2){
          int temp = perm[j]; perm[j] = perm[k-j]; perm[k-j] = temp;
          j = j + 1;
        }
        flipsCount = flipsCount + 1;
      }

      maxFlipsCount = new Math().max(maxFlipsCount, flipsCount);
      if (permCount % 2 == 0){
        checksum = checksum + flipsCount;
      } else {
        checksum = checksum - flipsCount;
      }

      /* Use incremental change to generate another permutation */
      boolean doBreak = false;
      while (!doBreak) {
        if (r == n) {
          System.out.println( checksum );
          return;
        }
        int perm0 = perm1[0];
        int l = 0;
        while (l < r) {
          int j = l + 1;
          perm1[l] = perm1[j];
          l = j;
        }
        perm1[r] = perm0;

        count[r] = count[r] - 1;
        if (count[r] > 0) doBreak = true;
        else r = r + 1;
      }
      permCount = permCount + 1;
    }
  }

  public static void main(String[] args){
    fannkuchredux fk = new fannkuchredux();
    fk.fannkuch(11);
    System.out.println(fk.maxFlipsCount);
  }
}
