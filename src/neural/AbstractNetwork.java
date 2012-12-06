package neural;

import equation.Equation;

/**
 * Created with IntelliJ IDEA.
 * User: Sam Wright
 * Date: 02/12/2012
 * Time: 22:09
 */
public abstract class AbstractNetwork implements Network {
    private final Layer input_layer, output_layer;
    private final int input_size, output_size, total_layers;
    private final Equation transition_equation;

    public AbstractNetwork(Equation transition_equation, int... neurones) {
        total_layers = neurones.length;

        if (total_layers < 2)
            throw new RuntimeException("The network must have at least two layers.");

        this.transition_equation = transition_equation;

        input_layer = createLayer();
        input_size = neurones[0];

        for (int i=0; i<input_size; ++i) {
            input_layer.addNeurone(new NeuroneImpl());
        }

        Layer layer = input_layer;
        Layer previous_layer = layer;
        int layer_size = input_size;

        for (int i=1; i<total_layers; ++i) {
            layer = createLayer();
            layer_size = neurones[i];

            for (int j=0; j<layer_size; ++j) {
                layer.addNeurone(new NeuroneImpl(transition_equation));
            }
            layer.setPrev(previous_layer);
            previous_layer = layer;
        }

        output_layer = layer;
        output_size = layer_size;
    }

    @Override
    public Layer getInputLayer() {
        return input_layer;
    }

    @Override
    public Layer getOutputLayer() {
        return output_layer;
    }

    @Override
    public double[] evaluate(double[] input) {
        if (input.length != input_size)
            throw new RuntimeException("Input array and input neurones array are different sizes.");

        int i=0;
        for (Neurone neurone : input_layer.getNeurones()) {
            neurone.setValue(input[i++]);
        }

        Layer layer = input_layer.getNext();
        do {
            layer.fire();
            layer = layer.getNext();
        } while(layer != null);

        double[] output = new double[output_size];
        i = 0;
        for (Neurone neurone : output_layer.getNeurones()) {
            output[i++] = neurone.getValue();
        }

        return output;
    }

    @Override
    public Equation getTransitionEquation() {
        return transition_equation;
    }

    @Override
    public int getNumberOfLayers() {
        return total_layers;
    }
}
