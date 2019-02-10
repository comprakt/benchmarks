/* This Benchmark focusses on the load store optimization in our compiler */
class CompraktBenchLoadStore {
    public static void main(String[] args) {
       BooleanUtils bool = new BooleanUtils();
        CompraktBenchLoadStore bench = new CompraktBenchLoadStore();

        bench.part1(bool);
        bench.part2(bool);
        bench.part3(bool);
    }

    /* test the implementation*/
    public void part1(BooleanUtils bool) {
        new Str().init20(84, 69, 83, 84, 32, 84, 72, 69, 32, 83, 72, 73, 70, 84, 32, 82, 69, 71, 73, 83)
            .concat(new Str().init3(84, 69, 82)).println(); /* TEST THE SHIFT REGISTER */
        MemberBasedShiftRegister shr = new MemberBasedShiftRegister();
        int val = 0;
        while (val < 256) {
            shr.setFromUnsignedInt(bool, val);
            shr.dump(bool);
            int res = shr.asUnsignedInt(bool);
            if (res != val) {
                new Str().init20(115, 104, 105, 102, 116, 32, 114, 101, 103, 105, 115, 116, 101, 114, 32, 105, 115, 32, 98, 114)
                    .concat(new Str().init4(111, 107, 101, 110)).println(); /* shift register is broken */
                System.out.println(res);
                System.out.println(val);
                int[] panic = new int[-1];
            }
            System.out.println(res);
            val = val + 1;
        }
    }

    /* Allocate shift register inside the loop, doesn't escape
       => should eliminate allocation, loads and stores

       ALLOCATES: 60000000 * sizeof(MemberBasedShiftRegister) + churn
    */
    public void part2(BooleanUtils bool) {
        int sum = 0;
        int i = 0;
        while (i < 6000000) {
            MemberBasedShiftRegister shr = new MemberBasedShiftRegister();
            shr.init(false);
             if ((i % 2) == 0) {
                shr.shiftLeft(false);
            } else {
                shr.shiftLeft(true);
            }
            sum = sum + shr.asUnsignedInt(bool);
            i = i + 1;
        }
        System.out.println(sum);
    }
    
    /* Allocate shift register outside of the loop. Allocation doesn't escape the function.
       => should eliminate allocation, loads and stores

       ALLOCATES: sizeof(MemberBasedShiftRegister) + churn
    */
    public void part3(BooleanUtils bool) {
        new Str().init20(69, 88, 69, 82, 67, 73, 83, 69, 32, 84, 72, 69, 32, 83, 72, 73, 70, 84, 32, 82)
            .concat(new Str().init16(69, 71, 73, 83, 84, 69, 82, 32, 65, 32, 76, 73, 84, 84, 76, 69)).println(); /* EXERCISE THE SHIFT REGISTER A LITTLE */

        MemberBasedShiftRegister shr = new MemberBasedShiftRegister();
        shr.init(false);
        int x = 0;
        int sum = 0;
        while (x < 250000000) {
            if ((x % 2) == 0) {
                shr.shiftLeft(false);
            } else {
                shr.shiftLeft(true);
            }
            sum = sum + shr.asUnsignedInt(bool);
            x = x+1;
        }
        shr.dump(bool);
        System.out.println(sum);
    }

}

class MemberBasedShiftRegister {

    /* LSB */
    public boolean bit0;
    public boolean bit1;
    public boolean bit2;
    public boolean bit3;
    public boolean bit4;
    public boolean bit5;
    public boolean bit6;
    /* MSB */
    public boolean bit7;

    public void init(boolean init) {
        bit0 = init;
        bit1 = init;
        bit2 = init;
        bit3 = init; 
        bit4 = init;
        bit5 = init;
        bit6 = init;
        bit7 = init;
    }

    public void shiftLeft(boolean bit) {
        bit7 = bit6;
        bit6 = bit5;
        bit5 = bit4;
        bit4 = bit3;
        bit3 = bit2;
        bit2 = bit1;
        bit0 = bit;
    }

    public void shiftRight(boolean bit) {
        bit0 = bit1;
        bit1 = bit2;
        bit2 = bit3;
        bit3 = bit4;
        bit4 = bit5;
        bit5 = bit6;
        bit6 = bit7;
        bit7 = bit;
    }

