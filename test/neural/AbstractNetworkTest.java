package neural;

import equation.Equation;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: Sam Wright
 * Date: 03/12/2012
 * Time: 14:52
 */
public class AbstractNetworkTest {
    private Network n;
    private Equation equation;

    @Before
    public void setUp() throws Exception {
        equation = new Equation() {
            @Override
            public double evaluate(double x) {
                return x + 1;
            }

            @Override
            public double evaluateDiff(double x) {
                return 0;
            }
        };
        n = new AbstractNetwork(equation, 4, 3, 2) {
            @Override
            public Layer createLayer() {
                return new AbstractLayer() {
                    @Override
                    public boolean isLinkBetween(int layer_1_index, int layer_2_index) {
                        return true;
                    }

                    @Override
                    public double getInitialWeight(int layer_1_index, int layer_2_index) {
                        return 1;
                    }
                };
            }
        };
    }

    @Test
    public void testGetInputLayer() throws Exception {
        assertEquals(n.getInputLayer(), n.getOutputLayer().getPrev().getPrev());
    }

    @Test
    public void testGetOutputLayer() throws Exception {
        Layer layer = n.getInputLayer();
        assertEquals(n.getOutputLayer(), n.getInputLayer().getNext().getNext());
    }

    @Test
    public void testEvaluate() throws Exception {
        assertArrayEquals(new double[]{1,2,3}, new double[]{1,2,3}, 0.01);

        assertArrayEquals(new double[]{38, 38}, n.evaluate(new double[]{1,2,3,4}), 0.01);
    }

    @Test
    public void testGetTransitionEquation() throws Exception {
        assertEquals(equation, n.getTransitionEquation());
    }

    @Test
    public void testGetNumberOfLayers() throws Exception {
        assertEquals(3, n.getNumberOfLayers());
    }
}
