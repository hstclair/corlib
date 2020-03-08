package com.stclair.corlib.math.fractal.curve;

import com.stclair.corlib.geometry.Point;
import com.stclair.corlib.permutation.GrayCodeFunction;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.Test;

import static com.stclair.corlib.math.fractal.curve.Orientation2D.*;
import static com.stclair.corlib.validation.Validation.inRange;
import static org.junit.Assert.*;

public class HilbertTest {

    Orientation2D[] hDegree0 = {
            Up, Right, Down
    };
    Orientation2D[] hDegree1 = {
            Right, Up, Left,    Up,
            Up, Right, Down,    Right,
            Up, Right, Down,    Down,
            Left, Down, Right
    };
    Orientation2D[] hDegree2 = {
            Up, Right, Down,    Right,
            Right, Up, Left,    Up,
            Right, Up, Left,    Left,
            Down, Left, Up,     Up,
            Right, Up, Left,    Up,
            Up, Right, Down,    Right,
            Up, Right, Down,    Down,
            Left, Down, Right,  Right,
            Right, Up, Left,    Up,
            Up, Right, Down,    Right,
            Up, Right, Down,    Down,
            Left, Down, Right,  Down,
            Down, Left, Up,     Left,
            Left, Down, Right,  Down,
            Left, Down, Right,  Right,
            Up, Right, Down
    };
    Orientation2D[] hDegree3 = {
            Right, Up, Left,    Up,     // 0
            Up, Right, Down,    Right,  // 4
            Up, Right, Down,    Down,   // 8
            Left, Down, Right,  Right,  // 12
            Up, Right, Down,    Right,  // 16
            Right, Up, Left,    Up,     // 20
            Right, Up, Left,    Left,   // 24
            Down, Left, Up,     Up,     // 28
            Up, Right, Down,    Right,  // 32
            Right, Up, Left,    Up,     // 36
            Right, Up, Left,    Left,   // 40
            Down, Left, Up,     Left,   // 44
            Left, Down, Right,  Down,   // 48
            Down, Left, Up,     Left,   // 52
            Down, Left, Up,     Up,     // 56
            Right, Up, Left,    Up,     // 60
            Up, Right, Down,    Right,  // 64
            Right, Up, Left,    Up,     // 68
            Right, Up, Left,    Left,   // 72
            Down, Left, Up,     Up,     // 76
            Right, Up, Left,    Up,     // 80
            Up, Right, Down,    Right,  // 84
            Up, Right, Down,    Down,   // 88
            Left, Down, Right,  Right,  // 92
            Right, Up, Left,    Up,     // 96
            Up, Right, Down,    Right,  // 100
            Up, Right, Down,    Down,   // 104
            Left, Down, Right,  Down,   // 108
            Down, Left, Up,     Left,   // 112
            Left, Down, Right,  Down,   // 116
            Left, Down, Right,  Right,  // 120
            Up, Right, Down,    Right,  // 124
            Up, Right, Down,    Right,  // 128
            Right, Up, Left,    Up,     // 132
            Right, Up, Left,    Left,   // 136
            Down, Left, Up,     Up,     // 140
            Right, Up, Left,    Up,     // 144
            Up, Right, Down,    Right,  // 148
            Up, Right, Down,    Down,   // 152
            Left, Down, Right,  Right,  // 156
            Right, Up, Left,    Up,     // 160
            Up, Right, Down,    Right,  // 164
            Up, Right, Down,    Down,   // 168
            Left, Down, Right,  Down,   // 172
            Down, Left, Up,     Left,   // 176
            Left, Down, Right,  Down,   // 180
            Left, Down, Right,  Right,  // 184
            Up, Right, Down,    Down,   // 188
            Left, Down, Right,  Down,   // 192
            Down, Left, Up,     Left,   // 196
            Down, Left, Up,     Up,     // 200
            Right, Up, Left,    Left,   // 204
            Down, Left, Up,     Left,   // 208
            Left, Down, Right,  Down,   // 212
            Left, Down, Right,  Right,  // 216
            Up, Right, Down,    Down,   // 220
            Down, Left, Up,     Left,   // 224
            Left, Down, Right,  Down,   // 228
            Left, Down, Right,  Right,  // 232
            Up, Right, Down,    Right,  // 236
            Right, Up, Left,    Up,     // 240
            Up, Right, Down,    Right,  // 244
            Up, Right, Down,    Down,   // 248
            Left, Down, Right           // 252
    };

