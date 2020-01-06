package com.stclair.corlib.math.matrix.determinant;


import com.stclair.corlib.math.matrix.Matrix;
import com.stclair.corlib.math.matrix.MatrixLUDecomposor;

/**
 * Created by hstclair on 4/22/17.
 */
public class LUDeterminantSolver implements DeterminantSolver {

    MatrixLUDecomposor decomposor = new MatrixLUDecomposor();

    @Override
    public <T> T determinant(Matrix<T> matrix) {
        return decomposor.computeUpperLower(matrix).determinant();
    }
}
