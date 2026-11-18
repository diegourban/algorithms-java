package io.github.diegourban.algorithms.datastructure.array;

/**
 * A growable list of values of any type, backed by an array that is replaced by a larger or smaller copy as values
 * are added and removed.
 * <p>
 * Growth policy: the capacity starts at {@value #INITIAL_CAPACITY}, doubles when an element is added to a full array,
 * and halves when a removal leaves the array less than a quarter full, but never drops below
 * {@value #INITIAL_CAPACITY}. Shrinking at a quarter (not a half) keeps alternating appends and removals from resizing
 * every time, so every operation is amortised O(1) apart from the index shifts of {@link #pop(int)}.
 * <p>
 * The backing store is an {@code Object[]} cast to {@code T[]}, because Java cannot create an array of a type
 * parameter; the cast is safe because only {@code T} values are ever stored in it.
 *
 * @param <T> the type of the stored values
 */
public class DynamicArray<T> {

    /**
     * The capacity of a new array, and the smallest capacity it ever shrinks to.
     */
    public static final int INITIAL_CAPACITY = 10;

    private int size;
    private int capacity;
    private T[] fixedArray;

    /**
     * Creates an empty array with capacity {@value #INITIAL_CAPACITY}.
     */
    public DynamicArray() {
        this.capacity = INITIAL_CAPACITY;
        this.size = 0;
        this.fixedArray = (T[]) new Object[capacity];
    }

    /**
     * Appends a value at the end, doubling the capacity first if the array is full.
     *
     * @param value the value to append
     */
    public void append(final T value) {
        if (this.size == this.capacity) {
            this.resize(this.capacity * 2);
        }
        this.fixedArray[this.size] = value;
        this.size += 1;
    }

    /**
     * Returns the value at the given index.
     *
     * @param index the zero-based index, from {@code 0} to {@code size() - 1}
     * @return the value at {@code index}
     * @throws IndexOutOfBoundsException if {@code index} is negative or not less than {@link #size()}
     */
    public T get(final int index) {
        if (index < 0 || index >= this.size) {
            throw new ArrayIndexOutOfBoundsException("Index: " + index + ", Size: " + this.size);
        }
        return this.fixedArray[index];
    }

    /**
     * Replaces the value at the given index.
     *
     * @param index the zero-based index, from {@code 0} to {@code size() - 1}
     * @param value the new value
     * @throws IndexOutOfBoundsException if {@code index} is negative or not less than {@link #size()}
     */
    public void set(final int index, final T value) {
        if (index < 0 || index >= this.size) {
            throw new ArrayIndexOutOfBoundsException("Index: " + index + ", Size: " + this.size);
        }
        this.fixedArray[index] = value;
    }

    /**
     * Returns the number of stored values.
     *
     * @return the number of values
     */
    public int size() {
        return this.size;
    }

    /**
     * Removes the last value, then halves the capacity if the array is left less than a quarter full (never below
     * {@value #INITIAL_CAPACITY}).
     *
     * @throws IndexOutOfBoundsException if the array is empty
     */
    public void popBack() {
        if (this.size == 0) {
            throw new ArrayIndexOutOfBoundsException("Index: 0, Size: " + this.size);
        }
        this.size -= 1;
        this.fixedArray[this.size] = null; // release the removed value so it can be garbage-collected
        if ((double) this.size / this.capacity < 0.25 && this.capacity > INITIAL_CAPACITY) {
            this.resize(this.capacity / 2);
        }
    }

    /**
     * Removes the value at the given index, shifting all later values one position left, then shrinks like
     * {@link #popBack()}.
     *
     * @param index the zero-based index, from {@code 0} to {@code size() - 1}
     * @return the removed value
     * @throws IndexOutOfBoundsException if {@code index} is negative or not less than {@link #size()}
     */
    public T pop(final int index) {
        if (index < 0 || index >= this.size) {
            throw new ArrayIndexOutOfBoundsException("Index: " + index + ", Size: " + this.size);
        }
        final T value = this.fixedArray[index];
        for (int i = index; i < this.size - 1; i++) {
            this.fixedArray[i] = this.fixedArray[i + 1];
        }
        this.popBack();
        return value;
    }

    private void resize(final int newCapacity) {
        final T[] newArray = (T[]) new Object[newCapacity];
        System.arraycopy(this.fixedArray, 0, newArray, 0, this.size);
        this.fixedArray = newArray;
        this.capacity = newCapacity;
    }
}
