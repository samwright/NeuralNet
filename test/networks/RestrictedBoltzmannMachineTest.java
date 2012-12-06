package networks;

import org.junit.Before;
import org.junit.Test;
import utility.MD5Checksum;
import utility.MNISTLoader;
import utility.ValueConverter;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.*;


/**
 * Created with IntelliJ IDEA.
 * User: Sam Wright
 * Date: 05/12/2012
 * Time: 17:59
 */
public class RestrictedBoltzmannMachineTest {
    private RestrictedBoltzmannMachine rbm;

    @Before
    public void setUp() throws Exception {
        rbm = new RestrictedBoltzmannMachine(MNISTLoader.PIXELS_PER_IMAGE, 400);
    }

    @Test
    public void testEvaluate() throws Exception {

    }

    @Test
    public void testTrain() throws Exception {
        MNISTLoader mnist = new MNISTLoader("/Users/eatmuchpie/Downloads/train-images-idx3-ubyte");
        rbm.train(mnist.getImages());
        rbm.saveToFile("train_test.bin");
    }

    @Test
    public void testSimilarHash() {
        MNISTLoader mnist = new MNISTLoader("/Users/eatmuchpie/Downloads/train-images-idx3-ubyte");
        rbm.readFromFile("train_test.bin");

        double[] image_hash = rbm.evaluate(mnist.getImages().get(2));
        double[] test_hash;
        double diff;
        List<Image> image_lst;
        List<Double> diff_lst;

        Map<Double,List<Image>> hash_differences = new HashMap<Double, List<Image>>();

        for (double[] image : mnist.getImages()) {
            diff = 0;
            test_hash = rbm.evaluate(image);
            for (int i = 0; i < 500; ++i) {
                diff += Math.abs(image_hash[i] - test_hash[i]);
            }
            if (hash_differences.get(diff) == null) {
                image_lst = new LinkedList<Image>();
                image_lst.add(new Image(image));
                hash_differences.put(diff, image_lst);
            } else {
                hash_differences.get(diff).add(new Image(image));
            }
        }

        diff_lst = new ArrayList<Double>(hash_differences.keySet());
        Collections.sort(diff_lst);
        for (double ordered_diff : diff_lst) {
            for (Image image : hash_differences.get(ordered_diff)) {
                System.out.println(MNISTLoader.toString(image.getImage()));
            }
        }

    }

    @Test
    public void testSaveAndOpenToFile() throws Exception {
        String filename1 = "test_save_file_1_(will_be_overwritten!)";
        rbm.saveToFile(filename1);
        RestrictedBoltzmannMachine rbm2 = new RestrictedBoltzmannMachine(MNISTLoader.PIXELS_PER_IMAGE, 400);
        rbm2.readFromFile(filename1);

        String filename2 = "test_save_file_2_(will_be_overwritten!)";
        rbm2.saveToFile(filename2);

        // Check files are equivalent
        String md5_1 = MD5Checksum.getMD5Checksum(filename1);
        String md5_2 = MD5Checksum.getMD5Checksum(filename2);

        assertEquals(md5_1, md5_2);
    }

    @Test
    public void testSaveIsStable() throws Exception {
        String filename1 = "test_save_file_1_(will_be_overwritten!)";
        rbm.saveToFile(filename1);

        String filename2 = "test_save_file_2_(will_be_overwritten!)";
        rbm.saveToFile(filename2);

        // Check files are equivalent
        String md5_1 = MD5Checksum.getMD5Checksum(filename1);
        String md5_2 = MD5Checksum.getMD5Checksum(filename2);

        assertEquals(md5_1, md5_2);
    }

    @Test
    public void testReloadIsStable() throws Exception {
        String filename1 = "test_save_file_1_(will_be_overwritten!)";
        rbm.saveToFile(filename1);

        rbm.readFromFile(filename1);

        String filename2 = "test_save_file_2_(will_be_overwritten!)";
        rbm.saveToFile(filename2);

        // Check files are equivalent
        String md5_1 = MD5Checksum.getMD5Checksum(filename1);
        String md5_2 = MD5Checksum.getMD5Checksum(filename2);

        assertEquals(md5_1, md5_2);
    }

    @Test
    public void testByteOutput() {
        byte[] bytes = new byte[8];
        double original = 0.12;
        ByteBuffer.wrap(bytes).putDouble(original);

        byte[] read_bytes = bytes;
        double copied = ByteBuffer.wrap(read_bytes).getDouble();

        System.out.println("original = " + Arrays.toString(bytes));
        System.out.println("copied   = " + Arrays.toString(read_bytes));

        assertEquals(original, copied, 0.001);
        assertArrayEquals(bytes, read_bytes);
    }
}

class Image {
    private final double[] image;

    public Image(double[] image) {
        this.image = image;
    }

    public double[] getImage() {
        return image;
    }
}