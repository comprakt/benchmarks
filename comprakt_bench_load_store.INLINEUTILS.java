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