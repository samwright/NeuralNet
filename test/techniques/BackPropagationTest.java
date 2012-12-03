package techniques;

import equation.Sigmoid;
import neural.AbstractLayer;
import neural.AbstractNetwork;
import neural.Layer;
import neural.Network;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: Sam Wright
 * Date: 03/12/2012
 * Time: 04:01
 */
public class BackPropagationTest {
    private Network net;
    private BackPropagation backprop;
    private boolean[][] inputs = new boolean[][] {
                new boolean[]{false, false},
                new boolean[]{false, true},
                new boolean[]{true, false},
                new boolean[]{true, true}
    };


    @Before
    public void setUp() throws Exception {
        net = new AbstractNetwork(new Sigmoid(), 2, 3, 1) {
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
        };

        backprop = new BackPropagation(net);
    }

    private double[] getBipodal(boolean ...input) {
        double[] output = new double[input.length];
        for (int i=0; i<input.length; ++i) {
            output[i] = input[i]? 1 : -1;
        }
        return output;
    }

    private double getError(boolean[] desired_output) {
        double total_error = 0;

        for (int i=0; i<4; ++i) {
            total_error = Math.max(total_error, Math.abs(getBipodal(desired_output[i])[0] - net.evaluate(getBipodal(inputs[i]))[0]));
        }

        return total_error;
    }

    private void printNetOutput() {
        double[] results = new double[4];
        for (int i = 0; i < 4; ++i) {
            results[i] = net.evaluate(getBipodal(inputs[i]))[0];
        }

        System.out.println("results: " + Arrays.toString(results));
    }

    private void teachUsingOutput(boolean[] desired_output) {
        assertEquals(4, desired_output.length);


        for (int i = 0; i < 100000; ++i) {
            for (int j = 0; j < 4; ++j) {
                backprop.teach(getBipodal(inputs[j]), getBipodal(desired_output[j]));
            }
            if (getError(desired_output) < 1e-1) {
                System.out.format("In %d iterations, ", i);
                printNetOutput();
                return;
            }
        }
        System.out.println("We wanted: " + Arrays.toString(getBipodal(desired_output)) + "\nBut got: ");
        printNetOutput();
        fail("Net didn't learn the encoding");
    }

    @Test
    public void testTeachXOR() throws Exception {
        teachUsingOutput(new boolean[]{false, true, true, false});
    }

    @Test
    public void testOtherEncodings() {
        // For the inputs (false,false), (false,true), (true,false), (true,true) , these are the possible boolean outputs:
        boolean[] posible_outputs = new boolean[]{false, true};

        for (boolean i1 : posible_outputs) {
            for (boolean i2 : posible_outputs) {
                for (boolean i3 : posible_outputs) {
                    for (boolean i4 : posible_outputs) {
                        teachUsingOutput(new boolean[]{i1, i2, i3, i4});
                    }
                }
            }
        }

    }
}
