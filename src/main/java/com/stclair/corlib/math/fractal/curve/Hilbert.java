package com.stclair.corlib.math.fractal.curve;

import com.stclair.corlib.geometry.Point;
import com.stclair.corlib.permutation.GrayCodeFunction;

import java.util.Arrays;

import static com.stclair.corlib.math.fractal.curve.Orientation2D.*;
import static com.stclair.corlib.validation.Validation.inRange;

/**
 *
 */
public class Hilbert {

    private static final int xPlus = Right.value - 1;
    private static final int yPlus = Up.value - 1;
    private static final int yMinus = Down.value - 1;
    private static final int xMinus = Left.value - 1;

    private final int[][] orientationMapping;

    private final int[][] grayCodeVariants;

    private final Orientation2D baseDirection;

    private final int dimension;

    public Hilbert() {
        this(Right, 2);
    }

    public Hilbert(Orientation2D baseDirection, int dimension) {

        inRange(dimension, 0, Integer.MAX_VALUE, "dimension");

        this.baseDirection = baseDirection;
        this.dimension = dimension;

        grayCodeVariants = generateGrayCodes(dimension);
        orientationMapping = generateOrientations(dimension);
    }

    public int[][] generateGrayCodes(int dimension) {
        // TODO: implement code to generate gray code variants based on coordinate degree (2d vs 3d vs...)
        // TODO: 3d should generate permutations of 3 bit gray codes, 4d should generate permutations of 4 bit gray codes...

        // Every valid transition either appears directly in the gray code
        // or in a bitwise permutation of each of the elements of the gray
        // code.  This is true because only single-bit transitions are
        // valid as inputs and because adjacent members of the gray
        // code always represent single bit transitions.
        //
        // The trick, then, is to find an efficient mechanism to select the
        // appropriate permutation for the desired transition
        //
        // Since invert is equivalent to reflection and the route chosen
        // is inconsequential, I've chosen rotation + single-bit inversion
        // (i.e. single-axis reflection) as the mechanism for selecting
        // the appropriate permutation.  This has the advantage of being
        // extremely easy to apply.
        //
        // The first step is select the bitwise rotation such that the
        // the axis of progress for the gray code matches the axis of
        // progress reflected between the start and end points.  Next,
        // reverse the sequence if the intended progress is reverse
        // (i.e. progress from 1 to 0 means reverse the sequence).
        // Finally, inverted any bits that remain non-zero between
        // the start point and the end point.

        // 000 -> 100 ==> 000 001 011 010 110 111 101 100 -> base code                      Away
        // 001 -> 101 ==> 001 000 010 011 111 110 100 101 -> base code bit0 inverted
        // 010 -> 110 ==> 010 011 001 000 100 101 111 110 -> base code bit1 inverted
        // 011 -> 111 ==> 011 010 000 001 101 100 110 111 -> base code bit0 & bit1 inverted

        // 000 -> 010 ==> 000 100 101 001 011 111 110 010 -> base code rotated rightx1      Right
        // 001 -> 011 ==> 001 101 100 000 010 110 111 011 -> base code rotated rightx1 + bit0 inverted
        // 100 -> 110 ==> 100 000 001 101 111 011 010 110 -> base code rotated rightx1 + bit2 inverted
        // 101 -> 111 ==> 101 001 000 100 110 010 011 111 -> base code rotated rightx1 + bit1 & bit2 inverted

        // 000 -> 001 ==> 000 010 110 100 101 111 011 001 -> base code rotated rightx2      Up
        // 010 -> 011 ==> 010 000 100 110 111 101 001 011 -> base code rotated rightx2 + bit1 inverted
        // 100 -> 101 ==> 100 110 010 000 001 011 111 101 -> base code rotated rightx2 + bit2 inverted
        // 110 -> 111 ==> 110 100 000 010 011 001 101 111 -> base code rotated rightx2 + bit1 & bit2 inverted


        // axis of progression = start ^ end
        // direction = end & axis of progression != 0
        // reflected = step ^ ~axis of progression

        // 00 -> 10 ==> 00 01 11 10     00 -> 01  up  01 -> 11  right  11 -> 10 down 10 -> 00 left
        // 01 -> 11 ==> 01 00 10 11

        // 00 -> 01 ==> 00 10 11 01
        // 10 -> 11 ==> 10 00 01 11

        int coordinateDirections = 2 * dimension;
        int positions = 1 << dimension;

        int mask = (1 << dimension) - 1;

        int[] baseCodes = new int[positions];

        for (int index = 0; index < baseCodes.length; index++)
            baseCodes[index] = (int) GrayCodeFunction.encode(index);

        int[][] codes = new int[coordinateDirections][];

        for (int orientation = 0; orientation < coordinateDirections; orientation++) {

            int signum = 1 - ((orientation & 1) << 1);

            int coordinate = (orientation >> 1) & mask;

            int rotateRightBitCount = (dimension - 1) - coordinate;

            if (coordinate == dimension - 1 && signum == 1) {
                codes[orientation] = baseCodes;
                continue;
            }

            codes[orientation] = Arrays.copyOf(baseCodes, baseCodes.length);

            for (int index = 0; index < positions; index++) {
                int value = (signum > 0) ? baseCodes[index] : (~baseCodes[index] & mask);

                codes[orientation][index] = ((value >> rotateRightBitCount) | (value << dimension - rotateRightBitCount)) & mask;
            }
        }

        return codes;
//        return new int[][] {
//                { 0, 1, 3, 2 }, // Right   ->  no reflection
//                { 0, 2, 3, 1 }, // Up      ->  x=y reflection
//                { 3, 1, 0, 2 }, // Down    ->  x=-y reflection
//                { 3, 2, 0, 1 }, // Left    ->  x=-y + x=y reflection (or simply y=-y & x=-x)
//
//                // Right means base Gray code
//                // Up means map X to Y and Y to X,       (single-bit rotation of base grey code)
//                // Down means map X to 1-Y and Y to 1-X, (single-bit rotation of bitwise inverse of base grey code)
//                // Left means map X to 1-X and Y to 1-Y  (bitwise inverse of base grey code)
//        };
    }