    public int asUnsignedInt(BooleanUtils bool) {
        int sum = 0;
        sum = sum +   1 * bool.toInt(bit0);
        sum = sum +   2 * bool.toInt(bit1);
        sum = sum +   4 * bool.toInt(bit2);
        sum = sum +   8 * bool.toInt(bit3);
        sum = sum +  16 * bool.toInt(bit4);
        sum = sum +  32 * bool.toInt(bit5);
        sum = sum +  64 * bool.toInt(bit6);
        sum = sum + 128 * bool.toInt(bit7);
        return sum;
    }

    public void setFromUnsignedInt(BooleanUtils bool, int x) {
        if (x < 0 || x > 255) {
            int[] y = new int[-1];
        }
        int left = x;
        left = left - 128;
        bit7 = left >= 0;
        if (!bit7) {
            left = left + 128;
        }

        left = left - 64;
        bit6 = left >= 0;
        if (!bit6) {
            left = left + 64;
        }

        left = left - 32;
        bit5 = left >= 0;
        if (!bit5) {
            left = left + 32;
        }

        left = left - 16;
        bit4 = left >= 0;
        if (!bit4) {
            left = left + 16;
        }

        left = left - 8;
        bit3 = left >= 0;
        if (!bit3) {
            left = left + 8;
        }

        left = left - 4;
        bit2 = left >= 0;
        if (!bit2) {
            left = left + 4;
        }

        left = left - 2;
        bit1 = left >= 0;
        if (!bit1) {
            left = left + 2;
        }

        left = left - 1;
        bit0 = left >= 0;
        if (!bit0) {
            left = left + 1;
        }
    }

    public void dump(BooleanUtils bool) {
        new Str().init6(98, 105, 116, 48, 58, 32).print(); /* bit0:  */
        bool.println(bit0);
        new Str().init6(98, 105, 116, 49, 58, 32).print(); /* bit1:  */
        bool.println(bit1);
        new Str().init6(98, 105, 116, 50, 58, 32).print(); /* bit2:  */
        bool.println(bit2);
        new Str().init6(98, 105, 116, 51, 58, 32).print(); /* bit3:  */
        bool.println(bit3);
        new Str().init6(98, 105, 116, 52, 58, 32).print(); /* bit4:  */
        bool.println(bit4);
        new Str().init6(98, 105, 116, 53, 58, 32).print(); /* bit5:  */
        bool.println(bit5);
        new Str().init6(98, 105, 116, 54, 58, 32).print(); /* bit6:  */
        bool.println(bit6);
        new Str().init6(98, 105, 116, 55, 58, 32).print(); /* bit7:  */
        bool.println(bit7);
    }

}

/* BEGIN ../mjtest/tests/exec/lib/BooleanUtils.java */


class BooleanUtils {

  public int toInt(boolean value){
    if (value){
      return 1;
    } else {
      return 0;
    }
  }

  public boolean toBoolean(int value) {
    return value != 0;
  }

  public BooleanUtils println(boolean value){
    System.out.println(toInt(value));
    return this;
  }
}


/* END ../mjtest/tests/exec/lib/BooleanUtils.java */

/* BEGIN ../mjtest/tests/exec/lib/Str.java */


/**
 * A very simple and wasteful implementation of strings
 * 
 * To quickly generate code for strings, you can use this script:
 * https://git.scc.kit.edu/ufebl/mjtest-scripts/blob/master/str_tool.py
 */
class Str {
    public int[] chars;
    public int length;

    public Str init(int[] chars, int length) {
        this.chars = chars;
        this.length = length;
        return this;
    }

    /**** generated constructors ****/
    public Str init0() {
        this.chars = new int[0];
        this.length = 0;
        return this;
    }

    public Str init1(int c1) {
        this.chars = new int[1];
        this.length = 1;
        this.chars[0] = c1;
        return this;
    }

    public Str init2(int c1, int c2) {
        this.chars = new int[2];
        this.length = 2;
        this.chars[0] = c1;
        this.chars[1] = c2;
        return this;
    }

    public Str init3(int c1, int c2, int c3) {
        this.chars = new int[3];
        this.length = 3;
        this.chars[0] = c1;
        this.chars[1] = c2;
        this.chars[2] = c3;
        return this;
    }

    public Str init4(int c1, int c2, int c3, int c4) {
        this.chars = new int[4];
        this.length = 4;
        this.chars[0] = c1;
        this.chars[1] = c2;
        this.chars[2] = c3;
        this.chars[3] = c4;
        return this;
    }

