package com.stclair.corlib.math.prime;

import com.stclair.corlib.math.util.LongOperationStrategy;
import com.stclair.corlib.math.util.Sequence;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class SquareSequenceTest {

    Sequence<Long> instance = new SquareSequence<>(new LongOperationStrategy());

    @Test
    public void getMemberZeroReturnsZero() {

        Sequence<Long> instance = new SquareSequence<>(new LongOperationStrategy());

        long expected = 0;

        long actual = instance.getMember(0);

        assertEquals(expected, actual);
    }

    @Test
    public void getMemberOneReturnsOne() {

        Sequence<Long> instance = new SquareSequence<>(new LongOperationStrategy());

        long expected = 1;

        long actual = instance.getMember(1);

        assertEquals(expected, actual);
    }

    @Test
    public void getMemberNineReturnsEightyOne() {

        Sequence<Long> instance = new SquareSequence<>(new LongOperationStrategy());

        long expected = 81;

        long actual = instance.getMember(9);

        assertEquals(expected, actual);
    }


    @Test
    public void nextMember() {

        Sequence<Long> instance = new SquareSequence<>(new LongOperationStrategy());

        long[] expected = { 0, 1, 4, 9, 16, 25, 36, 49, 64, 81, 100 };

        long[] actual = new long[expected.length];

        for (int index = 0; index < actual.length; index++)
            actual[index] = instance.nextMember();

        assertArrayEquals(expected, actual);
    }

    @Test
    public void prevMember() {
    }

    @Test
    public void getMember() {
    }

    @Test
    public void currentMember() {
    }

    @Test
    public void currentIndex() {
    }
}