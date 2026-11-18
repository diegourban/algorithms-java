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
 * Model-based check: any sequence of operations leaves {@link DynamicArray} holding exactly what a
 * {@link java.util.ArrayList} holds after the same operations. Indexes are generated as raw numbers and wrapped into
 * the valid range for the current size, so every operation is legal and long sequences grow and shrink the array
 * across several resizes.
 */
class DynamicArrayPropertyTest {

    sealed interface Operation permits Append, Set, PopBack, Pop {}

    record Append(int value) implements Operation {}

    record Set(int rawIndex, int value) implements Operation {}

    record PopBack() implements Operation {}

    record Pop(int rawIndex) implements Operation {}

    @Provide
    Arbitrary<List<Operation>> operationSequences() {
        return operations().list().ofMaxSize(200);
    }

    private static Arbitrary<Operation> operations() {
        final Arbitrary<Integer> values = Arbitraries.integers();
        final Arbitrary<Integer> rawIndexes = Arbitraries.integers().greaterOrEqual(0);
        return Arbitraries.frequencyOf(
                Tuple.of(5, values.map(Append::new)),
                Tuple.of(2, Combinators.combine(rawIndexes, values).as(Set::new)),
                Tuple.of(2, Arbitraries.just(new PopBack())),
                Tuple.of(2, rawIndexes.map(Pop::new)));
    }

    @Property
    void behavesLikeAnArrayList(@ForAll("operationSequences") final List<Operation> operations) {
        final DynamicArray<Integer> array = new DynamicArray<>();
        final List<Integer> model = new ArrayList<>();

        for (final Operation operation : operations) {
            apply(operation, array, model);

            assertThat(array.size()).isEqualTo(model.size());
            for (int i = 0; i < model.size(); i++) {
                assertThat(array.get(i))
                        .as("value at %d after %s", i, operation)
                        .isEqualTo(model.get(i));
            }
        }
    }

    private static void apply(final Operation operation, final DynamicArray<Integer> array, final List<Integer> model) {
        switch (operation) {
            case Append(final int value) -> {
                array.append(value);
                model.add(value);
            }
            case Set(final int rawIndex, final int value) -> {
                if (!model.isEmpty()) {
                    final int index = rawIndex % model.size();
                    array.set(index, value);
                    model.set(index, value);
                }
            }
            case PopBack() -> {
                if (!model.isEmpty()) {
                    array.popBack();
                    model.removeLast();
                }
            }
            case Pop(final int rawIndex) -> {
                if (!model.isEmpty()) {
                    final int index = rawIndex % model.size();
                    assertThat(array.pop(index)).isEqualTo(model.remove(index));
                }
            }
        }
    }
}