    public Str init5(int c1, int c2, int c3, int c4, int c5) {
        this.chars = new int[5];
        this.length = 5;
        this.chars[0] = c1;
        this.chars[1] = c2;
        this.chars[2] = c3;
        this.chars[3] = c4;
        this.chars[4] = c5;
        return this;
    }

    public Str init6(int c1, int c2, int c3, int c4, int c5, int c6) {
        this.chars = new int[6];
        this.length = 6;
        this.chars[0] = c1;
        this.chars[1] = c2;
        this.chars[2] = c3;
        this.chars[3] = c4;
        this.chars[4] = c5;
        this.chars[5] = c6;
        return this;
    }

    public Str init7(int c1, int c2, int c3, int c4, int c5, int c6, int c7) {
        this.chars = new int[7];
        this.length = 7;
        this.chars[0] = c1;
        this.chars[1] = c2;
        this.chars[2] = c3;
        this.chars[3] = c4;
        this.chars[4] = c5;
        this.chars[5] = c6;
        this.chars[6] = c7;
        return this;
    }

    public Str init8(int c1, int c2, int c3, int c4, int c5, int c6, int c7, int c8) {
        this.chars = new int[8];
        this.length = 8;
        this.chars[0] = c1;
        this.chars[1] = c2;
        this.chars[2] = c3;
        this.chars[3] = c4;
        this.chars[4] = c5;
        this.chars[5] = c6;
        this.chars[6] = c7;
        this.chars[7] = c8;
        return this;
    }

    public Str init9(int c1, int c2, int c3, int c4, int c5, int c6, int c7, int c8, int c9) {
        this.chars = new int[9];
        this.length = 9;
        this.chars[0] = c1;
        this.chars[1] = c2;
        this.chars[2] = c3;
        this.chars[3] = c4;
        this.chars[4] = c5;
        this.chars[5] = c6;
        this.chars[6] = c7;
        this.chars[7] = c8;
        this.chars[8] = c9;
        return this;
    }

    public Str init10(int c1, int c2, int c3, int c4, int c5, int c6, int c7, int c8, int c9, int c10) {
        this.chars = new int[10];
        this.length = 10;
        this.chars[0] = c1;
        this.chars[1] = c2;
        this.chars[2] = c3;
        this.chars[3] = c4;
        this.chars[4] = c5;
        this.chars[5] = c6;
        this.chars[6] = c7;
        this.chars[7] = c8;
        this.chars[8] = c9;
        this.chars[9] = c10;
        return this;
    }

    public Str init11(int c1, int c2, int c3, int c4, int c5, int c6, int c7, int c8, int c9, int c10, int c11) {
        this.chars = new int[11];
        this.length = 11;
        this.chars[0] = c1;
        this.chars[1] = c2;
        this.chars[2] = c3;
        this.chars[3] = c4;
        this.chars[4] = c5;
        this.chars[5] = c6;
        this.chars[6] = c7;
        this.chars[7] = c8;
        this.chars[8] = c9;
        this.chars[9] = c10;
        this.chars[10] = c11;
        return this;
    }

    public Str init12(int c1, int c2, int c3, int c4, int c5, int c6, int c7, int c8, int c9, int c10, int c11,
            int c12) {
        this.chars = new int[12];
        this.length = 12;
        this.chars[0] = c1;
        this.chars[1] = c2;
        this.chars[2] = c3;
        this.chars[3] = c4;
        this.chars[4] = c5;
        this.chars[5] = c6;
        this.chars[6] = c7;
        this.chars[7] = c8;
        this.chars[8] = c9;
        this.chars[9] = c10;
        this.chars[10] = c11;
        this.chars[11] = c12;
        return this;
    }

    public Str init13(int c1, int c2, int c3, int c4, int c5, int c6, int c7, int c8, int c9, int c10, int c11, int c12,
            int c13) {
        this.chars = new int[13];
        this.length = 13;
        this.chars[0] = c1;
        this.chars[1] = c2;
        this.chars[2] = c3;
        this.chars[3] = c4;
        this.chars[4] = c5;
        this.chars[5] = c6;
        this.chars[6] = c7;
        this.chars[7] = c8;
        this.chars[8] = c9;
        this.chars[9] = c10;
        this.chars[10] = c11;
        this.chars[11] = c12;
        this.chars[12] = c13;
        return this;
    }

