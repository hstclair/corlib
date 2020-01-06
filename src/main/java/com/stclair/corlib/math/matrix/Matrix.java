package com.stclair.corlib.math.matrix;


import com.stclair.corlib.math.array.Array2D;
import com.stclair.corlib.math.array.Array2DConcrete;
import com.stclair.corlib.math.array.Indexor;
import com.stclair.corlib.math.matrix.determinant.DeterminantSolver;
import com.stclair.corlib.math.matrix.determinant.LUDeterminantSolver;
import com.stclair.corlib.math.util.OperationStrategy;

import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Simple Matrix class
 */
public class Matrix<T> {

    /** 2D array representing elements of this matrix */
    private final Array2D<T> elements;

    private final DeterminantSolver determinantSolver = new LUDeterminantSolver();

    public Matrix(Array2D<T> elements) {
        this.elements = elements;
    }

    public Matrix(double[][] elements, OperationStrategy<T> op) {
        this.elements = new Array2DConcrete<T>(op, elements[0].length, elements.length, indexor -> op.from(elements[indexor.getRow()][indexor.getColumn()]));
    }

    public OperationStrategy<T> getOperationStrategy() {
        return elements.getOperationStrategy();
    }

    public int getRows() {
        return elements.getHeight();
    }

    public int getColumns() {
        return elements.getWidth();
    }

    public Array2D<T> getElements() {
        return elements;
    }

    public T member(int row, int column) {
        return elements.get(column, row);
    }

    public Matrix<T> minor(int mrow, int mcolumn) {

        Function<Indexor<T>, T> initializer = indexor -> {
            int currentRow = indexor.getRow();
            int currentColumn = indexor.getColumn();

            if (currentColumn >= mcolumn)
                currentColumn++;

            if (currentRow >= mrow)
                currentRow++;

            return elements.get(currentColumn, currentRow);
        };

        Array2D<T> members = new Array2DConcrete<T>(this.elements.getOperationStrategy(), this.elements.getWidth() - 1, this.elements.getHeight() - 1, initializer);

        return new Matrix<T>(members);
    }


    public T determinant() {
        return determinantSolver.determinant(this);
    }

    public static <T> Matrix<T> identity(int order, OperationStrategy<T> operationStrategy) {

        return new Matrix<>(Matrix.identityArray(order, operationStrategy));
    }


    public static <T> Array2D<T> identityArray(int order, OperationStrategy<T> operationStrategy) {

        T one = operationStrategy.one();
        T zero = operationStrategy.zero();

        Function<Indexor<T>, T> initializer = indexor -> {
            int currentRow = indexor.getRow();
            int currentColumn = indexor.getColumn();
            T currentValue = zero;

            if (currentColumn == currentRow)
                currentValue = one;

            return currentValue;
        };

        return new Array2DConcrete<T>(operationStrategy, order, order, initializer);
    }

    @Override
    public String toString() {

        return IntStream.range(0, elements.getHeight())
                .mapToObj( row ->
                        IntStream.range(0, elements.getWidth())
                        .mapToObj(col -> elements.get(col, row).toString())
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

        if (that.elements.getHeight() != this.elements.getHeight()
                || that.elements.getWidth() != this.elements.getWidth())
            return false;

        for (int row = 0; row < this.elements.getHeight(); row ++) {
            for (int col = 0; col < this.elements.getWidth(); col++) {
                if (! elements.get(col, row).equals(that.elements.get(col, row)))
                    return false;
            }
        }

        return true;
    }
}
