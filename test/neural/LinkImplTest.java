package neural;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: Sam Wright
 * Date: 03/12/2012
 * Time: 14:47
 */
public class LinkImplTest {
    private Link link;
    private Neurone in, out;

    @Before
    public void setUp() throws Exception {
        in = new InputNeurone();
        out = new InputNeurone();

        link = new LinkImpl(in, out);
    }

    @Test
    public void testGetInput() throws Exception {
        assertEquals(in, link.getInput());
        assertEquals(1, in.getOutputLinks().size());
        assertEquals(link, in.getOutputLinks().get(0));
    }

    @Test
    public void testGetOutput() throws Exception {
        assertEquals(out, link.getOutput());
        assertEquals(1, out.getInputLinks().size());
        assertEquals(link, out.getInputLinks().get(0));
    }

    @Test
    public void testSetWeight() throws Exception {
        link.setWeight(0.5);
        assertEquals(0.5, link.getWeight(), 0.01);
    }

    @Test
    public void testGetWeight() throws Exception {
        assertEquals(0., link.getWeight(), 0.01);
    }

    @Test
    public void testDelete() throws Exception {
        link.delete();

        assertEquals(0, in.getOutputLinks().size());
        assertEquals(0, out.getInputLinks().size());
    }
}
