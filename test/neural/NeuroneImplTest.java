package neural;

import equation.Equation;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: Sam Wright
 * Date: 03/12/2012
 * Time: 12:45
 */
public class NeuroneImplTest {
    private Neurone n_in;
    private Neurone n, n_out;
    private Equation ident;
    private Link link_in, link_out;

    @Before
    public void setUp() throws Exception {
        ident = new Equation() {
            @Override
            public double evaluate(double x) {
                return x + 1;
            }

            @Override
            public double evaluateDiff(double x) {
                return 0;
            }
        };

        n_in = new NeuroneImpl();
        n = new NeuroneImpl(ident);
        n_out = new NeuroneImpl(ident);

        link_in = new LinkImpl(n_in, n);
        link_in.setWeight(0.5);
        link_out = new LinkImpl(n, n_out);
        link_out.setWeight(0.5);
    }

    @Test
    public void testGetInputLinks() throws Exception {
        List<Link> links = n.getInputLinks();
        assertEquals(1, links.size());
        assertEquals(link_in, links.get(0));

        links = n_out.getInputLinks();
        assertEquals(1, links.size());
        assertEquals(link_out, links.get(0));
    }

    @Test
    public void testGetOutputLinks() throws Exception {
        List<Link> links = n.getOutputLinks();
        assertEquals(1, links.size());
        assertEquals(link_out, links.get(0));

        links = n_in.getOutputLinks();
        assertEquals(1, links.size());
        assertEquals(link_in, links.get(0));
    }

    @Test
    public void testAddInputLink() throws Exception {
        Link extra_link = new LinkImpl(n_in, n);

        List<Link> links = n_in.getOutputLinks();
        assertEquals(2, links.size());
        assertTrue(links.contains(extra_link));

        links = n.getInputLinks();
        assertEquals(2, links.size());
        assertTrue(links.contains(extra_link));
    }

    @Test
    public void testAddOutputLink() throws Exception {
        Link extra_link = new LinkImpl(n, n_out);

        List<Link> links = n.getOutputLinks();
        assertEquals(2, links.size());
        assertTrue(links.contains(extra_link));

        links = n_out.getInputLinks();
        assertEquals(2, links.size());
        assertTrue(links.contains(extra_link));
    }

    @Test
    public void testRemoveLink() throws Exception {
        link_in.delete();
        assertEquals(0, n_in.getOutputLinks().size());
        assertEquals(0, n.getInputLinks().size());
    }

    @Test
    public void testFire() throws Exception {
        n_in.setValue(5.0);

        n.fire();
        assertEquals(2.5, n.getWeightedInput(), 0.01);
        assertEquals(3.5, n.getValue(), 0.01);

        n_out.fire();
        assertEquals(1.75, n_out.getWeightedInput(), 0.01);
        assertEquals(2.75, n_out.getValue(), 0.01);
    }

    @Test
    public void testGetValue() throws Exception {
        n_in.setValue(5.0);
        assertEquals(5.0, n_in.getValue(), 0.01);
    }

    @Test
    public void testGetWeightedInput() throws Exception {
        n_in.setValue(5.0);

        n.fire();
        assertEquals(2.5, n.getWeightedInput(), 0.01);
    }

    @Test
    public void testGetBias() throws Exception {
        assertEquals(0, n.getBias(), 0.01);
    }

    @Test
    public void testSetBias() throws Exception {
        n.setBias(0.5);
        assertEquals(0.5, n.getBias(), 0.01);
    }
}
