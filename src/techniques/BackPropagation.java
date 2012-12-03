package techniques;

import neural.Layer;
import neural.Link;
import neural.Network;
import neural.Neurone;

/**
 * Created with IntelliJ IDEA.
 * User: Sam Wright
 * Date: 02/12/2012
 * Time: 20:06
 */
public class BackPropagation {

    private final Network net;
    private double[][] deltas;
    private double[][][] weight_changes;
    private double learning_rate = 0.55, momentum = 0.15;

    public BackPropagation(Network net, double learning_rate, double momentum) {
        this(net);
        this.learning_rate = learning_rate;
        this.momentum = momentum;
    }

    public BackPropagation(Network net) {
        this.net = net;
        deltas = new double[net.getNumberOfLayers()][];
        weight_changes = new double[net.getNumberOfLayers()][][];

        Layer layer = net.getOutputLayer();
        int layer_size, input_layer_size;

        for (int i = 0; i < net.getNumberOfLayers(); ++i) {
            //System.out.println("i = " + i + " , layer = " + layer);
            layer_size = layer.getNeurones().size();

            deltas[i] = new double[layer_size];

            if (layer != net.getInputLayer()) {
                input_layer_size = layer.getPrev().getNeurones().size();
                weight_changes[i] = new double[layer_size][];
                for (int j = 0; j < layer_size; ++j) {
                    weight_changes[i][j] = new double[input_layer_size];
                }
            }
            layer = layer.getPrev();
        }
    }

    public void teach(double[] input, double[] desired_output) {
        if (desired_output.length != net.getOutputLayer().getNeurones().size())
            throw new RuntimeException("Output list and output layer have different sizes.");

        double[] net_output = net.evaluate(input);

        double error_signal, weight_change, max_weight_change = 0, bias_change;

        Layer layer = net.getOutputLayer();
        int neurone_index, layer_index = 0, prev_index, link_index;

        do {
            //System.out.println("new layer");
            neurone_index = 0;
            for (Neurone neurone : layer.getNeurones()) {
                // Update deltas
                if (layer == net.getOutputLayer()) {
                    error_signal = desired_output[neurone_index] - net_output[neurone_index];
                } else {
                    error_signal = 0;
                    for (Link link : neurone.getOutputLinks()) {
                        prev_index = layer.getNext().getNeurones().indexOf(link.getOutput());
                        error_signal += link.getWeight() * deltas[layer_index - 1][prev_index];
                    }
                }

                deltas[layer_index][neurone_index] =  error_signal
                            * net.getTransitionEquation().evaluateDiff(neurone.getWeightedInput());

                // Update bias
                bias_change = learning_rate * deltas[layer_index][neurone_index];
                neurone.setBias(neurone.getBias() + bias_change);
                max_weight_change = Math.max(bias_change, max_weight_change);

                //System.out.println("error_signal = " + error_signal + " , delta = " + deltas[layer_index][neurone_index]);

                // Update weights
                link_index = 0;
                for (Link link : neurone.getInputLinks()) {
                    weight_change = learning_rate * deltas[layer_index][neurone_index] * link.getInput().getValue();
                    weight_change += momentum * weight_changes[layer_index][neurone_index][link_index];
                    link.setWeight(link.getWeight() + weight_change);

                    weight_changes[layer_index][neurone_index][link_index] = weight_change;
                    max_weight_change = Math.max(max_weight_change, weight_change);
                    //System.out.println("----- weight change = " + weight_change + " link input = " + link.getInput().getValue());

                    ++link_index;
                }
                ++neurone_index;
            }

            layer = layer.getPrev();
            ++layer_index;
        } while(layer != net.getInputLayer());
    }
}
