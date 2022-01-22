package fgdiCalculator.calc;

public class Modes {
	public enum Operations {

		NULL(""), BELL_NUMBER("Bellzahl"), STIRLING_NUMBER("Stirlingzahl"), BINOMIAL_COEFFICIENT("Binomialkoeffizient"),
		FACTORIAL("Faktultaet"), FUNCTIONS("Abbildungen"), ABOUT("About");

		private String name;

		private Operations(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	private static long[][] stirlingPool;
	private static long[][] binomialPool;
	private static long[] factorialPool;
	private static long[] bellPool;

	public static void initPools() {
		final int size = 100;
		initStirlingPool(size);
		initBinomialPool(size);
		initFactorialPool(size);
		initBellPool(size);
	}

	// creates the 2D array for the Stirling numbers
	private static void initStirlingPool(int size) {
		stirlingPool = new long[size][];
		// {n k} requires that n >= k
		for (int i = 0; i < stirlingPool.length; i++) {
			stirlingPool[i] = new long[i + 1];
		}
	}

	// creates the 2D array for the binomial coefficients
	private static void initBinomialPool(int size) {
		binomialPool = new long[size][];
		// (n k) requires that n >= k
		for (int i = 0; i < binomialPool.length; i++) {
			binomialPool[i] = new long[i + 1];
		}
	}

	// creates the array for the factorials
	private static void initFactorialPool(int size) {
		factorialPool = new long[size];
	}

	// creates the array for the Bell numbers
	private static void initBellPool(int size) {
		bellPool = new long[size];
	}

	/**
	 * calculates the desired 2nd Stirling Number of n over k.
	 * 
	 * @param n - A number of type long > 0.
	 * @param k - A number of type long <= n.
	 * @return long: The desired Stirling Number.
	 * @throws NumberException if n or k are not valid inputs.
	 */
	public static long calcStirling(long n, long k) throws NumberException {
		if (n < 1 || k < 1) {
			throw new NumberException("Input not valid");
		} else if (n < k) {
			throw new NumberException("n smaller than k");
		}
		if (n < stirlingPool.length && k < stirlingPool[(int) n].length) {
			int nInt = (int) n;
			int kInt = (int) k;
			if (stirlingPool[nInt][kInt] == 0) {
				if (nInt == kInt || kInt == 1) {
					stirlingPool[nInt][kInt] = 1;
				} else if (kInt == 2) {
					stirlingPool[nInt][kInt] = (long) Math.pow(2, nInt - 1) - 1;
				} else {
					stirlingPool[nInt][kInt] = calcStirling(nInt - 1, kInt - 1) + kInt * calcStirling(nInt - 1, kInt);
				}
			}
			return stirlingPool[nInt][kInt];
		} else {
			if (n == k || k == 1) {
				return 1;
			} else if (k == 2) {
				return (long) Math.pow(2, n - 1) - 1;
			} else {
				return calcStirling(n - 1, k - 1) + k * calcStirling(n - 1, k);
			}
		}

	}

	/**
	 * calculates the desired Bell number
	 * 
	 * @param n - Number to which the Bell number is to be calculated.
	 * @return long: The desired Bell number of n.
	 * @throws NumberException if n is not a valid input.
	 */
	public static long calcBell(long n) throws NumberException {
		long summe = 0;
		if (n < 1) {
			throw new NumberException("n too small");
		}
		if (n < bellPool.length) {
			if (bellPool[(int) n] == 0) {
				for (int i = 1; i <= n; i++) {
					summe += calcStirling(n, i);
					bellPool[i] = summe;
				}
				bellPool[(int) n] = summe;
			}
			return bellPool[(int) n];
		} else {
			for (int i = 1; i <= n; i++) {
				summe += calcStirling(n, i);
			}
			return summe;
		}

	}

	/**
	 * Calculates a binomial coefficient of n over k.
	 * 
	 * @param n - A number of type long > 0.
	 * @param k - A number of type long <= n.
	 * @return long: The desired binomial coefficient.
	 * @throws NumberException if n or k are not valid inputs.
	 */
	public static long calcBinomial(long n, long k) throws NumberException {
		if (n < 0 || k < 0) {
			throw new NumberException("Input not valid");
		} else if (n < k) {
			throw new NumberException("n smaller than k");
		}
		if (n < binomialPool.length && k < binomialPool[(int) n].length) {
			int nInt = (int) n;
			int kInt = (int) k;
			if (binomialPool[nInt][kInt] == 0) {
				if (kInt == 1) {
					binomialPool[nInt][kInt] = nInt;
				} else if (nInt == kInt || kInt == 0) {
					binomialPool[nInt][kInt] = 1;
				} else {
					binomialPool[nInt][kInt] = (calcFactorial(nInt)
							/ (calcFactorial(kInt) * calcFactorial(nInt - kInt)));
				}
			}
			return binomialPool[nInt][kInt];
		} else {
			if (k == 1) {
				return n;
			} else if (n == k || k == 0) {
				return 1;
			} else {
				return (calcFactorial(n) / (calcFactorial(k) * calcFactorial(n - k)));
			}
		}

	}

	/**
	 * Calculates the factorial of n.
	 * 
	 * @param n - Factorial of which n is to be calculated.
	 * @return long: the desired factorial
	 * @throws NumberException if n is not a valid input.
	 */
	public static long calcFactorial(long n) throws NumberException {
		if (n < 0) {
			throw new NumberException("Number not valid");
		}
		if (n < factorialPool.length) {
			int nInt = (int) n;
			if (factorialPool[nInt] == 0) {
				if (nInt <= 2) {
					factorialPool[nInt] = nInt;
				} else {
					factorialPool[nInt] = nInt * calcFactorial(nInt - 1);
				}
			}
			return factorialPool[nInt];
		} else {
			return n * calcFactorial(n - 1);
		}

	}

	/**
	 * Calculates the number of functions from a set of n elements to a set of m
	 * elements.
	 * 
	 * @param n - cardinality of the origin set
	 * @param k - cardinality of the destination / image set
	 * @return long[]: The number of functions: [0]: injective, [1]: surjective,
	 *         [2]: bijective, [3] all functions.
	 * @throws NumberException if n or k are not valid inputs.
	 */
	public static long[] calcFunctions(int n, int k) throws NumberException {
		long[] results = new long[4];

		// All functions
		results[3] = (int) Math.pow(k, n);

		if (n > k) {
			// injective:
			results[0] = 0;

			// surjective:
			results[1] = calcStirling(n, k) * calcFactorial(k);

			// bijektive:
			results[2] = 0;
		} else if (n < k) {
			// injective:
			results[0] = calcBinomial(k, n) * calcFactorial(n);

			// surjective:
			results[1] = 0;

			// bijektive:
			results[2] = 0;
		} else if (n == k) {
			// injective
			results[0] = calcBinomial(k, n) * calcFactorial(n);

			// surjective:
			results[1] = calcStirling(n, k) * calcFactorial(k);

			// bijektive:
			results[2] = calcFactorial(n);
		}

		// ERROR
		else {
			for (int i = 0; i < results.length; i++) {
				results[i] = 0;
			}
		}

		return results;
	}

}
