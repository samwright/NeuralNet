package neural;

import equation.Equation;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: Sam Wright
 * Date: 03/12/2012
 * Time: 14:05
 */
public class AbstractLayerTest {
    private Layer layer, input;
    private Neurone n1, n2, n3;
    private Neurone i1, i2;

    private class SimpleEquation implements Equation {
        @Override
        public double evaluate(double x) {
            return x + 1;
        }

        @Override
        public double evaluateDiff(double x) {
            return 0;
        }
    }

    private class SimpleLayer extends AbstractLayer {
        @Override
        public boolean isLinkBetween(int layer_1_index, int layer_2_index) {
            return true;
        }

        @Override
        public double getInitialWeight(int layer_1_index, int layer_2_index) {
            return 1;
        }
    }

    @Before
    public void setUp() throws Exception {
        layer = new SimpleLayer();

        n1 = new NeuroneImpl(new SimpleEquation());
        n2 = new NeuroneImpl(new SimpleEquation());
        n3 = new NeuroneImpl(new SimpleEquation());

        layer.addNeurone(n1);
        layer.addNeurone(n2);
        layer.addNeurone(n3);

        input = new SimpleLayer();

        i1 = new NeuroneImpl();
        i2 = new NeuroneImpl();

        i1.setValue(10.0);
        i2.setValue(20.0);

        input.addNeurone(i1);
        input.addNeurone(i2);
    }

    @Test
    public void testGetNeurones() throws Exception {
        assertEquals(3, layer.getNeurones().size());

        assertTrue(layer.getNeurones().contains(n1));
        assertTrue(layer.getNeurones().contains(n2));
        assertTrue(layer.getNeurones().contains(n3));

    }

    @Test
    public void testRemoveNeurone() throws Exception {
        layer.removeNeurone(n2);

        assertTrue(layer.getNeurones().contains(n1));
        assertFalse(layer.getNeurones().contains(n2));
        assertTrue(layer.getNeurones().contains(n3));
    }

    @Test
    public void testSetNext() throws Exception {
        input.setNext(layer);
        checkLinks();
    }

    @Test
    public void testGetNext() throws Exception {
        input.setNext(layer);
        assertEquals(layer, input.getNext());
    }

    @Test
    public void testSetPrev() throws Exception {
        layer.setPrev(input);
        checkLinks();
    }

    @Test
    public void testGetPrev() throws Exception {
        layer.setPrev(input);
        assertEquals(input, layer.getPrev());
    }

    @Test
    public void testFireAll() throws Exception {
        layer.setPrev(input);
        layer.fire();

        int i=0;
        for (Neurone n : layer.getNeurones()) {
            assertEquals(31., n.getWeightedInput(), 0.01);
            assertEquals(32., n.getValue(), 0.01);
        }
    }


    private void checkLinks() {
        Set<Neurone> expected_neurones = new HashSet<Neurone>();
        Set<Neurone> got_neurones = new HashSet<Neurone>();
        int expected_links, got_links;


        // From i1 to layer neurones
        expected_neurones.add(n1);
        expected_neurones.add(n2);
        expected_neurones.add(n3);

        expected_links = 3;
        got_links = 0;

        for (Link link : i1.getOutputLinks()) {
            got_neurones.add(link.getOutput());
            ++got_links;
        }

        assertEquals(expected_links, got_links);
        assertEquals(expected_neurones, got_neurones);

        // From i2 to layer neurones
        got_neurones.clear();

        got_links = 0;

        for (Link link : i1.getOutputLinks()) {
            got_neurones.add(link.getOutput());
            ++got_links;
        }

        assertEquals(expected_links, got_links);
        assertEquals(expected_neurones, got_neurones);

        // From n1 to input neurones
        got_neurones.clear();
        expected_neurones.clear();
        expected_neurones.add(i1);
        expected_neurones.add(i2);

        expected_links = 2;
        got_links = 0;

        for (Link link : n1.getInputLinks()) {
            got_neurones.add(link.getInput());
            ++got_links;
        }

        assertEquals(expected_links, got_links);
        assertEquals(expected_neurones, got_neurones);

        // From n2 to input neurones
        got_neurones.clear();

        got_links = 0;

        for (Link link : n2.getInputLinks()) {
            got_neurones.add(link.getInput());
            ++got_links;
        }

        assertEquals(expected_links, got_links);
        assertEquals(expected_neurones, got_neurones);

        // From n3 to input neurones
        got_neurones.clear();

        got_links = 0;

        for (Link link : n3.getInputLinks()) {
            got_neurones.add(link.getInput());
            ++got_links;
        }

        assertEquals(expected_links, got_links);
        assertEquals(expected_neurones, got_neurones);
    }
}
