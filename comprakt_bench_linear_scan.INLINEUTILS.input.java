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

