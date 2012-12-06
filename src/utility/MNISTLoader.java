package utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sam Wright
 * Date: 05/12/2012
 * Time: 17:03
 */
public class MNISTLoader {
    private File file;
    private List<double[]> images = new LinkedList<double[]>();
    public static final int PIXELS_PER_IMAGE = 28*28;

    public MNISTLoader(String filename) {
        load(filename);
    }

    public void load(String filename) {
        file = new File(filename);

        // Check the filename was valid:
        if (!file.exists())
            throw new RuntimeException("File does not exist!");

        if (!file.isFile())
            throw new RuntimeException("File is not a file!");

        // Open an input stream from the file:
        FileInputStream in;
        try {
            in = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Couldn't read from source file...", e);
        }

        // Clear old images
        images.clear();

        // Read from the file:
        int character = 0, image_number = 0;
        double[] image;
        try {
            // Skip the first 16 bytes
            for (int i = 0; i < 16; ++i) {
                character = in.read();
            }

            // Read all images
            while (image_number < 1000) {
                // Read image
                image = new double[PIXELS_PER_IMAGE];
                for (int pixel = 0; pixel < PIXELS_PER_IMAGE; ++pixel) {
                    // Read next pixel
                    character = in.read();

                    // Read pixel (as boolean: true = black, false = white)
                    image[pixel] = convertByteToBipodal(character);

                }
                ++image_number;

                // Save image to list
                images.add(image);
            }
        } catch (IOException e) {
            throw new RuntimeException("IOException while reading file", e);
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                throw new RuntimeException("IOException while closing source file", e);
            }
        }
    }

    private double convertByteToBipodal(int byt) {
        return byt > 127 ? +1. : -1.;
    }

    public List<double[]> getImages() {
        return Collections.unmodifiableList(images);
    }

    public static String toString(double[] image) {
        StringBuilder sbuf = new StringBuilder();
        for (int col = 0; col < 28; ++col) {
            for (int row = 0; row < 28; ++row) {
                sbuf.append((image[col * 28 + row] > 0. ? "$ " : ". "));
            }
            sbuf.append('\n');
        }
        return sbuf.toString();
    }
}
