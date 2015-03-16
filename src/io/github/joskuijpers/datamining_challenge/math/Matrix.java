package io.github.joskuijpers.datamining_challenge.math;

/**
 * A matrix with mxn elements.
 *
 * Indexes are 0-(n-1)
 *
 * @author joskuijpers
 */
public class Matrix {
	public float[][] elements;
	public int m, n;

	/**
	 * Create an empty matrix with mxn size.
	 *
	 * @param m
	 *            Number of rows
	 * @param n
	 *            Number of columns
	 */
	public Matrix(int m, int n) {
		if (n < 1 || m < 1)
			throw new IllegalArgumentException();

		this.m = m;
		this.n = n;

		this.elements = new float[m][n];
	}

	/**
	 * Create a new matrix with m rows, m columns, and given data.
	 *
	 * Data is cloned.
	 *
	 * @param m number of rows
	 * @param n number of columns
	 * @param elements
	 */
	public Matrix(int m, int n, float[][] elements) {
		if(m < 1 || n < 1 || elements.length != m || elements[0].length != n)
			throw new IllegalArgumentException();

		this.m = m;
		this.n = n;
		this.elements = elements.clone();
	}

	/**
	 * Create an I matrix with given size.
	 *
	 * @param n
	 * @return
	 */
	public static Matrix eye(int n) {
		if(n < 1)
			throw new IllegalArgumentException();

		Matrix mat = new Matrix(n,n);

		for(int i = 0; i < n; ++i) {
			for(int j = 0; j < n; ++j) {
				mat.elements[i][j] = 1;
			}
		}

		return mat;
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
	 * Set all elements to specified value.
	 *
	 * @param value
	 *            the value
	 */
	public void init(float value) {
		for (int i = 0; i < m; ++i) {
			for (int j = 0; j < n; ++j) {
				elements[i][j] = value;
			}
		}
	}

	/**
	 * Get a value from the matrix.
	 *
	 * @param m
	 *            Row
	 * @param n
	 *            Columns
	 * @return value
	 */
	public Float get(int m, int n) {
		if (m < 0 || m >= this.m || n < 0 || n >= this.n)
			throw new IllegalArgumentException();

		return elements[m][n];
	}

	/**
	 * Set a value in the matrix.
	 *
	 * @param m
	 *            Row
	 * @param n
	 *            Columns
	 * @return value
	 */
	public void set(int m, int n, float value) {
		if (m < 0 || m >= this.m || n < 0 || n >= this.n)
			throw new IllegalArgumentException();

		elements[m][n] = value;
	}

	/**
	 * Get the vector for specified column.
	 *
	 * @param n
	 *            the column index
	 * @return Vector
	 */
	public Vector getColumn(int n) {
		if (n < 0 || n >= this.n)
			throw new IllegalArgumentException();

		Vector v = new Vector(m);
		for (int i = 0; i < m; ++i)
			v.elements[i] = this.elements[i][n];

		return v;
	}

	/**
	 * Set the vector for specified column.
	 *
	 * @param n
	 *            the column index
	 * @param column
	 *            the column
	 */
	public void setColumn(int n, Vector column) {
		if (column == null || n < 0 || n >= this.n || column.size() != m)
			throw new IllegalArgumentException();

		for (int i = 0; i < m; ++i)
			this.elements[i][n] = column.elements[i];
	}

	/**
	 * Set the vector for specified row.
	 *
	 * @param m
	 *            the row index
	 * @return Vector
	 */
	public Vector getRow(int m) {
		if (m < 0 || m >= this.m)
			throw new IllegalArgumentException();

		return new Vector(n, elements[m]);
	}

	/**
	 * Set the vector for specified row.
	 *
	 * @param n
	 *            the row index
	 * @param row
	 *            the row data
	 */
	public void setRow(int m, Vector row) {
		if (row == null || m < 0 || n >= this.m || row.size() != this.n)
			throw new IllegalArgumentException();

		for (int i = 0; i < n; ++i)
			this.elements[m][i] = row.elements[i];
	}

	/**
	 * Substract a matrix from this one.
	 *
	 * Cloned. Original is not changed.
	 *
	 * @param other
	 *            Matrix to subtract
	 * @return A cloned matrix with the result
	 */
	public Matrix subtract(Matrix other) {
		if (other == null || other.n != n || other.m != m)
			throw new IllegalArgumentException();

		Matrix mat = new Matrix(m, n);

		for (int i = 0; i < m; ++i) {
			for (int j = 0; j < n; ++j) {
				mat.elements[i][j] = this.elements[i][j] - other.elements[i][j];
			}
		}

		return mat;
	}

	/**
	 * Add a matrix to this one.
	 *
	 * Cloned. Original is not changed.
	 *
	 * @param other
	 *            Matrix to add
	 * @return A cloned matrix with the result
	 */
	public Matrix add(Matrix other) {
		if (other == null || other.n != n || other.m != m)
			throw new IllegalArgumentException();

		Matrix mat = new Matrix(m, n);

		for (int i = 0; i < m; ++i) {
			for (int j = 0; j < n; ++j) {
				mat.elements[i][j] = this.elements[i][j] + other.elements[i][j];
			}
		}

		return mat;
	}

	/**
	 * Add a matrix to this one.
	 *
	 * @param other
	 *            Matrix to add
	 * @return A cloned matrix with the result
	 */
	public Matrix addNoClone(Matrix other) {
		if (other == null || other.n != n || other.m != m)
			throw new IllegalArgumentException();

		for (int i = 0; i < m; ++i) {
			for (int j = 0; j < n; ++j) {
				this.elements[i][j] += other.elements[i][j];
			}
		}

		return this;
	}

	/**
	 * Multiply a matrix with another matrix.
	 *
	 * mxp * pxn = mxn
	 *
	 * @param other matrix to multiply with
	 * @return a new Matrix
	 */
	public Matrix multiply(Matrix other) {
		if(other == null || this.n != other.m)
			throw new IllegalArgumentException();

		Matrix ret = new Matrix(this.m,other.n);

		for(int i = 0; i < this.m; ++i) {
			for(int j = 0; j < other.n; ++j) {
				ret.elements[i][j] = this.getRow(i).dotproduct(other.getColumn(j));
			}
		}

		return ret;
	}

	/**
	 * Multiply elementes with a scalar.
	 *
	 * @param scalar
	 * @return a new matrix
	 */
	public Matrix multiply(float scalar) {
		Matrix ret = this.clone();

		for(int i = 0; i < this.m; ++i) {
			for(int j = 0; j < this.n; ++j) {
				ret.elements[i][j] *= scalar;
			}
		}

		return ret;
	}

	/**
	 * Transpose the matrix.
	 *
	 * @return transposed matrix
	 */
	public Matrix transpose() {
		Matrix mat = new Matrix(this.n,this.n);

		for(int i = 0; i < this.m; ++i) {
			for(int j = 0; j < this.n; ++j) {
				mat.elements[j][i] = elements[i][j];
			}
		}

		return mat;
	}

	@Override
	public Matrix clone() {
		Matrix m = new Matrix(this.m, this.n);
		m.elements = elements.clone();
		return m;
	}
}