    public Orientation2D[][] preComputed = new Orientation2D[][] {
            hDegree0,
            hDegree1,
            hDegree2,
            hDegree3,
    };

    public Map<String, Frame> frames = new HashMap<>();

    public Point<Integer> moveInteger(Point<Integer> point, Orientation2D direction) {
        switch (direction) {
            case Up:
                return new Point<>(point.getX(), point.getY()+1);
                
            case Down:
                return new Point<>(point.getX(), point.getY()-1);
                
            case Right:
                return new Point<>(point.getX()+1, point.getY());

            case Left:
                return new Point<>(point.getX()-1, point.getY());
        }

        throw new IllegalArgumentException("Wrong direction");
    }

    public Point<Double> moveDouble(Point<Double> point, Orientation2D direction) {
        switch (direction) {
            case Up:
                return new Point<>(point.getX(), point.getY()+1);

            case Down:
                return new Point<>(point.getX(), point.getY()-1);

            case Right:
                return new Point<>(point.getX()+1, point.getY());

            case Left:
                return new Point<>(point.getX()-1, point.getY());
        }

        throw new IllegalArgumentException("Wrong direction");
    }


    Orientation2D[] expand2d(Orientation2D previous, Orientation2D next) {

        Orientation2D[] StepUp =    { Right, Up, Left };
        Orientation2D[] StepRight = { Up, Right, Down };
        Orientation2D[] StepDown =  { Left, Down, Right };
        Orientation2D[] StepLeft =  { Down, Left, Up, };

        if (previous == Up && next == Up)
            return StepUp;

        if (previous == Right && next == Up)
            return StepUp;

        if (previous == Up && next == Left)
            return StepUp;


        if (previous == Down && next == Down)
            return StepDown;

        if (previous == Down && next == Right)
            return StepDown;

        if (previous == Left && next == Down)
            return StepDown;


        if (previous == Right && next == Right)
            return StepRight;

        if (previous == Up && next == Right)
            return StepRight;

        if (previous == Right && next == Down)
            return StepRight;


        if (previous == Left && next == Left)
            return StepLeft;

        if (previous == Down && next == Left)
            return StepLeft;

        if (previous == Left && next == Up)
            return StepLeft;


        throw new RuntimeException();
    }

    Orientation2D[] expandAndReport(Orientation2D previous, Orientation2D next) {
        Orientation2D[] result = expand2d(previous, next);

        List<String> names = Arrays.stream(result)
                .map(Orientation2D::toString)
                .collect(Collectors.toList());

        return result;
    }

    Orientation2D[] expand2d(Orientation2D[] directions) {

        Orientation2D previous = directions[0] == Right ? Up : Right;

        List<Orientation2D> result = new ArrayList<>();


        for (Orientation2D current : directions) {
            result.addAll(Arrays.asList(expandAndReport(previous, current)));
            result.add(current);
            previous = current;
        }

        if (previous == Down)
            result.addAll(Arrays.asList(expand2d(Left, Down)));

        if (previous == Right)
            result.addAll(Arrays.asList(expand2d(Up, Right)));

        return result.toArray(new Orientation2D[0]);
    }

    public Orientation2D[] getDirections(int degree) {

        if (degree < preComputed.length)
            return preComputed[degree];

        Orientation2D[] current = preComputed[3];
        degree -= 3;

        while (degree-- != 0)
            current = expand2d(current);

        return current;
    }

