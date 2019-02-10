/* This benchmark demonstrates the capabilities of our Linear Scan allocator
 * We manage to keep all variables in registers.
 */
class CompraktLinearScan {
    public static void main(String[] args) throws Exception {

        IntArrayUtils utils = new IntArrayUtils();
    
        int elem2 = utils.readIntFromStdin();
        int elem1 = utils.readIntFromStdin();
        int elem0 = utils.readIntFromStdin();

        int shift_on = utils.readIntFromStdin(); 

        int c = 0;
        while (c < 1200000000) {
            c = c + 1;
            /* Mix in some of our bitshifting tricks :P */
            if (shift_on == 1) {
                int temp = elem0;
                elem0 = elem1 + 8;
                elem1 = elem2 + 16;
                elem2 = temp + 32;
            } else {
                int temp = elem0;
                elem0 = elem1 - 4;
                elem1 = elem2 - 8;
                elem2 = temp - 16;
            }
        }

        System.out.println(elem0);
        System.out.println(elem1);
        System.out.println(elem2);

    }
}


/* BEGIN ../mjtest/tests/exec/lib/IntArrayUtils.java */


/* 
 * Utils to read int arrays from stdin and output them back to stdout. 
 *
 * Methods using System.in.read() throw Exception to be valid Java code.
 * java.io.IOException is the only exception thrown, but that is not a valid MiniJava IDENT.
 */
class IntArrayUtils {
    /* Reads a line as an int from stdin */
    public int readIntFromStdin() throws Exception {
        int result = 0;
        int c = System.in.read();
        boolean negative = false;

        if (c == 45) { /* - */
            negative = true;
            c = System.in.read();
        }

        while (c != 10) { /* until newline */
            if (c > 57 || c < 48) {
                /* ignore non-digits */
            } else {
                int digit = c - 48; /* convert to digit */
                result = result * 10 + digit;
            }
            c = System.in.read();
        }

        /* output the final int */
        if (negative) {
            return -result;
        }
        return result;
    }

    /*
     * Reads a complete int array from stdin. First asks how many elements the array
     * has.
     */
    public IntArray readIntArrayFromStdin() throws Exception {
        System.out.write(110); /* n */
        System.out.write(58); /* : */
        System.out.write(32); /* [space] */
        System.out.flush();
        int n = readIntFromStdin();
        System.out.println(n);

        int[] result = new int[n];

        int i = 0;
        while (i < n) {
            result[i] = readIntFromStdin();
            i = i + 1;
        }

        IntArray resultObj = new IntArray();
        resultObj.array = result;
        resultObj.length = n;
        return resultObj;
    }

    /* Prints the int array back to stdout. */
    public void printIntArray(IntArray array) {
        int i = 0;
        while (i < array.length) {
            System.out.println(array.array[i]);
            i = i + 1;
        }
    }
}

/*
 * Wrapper class for an int array. As MiniJava arrays do not have a length
 * attribute, we need to store it separately.
 */
class IntArray {
    public int[] array;
    public int length;

    public IntArray init(int[] array, int length) {
        this.array = array;
        this.length = length;
        return this;
    }

    public int get(int index) {
        return array[index];
    }

    public void set(int index, int value) {
        array[index] = value;
    }
}

/* END ../mjtest/tests/exec/lib/IntArrayUtils.java */

