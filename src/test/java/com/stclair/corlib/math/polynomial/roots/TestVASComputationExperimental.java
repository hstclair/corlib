package com.stclair.corlib.math.polynomial.roots;

import com.stclair.corlib.math.RealMobiusTransformation;
import com.stclair.corlib.math.polynomial.Polynomial;
import com.stclair.corlib.math.polynomial.roots.VASComputationExperimental;
import com.stclair.corlib.math.polynomial.roots.VASOperation;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;


/**
 * @author hstclair
 * @since 3/30/16 5:58 PM
 */
@Ignore
public class TestVASComputationExperimental {

    // the problem is that this code is testing the wrong thing.  The VASComputation algorithm flatly rejects
    // this polynomial so that the Experimental version must do so as well.

    // TODO Update object to reject this polynomial.  Update test to expect polynomial to be rejected.
    // TODO Update object to reject polynomial without sign changes, as well.

    @Test
    public void evaluateReducesDegreeUntilNonzeroConstantThenReportsASingleZeroRoot() {

        Polynomial polynomial = Polynomial.of(new double[] { 0, 0, 0, 1 });

        VASComputationExperimental instance = new VASComputationExperimental(polynomial, RealMobiusTransformation.IDENTITY);

        List<VASOperation> result = instance.evaluate();

        assertEquals(2, result.size());

        assertEquals(VASComputationExperimental.class, result.get(0).getClass());

        VASComputationExperimental firstResult = (VASComputationExperimental) result.get(0);

        assertEquals(Polynomial.of(1), firstResult.polynomial);

        assertEquals(1, result.get(1).getResults().size());
        assertEquals(0, result.get(1).getResults().get(0).a, 0);
        assertEquals(0, result.get(1).getResults().get(0).b, 0);
    }
}