    public Str init14(int c1, int c2, int c3, int c4, int c5, int c6, int c7, int c8, int c9, int c10, int c11, int c12,
            int c13, int c14) {
        this.chars = new int[14];
        this.length = 14;
        this.chars[0] = c1;
        this.chars[1] = c2;
        this.chars[2] = c3;
        this.chars[3] = c4;
        this.chars[4] = c5;
        this.chars[5] = c6;
        this.chars[6] = c7;
        this.chars[7] = c8;
        this.chars[8] = c9;
        this.chars[9] = c10;
        this.chars[10] = c11;
        this.chars[11] = c12;
        this.chars[12] = c13;
        this.chars[13] = c14;
        return this;
    }

    public Str init15(int c1, int c2, int c3, int c4, int c5, int c6, int c7, int c8, int c9, int c10, int c11, int c12,
            int c13, int c14, int c15) {
        this.chars = new int[15];
        this.length = 15;
        this.chars[0] = c1;
        this.chars[1] = c2;
        this.chars[2] = c3;
        this.chars[3] = c4;
        this.chars[4] = c5;
        this.chars[5] = c6;
        this.chars[6] = c7;
        this.chars[7] = c8;
        this.chars[8] = c9;
        this.chars[9] = c10;
        this.chars[10] = c11;
        this.chars[11] = c12;
        this.chars[12] = c13;
        this.chars[13] = c14;
        this.chars[14] = c15;
        return this;
    }

    public Str init16(int c1, int c2, int c3, int c4, int c5, int c6, int c7, int c8, int c9, int c10, int c11, int c12,
            int c13, int c14, int c15, int c16) {
        this.chars = new int[16];
        this.length = 16;
        this.chars[0] = c1;
        this.chars[1] = c2;
        this.chars[2] = c3;
        this.chars[3] = c4;
        this.chars[4] = c5;
        this.chars[5] = c6;
        this.chars[6] = c7;
        this.chars[7] = c8;
        this.chars[8] = c9;
        this.chars[9] = c10;
        this.chars[10] = c11;
        this.chars[11] = c12;
        this.chars[12] = c13;
        this.chars[13] = c14;
        this.chars[14] = c15;
        this.chars[15] = c16;
        return this;
    }

    public Str init17(int c1, int c2, int c3, int c4, int c5, int c6, int c7, int c8, int c9, int c10, int c11, int c12,
            int c13, int c14, int c15, int c16, int c17) {
        this.chars = new int[17];
        this.length = 17;
        this.chars[0] = c1;
        this.chars[1] = c2;
        this.chars[2] = c3;
        this.chars[3] = c4;
        this.chars[4] = c5;
        this.chars[5] = c6;
        this.chars[6] = c7;
        this.chars[7] = c8;
        this.chars[8] = c9;
        this.chars[9] = c10;
        this.chars[10] = c11;
        this.chars[11] = c12;
        this.chars[12] = c13;
        this.chars[13] = c14;
        this.chars[14] = c15;
        this.chars[15] = c16;
        this.chars[16] = c17;
        return this;
    }

    public Str init18(int c1, int c2, int c3, int c4, int c5, int c6, int c7, int c8, int c9, int c10, int c11, int c12,
            int c13, int c14, int c15, int c16, int c17, int c18) {
        this.chars = new int[18];
        this.length = 18;
        this.chars[0] = c1;
        this.chars[1] = c2;
        this.chars[2] = c3;
        this.chars[3] = c4;
        this.chars[4] = c5;
        this.chars[5] = c6;
        this.chars[6] = c7;
        this.chars[7] = c8;
        this.chars[8] = c9;
        this.chars[9] = c10;
        this.chars[10] = c11;
        this.chars[11] = c12;
        this.chars[12] = c13;
        this.chars[13] = c14;
        this.chars[14] = c15;
        this.chars[15] = c16;
        this.chars[16] = c17;
        this.chars[17] = c18;
        return this;
    }

    public Str init19(int c1, int c2, int c3, int c4, int c5, int c6, int c7, int c8, int c9, int c10, int c11, int c12,
            int c13, int c14, int c15, int c16, int c17, int c18, int c19) {
        this.chars = new int[19];
        this.length = 19;
        this.chars[0] = c1;
        this.chars[1] = c2;
        this.chars[2] = c3;
        this.chars[3] = c4;
        this.chars[4] = c5;
        this.chars[5] = c6;
        this.chars[6] = c7;
        this.chars[7] = c8;
        this.chars[8] = c9;
        this.chars[9] = c10;
        this.chars[10] = c11;
        this.chars[11] = c12;
        this.chars[12] = c13;
        this.chars[13] = c14;
        this.chars[14] = c15;
        this.chars[15] = c16;
        this.chars[16] = c17;
        this.chars[17] = c18;
        this.chars[18] = c19;
        return this;
    }