    public int[][] generateOrientations(int dimension) {

        // TODO: implement code to generate orientation variants based on coordinate degree (2d vs 3d vs...)

        return new int[][] {
                {xPlus, yPlus, yPlus, xMinus},  // up
                {xMinus, yMinus, yMinus, xPlus},// down
                {yPlus, xPlus, xPlus, yMinus},  // right
                {yMinus, xMinus, xMinus, yPlus},// left
        };
    }


    public long[] h(long longDistance, int degree, Orientation2D direction) {

        // TODO: update to handle more than two dimensions

        inRange(longDistance, 0, Long.MAX_VALUE, "longDistance");

        int totalBitCount = dimension * (degree + 1);

        longDistance &= (1 << totalBitCount) - 1;

        int orientation = direction.value - 1;

        long[] encodedCoords = new long[dimension];

        int bitMask = (1 << dimension) - 1;

        for (int generation = 0, remainingBits = totalBitCount - dimension; generation <= degree; generation++, remainingBits -= dimension) {

            int bits = (int) (longDistance >>> remainingBits) & bitMask;

            int result = grayCodeVariants[orientation][bits];

            for (int coordinate = dimension - 1; coordinate >= 0; coordinate--) {
                encodedCoords[coordinate] = (encodedCoords[coordinate] << 1) | (result & 1);
                result >>= 1;
            }

            orientation = orientationMapping[orientation][bits];
        }

        return encodedCoords;
    }

    public long distanceToLong(double distance, int degree) {

        inRange(distance, 0, 1, "distance");

        if (distance == 0)
            return 0;

        long longBits = Double.doubleToLongBits(distance);

        int longExp = (int) (longBits >>> 52) - 1023;

        long rawBits = (longBits & 0x000FFFFFFFFFFFFFL) | 0x0010000000000000L;

        int shiftCount = 50 - longExp - (degree << 1);

        return rawBits >> shiftCount;
    }

    public double[] DistanceToCoordinateArray(double distance, int degree) {

        inRange(distance, 0, 1, "distance");

        long longDistance = distanceToLong(distance, degree);

        double scale = 1d /(2 << degree);

        return Arrays.stream(h(longDistance, degree, Right))
                .mapToDouble(coord -> scale * coord)
                .toArray();
    }

    public Point<Double> distanceToPoint(double distance, int degree) {

        Double[] coords = Arrays.stream(DistanceToCoordinateArray(distance, degree))
                .boxed()
                .toArray(Double[]::new);

        return new Point<>(coords);
    }

    public double PointToDistance(Point<Double> point) {
        return 0;
    }



