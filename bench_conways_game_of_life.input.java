/*
 * conways_game_of_life.mj -- Plays Conway's Game of Life.
 *
 * This is a simulator for Conway's Game of Life [1].  To get the maximum fun
 * out of it, you need a video terminal that understands ANSI escape sequences.
 * Then, piping the output of this program through a filter as explained in
 * `hello_world.mj` will show you a fancy animation of the game.  If you cannot
 * get access to a terminal (emulator) that is capable of this, you can set the
 * variable `HAVE_ANSI` near the beginning of `main` to `false`.  Then, the
 * output will not use ANSI escape codes but look rather sad.  You'll probably
 * also want to reduce the iteration count in this case.  Here are all
 * variables that you might want to tweak.
 *
 *  - `HAVE_ANSI` -- whether ANSI escape codes are supported by the terminal
 *  - `LINES` -- number of lines available on the terminal
 *  - `COLUMNS` -- number of columns available on the terminal
 *  - `ITERATIONS` -- number of iterations to play
 *  - `RANDOM_SEED` -- random seed for initializing the game
 *
 * Since MiniJava has no way to suspend, this program deliberately uses an
 * inefficient strategy to print the output (knowing that terminals are
 * *really* slow at interpreting ANSI escape codes).  This means that the
 * program will eat all your CPU while it is running.  The update rate is about
 * alright on my PC but if your computer is much faster or much slower, you
 * might not enjoy the animation very much.
 *
 * [1] https://en.wikipedia.org/wiki/Conway's_Game_of_Life
 *
 */

class CWGOLMain {

	public static void main(String[] args) throws Exception {
		boolean HAVE_ANSI = true;
		int LINES = 24;
		int COLUMNS = 80;
		int ITERATIONS = 50000;
		int RANDOM_SEED = System.in.read();
		Game g = new Game();
		g.init(LINES - 2, COLUMNS / 2 - 1);
		LCG lcg = new LCG();
		lcg.init(RANDOM_SEED);
		int i = 0;
		while (i < g.rows()) {
			int j = 0;
			while (j < g.columns()) {
				if (lcg.next() % 8 == 0) {
					g.set(i, j);
				}
				j = j + 1;
			}
			i = i + 1;
		}
		BoardPrinter bp = new BoardPrinter();
		bp.init();
		if (HAVE_ANSI) {
			bp.fancyInit(g);
		}
		int k = 0;
		while (k < ITERATIONS) {
			if (k == 1 || k + 1 == ITERATIONS) {
				if (HAVE_ANSI) {
					bp.fancyPrintBoard(g, k);
				} else {
					bp.printBoard(g, k);
				}
			}
			g.next();
			k = k + 1;
		}
		if (HAVE_ANSI) {
			bp.fancyFini();
		}
	}

}


class BoardPrinter {

	public void init() {
		_M_digitsBuffer = new int[10];
	}

	public void printBoard(Game game, int iteration) {
		_M_printInt(iteration);
		System.out.println(10);
		_M_printRule(game.columns());
		int i = 0;
		while (i < game.rows()) {
			int j = 0;
			System.out.println(124);
			while (j < game.columns()) {
				if (game.get(i, j)) {
					System.out.println(42);
				} else {
					System.out.println(32);
				}
				j = j + 1;
			}
			System.out.println(124);
			System.out.println(10);
			i = i + 1;
		}
		_M_printRule(game.columns());
		System.out.println(10);
	}

	public void fancyInit(Game game) {
		int k;
		_M_ansiDectcem(false);
		_M_ansiSgr(7, 0);
		_M_ansiEd(2);
		_M_ansiCup(1, 1);
		System.out.println(43);
		k = 0;
		while (k < 2 * game.columns()) {
			System.out.println(45);
			k = k + 1;
		}
		System.out.println(43);
		_M_ansiCup(2 + game.rows(), 1);
		System.out.println(43);
		k = 0;
		while (k < 2 * game.columns()) {
			System.out.println(45);
			k = k + 1;
		}
		System.out.println(43);
		k = 0;
		while (k < game.rows()) {
			_M_ansiCup(2 + k, 1);
			System.out.println(124);
			_M_ansiCup(2 + k, 2 + 2 * game.columns());
			System.out.println(124);
			k = k + 1;
		}
	}

	public void fancyFini() {
		_M_ansiDectcem(true);
		_M_ansiSgr(9, 9);
		_M_ansiEd(2);
		_M_ansiCup(1, 1);
	}

	public void fancyPrintBoard(Game game, int iteration) {
		int i;
		int j;
		_M_ansiCup(1, 10);
		_M_ansiSgr(7, 0);
		_M_printInt(iteration);
		i = 0;
		while (i < game.rows()) {
			j = 0;
			while (j < game.columns()) {
				_M_ansiCup(2 + i, 2 + 2 * j);
				if (game.get(i, j)) {
					_M_ansiSgr(9, 2);
				} else {
					_M_ansiSgr(9, 0);
				}
				System.out.println(32);
				System.out.println(32);
				j = j + 1;
			}
			i = i + 1;
		}
	}

