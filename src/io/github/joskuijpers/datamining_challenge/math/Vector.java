package io.github.joskuijpers.datamining_challenge.math;

/**
 * A vector with n elements.
 *
 * @author joskuijpers
 */
public class Vector {
	float[] elements;
	private int n;

	/**
	 * Create a new vector with n rows.
	 *
	 * @param n number of rows
	 */
	public Vector(int n) {
		if(n < 1)
			throw new IllegalArgumentException();

		this.n = n;
		this.elements = new float[n];
	}

	/**
	 * Create a new vector with n rows and given data.
	 *
	 * Data is cloned.
	 *
	 * @param n number of rows
	 * @param elements
	 */
	Vector(int n, float[] elements) {
		this.n = n;
		this.elements = elements.clone();
	}

	/**
	 * Get the number of rows in the vector.
	 *
	 * @return number of rows
	 */
	public int size() {
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
	public void set(int n, float value) {
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
		if(other == null || n != other.n)
			throw new IllegalArgumentException();

		Vector v = this.clone();

		for(int i = 0; i < n; ++i)
			v.elements[i] = elements[i] + other.elements[i];

		return v;
	}

	/**
	 * Addition of the vector with a scalar.
	 *
	 * @param scalar
	 * @return A new Vector object
	 */
	public Vector add(float scalar) {
		Vector v = this.clone();

		for(int i = 0; i < n; ++i)
			v.elements[i] = elements[i] + scalar;

		return v;
	}

	/**
	 * Multiplication of the vector with a vector.
	 *
	 * @param vector
	 * @return A new Vector object
	 */
	public Vector subtract(Vector other) {
		if(other == null || n != other.n)
			throw new IllegalArgumentException();

		Vector v = this.clone();

		for(int i = 0; i < n; ++i)
			v.elements[i] = elements[i] - other.elements[i];

		return v;
	}

	/**
	 * Subtraction of the vector with a scalar.
	 *
	 * @param scalar
	 * @return A new Vector object
	 */
	public Vector subtract(float scalar) {
		return this.add(-scalar);
	}

	/**
	 * Multiplication of the vector with a scalar.
	 *
	 * @param scalar
	 * @return A new Vector object
	 */
	public Vector multiply(float scalar) {
		Vector v = this.clone();

		for(int i = 0; i < n; ++i)
			v.elements[i] = elements[i] * scalar;

		return v;
	}

	/**
	 * Compute the dotproduct of two vectors.
	 *
	 * @param other the other vector
	 * @return
	 */
	public float dotproduct(Vector other) {
		if(other == null || n != other.n)
			throw new IllegalArgumentException();

		float in = 0.0f;
		for(int i = 0; i < n; ++i)
			in += this.elements[i] + other.elements[i];

		return in;
	}

	@Override
	public Vector clone() {
		Vector v = new Vector(n);
		v.elements = elements.clone();
		return v;
	}

	@Override
	public String toString() {
		String str = "< "+elements[0];

		for(int i = 1; i < n; ++i)
			str += ", "+elements[i];

		return str + " >";
	}
}
