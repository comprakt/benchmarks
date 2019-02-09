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
   http://benchmarksgame.alioth.debian.org/
 
   contributed by Jarkko Miettinen
*/
class binarytrees {

  public int minDepth;

  public static void main(String[] args){
    new binarytrees().init().run(19);
  }

  public binarytrees init(){
    minDepth = 4;
    return this;
  }

  public void run(int n){

    int maxDepth = 0;
    if (minDepth + 2 > n){
      maxDepth = minDepth + 2;
    } else {
      maxDepth = n;
    }
    int stretchDepth = maxDepth + 1;

    int check = bottomUpTree(0,stretchDepth).itemCheck();
    /*System.out.println("stretch tree of depth "+stretchDepth+"\t check: " + check);*/
    System.out.println(stretchDepth);
    System.out.println(check);

    TreeNode longLivedTree = bottomUpTree(0,maxDepth);
    int depth = minDepth;
    while (depth <= maxDepth){
      int iterations = new Math().pow(2, maxDepth - depth + minDepth);
      check = 0;

      int i = 1;
      while (i <= iterations){
        check = check + (bottomUpTree(i,depth)).itemCheck();
        check = check + (bottomUpTree(-i,depth)).itemCheck();
        i = i + 1;
      }
      /*System.out.println((iterations*2) + "\t trees of depth " + depth + "\t check: " + check);*/
      System.out.println(iterations * 2);
      System.out.println(depth);
      System.out.println(check);
      depth = depth + 2;
    }
    /*System.out.println("long lived tree of depth " + maxDepth + "\t check: "+ longLivedTree.itemCheck());*/
    System.out.println(maxDepth);
    System.out.println(longLivedTree.itemCheck());
  }

  public TreeNode bottomUpTree(int item, int depth){
    if (depth>0){
      return new TreeNode().init2(
              bottomUpTree(2*item-1, depth-1)
              , bottomUpTree(2*item, depth-1)
              , item
      );
    }
    else {
      return new TreeNode().init(item);
    }
  }

}

class TreeNode {
  public TreeNode left;
  public TreeNode right;
  public int item;

  public TreeNode init(int item){
    this.item = item;
    return this;
  }


  public TreeNode init2(TreeNode left, TreeNode right, int item){
    this.left = left;
    this.right = right;
    this.item = item;
    return this;
  }

  public int itemCheck(){
    if (left==null) return item;
    else return item + left.itemCheck() - right.itemCheck();
  }
}