	public int[] _M_digitsBuffer;

	public void _M_printRule(int columns) {
		System.out.println(43);
		while (columns > 0) {
			System.out.println(45);
			columns = columns - 1;
		}
		System.out.println(43);
		System.out.println(10);
	}

	public void _M_ansiSgr(int fg, int bg) {
		_M_ansiCsi();
		System.out.println(51);
		System.out.println(48 + fg);
		System.out.println(109);
		_M_ansiCsi();
		System.out.println(52);
		System.out.println(48 + bg);
		System.out.println(109);
	}

	public void _M_ansiEd(int code) {
		_M_ansiCsi();
		System.out.println(48 + code);
		System.out.println(74);
	}

	public void _M_ansiEl(int code) {
		_M_ansiCsi();
		System.out.println(48 + code);
		System.out.println(75);
	}

	public void _M_ansiCup(int row, int col) {
		_M_ansiCsi();
		_M_printInt(row);
		System.out.println(59);
		_M_printInt(col);
		System.out.println(72);
	}

	public void _M_ansiDectcem(boolean show) {
		_M_ansiCsi();
		System.out.println(63);
		System.out.println(50);
		System.out.println(53);
		if (show) {
			System.out.println(104);
		} else {
			System.out.println(108);
		}
		System.out.println(10);
	}

	public void _M_ansiCsi() {
		System.out.println(27);
		System.out.println(91);
	}

	public void _M_printInt(int n) {
		if (n == 0) {
			System.out.println(48);
			return;
		}
		int i = 0;
		while (n > 0) {
			_M_digitsBuffer[i] = n % 10;
			n = n / 10;
			i = i + 1;
		}
		while (i > 0) {
			System.out.println(48 + _M_digitsBuffer[i - 1]);
			i = i - 1;
		}
	}

}

class Game {

	public void init(int rows, int columns) {
		int size = rows * columns;
		_M_rows = rows;
		_M_columns = columns;
		_M_active_board = new boolean[size];
		_M_passive_board = new boolean[size];
		int idx = 0;
		while (idx < size) {
			_M_active_board[idx] = false;
			_M_passive_board[idx] = false;
			idx = idx + 1;
		}
	}

	public int rows() {
		return _M_rows;
	}

	public int columns() {
		return _M_columns;
	}

	public boolean get(int i, int j) {
		return _M_active_board[_M_getIndex(i, j)];
	}

	public void set(int i, int j) {
		_M_active_board[_M_getIndex(i, j)] = true;
	}

	public void next() {
		int i = 0;
		while (i < _M_rows) {
			int j = 0;
			while (j < _M_columns) {
				int index = _M_getIndex(i, j);
				int neighbours = _M_liveNeighbours(i, j);
				if ((neighbours < 2) || (neighbours > 3)) {
					_M_passive_board[index] = false;
				} else if (neighbours == 3) {
					_M_passive_board[index] = true;
				} else {
					_M_passive_board[index] = _M_active_board[index];
				}
				j = j + 1;
			}
			i = i + 1;
		}
		_M_swapBuffers();
	}

	public int _M_rows;
	public int _M_columns;
	public boolean[] _M_active_board;
	public boolean[] _M_passive_board;

	public int _M_getIndex(int i, int j) {
		i = (_M_rows + i) % _M_rows;
		j = (_M_columns + j) % _M_columns;
		return i * _M_columns + j;
	}

	public int _M_liveNeighbours(int i, int j) {
		int count = 0;
		if (get(i - 1, j - 1)) { count = count + 1; }
		if (get(i - 1, j    )) { count = count + 1; }
		if (get(i - 1, j + 1)) { count = count + 1; }
		if (get(i,     j - 1)) { count = count + 1; }
		if (get(i,     j + 1)) { count = count + 1; }
		if (get(i + 1, j - 1)) { count = count + 1; }
		if (get(i + 1, j    )) { count = count + 1; }
		if (get(i + 1, j + 1)) { count = count + 1; }
		return count;
	}

	public void _M_swapBuffers() {
		boolean[] temp = _M_active_board;
		_M_active_board = _M_passive_board;
		_M_passive_board = temp;
	}

}


class LCG {

	public void init(int seed) {
		_M_a = 5;
		_M_c = 11;
		_M_m = 7901;
		_M_state = seed % _M_m;
	}

	public int next() {
		int prev = _M_state;
		_M_state = (_M_a * prev + _M_c) % _M_m;
		return prev;
	}

	public int _M_a;
	public int _M_c;
	public int _M_m;
	public int _M_state;

}