    public void traverseAndValidateLongDistance(int degree) {

        Hilbert curve = new Hilbert();

        Orientation2D[] directions = getDirections(degree);

        Point<Integer> expected = new Point<>(0, 0);

        for (long distance = 0; distance <= directions.length; distance++) {

            if (distance > 0)
                expected = moveInteger(expected, directions[(int) distance - 1]);

            long[] actual = curve.h(distance, degree, Right);

            assertEquals((int) expected.getX(), (int) actual[0]);
            assertEquals((int) expected.getY(), (int) actual[1]);
        }
    }

    public void traverseAndValidateDoubleDistance(int degree) {

        Hilbert curve = new Hilbert();

        Orientation2D[] directions = getDirections(degree);

        Point<Double> expected = new Point<>(0d, 0d);

        double distance = 0;
        double increment = .99999d / directions.length;

        int edgeLength = 2 << degree;

        for (int index = 0; index <= directions.length; index++, distance += increment) {

            if (index > 0)
                expected = moveDouble(expected, directions[index - 1]);

            Point<Double> actual = curve.distanceToPoint(distance, degree);

            assertEquals(expected.getX(), actual.getX() * edgeLength, 0);
            assertEquals(expected.getY(), actual.getY() * edgeLength, 0);
        }
    }

    public void performHilbert3dTest(String testFrameName, String referenceFrameName, int degree, int maxValue) {

        Frame initialFrame = getFrame(testFrameName);

        Frame refFrame = getFrame(referenceFrameName);

        int[] transpositions = computeTranspositions(testFrameName, referenceFrameName);

        long reflections = computeReflections(testFrameName, referenceFrameName);

        int length = 1 << (3 * degree);

        long x0 = 0;
        long y0 = 0;
        long z0 = 0;

        long[] contributions = new long[degree];

        for (int index = 0; index < length; index++) {

            String[] frameNames = new String[degree];

            long[] frameCoords = getCoords(initialFrame, degree, index, contributions, frameNames);

            long[] refCoords = getCoords(refFrame, degree, index, contributions, null);

            refCoords = applyReflections(refCoords, reflections, maxValue);

            refCoords = applyTranspositions(refCoords, transpositions);

            long x = frameCoords[0];
            long y = frameCoords[1];
            long z = frameCoords[2];

            long xRef = refCoords[0];
            long yRef = refCoords[1];
            long zRef = refCoords[2];

            boolean referenceCheck = (x != xRef || y != yRef || z != zRef);

            boolean offsetCheck = index != 0 && (Math.abs(x - x0) + Math.abs(y - y0) + Math.abs(z - z0) != 1);

            boolean boundsCheck = (index == 0 || index == length - 1) && (x != 0 && x != maxValue || y!=0 && y!=maxValue || z!=0 && z!=maxValue);

            boolean invalidStartBracket = initialFrame.innerFrameNames[0].equals(initialFrame.innerFrameNames[1]);

            String errorMessage = (boundsCheck || invalidStartBracket || offsetCheck) ? "  <=== " : "";

            List<String> errorMessages = new LinkedList<>();

            if (invalidStartBracket)
                errorMessages.add("Invalid Start Bracket");

            if (boundsCheck)
                errorMessages.add("Bad Exit Coordinates");

            if (referenceCheck)
                errorMessages.add(String.format("coordinates (x=%d, y=%d, z=%d) diverge from reference values (xRef=%d, yRef=%d, zRef=%d)", x, y, z, xRef, yRef, zRef));

            if (offsetCheck)
                errorMessages.add(String.format("coordinates (x=%d, y=%d, z=%d) offset from last coordinates (x0=%d, y0=%d, z0=%d)", x, y, z, x0, y0, z0));

            errorMessage = errorMessage.concat(String.join(", ", errorMessages));

            String contributionsString = Arrays.stream(contributions)
                    .mapToObj(Long::toBinaryString)
                    .map(string -> String.format("%1$3s", string).replace(' ', '0'))
                    .collect(Collectors.joining(" "));

            String frameNamesString = String.join(" ", frameNames);

            if (errorMessages.size() != 0) {
                System.out.printf("innerFrame: %s contributions: %s  dist: %s (x:%s, y:%s, z:%s) %s\n", frameNamesString, contributionsString, index, x, y, z, errorMessage);
                fail(String.format("innerFrame: %s contributions: %s  dist: %s (x:%s, y:%s, z:%s) %s\n", frameNamesString, contributionsString, index, x, y, z, errorMessage));
            }

            x0 = x;
            y0 = y;
            z0 = z;
        }
    }

