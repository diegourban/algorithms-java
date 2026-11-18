package io.github.diegourban.algorithms.datastructure.array;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.Test;

class IntDynamicArrayTest {

    private static IntDynamicArray of(final int... values) {
        final IntDynamicArray array = new IntDynamicArray();
        for (final int value : values) {
            array.add(value);
        }
        return array;
    }

    @Test
    void startsEmptyWithTheInitialCapacity() {
        final IntDynamicArray array = new IntDynamicArray();

        assertThat(array.isEmpty()).isTrue();
        assertThat(array.size()).isZero();
        assertThat(array.capacity()).isEqualTo(IntDynamicArray.INITIAL_CAPACITY);
        assertThat(array.toArray()).isEmpty();
    }

    @Test
    void appendsInOrder() {
        final IntDynamicArray array = of(3, 1, 2);

        assertThat(array.size()).isEqualTo(3);
        assertThat(array.isEmpty()).isFalse();
        assertThat(array.get(0)).isEqualTo(3);
        assertThat(array.get(2)).isEqualTo(2);
        assertThat(array.toArray()).containsExactly(3, 1, 2);
    }

    @Test
    void insertsAtTheStartMiddleAndEnd() {
        final IntDynamicArray array = of(2, 4);

        array.add(0, 1);
        array.add(2, 3);
        array.add(4, 5);

        assertThat(array.toArray()).containsExactly(1, 2, 3, 4, 5);
    }

    @Test
    void setReplacesAndReturnsThePreviousValue() {
        final IntDynamicArray array = of(1, 2, 3);

        assertThat(array.set(1, 20)).isEqualTo(2);
        assertThat(array.toArray()).containsExactly(1, 20, 3);
    }

    @Test
    void removeShiftsLaterValuesAndReturnsTheRemovedOne() {
        final IntDynamicArray array = of(1, 2, 3, 4);

        assertThat(array.remove(1)).isEqualTo(2);
        assertThat(array.remove(2)).isEqualTo(4);
        assertThat(array.remove(0)).isEqualTo(1);
        assertThat(array.toArray()).containsExactly(3);
    }

    @Test
    void findsValues() {
        final IntDynamicArray array = of(5, 7, 5);

        assertThat(array.contains(7)).isTrue();
        assertThat(array.contains(9)).isFalse();
        assertThat(array.indexOf(5)).isZero();
        assertThat(array.indexOf(9)).isEqualTo(-1);
    }

    @Test
    void toArrayReturnsACopy() {
        final IntDynamicArray array = of(1, 2);

        array.toArray()[0] = 99;

        assertThat(array.get(0)).isEqualTo(1);
    }

    @Test
    void doublesTheCapacityWhenFull() {
        final IntDynamicArray array = new IntDynamicArray();
        for (int i = 0; i < IntDynamicArray.INITIAL_CAPACITY; i++) {
            array.add(i);
        }
        assertThat(array.capacity()).isEqualTo(10);

        array.add(10);
        assertThat(array.capacity()).isEqualTo(20);

        for (int i = 11; i <= 20; i++) {
            array.add(i);
        }
        assertThat(array.size()).isEqualTo(21);
        assertThat(array.capacity()).isEqualTo(40);
    }

    @Test
    void halvesTheCapacityWhenAQuarterFullButNotBelowTheInitialCapacity() {
        final IntDynamicArray array = new IntDynamicArray();
        for (int i = 0; i < 21; i++) {
            array.add(i);
        }
        assertThat(array.capacity()).isEqualTo(40);

        while (array.size() > 11) {
            array.remove(array.size() - 1);
        }
        assertThat(array.capacity()).as("11 of 40 is above a quarter").isEqualTo(40);

        array.remove(array.size() - 1);
        assertThat(array.size()).isEqualTo(10);
        assertThat(array.capacity()).as("10 of 40 is a quarter: halve").isEqualTo(20);

        while (array.size() > 5) {
            array.remove(array.size() - 1);
        }
        assertThat(array.capacity()).as("5 of 20 is a quarter: halve").isEqualTo(10);

        while (!array.isEmpty()) {
            array.remove(0);
        }
        assertThat(array.capacity()).as("never below the initial capacity").isEqualTo(10);
    }

    @Test
    void rejectsIndexesOutsideTheStoredValues() {
        final IntDynamicArray array = of(1, 2);

        assertThatExceptionOfType(IndexOutOfBoundsException.class).isThrownBy(() -> array.get(2));
        assertThatExceptionOfType(IndexOutOfBoundsException.class).isThrownBy(() -> array.get(-1));
        assertThatExceptionOfType(IndexOutOfBoundsException.class).isThrownBy(() -> array.set(2, 0));
        assertThatExceptionOfType(IndexOutOfBoundsException.class).isThrownBy(() -> array.remove(2));
        assertThatExceptionOfType(IndexOutOfBoundsException.class).isThrownBy(() -> array.add(3, 0));
        assertThatExceptionOfType(IndexOutOfBoundsException.class).isThrownBy(() -> array.add(-1, 0));
        assertThat(array.toArray()).containsExactly(1, 2);
    }

    @Test
    void rejectsAccessToAnEmptyArray() {
        final IntDynamicArray array = new IntDynamicArray();

        assertThatExceptionOfType(IndexOutOfBoundsException.class).isThrownBy(() -> array.get(0));
        assertThatExceptionOfType(IndexOutOfBoundsException.class).isThrownBy(() -> array.remove(0));
    }
}
