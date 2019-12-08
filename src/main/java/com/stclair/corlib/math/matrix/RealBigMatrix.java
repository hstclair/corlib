package com.stclair.corlib.math.matrix;

import com.stclair.corlib.math.array.Array2D;
import com.stclair.corlib.math.array.Array2DConcrete;
import com.stclair.corlib.math.array.Indexor;
import com.stclair.corlib.math.util.ApfloatOperationStrategy;
import org.apfloat.Apfloat;

import java.util.function.Function;

/**
 * Created by hstclair on 4/17/17.
 */
public class RealBigMatrix {

    final Array2D<Apfloat> members;

    final int rows;
    final int columns;
    final long precision;
    final int order;

    public RealBigMatrix(double[][] members) {
        this(members, 10000);
    }

    public RealBigMatrix(Array2D<Apfloat> members) {
        this.members = members;
        rows = members.getHeight();
        columns = members.getWidth();
        order = Math.min(rows, columns);
        precision = precision(members);
    }

    public RealBigMatrix(double[][] members, long precision) {
        int columnCount = 0;
        this.precision = precision;
        this.members = new Array2DConcrete<>(members[0].length, members.length, apfloatIndexor -> new Apfloat(members[apfloatIndexor.getRow()][apfloatIndexor.getColumn()], precision));
        rows = members.length;
        columns = columnCount;
        order = Math.min(rows, columns);
    }

    private int columns(Apfloat[][] members) {
        int columns = 0;

        for (int row=0; row < members.length; row++) {
            columns = Math.max(columns, members[row].length);
        }

        return columns;
    }

    private long precision(Array2D<Apfloat> members) {

        long[] precisionArray = new long[] { 0 };

        members.traverse(indexor -> precisionArray[0] = Math.max(precisionArray[0], indexor.getValue().precision()));

        return precisionArray[0];
    }

    private Apfloat subtract(Apfloat[] subtrahend, Apfloat[] minuend, int column) {
        Apfloat subtrahendCoefficient = minuend[column];
        Apfloat minuendCoefficient = subtrahend[column];

        for (int index = 0; index < subtrahend.length; index++) {
            subtrahend[index] = subtrahend[index].multiply(subtrahendCoefficient).subtract(minuend[index].multiply(minuendCoefficient));
        }

        return subtrahendCoefficient;
    }

    public Apfloat determinant() {
        MatrixLUDecomposition decomposer = new MatrixLUDecomposition();

        LUMatrixResult<Apfloat> result = decomposer.computeUpperLower(this.members, new ApfloatOperationStrategy(), true);

        return result.determinant();
    }

    public RealBigMatrix minor(int mrow, int mcolumn) {

        Function<Indexor<Apfloat>, Apfloat> initializer = indexor -> {
            int currentRow = indexor.getRow();
            int currentColumn = indexor.getColumn();

            if (currentColumn >= mcolumn)
                currentColumn--;

            if (currentRow >= mrow)
                currentRow--;

            return members.get(currentColumn, currentRow);
        };

        Array2D<Apfloat> members = new Array2DConcrete<Apfloat>(this.members.getWidth() - 1, this.members.getHeight() - 1, initializer);

        return new RealBigMatrix(members);
    }

//    public RealBigMatrix minor(int mrow, int mcolumn) {
//        Apfloat[][] members = new Apfloat[rows-1][columns-1];
//
//        int row = 0;
//
//        for (int srcRow = 0; srcRow < rows; srcRow++) {
//
//            if (srcRow == mrow)
//                continue;
//
//            int col = 0;
//
//            for (int srcCol = 0; srcCol < columns; srcCol++) {
//
//                if (srcCol == mcolumn)
//                    continue;
//
//                members[row][col] = this.members[srcRow][srcCol];
//
//                col++;
//            }
//
//            row++;
//        }
//
//        return new RealBigMatrix(members);
//    }
}
