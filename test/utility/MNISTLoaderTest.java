package utility;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: Sam Wright
 * Date: 05/12/2012
 * Time: 17:24
 */
public class MNISTLoaderTest {
    private MNISTLoader loader;

    @Before
    public void setUp() throws Exception {
        loader = new MNISTLoader("/Users/eatmuchpie/Downloads/train-images-idx3-ubyte");
    }

    @Test
    public void testImageSize() {
        assertEquals(28*28, loader.getImages().get(0).length);
    }

    @Test
    public void testPrintImage() {
        for (double[] image : loader.getImages()) {
            System.out.println(MNISTLoader.toString(image));
        }
    }
}
