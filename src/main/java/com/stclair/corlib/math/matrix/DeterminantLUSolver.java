package com.stclair.corlib.math.matrix;


/**
 * Created by hstclair on 4/22/17.
 */
public class DeterminantLUSolver {

    MatrixLUDecomposor decomposor = new MatrixLUDecomposor();

    <T> T solve(Matrix<T> matrix) {

        return decomposor.computeUpperLower(matrix).determinant();
    }
}
