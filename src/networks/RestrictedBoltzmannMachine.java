package networks;

import equation.Sigmoid;
import neural.*;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Sam Wright
 * Date: 04/12/2012
 * Time: 17:13
 */
public class RestrictedBoltzmannMachine extends AbstractNetwork {
    private static final double learning_rate = 0.4;


    public RestrictedBoltzmannMachine(int... neurones) {
        super(new Sigmoid(), neurones);
    }

    @Override
    public Layer createLayer() {
        return new AbstractLayer() {
            private Random random = new Random();

            @Override
            public boolean isLinkBetween(int layer_1_index, int layer_2_index) {
                return true;
            }

            @Override
            public double getInitialWeight(int layer_1_index, int layer_2_index) {
                return random.nextGaussian() * 0.01;
            }
        };
    }

    /**
     * Gets the most-hidden layer's probability values for the given input.
     *
     * @param input Values for the visible layer.
     * @return Probability values for the lowest (most-hidden) layer.
     */
    public double[] evaluate(double[] input) {
        Layer visible = getInputLayer();
        Layer hidden = visible.getNext();

        while (hidden != null) {
            evaluate(input, visible, hidden);
            input = hidden.getValues();

            visible = hidden;
            hidden = visible.getNext();
        }

        return input;
    }

    private void evaluate(double[] input, Layer visible, Layer hidden) {
        int N = 2;

        // Set input
        visible.setValues(input);

        for (int n = 0; n < N; ++n) {
            // Calculate hidden probabilities
            hidden.fire();

            // Binary-sample the hidden values
            setAsBinarySample(hidden.getNeurones());

            // Reconstruct visible values
            visible.reverseFire();
        }
    }

    private void setAsBinarySample(List<Neurone> neurones) {
        for (Neurone neurone : neurones) {
            neurone.setValue(neurone.getValue() > Math.abs(Math.random() * 2) - 1 ? +1. : -1.);
        }
    }

    public void train(List<double[]> inputs) {
        Layer visible = getInputLayer();
        Layer hidden = visible.getNext();
        List<double[]> next_inputs;

        while (hidden != null) {
            next_inputs = new LinkedList<double[]>();

            train(inputs, visible, hidden);

            for (double[] input : inputs) {
                evaluate(input, visible, hidden);

                // If we want the next layer to be trained on the
                // binary-sampling of the hidden layer, then we do:
                // setAsBinarySample(hidden.getNeurones());
                // But let's try not doing that first...
                next_inputs.add(hidden.getValues());
            }

            inputs = next_inputs;

            visible = hidden;
            hidden = visible.getNext();
        }
    }

