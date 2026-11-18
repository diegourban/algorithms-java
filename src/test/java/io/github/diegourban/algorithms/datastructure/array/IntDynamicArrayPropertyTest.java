package io.github.diegourban.algorithms.datastructure.array;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Combinators;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;
import net.jqwik.api.Tuple;

/**
 * Model-based check: any sequence of operations leaves {@link IntDynamicArray} holding exactly what a
 * {@link java.util.ArrayList} holds after the same operations, and the capacity always respects the growth policy.
 * Indexes are generated as raw numbers and wrapped into the valid range for the current size, so every operation is
 * legal and long sequences grow and shrink the array across several resizes.
 */
class IntDynamicArrayPropertyTest {

    sealed interface Operation permits Add, Insert, Set, Remove {}

    record Add(int value) implements Operation {}

    record Insert(int rawIndex, int value) implements Operation {}

    record Set(int rawIndex, int value) implements Operation {}

    record Remove(int rawIndex) implements Operation {}

    @Provide
    Arbitrary<List<Operation>> operationSequences() {
        return operations().list().ofMaxSize(200);
    }

    private static Arbitrary<Operation> operations() {
        final Arbitrary<Integer> values = Arbitraries.integers();
        final Arbitrary<Integer> rawIndexes = Arbitraries.integers().greaterOrEqual(0);
        return Arbitraries.frequencyOf(
                Tuple.of(4, values.map(Add::new)),
                Tuple.of(2, Combinators.combine(rawIndexes, values).as(Insert::new)),
                Tuple.of(2, Combinators.combine(rawIndexes, values).as(Set::new)),
                Tuple.of(3, rawIndexes.map(Remove::new)));
    }

    @Property
    void behavesLikeAnArrayList(@ForAll("operationSequences") final List<Operation> operations) {
        final IntDynamicArray array = new IntDynamicArray();
        final List<Integer> model = new ArrayList<>();

        for (final Operation operation : operations) {
            apply(operation, array, model);

            assertThat(array.size()).isEqualTo(model.size());
            assertThat(array.toArray()).containsExactly(toInts(model));
            assertThat(array.capacity())
                    .isGreaterThanOrEqualTo(Math.max(array.size(), IntDynamicArray.INITIAL_CAPACITY));
        }
    }

    private static void apply(final Operation operation, final IntDynamicArray array, final List<Integer> model) {
        switch (operation) {
            case Add(final int value) -> {
                array.add(value);
                model.add(value);
            }
            case Insert(final int rawIndex, final int value) -> {
                final int index = rawIndex % (model.size() + 1);
                array.add(index, value);
                model.add(index, value);
            }
            case Set(final int rawIndex, final int value) -> {
                if (!model.isEmpty()) {
                    final int index = rawIndex % model.size();
                    assertThat(array.set(index, value)).isEqualTo(model.set(index, value));
                }
            }
            case Remove(final int rawIndex) -> {
                if (!model.isEmpty()) {
                    final int index = rawIndex % model.size();
                    assertThat(array.remove(index)).isEqualTo(model.remove(index));
                }
            }
        }
    }

    private static int[] toInts(final List<Integer> values) {
        return values.stream().mapToInt(Integer::intValue).toArray();
    }
}
