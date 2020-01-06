package com.stclair.corlib.math.matrix.determinant;

import com.stclair.corlib.math.matrix.Matrix;

public interface DeterminantSolver {

    <T> T determinant(Matrix<T> matrix);
}
