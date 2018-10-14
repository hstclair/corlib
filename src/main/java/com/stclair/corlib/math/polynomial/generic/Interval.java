package com.stclair.corlib.math.polynomial.generic;

import com.stclair.corlib.math.util.OperationStrategy;

/**
 * @author hstclair
 * @since 8/13/15 6:52 PM
 */
public class Interval<T> {

    public final OperationStrategy<T> op;

    public final T a;

    public final T b;

    public final boolean aClosed;

    public final boolean bClosed;

    public Interval(T a, OperationStrategy<T> op) { this(a, true, a, true, op); }

    public Interval(T a, T b, OperationStrategy<T> op) { this(a, true, b, true, op); }

    public Interval(T a, boolean aClosed, T b, boolean bClosed, OperationStrategy<T> op) {
        if (op.greaterThan(a, b)) {
            T tmp = a;
            a = b;
            b = tmp;
        }

        this.a = a;
        this.aClosed = aClosed;

        this.b = b;
        this.bClosed = bClosed;

        this.op = op;
    }

    public boolean contains(T x) {
        if (aClosed && x.equals(a))
            return true;

        if (bClosed && x.equals(b))
            return true;

        return op.lessThan(a, x) && op.lessThan(x, b);
    }

    public boolean isExactValue() {
        return (aClosed && bClosed && a.equals(b));
    }

    public T getExactValue() {
        if (! isExactValue())
            throw new IllegalStateException("Interval is not an exact value");

        return a;
    }

    @Override
    public boolean equals(Object o) {

        if (o == null) return false;

        if (this == o) return true;

        if (! (o instanceof Interval || a.getClass().isInstance(o)))
            return false;

        if (o instanceof Interval) {
            Interval<T> interval = (Interval<T>) o;

            if (! (a.equals(interval.a) && b.equals(interval.b))) return false;

            return aClosed == interval.aClosed && bClosed == interval.bClosed;
        }

        if (! isExactValue()) return false;

        return a.equals(o);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = a.hashCode();
        result = (int) (temp ^ (temp >>> 32));
        temp = b.hashCode();
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (aClosed ? 1 : 0);
        result = 31 * result + (bClosed ? 1 : 0);
        return result;
    }

    String inequality(boolean closed) {
        if (closed)
            return "<=";

        return "<";
    }

    @Override
    public String toString() {
        if (isExactValue())
            return String.format("exactly %s", a);

        return String.format("interval %s %s X %s %s", a, inequality(aClosed), inequality(bClosed), b);
    }
}