    public Str init20(int c1, int c2, int c3, int c4, int c5, int c6, int c7, int c8, int c9, int c10, int c11, int c12,
            int c13, int c14, int c15, int c16, int c17, int c18, int c19, int c20) {
        this.chars = new int[20];
        this.length = 20;
        this.chars[0] = c1;
        this.chars[1] = c2;
        this.chars[2] = c3;
        this.chars[3] = c4;
        this.chars[4] = c5;
        this.chars[5] = c6;
        this.chars[6] = c7;
        this.chars[7] = c8;
        this.chars[8] = c9;
        this.chars[9] = c10;
        this.chars[10] = c11;
        this.chars[11] = c12;
        this.chars[12] = c13;
        this.chars[13] = c14;
        this.chars[14] = c15;
        this.chars[15] = c16;
        this.chars[16] = c17;
        this.chars[17] = c18;
        this.chars[18] = c19;
        this.chars[19] = c20;
        return this;
    }

    /**** generated constructors end ****/

    public void print() {
        int i = 0;
        while (i < length) {
            System.out.write(chars[i]);
            i = i + 1;
        }
    }

    public void println() {
        print();
        System.out.write(10); /* \n */
        System.out.flush();
    }

    public Str fromInt(int number) {
        boolean negative = number < 0;
        if (negative) {
            number = -number;
        }

        /* count digits */
        int copyOfNumber = number;
        int digits = 1;
        while (copyOfNumber >= 10) {
            copyOfNumber = copyOfNumber / 10;
            digits = digits + 1;
        }

        length = digits;
        if (negative) {
            length = length + 1;
        }

        chars = new int[length];
        int i = length - 1;
        if (negative) {
            chars[0] = 45;
        }

        while ((!negative && i >= 0) || i > 0) {
            chars[i] = digitToChar(number % 10);
            number = number / 10;
            i = i - 1;
        }
        return this;
    }

    /*
     * Returns an integer if the string depics one. Undefined if the string is not
     * an integer.
     */
    public int toInt() {
        int number = 0;
        int i = 0;
        boolean negative = false;
        if (chars[0] == 45) {
            negative = true;
        }

        while (i < length) {
            int c = charToDigit(chars[i]);
            if (c == -1) {
                return 0;
            }
            number = number * 10 + c;
            i = i + 1;
        }
        if (negative)
            return -number;
        return number;
    }

    public int digitToChar(int digit) {
        return digit + 48;
    }

    public int charToDigit(int character) {
        if (character >= 48 && character <= 57) {
            return character - 48;
        } else {
            return -1;
        }

    }

    /*
     * concats another string after this one and returns a new string with both
     * concatenated
     */
    public Str concat(Str other) {
        Str newString = new Str();
        newString.length = length + other.length;
        newString.chars = new int[newString.length];

        {
            int i = 0;
            while (i < length) {
                newString.chars[i] = chars[i];
                i = i + 1;
            }
        }
        {
            int i = 0;
            while (i < other.length) {
                newString.chars[i + length] = other.chars[i];
                i = i + 1;
            }
        }
        return newString;
    }

    /**
     * returns the first index that character appears or -1 if it does not appear
     */
    public int indexOf(int character) {
        return indexOfAfter(character, -1);
    }

    public int indexOfAfter(int character, int position) {
        int i = position + 1;
        while (i < length) {
            if (chars[i] == character) {
                return i;
            }
            i = i + 1;
        }
        return -1;
    }

    /**
     * Returns the substring between startIndex(inclusive) and endIndex(exclusive)
     */
    public Str substring(int startIndex, int endIndex) {
        Str newString = new Str();
        newString.length = endIndex - startIndex;
        newString.chars = new int[newString.length];

        int i = startIndex;
        while (i < endIndex) {
            newString.chars[i - startIndex] = chars[i];
            i = i + 1;
        }

        return newString;
    }

    public boolean equals(Str other) {
        if (length != other.length) {
            return false;
        }

        int i = 0;
        while (i < length) {
            if (chars[i] != other.chars[i]) {
                return false;
            }
            i = i + 1;
        }
        return true;
    }

}

/* END ../mjtest/tests/exec/lib/Str.java */

