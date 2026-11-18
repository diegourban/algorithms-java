package io.github.diegourban.algorithms.datastructure.array;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class DynamicArrayTest {

    @SafeVarargs
    private static <T> DynamicArray<T> of(final T... values) {
        final DynamicArray<T> array = new DynamicArray<>();
        for (final T value : values) {
            array.append(value);
        }
        return array;
    }

    private static <T> List<T> contents(final DynamicArray<T> array) {
        final List<T> values = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            values.add(array.get(i));
        }
        return values;
    }

    @Test
    void startsEmpty() {
        final DynamicArray<Integer> array = new DynamicArray<>();

        assertThat(array.size()).isZero();
        assertThatExceptionOfType(IndexOutOfBoundsException.class).isThrownBy(() -> array.get(0));
    }

    @Test
    void appendsInOrder() {
        final DynamicArray<Integer> array = of(3, 1, 2);

        assertThat(array.size()).isEqualTo(3);
        assertThat(contents(array)).containsExactly(3, 1, 2);
    }

    @Test
    void holdsValuesOfAnyType() {
        final DynamicArray<String> array = of("a", "b");

        array.set(0, "z");
        array.append(null);

        assertThat(contents(array)).containsExactly("z", "b", null);
        assertThat(array.pop(1)).isEqualTo("b");
    }

    @Test
    void setReplacesTheValueAtAnIndex() {
        final DynamicArray<Integer> array = of(1, 2, 3);

        array.set(1, 20);

        assertThat(contents(array)).containsExactly(1, 20, 3);
    }

    @Test
    void popBackRemovesTheLastValue() {
        final DynamicArray<Integer> array = of(1, 2, 3);

        array.popBack();

        assertThat(contents(array)).containsExactly(1, 2);
    }

    @Test
    void popRemovesTheValueAtAnIndexShiftingLaterOnesAndReturnsIt() {
        final DynamicArray<Integer> array = of(1, 2, 3, 4);

        assertThat(array.pop(1)).isEqualTo(2);
        assertThat(contents(array)).containsExactly(1, 3, 4);

        assertThat(array.pop(2)).isEqualTo(4);
        assertThat(array.pop(0)).isEqualTo(1);
        assertThat(contents(array)).containsExactly(3);
    }

    @Test
    void keepsEveryValueWhileGrowingPastTheInitialCapacity() {
        final DynamicArray<Integer> array = new DynamicArray<>();
        for (int i = 0; i < 1000; i++) {
            array.append(i);
        }

        assertThat(array.size()).isEqualTo(1000);
        for (int i = 0; i < 1000; i++) {
            assertThat(array.get(i)).isEqualTo(i);
        }
    }

    @Test
    void keepsTheRemainingValuesWhileShrinking() {
        final DynamicArray<Integer> array = new DynamicArray<>();
        for (int i = 0; i < 1000; i++) {
            array.append(i);
        }

        while (array.size() > 3) {
            array.popBack();
        }

        assertThat(contents(array)).containsExactly(0, 1, 2);
        array.append(99);
        assertThat(contents(array)).containsExactly(0, 1, 2, 99);
    }

    @Test
    void canBeEmptiedAndReused() {
        final DynamicArray<Integer> array = of(1, 2);

        array.popBack();
        array.pop(0);
        assertThat(array.size()).isZero();

        array.append(7);
        assertThat(contents(array)).containsExactly(7);
    }

    @Test
    void rejectsIndexesOutsideTheStoredValues() {
        final DynamicArray<Integer> array = of(1, 2);

        assertThatExceptionOfType(IndexOutOfBoundsException.class).isThrownBy(() -> array.get(2));
        assertThatExceptionOfType(IndexOutOfBoundsException.class).isThrownBy(() -> array.get(-1));
        assertThatExceptionOfType(IndexOutOfBoundsException.class).isThrownBy(() -> array.set(2, 0));
        assertThatExceptionOfType(IndexOutOfBoundsException.class).isThrownBy(() -> array.set(-1, 0));
        assertThatExceptionOfType(IndexOutOfBoundsException.class).isThrownBy(() -> array.pop(2));
        assertThatExceptionOfType(IndexOutOfBoundsException.class).isThrownBy(() -> array.pop(-1));
        assertThat(contents(array)).containsExactly(1, 2);
    }

    @Test
    void rejectsPoppingFromAnEmptyArray() {
        final DynamicArray<Integer> array = new DynamicArray<>();

        assertThatExceptionOfType(IndexOutOfBoundsException.class).isThrownBy(array::popBack);
        assertThatExceptionOfType(IndexOutOfBoundsException.class).isThrownBy(() -> array.pop(0));
    }

    @Test
    void popBackFromAFullArray() {
        final DynamicArray<Integer> array = new DynamicArray<>();
        for (int i = 0; i < DynamicArray.INITIAL_CAPACITY; i++) {
            array.append(i);
        }

        array.popBack();

        assertThat(array.size()).isEqualTo(DynamicArray.INITIAL_CAPACITY - 1);
        assertThat(array.get(array.size() - 1)).isEqualTo(DynamicArray.INITIAL_CAPACITY - 2);
    }
}
