package com.stclair.corlib.math.matrix;


import com.stclair.corlib.math.array.Array2D;
import com.stclair.corlib.math.array.Array2DConcrete;
import com.stclair.corlib.math.array.Indexor;
import com.stclair.corlib.math.util.OperationStrategy;

import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by hstclair on 4/22/17.
 */
public class Matrix<T> {

    Array2D<T> members;
//    private final T[][] members;

    final int rows;
    final int columns;
    final int order;
    final OperationStrategy<T> op;

    public Matrix(Array2D<T> members, OperationStrategy<T> op) {
        this.op = op;
        this.members = members;
        rows = members.getHeight();
        columns = members.getWidth();
        order = Math.min(rows, columns);
    }

    public Matrix(double[][] members, OperationStrategy<T> op) {
        this.op = op;
        this.members = new Array2DConcrete<T>(op, members[0].length, members.length, indexor -> op.from(members[indexor.getRow()][indexor.getColumn()]));
        rows = this.members.getHeight();
        columns = this.members.getWidth();
        order = Math.min(rows, columns);
    }

    private int columns(T[][] members) {
        int columns = 0;

        for (T[] row : members) {
            columns = Math.max(columns, row.length);
        }

        return columns;
    }

    private Array2D<T> clone(Array2D<T> original) {
        return op.matrix(original.getHeight(), original.getWidth());
    }

    Array2D<T> cloneMembers() {
        return clone(members);
    }

    private T subtract(T[] minuend, T[] subtrahend, int column) {
        T subtrahendCoefficient = subtrahend[column];
        T minuendCoefficient = minuend[column];

        for (int index = 0; index < minuend.length; index++) {
            minuend[index] = op.difference(op.product(minuend[index], subtrahendCoefficient), op.product(subtrahend[index], minuendCoefficient));
        }

        return subtrahendCoefficient;
    }

//    public Value<T> determinant() {
//        Value<T>[][] umembers=clone(this.members);
//        Value<T> determinant = factory.valueOfOne();
//        Value<T> coefficient = factory.valueOfOne();
//
//        for (int row=0; row < this.rows; row++) {
//            for (int col=0; col< this.columns; col++) {
//                if (col == row)
//                    break;
//
//                coefficient = coefficient.multiply(subtract(umembers[row], umembers[col], col));
//            }
//
//            determinant = determinant.multiply(umembers[row][row]);
//        }
//
//        return determinant.divide(coefficient);
//    }

    public T member(int row, int column) {
        return members.get(column, row);
    }

    public Matrix<T> minor(int mrow, int mcolumn) {

        Function<Indexor<T>, T> initializer = indexor -> {
            int currentRow = indexor.getRow();
            int currentColumn = indexor.getColumn();

            if (currentColumn >= mcolumn)
                currentColumn++;

            if (currentRow >= mrow)
                currentRow++;

            return members.get(currentColumn, currentRow);
        };

        Array2D<T> members = new Array2DConcrete<T>(this.op, this.members.getWidth() - 1, this.members.getHeight() - 1, initializer);

        return new Matrix<T>(members, op);
    }

    public static <T> Matrix<T> identity(int order, OperationStrategy<T> op) {

        return new Matrix<>(Matrix.identityArray(order, op), op);
    }


    public static <T> Array2D<T> identityArray(int order, OperationStrategy<T> op) {

        T one = op.one();
        T zero = op.zero();

        Function<Indexor<T>, T> initializer = indexor -> {
            int currentRow = indexor.getRow();
            int currentColumn = indexor.getColumn();
            T currentValue = zero;

            if (currentColumn == currentRow)
                currentValue = one;

            return currentValue;
        };

        return new Array2DConcrete<T>(op, order, order, initializer);
    }

    @Override
    public String toString() {

        return IntStream.range(0, rows)
                .mapToObj( row ->
                        IntStream.range(0, columns)
                        .mapToObj(col -> members.get(col, row).toString())
                        .collect(Collectors.joining(",", "| ", " |\n"))
                ).collect(Collectors.joining());
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null)
            return false;

        if (obj == this)
            return true;

        if (! (obj instanceof Matrix))
            return false;

        Matrix that = (Matrix) obj;

        if (that.order != order)
            return false;

        for (int row = 0; row < order; row ++) {
            for (int col = 0; col < order; col++) {
                if (! members.get(col, row).equals(that.members.get(col, row)))
                    return false;
            }
        }

        return true;
    }
}