    public int[] computeTranspositions(String target, String source) {

        target = target.toUpperCase();
        source = source.toUpperCase();

        int[] transpositions = new int[target.length()];

        for (int index = 0; index < source.length(); index++)
            transpositions[index] = source.indexOf(target.charAt(index));

        return transpositions;
    }

    public long[] applyTranspositions(long[] coords, int[] transpositions) {

        long[] transposed = new long[transpositions.length];

        for (int index = 0; index < coords.length; index++)
            transposed[index] = coords[transpositions[index]];

        return transposed;
    }

    public long[] getCoords(Frame initialFrame, int degree, long distance, long[] contributions, String[] frameNames) {

        int x = 0;
        int y = 0;
        int z = 0;

        Frame frame = null;
        Frame nextFrame = initialFrame;

        for (int currentDegree = 0; currentDegree < degree; currentDegree++) {

            frame = nextFrame;

            if (frameNames != null)
                frameNames[currentDegree] = frame.name;

            x <<= 1;
            y <<= 1;
            z <<= 1;

            long currentIndex = (distance >> (3 * (degree - currentDegree - 1))) & 7;

            int position = frame.apply(currentIndex);

            contributions[currentDegree] = position;

            nextFrame = getFrame(frame.innerFrameNames[(int) currentIndex]);

            x |= position >> 2;
            y |= (position >> 1) & 1;
            z |= position & 1;
        }

        return new long[] { x, y, z };
    }

    public long computeReflections(String testFrameName, String sourceFrameName) {

        String testFrameNameUpper = testFrameName.toUpperCase();

        long result = 0;

        for (int index = 0; index < sourceFrameName.length(); index++) {
            char sourceFrameComponent = sourceFrameName.charAt(sourceFrameName.length() - 1 - index);

            int testFrameComponentPos = testFrameNameUpper.indexOf(Character.toUpperCase(sourceFrameComponent));

            char testFrameComponent = testFrameName.charAt(testFrameComponentPos);

            if (testFrameComponent != sourceFrameComponent)
                result |= 1L << index;
        }

        return result;
    }

    public long[] applyReflections(long[] coords, long reflections, long maxValue) {

        long[] reflected = Arrays.copyOf(coords, coords.length);

        for (int index = 0; index < reflected.length; index++) {
            if ((reflections & (1L << (coords.length - 1) - index)) != 0)
                reflected[index] = maxValue - reflected[index];
        }

        return reflected;
    }


    public Frame getFrame(String name) {

        if (! frames.containsKey(name))
            frames.put(name, new Frame(name));

        return frames.get(name);
    }