    // TODO: Each smaller instance of the second iteration of the primary pattern
    // TODO: (the degree 1 result) rests in a corner of its 4x4x... enclosure.
    // TODO: Further, each smaller instance must enter on a point that is in
    // TODO: line with the outgoing element of the instance next to it.
    // TODO: Finally, each smaller instance must exit on a point that is
    // TODO: different than the one that it came in on.

    // TODO: In two dimensions, we appear to have no freedom to choose a different
    // TODO: configuration.

    // TODO: In three dimensions, we have the option to choose between two different
    // TODO: entry/exit points but only in a very restricted set of locations.
    // TODO:
    // TODO: The initial entry point on the base cube is 000 and in the configuration
    // TODO: that proceeds to the right, it must advance in the AWAY direction (z+).
    // TODO: This requires that the initial cube enter at 000 and its only valid
    // TODO: configuration is to exit at 100.  This cube must be followed by an
    // TODO: AWAY spacer bringing us to 200.

    // TODO: Because of this, the next cube must enter at 200 and it must
    // TODO: exit UP from that point.  Its only valid configuration requires
    // TODO: that it exit at 201.  All other points are dictated by the fact that
    // TODO: it must rest in the analog of the 100 corner (AWAY, LEFT, DOWN).
    // TODO: Again, this cube must be followed by an UP spacer so that we
    // TODO: arrive at 202.

    // TODO: The next cube must enter at 202 and exit in the TOWARD direction.
    // TODO: This cube represents our first real choice.  We may choose
    // TODO: either a cube that advances RIGHT by entering at 202 and
    // TODO: exiting at 212 OR a cube that advances UP by entering at 202 and
    // TODO: exiting at 203.  In either case, we must follow this cube with
    // TODO: a TOWARD spacer so that it arrives at either 112 (if we selected
    // TODO: a cube advancing RIGHT) or at 103 (if we selected a cube advancing
    // TODO: UP).

    // TODO: If we previously selected the cube that advances RIGHT, we must enter
    // TODO: at 112 but we two orientation choices available.  We may proceed
    // TODO: UP by exiting at 113 or we may proceed TOWARD by exiting at 012.
    // TODO: If we instead selected the cube that advances UP, our only
    // TODO: valid configuration choice is to advance RIGHT by entering at
    // TODO: 103 and exiting at 113.   In any of these cases, we must next
    // TODO: supply a RIGHT spacer that advances us from 113 to 123 or from
    // TODO: 012 to 022.  Note that irrespective of the various choices we
    // TODO: might have made before, our options at this point always reduce
    // TODO: to one of only two possible ways forward.
    // TODO:
    // TODO: Now that we have reached this point, it is almost certain that
    // TODO: we have a few more opportunities to make choices but it is
    // TODO: likely that we will find a simpler solution if we choose
    // TODO: to simply make the rest of our choices symmetrical with those
    // TODO: that we have made so far.
    // TODO:
    // TODO: One relatively simple solution is simply to choose all cubes
    // TODO: (other than the first and last) so that their entry and exit
    // TODO: points face <b>inward</b> with respect to the enclosing
    // TODO: container.
    //



    // TODO: The general pattern for extending the degree 0 pattern from one
    // TODO: dimension to the next appears to be not only the Gray code
    // TODO: but also to take the pattern from the lower order dimension
    // TODO: and render it in the new dimension, adding a reflection step
    // TODO: at the end
    //
    // TODO: To illustrate:

    // TODO: in one dimension, the degree 0 pattern is a line:
    // TODO:      UP
    // TODO: and there are no further constructions possible

    // TODO: However, in two dimensions, we can extend this to create a
    // TODO: new degree 0 form by projecting into the added dimension (X)
    // TODO: and reflecting in the old (Y):
    // TODO:      UP (the original line), RIGHT (the projection), DOWN (the inverse of UP)

    //
    // TODO: in two dimensions the degree 0 pattern is:
    // TODO:   UP, RIGHT, DOWN
    //
    // TODO: in three dimensions, the default (Gray code-driven) degree 0
    // TODO: pattern is:
    // TODO:   UP, RIGHT, DOWN,    AWAY,    UP, LEFT, DOWN
    //
    // TODO: So, when constructing the degree 1 pattern for a given
    // TODO: dimension, it seems that we can consider simply applying the
    // TODO: appropriate rotation of the degree 0 pattern for the current
    // TODO: dimension to the degree 0 pattern for the lower-order
    // TODO: dimension, then rotate or reflect the final element
    // TODO: to ensure that it exits at the appropriate vertex, and
    // TODO: finally project it into the new dimension and reflect
    // TODO: all of the steps that preceded this projection.
    //
    // TODO: this suggests the following solution for degree 1 in three
    // TODO: dimensions:

    // TODO:    STEP 1:  Apply degree 0 pattern for 3d to degree 0 pattern for 2d
    // TODO:        UP         RIGHT         DOWN     becomes:
    // TODO:   [UP] UP [RIGHT] RIGHT [RIGHT] DOWN [DOWN]
    // TODO:    STEP 2:  Rotate final element of this initial sequence
    // TODO:     so that it is oriented in the correct direction
    // TODO:   [UP] UP [RIGHT] RIGHT [RIGHT] DOWN [DOWN]   becomes:
    // TODO:   [UP] UP [RIGHT] RIGHT [RIGHT] DOWN [AWAY]
    // TODO:    STEP 3: attach the appropriate spacer:
    // TODO:   [UP] UP [RIGHT] RIGHT [RIGHT] DOWN [AWAY]   becomes:
    // TODO:   [UP] UP [RIGHT] RIGHT [RIGHT] DOWN [AWAY] AWAY
    // TODO:    STEP 4:  Reflect each of the prior elements to produce the
    // TODO:     completed pattern:
    // TODO:
    // TODO:   [UP] UP [RIGHT] RIGHT [RIGHT] DOWN [AWAY] AWAY  becomes
    // TODO:
    // TODO:   [UP] UP [RIGHT] RIGHT [RIGHT] DOWN [AWAY] AWAY [TOWARD] UP [LEFT] LEFT [LEFT] DOWN [DOWN]






    // 000 100 101 001 011 111 110 010 -> rotate right                   Right

    // base unit is:
    //     right => away, up, toward, right, away, down, toward

    // replacement rules:
    //
    //     down replaces with:
    //          away, right, right, down, down, left, left, toward
    //
    //     up replaces with:
    //          toward, left, left, up, up, right, right, away
    //
    //     toward replaces with:
    //



    //
    //
    //
    //      xxxxxxxxxxxxxxxx
    //      x              x
    //      x              x
    //      x              x
    //      x              x
    //      x              x
    //      x              x
    //      x              x
    //
    //
    //
    //      xxxxxxxxx      xxxxxxxxx
    //      x       x      x       x
    //      x       x      x       x
    //      x       x      x       x
    //      x       xxxxxxxx       x
    //      x                      x
    //      x                      x
    //      xxxxxxxx        xxxxxxxx
    //             x        x
    //             x        x
    //             x        x
    //      xxxxxxxx        xxxxxxxx
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //


    // initial orientation is 000 (no reflection)

    // 000 100 101 001 011 111 110 010 -> Right

    // 000 -> 100 replaces with Away cube + away




    // initial orientation is 00
    // segment is 00 to 01
    // 00 -> 01  replaces with:   00 10 11 01 + up
    // segment is 01 to 11
    // orientation is 01
    // apply orientation: 01 => 11 maps to 00 => 10
    // 00 -> 10  replaces with:   00 01 11 10
    // orientation is 11
    // segment is 11 to 10
    // apply orientation: 11 => 10 maps to 00 => 01
    // 00 => 01 replaces with:    00 10 11 01


    // 00 -> 10  replaces with:   00 01 11 10 + right
    // remaining elements are a reflection of this sequence: right and then down






    // the orientation (reflection inward) is determined by the outer origin coordinates
    // ANDed with the outer destination coordinates.  For example, from 000 to 001, the
    // reflection bits are (000 AND 001) 000.  From 001 to 011, the reflection bits are
    // (001 AND 011) 001.  For 000, no bits are reflected, however, for 001, bit0 is
    // reflected so that any transition along bit0 is considered to be a <i>downward</i>
    // transition rather than an upward transition.
    //
    // Putting this into practice, a cube rendered between 000 and 001 is rendered with
    // no reflection because all directions other than along the axis can be validly
    // expressed by the Gray code as increments (or decrements following increments).
    // However, between 001 and 011, any change along the Y axis (indicated by bit0)
    // must be <i>reflected</i> to ensure that the transition is <i>downward</i> and,
    // therefore, remains inside the original cube (in particular, the Gray code can
    // be XORed with the reflection bits so that any transition from DOWN to UP,
    // instead, becomes a transition from UP to DOWN.
    //
    // It should be easy to demonstrate that the same logic for determining the reflection
    // in three dimensions can be reliably applied to any number of dimensions - from 2
    // to infinity.



