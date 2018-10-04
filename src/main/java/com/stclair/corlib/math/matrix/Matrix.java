package com.stclair.corlib.math.matrix;


import com.stclair.corlib.math.util.OperationStrategy;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by hstclair on 4/22/17.
 */
public class Matrix<T> {

    private final T[][] members;

    final int rows;
    final int columns;
    final int order;
    final OperationStrategy<T> op;

    public Matrix(T[][] members, OperationStrategy<T> op) {
        this.op = op;
        this.members = members;
        rows = members.length;
        columns = columns(members);
        order = Math.min(rows, columns);
    }

    public Matrix(double[][] members, OperationStrategy<T> op) {
        int columnCount = 0;
        this.op = op;
        this.members = op.matrix(members.length, members[0].length);
        for (int rowIndex=0; rowIndex<members.length; rowIndex++) {
            for (int colIndex = 0; colIndex<members[rowIndex].length; colIndex++) {
                this.members[rowIndex][colIndex] = op.from(members[rowIndex][colIndex]);
            }
            columnCount = Math.max(columnCount, members[rowIndex].length);
        }
        rows = members.length;
        columns = columnCount;
        order = Math.min(rows, columns);
    }

    private int columns(T[][] members) {
        int columns = 0;

        for (T[] row : members) {
            columns = Math.max(columns, row.length);
        }

        return columns;
    }

    private T[][] clone(T[][] original) {
        T[][] clone= op.matrix(original.length, 0);

        int columns = columns(original);

        for (int row=0; row < original.length; row++) {
            clone[row] = op.array(columns);
            System.arraycopy(original[row], 0,clone[row],0, columns);
        }

        return clone;
    }

    T[][] cloneMembers() {
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
        return members[row][column];
    }

    public Matrix<T> minor(int mrow, int mcolumn) {
        T[][] members = op.matrix(rows-1, columns-1);

        int row = 0;

        for (int srcRow = 0; srcRow < rows; srcRow++) {

            if (srcRow == mrow)
                continue;

            int col = 0;

            for (int srcCol = 0; srcCol < columns; srcCol++) {

                if (srcCol == mcolumn)
                    continue;

                members[row][col] = this.members[srcRow][srcCol];

                col++;
            }

            row++;
        }

        return new Matrix<T>(members, op);
    }

    public static <T> Matrix<T> identity(int order, OperationStrategy<T> op) {

        return new Matrix<>(Matrix.identityArray(order, op), op);
    }


    public static <T> T[][] identityArray(int order, OperationStrategy<T> op) {

        T one = op.one();
        T zero = op.zero();

        return IntStream.range(0, order)
                .mapToObj( row ->
                        IntStream.range(0, order)
                                .mapToObj(column ->
                                        row == column ? one : zero
                                ).toArray(op::array)
                ).toArray(op::matrix);
    }

    @Override
    public String toString() {

        return IntStream.range(0, rows)
                .mapToObj( row ->
                        IntStream.range(0, columns)
                        .mapToObj(col -> members[row][col].toString())
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
                if (! members[row][col].equals(that.members[row][col]))
                    return false;
            }
        }

        return true;
    }
}
