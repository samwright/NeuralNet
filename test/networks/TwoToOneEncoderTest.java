package networks;

import org.junit.Before;
import org.junit.Test;
import utility.ValueConverter;

import static org.junit.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: Sam Wright
 * Date: 04/12/2012
 * Time: 16:44
 */
public class TwoToOneEncoderTest {
    private TwoToOneEncoder net;

    @Before
    public void setUp() throws Exception {
        net = new TwoToOneEncoder();
    }

    @Test
    public void testTeachUsingOutput() throws Exception {
        boolean[] posible_outputs = new boolean[]{false, true};
        boolean[] desired_outputs;
        int loops_required;

        for (boolean i1 : posible_outputs) {
            for (boolean i2 : posible_outputs) {
                for (boolean i3 : posible_outputs) {
                    for (boolean i4 : posible_outputs) {
                        desired_outputs = new boolean[]{i1, i2, i3, i4};
                        loops_required = net.teachUsingOutput(desired_outputs, 0.1);

                        for (int i=0; i<4; ++i) {
                            assertEquals(desired_outputs[i], ValueConverter.getBoolean(0.1, net.evaluate(ValueConverter.getBipodal(net.getInputs()[i])))[0]);
                        }
                    }
                }
            }
        }
    }
}