    private void train(List<double[]> inputs, Layer visible, Layer hidden) {
        int visible_neurone_index, hidden_neurone_index, link_index, N = 2;

        double[] visible_bias_change = new double[visible.getNeurones().size()];
        double[] hidden_bias_change = new double[hidden.getNeurones().size()];
        double[][] CD = new double[visible.getNeurones().size()][hidden.getNeurones().size()];

        for (int epoch = 0; epoch < 50; ++epoch) {
            System.out.println("Starting epoch " + epoch);
            for (double[] input : inputs) {
                visible.setValues(input);

                for (int n = 0; n < N; ++n) {
                    // Calculate hidden value probabilities
                    hidden.fire();

                    // If this is the first iteration (ie. hidden values are being
                    // taken directly from the input data)
                    if (n == 0) {
                        // Calculate the CD_positive values for link weights:
                        // (input (visible) binary value * hidden probability)
                        // NB. this is the only time the hidden probability is used.  From here on,
                        // it is always a stochastic binary value.

                        visible_neurone_index = 0;
                        for (Neurone visible_neurone : visible.getNeurones()) {
                            link_index = 0;
                            for (Link link : visible_neurone.getOutputLinks()) {
                                CD[visible_neurone_index][link_index] += input[visible_neurone_index] * link.getOutput().getValue();
                                ++link_index;
                            }
                            ++visible_neurone_index;
                        }
                        // Calculate the CD_positive values for visible neurone biases
                        visible.getValues(visible_bias_change);

                        // Calculate the CD_positive values for hidden neurone biases
                        hidden.getValues(hidden_bias_change);
                    }

                    // If this isn't the last iteration
                    if (n != N - 1) {
                        // Then use that probability to sample a binary value.
                        // "This information bottleneck acts as a strong regularizer" (Hilton).
                        setAsBinarySample(hidden.getNeurones());

                        // And reconstruct visible values as probabilities
                        visible.reverseFire();

                    } else {
                        // This is the last iteration, so we don't want to make another recreation.
                        // Also, as Hilton prescribes, we shouldn't binary-sample the hidden values
                        // as this pointlessly adds error (ie. why do a random walk if we are stood
                        // on the solution?).

                        // Calculate the CD_negative values (visible probability * hidden probability)
                        visible_neurone_index = 0;
                        for (Neurone visible_neurone : visible.getNeurones()) {
                            link_index = 0;
                            for (Link link : visible_neurone.getOutputLinks()) {
                                CD[visible_neurone_index][link_index] -= visible_neurone.getValue() * link.getOutput().getValue();
                                ++link_index;
                            }

                            // Subtract the CD_negative values for visible neurone biases
                            // (which are the probabilities for the visible neurones)
                            visible_bias_change[visible_neurone_index] -= visible_neurone.getValue();

                            ++visible_neurone_index;
                        }

                        // Subtract the CD_negative values for hidden neurone biases
                        // (which are the probabilities for the hidden neurones)
                        hidden_neurone_index = 0;
                        for (double hidden_neurone_prob : hidden.getValues()) {
                            hidden_bias_change[hidden_neurone_index++] -= hidden_neurone_prob;
                        }
                    }
                }

                visible_neurone_index = 0;
                for (Neurone visible_neurone : visible.getNeurones()) {
                    link_index = 0;
                    for (Link link : visible_neurone.getOutputLinks()) {
                        // Update the link weights
                        link.setWeight(link.getWeight() + learning_rate * CD[visible_neurone_index][link_index] / (N-1));
                        ++link_index;
                    }
                    // Update the visible neurone biases
                    visible_neurone.setBias(visible_neurone.getBias() + visible_bias_change[visible_neurone_index]);

                    ++visible_neurone_index;
                }

                // Update the hidden neurone biases
                hidden_neurone_index = 0;
                for (Neurone hidden_neurone : hidden.getNeurones()) {
                    hidden_neurone.setBias(hidden_neurone.getBias() + hidden_bias_change[hidden_neurone_index]);
                    ++hidden_neurone_index;
                }
            }
        }
    }

    public void saveToFile(String filename) {
        File file = new File(filename);
        if (file.exists())
            file.delete();

        FileOutputStream out;

        try {
            out = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Couldn't open destination file for writing...", e);
        }

        for(Layer layer = getInputLayer(); layer != null; layer = layer.getNext()) {
            for (Neurone neurone : layer.getNeurones()) {
                writeDouble(out, neurone.getBias());
                for (Link link : neurone.getOutputLinks()) {
                    writeDouble(out, link.getWeight());
                }
            }
        }

        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            throw new RuntimeException("Couldn't close file", e);
        }

    }

    public void readFromFile(String filename) {
        File file = new File(filename);

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

        for (Layer layer = getInputLayer(); layer != null; layer = layer.getNext()) {
            for (Neurone neurone : layer.getNeurones()) {
                neurone.setBias(readDouble(in));
                for (Link link : neurone.getOutputLinks()) {
                    link.setWeight(readDouble(in));
                }
            }
        }

        try {
            in.close();
        } catch (IOException e) {
            throw new RuntimeException("Couldn't close file", e);
        }
    }

    private static void writeDouble(FileOutputStream out, double value) {
        byte[] bytes = new byte[8];
        ByteBuffer.wrap(bytes).putDouble(value);

        try {
            out.write(bytes);
        } catch (IOException e) {
            try {
                out.close();
            } catch (IOException err) {
                throw new RuntimeException("Couldn't close file", err);
            }
            throw new RuntimeException("Couldn't write to file", e);
        }
    }

    private static double readDouble(FileInputStream in) {
        byte[] bytes = new byte[8];
        for (int i = 0; i < 8; ++i) {
            try {
                bytes[i] = (byte) in.read();
            } catch (IOException e) {
                throw new RuntimeException("Couldn't read double from file", e);
            }
        }

        return ByteBuffer.wrap(bytes).getDouble();
    }

}

class StochasticSigmoid extends Sigmoid {
    //private Random random = new Random();
    private static final double stdev = 0.2;

    @Override
    public double evaluate(double x) {

        //return super.evaluate(x) + random.nextGaussian() * stdev;
        return super.evaluate(x) > Math.abs(Math.random() * 2) - 1 ? +1. : -1.;
    }

    @Override
    public double evaluateDiff(double x) {
        return super.evaluateDiff(x);
    }
}