    @Test
    public void testExpandDegree0() {

        Orientation2D[] expected = hDegree1;

        Orientation2D[] actual = expand2d(hDegree0);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testExpandDegree1ToDegree2() {

        Orientation2D[] expected = hDegree2;

        Orientation2D[] actual = expand2d(hDegree1);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testExpandDegree2ToDegree3() {

        Orientation2D[] expected = hDegree3;

        Orientation2D[] actual = expand2d(hDegree2);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testDistanceToPointDegree0() {
        traverseAndValidateDoubleDistance(0);
    }

    @Test
    public void testDistanceToPointDegree1() {
        traverseAndValidateDoubleDistance(1);
    }

    @Test
    public void testDistanceToPointDegree2() {
        traverseAndValidateDoubleDistance(2);
    }

    @Test
    public void testDistanceToPointDegree3() {
        traverseAndValidateDoubleDistance(3);
    }

    @Test
    public void testDistanceToPointDegree4() {
        traverseAndValidateDoubleDistance(4);
    }

    @Test
    public void testDistanceToPointDegree5() {
        traverseAndValidateDoubleDistance(5);
    }

    @Test
    public void testDistanceToPointDegree6() {
        traverseAndValidateDoubleDistance(6);
    }

    @Test
    public void testDistanceToPointDegree7() {
        traverseAndValidateDoubleDistance(7);
    }

    @Test
    public void testHDegree0() {
        traverseAndValidateLongDistance(0);
    }

    @Test
    public void testHDegree1() {
        traverseAndValidateLongDistance(1);
    }

    @Test
    public void testHDegree2() {
        traverseAndValidateLongDistance(2);
    }

    @Test
    public void testHDegree3() {
        traverseAndValidateLongDistance(3);
    }

    @Test
    public void testHDegree4() {
        traverseAndValidateLongDistance(4);
    }

    @Test
    public void testHDegree5() {
        traverseAndValidateLongDistance(5);
    }

    @Test
    public void testHDegree6() {
        traverseAndValidateLongDistance(6);
    }

    @Test
    public void testHDegree7() {
        traverseAndValidateLongDistance(7);
    }

    @Test
    public void testHDegree8() {
        traverseAndValidateLongDistance(8);
    }

    @Test
    public void testFrameABCReturnsGrayCode() {

        Frame frame = new Frame("ABC");

        for (int index = 0; index < 8; index++) {

            int expected = index ^ (index >> 1);

            assertEquals(expected, frame.apply(index));
        }
    }

    @Test
    public void testFrameabcInvertsThreeLowestBits() {

        Frame frame = new Frame("abc");

        for (int index = 0; index < 8; index++) {

            int expected = (index ^ (index >> 1)) ^ 7;

            assertEquals(expected, frame.apply(index));
        }
    }

    @Test
    public void testFrameCBAExchangesBit2AndBit0() {

        Frame frame = new Frame("CBA");

        for (int index = 0; index < 8; index++) {

            int grayCode = index ^ (index >> 1);

            int expected = ((grayCode & 4) >> 2) | (grayCode & 2) | ((grayCode & 1) << 2);

            assertEquals(expected, frame.apply(index));
        }
    }

    @Test
    public void testFrameCBaExchangesBit2AndBit0ThenNegatesBit2() {

        Frame frame = new Frame("CBa");

        for (int index = 0; index < 8; index++) {
            int grayCode = index ^ (index >> 1);

            int expected = ((~grayCode & 4) >> 2) | (grayCode & 2) | (((grayCode) & 1) << 2);

            assertEquals(expected, frame.apply(index));
        }
    }

    @Test
    public void testInnerFrameNamesForABC() {

        Frame frame = new Frame("ABC");

        String[] expected = new String[] {
                "BCA",
                "BAC",
                "BAC",
                "Abc",
                "Abc",
                "baC",
                "baC",
                "bCa",
        };

        String[] actual = frame.innerFrameNames;

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testInnerFrameNamesForBCA() {

        Frame frame = new Frame("BCA");

        String[] expected = new String[] {
                "CAB",
                "ACB",
                "ACB",
                "bcA",
                "bcA",
                "aCb",
                "aCb",
                "Cab",
        };

        String[] actual = frame.innerFrameNames;

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testInnerFrameNamesForBAC() {

        Frame frame = new Frame("BAC");

        String[] expected = new String[] {
                "CBA",
                "ABC",
                "ABC",
                "bAc",
                "bAc",
                "abC",
                "abC",
                "Cba",
        };

        String[] actual = frame.innerFrameNames;

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testInnerFrameNamesForCAB() {

        Frame frame = new Frame("CAB");

        String[] expected = new String[] {
                "ABC",
                "CBA",
                "CBA",
                "cAb",
                "cAb",
                "Cba",
                "Cba",
                "abC",
        };

        String[] actual = frame.innerFrameNames;

        assertArrayEquals(expected, actual);
    }


    @Test
    public void testInnerFrameNamesForCBA() {

        Frame frame = new Frame("CBA");

        String[] expected = new String[] {
                "ACB",
                "CAB",
                "CAB",
                "cbA",
                "cbA",
                "Cab",
                "Cab",
                "aCb",
        };

        String[] actual = frame.innerFrameNames;

        assertArrayEquals(expected, actual);
    }


    @Test
    public void testInnerFrameNamesForACB() {

        Frame frame = new Frame("ACB");

        String[] expected = new String[] {
                "BAC",
                "BCA",
                "BCA",
                "Acb",
                "Acb",
                "bCa",
                "bCa",
                "baC",
        };

        String[] actual = frame.innerFrameNames;

        assertArrayEquals(expected, actual);
    }


    @Test
    public void testHilbert3dABCDegree1() {

        performHilbert3dTest("ABC", "CAB", 1, 1);
    }

    @Test
    public void testHilbert3dABCDegree2() {

        performHilbert3dTest("ABC", "CAB", 2, 3);
    }

    @Test
    public void testHilbert3dabCDegree1() {

        performHilbert3dTest("abC", "CAB", 1, 1);
    }

    @Test
    public void testHilbert3dabCDegree2() {

        performHilbert3dTest("abC", "CAB", 2, 3);
    }

    @Test
    public void testHilbert3daBcDegree2() {

        performHilbert3dTest("aBc", "CAB", 2, 3);
    }

    @Test
    public void testHilbert3dAbcDegree2() {

        performHilbert3dTest("Abc", "CAB", 2, 3);
    }

    @Test
    public void testHilbert3dACBDegree2() {

        performHilbert3dTest("ACB", "CAB", 2, 3);
    }

    @Test
    public void testHilbert3dacBDegree2() {

        performHilbert3dTest("acB", "CAB", 2, 3);
    }

    @Test
    public void testHilbert3daCbDegree2() {

        performHilbert3dTest("aCb", "CAB", 2, 3);
    }

    @Test
    public void testHilbert3dAcbDegree2() {

        performHilbert3dTest("Acb", "CAB", 2, 3);
    }

    @Test
    public void testHilbert3dBCADegree2() {

        performHilbert3dTest("BCA", "CAB", 2, 3);
    }

    @Test
    public void testHilbert3dbcADegree2() {

        performHilbert3dTest("bcA", "CAB", 2, 3);
    }

    @Test
    public void testHilbert3dbCaDegree2() {

        performHilbert3dTest("bCa", "CAB", 2, 3);
    }

    @Test
    public void testHilbert3dBcaDegree2() {

        performHilbert3dTest("Bca", "CAB", 2, 3);
    }

    @Test
    public void testHilbert3dBACDegree2() {

        performHilbert3dTest("BAC", "CAB", 2, 3);
    }

    @Test
    public void testHilbert3dbaCDegree2() {

        performHilbert3dTest("baC", "CAB", 2, 3);
    }

    @Test
    public void testHilbert3dbAcDegree2() {

        performHilbert3dTest("bAc", "CAB", 2, 3);
    }

    @Test
    public void testHilbert3dBacDegree2() {

        performHilbert3dTest("Bac", "CAB", 2, 3);
    }

    @Test
    public void testHilbert3dCABDegree1() {

        performHilbert3dTest("CAB", "CAB", 1, 1);
    }

    @Test
    public void testHilbert3dCABDegree2() {

        performHilbert3dTest("CAB", "CAB", 2, 3);
    }

    @Test
    public void testHilbert3dcaBDegree2() {

        performHilbert3dTest("caB", "CAB", 2, 3);
    }

    @Test
    public void testHilbert3dcAbDegree2() {

        performHilbert3dTest("cAb", "CAB", 2, 3);
    }

    @Test
    public void testHilbert3dCabDegree2() {

        performHilbert3dTest("Cab", "CAB", 2, 3);
    }

    @Test
    public void testHilbert3dCBADegree2() {

        performHilbert3dTest("CBA", "CAB", 2, 3);
    }

    @Test
    public void testHilbert3dcbADegree2() {

        performHilbert3dTest("cbA", "CAB", 2, 3);
    }

    @Test
    public void testHilbert3dcBaDegree2() {

        performHilbert3dTest("cBa", "CAB", 2, 3);
    }

    @Test
    public void testHilbert3dCbaDegree2() {

        performHilbert3dTest("Cba", "CAB", 2, 3);
    }

    public static class Frame {

        public static final String noninversted = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        public static final String inverted = noninversted.toLowerCase();

        public static final int inversionFlag = 1 << 30;

        public static final int inversionFlagMask = inversionFlag - 1;

        public final String name;

        public final int[] mappings;

        public final String[] innerFrameNames;

        public Frame(String name) {
            this.name = name;

            mappings = computeMappings(name);

            innerFrameNames = computeInnerFrameNamesRight(name);
        }

        public String[] computeInnerFrameNamesRight(String name) {

            String nonshifted = "ABCABCabcabc";
            String shifted = "BCABCAbcabca";
            String abc = "ABCabc";
            String abInverted = "abCABc";
            String bcInverted = "AbcaBC";
            String acExchanged = "CBAcba";

            String startFrame = applyCharacterSubstitution(name, nonshifted, shifted);

            String introFrame = applyCharacterSubstitution(startFrame, abc, acExchanged);    // preserve B

            String centerFrame = applyCharacterSubstitution(name, abc, bcInverted);          // preserve A

            String outroFrame = applyCharacterSubstitution(introFrame, abc, abInverted);     // preserve C

            String endFrame = applyCharacterSubstitution(startFrame, abc, abInverted);       // preserve C

            return new String[] {
                    startFrame,
                    introFrame,
                    introFrame,
                    centerFrame,
                    centerFrame,
                    outroFrame,
                    outroFrame,
                    endFrame
            };
        }

        public String applyCharacterSubstitution(String value, String originalChars, String substituteChars) {

            return IntStream.range(0, value.length())
                    .map(value::charAt)
                    .map(originalChars::indexOf)
                    .map(substituteChars::charAt)
                    .collect(StringBuilder::new, (a, b) -> a.append((char) b), StringBuilder::append)
                    .toString();
        }

        public int apply(long value) {

            long grayCode = value ^ (value >> 1);

            int register = 0;

            if (name.equals("CAB") && value != 0)
                register = 0;

            for (int index = 0; index < mappings.length; index++) {

                int mapping = mappings[index];

                int bitMask = mapping & inversionFlagMask;

                int inversion = Integer.signum(mapping & inversionFlag);

                int bit = Long.signum(grayCode & bitMask) ^ inversion;

                register <<= 1;
                register |= bit;
            }

            return register;
        }

        public int[] computeMappings(String name) {

            int index = 0;
            int[] mappings = new int[name.length()];

            for (char c : name.toCharArray()) {

                int pos = noninversted.indexOf(c);

                int mapping = 0;

                if (pos < 0) {
                    pos = inverted.indexOf(c);

                    if (pos < 0)
                        throw new IllegalArgumentException(String.format("bad character %c in parameter \"name\"", c));

                    mapping = inversionFlag;
                }

                mapping |= 1 << (name.length() - pos - 1);

                mappings[index++] = mapping;
            }

            return mappings;
        }
    }
}
