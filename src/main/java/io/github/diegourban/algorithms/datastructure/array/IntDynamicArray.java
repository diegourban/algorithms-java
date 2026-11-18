package io.github.diegourban.algorithms.datastructure.array;

/**
 * A growable list of {@code int} values backed by an {@code int[]}, so values are never boxed.
 * <p>
 * Growth policy: the capacity starts at {@value #INITIAL_CAPACITY}, doubles when an element is added to a full array,
 * and halves when a removal leaves the array a quarter full, but never drops below {@value #INITIAL_CAPACITY}.
 * Shrinking at a quarter (not a half) keeps alternating adds and removes from resizing every time, so every operation
 * is amortised O(1) apart from the index shifts of {@link #add(int, int)} and {@link #remove(int)}.
 */
public class IntDynamicArray {

    /** The capacity of a new array, and the smallest capacity it ever shrinks to. */
    public static final int INITIAL_CAPACITY = 10;

    /** Creates an empty array with capacity {@value #INITIAL_CAPACITY}. */
    public IntDynamicArray() {
        throw new UnsupportedOperationException("not implemented");
    }

    /**
     * Appends a value at the end, doubling the capacity first if the array is full.
     *
     * @param value the value to append
     */
    public void add(final int value) {
        throw new UnsupportedOperationException("not implemented");
    }

    /**
     * Inserts a value at the given index, shifting the value at that index and all later ones one position right.
     * Index {@link #size()} appends.
     *
     * @param index the zero-based index, from {@code 0} to {@link #size()}
     * @param value the value to insert
     * @throws IndexOutOfBoundsException if {@code index} is negative or greater than {@link #size()}
     */
    public void add(final int index, final int value) {
        throw new UnsupportedOperationException("not implemented");
    }

    /**
     * Returns the value at the given index.
     *
     * @param index the zero-based index, from {@code 0} to {@code size() - 1}
     * @return the value at {@code index}
     * @throws IndexOutOfBoundsException if {@code index} is negative or not less than {@link #size()}
     */
    public int get(final int index) {
        throw new UnsupportedOperationException("not implemented");
    }

    /**
     * Replaces the value at the given index.
     *
     * @param index the zero-based index, from {@code 0} to {@code size() - 1}
     * @param value the new value
     * @return the value previously at {@code index}
     * @throws IndexOutOfBoundsException if {@code index} is negative or not less than {@link #size()}
     */
    public int set(final int index, final int value) {
        throw new UnsupportedOperationException("not implemented");
    }

    /**
     * Removes the value at the given index, shifting all later values one position left, then halves the capacity if
     * the array is left a quarter full (never below {@value #INITIAL_CAPACITY}).
     *
     * @param index the zero-based index, from {@code 0} to {@code size() - 1}
     * @return the removed value
     * @throws IndexOutOfBoundsException if {@code index} is negative or not less than {@link #size()}
     */
    public int remove(final int index) {
        throw new UnsupportedOperationException("not implemented");
    }

    /**
     * Returns the number of stored values.
     *
     * @return the number of values
     */
    public int size() {
        throw new UnsupportedOperationException("not implemented");
    }

    /**
     * Tells whether the array holds no values.
     *
     * @return {@code true} if {@link #size()} is {@code 0}
     */
    public boolean isEmpty() {
        throw new UnsupportedOperationException("not implemented");
    }

    /**
     * Returns the length of the backing array, which is at least {@link #size()}. Exposed so the growth policy can
     * be observed and tested.
     *
     * @return the current capacity
     */
    public int capacity() {
        throw new UnsupportedOperationException("not implemented");
    }

    /**
     * Tells whether the array holds the given value.
     *
     * @param value the value to look for
     * @return {@code true} if some index holds {@code value}
     */
    public boolean contains(final int value) {
        throw new UnsupportedOperationException("not implemented");
    }

    /**
     * Returns the index of the first occurrence of the given value.
     *
     * @param value the value to look for
     * @return the lowest index holding {@code value}, or {@code -1} if it is absent
     */
    public int indexOf(final int value) {
        throw new UnsupportedOperationException("not implemented");
    }

    /**
     * Returns the stored values in order, as a new array of length {@link #size()}; changing it does not change this
     * dynamic array.
     *
     * @return a copy of the stored values
     */
    public int[] toArray() {
        throw new UnsupportedOperationException("not implemented");
    }
}
