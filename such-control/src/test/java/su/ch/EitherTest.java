package su.ch;

import com.google.common.collect.ImmutableList;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.Collection;
import java.util.List;

import static su.ch.Either.Left;
import static su.ch.Either.Right;
import static su.ch.Either.lefts;
import static su.ch.Either.partition;
import static su.ch.Either.partitionLeft;
import static su.ch.Either.partitionRight;
import static su.ch.Either.rights;
import static su.ch.Maybe.Just;
import static su.ch.Maybe.Nothing;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

@SuppressWarnings("unchecked")
public class EitherTest {

    @Test
    public void partitionLeftShouldInsertNothingForSingletonList() throws Exception {

        Pair<List<Maybe<Either.Left<?, ?>>>, List<Maybe<Either.Right<?, ?>>>> partition =
                Either.partitionLeft(ImmutableList.of(Either.Left("I")));

        MatcherAssert.assertThat(partition.getLeft(), Matchers.contains(Maybe.Just(Either.Left("II"))));
        MatcherAssert.assertThat(partition.getRight(), Matchers.contains(Maybe.Nothing()));
    }

    @Test
    public void partitionLeftShouldInsertNothings() throws Exception {

        Pair<List<Maybe<Either.Left<?, ?>>>, List<Maybe<Either.Right<?, ?>>>> partition =
                Either.partitionLeft(ImmutableList.of(Either.Left("II"), Either.Right(3), Either.Right(5), Either.Left("VIII")));

        MatcherAssert.assertThat(partition.getLeft(), Matchers.contains(Maybe.Just(Either.Left("II")), Maybe.Nothing(), Maybe.Just(Either.Left("VIII"))));
        MatcherAssert.assertThat(partition.getRight(), Matchers.contains(Maybe.Nothing(), Maybe.Just(Either.Right(3)), Maybe.Just(Either.Right(5))));
    }

    @Test
    public void partitionLeftShouldBalanceLeftAndRightRelations() throws Exception {

        Pair<List<Maybe<Either.Left<?, ?>>>, List<Maybe<Either.Right<?, ?>>>> partition =
                Either.partitionLeft(ImmutableList.of(Either.Right(3), Either.Right(5), Either.Left("VIII")));

        MatcherAssert.assertThat(partition.getLeft(), Matchers.contains(Maybe.Nothing(), Maybe.Just(Either.Left("VIII"))));
        MatcherAssert.assertThat(partition.getRight(), Matchers.contains(Maybe.Just(Either.Right(3)), Maybe.Just(Either.Right(5))));
    }

    @Test
    public void partitionRightShouldInsertNothingForSingletonList() throws Exception {

        Pair<List<Maybe<Either.Left<?, ?>>>, List<Maybe<Either.Right<?, ?>>>> partition =
                Either.partitionLeft(ImmutableList.of(Either.Right(1)));

        MatcherAssert.assertThat(partition.getLeft(), Matchers.contains(Maybe.Nothing()));
        MatcherAssert.assertThat(partition.getRight(), Matchers.contains(Maybe.Just(Either.Right(1))));
    }

    @Test
    public void partitionRightShouldInsertNothings() throws Exception {

        Pair<List<Maybe<Either.Left<?, ?>>>, List<Maybe<Either.Right<?, ?>>>> partition =
                Either.partitionRight(ImmutableList.of(Either.Right(3), Either.Left("VIII"), Either.Right(5)));

        MatcherAssert.assertThat(partition.getLeft(), Matchers.contains(Maybe.Nothing(), Maybe.Just(Either.Left("VIII"))));
        MatcherAssert.assertThat(partition.getRight(), Matchers.contains(Maybe.Just(Either.Right(3)), Maybe.Just(Either.Right(5))));
    }


    @Test
    public void partitionRightShouldBalanceLeftAndRightRelations() throws Exception {

        Pair<List<Maybe<Either.Left<?, ?>>>, List<Maybe<Either.Right<?, ?>>>> partition =
                Either.partitionRight(ImmutableList.of(Either.Right(3), Either.Right(5), Either.Left("VIII")));

        MatcherAssert.assertThat(partition.getLeft(), Matchers.contains(Maybe.Nothing(), Maybe.Nothing(), Maybe.Just(Either.Left("VIII"))));
        MatcherAssert.assertThat(partition.getRight(), Matchers.contains(Maybe.Just(Either.Right(3)), Maybe.Just(Either.Right(5)), Maybe.Nothing()));
    }


    @Test
    public void partitionShouldSeparateLeftAndRight() throws Exception {

        Pair<List<Either.Left<?, ?>>, List<Either.Right<?, ?>>> partition =
                Either.partition(ImmutableList.of(Either.Right(3), Either.Left("VIII"), Either.Right(5)));

        MatcherAssert.assertThat(partition.getLeft(), Matchers.contains(Either.Left("VIII")));
        MatcherAssert.assertThat(partition.getRight(), Matchers.contains(Either.Right(3), Either.Right(5)));
    }


    @Test
    public void leftsShouldReturnLefts() throws Exception {


        Collection<Either.Left<?, ?>> lefts = Either.lefts(ImmutableList.of(Either.Right(3), Either.Left("VIII"), Either.Right(5)));

        MatcherAssert.assertThat(lefts, Matchers.contains(Either.Left(8)));
    }


    @Test
    public void rightsShouldReturnRights() throws Exception {

        Collection<Either.Right<?, ?>> rights = Either.rights(ImmutableList.of(Either.Right(3), Either.Left("VIII"), Either.Right(5)));

        MatcherAssert.assertThat(rights, Matchers.contains(Either.Right(3), Either.Right(5)));
    }
}
