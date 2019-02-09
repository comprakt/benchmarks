class CompraktBenchmarkArithmetics {

    public void foldingOpportunity(int[] x, int c) {
        /* if any of the conditions matches, the others are automatically true */
        if (x[c] / 1024 >= 1) {
            x[c] = 1024;
        }
        if (x[c] / 512 >= 1) {
            x[c] = 512;
        }
        if (x[c] / 256 >= 1) {
            x[c] = 256;
        }
        if (x[c] / 128 >= 1) {
            x[c] = 128;
        }
        if (x[c] / 64 >= 1) {
            x[c] = 64;
        }
        if (x[c] / 32 >= 1) {
            x[c] = 32;
        }
        if (x[c] / 16 >= 1) {
            x[c] = 16;
        }
        if (x[c] / 8 >= 1) {
            x[c] = 8;
        }
        if (x[c] / 4 >= 1) {
            x[c] = 4;
        }
        if (x[c] / 2 >= 1) {
            x[c] = 2;
        }
    }

    public static void main(String[] args) {

        CompraktBenchmarkArithmetics bench = new CompraktBenchmarkArithmetics();

        int numInts = 1024 * 1024 * 6;
        int[] x = new int[numInts];

        {
            int c = 0;
            while (c < numInts) {
                x[c] = c;
                c = c + 1;
            }
        }
        {
            int c = 0;
            while (c < numInts) {
                bench.foldthis(x, c);
                c = c + (64 / 4); /* in cache line stdie */
            }
        }

    }

}

