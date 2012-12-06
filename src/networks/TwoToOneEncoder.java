package networks;

import equation.Equation;
import equation.Sigmoid;
import neural.AbstractLayer;
import neural.AbstractNetwork;
import neural.Layer;
import techniques.BackPropagation;
import utility.ValueConverter;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created with IntelliJ IDEA.
 * User: Sam Wright
 * Date: 04/12/2012
 * Time: 16:24
 *
 * This learns any encoding which takes two binary inputs and returns one binary output,
 * eg. XOR.
 *
 */
public class TwoToOneEncoder extends AbstractNetwork {
    private BackPropagation backprop;
    private boolean[][] inputs = new boolean[][]{
            new boolean[]{false, false},
            new boolean[]{false, true},
            new boolean[]{true, false},
            new boolean[]{true, true}
    };

    public TwoToOneEncoder() {
        super(new Sigmoid(), 2, 3, 1);
        reset();
    }

    private void reset() {
        backprop = new BackPropagation(this);
    }

    @Override
    public Layer createLayer() {
        return new AbstractLayer() {
            @Override
            public boolean isLinkBetween(int layer_1_index, int layer_2_index) {
                return true;
            }

            @Override
            public double getInitialWeight(int layer_1_index, int layer_2_index) {
                return Math.abs(Math.random() * 2) - 1;
            }
        };
    }

    private double getError(boolean[] desired_output) {
        double total_error = 0;

        for (int i = 0; i < 4; ++i) {
            total_error = Math.max(total_error, Math.abs(ValueConverter.getBipodal(desired_output[i])[0] - evaluate(ValueConverter.getBipodal(inputs[i]))[0]));
        }

        return total_error;
    }

    private void printNetOutput() {
        double[] results = new double[4];
        for (int i = 0; i < 4; ++i) {
            results[i] = evaluate(ValueConverter.getBipodal(inputs[i]))[0];
        }

        System.out.println("results: " + Arrays.toString(results));
    }

    public int teachUsingOutput(boolean[] desired_output, double max_error) {
        assertEquals(4, desired_output.length);

        for (int i = 0; i < 100000; ++i) {
            for (int j = 0; j < 4; ++j) {
                backprop.teach(ValueConverter.getBipodal(inputs[j]), ValueConverter.getBipodal(desired_output[j]));
            }
            if (getError(desired_output) < max_error)
                return i;
        }
        return -1;
    }

    public boolean[][] getInputs() {
        return inputs;
    }
}