    // TODO: this orientation information must be applied recursively.  It is easy to see the outer
    // TODO: orientation information but how do we determine the orientation for inner steps????
    // TODO: it seems that it would be necessary to know the step before as well as the current step...
    //
    // TODO: Hmmm...  It seems that if I know my current coordinates, it should suffice to determine
    // TODO: the reflection bits relative to my current position!!!
    //
    // TODO: Perhaps it suffices to use my current XYZ bits to express the reflection bits.
    // TODO: After all, if I am at any of these limits and the ordered movement demands further
    // TODO: increment along an already limited axis then that movement must be reflected, mustn't it?

    // TODO: Nope, that's close but not quite right.  Consider 2d case (UP, RIGHT, DOWN):
    // TODO: 2nd degree starts at 0,0 on a 4x4 square.
    // TODO: 1st step replaces 00-01 (UP) with (RIGHT, UP, LEFT) to advance from 0,0 to 0,1
    // TODO:    This is followed by an UP spacer to advance from 0,1 to 0,2
    // TODO:     (so far, this is consistent with reflection model above)
    // TODO: 2nd step replaces 01-11 (RIGHT) with (UP, RIGHT, DOWN) to <i>touch</i> 0,3 on its way to 1,2
    // TODO:    This is followed by a RIGHT spacer to advance from 1,2 to 2,2
    // TODO: 3rd step replaces  (UP, RIGHT, DOWN) to <i>touch</i> 3,3 on its way to 3,2
    // TODO:    This is followed by a DOWN spacer to advance from 3,2 to 3,1
    // TODO: 4th step generates (LEFT, DOWN, RIGHT) to advance from 3,2 to 3,0

    // AWAY:   000 001 011 010 110 111 101 100 (UP, RIGHT, DOWN, AWAY, UP, LEFT, DOWN)
    // RIGHT:  000 100 101 001 011 111 110 010 (AWAY, UP, TOWARD, RIGHT, AWAY, DOWN, TOWARD)
    // UP:     000 010 110 100 101 111 011 001 (RIGHT, AWAY, LEFT, UP, RIGHT, TOWARD, LEFT)
    // DOWN:   001 011 111 101 100 110 010 000 (RIGHT, AWAY, LEFT, DOWN, RIGHT, TOWARD, LEFT)
    // TOWARD: 100 101 111 110 010 011 001 000 (UP, RIGHT, DOWN, TOWARD, UP, LEFT, DOWN)


    // TODO: 3d case based on ZXY: 000 100 101 001 011 111 110 010
    // TODO:                      (AWAY, UP, TOWARD, RIGHT, AWAY, DOWN, TOWARD)
    // TODO: 2nd degree starts at 0,0,0 on 4x4x4 cube
    // TODO: 1st step inserts 000 001 011 010 110 111 101 100 (UP, RIGHT, DOWN, AWAY, UP, LEFT, DOWN) [AWAY]
    // TODO:     before 000-100 (AWAY) to progress from 000-100 and then from 100-200.
    // TODO: 2nd step inserts 000 010 110 100 101 111 011 001 (RIGHT, AWAY, LEFT, UP, RIGHT, TOWARD, LEFT) [UP]
    // TODO:     before 100-101 (UP) to touch 300 as it progresses from 200-201 and then from 201-202.
    // TODO: 3rd step inserts 000 010 110 100 101 111 011 001 (RIGHT, AWAY, LEFT, UP, RIGHT, TOWARD, LEFT) [UP]
    // TODO:     before 101-001 (TOWARD) to touch 303 as it progresses from 202-203 and then from 203 to 103

    // 000 001 011 010 110 111 101 100
    // 001 000 100 101 111 110 010 011

    // TODO: 4th step inserts 001 000 100 101 111 110 010 011 (AWAY, DOWN, TOWARD, RIGHT, AWAY, UP, TOWARD) [RIGHT, from AWAY rotated and reversed]
    // TODO:     before 001-011 (RIGHT) to touch 003 as it progresses from 103 to 113 and then from 113 to 123
    // TODO: 5th step inserts 001 101 100 000 010 110 111 011 (AWAY, UP, TOWARD, RIGHT, AWAY, DOWN, TOWARD) [RIGHT, from AWAY rotated and reversed]
    // TODO:     before 011-111 (AWAY) to touch 033 as it progresses from 123 to 133 and then from 133 to 233

