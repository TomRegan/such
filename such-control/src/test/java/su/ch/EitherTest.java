package su.ch;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

@SuppressWarnings("unchecked") class EitherTest {

    @Test void partitionLeftShouldInsertNothingForSingletonList() {

        Pair<List<Maybe<Either.Left<?, ?>>>, List<Maybe<Either.Right<?, ?>>>> partition =
                Either.partitionLeft(ImmutableList.of(Either.Left("I")));

        assertThat(partition.getLeft(), contains(Maybe.Just(Either.Left("II"))));
        assertThat(partition.getRight(), contains(Maybe.Nothing()));
    }

    @Test void partitionLeftShouldInsertNothings() {

        Pair<List<Maybe<Either.Left<?, ?>>>, List<Maybe<Either.Right<?, ?>>>> partition =
                Either.partitionLeft(ImmutableList.of(Either.Left("II"), Either.Right(3), Either.Right(5), Either.Left("VIII")));

        assertThat(partition.getLeft(), contains(Maybe.Just(Either.Left("II")), Maybe.Nothing(), Maybe.Just(Either.Left("VIII"))));
        assertThat(partition.getRight(), contains(Maybe.Nothing(), Maybe.Just(Either.Right(3)), Maybe.Just(Either.Right(5))));
    }

    @Test void partitionLeftShouldBalanceLeftAndRightRelations() {

        Pair<List<Maybe<Either.Left<?, ?>>>, List<Maybe<Either.Right<?, ?>>>> partition =
                Either.partitionLeft(ImmutableList.of(Either.Right(3), Either.Right(5), Either.Left("VIII")));

        assertThat(partition.getLeft(), contains(Maybe.Nothing(), Maybe.Just(Either.Left("VIII"))));
        assertThat(partition.getRight(), contains(Maybe.Just(Either.Right(3)), Maybe.Just(Either.Right(5))));
    }

    @Test void partitionRightShouldInsertNothingForSingletonList() {

        Pair<List<Maybe<Either.Left<?, ?>>>, List<Maybe<Either.Right<?, ?>>>> partition =
                Either.partitionLeft(ImmutableList.of(Either.Right(1)));

        assertThat(partition.getLeft(), contains(Maybe.Nothing()));
        assertThat(partition.getRight(), contains(Maybe.Just(Either.Right(1))));
    }

    @Test void partitionRightShouldInsertNothings() {

        Pair<List<Maybe<Either.Left<?, ?>>>, List<Maybe<Either.Right<?, ?>>>> partition =
                Either.partitionRight(ImmutableList.of(Either.Right(3), Either.Left("VIII"), Either.Right(5)));

        assertThat(partition.getLeft(), contains(Maybe.Nothing(), Maybe.Just(Either.Left("VIII"))));
        assertThat(partition.getRight(), contains(Maybe.Just(Either.Right(3)), Maybe.Just(Either.Right(5))));
    }

    @Test void partitionRightShouldBalanceLeftAndRightRelations() {

        Pair<List<Maybe<Either.Left<?, ?>>>, List<Maybe<Either.Right<?, ?>>>> partition =
                Either.partitionRight(ImmutableList.of(Either.Right(3), Either.Right(5), Either.Left("VIII")));

        assertThat(partition.getLeft(), contains(Maybe.Nothing(), Maybe.Nothing(), Maybe.Just(Either.Left("VIII"))));
        assertThat(partition.getRight(), contains(Maybe.Just(Either.Right(3)), Maybe.Just(Either.Right(5)), Maybe.Nothing()));
    }

    @Test void partitionShouldSeparateLeftAndRight() {

        Pair<List<Either.Left<?, ?>>, List<Either.Right<?, ?>>> partition =
                Either.partition(ImmutableList.of(Either.Right(3), Either.Left("VIII"), Either.Right(5)));

        assertThat(partition.getLeft(), contains(Either.Left("VIII")));
        assertThat(partition.getRight(), contains(Either.Right(3), Either.Right(5)));
    }

    @Test void leftsShouldReturnLefts() {

        Collection<Either.Left<?, ?>> lefts = Either.lefts(ImmutableList.of(Either.Right(3), Either.Left("VIII"), Either.Right(5)));

        assertThat(lefts, contains(Either.Left(8)));
    }

    @Test void rightsShouldReturnRights() {

        Collection<Either.Right<?, ?>> rights = Either.rights(ImmutableList.of(Either.Right(3), Either.Left("VIII"), Either.Right(5)));

        assertThat(rights, contains(Either.Right(3), Either.Right(5)));
    }
}
