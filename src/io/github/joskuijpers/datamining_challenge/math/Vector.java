package io.github.joskuijpers.datamining_challenge.math;

/**
 * A vector with n elements.
 *
 * @author joskuijpers
 */
public class Vector {
	private Float[] elements;
	private int n;

	/**
	 * Create a new vector with n rows.
	 *
	 * @param n number of rows
	 */
	public Vector(int n) {
		this.n = n;
		this.elements = new Float[n];
	}

	/**
	 * Get the number of rows in the vector.
	 *
	 * @return number of rows
	 */
	public int getNumberOfRows() {
		return n;
	}

	/**
	 * Get a value from the vector.
	 *
	 * @param n Row
	 * @return value
	 */
	public Float get(int n) {
		if(n < 0 || n >= this.n)
			throw new IllegalArgumentException();

		return elements[n];
	}

	/**
	 * Set a value in the vector.
	 *
	 * @param n Row
	 * @return value
	 */
	public void set(int n, Float value) {
		if(n < 0 || n >= this.n)
			throw new IllegalArgumentException();

		elements[n] = value;
	}

	/**
	 * Multiplication of the vector with a vectir.
	 *
	 * @param vector
	 * @return A new Vector object
	 */
	public Vector add(Vector other) {
		if(n != other.n)
			throw new IllegalArgumentException();

		Vector v = this.clone();

		for(int i = 0; i < n; ++i)
			v.elements[i] = v.elements[i] + other.elements[i];

		return v;
	}

	/**
	 * Addition of the vector with a scalar.
	 *
	 * @param scalar
	 * @return A new Vector object
	 */
	public Vector add(Float scalar) {
		Vector v = this.clone();

		for(int i = 0; i < n; ++i)
			v.elements[i] = v.elements[i] + scalar;

		return v;
	}

	/**
	 * Multiplication of the vector with a vector.
	 *
	 * @param vector
	 * @return A new Vector object
	 */
	public Vector subtract(Vector other) {
		if(n != other.n)
			throw new IllegalArgumentException();

		Vector v = this.clone();

		for(int i = 0; i < n; ++i)
			v.elements[i] = v.elements[i] - other.elements[i];

		return v;
	}

	/**
	 * Subtraction of the vector with a scalar.
	 *
	 * @param scalar
	 * @return A new Vector object
	 */
	public Vector subtract(Float scalar) {
		return this.add(-scalar);
	}

	/**
	 * Multiplication of the vector with a scalar.
	 *
	 * @param scalar
	 * @return A new Vector object
	 */
	public Vector multiply(Float scalar) {
		Vector v = this.clone();

		for(int i = 0; i < n; ++i)
			v.elements[i] = v.elements[i] * scalar;

		return v;
	}

	@Override
	public Vector clone() {
		Vector v = new Vector(n);
		v.elements = elements.clone();
		return v;
	}
}
