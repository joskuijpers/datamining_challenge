package io.github.joskuijpers.datamining_challenge.math;

/**
 * A matrix with mxn elements.
 *
 * @author joskuijpers
 */
public class Matrix {
	private Float[][] elements;
	private int m, n;

	public Matrix(int m, int n) {
		this.m = m;
		this.n = n;

		this.elements = new Float[m][n];
	}

	/**
	 * Get the number of rows in the vector.
	 *
	 * @return number of rows
	 */
	public int getNumberOfRows() {
		return m;
	}

	/**
	 * Get the number of columns in the vector.
	 *
	 * @return number of columns
	 */
	public int getNumberOfColumns() {
		return n;
	}

	/**
	 * Get a value from the matrix.
	 *
	 * @param m Row
	 * @param n Columns
	 * @return value
	 */
	public Float get(int m, int n) {
		if(m < 0 || m >= this.m || n < 0 || n >= this.n)
			throw new IllegalArgumentException();

		return elements[m][n];
	}

	/**
	 * Set a value in the matrix.
	 *
	 * @param m Row
	 * @param n Columns
	 * @return value
	 */
	public void set(int m, int v, Float value) {
		if(m < 0 || m >= this.m || n < 0 || n >= this.n)
			throw new IllegalArgumentException();

		elements[m][n] = value;
	}

	@Override
	public Matrix clone() {
		Matrix m = new Matrix(this.m,this.n);
		m.elements = elements.clone();
		return m;
	}
}