    //
    // 010 110 111 101 100

    // TODO: 6th step inserts 011 001 000 100 101 111 110 010 (LEFT, AWAY, RIGHT, DOWN, LEFT, TOWARD, RIGHT) [DOWN, from AWAY rotated and reversed]
    // TODO:     before 111-110 (DOWN) to touch 333 as it progresses from 233 to 231 and then from 231 to 230


    // 000 001 011 010 110 111 101 100
    // 110 111 101 100 000 001 011 010

    // TODO: 7th step inserts 110 111 101 100 000 001 011 010 (UP, RIGHT, DOWN, TOWARD, UP, LEFT, DOWN) [TOWARD, from AWAY rotated]
    // TODO:     before 110-010 (TOWARD) to exit at 333 as it progresses from 230 to 130 and then from 231 to 230


    // 000 001 011 010 110 111 101 100   200
    // 200 210 310 300 301 311 211 201   202
    // 202 212 312 302 303 313 213 003   103
    // 103 203 202 102 112 212 213 113   123
    // 123 223 322 122 132 232 233 133   233
    // 233 223 323 333 332 322 222 232   231
    // 231 111 101 100 000 001 011 010
    // 110






    // Every valid transition either appears directly in the gray code
    // or in a bitwise permutation of each of the elements of the gray
    // code.  This is true because only single-bit transitions are
    // valid as inputs and because adjacent members of the gray
    // code always represent single bit transitions.
    //
    // The trick, then, is to find an efficient mechanism to select the
    // appropriate permutation for the desired transition
    //
    // Since invert is equivalent to reflection and the route chosen
    // is inconsequential, I've chosen rotation + single-bit inversion
    // (i.e. single-axis reflection) as the mechanism for selecting
    // the appropriate permutation.  This has the advantage of being
    // extremely easy to apply.
    //
    // The first step is select the bitwise rotation such that the
    // the axis of progress for the gray code matches the axis of
    // progress reflected between the start and end points.  Next,
    // reverse the sequence if the intended progress is reverse
    // (i.e. progress from 1 to 0 means reverse the sequence).
    // Finally, inverted any bits that remain non-zero between
    // the start point and the end point.

    // 000 -> 100 ==> 000 001 011 010 110 111 101 100 -> base code                      Away
    // 001 -> 101 ==> 001 000 010 011 111 110 100 101 -> base code bit0 inverted
    // 010 -> 110 ==> 010 011 001 000 100 101 111 110 -> base code bit1 inverted
    // 011 -> 111 ==> 011 010 000 001 101 100 110 111 -> base code bit0 & bit1 inverted

    // 000 -> 010 ==> 000 100 101 001 011 111 110 010 -> base code rotated rightx1      Right
    // 001 -> 011 ==> 001 101 100 000 010 110 111 011 -> base code rotated rightx1 + bit0 inverted
    // 100 -> 110 ==> 100 000 001 101 111 011 010 110 -> base code rotated rightx1 + bit2 inverted
    // 101 -> 111 ==> 101 001 000 100 110 010 011 111 -> base code rotated rightx1 + bit1 & bit2 inverted

    // 000 -> 001 ==> 000 010 110 100 101 111 011 001 -> base code rotated rightx2      Up
    // 010 -> 011 ==> 010 000 100 110 111 101 001 011 -> base code rotated rightx2 + bit1 inverted
    // 100 -> 101 ==> 100 110 010 000 001 011 111 101 -> base code rotated rightx2 + bit2 inverted
    // 110 -> 111 ==> 110 100 000 010 011 001 101 111 -> base code rotated rightx2 + bit1 & bit2 inverted


    // axis of progression = start ^ end
    // direction = end & axis of progression != 0
    // reflected = step ^ ~axis of progression

    // 00 -> 10 ==> 00 01 11 10     00 -> 01  up  01 -> 11  right  11 -> 10 down 10 -> 00 left
    // 01 -> 11 ==> 01 00 10 11

    // 00 -> 01 ==> 00 10 11 01
    // 10 -> 11 ==> 10 00 01 11




}






